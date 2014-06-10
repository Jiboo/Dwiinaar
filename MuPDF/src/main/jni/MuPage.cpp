/*
    package com.github.jiboo.dwiinaar.mupdf;
    protected static native void nFree(long doc, long page);
    protected static native long nLoadLinks(long doc, long page);
    protected static native void nBoundPage(long ctx, long doc, long page, Rect rect);
    protected static native void nRender(long ctx, long doc, long page, float scale, Bitmap dst);
    protected static native long nRenderDL(long ctx, long doc, long page);
*/

#include <android/bitmap.h>
#include "global.h"

JNIFUNC(void, MuPage, nFree)(JNIEnv *, jclass, jint doc, jint page) {
    fz_free_page(doc_cast(doc), page_cast(page));
}

JNIFUNC(jlong, MuPage, nLoadLinks)(JNIEnv *env, jclass, jlong doc, jlong page) {
    return 0; //TODO
}

JNIFUNC(void, MuPage, nBoundPage)(JNIEnv *env, jclass, jlong ctx, jlong doc, jlong page, jobject rect) {
    fz_rect bounds;
    fz_bound_page(doc_cast(doc), page_cast(page), &bounds);
    env->SetFloatField(rect, glue_cast(ctx)->RectF_left, bounds.x0);
    env->SetFloatField(rect, glue_cast(ctx)->RectF_top, bounds.y0);
    env->SetFloatField(rect, glue_cast(ctx)->RectF_right, bounds.x1);
    env->SetFloatField(rect, glue_cast(ctx)->RectF_bottom, bounds.y1);
}

JNIFUNC(void, MuPage, nRender)(JNIEnv *env, jclass, jlong ctx, jlong doc, jlong page, jfloat scale, jobject bmp) {
    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bmp, &info);

    void *pixels;
    AndroidBitmap_lockPixels(env, bmp, &pixels);

    fz_matrix transform;
    fz_scale(&transform, scale, scale);
    fz_rect bounds;
    fz_bound_page(doc_cast(doc), page_cast(page), &bounds);
    fz_transform_rect(&bounds, &transform);
    fz_irect bbox;
    fz_round_rect(&bbox, &bounds);

    //bbox.x1 = bbox.x0 + info.stride;

    fz_pixmap *pixmap = fz_new_pixmap_with_bbox_and_data(ctx_cast(ctx), fz_device_rgb(ctx_cast(ctx)), &bbox, (unsigned char*)pixels);
    fz_clear_pixmap_with_value(ctx_cast(ctx), pixmap, 0xff);

    fz_device *dev = fz_new_draw_device_with_bbox(ctx_cast(ctx), pixmap, &bbox);
    fz_run_page(doc_cast(doc), page_cast(page), dev, &transform, NULL);

    fz_free_device(dev);
    fz_drop_pixmap(ctx_cast(ctx), pixmap);
}

JNIFUNC(jlong, MuPage, nRenderDL)(JNIEnv *env, jclass, jlong ctx, jlong doc, jlong page) {
    fz_display_list* nativeDL = fz_new_display_list(ctx_cast(ctx));
    fz_device* dev = fz_new_list_device(ctx_cast(ctx), nativeDL);
    fz_run_page_contents(doc_cast(doc), page_cast(page), dev, &fz_identity, nullptr);
    fz_free_device(dev);

    return reinterpret_cast<jlong>(nativeDL);
}