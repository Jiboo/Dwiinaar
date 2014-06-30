// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class Image extends Table {
    public Image __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public int id() {
        int o = __offset(4);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public int maskId() {
        int o = __offset(6);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public static void startImage(FlatBufferBuilder builder) {
        builder.startObject(2);
    }

    public static void addId(FlatBufferBuilder builder, int id) {
        builder.addInt(0, id, 0);
    }

    public static void addMaskId(FlatBufferBuilder builder, int maskId) {
        builder.addInt(1, maskId, 0);
    }

    public static int endImage(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

