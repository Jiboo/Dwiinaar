package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Bitmap;

public class MuContext {
    protected int dNativePointer;

    protected static native int nNew();
    protected static native void nFree(int ctx);

    protected MuContext() {
        dNativePointer = nNew();
    }

    public void recycle() {
        nFree(dNativePointer);
    }
}