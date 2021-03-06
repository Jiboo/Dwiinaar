// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class PathNode extends Table {
    public PathNode __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public byte cmd() {
        int o = __offset(4);
        return o != 0 ? bb.get(o + bb_pos) : 0;
    }

    public Point coord(int j) {
        return coord(new Point(), j);
    }

    public Point coord(Point obj, int j) {
        int o = __offset(6);
        return o != 0 ? obj.__init(__vector(o) + j * 8, bb) : null;
    }

    public int coordLength() {
        int o = __offset(6);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static void startPathNode(FlatBufferBuilder builder) {
        builder.startObject(2);
    }

    public static void addCmd(FlatBufferBuilder builder, byte cmd) {
        builder.addByte(0, cmd, 0);
    }

    public static void addCoord(FlatBufferBuilder builder, int coord) {
        builder.addOffset(1, coord, 0);
    }

    public static void startCoordVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static int endPathNode(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

