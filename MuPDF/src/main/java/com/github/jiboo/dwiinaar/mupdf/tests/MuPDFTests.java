package com.github.jiboo.dwiinaar.mupdf.tests;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.github.jiboo.dwiinaar.mupdf.MuContext;
import com.github.jiboo.dwiinaar.mupdf.MuDocument;
import com.github.jiboo.dwiinaar.mupdf.MuPage;

import java.io.File;
import java.io.FileOutputStream;

public class MuPDFTests extends InstrumentationTestCase {
    public void testContextCreation() throws Exception {
        final MuContext ctx = new MuContext();
        ctx.recycle();
    }

    public void testOpenDocument() throws Exception {
        final MuContext ctx = new MuContext();
        final MuDocument doc = new MuDocument(ctx, new File("/sdcard/Download/blendmode.pdf"));
        doc.recycle();
        ctx.recycle();
    }

    public void testRenderPage() throws Exception {
        final MuContext ctx = new MuContext();
        final MuDocument doc = new MuDocument(ctx, new File("/sdcard/Download/blendmode.pdf"));
        final MuPage page = doc.loadPage(0);
        final RectF bounds = new RectF();
        page.getBounds(bounds);
        final Bitmap target = Bitmap.createBitmap(Math.round(bounds.width()), Math.round(bounds.height()), Bitmap.Config.ARGB_8888);
        final long time = SystemClock.elapsedRealtime();
        page.render(1, target);
        final long timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderPage render time: " + (timeEnd - time));
        target.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream("/sdcard/Download/blendmode-0.png"));
        page.recycle();
        doc.recycle();
        ctx.recycle();
    }
}
