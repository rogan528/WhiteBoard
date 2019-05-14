package com.zhangbin.paint.whiteboard.shape;

import android.graphics.Paint;

import com.zhangbin.paint.beans.OrderBean;

public abstract class BaseShape
        extends BaseDraw {


    public BaseShape() {
    }



    //移动
    public void moveOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public BaseShape(Paint paint) {
        super(paint);
    }


    /**
     * 解释指令
     *
     * @param orderBean
     */
    public void explainOrder(OrderBean orderBean) {
        if (orderBean == null) {
            throw new IllegalArgumentException();
        }
        //没有id 给个时间戳
        if (orderBean.getUuid() == null || orderBean.getUuid().equals("")) {
            orderBean.setUuid(java.util.UUID.randomUUID().toString());
        }


        this.setId(orderBean.getUuid());
        this.dataList = orderBean.getData();

    }
}

