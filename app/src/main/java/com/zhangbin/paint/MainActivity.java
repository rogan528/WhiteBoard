package com.zhangbin.paint;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    private String url = "https://www.baidu.com/";
    private WebView mWebView;
    private LinearLayout mBottom;
    private EditText mPaintSize;//设置画笔大小
    private EditText mEraserSize;//设置橡皮大小
    private EditText mPaintColor;//设置颜色
    private GraffitiView tuyaView;//自定义涂鸦板
    private int screenWidth;
    private int screenHeight;
    private int realHeight;//控件真实高度，去除头部标题后的
    private boolean isPaint = true;//是否是画笔
    private boolean isOpen = true;//是否打开
    private int select_paint_style_paint = 0; //画笔的样式
    private int select_paint_style_eraser = 1; //橡皮擦的样式
    private static final int DRAW_PATH = 0; //画线
    private static final int DRAW_CIRCLE = 1;//画圆
    private static final int DRAW_RECTANGLE = 2;//画矩形
    private static final int DRAW_ARROW = 3;//画箭头
    private Toast mToast;
    private DragTextView mTextView;
    private float paintSizeValue = 5;//画笔的默认大小
    private float eRaserSizeValue = 50;//橡皮的默认大小
    private String mPaintColorValue = "#DC143C";//画笔的默认颜色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initWebSetting();
        initData();
        initListener();
        initDragView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        mWebView = findViewById(R.id.wv);
        mWebView = findViewById(R.id.wv);
        mPaintSize = findViewById(R.id.et_paint_size);//设置画笔大小
        mEraserSize = findViewById(R.id.et_eraser_size);//设置橡皮大小
        mPaintColor = findViewById(R.id.et_paint_color);//设置颜色
        mBottom = findViewById(R.id.ll_bottom);
        mTextView = findViewById(R.id.textView);
    }

    /**
     * 拖动视频
     */
    private void initDragView() {
        mTextView.setTextColor(Color.parseColor("#0000CD"));
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("zhangbin----","5555555555555555555");
            }
        });
        mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Log.e("zhangbin----","66666666666666666");
                return false;
            }
        });

    }


    /**
     * 初始化数据
     */
    private void initData() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        screenWidth = defaultDisplay.getWidth();
        screenHeight = defaultDisplay.getHeight();
        // realHeight = (int) (screenHeight - getResources().getDimension(R.dimen.DIMEN_100PX) - getResources().getDimension(R.dimen.DIMEN_100PX));
        realHeight = screenHeight;
        tuyaView = new GraffitiView(this, screenWidth, realHeight);
        mWebView.addView(tuyaView);
        tuyaView.requestFocus();
        initSetting();
    }

    private void initSetting() {
        mPaintSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(mPaintSize.getText().toString().trim())) {
                    paintSizeValue = Float.parseFloat(mPaintSize.getText().toString().trim());
                    tuyaView.setPaintSize(paintSizeValue);
                }
            }
        });
        mEraserSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(mEraserSize.getText().toString().trim())) {
                    eRaserSizeValue = Float.parseFloat(mEraserSize.getText().toString().trim());
                    tuyaView.setEraserSize(eRaserSizeValue);
                }
            }
        });
        mPaintColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(mPaintColor.getText().toString().trim()) && mPaintColor.getText().toString().trim().length() == 7) {
                    mPaintColorValue = mPaintColor.getText().toString().trim();
                    tuyaView.setPaintColor(Color.parseColor(mPaintColorValue));
                } else {
                    tuyaView.setPaintColor(Color.parseColor("#DC143C"));
                }
            }
        });
    }

    private void initWebSetting() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebContentsDebuggingEnabled(true);
        mWebView.loadUrl(url);

    }

    /**
     * 监听事件
     */
    private void initListener() {
        //撤销
        findViewById(R.id.btn_undo).setOnClickListener(this);
        //还原
        findViewById(R.id.btn_redo).setOnClickListener(this);
        //清空
        findViewById(R.id.btn_clear).setOnClickListener(this);
        //画笔
        findViewById(R.id.btn_paint).setOnClickListener(this);
        //橡皮
        findViewById(R.id.iv_reaserstyle).setOnClickListener(this);
        //画圆
        findViewById(R.id.btn_drawcycle).setOnClickListener(this);
        //画方形
        findViewById(R.id.btn_drawrec).setOnClickListener(this);
        //箭头
        findViewById(R.id.btn_drawarrow).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //画笔
            case R.id.btn_paint:
                aboutPaintStyleSetting();
                break;
            //橡皮
            case R.id.iv_reaserstyle:
                aboutReaserStyleSetting();
                break;
            //撤销按钮
            case R.id.btn_undo:
                tuyaView.undo();
                break;
            //还原按钮
            case R.id.btn_redo:
                tuyaView.redo();
                break;
            //清除按钮 重做
            case R.id.btn_clear:
                //Toast.makeText(MainActivity.this,"清除按钮",Toast.LENGTH_SHORT).show();
                tuyaView.clear();
                mWebView.setBackgroundResource(R.color.white);
                //恢复成画笔状态
                tuyaView.setSrcBitmap(null);
                paintStyleSettingDesc(select_paint_style_paint, true);
                tuyaView.drawGraphics(DRAW_PATH);
                break;
            //以下为画图形状按钮
            case R.id.btn_drawarrow:
                tellPaintStyleAndSetDrawGraphics(DRAW_ARROW, select_paint_style_paint);
                break;
            case R.id.btn_drawrec:
                tellPaintStyleAndSetDrawGraphics(DRAW_RECTANGLE, select_paint_style_paint);
                break;
            case R.id.btn_drawcycle:
                tellPaintStyleAndSetDrawGraphics(DRAW_CIRCLE, select_paint_style_paint);
                break;


        }
    }


    //判断画笔样式并切换画图样式
    private void tellPaintStyleAndSetDrawGraphics(int drawArrow, int select_paint_style_paint) {
        if (isPaint) {
            tuyaView.drawGraphics(drawArrow);
        } else {//当前为橡皮擦
            tuyaView.selectPaintStyle(select_paint_style_paint);
            tuyaView.drawGraphics(drawArrow);
            isPaint = true;
        }
    }

    //画笔样式设置
    private void aboutPaintStyleSetting() {
        paintStyleSettingDesc(select_paint_style_paint, true);
        tuyaView.drawGraphics(DRAW_PATH);
    }

    //橡皮样式设置
    private void aboutReaserStyleSetting() {
        reaserStyleSettingDesc(select_paint_style_eraser, false);
    }

    //画笔样式设置详情
    private void reaserStyleSettingDesc(int paintStyle, boolean styleTarget) {
        tuyaView.selectPaintStyle(paintStyle);
        isPaint = styleTarget;
    }

    //画笔样式设置详情
    private void paintStyleSettingDesc(int paintStyle, boolean styleTarget) {
        //mPaintStyle.setBackgroundResource(paintStyleResouce);
        tuyaView.selectPaintStyle(paintStyle);
        isPaint = styleTarget;
    }
}
