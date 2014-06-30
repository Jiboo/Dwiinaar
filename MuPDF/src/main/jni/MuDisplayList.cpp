/*
    package com.github.jiboo.dwiinaar.mupdf;
    protected static native void nRender(long ctx, long dl, float scale, Rect src, Bitmap dst);
    protected static native void nFree(long ctx, long dl);
    protected static native byte[] nFlattern(long ctx, long dl);
*/

#include <android/bitmap.h>
#include "flatbuffers/flatbuffers.h"
#include "flatbuffers/idl.h"
#include "flatbuffers/util.h"
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

std::unique_ptr<mupdf::Point> cast_point(fz_point& r) {
    return std::unique_ptr<mupdf::Point>(new mupdf::Point(r.x, r.y));
}

std::unique_ptr<mupdf::Rect> cast_rect(fz_rect& r) {
    return std::unique_ptr<mupdf::Rect>(new mupdf::Rect(r.x0, r.y0, r.x1, r.y1));
}

std::unique_ptr<mupdf::Matrix> cast_matrix(fz_matrix& m) {
    return std::unique_ptr<mupdf::Matrix>(new mupdf::Matrix(m.a, m.b, m.c, m.d, m.e, m.f));
}

int32_t create_color(float alpha, float red, float green, float blue) {
    return
        ((int32_t)(alpha * 255) << 24 & 0xFF000000) |
        ((int32_t)(red   * 255) << 16 & 0x00FF0000) |
        ((int32_t)(green * 255) <<  8 & 0x0000FF00) |
        ((int32_t)(blue  * 255)       & 0x000000FF);
}

int32_t cast_color(fz_context* ctx, fz_colorspace* cs, float alpha, float* color) {
    float colorfv[3];
    fz_convert_color(ctx, cs, colorfv, fz_device_rgb(ctx), color);
    return create_color(alpha, colorfv[0], colorfv[1], colorfv[2]);
}

flatbuffers::Offset<flatbuffers::Vector<int32_t>> createColors(flatbuffers::FlatBufferBuilder& fbb, fz_context* ctx, fz_colorspace* cs, float alpha, float* color, int count) {
    int colors[count];
    for(int i = 0; i < count; i++) {
        if(color != nullptr) {
            colors[i] = cast_color(ctx, cs, alpha, color);
            color += cs->n;
        }
        else
            colors[i] = (int32_t)(alpha * 255);
    }
    return fbb.CreateVector(colors, count);
}

flatbuffers::Offset<mupdf::Path> createPath(flatbuffers::FlatBufferBuilder& fbb, fz_path_s* path) {
    std::vector<flatbuffers::Offset<mupdf::PathNode>> nodes;
    int offset = 0;
    for(unsigned char* cmd = path->cmds; *cmd != 0; cmd++) {
        switch(*cmd) {
            case 'M': {
                auto array = fbb.CreateVectorOfStructs((mupdf::Point*)(path->coords + offset), 1);
                offset += 2;

                mupdf::PathNodeBuilder pnb(fbb);
                pnb.add_cmd(mupdf::PathCommand_MOVETO);
                pnb.add_coord(array);
                nodes.push_back(pnb.Finish());
            } break;
            case 'L': {
                auto array = fbb.CreateVectorOfStructs((mupdf::Point*)(path->coords + offset), 1);
                offset += 2;

                mupdf::PathNodeBuilder pnb(fbb);
                pnb.add_cmd(mupdf::PathCommand_LINETO);
                pnb.add_coord(array);
                nodes.push_back(pnb.Finish());
            } break;
            case 'C': {
                auto array = fbb.CreateVectorOfStructs((mupdf::Point*)(path->coords + offset), 3);
                offset += 6;

                mupdf::PathNodeBuilder pnb(fbb);
                pnb.add_cmd(mupdf::PathCommand_CURVETO);
                pnb.add_coord(array);
                nodes.push_back(pnb.Finish());
            } break;
            case 'Z': {
                mupdf::PathNodeBuilder pnb(fbb);
                pnb.add_cmd(mupdf::PathCommand_CLOSE);
                nodes.push_back(pnb.Finish());
            } break;
        }
    }
    return mupdf::CreatePath(fbb, cast_point(path->begin).get(), fbb.CreateVector(nodes));
}

