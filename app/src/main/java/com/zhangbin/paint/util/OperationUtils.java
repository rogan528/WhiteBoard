package com.zhangbin.paint.util;
import android.graphics.Color;

/**
 * 白板操作公共类
 *
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
     * 单例
     */
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
