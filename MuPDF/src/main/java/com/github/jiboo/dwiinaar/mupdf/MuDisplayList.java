package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class MuDisplayList {
    protected long dNativePointer;
    protected long dNativeContext;

    protected static native void nRender(long ctx, long dl, float scale, RectF src, Bitmap dst);

    protected static native void nFree(long ctx, long dl);

    protected MuDisplayList(long ctx, long dl) {
        dNativeContext = ctx;
        dNativePointer = dl;
    }

    public void recycle() {
        nFree(dNativeContext, dNativePointer);
    }

    public void render(float scale, RectF src, Bitmap dst) {
        nRender(dNativeContext, dNativePointer, scale, src, dst);
    }
}
