#ifndef __GLOBAL_H__
#define __GLOBAL_H__

    #include <jni.h>
    #include <android/log.h>
    extern "C" {
        #include "mupdf/pdf.h"
    }

    #define JNIFUNC(rtype, clazz, function) extern "C" JNIEXPORT rtype JNICALL Java_com_github_jiboo_dwiinaar_mupdf_##clazz##_##function

    #define TRY(ctx) fz_try(ctx_cast(ctx))
    #define CATCH(ctx) fz_catch(ctx_cast(ctx))
    #define ALWAYS(ctx) fz_always(ctx_cast(ctx))
    #define THROW2JAVA(env, ctx) env->ThrowNew(glue_cast(ctx)->MuPDFException, ctx_cast(ctx)->error->message);

    #define LOG(...) __android_log_print(ANDROID_LOG_INFO, "MuPDF", __VA_ARGS__)

    struct ContextGlue {
        fz_context* ctx;

        jclass RectF, Bitmap, Point;
        jfieldID RectF_left, RectF_top, RectF_right, RectF_bottom;
        jfieldID Bitmap_mWidth, Bitmap_mHeight;
        jfieldID Point_x, Point_y;

        jclass MuDocument;
        jfieldID MuDocument_dPageCount, MuDocument_dNeedsPassword;

        jclass MuPDFException;
    };

    inline ContextGlue* glue_cast(const jlong ctx) {
        return reinterpret_cast<ContextGlue*>(ctx);
    }

    inline fz_context* ctx_cast(const jlong ctx) {
        return glue_cast(ctx)->ctx;
    }

    inline fz_document* doc_cast(const jlong doc) {
        return reinterpret_cast<fz_document*>(doc);
    }

    inline fz_page* page_cast(const jlong page) {
        return reinterpret_cast<fz_page*>(page);
    }

    inline fz_display_list* dl_cast(const jlong dl) {
        return reinterpret_cast<fz_display_list*>(dl);
    }

#endif