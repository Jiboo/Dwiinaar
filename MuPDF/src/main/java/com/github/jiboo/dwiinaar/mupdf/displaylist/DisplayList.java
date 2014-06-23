// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import flatbuffers.FlatBufferBuilder;
import flatbuffers.Table;

public class DisplayList extends Table {
    public static DisplayList getRootAsDisplayList(ByteBuffer _bb, int offset) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return (new DisplayList()).__init(_bb.getInt(offset) + offset, _bb);
    }

    public DisplayList __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public DisplayListNode nodes(int j) {
        return nodes(new DisplayListNode(), j);
    }

    public DisplayListNode nodes(DisplayListNode obj, int j) {
        int o = __offset(4);
        return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null;
    }

    public int nodesLength() {
        int o = __offset(4);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static void startDisplayList(FlatBufferBuilder builder) {
        builder.startObject(1);
    }

    public static void addNodes(FlatBufferBuilder builder, int nodes) {
        builder.addOffset(0, nodes, 0);
    }

    public static void startNodesVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static int endDisplayList(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

