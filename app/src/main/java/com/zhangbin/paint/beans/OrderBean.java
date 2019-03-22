package com.zhangbin.paint.beans;

import java.util.List;

/**
 * @ClassName OrderBean
 * @Description TODO
 * @Author yangjie
 * @Date 2019/3/20 上午11:23
 */
public class OrderBean {
    /**
     * data : [{"x":173,"y":-18},{"x":170,"y":-20},{"x":165,"y":-23},{"x":158,"y":-26},{"x":152,"y":-27},{"x":139,"y":-31},{"x":124,"y":-31},{"x":98,"y":-36},{"x":65,"y":-38},{"x":30,"y":-40},{"x":-8,"y":-40},{"x":-51,"y":-40},{"x":-93,"y":-40},{"x":-135,"y":-40},{"x":-177,"y":-39},{"x":-218,"y":-32},{"x":-253,"y":-32},{"x":-287,"y":-29},{"x":-316,"y":-25},{"x":-340,"y":-21},{"x":-354,"y":-18},{"x":-365,"y":-16},{"x":-372,"y":-14},{"x":-376,"y":-13},{"x":-377,"y":-13},{"x":-377,"y":-12}]
     * endTime : 1552979571330
     * startTime : 1552979571017
     * type : 401
     */

    //结速时间
    private long endTime;
    //开始时间
    private long startTime;
    private String type;
    //类型
    private List<DataBean> data;
    //非数组当前值
    private String value;
    //当前页
    private int currentPage;
    //当前动画
    private int currentAnimation;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String text;
    private String uuid;
    private float x;
    private float y;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float w;
    private float h;


    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
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

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
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

    public static class DataBean {
        /**
         * x : 173
         * y : -18
         */

        private int x;
        private int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}



