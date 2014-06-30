package com.github.jiboo.dwiinaar.mupdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.jiboo.dwiinaar.mupdf.displaylist.DisplayCommand;
import com.github.jiboo.dwiinaar.mupdf.displaylist.DisplayList;
import com.github.jiboo.dwiinaar.mupdf.displaylist.DisplayListItem;
import com.github.jiboo.dwiinaar.mupdf.displaylist.DisplayListNode;
import com.github.jiboo.dwiinaar.mupdf.displaylist.Matrix;
import com.github.jiboo.dwiinaar.mupdf.displaylist.Path;
import com.github.jiboo.dwiinaar.mupdf.displaylist.PathCommand;
import com.github.jiboo.dwiinaar.mupdf.displaylist.PathNode;
import com.github.jiboo.dwiinaar.mupdf.displaylist.Point;

import java.nio.ByteBuffer;

import flatbuffers.Table;

public class MuDisplayList {
    protected long dNativePointer;
    protected long dNativeContext;

    protected DisplayList dJavaList = null;

    protected static native void nRender(long ctx, long dl, float scale, RectF src, Bitmap dst);

    protected static native byte[] nFlattern(long ctx, long dl);

    protected static native void nFree(long ctx, long dl);

    protected MuDisplayList(long ctx, long dl) {
        dNativeContext = ctx;
        dNativePointer = dl;
    }

    public void recycle() {
        nFree(dNativeContext, dNativePointer);
        dNativePointer = 0;
    }

    public void render(float scale, @NonNull RectF src, @NonNull Bitmap dst) {
        if (dNativePointer != 0)
            nRender(dNativeContext, dNativePointer, scale, src, dst);
        else
            throw new IllegalStateException("Display list is recycled");
    }

    public void flattern() throws MuPDFException {
        final byte[] buff = nFlattern(dNativeContext, dNativePointer);
        dJavaList = DisplayList.getRootAsDisplayList(ByteBuffer.wrap(buff), 0);
    }

    public void render(@NonNull Canvas canvas) {
        if (dJavaList == null)
            throw new IllegalStateException("DisplayList not flatterned");
        for (int i = 0; i < dJavaList.nodesLength(); i++) {
            renderNode(canvas, dJavaList.nodes(i));
        }
    }

    private void renderNode(@NonNull Canvas canvas, DisplayListNode node) {
        switch (node.cmd()) {
            case DisplayCommand.FILL_PATH:
                renderFillPath(canvas, node);
                break;
        }
    }

    private void renderFillPath(@NonNull Canvas canvas, final DisplayListNode node) {
        if (node.itemType() != DisplayListItem.Path)
            throw new RuntimeException("Fill path with item type != path");
        if (node.colorLength() != 1)
            throw new RuntimeException("Wrong color number");

        //canvas.save(Canvas.MATRIX_SAVE_FLAG);
        //canvas.concat(parseMatrix(node.ctm(new Matrix())));
        canvas.drawPath(parsePath((Path) node.item(new Path())), new Paint() {{
            setColor(node.color(0));
            setStyle(Paint.Style.FILL);
        }});
        //canvas.restore();
    }

    private android.graphics.Matrix parseMatrix(Matrix ctm) {
        if (ctm == null)
            return null;
        android.graphics.Matrix result = new android.graphics.Matrix();
        float[] values = new float[9];
        values[android.graphics.Matrix.MTRANS_X] = ctm.e();
        values[android.graphics.Matrix.MTRANS_Y] = ctm.f();
        values[android.graphics.Matrix.MSCALE_X] = ctm.a();
        values[android.graphics.Matrix.MSCALE_Y] = ctm.d();
        values[android.graphics.Matrix.MSKEW_X] = ctm.c();
        values[android.graphics.Matrix.MSKEW_Y] = ctm.b();
        result.setValues(values);

        return result;
    }

    private android.graphics.Path parsePath(Path path) {
        final PathNode curNode = new PathNode();
        final Point[] points = new Point[3];
        for (int i = 0; i < points.length; i++)
            points[i] = new Point();
        final android.graphics.Path androidPath = new android.graphics.Path();
        final Point begin = path.begin();

        if (begin == null)
            throw new RuntimeException("Can't parse begin");

        androidPath.moveTo(begin.x(), begin.y());

        for (int i = 0; i < path.nodesLength(); i++) {
            path.nodes(curNode, i);
            switch (curNode.cmd()) {
                case PathCommand.MOVETO:
                    if (curNode.coordLength() != 1)
                        throw new RuntimeException("Wrong coords size for MOVETO");
                    curNode.coord(points[0], 0);
                    androidPath.moveTo(points[0].x(), points[0].y());
                    break;
                case PathCommand.LINETO:
                    if (curNode.coordLength() != 1)
                        throw new RuntimeException("Wrong coords size for LINETO");
                    curNode.coord(points[0], 0);
                    androidPath.lineTo(points[0].x(), points[0].y());
                    break;
                case PathCommand.CURVETO:
                    if (curNode.coordLength() != 3)
                        throw new RuntimeException("Wrong coords size for CURVETO");
                    curNode.coord(points[0], 0);
                    curNode.coord(points[1], 1);
                    curNode.coord(points[2], 2);
                    androidPath.cubicTo(points[0].x(), points[0].y(), points[1].x(), points[1].y(), points[2].x(), points[2].y());
                    break;
                case PathCommand.CLOSE:
                    if (curNode.coordLength() != 0)
                        throw new RuntimeException("Wrong coords size for CLOSE");
                    androidPath.close();
                    break;
            }
        }
        return androidPath;
    }
}
