LOCAL_PATH := $(call my-dir)
TOP_LOCAL_PATH := $(LOCAL_PATH)

MUPDF_ROOT := $(LOCAL_PATH)/../../../mupdf

ifdef NDK_PROFILER
include android-ndk-profiler.mk
endif

include $(TOP_LOCAL_PATH)/Core.mk
include $(TOP_LOCAL_PATH)/ThirdParty.mk

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
	jni/andprof \
	$(MUPDF_ROOT)/include \
	$(MUPDF_ROOT)/source/fitz \
	$(MUPDF_ROOT)/source/pdf
LOCAL_CFLAGS := -std=c++11
LOCAL_MODULE    := mupdf
LOCAL_SRC_FILES := $(wildcard $(LOCAL_PATH)/*.cpp)
LOCAL_STATIC_LIBRARIES := mupdfcore mupdfthirdparty
ifdef NDK_PROFILER
LOCAL_CFLAGS += -pg -DNDK_PROFILER
LOCAL_STATIC_LIBRARIES += andprof
else
endif

LOCAL_LDLIBS    := -lm -llog -ljnigraphics
ifdef SSL_BUILD
LOCAL_LDLIBS	+= -L$(MUPDF_ROOT)/thirdparty/openssl/android -lcrypto -lssl
endif

LOCAL_SRC_FILES := $(addprefix ../, $(LOCAL_SRC_FILES))

include $(BUILD_SHARED_LIBRARY)
