package com.zhangbin.paint.whiteboard;

import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;
import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.OperationUtils;
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
        if (!orderBean.getType().equals("")) {
            int type = Util.toInteger(orderBean.getType());
            List<OrderBean.DataBean> lst = this.orderBean.getData();
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
                    //设置画笔大小
                    whiteboardPresenter.setPaintSize(Float.parseFloat(this.orderBean.getValue()));
                    break;
                case 306:
                    //设置画笔颜色
                    whiteboardPresenter.setPaintColor(Color.parseColor(this.orderBean.getValue()));
                    break;
                case 307:
                    //设置橡皮大小
                    whiteboardPresenter.setReaserSize(Float.parseFloat(this.orderBean.getValue()));
                    break;
                case 308:
                    //文字大小
                    whiteboardPresenter.setTextSize(Integer.parseInt(this.orderBean.getValue()));
                    break;
                case 309:
                    //文字颜色
                    whiteboardPresenter.setTextColor(Color.parseColor(this.orderBean.getValue()));
                    break;
                //400画笔401橡皮402创建文字403编辑文字404移动文字
                // 405画线406画虚线407画矩形408画圆409图形移动
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
                    //打开草稿纸
                    whiteboardPresenter.openDraftPaper(OperationUtils.getInstance().mEndDraftPage);
                    break;
                case 411:
                    //关闭草稿纸
                    whiteboardPresenter.closeDraftPaper();
                    break;
                case 412:
                    //翻页草稿纸
                    whiteboardPresenter.changeDraftPaper(Integer.parseInt(this.orderBean.getValue())+OperationUtils.getInstance().mStartDraftPage);
                    break;
                case 413:
                    //设置背景色
                    whiteboardPresenter.setBackgroundColor(this.orderBean.getValue());
                    break;
                case 414:
                    //新建草稿纸
                    whiteboardPresenter.addDraftPaper(OperationUtils.getInstance().mEndDraftPage);
                    break;
                case 500:
                    //清空
                    whiteboardPresenter.orderClear();
                    break;
                case 501:
                    //撤销
                    whiteboardPresenter.undo();
                    break;
                case 502:
                    //回退
                    whiteboardPresenter.redo();
                    break;
                case 503:
                    //跳转指定页
                    whiteboardPresenter.jumpPage(this.orderBean.getCurrentPage(), this.orderBean.getCurrentAnimation());
                    break;
                case 504:
                    //上一页
                    whiteboardPresenter.lastSlideS(this.orderBean.getCurrentPage(), this.orderBean.getCurrentAnimation());
                    break;
                case 505:
                    //下一页
                    whiteboardPresenter.nextSlideS(this.orderBean.getCurrentPage(), this.orderBean.getCurrentAnimation());
                    break;
            }
        }
    }


    public void setListorderBean(ArrayList<OrderBean> listorderBean) {
        this.listorderBean = listorderBean;
    }
}
