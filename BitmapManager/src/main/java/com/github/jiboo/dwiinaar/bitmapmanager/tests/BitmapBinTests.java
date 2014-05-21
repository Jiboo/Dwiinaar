package com.github.jiboo.dwiinaar.bitmapmanager.tests;

import android.graphics.Bitmap;
import android.os.Build;
import android.test.InstrumentationTestCase;

import com.github.jiboo.dwiinaar.bitmapmanager.BitmapBin;

public class BitmapBinTests extends InstrumentationTestCase {

    public void testClaim() throws Exception {
        final BitmapBin bin = BitmapBin.getInstance();
        bin.gc();

        final Bitmap bmp = bin.claim(64, 128, Bitmap.Config.ARGB_8888);

        assertEquals(64, bmp.getWidth());
        assertEquals(128, bmp.getHeight());
        assertEquals(Bitmap.Config.ARGB_8888, bmp.getConfig());

        bin.offer(bmp);
    }

    public void testReuse() throws Exception {
        if(Build.VERSION.SDK_INT >= 11) {
            final BitmapBin bin = BitmapBin.getInstance();
            bin.gc();

            final Bitmap bmp = bin.claim(64, 64, Bitmap.Config.ARGB_8888);
            bin.offer(bmp);
            final Bitmap bmp2 = bin.claim(64, 64, Bitmap.Config.ARGB_8888);
            assertEquals(bmp, bmp2);
        }
    }

    public void testMultiple() throws Exception {
        final BitmapBin bin = BitmapBin.getInstance();
        bin.gc();

        bin.offer(bin.claim(64, 64, Bitmap.Config.ARGB_8888));
        bin.offer(bin.claim(128, 64, Bitmap.Config.ARGB_8888));
        bin.offer(bin.claim(64, 128, Bitmap.Config.ARGB_8888));
        bin.offer(bin.claim(128, 128, Bitmap.Config.ARGB_8888));

        final Bitmap bmp1 = bin.claim(64, 128, Bitmap.Config.ARGB_8888);
        assertEquals(64, bmp1.getWidth());
        assertEquals(128, bmp1.getHeight());

        final Bitmap bmp2 = bin.claim(128, 64, Bitmap.Config.ARGB_8888);
        assertEquals(128, bmp2.getWidth());
        assertEquals(64, bmp2.getHeight());

        bin.offer(bmp1);
        bin.offer(bmp2);
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

    public void testPow2RoundUp() throws Exception {
        assertEquals(128, roundUpPow2(127));
        assertEquals(128, roundUpPow2(128));
        assertEquals(2048, roundUpPow2(1042));
    }
}
