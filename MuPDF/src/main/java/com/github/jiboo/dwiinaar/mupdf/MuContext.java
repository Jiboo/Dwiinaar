package com.github.jiboo.dwiinaar.mupdf;

public class MuContext {
    static {
        System.loadLibrary("mupdf");
    }

    protected long dNativePointer;

    protected static native long nNew();

    protected static native void nFree(long ctx);

    public MuContext() {
        dNativePointer = nNew();
    }

    public void recycle() {
        nFree(dNativePointer);
    }
}