flatbuffers::Offset<mupdf::DisplayListNode> createNode(flatbuffers::FlatBufferBuilder& fbb, fz_context* ctx, fz_display_node_s* node) {
    switch(node->cmd) {
        case FZ_CMD_BEGIN_PAGE: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_BEGIN_PAGE);
            dlnb.add_rect(cast_rect(node->rect).get());
            dlnb.add_ctm(cast_matrix(node->ctm).get());
            return dlnb.Finish();
        } break;
        case FZ_CMD_FILL_PATH: {
            auto path = createPath(fbb, node->item.path);
            auto colors = createColors(fbb, ctx, node->colorspace, node->alpha, node->color, 1);
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_PATH);
            dlnb.add_item_type(mupdf::DisplayListItem_Path);
            dlnb.add_item(path.Union());
            dlnb.add_flags(node->flag);
            dlnb.add_ctm(cast_matrix(node->ctm).get());
            dlnb.add_color(colors);
            return dlnb.Finish();
        } break;
        case FZ_CMD_STROKE_PATH: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_STROKE_PATH);
            return dlnb.Finish();
        } break;
        case FZ_CMD_CLIP_PATH: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_PATH);
            return dlnb.Finish();
        } break;
        case FZ_CMD_CLIP_STROKE_PATH: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_STROKE_PATH);
            return dlnb.Finish();
        } break;
        case FZ_CMD_FILL_TEXT: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_TEXT);
            return dlnb.Finish();
        } break;
        case FZ_CMD_STROKE_TEXT: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_STROKE_TEXT);
            return dlnb.Finish();
        } break;
        case FZ_CMD_CLIP_TEXT: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_TEXT);
            return dlnb.Finish();
        } break;
        case FZ_CMD_CLIP_STROKE_TEXT: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_STROKE_TEXT);
            return dlnb.Finish();
        } break;
        case FZ_CMD_IGNORE_TEXT: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_IGNORE_TEXT);
            return dlnb.Finish();
        } break;
        case FZ_CMD_FILL_SHADE: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_SHADE);
            return dlnb.Finish();
        } break;
        case FZ_CMD_FILL_IMAGE: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_IMAGE);
            return dlnb.Finish();
        } break;
        case FZ_CMD_FILL_IMAGE_MASK: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_FILL_IMAGE_MASK);
            return dlnb.Finish();
        } break;
        case FZ_CMD_CLIP_IMAGE_MASK: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_CLIP_IMAGE_MASK);
            return dlnb.Finish();
        } break;
        case FZ_CMD_BEGIN_MASK: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_BEGIN_MASK);
            return dlnb.Finish();
        } break;
        case FZ_CMD_BEGIN_GROUP: {
            auto blendmode = mupdf::CreateBlendMode(fbb, node->item.blendmode);
            auto alpha = createColors(fbb, ctx, node->colorspace, node->alpha, nullptr, 1);

            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_BEGIN_GROUP);
            dlnb.add_item_type(mupdf::DisplayListItem_BlendMode);
            dlnb.add_item(blendmode.Union());
            dlnb.add_flags(node->flag);
            dlnb.add_color(alpha);
            return dlnb.Finish();
        } break;
        case FZ_CMD_BEGIN_TILE: {
            mupdf::DisplayListNodeBuilder dlnb(fbb);
            dlnb.add_cmd(mupdf::DisplayCommand_BEGIN_TILE);
            return dlnb.Finish();
        } break;
    }

    mupdf::DisplayListNodeBuilder dlnb(fbb);
    switch(node->cmd) {
        case FZ_CMD_END_PAGE:
            dlnb.add_cmd(mupdf::DisplayCommand_END_PAGE);
        break;
        case FZ_CMD_POP_CLIP:
            dlnb.add_cmd(mupdf::DisplayCommand_POP_CLIP);
        break;
        case FZ_CMD_END_MASK:
            dlnb.add_cmd(mupdf::DisplayCommand_END_MASK);
        break;
        case FZ_CMD_END_GROUP:
            dlnb.add_cmd(mupdf::DisplayCommand_END_GROUP);
        break;
        case FZ_CMD_END_TILE:
            dlnb.add_cmd(mupdf::DisplayCommand_END_TILE);
        break;
    }
    return dlnb.Finish();
}

JNIFUNC(jobject, MuDisplayList, nFlattern)(JNIEnv *env, jclass, jlong _ctx, jlong _dl) {
    flatbuffers::FlatBufferBuilder fbb;
    fz_context* ctx = ctx_cast(_ctx);
    fz_display_list* dl = dl_cast(_dl);
    fz_display_node_s* node;

    std::vector<flatbuffers::Offset<mupdf::DisplayListNode>> nodes;
    for(node = dl->first; node != nullptr; node = node->next) {
        nodes.push_back(createNode(fbb, ctx, node));
    }
    fbb.Finish(CreateDisplayList(fbb, fbb.CreateVector(nodes)));

    /*if(true) {
        flatbuffers::Parser parser;
        std::string fbs;
        flatbuffers::LoadFile("/sdcard/Download/mupdf.fbs", false, &fbs);
        parser.Parse(fbs.c_str());
        std::string buffer;
        flatbuffers::GenerateText(parser, fbb.GetBufferPointer(), 2, &buffer);
        flatbuffers::SaveFile("/sdcard/Download/dl_debug.json", buffer, false);
    }*/

    jbyteArray buffer = env->NewByteArray(fbb.GetSize());
    static_assert(sizeof(jbyte) == sizeof(uint8_t), "oops");
    env->SetByteArrayRegion(buffer, 0, fbb.GetSize(), (const jbyte*)fbb.GetBufferPointer());
    return buffer;
}

