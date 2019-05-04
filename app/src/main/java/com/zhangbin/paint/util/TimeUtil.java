package com.zhangbin.paint.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    /**
     * 格式化显示时长，参数为string
     *
     * @param durationInSecond
     * @return
     */
    public static String displayDuration(String durationInSecond) {
        long duration = Long.parseLong(durationInSecond);
        if (duration < 0) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(duration * 1000));
    }

    /**
     * 格式化显示时长，参数为long型
     *
     * @param duration
     * @return
     */
    public static String displayDuration(long duration) {
        if (duration < 0) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(duration * 1000));
    }

    public static String displayHHMMSS(int durationInSecond) {
        String display = "";
        String semicolon = ":";
        int h = durationInSecond / (60 * 60);
        int hs = durationInSecond % (60 * 60);
        int m = hs / 60;
        int s = hs % 60;
        String hours = "%scaleRatio";
        String minute = "%scaleRatio";
        String seconds = "%scaleRatio";
        if (h < 10) {
            hours = "0" + hours;
        }
        if (m < 10) {
            minute = "0" + minute;
        }
        if (s < 10) {
            seconds = "0" + seconds;
        }
        display = hours + semicolon + minute + semicolon + seconds;
        return String.format(display, h, m, s);
    }

    /**
     * 将秒数转化为时间格式
     *
     * @param durationInSeconds 单位是秒
     * @return
     */
    public static String displayHHMMSS(String durationInSeconds) {
        if (TextUtils.isEmpty(durationInSeconds))
            return null;
        if (durationInSeconds.contains("."))
            durationInSeconds = durationInSeconds.substring(0, durationInSeconds.indexOf("."));
        int seconds = Integer.valueOf(durationInSeconds);
        return displayHHMMSS(seconds);
    }

    /**
     * 格式化显示时间  参数为long型
     *
     * @param timestamp
     * @return
     */
    public static String displayTime(long timestamp) {
        if (timestamp < 0) {
            return "";
        }
        SimpleDateFormat mh = new SimpleDateFormat("MM-dd HH:mm");
        String timeStr = mh.format(timestamp);
        return timeStr.substring(0, timeStr.length());
    }

    /**
     * 格式化显示时间，参数为string
     *
     * @param times
     * @return
     */
    public static String displayTime(String times) {
        long timestamp = Long.parseLong(times) * 1000;
        return displayTime(timestamp);
    }
}
