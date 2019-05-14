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
    private int si;
    @SerializedName("t")
    private String type;
    @SerializedName("s")
    private long startTime;
    @SerializedName("w")
    private float w;
    @SerializedName("h")
    private float h;
    /**
     * v : 6
     */

    @SerializedName("v")
    private String value;
    /**
     * cp : 3
     * ca : 5
     */

    @SerializedName("cp")
    private int currentPage;
    @SerializedName("ca")
    private int currentAnimation;
    /**
     * e : 223434349
     * th : 30
     * pc : ffffff
     * d : [{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":364,"y":110},{"x":361,"y":110}]
     */

    @SerializedName("e")
    private long endTime;
    @SerializedName("th")
    private int strokeWidth;
    @SerializedName("pc")
    private String penColor;
    @SerializedName("d")
    private List<DataBean> data;
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
    private float x;
    @SerializedName("y")
    private float y;
    @SerializedName("u")
    private String uuid;
    @SerializedName("tx")
    private String text;
    /**
     * x1 : 100.0
     * y1 : 100.0
     * x2 : 100.0
     * y2 : 100.0
     */

    @SerializedName("x1")
    private float x1;
    @SerializedName("y1")
    private float y1;
    @SerializedName("x2")
    private float x2;
    @SerializedName("y2")
    private float y2;

    /**
     * e : 223434349
     * d : [{"x":364,"y":110},{"x":361,"y":110}]
     */
    public static OrderBean objectFromData(String str) {

        return new Gson().fromJson(str, OrderBean.class);
    }

    public int getSi() {
        return si;
    }

    public void setSi(int si) {
        this.si = si;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(int currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getPenColor() {
        return penColor;
    }

    public void setPenColor(String penColor) {
        this.penColor = penColor;
    }


    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<DataBean> getData() {
        return data;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public static class DataBean {
        /**
         * x : 364
         * y : 110
         */

        @SerializedName("x")
        private float x;
        @SerializedName("y")
        private float y;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
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



