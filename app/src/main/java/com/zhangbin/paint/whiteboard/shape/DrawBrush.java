package com.zhangbin.paint.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Path;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;


/**
 * 笔刷
 */
public class DrawBrush
        extends BaseShape {
    private Path path = new Path();

    public DrawBrush() {
        setDrawType(0);
    }

    @Override
    public void moveTo(float x, float y) {

    }


    public  void draw(Canvas canvas) {
        if ((this.dataList != null) && (this.dataList.size() > 0)) {
            this.path.reset();
            OrderBean.DataBean start = this.dataList.get(0);
            this.path.moveTo(start.getX() * this.scaleRatio, start.getY() * this.scaleRatio);
            for (int i = 0; i <= this.dataList.size() - 1; i++) {
                OrderBean.DataBean end = this.dataList.get(i);
                this.path.lineTo(end.getX() * this.scaleRatio, end.getY() * this.scaleRatio);
            }
        }
        canvas.drawPath(this.path, this.paint);
    }

    public final void linTo(float x, float y) {
        this.path.lineTo(x, y);
    }

    public final void move(float x, float y) {
        this.path.moveTo(x, y);
    }

    public final void linTo(float x1, float y1, float x2, float y2) {
        this.path.quadTo(x1, y1, x2, y2);
    }


    public final void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        this.paint.setColor(OperationUtils.getInstance().mCurrentPenColor);
        this.strokeWidth = OperationUtils.getInstance().mCurrentPenSize;
        this.paint.setStrokeWidth(OperationUtils.getInstance().mCurrentPenSize);
    }
}


