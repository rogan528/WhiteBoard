package com.zhangbin.paint.whiteboard;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.zhangbin.paint.whiteboard.shape.BaseDraw;
import com.zhangbin.paint.whiteboard.shape.DrawMove;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 白板控制类
 * 产生画板绘制部
 */
public final class PageWhite {

    /**
     * 主要获取画板布局的宽高和进行重绘部分
     */
    private WhiteDrawView whitedrawView;

    /**
     * 两个
     */
    private DrawLayerView bFabricView;
    private DrawLayerView aFabricView;
    private HistoryOrder historyOrder;
    //撤销绘制集合
    private List<Integer> undoDrawableList;
    //回退绘制集合
    private List<Integer> redoDrawableList;
    private float scaleRatio = 1.0F;
    int imageWidth = 0;
    int imageHeight = 0;
    private int layoutWidth = 0;
    private int layoutHeight = 0;
    private int mWidth;
    private int mHeight;
    //是否是草稿纸
    private boolean isScratch = false;
    private Integer page = -1;
    //是否是客户端
    private boolean isClient = false;
    private Drawable drawable = null;


    /**
     * 构造函数
     *
     * @param b             标志
     * @param whitedrawView
     */
    public PageWhite(boolean b, WhiteDrawView whitedrawView) {
        this.whitedrawView = whitedrawView;
        this.bFabricView = whitedrawView.getDrawFabricView();
        this.aFabricView = whitedrawView.getImageFabricView();
        this.isClient = b;
        this.historyOrder = new HistoryOrder();
        this.undoDrawableList = new CopyOnWriteArrayList();
        this.redoDrawableList = new CopyOnWriteArrayList();
        this.aFabricView.setOnDrawListener(new CallBackImpl(1));
        this.bFabricView.setOnDrawListener(new CallBackImpl(2));
    }

    public final void drawObjA(final float x) {
        final int y2 = (int) (this.scaleRatio);
        if (this.whitedrawView.getScaleY() == y2) {
            return;
        }
        this.whitedrawView.post(new Runnable() {
            @Override
            public void run() {
                whitedrawView.scrollTo((int) x, y2);
            }
        });
    }

    public final void drawObjA(int toPage, BaseDraw baseDraw) {
        if ((this.historyOrder != null) && this.isClient) {
            this.historyOrder.clear(toPage, baseDraw);
        }
        if (this.page == toPage) {
            this.aFabricView.DrawLayerView(baseDraw);
        }
    }

    /**
     * 如果开始画
     *
     * @param toPage
     * @param draw
     */
    public final void drawObjB(int toPage, BaseDraw draw) {

        //如果初始为-1，并要进行绘画就跳到第一页
        if (this.page <=0) {
            ToPage(1);
        }

        Log.i("页", this.page + "");

        //转换移动为普通绘画
        if (draw.getDrawType() == 10) {
            BaseDraw d = this.converMoveTo(draw);
            if (d != null) {
                draw = d;
            }
        }

        if ((this.historyOrder != null) && (this.isClient)) {
            if (draw.getDrawType() == 0) {
                this.historyOrder.getDrawA(toPage, draw);
            } else {
                this.historyOrder.getDrawB(toPage, draw);
            }
        }

        if (this.page == toPage) {
            if (draw.getDrawType() == 0) {
                this.aFabricView.DrawLayerView(draw);
            } else {
                this.bFabricView.DrawLayerView(draw);
            }
            undoDrawableList.add(draw.getDrawType());
        }
    }


