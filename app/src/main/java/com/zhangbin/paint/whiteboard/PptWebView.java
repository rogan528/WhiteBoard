package com.zhangbin.paint.whiteboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class PptWebView extends WebView {
    public PptWebView(Context context) {
        super(context);
    }

    public PptWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PptWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean zoomIn() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean zoomOut() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount()==1) {
            return super.onTouchEvent(event);
        }else {
            return false;
        }
    }
    @Override
    @Deprecated
    public boolean canZoomIn() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    @Deprecated
    public boolean canZoomOut() {
        // TODO Auto-generated method stub
        return false;
    }

}
