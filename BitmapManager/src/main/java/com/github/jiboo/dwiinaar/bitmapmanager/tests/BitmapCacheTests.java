package com.github.jiboo.dwiinaar.bitmapmanager.tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.test.InstrumentationTestCase;

import com.github.jiboo.dwiinaar.bitmapmanager.BitmapCache;
import com.github.jiboo.dwiinaar.bitmapmanager.R;

import java.net.URL;

public class BitmapCacheTests extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        BitmapCache.BitmapDiskCache.initInstance(getInstrumentation().getContext());
    }

    public void testSimple() throws Exception {
        final Object testLock = new Object();
        final Context ctx = getInstrumentation().getContext();

        final BitmapCache.Key key = BitmapCache.getKey(ctx, R.drawable.abc_ic_go);
        final BitmapCache.Listener listener = new BitmapCache.Listener() {
            @Override
            public void onBitmapLoaded(@NonNull BitmapCache.Key key, @NonNull Bitmap value) {
                synchronized (testLock) {
                    testLock.notify();
                }
                assertTrue(value != null);
            }

            @Override
            public void onBitmapEvicted(@NonNull BitmapCache.Key key, boolean evicted, @NonNull Bitmap oldValue, Bitmap newValue) {
                synchronized (testLock) {
                    testLock.notify();
                }
                fail("Evicted?");
            }

            @Override
            public void onBitmapDecodingError(@NonNull BitmapCache.Key key, @NonNull Throwable error) {
                synchronized (testLock) {
                    testLock.notify();
                }
                fail(error.getMessage());
            }
        };
        BitmapCache.subscribe(key, listener);
        BitmapCache.asyncDecode(key, new BitmapCache.Options());

        synchronized (testLock) {
            testLock.wait();
        }

        BitmapCache.unsubscribe(key, listener);
    }

    public void testKeyReconfigure() throws Exception {
        final BitmapCache.Key key1 = BitmapCache.getKey(new URL("http://foo.bar/test.png"));
        final BitmapCache.Key key2 = BitmapCache.reconfigure(key1, Bitmap.Config.RGB_565, 4, null);

        assertNotSame(key1, key2);
    }
}
