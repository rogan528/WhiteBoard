package com.zhangbin.paint.whiteboard.presenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zhangbin.paint.R;
import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;
import com.zhangbin.paint.whiteboard.WhiteDrawView;
import com.zhangbin.paint.whiteboard.shape.BaseDraw;
import com.zhangbin.paint.whiteboard.shape.DrawFactory;

/**
 * @ClassName fffff
 * @Description TODO
 * @Author yangjie
 * @Date 2019/4/1 下午1:46
 */
public class WhiteboardPresenter {
    private Context context;
    private ViewGroup c;
    protected WhiteDrawView whiteDrawView;
    private PageCommandCallback f;
    private ViewGroup viewGroup;
    private int indexPage;
    private int backPage = 1;
    private float backpenSize = 2;
    private int backPenColor = Color.RED;
    private float backEraserSize = 5;
    private float backTextSize = 50;
    private int backTextColor = Color.RED;
    private int addDraftPage = 100000;

    public WhiteboardPresenter(Context context, ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        this.context = context;
        this.init();

    }

    public void init() {
        this.whiteDrawView = new WhiteDrawView(this.context);
        this.whiteDrawView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        setPPTContainer(viewGroup);
        this.whiteDrawView.init(true);

    }

    public void setPPTContainer(final ViewGroup c) {
        if (this.viewGroup == c && this.c != null && this.whiteDrawView != null && this.c.indexOfChild((View) this.whiteDrawView) != -1) {
            return;
        }
        Log.d("video", "设置ppt");
        this.removeFromContainer();
        this.c = c;
        if (this.whiteDrawView != null) {
            this.c.addView((View) this.whiteDrawView);
        }
    }

    public void setWhiteboardLoadFailDrawable(final Drawable pptLoadFailDrawable) {
        if (this.whiteDrawView != null) {
            this.whiteDrawView.setPPTLoadFailDrawable(pptLoadFailDrawable);
        }
    }

    public void removeFromContainer() {
        if (this.whiteDrawView == null) {
            return;
        }
        final ViewGroup viewGroup;
        if ((viewGroup = (ViewGroup) this.whiteDrawView.getParent()) != null) {
            viewGroup.removeView((View) this.whiteDrawView);
        }
    }

    /**
     * 设置PPT的背景色
     * @param backgroundColor
     */
    public void setWhiteboardBackgroundColor(final int backgroundColor) {
        if (this.whiteDrawView == null) {
            return;
        }
        this.whiteDrawView.setBackgroundColor(backgroundColor);
    }

    public void release() {
        if (this.c != null) {
            this.c.removeView((View) this.whiteDrawView);
        }
    }

    /**
     * 305指令  画笔大小
     * @param penSize
     */
    public void setPaintSize(float penSize) {
        OperationUtils.getInstance().mCurrentPenSize = penSize;
    }

    /**
     * 306指令 画笔颜色
     *
     * @param paintColor
     */
    public void setPaintColor(int paintColor) {
        OperationUtils.getInstance().mCurrentPenColor = paintColor;
    }

    /**
     * 307指令 橡皮大小
     *
     * @param eraserSize
     */
    public void setReaserSize(float eraserSize) {
        OperationUtils.getInstance().mCurrentEraserSize = eraserSize;
    }

    /**
     * 308指令 设置文字字号
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        OperationUtils.getInstance().mCurrentTextSize = textSize;
    }

    /**
     * 309指令 设置文字颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        OperationUtils.getInstance().mCurrentTextColor = textColor;
    }

    /**
     * 400-408指令
     *
     * @param s
     */
    public void addDrawData(final OrderBean s) {
        if (this.whiteDrawView == null) {
            return;
        }
        final BaseDraw pageDrawable = DrawFactory.createPageDrawable(s);
        if (pageDrawable == null) {
            return;
        }
        this.whiteDrawView.drawObj(this.indexPage, pageDrawable);
    }

