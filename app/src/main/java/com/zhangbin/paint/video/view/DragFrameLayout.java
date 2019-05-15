package com.zhangbin.paint.video.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.zhangbin.paint.R;


public class DragFrameLayout extends FrameLayout {

    private String TAG = "--DragFrameLayout--";
    private ViewDragHelper dragHelper;
    private int screenWidth,screenHeight;
    private int statusType = 0;//0无 随便移动   1靠左  2靠右 0靠左右
    private float showPercent = 1;
    private int leftBound, rightBound, topBound, bottomBound;
    private int curLeft,curTop;
    private int width, height;
    private int finalLeft = -1;
    private int finalTop = -1;
    private TypedArray typedArray;
    private  int viewWidth,viewHeight;
    private boolean isDrag = false;
    public DragFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.DraggableFrameLayout);
        statusType = typedArray.getInt(R.styleable.DraggableFrameLayout_direction, 0);//0
        showPercent = typedArray.getFloat(R.styleable.DraggableFrameLayout_showPercent, 1);//1.0
        getScreenInformation(context);
        init();
    }

    /**
     * 设置是否可以滑动
     * @param isDrag
     */
    public void setIsDrag(boolean isDrag){
        this.isDrag = isDrag;
    }

    /**
     * 得到是否可以滑动
     * @return
     */
    public boolean getIsDrag(){
        return this.isDrag;
    }
    private void init() {
        dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            /**
             * 是否可以滑动
             * @param child
             * @param pointerId
             * @return
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return getIsDrag();
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return Math.min(Math.max(left, leftBound), width);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return Math.min(Math.max(top, topBound), height);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return width;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return height;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                viewWidth = releasedChild.getWidth();
                viewHeight = releasedChild.getHeight();
                curLeft = releasedChild.getLeft();
                curTop = releasedChild.getTop();
                finalTop = curTop < 0 ? 0 : curTop;
                finalLeft = curLeft < 0 ? 0 : curLeft;
                if ((finalTop + viewHeight) > height) {
                    finalTop = height - viewHeight;
                }

                if ((finalLeft + viewWidth) > width) {
                    finalLeft = width - viewWidth;
                }
                switch (statusType) {
                    case 0://无
                        break;
                    case 1://左
                        finalLeft = -(int) (viewWidth * (1 - showPercent));
                        break;
                    case 2://右
                        finalLeft = width - (int) (viewWidth * showPercent);
                        break;
                    case 3://左右
                        finalLeft = -(int) (viewWidth * (1 - showPercent));
                        if ((curLeft + viewWidth / 2) > width / 2) {
                            finalLeft = width - (int) (viewWidth * showPercent);
                        }
                        break;
                }
                dragHelper.settleCapturedViewAt(finalLeft, finalTop);
                invalidate();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return isTouchChildView(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    private boolean isTouchChildView(MotionEvent ev) {
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            rect.set((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getWidth(), (int) view.getY() + view.getHeight());
            if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                return true;
            }
        }
        return false;
    }

    private void getScreenInformation(Context context) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        width = screenWidth;
        height = screenHeight;
    }
}
