package com.zhangbin.paint.util;
import android.graphics.Color;

/**
 * 白板操作公共类
 *
 * @author gpy
 */

public class OperationUtils {
    /**
     * 单例
     */
    private static OperationUtils mOperationStack;


    /**
     * 当前颜色
     */
    public int mCurrentPenColor = Color.RED;
    /**
     * 当前形状颜色
     */
    public int mCurrentShapeColor = Color.RED;
    /**
     * 当前画笔大小
     */
    public float mCurrentPenSize = 2;
    /**
     * 当前橡皮擦大小
     */
    public float mCurrentEraserSize = 5;
    /**
     * 当前文字大小
     */
    public float mCurrentTextSize = 25;
    /**
     * 当前文字颜色
     */
    public int mCurrentTextColor = Color.RED;
    /**
     * 当前宽
     */
    public int mBoardWidth = 0;
    /**
     * 当前高
     */
    public int mBoardHeight = 0;
    /**
     * 草稿纸背景颜色
     */
    public int mWhiteboardBackgroundColor = Color.WHITE;
    /**
     * 草稿纸开始页码
     */
    public int mStartDraftPage =100000;
    /**
     * 草稿纸结束页码
     */
    public int mEndDraftPage =100000;
    /**
     * 记录打开草稿纸之前的页码
     */
    public int mBackPage = 1;
    /**
     * 单例
     */

    public float mCurrentTextSizeRatio = 1.33f;
    public static OperationUtils getInstance() {
        if (mOperationStack == null) {
            mOperationStack = new OperationUtils();
        }
        return mOperationStack;
    }

    /**
     * 私有实例化
     */
    private OperationUtils() {
    }

    /**
     * 初始化操作集
     */
    public void init() {
        mCurrentPenColor = Color.RED;
        mCurrentTextColor = Color.RED;
        mCurrentPenSize = 5;
        mCurrentEraserSize = 50;
        mCurrentTextSize = 8;
    }


}
