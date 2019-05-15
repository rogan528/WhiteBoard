package com.zhangbin.paint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.ActivityUtil;
import com.zhangbin.paint.util.DimensionUtils;
import com.zhangbin.paint.util.ScreenSwitchUtils;
import com.zhangbin.paint.util.Util;
import com.zhangbin.paint.video.view.DragFrameLayout;
import com.zhangbin.paint.whiteboard.presenter.WhiteboardPresenter;
import com.zhangbin.paint.whiteboard.OrderDrawManger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tv.danmaku.ijk.media.example.callback.OnGetMediaPlayInterface;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkDragVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class MainActivity extends Activity implements View.OnClickListener{
    private FrameLayout pptLayout;
    private DragFrameLayout dragFrameLayout;
    private WhiteboardPresenter whiteboardPresenter;
    private int screenWidth;
    private int screenHeight;
    private int realHeight;//控件真实高度，去除头部标题后的
    private IjkDragVideoView mDragIjkVideoView;
    private String ijkVideoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private AndroidMediaController mMediaController;
    private Context mContext;
    private ArrayList<OrderBean> listOrderBean;
    private OrderDrawManger orderDrawManger;
    private Handler handler = new Handler();
    protected boolean isTitleBarShow = false;
    protected boolean isLongShowTitleBar = false;
    //是否是VIP
    public static String IS_VIP = "isVip";
    private boolean isVip;
    //用户id和用户名
    public static String USER_ID = "userId";
    public static String USER_NAME = "userName";
    private String userId,userName;
    private ScheduledExecutorService lance;
    private LinearLayout titlebarContainer;
    private Toast mToast;
    private long preClickTime = 0L;
    private InputMethodManager imm;
    private ImageView mImageVideoView;
    int mDefaultRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        isVip = getIntent().getBooleanExtra(IS_VIP, false);
        userId = getIntent().getStringExtra(USER_ID);
        userName = getIntent().getStringExtra(USER_NAME);
        mToast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
        float scale = getResources().getDisplayMetrics().density;
        initView();
        initData();
        playiJKVideo();
        initAssetsData();
        initOrderData();
        showTitleBar();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        pptLayout =  findViewById(R.id.pptLayout);
        dragFrameLayout =  findViewById(R.id.dragframeLayout);
        titlebarContainer = findViewById(R.id.title_bar);
        mImageVideoView = findViewById(R.id.iv_videoView);
        findViewById(R.id.jx_next).setOnClickListener(this);
        findViewById(R.id.undo).setOnClickListener(this);
        findViewById(R.id.redo).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.iv_go_back).setOnClickListener(this);
        findViewById(R.id.is_fullscreen).setOnClickListener(this);
        findViewById(R.id.pptLayout).setOnClickListener(this);
    }
    /**
     * 播放IJK视频
     */
    private void playiJKVideo() {
        mDragIjkVideoView = findViewById(R.id.ijk_videoView);
        Uri uri = Uri.parse(ijkVideoUrl);
        mMediaController = new AndroidMediaController(this, false);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mMediaController.setVisibility(View.VISIBLE);
        mDragIjkVideoView.setMediaController(mMediaController);
        if (TextUtils.isEmpty(ijkVideoUrl)) {
            mToast.setText("没有发现视频，请退出！");
            mToast.show();
        } else {
            mDragIjkVideoView.setVideoURI(uri);
            dragFrameLayout.setVisibility(View.VISIBLE);
            mDragIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    DecimalFormat df = new DecimalFormat("0.00");//格式化小数
                    float ratio = Float.parseFloat(df.format((float)mp.getVideoWidth()/mp.getVideoHeight()));
                    ViewGroup.LayoutParams lp;
                    lp = mDragIjkVideoView.getLayoutParams();
                    lp.width = mDragIjkVideoView.getWidth();
                    lp.height =  Math.round(mDragIjkVideoView.getWidth()/ratio);
                    mDragIjkVideoView.setLayoutParams(lp);
                    mDragIjkVideoView.setVisibility(View.VISIBLE);
                    mDragIjkVideoView.start();
                    mHandler.sendEmptyMessageDelayed(DRAG_SHOW,1500);

                }
            });
        }
        mDragIjkVideoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }
    /**
     * 初始化数据
     */
    private void initData() {
        whiteboardPresenter = new WhiteboardPresenter(mContext,pptLayout);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        screenWidth = defaultDisplay.getWidth();
        screenHeight = defaultDisplay.getHeight();
        ScreenSwitchUtils.getInstance(MainActivity.this).start(MainActivity.this);
        realHeight = screenHeight;
        orderDrawManger = new OrderDrawManger(whiteboardPresenter);
        updateLayout();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            Glide.with(MainActivity.this)
                    .setDefaultRequestOptions(
                            new RequestOptions()
                                    .frame(1000000)
                                    .centerCrop()
                                    .error(mDefaultRes)
                                    .placeholder(mDefaultRes))
                    .load(ijkVideoUrl)
                    .into(mImageVideoView);
        }catch (Exception e){
           e.printStackTrace();
        }

    }
    /**
     * 使用本地json数据解析
     */
    private void initAssetsData() {
        String input = Util.readFileFromAssets(this, "LiveClientNew.json");
        Gson gson = new Gson();
        listOrderBean = gson.fromJson(input, new TypeToken<ArrayList<OrderBean>>() {
        }.getType());
        orderDrawManger.setListorderBean(listOrderBean);
    }

    /**
     * 回调白板指令
     */
    private void initOrderData() {
        String text = mDragIjkVideoView.getJsonMsg();
        executeOrder(text);
        mDragIjkVideoView.setCalReCallBackListenner(new OnGetMediaPlayInterface() {
            @Override
            public void GetMediaPlayerText(String text) {
                    executeOrder(text);
            }
        });
    }

    /**
     * 处理指令的方法
     * @param text 传递过来的内容
     */
    private void executeOrder(String text){
        if (text != null && !"".equals(text)) {
            Gson gson = new Gson();
            OrderBean orderBean = gson.fromJson(text, OrderBean.class);
            orderDrawManger.SetOrder(orderBean).ExecuteOrder();
        }
    }
    private static final int MSG_UPDATE_BOARD = 1;
    private static final int DRAG_SHOW = 2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DRAG_SHOW: {
                    mImageVideoView.setVisibility(View.GONE);
                    dragFrameLayout.setIsDrag(true);
                    mHandler.removeMessages(DRAG_SHOW);
                }
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_go_back:
                gobackAction();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                }
                break;
            case R.id.jx_next:
                orderDrawManger.NextOrder().ExecuteOrder();
                break;
            case R.id.undo:
                whiteboardPresenter.undo();
                break;
            case R.id.redo:
                whiteboardPresenter.redo();
                break;
            case R.id.clear:
                whiteboardPresenter.orderClear();
                break;
            case R.id.is_fullscreen: //全屏
                switchFullScreen();
                break;
            case R.id.pptLayout:
                if (System.currentTimeMillis() - preClickTime < 300) {  //双击全屏
                    switchFullScreen();
                    return;
                }
                preClickTime = System.currentTimeMillis();
                if (isTitleBarShow) {
                    isLongShowTitleBar = false;
                    hideTitleBar();
                } else {
                    //刚进来显示，三秒之后自动隐藏
                    isTitleBarShow = true;
                    showTitleBar();
                }
                break;
        }
    }



    /**
     * 显示标题栏和操作按钮
     */
    protected final void showTitleBar() {
        if (lance != null && !lance.isShutdown())
            lance.shutdown();
        titlebarContainer.setVisibility(View.VISIBLE);
        isTitleBarShow = true;
        autoDismissTitleBar();
    }

    /**
     * 标题栏计时器. 3秒后自动隐藏
     */
    protected void autoDismissTitleBar() {
        stopDismissTitleBar();
        Runnable sendBeatRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTitleBarShow) {
                    if (lance != null && !lance.isShutdown() && !isLongShowTitleBar) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isTitleBarShow) {
                                    hideTitleBar();
                                } else {
                                    stopDismissTitleBar();
                                }
                            }
                        });
                    }
                }
            }
        };

        lance = Executors.newSingleThreadScheduledExecutor();
        lance.scheduleAtFixedRate(sendBeatRunnable, 5, 5, TimeUnit.SECONDS);
    }

    protected void stopDismissTitleBar() {
        if (lance != null) {
            if (!lance.isShutdown()) {
                lance.shutdown();
            }
            lance = null;
        }
    }

    /**
     * 隐藏标题栏和操作按钮
     */
    protected final void hideTitleBar() {
        if (isLongShowTitleBar)
            return;
        stopDismissTitleBar();
        if (titlebarContainer == null)
            return;
        titlebarContainer.setVisibility(View.GONE);
        isTitleBarShow = false;
    }
    /**
     * 全屏。非全屏切换
     */
    public void switchFullScreen() {
        onFullScreenChange();

    }
    /**
     * 全屏和非全屏切换
     */
    public void onFullScreenChange() {
        ScreenSwitchUtils.getInstance(this).setIsFullScreen(!ScreenSwitchUtils.getInstance(this).isFullScreen());
        if (ScreenSwitchUtils.getInstance(this).isSensorSwitchLandScreen()) {  //重力切换的横屏的话
            updateLayout();
        } else {
            ScreenSwitchUtils.getInstance(this).toggleScreen();
        }
    }
    /**
     * 返回
     * @return
     */
    public void gobackAction() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScreenSwitchUtils.getInstance(MainActivity.this).toggleScreen(false);
        } else {
            showExitDialog();
        }
    }

    /**
     * 退出的dialog
     */
    private void showExitDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("是否确认退出?");

        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        ScreenSwitchUtils.getInstance(MainActivity.this).stop();
                        exitActivity();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        normalDialog.show();
    }

    /**
     * 退出并停止视频,停止IM服务
     */
    private void exitActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IjkMediaPlayer.native_profileEnd();
                handler.removeCallbacksAndMessages(null);
                mDragIjkVideoView.stopBackgroundPlay();
            }
        }).start();
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

    /**
     * 更新布局
     */
    public void updateLayout() {
        int width = DimensionUtils.getScreenWidth(this);
        int height = DimensionUtils.getScreenHeight(this);
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
            pptLayout.setLayoutParams(pptParams);

        }
    }
    @Override
    public void onBackPressed() {
        gobackAction();
    }

    @Override
    protected void onStop() {
        IjkMediaPlayer.native_profileEnd();
        handler.removeCallbacksAndMessages(null);
        mDragIjkVideoView.stopBackgroundPlay();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
