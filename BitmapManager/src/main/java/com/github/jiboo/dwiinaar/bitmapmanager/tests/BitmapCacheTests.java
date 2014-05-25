package com.github.jiboo.dwiinaar.bitmapmanager.tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.InstrumentationTestCase;

import com.github.jiboo.dwiinaar.bitmapmanager.BitmapCache;
import com.github.jiboo.dwiinaar.bitmapmanager.R;

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
            public void onBitmapLoaded(BitmapCache.Key key, Bitmap value) {
                synchronized (testLock) {
                    testLock.notify();
                }
                assertTrue(value != null);
            }

            @Override
            public void onBitmapEvicted(BitmapCache.Key key, boolean evicted, Bitmap oldValue, Bitmap newValue) {
                synchronized (testLock) {
                    testLock.notify();
                }
                fail("Evicted?");
            }

            @Override
            public void onBitmapDecodingError(BitmapCache.Key key, Throwable error) {
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
}
