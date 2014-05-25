package com.github.jiboo.dwiinaar.bitmapmanager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

import com.github.jiboo.dwiinaar.bitmapmanager.utils.BitmapUtils;
import com.github.jiboo.dwiinaar.bitmapmanager.utils.IOUtils;
import com.github.jiboo.dwiinaar.bitmapmanager.utils.ReusableBufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Async BitmapFactory with memory and disk caching.
 */
public class BitmapCache {

    /**
     * Unique identifier for a bitmap.
     */
    public static abstract class Key {
        Context dContext;
        Bitmap.Config dPrefConfig = Bitmap.Config.ARGB_8888;
        int dSampleSize = 1;

        protected Key(Context context) {
            dContext = context;
        }

        abstract @NonNull
        InputStream getStream(@Nullable Options opts) throws IOException;
    }

    protected static class FileKey extends Key {
        protected File dFile;

        public FileKey(Context ctx, @NonNull File file) {
            super(ctx);
            dFile = file;
        }

        public @NonNull
        InputStream getStream(@Nullable Options opts) throws IOException {
            return new FileInputStream(dFile);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            return o != null && o instanceof FileKey && o.hashCode() == hashCode();
        }

        @Override
        public int hashCode() {
            int result = 43;

            result = 31 * result + dPrefConfig.ordinal();
            result = 31 * result + dSampleSize;
            result = 31 * result + dFile.hashCode();

            return result;
        }
    }

    protected static class ResKey extends Key {
        protected int dResID;

        public ResKey(@NonNull Context ctx, int resID) {
            super(ctx);
            dResID = resID;
        }

        public @NonNull
        InputStream getStream(@Nullable Options opts) throws IOException {
            return dContext.getResources().openRawResource(dResID);
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof ResKey && o.hashCode() == hashCode();
        }

        @Override
        public int hashCode() {
            int result = 45;

            result = 31 * result + dPrefConfig.ordinal();
            result = 31 * result + dSampleSize;
            result = 31 * result + dResID;

            return result;
        }
    }

    protected static class UrlKey extends Key {
        protected URL dUrl;

        public UrlKey(Context ctx, @NonNull URL url) {
            super(ctx);
            dUrl = url;
        }

        public @NonNull
        InputStream getStream(@Nullable Options opts) throws IOException {
            if(BitmapDiskCache.getInstance() != null && (opts != null && opts.extraInDiskCache)) // Device has sd card
                return new FileInputStream(BitmapDiskCache.getInstance().get(dUrl));
            else
                return dUrl.openStream();
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof UrlKey && o.hashCode() == hashCode();
        }

        @Override
        public int hashCode() {
            int result = 44;

            result = 31 * result + dPrefConfig.ordinal();
            result = 31 * result + dSampleSize;
            result = 31 * result + dUrl.hashCode();

            return result;
        }
    }

    public static class BitmapDiskCache extends LruCache<URL, File> {
        protected static BitmapDiskCache dInstance;

        public static @Nullable
        BitmapDiskCache getInstance() {
            return dInstance;
        }

        public static void initInstance(Context ctx) {
            if(ctx.getExternalCacheDir() != null)
                dInstance = new BitmapDiskCache(new File(ctx.getExternalCacheDir(), "BitmapDiskCache"));
        }

        protected File dDir;
        protected ThreadLocal<byte[]> dBuffer = new ThreadLocal<byte[]>() {
            @Override protected byte[] initialValue() {
                return new byte[BuildConfig.READ_BUFFER_SIZE];
            }
        };

        public BitmapDiskCache(@NonNull File cacheDir) {
            super(BuildConfig.DISK_CACHE_SIZE);
            dDir = cacheDir;
            if (!dDir.exists()) {
                dDir.mkdir();
            } else if (!dDir.isDirectory()) {
                dDir.delete();
                dDir.mkdir();
            }
            load();
        }

        private void load() {
            for (File cacheFile : dDir.listFiles()) {
                try {
                    put(new URL(URLDecoder.decode(cacheFile.getName(), "UTF-8")), cacheFile);
                } catch (Exception e) {
                    cacheFile.delete();
                }
            }
        }

