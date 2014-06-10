/*
    package com.github.jiboo.dwiinaar.mupdf;
    protected static native long nNew();
    protected static native void nFree(long ctx);
*/

#include "global.h"

JNIFUNC(jlong, MuContext, nNew)(JNIEnv *env, jclass clazz) {
    ContextGlue* glue = new ContextGlue;
    glue->ctx = fz_new_context(nullptr, nullptr, FZ_STORE_UNLIMITED);

    fz_register_document_handlers(glue->ctx);

    glue->RectF = env->FindClass("android/graphics/RectF");
        glue->RectF_left = env->GetFieldID(glue->RectF, "left", "F");
        glue->RectF_top = env->GetFieldID(glue->RectF, "top", "F");
        glue->RectF_right = env->GetFieldID(glue->RectF, "right", "F");
        glue->RectF_bottom = env->GetFieldID(glue->RectF, "bottom", "F");

    glue->Bitmap = env->FindClass("android/graphics/Bitmap");
        glue->Bitmap_mWidth = env->GetFieldID(glue->Bitmap, "mWidth", "I");
        glue->Bitmap_mHeight = env->GetFieldID(glue->Bitmap, "mHeight", "I");

    glue->MuDocument = env->FindClass("com/github/jiboo/dwiinaar/mupdf/MuDocument");
        glue->MuDocument_dPageCount = env->GetFieldID(glue->MuDocument, "dPageCount", "I");
        glue->MuDocument_dNeedsPassword = env->GetFieldID(glue->MuDocument, "dNeedsPassword", "Z");

    glue->MuPDFException = env->FindClass("com/github/jiboo/dwiinaar/mupdf/MuPDFException");
    return reinterpret_cast<jlong>(glue);
}

JNIFUNC(void, MuContext, nFree) (JNIEnv *, jclass, jlong ctx) {
    fz_free_context(ctx_cast(ctx));
    delete glue_cast(ctx);
}
