package com.zhangbin.paint;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * @Description:
 * @Author: 张彬
 * @CreateDate: 2019-05-15
 * @QQ: 3097940262
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "506b230cda", true);
    }
}
