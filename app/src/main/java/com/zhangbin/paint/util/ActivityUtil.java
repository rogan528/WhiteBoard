package com.zhangbin.paint.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtil {
    /**
     * @param activity
     * @return 判断当前手机是否是全屏
     */
    public static boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        if((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 设置Activity是否全屏
     * @param activity
     * @param fullScreen
     */
    public static void setFullScreen(Activity activity, boolean fullScreen){
        if(activity == null)
            return;
        Window window = activity.getWindow();
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if(fullScreen){
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            attrs.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        }else{
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        window.setAttributes(attrs);
    }

    /**
     *
     * @param activity
     * @return 判断当前手机是否是竖屏
     */
    public static boolean isPortrait(Activity activity){
        int orientation = activity.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            return true;
        }else {
            return false;
        }
    }


    public static void jump(Context context, Class mActivity) {
        jump(context, mActivity, null);

    }

    public static void jump(Context context, Class mActivity, Bundle bundle) {
        Intent mIntent = new Intent(context, mActivity);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        context.startActivity(mIntent);

    }


}
