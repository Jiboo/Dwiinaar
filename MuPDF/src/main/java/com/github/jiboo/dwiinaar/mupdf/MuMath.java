package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Rect;
import android.graphics.RectF;

public class MuMath {
    public static final int MAX_SAFE_INT = 16777216;
    public static final int MIN_SAFE_INT = -16777216;

    public static Rect round_rect(RectF src) {
        return new Rect(
                (int) Math.min(Math.max(Math.floor(src.left + 0.001), MIN_SAFE_INT), MAX_SAFE_INT),
                (int) Math.min(Math.max(Math.floor(src.top + 0.001), MIN_SAFE_INT), MAX_SAFE_INT),
                (int) Math.min(Math.max(Math.ceil(src.right - 0.001), MIN_SAFE_INT), MAX_SAFE_INT),
                (int) Math.min(Math.max(Math.ceil(src.bottom - 0.001), MIN_SAFE_INT), MAX_SAFE_INT));
    }
}
