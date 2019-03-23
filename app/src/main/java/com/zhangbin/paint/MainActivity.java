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

public class MainActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, MediaPlayer.OnPreparedListener {

    private String url = "https://www.baidu.com/";
    //private String url = "http://192.168.8.37:8081/83461B08A0401FC68D9C2A7E036C4710/h5/h5.html?aaaa";
    //  private String url = "file:///android_asset/javascript.html";


    private WebView mWebView;
    private Button mBack;//上一页
    private Button mOpen;//打开
    private LinearLayout mBottom;
    private Button mNext;//下一页
    private Button mJxBack;//上一步
    private Button mJxNext;//下一步
    private ImageView mPaintStyle;//画笔
    private ImageView mEraserStyle;//橡皮
    private EditText mPaintSize;//设置画笔大小
    private EditText mEraserSize;//设置橡皮大小
    private EditText mPaintColor;//设置颜色
    private LinearLayout ll_paintcolor_state;
    private RadioGroup rg_paint_color;
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
    private DragVideoView mVideoView;
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
        setupVideo();
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
        ll_paintcolor_state = findViewById(R.id.ll_paintcolor_state);
        rg_paint_color = findViewById(R.id.rg_paint_color);
        mBtnRevoke = findViewById(R.id.btn_revoke);
        mBtnClean = findViewById(R.id.btn_clean);
        mBtnPicselect = findViewById(R.id.btn_picselect);
        mBtnDrawCycle = findViewById(R.id.btn_drawcycle);
        mBtnDrawRec = findViewById(R.id.btn_drawrec);
        mBtnDrawRrow = findViewById(R.id.btn_drawarrow);
        mBtnSaved = findViewById(R.id.btn_savesd);
        mBack = findViewById(R.id.back);
        mNext = findViewById(R.id.next);
        mOpen = findViewById(R.id.open);
        mBottom = findViewById(R.id.ll_bottom);
        mVideoView = findViewById(R.id.videoView);
        mTextView = findViewById(R.id.textView);
        mJxBack = findViewById(R.id.jx_back);
        mJxNext = findViewById(R.id.jx_next);
    }

    /**
     * 播放视频
     */
    private void setupVideo() {
        Uri uri = Uri.parse(videoUrl);
        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mediaController.setVisibility(View.GONE);
        mVideoView.setVideoURI(uri);
        mVideoView.start();
    }

    /**
     * 拖动视频
     */
    private void initDragView() {

        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("zhangbin----","5555555555555555555");
            }
        });
        mVideoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Log.e("zhangbin----","66666666666666666");
                return false;
            }
        });
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
        mBack.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mOpen.setOnClickListener(this);
        mVideoView.setOnPreparedListener(this);
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
                    // tuyaView.setPaintSize("1",paintSizeValue);
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
                    //tuyaView.setReaserSize("1",eRaserSizeValue);
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
                    selectPaintColorAndSetting(Color.parseColor(mPaintColorValue));
                } else {
                    selectPaintColorAndSetting(Color.parseColor("#DC143C"));
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
     /*   mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });*/

    }

    /**
     * 监听事件
     */
    private void initListener() {
        mPaintStyle.setOnClickListener(this);//画笔的监听
        mEraserStyle.setOnClickListener(this);//橡皮的监听
        ll_paintcolor_state.setOnClickListener(this);
        mBtnSaved.setOnClickListener(this);
        mBtnRevoke.setOnClickListener(this);
        mBtnClean.setOnClickListener(this);
        mBtnPicselect.setOnClickListener(this);
        mBtnDrawCycle.setOnClickListener(this);
        mBtnDrawRec.setOnClickListener(this);
        mBtnDrawRrow.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mJxBack.setOnClickListener(this);
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
            case R.id.back:
                /*mWebView.post(new Runnable() {
                    @Override
                    public void run() {

                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
                        mWebView.loadUrl("javascript:callJS()");
                    }
                });*/
                mWebView.evaluateJavascript("javascript:LastSlide()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
                break;
            case R.id.open:
                aboutOpenSetting();
                break;
            case R.id.next:
                mWebView.evaluateJavascript("javascript:NextSlide()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
                break;
            //画笔
            case R.id.iv_paintstyle:
                aboutPaintStyleSetting();
                break;
            //橡皮
            case R.id.iv_reaserstyle:
                aboutReaserStyleSetting();
                break;
            /*//颜色
            case R.id.iv_paint_color:
                changeColorAndSizeState(View.VISIBLE, View.GONE);
                rg_paint_color.setOnCheckedChangeListener(this);
                break;*/
            //画笔大小
            /*case R.id.iv_paint_size:
                changeColorAndSizeState(View.GONE, View.VISIBLE);
                break;*/
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
    private void changeColorAndSizeState(int visible, int gone) {
        ll_paintcolor_state.setVisibility(visible);

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
        changeColorAndSizeState(View.GONE, View.GONE);
        if (mEraserStyle.getVisibility() != View.VISIBLE) {
            if (isPaint) {//当前为画笔,点击后变为橡皮擦
                paintStyleSettingDesc(R.drawable.reaser_style, select_paint_style_eraser, false);
            } else {
                paintStyleSettingDesc(R.drawable.paint_style, select_paint_style_paint, true);
                tuyaView.drawGraphics(DRAW_PATH);
            }
        } else {
            paintStyleSettingDesc(R.drawable.paint_style, select_paint_style_paint, true);
            tuyaView.drawGraphics(DRAW_PATH);
        }
    }

    //橡皮样式设置
    private void aboutReaserStyleSetting() {
        changeColorAndSizeState(View.GONE, View.GONE);
        reaserStyleSettingDesc(select_paint_style_eraser, false);
    }

    //画笔样式设置详情
    private void reaserStyleSettingDesc(int paintStyle, boolean styleTarget) {
        tuyaView.selectPaintStyle(paintStyle);
        isPaint = styleTarget;
    }

    //画笔样式设置详情
    private void paintStyleSettingDesc(int paintStyleResouce, int paintStyle, boolean styleTarget) {
        mPaintStyle.setBackgroundResource(paintStyleResouce);
        tuyaView.selectPaintStyle(paintStyle);
        isPaint = styleTarget;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String value;
        switch (checkedId) {
            //处理颜色
            case R.id.rb_purple:
                //value = "#E372FF";
                value = "#0000FF";
                selectPaintColorAndSetting(Color.parseColor(value));
                break;
            case R.id.rb_orange:
                value = "#FE7C2E";
                selectPaintColorAndSetting(Color.parseColor(value));
                break;
            case R.id.rb_green:
                value = "#6CD685";
                selectPaintColorAndSetting(Color.parseColor(value));
                break;
            case R.id.rb_yellow:
                value = "#FFB42B";
                selectPaintColorAndSetting(Color.parseColor(value));
                break;
            case R.id.rb_black:
                value = "#000000";
                selectPaintColorAndSetting(Color.parseColor(value));
                break;
        }
    }

    //选择画笔颜色
    private void selectPaintColorAndSetting(int setColor) {
        // tuyaView.setPaintColor("1",setColor);
        ll_paintcolor_state.setVisibility(View.GONE);
    }
}
