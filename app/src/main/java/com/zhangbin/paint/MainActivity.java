package com.zhangbin.paint;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangbin.paint.beans.OrderBean;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    private String url = "https://www.baidu.com/";


    private WebView mWebView;
    private Button mOpen;//打开
    private LinearLayout mBottom;
    private Button mJxNext;//下一步
    private Button mPaintStyle;//画笔
    private Button mEraserStyle;//橡皮
    private EditText mPaintSize;//设置画笔大小
    private EditText mEraserSize;//设置橡皮大小
    private EditText mPaintColor;//设置颜色
    private Button mBtnSaved;//保存按钮
    private Button mBtnRevoke;//撤销按钮
    private Button mBtnClean;//清除按钮
    private Button mBtnPicselect;//选择图片按钮
    private Button mBtnDrawCycle;//圆形按钮
    private Button mBtnDrawRec;//方形按钮
    private Button mBtnDrawRrow;//箭头按钮
    private GraffitiView tuyaView;//自定义涂鸦板
    private int screenWidth;
    private int screenHeight;
    private int realHeight;//控件真实高度，去除头部标题后的
    private boolean isPaint = true;//是否是画笔
    private boolean isOpen = false;//是否打开
    private int select_paint_style_paint = 0; //画笔的样式
    private int select_paint_style_eraser = 1; //橡皮擦的样式
    private static final int DRAW_PATH = 0; //画线
    private static final int DRAW_CIRCLE = 1;//画圆
    private static final int DRAW_RECTANGLE = 2;//画矩形
    private static final int DRAW_ARROW = 3;//画箭头
    private Toast mToast;
    private DragTextView mTextView;
    private String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private float paintSizeValue = 5;//画笔的默认大小
    private float eRaserSizeValue = 50;//橡皮的默认大小
    private String mPaintColorValue = "#DC143C";//画笔的默认颜色
    private ArrayList<OrderBean> listOrderBean;
    private DrawManger drawManger;

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
        mPaintStyle = findViewById(R.id.iv_paintstyle);//画笔
        mEraserStyle = findViewById(R.id.iv_reaserstyle);//橡皮
        mPaintSize = findViewById(R.id.et_paint_size);//设置画笔大小
        mEraserSize = findViewById(R.id.et_eraser_size);//设置橡皮大小
        mPaintColor = findViewById(R.id.et_paint_color);//设置颜色
        mBtnRevoke = findViewById(R.id.btn_revoke);
        mBtnClean = findViewById(R.id.btn_clean);
        mBtnPicselect = findViewById(R.id.btn_picselect);
        mBtnDrawCycle = findViewById(R.id.btn_drawcycle);
        mBtnDrawRec = findViewById(R.id.btn_drawrec);
        mBtnDrawRrow = findViewById(R.id.btn_drawarrow);
        mBtnSaved = findViewById(R.id.btn_savesd);
        mOpen = findViewById(R.id.open);
        mBottom = findViewById(R.id.ll_bottom);
        mTextView = findViewById(R.id.textView);
        mJxNext = findViewById(R.id.jx_next);
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
        /*tuyaView.setPaintSize(paintSizeValue);
        tuyaView.setReaserSize("1",eRaserSizeValue);
        tuyaView.setPaintColor("1",Color.parseColor(mPaintColorValue));*/
        mOpen.setOnClickListener(this);
        initSetting();
        String input = Util.readFileFromAssets(this, "LiveClient.json");
        Gson gson = new Gson();
        listOrderBean = gson.fromJson(input, new TypeToken<ArrayList<OrderBean>>() {
        }.getType());
        drawManger = new DrawManger(tuyaView, mWebView);
        drawManger.setListorderBean(listOrderBean);


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
        //mWebView.setWebViewClient(new WebChromeClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebContentsDebuggingEnabled(true);
        mWebView.loadUrl(url);

    }

    /**
     * 监听事件
     */
    private void initListener() {
        mPaintStyle.setOnClickListener(this);//画笔的监听
        mEraserStyle.setOnClickListener(this);//橡皮的监听
        mBtnSaved.setOnClickListener(this);
        mBtnRevoke.setOnClickListener(this);
        mBtnClean.setOnClickListener(this);
        mBtnPicselect.setOnClickListener(this);
        mBtnDrawCycle.setOnClickListener(this);
        mBtnDrawRec.setOnClickListener(this);
        mBtnDrawRrow.setOnClickListener(this);
        mJxNext.setOnClickListener(this);
    }

    /**
     * 轮询播放
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jx_next:
                drawManger.NextOrder().ExecuteOrder();
                break;
            case R.id.open:
                aboutOpenSetting();
                break;
            //画笔
            case R.id.iv_paintstyle:
                aboutPaintStyleSetting();
                break;
            //橡皮
            case R.id.iv_reaserstyle:
                aboutReaserStyleSetting();
                break;
            //撤销按钮
            case R.id.btn_revoke://撤销
                //Toast.makeText(MainActivity.this,"撤销按钮",Toast.LENGTH_SHORT).show();
                tuyaView.undo();
                break;
            //前进按钮
            case R.id.btn_savesd://前进
                //Toast.makeText(MainActivity.this,"前进按钮",Toast.LENGTH_SHORT).show();
                tuyaView.recover();
                break;
            //清除按钮 重做
            case R.id.btn_clean:
                //Toast.makeText(MainActivity.this,"清除按钮",Toast.LENGTH_SHORT).show();
                tuyaView.redo();
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
            mPaintStyle.setBackgroundResource(R.drawable.paint_style);
            tuyaView.selectPaintStyle(select_paint_style_paint);
            tuyaView.drawGraphics(drawArrow);
            isPaint = true;
        }
    }


    //打开
    private void aboutOpenSetting() {
        if (isOpen) {//
            isOpen = false;
            mOpen.setText("打开");
            mBottom.setVisibility(View.GONE);
        } else {
            isOpen = true;
            mOpen.setText("关闭");
            mBottom.setVisibility(View.VISIBLE);
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
