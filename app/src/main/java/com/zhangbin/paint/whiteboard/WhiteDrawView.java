package com.zhangbin.paint.whiteboard;

/**
 * @ClassName WhitedrawView
 * @Description 主画板视图类，控制发起由这发起
 * @Author yangjie
 * @Date 2019/3/28 下午6:27
 */

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zhangbin.paint.whiteboard.DrawLayerView;
import com.zhangbin.paint.whiteboard.PageWhite;
import com.zhangbin.paint.whiteboard.PptWebView;
import com.zhangbin.paint.whiteboard.shape.BaseDraw;


public final class WhiteDrawView extends FrameLayout {
    private ImageView imageView;
    private DrawLayerView imageFabricView;
    private DrawLayerView drawFabricView;
    private EditText editText;
    private int paintColor = 16711680;
    private FrameLayout frameLayout;

    private PageWhite pageWhite;
    private PptWebView webView;
    private int backColor = -1;


    public WhiteDrawView(Context context) {
        this(context, null);
    }

    private WhiteDrawView(Context context, AttributeSet attributeSet) {
        this(context, null, 0);
    }

    private WhiteDrawView(Context context, AttributeSet attributeSet, int index) {
        super(context, attributeSet, 0);

        super.setBackgroundColor(this.backColor);
        this.frameLayout = new FrameLayout(this.getContext());
        this.editText = new EditText(this.getContext());
        this.drawFabricView = new DrawLayerView(this.getContext());
        this.imageFabricView = new DrawLayerView(this.getContext());
        this.webView = new PptWebView(this.getContext());
        this.imageView = new ImageView(this.getContext());
        this.imageView.setAdjustViewBounds(true);
        this.editText.setPadding(0, 0, 0, 0);
        LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        initWebSetting();
        this.frameLayout.addView(this.webView, layoutParams);
        this.frameLayout.addView(this.imageView, layoutParams);
        this.frameLayout.addView(this.drawFabricView, layoutParams);
        this.frameLayout.addView(this.imageFabricView, layoutParams);
        this.addView(this.frameLayout, index);


    }

    private void initWebSetting() {
        String url = "http://192.168.8.37:8081/83461B08A0401FC68D9C2A7E036C4710/h5/h5.html?aaaa";
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }


    /**
     * 初始化
     *
     * @param isClient
     */
    public final void init(boolean isClient) {
        this.pageWhite = new PageWhite(isClient, this);
        //要设置原始ppt 大小以便进行缩放控制
        this.pageWhite.setWidthHeight(934, 508);
//        }

    }

    public final ImageView getImageView() {
        return this.imageView;
    }

    public final DrawLayerView getImageFabricView() {
        return this.imageFabricView;
    }

    public final DrawLayerView getDrawFabricView() {
        return this.drawFabricView;
    }

    public final FrameLayout getFrameLayout() {
        return this.frameLayout;
    }

    public final EditText getEditText() {
        return this.editText;
    }

    public final WebView getWebView() {
        return this.webView;
    }

    public final void setImageResource(int paramInt) {
        if (this.imageView != null) {
            this.imageView.setImageResource(paramInt);
        }
    }

    public final void setImageDrawable(int color) {
        if (this.imageView != null) {
            this.imageView.setImageDrawable(new ColorDrawable(color));
        }
    }

    public final void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
        this.drawFabricView.setColor(this.paintColor);
    }

    public final int getPaintColor() {
        return this.paintColor;
    }

    public final void setStrokeWidth(float strokeWidth) {
        this.drawFabricView.setStrokeWidth(strokeWidth);
    }

    public final float getStrokeWidth() {
        return this.drawFabricView.getStrokeWidth();
    }

    public final void setTextSize(int size) {
        this.drawFabricView.setTextSize(size);
    }

    public final int getTextSize() {
        return this.drawFabricView.getTextSize();
    }

    public final void setDrawType(int drawType) {
        this.drawFabricView.setDrawType(drawType);
    }

    public final int getDrawType() {
        return this.drawFabricView.getDrawType();
    }

    public final void setBackgroundColor(int color) {
        this.backColor = color;
        super.setBackgroundColor(this.backColor);
    }

    public final boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
