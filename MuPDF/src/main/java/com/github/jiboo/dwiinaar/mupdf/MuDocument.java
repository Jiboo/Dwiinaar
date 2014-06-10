package com.github.jiboo.dwiinaar.mupdf;

import java.io.File;
import java.io.FileNotFoundException;

public class MuDocument {
    protected long dNativePointer;
    protected long dNativeContext;
    protected int dPageCount;
    protected boolean dNeedsPassword;

    protected native long nOpenDocument(long ctx, String path);

    protected static native void nClose(long doc);

    protected static native boolean nAuthenticatePassword(long doc, String password);

    //protected static native int nLoadOutline(int doc);
    protected static native int nCountPages(long doc);

    protected static native long nLoadPage(long doc, int index);

    public MuDocument(MuContext ctx, File file) throws FileNotFoundException {
        if (file.exists() && file.isFile()) {
            dNativeContext = ctx.dNativePointer;
            dNativePointer = nOpenDocument(dNativeContext, file.getAbsolutePath());
        } else {
            throw new FileNotFoundException("Can't find " + file.toString());
        }
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

    /*public MuOutline loadOutline() {
        return new MuOutline(dNativeContext, nLoadOutline(dNativePointer));
    }*/

    public MuPage loadPage(int index) {
        return new MuPage(dNativeContext, dNativePointer, nLoadPage(dNativePointer, index), index);
    }
}
