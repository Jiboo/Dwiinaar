package com.github.jiboo.dwiinaar.bitmapmanager.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public class BitmapUtils {
    public static
    int getConfigByteSize(@NonNull Bitmap.Config config) {
        switch(config) {
            case ALPHA_8:
                return 1;
            case ARGB_4444:
            case RGB_565:
                return 2;
            case ARGB_8888:
                return 4;
            default:
                throw new IllegalArgumentException("Illegal configuration");
        }
    }
}
