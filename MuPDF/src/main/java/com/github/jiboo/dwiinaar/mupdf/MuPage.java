package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class MuPage {
    protected long dNativePointer;
    protected long dNativeDocument;
    protected long dNativeContext;

    protected long dPageIndex;
    protected RectF dBounds = new RectF();

    protected static native void nFree(long doc, long page);

    //protected static native int nLoadLinks(int doc, int page);
    protected static native void nBoundPage(long ctx, long doc, long page, RectF rect);

    protected static native void nRender(long ctx, long doc, long page, float scale, Bitmap dst);

    protected static native int nRenderDL(long ctx, long doc, long page);

    protected MuPage(long ctx, long doc, long nativePointer, int index) {
        dNativeContext = ctx;
        dNativeDocument = doc;
        dNativePointer = nativePointer;
        dPageIndex = index;

        nBoundPage(dNativeContext, dNativeDocument, dNativePointer, dBounds);
    }

    public void recycle() {
        nFree(dNativeContext, dNativePointer);
    }

    /*public MuLinks loadLinks() {
        return new MuLinks(dNativeContext, nLoadLinks(dNativeDocument, dNativePointer));
    }*/

    public void getBounds(RectF dst) {
        dst.set(dBounds);
    }

    public void render(float scale, Bitmap dst) {
        nRender(dNativeContext, dNativeDocument, dNativePointer, scale, dst);
    }

    public MuDisplayList renderDisplayList() {
        return new MuDisplayList(dNativeContext, nRenderDL(dNativeContext, dNativeDocument, dNativePointer));
    }
}
