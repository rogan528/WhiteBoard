package com.zhangbin.paint;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.ActivityUtil;
import com.zhangbin.paint.util.DimensionUtils;
import com.zhangbin.paint.util.ScreenSwitchUtils;
import com.zhangbin.paint.util.Util;
import com.zhangbin.paint.whiteboard.OrderDrawManger;
import com.zhangbin.paint.whiteboard.presenter.WhiteboardPresenter;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    private String url = "https://www.baidu.com/";
    private Button mOpen;//打开
    private LinearLayout mBottom;
    private Button mJxNext;//下一步
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
    private String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private float paintSizeValue = 5;//画笔的默认大小
    private float eRaserSizeValue = 50;//橡皮的默认大小
    private String mPaintColorValue = "#DC143C";//画笔的默认颜色
    private ArrayList<OrderBean> listOrderBean;
    private DrawManger drawManger;
    private FrameLayout pptLayout;
    private WhiteboardPresenter whiteboardPresenter;
    private Context mContext;
    private OrderDrawManger orderDrawManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initData();
        //initAssetsData();
        initListener();
        initDragView();

    }

    private void initAssetsData() {
        String input = Util.readFileFromAssets(this, "LiveClient.json");
        Gson gson = new Gson();
        listOrderBean = gson.fromJson(input, new TypeToken<ArrayList<OrderBean>>() {
        }.getType());
        //drawManger = new DrawManger(tuyaView, mWebView);
        drawManger.setListorderBean(listOrderBean);
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mPaintSize = findViewById(R.id.et_paint_size);//设置画笔大小
        mEraserSize = findViewById(R.id.et_eraser_size);//设置橡皮大小
        mPaintColor = findViewById(R.id.et_paint_color);//设置颜色
        mOpen = findViewById(R.id.open);
        mBottom = findViewById(R.id.ll_bottom);
        mTextView = findViewById(R.id.textView);
        mJxNext = findViewById(R.id.jx_next);
        pptLayout =  findViewById(R.id.pptLayout);
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
        realHeight = screenHeight;
        tuyaView = new GraffitiView(this, screenWidth, realHeight);
        //mWebView.addView(tuyaView);
        tuyaView.requestFocus();

        whiteboardPresenter = new WhiteboardPresenter(mContext,pptLayout);
        orderDrawManger = new OrderDrawManger(whiteboardPresenter);
        updateLayout();
        mOpen.setOnClickListener(this);
        initSetting();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        boolean isPortrait = ScreenSwitchUtils.getInstance(this).isPortrait();
        boolean isFullScreen = ScreenSwitchUtils.getInstance(this).isFullScreen();
        if (!isPortrait && isFullScreen) {
            ActivityUtil.setFullScreen(this, true);
        } else if (isPortrait && !isFullScreen) {
            ActivityUtil.setFullScreen(this, true);
        }
        updateLayout();
        super.onConfigurationChanged(newConfig);
    }
    public void updateLayout() {
        int width = DimensionUtils.getScreenWidth(this);
        int height = DimensionUtils.getScreenHeight(this);
        Log.i("ppt宽高1", "宽：" + width + "  高：" + height);
        Boolean isPortrait = height > width;
        if (!ActivityUtil.isFullScreen(this) && isPortrait) {
//            height -= DimensionUtils.getStatusBarHeight(this);
        }
        screenHeight = height;
        //获取宽高
        int pptLayoutWidth = 0;
        if (pptLayout != null) {
            ViewGroup.LayoutParams pptParams = pptLayout.getLayoutParams();
            pptLayout.setBackgroundColor(Color.TRANSPARENT);
            if (isPortrait) {   //竖屏
                pptLayoutWidth = width;
                height = 3 * width / 4;
            } else {  //横屏
                if (DimensionUtils.isPad(this)) {
                    pptLayoutWidth = (int) (width * 0.72);
                    height = pptLayoutWidth * 3 / 4;
                    pptLayout.setBackgroundColor(Color.BLACK);

                } else {
                    pptLayoutWidth = width;
                }
            }
            pptParams.width = pptLayoutWidth;
            pptParams.height = height;
            Log.i("ppt宽高", "宽：" + pptParams.width + "  高：" + height);
            pptLayout.setLayoutParams(pptParams);

        }
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
                //drawManger.NextOrder().ExecuteOrder();
                orderDrawManger.NextOrder().ExecuteOrder();
                break;
            case R.id.open:
                aboutOpenSetting();
                break;
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
