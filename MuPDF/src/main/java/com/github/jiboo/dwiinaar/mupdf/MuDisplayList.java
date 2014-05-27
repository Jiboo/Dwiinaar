package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

public class MuDisplayList {
    protected int dNativePointer;
    protected int dNativeContext;

    protected static native int nRender(int ctx, int dl, Matrix transform, Rect src, Bitmap dst);
    protected static native void nFree(int ctx, int dl);

    protected MuDisplayList(int ctx, int dl) {
        dNativeContext = ctx;
        dNativePointer = dl;
    }

    public void recycle() {
        nFree(dNativeContext, dNativePointer);
    }

    public void render(Matrix transform, Rect src, Bitmap dst) {
        nRender(dNativeContext, dNativePointer, transform, src, dst);
    }
}