        @Override
        protected File create(URL key) {
            File result = null;
            InputStream is = null;
            OutputStream os = null;
            try {
                final File cacheFile = new File(dDir, URLEncoder.encode(key.toString(), "UTF-8"));
                os = new FileOutputStream(cacheFile);
                is = key.openStream();
                IOUtils.copy(is, os, dBuffer.get());
                result = cacheFile;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
            return result;
        }

        @Override
        protected int sizeOf(URL key, File value) {
            return (int) value.length();
        }

        @Override
        protected void entryRemoved(boolean evicted, URL key, File oldValue, File newValue) {
            if (evicted && oldValue != null)
                oldValue.delete();
        }
    }

    protected static class AbsBitmapLruCache extends LruCache<Key, Bitmap> {
        public AbsBitmapLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected void entryRemoved(boolean evicted, Key key, Bitmap oldValue, Bitmap newValue) {
            notifyEvicted(key, evicted, oldValue, newValue);
            if(evicted && oldValue != null && !oldValue.isRecycled())
                BitmapBin.getInstance().offer(oldValue);
        }
    }

    protected static class BitmapLruCache10 extends AbsBitmapLruCache {
        public BitmapLruCache10(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(Key key, Bitmap value) {
            return value.getWidth() * value.getHeight() * BitmapUtils.getConfigByteSize(value.getConfig());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected static class BitmapLruCache12 extends AbsBitmapLruCache {
        public BitmapLruCache12(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(Key key, Bitmap value) {
            return value.getByteCount();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected static class BitmapLruCache19 extends AbsBitmapLruCache {
        public BitmapLruCache19(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(Key key, Bitmap value) {
            return value.getAllocationByteCount();
        }
    }

    protected static class DecodeJob implements Runnable {
        Key _key;
        Options _options;

        public DecodeJob(Key key, Options options) {
            _key = key;
            _options = options;
        }

        @Override
        public void run() {
            synchronized (sPendingJobs) {
                sPendingJobs.remove(_key);
            }

            _options.inSampleSize = _key.dSampleSize;
            _options.inPreferredConfig = _key.dPrefConfig;
            _options.mCancel = false;
            if(_options.inTempStorage == null)
                _options.inTempStorage = sDecodeBuffer.get();

            InputStream is = null;
            try {
                is = new ReusableBufferedInputStream(_key.getStream(_options), sReadBuffer.get());
                _options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, _options);

                if(!_options.mCancel && _options.outWidth != -1 && _options.outHeight != -1) {

                    if(Build.VERSION.SDK_INT >= 11) {
                        _options.inBitmap = BitmapBin.getInstance().claim(_options.outWidth, _options.outHeight, _key.dPrefConfig);

                        if(_key.dSampleSize != 1 && Build.VERSION.SDK_INT < 19)
                            throw new RuntimeException("Error, key has sample size, this API level don't support reuse with sample size");
                    }

                    is = new ReusableBufferedInputStream(_key.getStream(_options), sReadBuffer.get());
                    _options.inJustDecodeBounds = false;
                    final Bitmap decoded = BitmapFactory.decodeStream(is, null, _options);

                    if(!_options.mCancel) {
                        if (decoded == null)
                            notifyDecodingError(_key, new RuntimeException("decodeStream returned null"));
                        else {
                            notifyLoaded(_key, decoded);
                            sCache.put(_key, decoded);
                        }
                    }
                }
            }
            catch(Throwable e) {
                notifyDecodingError(_key, e);
            }
            finally {
                IOUtils.closeQuietly(is);
            }
        }
    }

    protected static final Map<Key, DecodeJob> sPendingJobs = new HashMap<>();
    protected static final Map<Key, List<Listener>> sListeners = new HashMap<>();
    protected static final BlockingQueue<Runnable> sJobPool = new LinkedBlockingQueue<>();
    protected static final ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(1, 6, 5, TimeUnit.SECONDS, sJobPool);

    protected static AbsBitmapLruCache sCache;
    static {
        final int size = BuildConfig.BITMAP_CACHE_SIZE;
        if(Build.VERSION.SDK_INT >= 19)
            sCache = new BitmapLruCache19(size);
        else if(Build.VERSION.SDK_INT >= 12)
            sCache = new BitmapLruCache12(size);
        else
            sCache = new BitmapLruCache10(size);
    }

    protected static final ThreadLocal<byte[]> sReadBuffer = new ThreadLocal<byte[]>() {
        @Override protected byte[] initialValue() {
            return new byte[BuildConfig.READ_BUFFER_SIZE];
        }
    };

    protected static final ThreadLocal<byte[]> sDecodeBuffer = new ThreadLocal<byte[]>() {
        @Override protected byte[] initialValue() {
            return new byte[BuildConfig.DECODE_BUFFER_SIZE];
        }
    };

    protected static void notifyLoaded(Key key, Bitmap value) {
        final List<Listener> listeners = sListeners.get(key);
        if(listeners != null) {
            for (Listener listener : listeners)
                listener.onBitmapLoaded(key, value);
        }
    }

    protected static void notifyDecodingError(Key key, Throwable value) {
        final List<Listener> listeners = sListeners.get(key);
        if(listeners != null) {
            for (Listener listener : listeners)
                listener.onBitmapDecodingError(key, value);
        }
    }

    protected static void notifyEvicted(Key key, boolean evicted, Bitmap oldValue, Bitmap newValue) {
        final List<Listener> listeners = sListeners.get(key);
        if(listeners != null) {
            for (Listener listener : listeners)
                listener.onBitmapEvicted(key, evicted, oldValue, newValue);
        }
    }

    /**
     * Used to notify when the decoding of the bitmap has finished.
     */
    public interface Listener {
        void onBitmapLoaded(Key key, Bitmap value);
        void onBitmapEvicted(Key key, boolean evicted, Bitmap oldValue, Bitmap newValue);
        void onBitmapDecodingError(Key key, Throwable error);
    }

    /**
     * BitmapFactory.Options with some extras.
     */
    public static class Options extends BitmapFactory.Options {
        boolean extraInDiskCache = true;
    }

    /**
     * @return a BitmapCacheKey computed from file
     */
    public static Key getKey(Context ctx, File file) {
        return new FileKey(ctx, file);
    }

    /**
     * @return a BitmapCacheKey computed from url
     */
    public static Key getKey(Context ctx, URL url) {
        return new UrlKey(ctx, url);
    }

    /**
     * @return a BitmapCacheKey computed from resId
     */
    public static Key getKey(Context ctx, int resId) {
        return new ResKey(ctx, resId);
    }

    /**
     * @return a BitmapCacheKey computed from uri, might return null.
     */
    /*public static @Nullable Key getKey(Context ctx, Uri uri) {
        final String scheme = uri.getScheme();
        if(scheme.equals(ContentResolver.SCHEME_ANDROID_RESOURCE)) {
            ContentResolver.
        }
        else if(scheme.equals(ContentResolver.SCHEME_FILE)) {
            return getKey(ctx, new File(uri.getPath()));
        }
        else if(scheme.equals("http")) {
            try {
                return getKey(ctx, new URL(uri.toString()));
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }*/

    /**
     * Subscribe listener for event on key.
     */
    public static void subscribe(final Key key, final Listener listener) {
        synchronized (sListeners) {
            final List<Listener> listeners = sListeners.get(key);
            if (listeners != null)
                listeners.add(listener);
            else {
                sListeners.put(key, new ArrayList<Listener>() {{
                    add(listener);
                }});
            }
        }
    }

    /**
     * Unsuscribe the listener for events on key.
     */
    public static void unsubscribe(final Key key, final Listener listener) {
        synchronized (sListeners) {
            final List<Listener> listeners = sListeners.get(key);
            if (listeners != null)
                listeners.remove(listener);
            if(listeners.isEmpty())
                sListeners.remove(key);
        }
    }

    /**
     * Starts the decoding of the bitmap.
    */
    public static void asyncDecode(final Key key, final Options options) {
        final Bitmap cached = sCache.get(key);
        if(cached != null) {
            notifyLoaded(key, cached);
        }
        else {
            sExecutor.execute(new DecodeJob(key, options));
        }
    }
}