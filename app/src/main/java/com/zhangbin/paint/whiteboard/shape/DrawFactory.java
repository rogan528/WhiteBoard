package com.zhangbin.paint.whiteboard.shape;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.util.Util;

/**
 * @ClassName DrawFactory
 * @Description TODO
 * @Author yangjie
 * @Date 2019/4/2 上午9:39
 */
public class DrawFactory {
    public static BaseDraw createPageDrawable(OrderBean order) {
        if (order == null) {
            return null;
        }

        int type = Util.toInteger(order.type);
        Object var3;
        switch (type) {
            case 400:
                var3 = new DrawBrush();
                break;
            case 401:
                var3 = new DrawEraser();
                break;
            case 405:
                var3 = new DrawLine();
                break;
            case 406:
                var3 = new DrawDotLine();
                break;
            case 407:
                var3 = new DrawRectangle();
                break;
            case 408:
                var3 = new DrawCircle();
                break;
//            case 19:
//                var3 = new DrawArrow();
//                break;
            case 402:
                var3 = new DrawText();
                break;
            case 403:
                var3 = new DrawText();
                break;
            //移动
            case 404:
                var3 = new DrawMove();
                break;
            case 409:
                var3 = new DrawMove();
                break;

            default:
                var3 = null;
        }
        if (var3 != null) {
            try {
                ((BaseDraw) var3).explainOrder(order);
            } catch (IllegalArgumentException localIllegalArgumentException) {
                return null;
            }
        }
        return (BaseDraw) var3;
    }
}


