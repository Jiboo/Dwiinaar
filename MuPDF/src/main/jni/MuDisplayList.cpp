/*
    package com.github.jiboo.dwiinaar.mupdf;
    protected static native void nRender(long ctx, long dl, float scale, Rect src, Bitmap dst);
    protected static native void nFree(long ctx, long dl);
*/

#include <android/bitmap.h>
#include "global.h"

JNIFUNC(void, MuDisplayList, nRender) (JNIEnv *env, jclass, jlong ctx, jlong dl, jfloat scale, jobject src, jobject bmp) {
    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bmp, &info);

    void *pixels;
    AndroidBitmap_lockPixels(env, bmp, &pixels);

    fz_matrix transform;
    fz_scale(&transform, scale, scale);
    fz_rect bounds;
    bounds.x0 = env->GetFloatField(src, glue_cast(ctx)->RectF_left);
    bounds.y0 = env->GetFloatField(src, glue_cast(ctx)->RectF_top);
    bounds.x1 = env->GetFloatField(src, glue_cast(ctx)->RectF_right);
    bounds.y1 = env->GetFloatField(src, glue_cast(ctx)->RectF_bottom);
    fz_transform_rect(&bounds, &transform);
    fz_irect bbox;
    fz_round_rect(&bbox, &bounds);

    //bbox.x1 = bbox.x0 + info.stride;

    fz_pixmap *pixmap = fz_new_pixmap_with_bbox_and_data(ctx_cast(ctx), fz_device_rgb(ctx_cast(ctx)), &bbox, (unsigned char *)pixels);
    fz_clear_pixmap_with_value(ctx_cast(ctx), pixmap, 0xff);

    fz_device *dev = fz_new_draw_device_with_bbox(ctx_cast(ctx), pixmap, &bbox);
    fz_run_display_list(reinterpret_cast<fz_display_list*>(dl), dev, &transform, &bounds, NULL);

    fz_free_device(dev);
    fz_drop_pixmap(ctx_cast(ctx), pixmap);
}

JNIFUNC(void, MuDisplayList, nFree)(JNIEnv *env, jclass, jlong ctx, jlong dl) {
    fz_drop_display_list(ctx_cast(ctx), reinterpret_cast<fz_display_list*>(dl));
}