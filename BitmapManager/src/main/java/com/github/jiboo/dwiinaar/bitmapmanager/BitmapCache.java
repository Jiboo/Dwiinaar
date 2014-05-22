package com.github.jiboo.dwiinaar.bitmapmanager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.github.jiboo.dwiinaar.bitmapmanager.utils.BitmapUtils;
import com.github.jiboo.dwiinaar.bitmapmanager.utils.ReusableBufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BitmapCache {

    public static abstract class BitmapCacheKey {
        Bitmap.Config dPrefConfig;
        int dSampleSize;

        abstract @NonNull
        InputStream getStream() throws IOException;
    }

    protected static class FileBitmapCacheKey extends BitmapCacheKey {
        protected File dFile;

        public FileBitmapCacheKey(@NonNull File file) {
            dFile = file;
        }

        public @NonNull
        InputStream getStream() throws IOException {
            return new ReusableBufferedInputStream(new FileInputStream(dFile), sReadBuffer.get());
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof FileBitmapCacheKey && o.hashCode() == hashCode();
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

    protected static class UrlBitmapCacheKey extends BitmapCacheKey {
        protected URL dUrl;

        public UrlBitmapCacheKey(@NonNull URL url) {
            dUrl = url;
        }

        public @NonNull
        InputStream getStream() throws IOException {
            return new ReusableBufferedInputStream(dUrl.openStream(), sReadBuffer.get());
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof UrlBitmapCacheKey && o.hashCode() == hashCode();
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

    protected static class ResBitmapCacheKey extends BitmapCacheKey {
        protected int dResID;
        protected Context dCtx;

        public ResBitmapCacheKey(@NonNull Context ctx, int resID) {
            dCtx = ctx;
            dResID = resID;
        }

        public @NonNull
        InputStream getStream() throws IOException {
            return new ReusableBufferedInputStream(dCtx.getResources().openRawResource(dResID), sReadBuffer.get());
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof ResBitmapCacheKey && o.hashCode() == hashCode();
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

    public static BitmapCacheKey getKey(File file) {
        return new FileBitmapCacheKey(file);
    }

    public static BitmapCacheKey getKey(URL url) {
        return new UrlBitmapCacheKey(url);
    }

    public static BitmapCacheKey getKey(Context ctx, int resId) {
        return new ResBitmapCacheKey(ctx, resId);
    }

    protected static class AbsBitmapLruCache extends LruCache<BitmapCacheKey, Bitmap> {
        public AbsBitmapLruCache(int maxSize) {
            super(maxSize);
        }
    }

    protected static class BitmapLruCache10 extends AbsBitmapLruCache {
        public BitmapLruCache10(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(BitmapCacheKey key, Bitmap value) {
            return value.getWidth() * value.getHeight() * BitmapUtils.getConfigByteSize(value.getConfig());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected static class BitmapLruCache12 extends AbsBitmapLruCache {
        public BitmapLruCache12(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(BitmapCacheKey key, Bitmap value) {
            return value.getByteCount();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected static class BitmapLruCache19 extends AbsBitmapLruCache {
        public BitmapLruCache19(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(BitmapCacheKey key, Bitmap value) {
            return value.getAllocationByteCount();
        }
    }

    protected static AbsBitmapLruCache dCache;
    static {
        final int size = BuildConfig.BITMAP_CACHE_SIZE;
        if(Build.VERSION.SDK_INT >= 19)
            dCache = new BitmapLruCache19(size);
        else if(Build.VERSION.SDK_INT >= 12)
            dCache = new BitmapLruCache12(size);
        else
            dCache = new BitmapLruCache10(size);
    }

    protected static final ThreadLocal<byte[]> sReadBuffer = new ThreadLocal<byte[]>() {
        @Override protected byte[] initialValue() {
            return new byte[BuildConfig.READ_BUFFER_SIZE];
        }
    };

    public interface BitmapCacheListener {
        void onBitmapLoaded(BitmapCacheKey key, Bitmap value);
        void onBitmapDecodingError(BitmapCacheKey key, Throwable error);
    }

    public static void decode(final BitmapCacheKey key, final BitmapCacheListener listener) {

    }
}
