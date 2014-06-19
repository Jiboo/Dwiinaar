package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.jiboo.dwiinaar.mupdf.displaylist.DisplayList;
import com.github.jiboo.dwiinaar.mupdf.displaylist.DisplayListNode;

import java.nio.ByteBuffer;

public class MuDisplayList {
    protected long dNativePointer;
    protected long dNativeContext;

    protected DisplayList dJavaList = null;

    protected static native void nRender(long ctx, long dl, float scale, RectF src, Bitmap dst);

    protected static native ByteBuffer nFlattern(long ctx, long dl);
    protected static native void nFree(long ctx, long dl);

    protected MuDisplayList(long ctx, long dl) {
        dNativeContext = ctx;
        dNativePointer = dl;
    }

    public void recycle() {
        nFree(dNativeContext, dNativePointer);
        dNativePointer = 0;
    }

    public void render(float scale, @NonNull RectF src, @NonNull Bitmap dst) {
        if (dNativePointer != 0)
            nRender(dNativeContext, dNativePointer, scale, src, dst);
        else
            throw new IllegalStateException("Display list is recycled");
    }

    public void flattern() throws MuPDFException {
        dJavaList = DisplayList.getRootAsDisplayList(nFlattern(dNativeContext, dNativePointer), 0);
    }

    public void render(@NonNull Canvas canvas) {
        if (dJavaList == null)
            throw new IllegalStateException("DisplayList not flatterned");
        for (int i = 0; i < dJavaList.nodesLength(); i++) {
            renderNode(canvas, dJavaList.nodes(i));
        }
    }

    private void renderNode(@NonNull Canvas canvas, DisplayListNode node) {
        // Stuff
    }
}
