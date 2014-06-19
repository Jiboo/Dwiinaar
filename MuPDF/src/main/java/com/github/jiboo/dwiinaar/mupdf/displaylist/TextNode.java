// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class TextNode extends Table {
    public TextNode __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public Point translate() {
        return translate(new Point());
    }

    public Point translate(Point obj) {
        int o = __offset(4);
        return o != 0 ? obj.__init(o + bb_pos, bb) : null;
    }

    public int gid() {
        int o = __offset(6);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public int ucs() {
        int o = __offset(8);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public static void startTextNode(FlatBufferBuilder builder) {
        builder.startObject(3);
    }

    public static void addTranslate(FlatBufferBuilder builder, int translate) {
        builder.addStruct(0, translate, 0);
    }

    public static void addGid(FlatBufferBuilder builder, int gid) {
        builder.addInt(1, gid, 0);
    }

    public static void addUcs(FlatBufferBuilder builder, int ucs) {
        builder.addInt(2, ucs, 0);
    }

    public static int endTextNode(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

