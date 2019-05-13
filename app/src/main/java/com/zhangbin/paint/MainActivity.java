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
    private Button mJxNext;//下一步
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
        initDragView();

    }
    /**
     * 初始化控件
     */
    private void initView() {
        mTextView = findViewById(R.id.textView);
        mJxNext = findViewById(R.id.jx_next);
        pptLayout =  findViewById(R.id.pptLayout);
        mJxNext.setOnClickListener(this);
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
        String input = Util.readFileFromAssets(this, "LiveClientNew.json");
        Gson gson = new Gson();
        orderDrawManger = new OrderDrawManger(whiteboardPresenter);
        listOrderBean = gson.fromJson(input, new TypeToken<ArrayList<OrderBean>>() {
        }.getType());
        orderDrawManger.setListorderBean(listOrderBean);
        updateLayout();
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
        }
    }
}
