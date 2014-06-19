// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.*;
import java.lang.*;
import java.util.*;

import flatbuffers.*;

public class DisplayListNode extends Table {
    public DisplayListNode __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public byte cmd() {
        int o = __offset(4);
        return o != 0 ? bb.get(o + bb_pos) : 0;
    }

    public Rect rect() {
        return rect(new Rect());
    }

    public Rect rect(Rect obj) {
        int o = __offset(6);
        return o != 0 ? obj.__init(o + bb_pos, bb) : null;
    }

    public byte itemType() {
        int o = __offset(8);
        return o != 0 ? bb.get(o + bb_pos) : 0;
    }

    public Table item(Table obj) {
        int o = __offset(10);
        return o != 0 ? __union(obj, o) : null;
    }

    public StrokeState strokeState() {
        return strokeState(new StrokeState());
    }

    public StrokeState strokeState(StrokeState obj) {
        int o = __offset(12);
        return o != 0 ? obj.__init(__indirect(o + bb_pos), bb) : null;
    }

    public int flags() {
        int o = __offset(14);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public Matrix ctm() {
        return ctm(new Matrix());
    }

    public Matrix ctm(Matrix obj) {
        int o = __offset(16);
        return o != 0 ? obj.__init(o + bb_pos, bb) : null;
    }

    public float alpha() {
        int o = __offset(18);
        return o != 0 ? bb.getFloat(o + bb_pos) : 0;
    }

    public float color(int j) {
        int o = __offset(20);
        return o != 0 ? bb.getFloat(__vector(o) + j * 4) : 0;
    }

    public int colorLength() {
        int o = __offset(20);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static void startDisplayListNode(FlatBufferBuilder builder) {
        builder.startObject(9);
    }

    public static void addCmd(FlatBufferBuilder builder, byte cmd) {
        builder.addByte(0, cmd, 0);
    }

    public static void addRect(FlatBufferBuilder builder, int rect) {
        builder.addStruct(1, rect, 0);
    }

    public static void addItemType(FlatBufferBuilder builder, byte itemType) {
        builder.addByte(2, itemType, 0);
    }

    public static void addItem(FlatBufferBuilder builder, int item) {
        builder.addOffset(3, item, 0);
    }

    public static void addStrokeState(FlatBufferBuilder builder, int strokeState) {
        builder.addOffset(4, strokeState, 0);
    }

    public static void addFlags(FlatBufferBuilder builder, int flags) {
        builder.addInt(5, flags, 0);
    }

    public static void addCtm(FlatBufferBuilder builder, int ctm) {
        builder.addStruct(6, ctm, 0);
    }

    public static void addAlpha(FlatBufferBuilder builder, float alpha) {
        builder.addFloat(7, alpha, 0);
    }

    public static void addColor(FlatBufferBuilder builder, int color) {
        builder.addOffset(8, color, 0);
    }

    public static void startColorVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static int endDisplayListNode(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

