package com.zhangbin.paint.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zhangbin.paint.beans.OrderBean;

import java.util.List;

//画图基类
public abstract class BaseDraw implements Cloneable {
    public Paint paint;
    private int drawType;
    public String id = "";
    public boolean isShow = true;
    private int color;
    public float scaleRatio = 1.0F;
    public float strokeWidth;
    public List<OrderBean.DataBean> dataList;

    float offsetX;
    float offsetY;
    float width;

    public BaseDraw() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setColor(Color.RED);
    }

    //移动
    public void moveOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    //移动
    public abstract void moveTo(float x, float y);

    public BaseDraw(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return this.width;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
    }

    public int getDrawType() {
        return this.drawType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean getIsShow() {
        return this.isShow;
    }

    public void setColor(int color) {
        this.color = color;
        if (this.paint != null) {
            this.paint.setColor(color);
        }
    }

    public int getColor() {
        return this.color;
    }

    public void setScaleRatio(float scaleRatio) {
        if ((this.scaleRatio != scaleRatio) && (this.paint != null)) {
            this.scaleRatio = scaleRatio;
            this.paint.setStrokeWidth(this.strokeWidth * scaleRatio);
        }
    }

    public float getScaleRatio() {
        return this.scaleRatio;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        if (this.paint != null) {
            this.paint.setStrokeWidth(strokeWidth * this.scaleRatio);
        }
    }


    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setAlpha(int paramInt) {
        if (this.paint != null) {
            this.paint.setAlpha(paramInt);
        }
    }

    public int getAlpha() {
        return 0;
    }


    public void setDrawList(List<String> paramList) {
    }

    public abstract void draw(Canvas canvas);

    public abstract void explainOrder(OrderBean orderBean)
            throws IllegalArgumentException;


    public static int convertAlpha(Float alpha) {
        return (int) (alpha.floatValue() * 255.0F);
    }

    @Override
    public  Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}