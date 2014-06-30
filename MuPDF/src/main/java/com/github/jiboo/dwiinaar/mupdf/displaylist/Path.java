// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class Path extends Table {
    public Path __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public Point begin() {
        return begin(new Point());
    }

    public Point begin(Point obj) {
        int o = __offset(4);
        return o != 0 ? obj.__init(o + bb_pos, bb) : null;
    }

    public PathNode nodes(int j) {
        return nodes(new PathNode(), j);
    }

    public PathNode nodes(PathNode obj, int j) {
        int o = __offset(6);
        return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null;
    }

    public int nodesLength() {
        int o = __offset(6);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static void startPath(FlatBufferBuilder builder) {
        builder.startObject(2);
    }

    public static void addBegin(FlatBufferBuilder builder, int begin) {
        builder.addStruct(0, begin, 0);
    }

    public static void addNodes(FlatBufferBuilder builder, int nodes) {
        builder.addOffset(1, nodes, 0);
    }

    public static void startNodesVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static int endPath(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