//        if (this.g == null) {
        return true;
//        }
//        return this.g.imageView(paramMotionEvent);
    }

    public final boolean onTouchEvent(MotionEvent paramMotionEvent) {
        return false;
    }

    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.pageWhite != null) {
            this.pageWhite.drawObjA();
        }

    }

    public final void init(float width, float height) {
        if (this.pageWhite != null) {
            this.pageWhite.drawObjA(0.0F);
        }
    }


    public final void drawObj(int pageIndex, BaseDraw baseDraw) {

        if (this.pageWhite != null) {
            if (pageIndex<=0){
                pageIndex=1;
                this.pageWhite.ToPage(pageIndex);
            }

            this.pageWhite.drawObjB(pageIndex, baseDraw);
        }
    }


    public final void setPPTLoadFailDrawable(Drawable paramDrawable) {
        if (this.pageWhite != null) {
            this.pageWhite.drawObjA(paramDrawable);
        }
    }

    /**
     * 指定页数清除
     * @param pageIndex
     */
    public final void clearPageIndex(int pageIndex) {
        if (this.pageWhite != null) {
            this.pageWhite.clearPageIndex(pageIndex);
        }
    }

    public final void init() {
        if (this.pageWhite != null) {
            this.pageWhite.clear();
        }
    }


    public final boolean drawObj(int pageIndex) {
        return this.pageWhite.drawObjB(pageIndex);
    }

    public final void drawObj() {
        if (this.pageWhite != null) {
            this.pageWhite.drawObjB();
        }
    }

    public final void clearAll() {
        if (this.pageWhite != null) {
            this.pageWhite.clearAll();
        }
    }


    /**
     * 410 打开草稿纸
     *
     * @param currentPage
     */
    public void openDraftPaper(int currentPage) {
        webView.setVisibility(INVISIBLE);
        this.pageWhite.ToPage(currentPage);
    }

    /**
     * 411 关闭草稿纸
     *
     * @param currentPage
     */
    public void closeDraftPaper(int currentPage) {
        webView.setVisibility(VISIBLE);
        jumpPage(currentPage, 1);
    }

    /**
     * 414 增加草稿纸
     *
     * @param currentPage
     */
    public void addDraftPaper(int currentPage) {
        webView.setVisibility(INVISIBLE);
        this.pageWhite.addDraftPage(currentPage);
    }

    /**
     * 跳转指定页面
     * @param currentPage
     * @param currentAnimation
     */
    public void jumpPage(int currentPage, int currentAnimation) {
        String js = "javascript:JumpPage(" + currentPage + "," + currentAnimation + ",1)";
        webViewLoad(js);
        this.pageWhite.ToPage(currentPage);

    }

    /**
     * 针对不同版本调用不同的API
     * @param js
     */
    private void webViewLoad(String js) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null);
        } else {
            webView.loadUrl(js);
        }
    }


    /**
     * 上一页
     * @param currentPage
     * @param currentAnimation
     */
    public void lastSlideS(int currentPage, int currentAnimation) {
        String js = "javascript:LastSlideS(" + currentPage + "," + currentAnimation + ")";
        webViewLoad(js);
        this.pageWhite.ToPage(currentPage);
    }

    /**
     * 下一页
     * @param currentPage
     * @param currentAnimation
     */
    public void nextSlideS(int currentPage, int currentAnimation) {
        String js = "javascript:NextSlideS(" + currentPage + "," + currentAnimation + ")";
        webViewLoad(js);
        this.pageWhite.ToPage(currentPage);
    }


    /**
     * 501指令,撤销
     */
    public void undo() {
        this.pageWhite.undo();
    }

    /**
     * 502指令,恢复
     */
    public void redo() {
        this.pageWhite.redo();
    }


}
