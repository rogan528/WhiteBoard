package com.zhangbin.paint;

import android.graphics.Color;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.zhangbin.paint.beans.OrderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DrawManger
 * @Description 操作json指令的管理类
 * @Author 张彬
 * @Date 2019/3/20 下午1:13
 */
public class DrawManger {

    private GraffitiView view;
    private WebView webView;
    private Gson gson = new Gson();

    private int cur;

    private OrderBean orderBean;
    private ArrayList listorderBean = new ArrayList<OrderBean>();


    public DrawManger(GraffitiView view, WebView webView) {
        this.view = view;
        this.webView = webView;
    }

    public DrawManger NextOrder() {


        if (cur > listorderBean.size() - 1) {
            this.orderBean = null;
            return this;
        }

        this.orderBean = (OrderBean) listorderBean.get(cur);
        cur = cur + 1;
        return this;

    }


    public void ExecuteOrder() {
        if (orderBean == null) {
            return;
        }

        Log.i("orderorder", gson.toJson(orderBean));

        if (!orderBean.type.equals("")) {
            int type = Util.toInteger(orderBean.type);
            Log.i("itemorderorder", orderBean.type+"----"+this.orderBean.uuid);
            List<OrderBean.DataBean> lst = this.orderBean.data;
            switch (type) {
                case 300:
                    break;
                case 301:
                    break;
                case 302:
                    break;
                case 303:
                    break;
                case 304:
                    break;
                case 305:
                    this.view.setPaintSize(Float.parseFloat(this.orderBean.value+""));
                    break;
                case 306:
                   this.view.setPaintColor(Color.parseColor(this.orderBean.value));
                    break;
                case 307:
                    this.view.setEraserSize(Float.parseFloat(this.orderBean.value+""));
                    break;
                case 308:
                    break;
                case 309:
                   // this.view.setCharacterColor(this.orderBean.getUuid(),Color.parseColor(this.orderBean.getValue()));
                    break;
                case 400:
                    //设置画笔路径
                    this.view.orderDraw(lst);
                    break;
                case 401:
                    //设置橡皮路径
                   this.view.setReaserPath(this.orderBean.uuid,lst);
                    break;
                case 402:
                    break;
                case 403:
                    break;
                case 404:
                    break;
                case 405:
                    //画直线
                    this.view.orderDrawLIne(this.orderBean.uuid,false,this.orderBean.x1, this.orderBean.y1, this.orderBean.x2, this.orderBean.y2);
                    break;
                case 406:
                    //画虚线
                    this.view.orderDrawDashLine(this.orderBean.uuid,false,this.orderBean.x1, this.orderBean.y1, this.orderBean.x2, this.orderBean.y2);
                    break;
                case 407:
                    //画矩形
                    this.view.orderDrawLRectangle(this.orderBean.uuid,false,this.orderBean.x1, this.orderBean.y1, this.orderBean.x2, this.orderBean.y2);
                    break;
                case 408:
                    //画椭圆
                    this.view.orderDrawCircle(this.orderBean.uuid,false,this.orderBean.x1, this.orderBean.y1, this.orderBean.x2, this.orderBean.y2);
                    break;
                case 409:
                    //拖拽操作
                    //this.view.orderDragView(this.orderBean.getUuid(),this.orderBean.getX(), this.orderBean.getY());
                    break;
                case 410:
                    break;
                case 411:
                    break;
                case 412:
                    break;
                case 413:
                    break;
                case 414:
                    break;
                case 500:
                    this.view.clearAnimation();
                    break;
                case 501:
                    //撤销
                    this.view.undo();
                    break;
                case 502:
                    //回退
                    this.view.recover();
                    break;
            }


        }


    }


    public void setListorderBean(ArrayList<OrderBean> listorderBean) {
        this.listorderBean = listorderBean;
    }
}
