/*
    package com.github.jiboo.dwiinaar.mupdf;
    protected static native long nOpenDocument(long ctx, String path);
    protected static native void nClose(long doc);
    protected static native boolean nAuthenticatePassword(long doc, String password);
    protected static native long nLoadOutline(long doc);
    protected static native long nLoadPage(long doc, int index);
*/

#include "global.h"

JNIFUNC(jlong, MuDocument, nOpenDocument)(JNIEnv *env, jobject thiz, jlong ctx, jstring path) {
    const char *nativePath = env->GetStringUTFChars(path, nullptr);

    jlong doc = 0;
    TRY(ctx) {
        fz_document* nativeDoc = fz_open_document(ctx_cast(ctx), nativePath);
        env->SetIntField(thiz, glue_cast(ctx)->MuDocument_dPageCount, fz_count_pages(nativeDoc));
        env->SetBooleanField(thiz, glue_cast(ctx)->MuDocument_dNeedsPassword, fz_needs_password(nativeDoc) != 0);
        doc = reinterpret_cast<jlong>(nativeDoc);
    }
    CATCH(ctx) {
        THROW2JAVA(env, ctx);
    }

    env->ReleaseStringUTFChars(path, nativePath);

    return doc;
}

JNIFUNC(void, MuDocument, nClose)(JNIEnv *, jclass, jlong doc) {
    fz_close_document(doc_cast(doc));
}

JNIFUNC(jboolean, MuDocument, nAuthenticatePassword)(JNIEnv *, jclass, jlong, jstring) {
    return false; //TODO
}

JNIFUNC(jlong, MuDocument, nLoadOutline)(JNIEnv *, jclass, jlong) {
    return 0; //TODO
}

JNIFUNC(jlong, MuDocument, nLoadPage)(JNIEnv *, jclass, jlong doc, jint pageIndex) {
    return reinterpret_cast<jlong>(fz_load_page(doc_cast(doc), pageIndex));
}
