// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class Rect extends Struct {
    public Rect __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public float x0() {
        return bb.getFloat(bb_pos + 0);
    }

    public float y0() {
        return bb.getFloat(bb_pos + 4);
    }

    public float x1() {
        return bb.getFloat(bb_pos + 8);
    }

    public float y1() {
        return bb.getFloat(bb_pos + 12);
    }

    public static int createRect(FlatBufferBuilder builder, float x0, float y0, float x1, float y1) {
        builder.prep(4, 0);
        builder.putFloat(y1);
        builder.putFloat(x1);
        builder.putFloat(y0);
        builder.putFloat(x0);
        return builder.offset();
    }
};

