package com.zhangbin.paint.whiteboard.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;

/**
 * 画文字
 */
public final class DrawText
        extends BaseShape {
    private String text;
    private float size = 25;
    private float x = 0;
    private float y = 0;
    private float height = 10;
    private Context n;

    public DrawText() {
        setDrawType(5);
    }

    private DrawText(String s, int y, int x) {
        this();
        this.text = s;
        this.y = y;
        this.x = x;

    }

    @Override
    public void moveTo(float x, float y) {
        super.moveOffset(x - this.x, y - this.y);
        this.x = this.x + this.offsetX;
        this.y = this.y + this.offsetY;

    }

    public DrawText(Context context, String s, int y, int x, Paint paint) {
        this(s, y, x);
        this.paint = new Paint(paint);
        this.n = context;
    }

    public final void size(float size) {
        this.size = size;
        this.paint.setTextSize(this.size);
    }

    public final void draw(Canvas canvas) {
        this.paint.setTextSize(this.size * this.scaleRatio);
        this.paint.setSubpixelText(true);
//        canvas.drawText(this.text, this.x * this.scaleRatio, this.y * this.scaleRatio + this.size * this.scaleRatio, this.paint);
        this.lineBreak(canvas, this.text.trim(), 0);
    }


    /**
     * @param canvas
     * @param str
     * @param height
     */
    private void lineBreak(Canvas canvas, String str, float height) {
        //计算当前宽度(width)能显示多少个汉字
        //以主心为坐标系，取宽度除以2
        int subIndex = this.paint.breakText(str, 0, str.length(), true, (canvas.getWidth() >> 1) - this.x * this.scaleRatio, null);
        //截取可以显示的汉字
        String mytext = str.substring(0, subIndex);
        canvas.drawText(mytext, this.x * this.scaleRatio, this.y * this.scaleRatio + this.size * this.scaleRatio + height, this.paint);

        //计算剩下的汉字
        String ss = str.substring(subIndex);
        if (ss.length() > 0) {
            //行距 为高度0.2倍
            lineBreak(canvas, ss, height + this.size * this.scaleRatio + this.size * this.scaleRatio * 0.2f);
        }
    }

//    public void onDraw1(Canvas canvas)
//    {
//        int MARGIN = 1;
//        int BORDER_WIDTH = 1;
//
//        Paint p = new Paint();
//        p.setAntiAlias(true);
//        p.setTextSize(12);
//        p.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
//        p.setSubpixelText(true);
//
//        RectF rect = getRect();
//
//
//        float maxWidth = rect.width() - MARGIN - BORDER_WIDTH * 2;
//
//        String str = getText();
//        char[] chars = str.toCharArray();
//        int nextPos = p.breakText(chars, 0, chars.length, maxWidth, null);
//        str = str.substring(0, nextPos);
//
//        float textX = MARGIN + BORDER_WIDTH;
//        float textY = (float) (Math.abs(p.getFontMetrics().ascent) + BORDER_WIDTH + MARGIN);
//
//        canvas.drawText(str, textX, textY, p);
//
//        p.setStrokeWidth(BORDER_WIDTH);
//        p.setStyle(Paint.Style.STROKE);
//
//        canvas.drawRect(rect, p);
//    }


    public final void explainOrder(OrderBean orderBean) {
        super.explainOrder(orderBean);
        text = orderBean.getText();
        x = orderBean.getX();
        y = orderBean.getY();
        //window转安卓字体系数2.22

        this.size = 1.35f* OperationUtils.getInstance().mCurrentTextSize;
        this.paint.setColor(OperationUtils.getInstance().mCurrentTextColor);

    }
}




