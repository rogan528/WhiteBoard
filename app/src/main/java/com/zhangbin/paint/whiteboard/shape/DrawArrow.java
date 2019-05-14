package com.zhangbin.paint.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Path;

import com.zhangbin.paint.beans.OrderBean;

/**
 * 画线头
 */
public final class DrawArrow
        extends BaseShape {
    private float j;
    private float k;
    private float l;
    private float m;
    private Path path;

    public DrawArrow() {
        setDrawType(0);
    }

    @Override
    public void moveTo(float x, float y) {

    }

    public final void draw(Canvas canvas) {
        int var10002 = (int) (this.j * this.scaleRatio);
        int var10003 = (int) (this.k * this.scaleRatio);
        int var10004 = (int) (this.l * this.scaleRatio);
        int endY = (int) (this.m * this.scaleRatio);
        int endX = var10004;
        int startY = var10003;
        int startX = var10002;
        float var7;
        double var9 = (double) (var7 = this.paint.getStrokeWidth() >= 7.2F ? this.paint.getStrokeWidth() : 5.0F) * Math.sqrt(3.0D);
        double var11;
        double var17 = Math.atan((var11 = (double) var7) / var9);
        double var19 = Math.sqrt(var11 * var11 + var9 * var9);
        double[] var31 = a(endX - startX, endY - startY, var17, true, var19);
        double[] var8 = a(endX - startX, endY - startY, -var17, true, var19);
        double var23 = (double) endX - var31[0];
        double var25 = (double) endY - var31[1];
        double var27 = (double) endX - var8[0];
        double var29 = (double) endY - var8[1];
        int var32 = (new Double(var23)).intValue();
        int var33 = (new Double(var25)).intValue();
        int var34 = (new Double(var27)).intValue();
        int var10 = (new Double(var29)).intValue();
        this.path = new Path();
        this.path.moveTo((float) endX, (float) endY);
        this.path.lineTo((float) var32, (float) var33);
        this.path.lineTo((float) var34, (float) var10);
        this.path.close();
        canvas.drawLine((float) startX, (float) startY, (float) endX, (float) endY, this.paint);
        canvas.drawPath(this.path, this.paint);
    }

    public final void a(float paramFloat1, float paramFloat2) {
        this.l = paramFloat1;
        this.m = paramFloat2;
    }

    public final void b(float paramFloat1, float paramFloat2) {
        this.j = paramFloat1;
        this.k = paramFloat2;
    }

    private static double[] a(int var0, int var1, double var2, boolean var4, double var5) {
        double[] var14 = new double[2];
        double var8 = (double) var0 * Math.cos(var2) - (double) var1 * Math.sin(var2);
        double var10 = (double) var0 * Math.sin(var2) + (double) var1 * Math.cos(var2);
        double var12 = Math.sqrt(var8 * var8 + var10 * var10);
        var8 = var8 / var12 * var5;
        var10 = var10 / var12 * var5;
        var14[0] = var8;
        var14[1] = var10;
        return var14;
    }


    public final void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
    }
}

