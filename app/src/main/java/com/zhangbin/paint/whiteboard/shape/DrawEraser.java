package com.zhangbin.paint.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.zhangbin.paint.util.OperationUtils;

public class DrawEraser extends DrawBrush {
    public DrawEraser() {
        setDrawType(0);
    }

    public final void draw(Canvas canvas) {
        this.paint.setAlpha(0);
//        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //这两个方法一起使用才能出现橡皮擦效果
        this.paint.setColor(Color.TRANSPARENT);
        this.paint.setStrokeWidth(OperationUtils.getInstance().mCurrentEraserSize);
        super.draw(canvas);
    }


}
