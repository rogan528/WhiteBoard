package com.zhangbin.paint;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.TextView;

import java.lang.reflect.Field;

public class DragTextView extends TextView {
    private int width, height;

    private int screenWidth, screenHeight;
    private int left, right, top, bottom;
    private int[] location = new int[2];
    private int margin = 0;
    private int lastX, lastY;

    public DragTextView(Context context) {
        super(context);
        getScreenInformation(context);
    }

    private void getScreenInformation(Context context) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        //screenHeight = dm.heightPixels - getStatusHeight(context) -dp2px(context, 102);
        screenHeight = dm.heightPixels;
    }

    public DragTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getScreenInformation(context);
    }

    public DragTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getScreenInformation(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                left = getLeft() + dx;
                top = getTop() + dy;
                right = getRight() + dx;
                bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }

                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }

                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (top > screenHeight) {
                    top = screenHeight;
                }

                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                if (Math.abs(dx) > 8 || Math.abs(dy) > 8) {
                    layout(left, top, right, bottom);
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //Log.e("TAG", "ACTION_UP    ");
                break;
        }
        return super.onTouchEvent(event);
    }


    /***
     * 获取状态栏高度
     *
     * @param mContext
     * @return
     */
    public static int getStatusHeight(Context mContext) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }


    public static int dp2px(Context context, int dip) {
        Resources resources = context.getResources();
        int px = Math.round(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics()));
        return px;
    }
}