    /**
     * 转换移动指令为普通绘画指类
     *
     * @param draw
     * @return
     */
    private BaseDraw converMoveTo(BaseDraw draw) {
        if (draw == null) {
            return null;
        }
        //如果初始为-1，并要进行绘画就跳到第一页
        if (this.page == -1) {
            return null;
        }
        if (this.historyOrder == null) {
            return null;
        }

        if (!(draw instanceof DrawMove)) {
            return null;
        }

        CopyOnWriteArrayList<BaseDraw> lst = this.historyOrder.getDrawB(this.page);
        Iterator iterator = lst.iterator();
        while (iterator.hasNext()) {
            BaseDraw localBaseDraw = (BaseDraw) iterator.next();
            if (localBaseDraw.getId() != null && !"".equals(localBaseDraw.getId())
                    && draw.getId() != null && !"".equals(draw.getId())) {
                if (localBaseDraw.getId().equals(draw.getId())) {
                    DrawMove drawMove = (DrawMove) draw;

                    try {
                        BaseDraw b;
                        b = (BaseDraw) localBaseDraw.clone();
                        b.moveTo(drawMove.getX(), drawMove.getY());
                        b.setWidth(drawMove.getWidth());
                        return b;
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                        return null;
                    }


                }
            }
        }

        return null;
    }

    /**
     * 清除指定页涂鸦
     * @param pageIndex
     */
    public final void clearPageIndex(int pageIndex) {
        if (this.historyOrder != null) {
            this.historyOrder.clear(pageIndex);
        }
        if (this.page == pageIndex) {
            this.aFabricView.clear();
            this.bFabricView.clear();
        }
    }

    public final boolean drawObjB(int pageIndex) {
        if (this.historyOrder != null) {
            return this.historyOrder.exist(pageIndex);
        }
        return false;
    }

    public final void drawObjA(Drawable paramDrawable) {
        this.drawable = paramDrawable;
    }

    /**
     * 增加草稿纸
     *
     * @param pageIndex
     */
    public final void addDraftPage(int pageIndex) {
        ToPage(pageIndex);
    }
    /**
     * 跑到到指定页面
     *
     * @param pageIndex
     */
    public final void ToPage(int pageIndex) {
        boolean b = (this.page != pageIndex) || (!this.isClient);
        this.page = pageIndex;
        if (!this.isClient) {
            this.historyOrder.clear(pageIndex);
            this.undoDrawableList.clear();
            this.redoDrawableList.clear();
        }
        drawObjA(this.imageWidth, this.imageHeight);
        if (b) {
            drawObjB();
        }
        if (this.historyOrder != null) {
            drawObjA(this.historyOrder.getDrawA(pageIndex));
            drawObjB(this.historyOrder.getDrawB(pageIndex));
        }
    }

    /**
     * 设置绘画区域大小
     *
     * @param width
     * @param height
     */
    public void setWidthHeight(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
    }

    public final void drawObjA(CopyOnWriteArrayList<BaseDraw> drawLayerViews) {
        this.aFabricView.setFabricViewDataList(drawLayerViews);
    }

    public final void drawObjB(CopyOnWriteArrayList<BaseDraw> drawLayerViews) {
        this.bFabricView.setFabricViewDataList(drawLayerViews);
    }

    public final void drawObjA() {


        this.whitedrawView.postDelayed(new ToRun(this), 100L);
    }

    public final void drawObjA(int width, int height) {
        if ((width == 0) || (height == 0)) {
            return;
        }


        FrameLayout.LayoutParams drawFabricViewLayout;
        drawFabricViewLayout = (FrameLayout.LayoutParams) this.bFabricView.getLayoutParams();
        drawFabricViewLayout.gravity = Gravity.CENTER;


        float tWidth = this.mWidth;
        float tHeight = this.mHeight;
//        this.t = false;

        //是草稿纸
        if (this.isScratch) {
            this.scaleRatio = (tWidth / 800.0F);
            drawFabricViewLayout.gravity = 49;
        } else if (width < height) {
            tWidth = 0.025F * this.layoutWidth;
            tWidth = this.mWidth - tWidth;
            this.scaleRatio = (tWidth / width);
            tHeight = this.scaleRatio * height;
            drawFabricViewLayout.gravity = 49;


        } else {
            if (this.mWidth < this.layoutWidth) {
                tWidth = (float) this.mWidth / (float) width * (float) height;

                float localRatio = (float) this.layoutWidth / (float) this.mWidth;
                if (localRatio * tWidth > this.layoutHeight) {
                    localRatio = this.layoutHeight / tWidth;
                    tHeight = this.mHeight * localRatio;
                    tWidth = this.mWidth * localRatio;
                } else {
                    tHeight *= localRatio;
                    tWidth = this.layoutWidth;
                }
            }

            this.scaleRatio = (tWidth / width);
        }
        int outWidth = Math.round(tWidth);
        height = Math.round(tHeight);


        drawFabricViewLayout.width = outWidth;
        drawFabricViewLayout.height = height;


        this.bFabricView.setLayoutParams(drawFabricViewLayout);
        this.bFabricView.setScaleRatio(this.scaleRatio);
        this.aFabricView.setScaleRatio(this.scaleRatio);
        if (this.bFabricView.getScrollY() != 0) {
            this.bFabricView.setScrollY(0);
        }
        if (this.aFabricView.getScrollY() != 0) {
            this.aFabricView.setScrollY(0);
        }
        drawObjA(0.0F);
    }


