package com.zhangbin.paint.whiteboard;

import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;
import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.Util;
import com.zhangbin.paint.whiteboard.presenter.WhiteboardPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DrawManger
 * @Description TODO
 * @Author yangjie
 * @Date 2019/3/20 下午1:13
 */
public class OrderDrawManger {

    private WhiteboardPresenter whiteboardPresenter;
    private Gson gson = new Gson();
    private int cur=-1;

    private OrderBean orderBean;
    private ArrayList listorderBean = new ArrayList<OrderBean>();


    public OrderDrawManger(WhiteboardPresenter whiteboardPresenter) {
        this.whiteboardPresenter = whiteboardPresenter;
    }

    public OrderDrawManger SetOrder(OrderBean orderBean) {

        this.orderBean = orderBean;
        return this;
    }

    public OrderDrawManger NextOrder() {


        if (cur >= listorderBean.size() - 1) {
            this.orderBean = null;
            return this;
        }
        cur = cur + 1;
        this.orderBean = (OrderBean) listorderBean.get(cur);

        return this;

    }

    public OrderDrawManger PreOrder() {
        if (cur <= 0) {
            this.orderBean = null;
            return this;
        }
        cur = cur - 1;
        this.orderBean = (OrderBean) listorderBean.get(cur);
        return this;


    }


    public void ExecuteOrder() {
        if (orderBean == null) {
            return;
        }
        Log.e("orderorder","toString--"+orderBean.toString());
        if (!orderBean.type.equals("")) {
            int type = Util.toInteger(orderBean.type);
            Log.i("itemorderorder", "--type--"+type);
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
                    whiteboardPresenter.setPaintSize(Float.parseFloat(this.orderBean.value));
                    break;
                case 306:
                    whiteboardPresenter.setPaintColor(Color.parseColor(this.orderBean.value));
                    break;
                case 307:
                    whiteboardPresenter.setReaserSize(Float.parseFloat(this.orderBean.value));
                    break;
                case 308:
                    //文字大小
                    whiteboardPresenter.setTextSize(Integer.parseInt(this.orderBean.value));
                    break;
                case 309:
                    //文字颜色
                    whiteboardPresenter.setTextColor(Color.parseColor(this.orderBean.value));
                    break;
                case 400:
                case 401:
                case 402:
                case 403:
                case 404:
                case 405:
                case 406:
                case 407:
                case 408:
                case 409:
                    whiteboardPresenter.addDrawData(orderBean);
                    break;
                case 410:
                    whiteboardPresenter.openDraftPaper();
                    break;
                case 411:
                    whiteboardPresenter.closeDraftPaper();
                    break;
                case 412:
                    break;
                case 413:
                    whiteboardPresenter.setBackgroundColor(this.orderBean.value);
                    break;
                case 414:
                    whiteboardPresenter.addDraftPaper();
                    break;
                case 500:
                    whiteboardPresenter.orderClear();
                    break;
                case 501:
                    whiteboardPresenter.undo();
                    break;
                case 502:
                    whiteboardPresenter.redo();
                    break;
                case 503:
                    //跳转指定页
                    whiteboardPresenter.jumpPage(this.orderBean.currentPage, this.orderBean.currentAnimation);
                    break;
                case 504:
                    //上一页
                    whiteboardPresenter.lastSlideS(this.orderBean.currentPage, this.orderBean.currentAnimation);
                    break;
                case 505:
                    //下一页
                    whiteboardPresenter.nextSlideS(this.orderBean.currentPage, this.orderBean.currentAnimation);
                    break;
            }


        }
    }


    public void setListorderBean(ArrayList<OrderBean> listorderBean) {
        this.listorderBean = listorderBean;
    }
}
