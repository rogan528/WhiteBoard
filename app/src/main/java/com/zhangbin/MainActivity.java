package com.zhangbin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangbin.paint.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , RadioGroup.OnCheckedChangeListener{

    // Used to load the 'native-lib' library on application startup.


    private String url = "https://www.baidu.com/";

    private WebView mWebView;
    private ImageView mPaintStyle;//画笔或者橡皮
    private ImageView mPaintColor;//设置颜色
    private ImageView mPaintSize;//设置画笔大小
    private LinearLayout ll_paintcolor_state;
    private RadioGroup rg_paint_color;
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
    private SeekBar sb_size;
    private boolean isPaint = true;//是否是画笔
    private int select_paint_style_paint = 0; //画笔的样式
    private int select_paint_style_eraser = 1; //橡皮擦的样式
    private static final int DRAW_PATH = 0; //画线
    private static final int DRAW_CIRCLE = 1;//画圆
    private static final int DRAW_RECTANGLE = 2;//画矩形
    private static final int DRAW_ARROW = 3;//画箭头
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();

    }



    /**
     * 初始化控件
     */
    private void initView() {

        // tv = findViewById(R.id.sample_text);

        mWebView = findViewById(R.id.wv);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(url);
        sb_size = (SeekBar) findViewById(R.id.sb_size);
        mPaintStyle = findViewById(R.id.iv_paintstyle);//画笔或者橡皮
        mPaintColor = findViewById(R.id.iv_paint_color);//设置颜色
        mPaintSize = findViewById(R.id.iv_paint_size);//设置画笔大小
        ll_paintcolor_state = (LinearLayout) findViewById(R.id.ll_paintcolor_state);
        rg_paint_color = (RadioGroup) findViewById(R.id.rg_paint_color);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //tv.setText(stringFromJNI()+"\n add:"+add(2,5)+"\n minus:"+minus(8,5));

        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        screenWidth = defaultDisplay.getWidth();
        screenHeight = defaultDisplay.getHeight();
        realHeight = (int) (screenHeight - getResources().getDimension(R.dimen.DIMEN_100PX) - getResources().getDimension(R.dimen.DIMEN_100PX));
        tuyaView = new GraffitiView(this, screenWidth, realHeight);
        mWebView.addView(tuyaView);
        tuyaView.requestFocus();
        tuyaView.selectPaintSize(sb_size.getProgress());
        mBtnRevoke = findViewById(R.id.btn_revoke);
        mBtnClean = findViewById(R.id.btn_clean);
        mBtnPicselect = findViewById(R.id.btn_picselect);
        mBtnDrawCycle = findViewById(R.id.btn_drawcycle);
        mBtnDrawRec = findViewById(R.id.btn_drawrec);
        mBtnDrawRrow = findViewById(R.id.btn_drawarrow);

    }
    /**
     * 监听事件
     */
    private void initListener() {
        mPaintStyle.setOnClickListener(this);//画笔的监听
        ll_paintcolor_state.setOnClickListener(this);
        mPaintColor.setOnClickListener(this);//颜色的监听
        mPaintSize.setOnClickListener(this);//大小的监听
        sb_size.setOnSeekBarChangeListener(new MySeekChangeListener());
        mBtnRevoke.setOnClickListener(this);
        mBtnClean.setOnClickListener(this);
        mBtnPicselect.setOnClickListener(this);
        mBtnDrawCycle.setOnClickListener(this);
        mBtnDrawRec.setOnClickListener(this);
        mBtnDrawRrow.setOnClickListener(this);
    }
    class MySeekChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tuyaView.selectPaintSize(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            tuyaView.selectPaintSize(seekBar.getProgress());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            sb_size.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //画笔或者橡皮擦
            case R.id.iv_paintstyle:
                aboutPaintStyleSetting();
                break;
            //颜色
            case R.id.iv_paint_color:
                changeColorAndSizeState(View.VISIBLE, View.GONE);
                rg_paint_color.setOnCheckedChangeListener(this);
                break;
            //画笔大小
            case R.id.iv_paint_size:
                changeColorAndSizeState(View.GONE, View.VISIBLE);
                break;
            //撤销按钮
            case R.id.btn_revoke://撤销
                tuyaView.undo();
                break;
            //清除按钮 重做
            case R.id.btn_clean:
                tuyaView.redo();
                mWebView.setBackgroundResource(R.color.white);
                //恢复成画笔状态
                tuyaView.setSrcBitmap(null);
                paintStyleSettingDesc(R.drawable.paint_style, select_paint_style_paint, true);
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
    //切换画笔颜色和画笔尺寸显隐状态
    private void changeColorAndSizeState(int visible,int gone) {
        ll_paintcolor_state.setVisibility(visible);
        sb_size.setVisibility(gone);
    }
    //画笔样式设置
    private void aboutPaintStyleSetting() {
        changeColorAndSizeState(View.GONE, View.GONE);
        if (isPaint) {//当前为画笔,点击后变为橡皮擦
            paintStyleSettingDesc(R.drawable.reaser_style, select_paint_style_eraser, false);
            //btn_null.setChecked(true);//使单选消失
        } else {
            paintStyleSettingDesc(R.drawable.paint_style, select_paint_style_paint, true);
            tuyaView.drawGraphics(DRAW_PATH);
        }

    }
    //画笔样式设置详情
    private void paintStyleSettingDesc(int paintStyleResouce, int paintStyle, boolean styleTarget) {
        mPaintStyle.setBackgroundResource(paintStyleResouce);
        tuyaView.selectPaintStyle(paintStyle);
        isPaint = styleTarget;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            //处理颜色
            case R.id.rb_purple:
                selectPaintColorAndSetting(0);
                break;
            case R.id.rb_orange:
                selectPaintColorAndSetting(1);
                break;
            case R.id.rb_green:
                selectPaintColorAndSetting(2);
                break;
            case R.id.rb_yellow:
                selectPaintColorAndSetting(3);
                break;
            case R.id.rb_black:
                selectPaintColorAndSetting(4);
                break;


           /* //以下为画图形状按钮
            case R.id.btn_drawarrow:
                tellPaintStyleAndSetDrawGraphics(DRAW_ARROW, select_paint_style_paint);
                break;
            case R.id.btn_drawrec:
                tellPaintStyleAndSetDrawGraphics(DRAW_RECTANGLE, select_paint_style_paint);
                break;
            case R.id.btn_drawcycle:
                tellPaintStyleAndSetDrawGraphics(DRAW_CIRCLE, select_paint_style_paint);
                break;*/
        }
    }
    //选择画笔颜色
    private void selectPaintColorAndSetting(int which) {
        tuyaView.selectPaintColor(which);
        ll_paintcolor_state.setVisibility(View.GONE);
    }
}