    /**
     * 410 打开草稿纸
     */
    public void openDraftPaper() {
        backPage = this.indexPage;
        backpenSize = OperationUtils.getInstance().mCurrentPenSize;
        backPenColor = OperationUtils.getInstance().mCurrentPenColor;
        backEraserSize = OperationUtils.getInstance().mCurrentEraserSize;
        backTextSize = OperationUtils.getInstance().mCurrentTextSize;
        backTextColor = OperationUtils.getInstance().mCurrentTextColor;
        this.whiteDrawView.openDraftPaper(addDraftPage);
        this.indexPage = addDraftPage;
    }

    /**
     * 411 关闭草稿纸
     */
    public void closeDraftPaper() {
        OperationUtils.getInstance().mCurrentPenSize = backpenSize;
        OperationUtils.getInstance().mCurrentPenColor = backPenColor;
        OperationUtils.getInstance().mCurrentEraserSize = backEraserSize;
        OperationUtils.getInstance().mCurrentTextSize = backTextSize;
        OperationUtils.getInstance().mCurrentTextColor = backTextColor;
        this.whiteDrawView.closeDraftPaper(backPage);
        this.indexPage = backPage;

    }

    /**
     * 413 草稿纸背景切换
     */
    public void setBackgroundColor(String value) {
        if (value.equals("1")) {  //白色
            setWhiteboardBackgroundColor(context.getResources().getColor(R.color.white));
        } else {//黑色
            setWhiteboardBackgroundColor(context.getResources().getColor(R.color.black));
        }
    }

    /**
     * 414 增加草稿纸
     */
    public void addDraftPaper() {
        backPage = this.indexPage;
        backpenSize = OperationUtils.getInstance().mCurrentPenSize;
        backPenColor = OperationUtils.getInstance().mCurrentPenColor;
        backEraserSize = OperationUtils.getInstance().mCurrentEraserSize;
        backTextSize = OperationUtils.getInstance().mCurrentTextSize;
        backTextColor = OperationUtils.getInstance().mCurrentTextColor;
        setWhiteboardBackgroundColor(context.getResources().getColor(R.color.white));
        addDraftPage = addDraftPage+1;
        this.indexPage = addDraftPage;
        this.whiteDrawView.addDraftPaper(this.indexPage);
    }

    /**
     * 500指令,清除
     */
    public void orderClear() {
        clearPageDraw(this.indexPage);
    }

    /**
     * 501指令,撤销
     */
    public void undo() {
        whiteDrawView.undo();
    }

    /**
     * 502指令,恢复
     */
    public void redo() {
        whiteDrawView.redo();
    }

    /**
     * 503指令,跳转指定页面
     *
     * @param currentPage
     * @param currentAnimation
     */
    public void jumpPage(int currentPage, int currentAnimation) {
        this.whiteDrawView.jumpPage(currentPage, currentAnimation);
        this.indexPage = currentPage;
    }

    /**
     * 504指令,上一页
     *
     * @param currentPage
     * @param currentAnimation
     */
    public void lastSlideS(int currentPage, int currentAnimation) {
        this.whiteDrawView.lastSlideS(currentPage, currentAnimation);
        this.indexPage = currentPage;

    }

    /**
     * 505指令,下一页
     *
     * @param currentPage
     * @param currentAnimation
     */
    public void nextSlideS(int currentPage, int currentAnimation) {
        this.whiteDrawView.nextSlideS(currentPage, currentAnimation);
        this.indexPage = currentPage;
    }


    /**
     * 清除指定页涂鸦
     * @param pageIndex
     */
    public void clearPageDraw(int pageIndex) {
        if (this.whiteDrawView == null) {
            return;
        }
        this.whiteDrawView.clearPageIndex(pageIndex);
    }
    /**
     * 清除所有
     */
    public void clearAllDraw() {
        if (this.whiteDrawView == null) {
            return;
        }
        this.whiteDrawView.init();
    }

    public void setPageCommandCallback(final PageCommandCallback f) {
        this.f = f;
    }

    public interface PageCommandCallback {
        void getPageCommand(final int p0);
    }
}