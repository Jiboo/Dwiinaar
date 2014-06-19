// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class StrokeState extends Table {
    public StrokeState __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public int refs() {
        int o = __offset(4);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public byte startCap() {
        int o = __offset(6);
        return o != 0 ? bb.get(o + bb_pos) : 0;
    }

    public byte dashCap() {
        int o = __offset(8);
        return o != 0 ? bb.get(o + bb_pos) : 0;
    }

    public byte endCap() {
        int o = __offset(10);
        return o != 0 ? bb.get(o + bb_pos) : 0;
    }

    public float width() {
        int o = __offset(12);
        return o != 0 ? bb.getFloat(o + bb_pos) : 0;
    }

    public float miterLimit() {
        int o = __offset(14);
        return o != 0 ? bb.getFloat(o + bb_pos) : 0;
    }

    public float dashPhase() {
        int o = __offset(16);
        return o != 0 ? bb.getFloat(o + bb_pos) : 0;
    }

    public float dashList(int j) {
        int o = __offset(18);
        return o != 0 ? bb.getFloat(__vector(o) + j * 4) : 0;
    }

    public int dashListLength() {
        int o = __offset(18);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static void startStrokeState(FlatBufferBuilder builder) {
        builder.startObject(8);
    }

    public static void addRefs(FlatBufferBuilder builder, int refs) {
        builder.addInt(0, refs, 0);
    }

    public static void addStartCap(FlatBufferBuilder builder, byte startCap) {
        builder.addByte(1, startCap, 0);
    }

    public static void addDashCap(FlatBufferBuilder builder, byte dashCap) {
        builder.addByte(2, dashCap, 0);
    }

    public static void addEndCap(FlatBufferBuilder builder, byte endCap) {
        builder.addByte(3, endCap, 0);
    }

    public static void addWidth(FlatBufferBuilder builder, float width) {
        builder.addFloat(4, width, 0);
    }

    public static void addMiterLimit(FlatBufferBuilder builder, float miterLimit) {
        builder.addFloat(5, miterLimit, 0);
    }

    public static void addDashPhase(FlatBufferBuilder builder, float dashPhase) {
        builder.addFloat(6, dashPhase, 0);
    }

    public static void addDashList(FlatBufferBuilder builder, int dashList) {
        builder.addOffset(7, dashList, 0);
    }

    public static void startDashListVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static int endStrokeState(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

