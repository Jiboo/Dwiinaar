package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

public class MuPage {
    protected int dNativePointer;
    protected int dNativeDocument;
    protected int dNativeContext;

    protected int dPageIndex;
    protected Rect dBounds = new Rect();

    protected static native void nFree(int page);
    protected static native int nLoadLinks(int doc, int page);
    protected static native void nBoundPage(int doc, int page, Rect rect);
    protected static native void nRender(int ctx, int doc, int page, Matrix transform, Bitmap dst);
    protected static native int nRenderDL(int ctx, int doc, int page);

    protected MuPage(int ctx, int doc, int nativePointer, int index) {
        dNativeContext = ctx;
        dNativeDocument = doc;
        dNativePointer = nativePointer;
        dPageIndex = index;

        nBoundPage(dNativeDocument, dNativePointer, dBounds);
    }

    public void recycle() {
        nFree(dNativePointer);
    }

    public MuLinks loadLinks() {
        return new MuLinks(dNativeContext, nLoadLinks(dNativeDocument, dNativePointer));
    }

    public void getBounds(Rect dst) {
        dst.set(dBounds);
    }

    public void render(Matrix transform, Bitmap dst) {
        nRender(dNativeContext, dNativeDocument, dNativePointer, transform, dst);
    }

    public MuDisplayList renderDisplayList() {
        return new MuDisplayList(dNativeContext, nRenderDL(dNativeContext, dNativeDocument, dNativePointer));
    }
}
