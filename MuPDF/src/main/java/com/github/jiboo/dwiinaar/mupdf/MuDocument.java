package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Rect;

import java.io.File;

public class MuDocument {
    protected int dNativePointer;
    protected int dNativeContext;
    protected int dPageCount;
    protected boolean dNeedsPassword;

    protected static native int nOpenDocument(int ctx, String path);
    protected static native void nClose(int doc);
    protected static native boolean nAuthenticatePassword(int doc, String password);
    protected static native int nLoadOutline(int doc);
    protected static native int nLoadPage(int doc, int index);

    public MuDocument(MuContext ctx, File file) {
        dNativeContext = ctx.dNativePointer;
        dNativePointer = nOpenDocument(dNativeContext, file.getAbsolutePath());
    }

    public void recycle() {
        nClose(dNativePointer);
    }

    public boolean needsPassword() {
        return dNeedsPassword;
    }

    public boolean tryPassword(String password) {
        return nAuthenticatePassword(dNativePointer, password);
    }

    public int getPageCount() {
        return dPageCount;
    }

    public MuOutline loadOutline() {
        return new MuOutline(dNativeContext, (dNativePointer));
    }

    public MuPage loadPage(int index) {
        return new MuPage(dNativeContext, dNativePointer, nLoadPage(dNativePointer, index), index);
    }
}
