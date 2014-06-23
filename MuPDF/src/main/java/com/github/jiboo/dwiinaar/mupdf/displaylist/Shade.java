// automatically generated, do not modify

package com.github.jiboo.dwiinaar.mupdf.displaylist;

import java.nio.ByteBuffer;

import flatbuffers.FlatBufferBuilder;
import flatbuffers.Table;

public class Shade extends Table {
    public Shade __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public byte type() {
        int o = __offset(4);
        return o != 0 ? bb.get(o + bb_pos) : 0;
    }

    public Rect bbox() {
        return bbox(new Rect());
    }

    public Rect bbox(Rect obj) {
        int o = __offset(6);
        return o != 0 ? obj.__init(o + bb_pos, bb) : null;
    }

    public Matrix matrix() {
        return matrix(new Matrix());
    }

    public Matrix matrix(Matrix obj) {
        int o = __offset(8);
        return o != 0 ? obj.__init(o + bb_pos, bb) : null;
    }

    public int useBackground() {
        int o = __offset(10);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public float background(int j) {
        int o = __offset(12);
        return o != 0 ? bb.getFloat(__vector(o) + j * 4) : 0;
    }

    public int backgroundLength() {
        int o = __offset(12);
        return o != 0 ? __vector_len(o) : 0;
    }

    public int useFunction() {
        int o = __offset(14);
        return o != 0 ? bb.getInt(o + bb_pos) : 0;
    }

    public float function(int j) {
        int o = __offset(16);
        return o != 0 ? bb.getFloat(__vector(o) + j * 4) : 0;
    }

    public int functionLength() {
        int o = __offset(16);
        return o != 0 ? __vector_len(o) : 0;
    }

    public int extend(int j) {
        int o = __offset(18);
        return o != 0 ? bb.getInt(__vector(o) + j * 4) : 0;
    }

    public int extendLength() {
        int o = __offset(18);
        return o != 0 ? __vector_len(o) : 0;
    }

    public float coords(int j) {
        int o = __offset(20);
        return o != 0 ? bb.getFloat(__vector(o) + j * 4) : 0;
    }

    public int coordsLength() {
        int o = __offset(20);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static void startShade(FlatBufferBuilder builder) {
        builder.startObject(9);
    }

    public static void addType(FlatBufferBuilder builder, byte type) {
        builder.addByte(0, type, 0);
    }

    public static void addBbox(FlatBufferBuilder builder, int bbox) {
        builder.addStruct(1, bbox, 0);
    }

    public static void addMatrix(FlatBufferBuilder builder, int matrix) {
        builder.addStruct(2, matrix, 0);
    }

    public static void addUseBackground(FlatBufferBuilder builder, int useBackground) {
        builder.addInt(3, useBackground, 0);
    }

    public static void addBackground(FlatBufferBuilder builder, int background) {
        builder.addOffset(4, background, 0);
    }

    public static void startBackgroundVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static void addUseFunction(FlatBufferBuilder builder, int useFunction) {
        builder.addInt(5, useFunction, 0);
    }

    public static void addFunction(FlatBufferBuilder builder, int function) {
        builder.addOffset(6, function, 0);
    }

    public static void startFunctionVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static void addExtend(FlatBufferBuilder builder, int extend) {
        builder.addOffset(7, extend, 0);
    }

    public static void startExtendVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static void addCoords(FlatBufferBuilder builder, int coords) {
        builder.addOffset(8, coords, 0);
    }

    public static void startCoordsVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems);
    }

    public static int endShade(FlatBufferBuilder builder) {
        return builder.endObject();
    }
};

