// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class Text extends Table {
    public Text __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public int font() {
        int o = __offset(4);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public Matrix trm() {
        return trm(new Matrix());
    }

    public Matrix trm(Matrix obj) {
        int o = __offset(6);
        return o != 0 ? obj.__init(o + bb_pos, bb) : null;
    }

    public TextNode nodes(int j) {
        return nodes(new TextNode(), j);
    }

    public TextNode nodes(TextNode obj, int j) {
        int o = __offset(8);
        return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null;
    }

    public int nodesLength() {
        int o = __offset(8);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static void startText(FlatBufferBuilder builder) {
        builder.startObject(3);
    }

    public static void addFont(FlatBufferBuilder builder, int font) {
        builder.addInt(0, font, 0);
    }

    public static void addTrm(FlatBufferBuilder builder, int trm) {
        builder.addStruct(1, trm, 0);
    }

    public static void addNodes(FlatBufferBuilder builder, int nodes) {
        builder.addOffset(2, nodes, 0);
    }

    public static void startNodesVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static int endText(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

