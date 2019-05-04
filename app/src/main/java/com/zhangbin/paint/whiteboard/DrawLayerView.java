package com.zhangbin.paint.whiteboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zhangbin.paint.whiteboard.CallBack;
import com.zhangbin.paint.whiteboard.shape.BaseDraw;
import com.zhangbin.paint.whiteboard.shape.DrawText;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
public class DrawLayerView
        extends View {
    private float strokeWidth = 5;
    private int color = Color.RED;
    private int textSize = 15;

    /**
     * 是否在所有页进行绘画指定信息，注：所有页
     */
    private boolean isDrawAll = false;
    private float scaleRatio = 1.0F;
    private Paint paint;
    private Paint.Style style = Paint.Style.STROKE;
    private int drawType = 0;
    private List<BaseDraw> fabricViewDataList = new CopyOnWriteArrayList();
    private List<BaseDraw> undoDrawableList = new CopyOnWriteArrayList();
    private List<BaseDraw> redoDrawableList = new CopyOnWriteArrayList();
    private CallBack t;
    private int layerId = 0;

    /**
     * @param context
     */
    public DrawLayerView(Context context) {
        this(context, null);

    }

    private DrawLayerView(Context context, AttributeSet attributeSet) {
        this(context, null, 0);
    }

    private DrawLayerView(Context context, AttributeSet attributeSet, int i) {

        super(context, attributeSet, 0);

        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置硬件加速与橡皮擦有关
        setLayerType(LAYER_TYPE_HARDWARE, null);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(this.color);
        this.paint.setStyle(this.style);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeWidth(this.strokeWidth);

    }

    protected final void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.translate(getWidth() >> 1, getHeight() >> 1);
        Iterator iterator = this.fabricViewDataList.iterator();
        while (iterator.hasNext()) {
            BaseDraw next = (BaseDraw) iterator.next();
            next.setScaleRatio(this.scaleRatio);
            next.draw(canvas);
        }

        if (this.t != null) {

            if (this.isDrawAll) {
                this.t.onDrawBack(this.fabricViewDataList, this.undoDrawableList, this.redoDrawableList, null);
                this.isDrawAll = false;
            }
        }
    }


    /**
     * 每个绘图都进行绘画
     *
     * @param context
     * @param text
     * @param x
     * @param y
     * @return
     */
    public final BaseDraw DrawLayerView(Context context, String text, int x, int y) {
        DrawText drawText;
        (drawText = new DrawText(context, text, x, y, this.paint)).size(this.textSize);
        drawText.setId("1");
        drawText.setDrawType(5);
        this.fabricViewDataList.add(drawText);
        this.undoDrawableList.add(drawText);
        this.invalidate();
        return drawText;
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
    }

    public final void setPaint(Paint paint) {
        if (paint != null) {
            this.paint = paint;
        }
    }

    public final Paint.Style getStyle() {
        return this.style;
    }

    public final void setStyle(Paint.Style style) {
        this.style = style;
        this.paint.setStyle(style);
    }

    public final float getStrokeWidth() {
        return this.strokeWidth;
    }

    public final void setStrokeWidth(float width) {
        this.strokeWidth = width;
        this.paint.setStrokeWidth(width);
    }

    public final void setTextSize(int size) {
        this.textSize = size;
    }

    public final int getTextSize() {
        return this.textSize;
    }

    public final void setScaleRatio(float scaleRatio) {
        if (this.scaleRatio == scaleRatio) {
            return;
        }
        this.scaleRatio = scaleRatio;
    }

    public final void setCmdScaleRatio(float cmdScaleRatio) {
    }

    public final void setCmdPPTRatio(float cmdPPTRatio) {
    }


    public final int getDrawType() {
        return this.drawType;
    }

    public final void setDrawType(int drawType) {
        this.drawType = drawType;
    }

    public final List<BaseDraw> getAllPointList() {
        if (this.fabricViewDataList.size() > 0) {
            return this.fabricViewDataList;
        }
        return null;
    }

    public final List<BaseDraw> getUndoDrawableList() {
        return this.undoDrawableList;
    }

    public final List<BaseDraw> getRedoDrawableList() {
        return this.redoDrawableList;
    }

    public final void setFabricViewDataList(CopyOnWriteArrayList<BaseDraw> paramCopyOnWriteArrayList) {
        if ((paramCopyOnWriteArrayList != null) && (this.fabricViewDataList != null)) {
            this.fabricViewDataList.clear();
            this.fabricViewDataList.addAll(paramCopyOnWriteArrayList);
            invalidate();
        }
    }

    /**
     * 进行绘画
     *
     * @param draw
     */
    public final void DrawLayerView(BaseDraw draw) {
        if (this.fabricViewDataList != null) {
            Iterator iterator = this.fabricViewDataList.iterator();
            while (iterator.hasNext()) {
                BaseDraw localDraw = (BaseDraw) iterator.next();
                if (localDraw == null) {
                    continue;
                }
                //清除重复id 的绘画
                if (localDraw.getId() != null && !"".equals(localDraw.getId()) && draw.getId() != null && !"".equals(draw.getId())) {
                    if (localDraw.getId().equals(draw.getId())) {
                        this.fabricViewDataList.remove(localDraw);
                        break;
                    }
                }
            }
//            if (draw.getIsShow()) {
            this.fabricViewDataList.add(draw);
//            }
            this.undoDrawableList.add(draw);
            invalidate();
        }
    }

    public final void DrawLayerViewC(BaseDraw draw) {
        if (this.fabricViewDataList != null) {
            Iterator iterator = this.fabricViewDataList.iterator();
            while (iterator.hasNext()) {
                BaseDraw localDraw = (BaseDraw) iterator.next();
                if (localDraw == null) {
                    continue;
                }
                //清除重复id 的绘画
                if (localDraw.getId() != null && !"".equals(localDraw.getId()) && draw.getId() != null && !"".equals(draw.getId())) {
                    if (localDraw.getId().equals(draw.getId())) {
                        this.fabricViewDataList.remove(localDraw);

                        break;
                    }
                }
            }
//            if (draw.getIsShow()) {
            this.fabricViewDataList.add(draw);
//            }
        }
    }

    /**
     * 501指令,撤销
     */
    public void undo() {
        int size = undoDrawableList.size();
        if (size == 0) {
            return;
        } else {
            this.fabricViewDataList.clear();
            redoDrawableList.add(undoDrawableList.get(size - 1));
            undoDrawableList.remove(size - 1);
            isDrawAll = true;
            redrawOnBitmap();
        }
    }

    /**
     * 502指令,恢复
     */
    public void redo() {
        int size = redoDrawableList.size();
        if (size == 0) {
            return;
        } else {
            undoDrawableList.add(redoDrawableList.get(size - 1));
            redoDrawableList.remove(size - 1);
            isDrawAll = true;
            redrawOnBitmap();
        }
    }

    //将剩下的path重绘
    private void redrawOnBitmap() {
        Iterator<BaseDraw> iterator = undoDrawableList.iterator();
        while (iterator.hasNext()) {
            BaseDraw localDraw = (BaseDraw) iterator.next();
            if (localDraw == null) {
                continue;
            }
            this.DrawLayerViewC(localDraw);
        }
        invalidate();// 刷新
    }

    public final void DrawLayerView() {
        this.undoDrawableList.clear();
        this.fabricViewDataList.clear();
        this.redoDrawableList.clear();
        invalidate();
    }

    public final void setOnDrawListener(CallBack paramc) {
        this.t = paramc;
    }


    public int getLayerId() {
        return layerId;
    }

    //设置层id
    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }
}