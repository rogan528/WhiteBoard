package com.zhangbin.paint.whiteboard;

import com.zhangbin.paint.beans.OrderBean;
import com.zhangbin.paint.whiteboard.shape.BaseDraw;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * 白板历史管理类
 * 保存两层的map
 */
public class OrderHistoryStack {
    private Map<Integer, CopyOnWriteArrayList<BaseDraw>> mOrderHistorydrawImageFabricViewMap = new HashMap();
    private Map<Integer, CopyOnWriteArrayList<BaseDraw>> mOrderHistorydrawFabricViewMap = new HashMap();


    public final boolean clear(int pageIndex, BaseDraw baseDraw) {
        CopyOnWriteArrayList copyOnWriteArrayList = this.getDrawB(pageIndex);
        clear(baseDraw, copyOnWriteArrayList);
        return true;
    }

    public final boolean getDrawA(int pageIndex, BaseDraw baseDraw) {
        CopyOnWriteArrayList copyOnWriteArrayList = this.getDrawA(pageIndex);
        clear(baseDraw, copyOnWriteArrayList);
        return true;
    }

    public final boolean clear(int pageIndex) {

        CopyOnWriteArrayList arrayList = this.mOrderHistorydrawImageFabricViewMap.get(pageIndex);
        if (arrayList != null && arrayList.size() > 0) {
            arrayList.clear();
        }
        return true;

    }

    private static void clear(BaseDraw baseDraw, CopyOnWriteArrayList<BaseDraw> baseDraws) {
        if ((baseDraw == null) || (baseDraws == null)) {
            return;
        }
        Iterator iterator = baseDraws.iterator();
        while (iterator.hasNext()) {
            BaseDraw baseDraw1 = (BaseDraw) iterator.next();
            if (baseDraw1.getId() != null && !"".equals(baseDraw1.getId()) && baseDraw.getId() != null && !"".equals(baseDraw.getId())) {
                baseDraws.remove(baseDraw1);
                break;
            }
        }


//        if (baseDraw.getIsShow()) {
        baseDraws.add(baseDraw);
//        }
    }

    public final CopyOnWriteArrayList<BaseDraw> getDrawA(int pageIndex) {
        CopyOnWriteArrayList copyOnWriteArrayList = this.mOrderHistorydrawImageFabricViewMap.get(pageIndex);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList();
            this.mOrderHistorydrawImageFabricViewMap.put(pageIndex, copyOnWriteArrayList);
        }
        return copyOnWriteArrayList;
    }

    public final CopyOnWriteArrayList<BaseDraw> getDrawB(int pageIndex) {
        CopyOnWriteArrayList copyOnWriteArrayList = this.mOrderHistorydrawFabricViewMap.get(pageIndex);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList();
            this.mOrderHistorydrawFabricViewMap.put(pageIndex, copyOnWriteArrayList);
        }
        return copyOnWriteArrayList;
    }

    public final boolean exist(int pageIndex) {
        CopyOnWriteArrayList list = this.mOrderHistorydrawImageFabricViewMap.get(pageIndex);
        return (list != null) && (list.size() > 0);
    }

    public final void clear() {
        this.mOrderHistorydrawImageFabricViewMap.clear();
        this.mOrderHistorydrawFabricViewMap.clear();
    }

    public final void getDrawA() {
        clear();
    }
    /**
     * 保存记录
     * @param pageSaveIndex
     * @param copyOnWriteArrayList
     */
    public void putOrderHistorySave(int pageSaveIndex ,CopyOnWriteArrayList<BaseDraw> copyOnWriteArrayList){
        mOrderHistorydrawImageFabricViewMap.put(pageSaveIndex,copyOnWriteArrayList);
    }
    /**
     * 删除保存命令指令
     * @param orderBean
     */
    public void removeOrderHistorySave(OrderBean orderBean){
        mOrderHistorydrawImageFabricViewMap.remove(orderBean);
    }

    /**
     * @return  返回保存map
     */
    public Map<Integer, CopyOnWriteArrayList<BaseDraw>> getOrderHistorySave(){
        return mOrderHistorydrawImageFabricViewMap;
    }

    /**
     * 删除命令指令
     * @param copyOnWriteArrayList
     */
    public void putOrderHistoryDelete(int pageDeletIndex,CopyOnWriteArrayList<BaseDraw> copyOnWriteArrayList){
        mOrderHistorydrawFabricViewMap.put(pageDeletIndex,copyOnWriteArrayList);
    }
    /**
     * 删除保存命令指令
     * @param copyOnWriteArrayList
     */
    public void removeOrderHistoryDelete(CopyOnWriteArrayList<BaseDraw> copyOnWriteArrayList){
        mOrderHistorydrawFabricViewMap.remove(copyOnWriteArrayList);
    }
    /**
     * @return  返回删除map
     */
    public Map<Integer, CopyOnWriteArrayList<BaseDraw>> getOrderHistoryDelete(){
        return mOrderHistorydrawFabricViewMap;
    }
}
