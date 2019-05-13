package com.zhangbin.paint.whiteboard.shape;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;

/**
 * 画带点线
 */
public final class DrawDotLine extends DrawLine {
    public DrawDotLine() {
        setDrawType(6);
    }

    public final void setStrokeWidth(float strokeWidth) {
        super.setStrokeWidth(strokeWidth);
        setDot();
    }

    public final void setScaleRatio(float scaleRatio) {
        super.setScaleRatio(scaleRatio);
        setDot();
    }


    private void setDot() {

        float f1 = (8.0F + this.strokeWidth) * this.scaleRatio;
        float f2 = (2.0F + this.strokeWidth * 3.0F) * this.scaleRatio;
        DashPathEffect localDashPathEffect = new DashPathEffect(new float[]{f1, f2}, 1.0F);
        if (this.paint != null) {
            this.paint.setStyle(Paint.Style.STROKE);
//            this.paint.setARGB(255, 0, 0, 0);

            this.paint.setPathEffect(localDashPathEffect);
        }
    }

    public final void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        this.strokeWidth = OperationUtils.getInstance().mCurrentPenSize;
        this.paint.setColor(OperationUtils.getInstance().mCurrentPenColor);
        this.paint.setStrokeWidth(OperationUtils.getInstance().mCurrentPenSize);
    }
}


