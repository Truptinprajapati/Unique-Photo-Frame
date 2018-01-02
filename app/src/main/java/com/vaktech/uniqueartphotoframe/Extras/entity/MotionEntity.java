package com.vaktech.uniqueartphotoframe.Extras.entity;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vaktech.uniqueartphotoframe.Extras.utils.MathUtils;
import com.vaktech.uniqueartphotoframe.Extras.viewmodel.Layer;
import com.vaktech.uniqueartphotoframe.Extras.viewmodel.TextLayer;


public abstract class MotionEntity {

    protected final Layer layer;

    protected final Matrix matrix = new Matrix();

    private boolean isSelected;

    protected float holyScale;

    @IntRange(from = 0)
    protected int canvasWidth;
    @IntRange(from = 0)
    protected int canvasHeight;
    private final float[] destPoints = new float[10]; // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0
    protected final float[] srcPoints = new float[10];  // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0

    @NonNull
    private Paint borderPaint = new Paint();

    public MotionEntity(@NonNull TextLayer layer,
                        @IntRange(from = 1) int canvasWidth,
                        @IntRange(from = 1) int canvasHeight) {
        this.layer = layer;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    private boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    protected void updateMatrix() {
        matrix.reset();

        float topLeftX = layer.getX() * canvasWidth;
        float topLeftY = layer.getY() * canvasHeight;

        float centerX = topLeftX + getWidth() * holyScale * 0.5F;
        float centerY = topLeftY + getHeight() * holyScale * 0.5F;

        float rotationInDegree = layer.getRotationInDegrees();
        float scaleX = layer.getScale();
        float scaleY = layer.getScale();
        if (layer.isFlipped()) {

            rotationInDegree *= -1.0F;
            scaleX *= -1.0F;
        }

        matrix.preScale(scaleX, scaleY, centerX, centerY);
        matrix.preRotate(rotationInDegree, centerX, centerY);
        matrix.preTranslate(topLeftX, topLeftY);
        matrix.preScale(holyScale, holyScale);
    }

    public float absoluteCenterX() {
        float topLeftX = layer.getX() * canvasWidth;
        return topLeftX + getWidth() * holyScale * 0.5F;
    }

    public float absoluteCenterY() {
        float topLeftY = layer.getY() * canvasHeight;

        return topLeftY + getHeight() * holyScale * 0.5F;
    }

    public PointF absoluteCenter() {
        float topLeftX = layer.getX() * canvasWidth;
        float topLeftY = layer.getY() * canvasHeight;

        float centerX = topLeftX + getWidth() * holyScale * 0.5F;
        float centerY = topLeftY + getHeight() * holyScale * 0.5F;

        return new PointF(centerX, centerY);
    }

    public void moveToCanvasCenter() {
        moveCenterTo(new PointF(canvasWidth * 0.5F, canvasHeight * 0.5F));
    }

    public void moveCenterTo(PointF moveToCenter) {
        PointF currentCenter = absoluteCenter();
        layer.postTranslate(1.0F * (moveToCenter.x - currentCenter.x) / canvasWidth,
                1.0F * (moveToCenter.y - currentCenter.y) / canvasHeight);
    }

    private final PointF pA = new PointF();
    private final PointF pB = new PointF();
    private final PointF pC = new PointF();
    private final PointF pD = new PointF();

    public boolean pointInLayerRect(PointF point) {

        updateMatrix();
        matrix.mapPoints(destPoints, srcPoints);

        pA.x = destPoints[0];
        pA.y = destPoints[1];
        pB.x = destPoints[2];
        pB.y = destPoints[3];
        pC.x = destPoints[4];
        pC.y = destPoints[5];
        pD.x = destPoints[6];
        pD.y = destPoints[7];

        return MathUtils.pointInTriangle(point, pA, pB, pC) || MathUtils.pointInTriangle(point, pA, pD, pC);
    }
    public final void draw(@NonNull Canvas canvas, @Nullable Paint drawingPaint) {
        updateMatrix();
        canvas.save();
        drawContent(canvas, drawingPaint);

        if (isSelected()) {

            int storedAlpha = borderPaint.getAlpha();
            if (drawingPaint != null) {
                borderPaint.setAlpha(drawingPaint.getAlpha());
            }
            drawSelectedBg(canvas);

            borderPaint.setAlpha(storedAlpha);
        }
        canvas.restore();
    }

    private void drawSelectedBg(Canvas canvas) {
        matrix.mapPoints(destPoints, srcPoints);
        //noinspection Range
        canvas.drawLines(destPoints, 0, 8, borderPaint);
        //noinspection Range
        canvas.drawLines(destPoints, 2, 8, borderPaint);
    }

    @NonNull
    public TextLayer getLayer() {
        return (TextLayer) layer;
    }

    public void setBorderPaint(@NonNull Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    protected abstract void drawContent(@NonNull Canvas canvas, @Nullable Paint drawingPaint);

    public abstract int getWidth();

    public abstract int getHeight();

    public void release() {
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            release();
        } finally {

            super.finalize();
        }
    }
}