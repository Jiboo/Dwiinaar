package com.github.jiboo.dwiinaar.bitmapmanager;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.jiboo.dwiinaar.bitmapmanager.utils.BitmapUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Portable way to allocate and free bitmaps.
 * For convenience BitmapBin is a singleton that you can acquire with getInstance()
 *
 * Keep in mind that you should try to keep balanced:
 *  - Don't forget to offer bitmaps you don't use anymore
 *  - Don't offer bitmaps you created without something else than claim
 *
 * Created bitmaps are mutable.
 */
public abstract class BitmapBin {
    /**
     * Creates (or reuse a previously offered) mutable bitmap.
     */
    public abstract @NonNull
    Bitmap claim(int width, int height, @NonNull Bitmap.Config config) throws OutOfMemoryError;

    /**
     * Offer a bitmap that you don't use anymore to the bin.
     */
    public abstract
    void offer(@NonNull Bitmap bitmap);

    /**
     * Empty the bin and release all reference to bitmaps (you can follow up with an explicit gc if you wan't memory now).
     */
    public abstract
    void gc();

    // API LEVEL 10 and lower
    // Not possible to reuse with thus API levels, only create/recycle.
    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    protected static class BitmapBin10 extends BitmapBin {
        @Override public @NonNull
        Bitmap claim(int width, int height, @NonNull Bitmap.Config config) throws OutOfMemoryError {
            return Bitmap.createBitmap(width, height, config);
        }

        @Override public
        void offer(@NonNull Bitmap bitmap) {
            bitmap.recycle();
        }

        @Override
        public void gc() {
        }
    }

    // API LEVEL 11 to 18
    // We can reuse bitmap with the same width+height+config combo
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected static class BitmapBin11 extends BitmapBin {

        protected static class BitmapBin11Key {
            Bitmap.Config dConfig;
            int dWidth, dHeight;

            public BitmapBin11Key(int width, int height, Bitmap.Config config) {
                dConfig = config;
                dWidth = width;
                dHeight = height;
            }

            @Override
            public boolean equals(Object o) {
                return o != null && o instanceof BitmapBin11Key && o.hashCode() == hashCode();
            }

            @Override
            public int hashCode() {
                int result = 42;

                result = 31 * result + dConfig.ordinal();
                result = 31 * result + dWidth;
                result = 31 * result + dHeight;

                return result;
            }
        }

        final Map<BitmapBin11Key, Queue<Bitmap>> dBitmaps = new HashMap<>();

        @Override public synchronized @NonNull
        Bitmap claim(int width, int height, @NonNull Bitmap.Config config) throws OutOfMemoryError {
            final BitmapBin11Key key = new BitmapBin11Key(width, height, config);
            final Queue<Bitmap> queue = dBitmaps.get(key);

            if (queue != null && !queue.isEmpty())
                return queue.poll();

            try {
                return Bitmap.createBitmap(width, height, config);
            }
            catch (OutOfMemoryError error) {
                gc();
                System.gc();
                return Bitmap.createBitmap(width, height, config);
            }
        }

        @Override public synchronized
        void offer(@NonNull final Bitmap bitmap) {
            final BitmapBin11Key key = new BitmapBin11Key(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            final Queue<Bitmap> queue = dBitmaps.get(key);

            if (queue != null)
                queue.offer(bitmap);
            else {
                dBitmaps.put(key, new LinkedList<Bitmap>() {{
                    offer(bitmap);
                }});
            }
        }

        @Override public synchronized
        void gc() {
            dBitmaps.clear();
        }
    }

    // API LEVEL 19 and up
    // We can reuse bitmaps with the same allocated size and reconfigure them
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected static class BitmapBin19 extends BitmapBin {

        final Map<Integer, Queue<Bitmap>> dBitmaps = new TreeMap<>();

        @Override public synchronized @NonNull
        Bitmap claim(int width, int height, @NonNull Bitmap.Config config) throws OutOfMemoryError {
            final int required = width * height * BitmapUtils.getConfigByteSize(config);
            final Queue<Bitmap> match = getMatch(required);
            if(match != null) {
                final Bitmap bmp = match.poll();
                bmp.reconfigure(width, height, config);
                return bmp;
            }

            final int pow2width = roundUpPow2(width);
            final int pow2height = roundUpPow2(height);

            try {
                final Bitmap bmp = Bitmap.createBitmap(pow2width, pow2height, config);
                bmp.reconfigure(width, height, config);
                return bmp;
            }
            catch (OutOfMemoryError error) {
                gc();
                System.gc();

                final Bitmap bmp = Bitmap.createBitmap(pow2width, pow2height, config);
                bmp.reconfigure(width, height, config);
                return bmp;
            }
        }

        @Override public synchronized
        void offer(@NonNull final Bitmap bitmap) {
            final int key = bitmap.getAllocationByteCount();
            final Queue<Bitmap> queue = dBitmaps.get(key);

            if (queue != null)
                queue.offer(bitmap);
            else {
                dBitmaps.put(key, new LinkedList<Bitmap>() {{
                    offer(bitmap);
                }});
            }
        }

        @Override public synchronized
        void gc() {
            dBitmaps.clear();
        }

        protected @Nullable
        Queue<Bitmap> getMatch(int required) {
            final Queue<Bitmap> perfectMatch = dBitmaps.get(required);
            if(perfectMatch != null && !perfectMatch.isEmpty())
                return perfectMatch;

            final Integer ceilingKey = ((TreeMap<Integer, Queue<Bitmap>>)dBitmaps).ceilingKey(required);
            if(ceilingKey != null) {
                final Queue<Bitmap> ceilingMatch = dBitmaps.get(ceilingKey);
                if(ceilingMatch != null && !ceilingMatch.isEmpty())
                    return ceilingMatch;
            }

            return null;
        }

        protected static
        int roundUpPow2(int v) {
            // http://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2
            v--;
            v |= v >> 1;
            v |= v >> 2;
            v |= v >> 4;
            v |= v >> 8;
            v |= v >> 16;
            v++;
            return v;
        }
    }

    protected static BitmapBin dInstance;
    static {
        if(Build.VERSION.SDK_INT >= 19)
            dInstance = new BitmapBin19();
        else if(Build.VERSION.SDK_INT >= 11)
            dInstance = new BitmapBin11();
        else
            dInstance = new BitmapBin10();
    }

    /**
     * Return the unique instance of the BitmapBin.
     */
    public static @NonNull
    BitmapBin getInstance() {
        return dInstance;
    }

    protected BitmapBin() {}
}
