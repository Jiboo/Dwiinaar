/*
    package com.github.jiboo.dwiinaar.mupdf;
    protected static native void nRender(long ctx, long dl, float scale, Rect src, Bitmap dst);
    protected static native void nFree(long ctx, long dl);
    protected static native byte[] nFlattern(long ctx, long dl);
*/

#include <android/bitmap.h>
#include "flatbuffers/flatbuffers.h"
#include "global.h"
#include "mupdf_generated.h"
#include "mupdf_private.h"

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
    fz_run_display_list(dl_cast(dl), dev, &transform, &bounds, NULL);

    fz_free_device(dev);
    fz_drop_pixmap(ctx_cast(ctx), pixmap);
}

JNIFUNC(void, MuDisplayList, nFree)(JNIEnv *env, jclass, jlong ctx, jlong dl) {
    fz_drop_display_list(ctx_cast(ctx), reinterpret_cast<fz_display_list*>(dl));
}

std::unique_ptr<mupdf::Rect> cast_rect(fz_rect& r) {
    return std::unique_ptr<mupdf::Rect>(new mupdf::Rect(r.x0, r.y0, r.x1, r.y1));
}

std::unique_ptr<mupdf::Matrix> cast_matrix(fz_matrix& m) {
    return std::unique_ptr<mupdf::Matrix>(new mupdf::Matrix(m.a, m.b, m.c, m.d, m.e, m.f));
}

int32_t cast_color(fz_colorspace* cs, float alpha, float color) {
    return 0; //TODO
}

flatbuffers::Offset<mupdf::DisplayListNode> serialNode(flatbuffers::FlatBufferBuilder& fbb, fz_display_node_s* node) {
    switch(node->cmd) {
        case FZ_CMD_BEGIN_GROUP:
            auto blendmode = mupdf::CreateBlendMode(fbb, node->item.blendmode);

            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_BEGIN_GROUP);
            dlnb.add_item_type(mupdf::DisplayListItem_BlendMode);
            dlnb.add_item(blendmode.Union());
            dlnb.add_flags(node->flag);
            dlnb.add_alpha(node->alpha);
            return dlnb.Finish();
        break;
    }

    mupdf::DisplayListNodeBuilder dlnb(fbb);
    switch(node->cmd) {
        case FZ_CMD_BEGIN_PAGE:
            dlnb.add_cmd(mupdf::DisplayCommand_BEGIN_PAGE);
            dlnb.add_rect(cast_rect(node->rect).get());
            dlnb.add_ctm(cast_matrix(node->ctm).get());
        break;
        case FZ_CMD_END_PAGE:
            dlnb.add_cmd(mupdf::DisplayCommand_END_PAGE);
        break;
        case FZ_CMD_FILL_PATH:
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_PATH);
        break;
        case FZ_CMD_STROKE_PATH:
            dlnb.add_cmd(mupdf::DisplayCommand_STROKE_PATH);
        break;
        case FZ_CMD_CLIP_PATH:
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_PATH);
        break;
        case FZ_CMD_CLIP_STROKE_PATH:
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_STROKE_PATH);
        break;
        case FZ_CMD_FILL_TEXT:
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_TEXT);
        break;
        case FZ_CMD_STROKE_TEXT:
            dlnb.add_cmd(mupdf::DisplayCommand_STROKE_TEXT);
        break;
        case FZ_CMD_CLIP_TEXT:
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_TEXT);
        break;
        case FZ_CMD_CLIP_STROKE_TEXT:
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_STROKE_TEXT);
        break;
        case FZ_CMD_IGNORE_TEXT:
            dlnb.add_cmd(mupdf::DisplayCommand_IGNORE_TEXT);
        break;
        case FZ_CMD_FILL_SHADE:
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_SHADE);
        break;
        case FZ_CMD_FILL_IMAGE:
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_IMAGE);
        break;
        case FZ_CMD_FILL_IMAGE_MASK:
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_IMAGE_MASK);
        break;
        case FZ_CMD_CLIP_IMAGE_MASK:
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_IMAGE_MASK);
        break;
        case FZ_CMD_POP_CLIP:
            dlnb.add_cmd(mupdf::DisplayCommand_POP_CLIP);
        break;
        case FZ_CMD_BEGIN_MASK:
            dlnb.add_cmd(mupdf::DisplayCommand_BEGIN_MASK);
        break;
        case FZ_CMD_END_MASK:
            dlnb.add_cmd(mupdf::DisplayCommand_END_MASK);
        break;
        case FZ_CMD_END_GROUP:
            dlnb.add_cmd(mupdf::DisplayCommand_END_GROUP);
        break;
        case FZ_CMD_BEGIN_TILE:
            dlnb.add_cmd(mupdf::DisplayCommand_BEGIN_TILE);
        break;
        case FZ_CMD_END_TILE:
            dlnb.add_cmd(mupdf::DisplayCommand_END_TILE);
        break;
    }
    return dlnb.Finish();
}

JNIFUNC(jobject, MuDisplayList, nFlattern)(JNIEnv *env, jclass, jlong ctx, jlong dl) {
    flatbuffers::FlatBufferBuilder fbb;
    fz_display_list* list = dl_cast(dl);
    fz_display_node_s* node;

    std::vector<flatbuffers::Offset<mupdf::DisplayListNode>> nodes;
    for(node = list->first; node != nullptr; node = node->next) {
        nodes.push_back(serialNode(fbb, node));
    }
    fbb.Finish(CreateDisplayList(fbb, fbb.CreateVector(nodes)));

    jbyteArray buffer = env->NewByteArray(fbb.GetSize());
    static_assert(sizeof(jbyte) == sizeof(uint8_t), "oops");
    env->SetByteArrayRegion(buffer, 0, fbb.GetSize(), (const jbyte*)fbb.GetBufferPointer());
    return buffer;
}

