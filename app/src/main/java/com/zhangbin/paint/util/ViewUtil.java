package com.zhangbin.paint.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by asus on 2018/1/11.
 */

public class ViewUtil {
    /**
     * 设置视频容器的位置
     */
    public static void setViewXY(View target, int x, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(target.getLayoutParams());
        margin.leftMargin = x;
        margin.topMargin = y;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        target.setLayoutParams(layoutParams);
    }

}
