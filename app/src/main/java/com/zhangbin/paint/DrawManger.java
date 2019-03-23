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

    public DrawManger SetOrder(OrderBean orderBean) {

        this.orderBean = orderBean;
        return this;
    }

    public DrawManger NextOrder() {


        if (cur >= listorderBean.size() - 1) {
            this.orderBean = null;
            return this;
        }
        cur = cur + 1;
        this.orderBean = (OrderBean) listorderBean.get(cur);
        return this;

    }


    public void ExecuteOrder() {
        if (orderBean == null) {
            return;
        }

        Log.i("orderorder", gson.toJson(orderBean));

        if (!orderBean.type.equals("")) {
            int type = Util.toInteger(orderBean.type);
            Log.i("itemorderorder", orderBean.type);
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
                    //this.view.setPanitPath(lst);
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
                   // this.view.orderDrawLIne(this.orderBean.getUuid(),false,this.orderBean.getX1(), this.orderBean.getY1(), this.orderBean.getX2(), this.orderBean.getY2());
                    break;
                case 406:
                    //画虚线
                   // this.view.orderDrawDashLine(this.orderBean.getUuid(),false,this.orderBean.getX1(), this.orderBean.getY1(), this.orderBean.getX2(), this.orderBean.getY2());
                    break;
                case 407:
                    //画矩形
                    this.view.orderDrawLRectangle(this.orderBean.uuid,false,this.orderBean.x1, this.orderBean.y1, this.orderBean.x2, this.orderBean.y2);
                    break;
                case 408:
                    //画椭圆
                    this.view.orderDrawLRectangle(this.orderBean.uuid,false,this.orderBean.x1, this.orderBean.y1, this.orderBean.x2, this.orderBean.y2);
                    //this.view.orderDrawCircle(this.orderBean.getUuid(),false,this.orderBean.getX1(), this.orderBean.getY1(), this.orderBean.getX2(), this.orderBean.getY2());
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
                    this.view.undo();
                    break;
                case 502:
                    this.view.recover();
                    break;
                case 503:
                    this.webView.evaluateJavascript("javascript:JumpPage(" + this.orderBean.currentPage + "," + this.orderBean.currentAnimation + ",1)", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                   // this.view.setPage(this.orderBean.getCurrentPage());
                    break;
                case 504:
                    this.webView.evaluateJavascript("javascript:LastSlideS(" + this.orderBean.currentPage + "," + this.orderBean.currentAnimation + ")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                   // this.view.setPage(this.orderBean.getCurrentPage());
                    break;
                case 505:
                    this.webView.evaluateJavascript("javascript:NextSlideS(" + this.orderBean.currentPage + "," + this.orderBean.currentAnimation + ")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                    //this.view.setPage(this.orderBean.getCurrentPage());
                    break;
            }


        }


    }


    public void setListorderBean(ArrayList<OrderBean> listorderBean) {
        this.listorderBean = listorderBean;
    }
}
