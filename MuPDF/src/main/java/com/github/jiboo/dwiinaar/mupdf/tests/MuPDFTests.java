package com.github.jiboo.dwiinaar.mupdf.tests;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.github.jiboo.dwiinaar.mupdf.MuContext;
import com.github.jiboo.dwiinaar.mupdf.MuDisplayList;
import com.github.jiboo.dwiinaar.mupdf.MuDocument;
import com.github.jiboo.dwiinaar.mupdf.MuMath;
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
        long time = SystemClock.elapsedRealtime();
        final MuContext ctx = new MuContext();
        long timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderPage context creation: " + (timeEnd - time));

        time = SystemClock.elapsedRealtime();
        final MuDocument doc = new MuDocument(ctx, new File("/sdcard/Download/blendmode.pdf"));
        timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderPage open document: " + (timeEnd - time));

        time = SystemClock.elapsedRealtime();
        final MuPage page = doc.loadPage(0);
        timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderPage page load: " + (timeEnd - time));

        final RectF bounds = new RectF();

        time = SystemClock.elapsedRealtime();
        page.getBounds(bounds);
        timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderPage getBounds: " + (timeEnd - time));

        final Rect ibounds = MuMath.round_rect(bounds);
        final Bitmap target = Bitmap.createBitmap(ibounds.width(), ibounds.height(), Bitmap.Config.ARGB_8888);

        time = SystemClock.elapsedRealtime();
        page.render(1, target);
        timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderPage render time: " + (timeEnd - time));

        target.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream("/sdcard/Download/blendmode.png"));
        page.recycle();
        doc.recycle();
        ctx.recycle();
    }

    public void testRenderTile() throws Exception {
        final MuContext ctx = new MuContext();
        final MuDocument doc = new MuDocument(ctx, new File("/sdcard/Download/blendmode.pdf"));
        final MuPage page = doc.loadPage(0);
        final MuDisplayList dl = page.renderDisplayList();
        final int tileSize = 100;
        final float scale = 4;
        final RectF tile0 = new RectF(tileSize * 0, tileSize * 0, tileSize * 1, tileSize * 1);
        final RectF tile1 = new RectF(tileSize * 1, tileSize * 0, tileSize * 2, tileSize * 1);
        final RectF tile2 = new RectF(tileSize * 1, tileSize * 1, tileSize * 2, tileSize * 2);

        final Bitmap target = Bitmap.createBitmap(Math.round(tileSize * scale), Math.round(tileSize * scale), Bitmap.Config.ARGB_8888);

        long time = SystemClock.elapsedRealtime();
        dl.render(4, tile0, target);
        long timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderTile 0: " + (timeEnd - time));
        target.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream("/sdcard/Download/tile-0.png"));

        time = SystemClock.elapsedRealtime();
        dl.render(4, tile1, target);
        timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderTile 1: " + (timeEnd - time));
        target.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream("/sdcard/Download/tile-1.png"));

        time = SystemClock.elapsedRealtime();
        dl.render(4, tile2, target);
        timeEnd = SystemClock.elapsedRealtime();
        Log.d("TESTS", "testRenderTile 2: " + (timeEnd - time));
        target.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream("/sdcard/Download/tile-2.png"));

        dl.recycle();
        page.recycle();
        doc.recycle();
        ctx.recycle();
    }

    public void testReconfiguredBitmap() throws Exception {
        if (Build.VERSION.SDK_INT >= 19) {
            //Check if reconfigured bitmap woudln't work with fz_new_pixmap_with_bbox_and_data
            final MuContext ctx = new MuContext();
            final MuDocument doc = new MuDocument(ctx, new File("/sdcard/Download/blendmode.pdf"));
            final MuPage page = doc.loadPage(0);
            final RectF bounds = new RectF();
            page.getBounds(bounds);
            final Rect ibounds = MuMath.round_rect(bounds);
            final Bitmap target = Bitmap.createBitmap(2048, 2048, Bitmap.Config.ARGB_8888);
            target.reconfigure(ibounds.width(), ibounds.height(), Bitmap.Config.ARGB_8888);
            page.render(1, target);
            target.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream("/sdcard/Download/reconfigured.png"));
            page.recycle();
            doc.recycle();
            ctx.recycle();
        }
    }
}
