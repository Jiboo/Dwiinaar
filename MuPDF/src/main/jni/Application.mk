APP_PLATFORM=android-9
APP_ABI := x86
#APP_ABI := all

ifdef NDK_PROFILER
APP_OPTIM := debug
APP_CFLAGS := -O2
else
APP_OPTIM := release
endif

ifdef V8_BUILD
APP_STL := stlport_static
endif

ifdef MEMENTO
APP_CFLAGS += -DMEMENTO -DMEMENTO_LEAKONLY
endif

NDK_TOOLCHAIN_VERSION=clang3.4
