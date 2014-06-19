APP_PLATFORM=android-9
APP_ABI := x86
#APP_ABI := all

APP_STL := c++_static
NDK_TOOLCHAIN_VERSION=clang3.4

ifdef NDK_PROFILER
APP_OPTIM := debug
APP_CFLAGS := -O2
else
APP_OPTIM := release
endif

ifdef MEMENTO
APP_CFLAGS += -DMEMENTO -DMEMENTO_LEAKONLY
endif

