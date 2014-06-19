// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class BlendMode extends Table {
    public BlendMode __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public int mode() {
        int o = __offset(4);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public static void startBlendMode(FlatBufferBuilder builder) {
        builder.startObject(1);
    }

    public static void addMode(FlatBufferBuilder builder, int mode) {
        builder.addInt(0, mode, 0);
    }

    public static int endBlendMode(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

