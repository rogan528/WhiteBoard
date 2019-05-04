package com.zhangbin.paint.whiteboard;



import com.zhangbin.paint.whiteboard.shape.BaseDraw;

import java.util.List;
public interface CallBack {
    public void onDrawBack(List<BaseDraw> fabricViewDataList, List<BaseDraw> undoDrawableList, List<BaseDraw> redoDrawableList, BaseDraw baseDraw);
}
