package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.NonNull;

public class MuPage {
    protected long dNativePointer;
    protected long dNativeDocument;
    protected long dNativeContext;

    protected long dPageIndex;
    protected RectF dBounds = new RectF();

    protected static native void nFree(long doc, long page);
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

    public void getBounds(@NonNull RectF dst) {
        dst.set(dBounds);
    }

    public void render(float scale, @NonNull Bitmap dst) {
        nRender(dNativeContext, dNativeDocument, dNativePointer, scale, dst);
    }

    public
    @NonNull
    MuDisplayList renderDisplayList() throws MuPDFException {
        return new MuDisplayList(dNativeContext, nRenderDL(dNativeContext, dNativeDocument, dNativePointer));
    }
}
