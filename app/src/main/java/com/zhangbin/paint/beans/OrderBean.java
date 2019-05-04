package com.zhangbin.paint.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderBean {

    /**
     * si : 0
     * t : 300
     * s : 223434343
     * w : 640
     * h : 480
     */

    @SerializedName("si")
    public int si;
    @SerializedName("t")
    public String type;
    @SerializedName("s")
    public long startTime;
    @SerializedName("w")
    public float w;
    @SerializedName("h")
    public float h;
    /**
     * v : 6
     */

    @SerializedName("v")
    public String value;
    /**
     * cp : 3
     * ca : 5
     */

    @SerializedName("cp")
    public int currentPage;
    @SerializedName("ca")
    public int currentAnimation;
    /**
     * e : 223434349
     * th : 30
     * pc : ffffff
     * d : [{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":361,"y":110}]
     */

    @SerializedName("e")
    public long endTime;
    @SerializedName("th")
    public int strokeWidth;
    @SerializedName("pc")
    public String penColor;
    @SerializedName("d")
    public List<DataBean> data;
    /**
     * e : 223434349
     * x : 5.1
     * y : 5.2
     * w : 10.1
     * h : 10.1
     * u : 2012
     * tx : 文本输入
     */

    @SerializedName("x")
    public float x;
    @SerializedName("y")
    public float y;
    @SerializedName("u")
    public String uuid;
    @SerializedName("tx")
    public String text;
    /**
     * x1 : 100.0
     * y1 : 100.0
     * x2 : 100.0
     * y2 : 100.0
     */

    @SerializedName("x1")
    public float x1;
    @SerializedName("y1")
    public float y1;
    @SerializedName("x2")
    public float x2;
    @SerializedName("y2")
    public float y2;

    /**
     * e : 223434349
     * d : [{"x":364,"y":110},{"x":361,"y":110}]
     */
    public static OrderBean objectFromData(String str) {

        return new Gson().fromJson(str, OrderBean.class);
    }


    public static class DataBean {
        /**
         * x : 364
         * y : 110
         */

        @SerializedName("x")
        public float x;
        @SerializedName("y")
        public float y;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "si=" + si +
                ", type='" + type + '\'' +
                ", startTime=" + startTime +
                ", w=" + w +
                ", h=" + h +
                ", value='" + value + '\'' +
                ", currentPage=" + currentPage +
                ", currentAnimation=" + currentAnimation +
                ", endTime=" + endTime +
                ", strokeWidth=" + strokeWidth +
                ", penColor='" + penColor + '\'' +
                ", data=" + data +
                ", x=" + x +
                ", y=" + y +
                ", uuid='" + uuid + '\'' +
                ", text='" + text + '\'' +
                ", x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}';
    }
}



