// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class Matrix extends Struct {
    public Matrix __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public float a() {
        return bb.getFloat(bb_pos + 0);
    }

    public float b() {
        return bb.getFloat(bb_pos + 4);
    }

    public float c() {
        return bb.getFloat(bb_pos + 8);
    }

    public float d() {
        return bb.getFloat(bb_pos + 12);
    }

    public float e() {
        return bb.getFloat(bb_pos + 16);
    }

    public float f() {
        return bb.getFloat(bb_pos + 20);
    }

    public static int createMatrix(FlatBufferBuilder builder, float a, float b, float c, float d, float e, float f) {
        builder.prep(4, 0);
        builder.putFloat(f);
        builder.putFloat(e);
        builder.putFloat(d);
        builder.putFloat(c);
        builder.putFloat(b);
        builder.putFloat(a);
        return builder.offset();
    }
};