    public final void drawObjB() {
        this.aFabricView.clear();
        this.bFabricView.clear();
    }

    public final void clear() {
        if (this.historyOrder != null) {
            this.historyOrder.clear();
        }
        drawObjB();
    }

    /**
     * 清除所有
     */
    public final void clearAll() {
        drawObjB();
        this.historyOrder.clearAll();
        this.imageHeight = 0;
        this.imageWidth = 0;

    }

    /**
     * 撤销
     */
    public void undo() {
        int size = undoDrawableList.size();
        if (size == 0) {
            return;
        }
        redoDrawableList.add(undoDrawableList.get(size - 1));
        undoDrawableList.remove(size - 1);
        int type = redoDrawableList.get(redoDrawableList.size() - 1);
        if (type == 0) {
            aFabricView.undo();
        } else {
            bFabricView.undo();
        }
    }

    /**
     * 回退操作
     */
    public void redo() {
        int size = redoDrawableList.size();
        if (size == 0) {
            return;
        }
        undoDrawableList.add(redoDrawableList.get(size - 1));
        redoDrawableList.remove(size - 1);
        int type = undoDrawableList.get(undoDrawableList.size() - 1);
        if (type == 0) {
            aFabricView.redo();
        } else {
            bFabricView.redo();
        }

    }


    final class ToRun
            implements Runnable {
        private PageWhite p;

        ToRun(PageWhite pageWhite) {
            if (this.p != null && this.p.equals(pageWhite)) {
                Log.i("相同", "");
                return;
            }
            this.p = pageWhite;
        }


        public final void run() {
            p.layoutWidth = whitedrawView.getWidth();
            p.layoutHeight = whitedrawView.getHeight();
            Log.i("宽度高度重新调整", "宽度：" + p.layoutWidth + ",高度：" + p.layoutHeight);
            if (p.layoutWidth <= 0 || p.layoutHeight <= 0) {
                return;
            }
            p.mHeight = p.layoutHeight;
            //转4：3比例
            p.mWidth = (p.layoutHeight << 2) / 3;


            Log.i("宽度高度重新调整", "宽度m：" + p.mWidth + ",高度m：" + p.mHeight);
            p.drawObjA(p.imageWidth, p.imageHeight);
            p.whitedrawView.invalidate();
        }
    }

    class CallBackImpl implements CallBack {

        private int layerType;


        CallBackImpl(int layerType) {
            this.layerType = layerType;

        }


        @Override
        public final void onDrawBack(List<BaseDraw> fabricViewDataList, List<BaseDraw> undoDrawableList, List<BaseDraw> redoDrawableList, BaseDraw bDraw) {
            if (bDraw == null) {
                if (layerType == 1) {
                    CopyOnWriteArrayList lst = historyOrder.getDrawA(page);
                    lst.clear();
                    lst.addAll(fabricViewDataList);
                }

                if (layerType == 2) {
                    CopyOnWriteArrayList lst = historyOrder.getDrawB(page);
                    lst.clear();
                    lst.addAll(fabricViewDataList);
                }
            }

        }
    }
}



