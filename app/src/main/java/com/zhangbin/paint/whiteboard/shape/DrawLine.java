package com.zhangbin.paint.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Path;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;

/**
 * 画直线
 */
public class DrawLine
        extends BaseShape {
    private Path path = new Path();

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public DrawLine() {
        setDrawType(1);
    }

    public void draw(Canvas canvas) {
        this.path.reset();
        this.move(this.x1 * this.scaleRatio, this.y1 * this.scaleRatio);
        this.toLine(this.x2 * this.scaleRatio, this.y2 * this.scaleRatio);
        canvas.drawPath(this.path, this.paint);
    }


    @Override
    public void moveTo(float x, float y) {
        super.moveOffset(x - this.x1, y - this.y1);
        this.x1 = this.x1 + offsetX;
        this.y1 = this.y1 + offsetY;
        this.x2 = this.x2 + offsetX;
        this.y2 = this.y2 + this.offsetY;
    }


    public final void toLine(float x, float y) {
        this.path.lineTo(x, y);
    }

    public final void move(float x, float y) {
        this.path.moveTo(x, y);
    }

    public final void toLine() {
        this.path.reset();
    }


    public void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        this.x1 = orderBean.getX1();
        this.y1 = orderBean.getY1();
        this.x2 = orderBean.getX2();
        this.y2 = orderBean.getY2();
        this.strokeWidth = OperationUtils.getInstance().mCurrentPenSize;
        this.paint.setColor(OperationUtils.getInstance().mCurrentShapeColor);
        this.paint.setStrokeWidth(OperationUtils.getInstance().mCurrentPenSize);
    }
}
