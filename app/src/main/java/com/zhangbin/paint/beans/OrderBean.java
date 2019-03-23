package com.zhangbin.paint.beans;

import com.google.gson.Gson;

import java.util.List;

/**
 * @ClassName OrderBean
 * @Description TODO
 * @Author 张彬
 * @Date 2019/3/20 上午11:23
 */
public class OrderBean {


    /**
     * endTime : 1553152551256
     * startTime : 1553152549836
     * type : 407
     * uuid : {5c34196e-0128-4b5a-8c43-e7a69361f381}
     * x1 : -352
     * x2 : 329
     * y1 : -141
     * y2 : 174
     * value : 45
     * x : -14
     * y : 36
     * data : [{"x":-385,"y":-184},{"x":-379,"y":-186},{"x":-370,"y":-190},{"x":-364,"y":-192},{"x":-355,"y":-195},{"x":-337,"y":-198},{"x":-325,"y":-200},{"x":-304,"y":-203},{"x":-283,"y":-207},{"x":-268,"y":-208},{"x":-259,"y":-210},{"x":-256,"y":-210}]
     * currentAnimation : 1
     * currentPage : 2
     * h : 27
     * text :
     * w : 8
     */

    public long endTime;
    public long startTime;
    public String type;
    public String uuid;
    public int x1;
    public int x2;
    public int y1;
    public int y2;
    public String value;
    public int x;
    public int y;
    public int currentAnimation;
    public int currentPage;
    public int h;
    public String text;
    public int w;
    public List<DataBean> data;

    public static OrderBean objectFromData(String str) {

        return new Gson().fromJson(str, OrderBean.class);
    }

    public static class DataBean {
        /**
         * x : -385
         * y : -184
         */

        public int x;
        public int y;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }
    }
}



