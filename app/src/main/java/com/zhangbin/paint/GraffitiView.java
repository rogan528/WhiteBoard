package com.zhangbin.paint;

/**
 * Created by zpxiang on 2016/4/6.
 * 类描述：View实现涂鸦、橡皮擦、撤销以及重做功能
 * 进行保存以及图片选择
 * 小功能包括画笔颜色、尺寸调整
 * 之后添加了画椭圆，画矩形以及画箭头的功能
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhangbin.paint.beans.OrderBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GraffitiView extends View {


    private Context context;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;// 画布的画笔
    private Paint mPaint;// 真实的画笔
    private float mX, mY;// 临时点坐标
    private static final float TOUCH_TOLERANCE = 4;

    // 保存Path路径的集合
    private static List<DrawPath> savePath;
    // 保存已删除Path路径的集合
    private static List<DrawPath> deletePath;
    // 记录Path,paint路径的对象
    private DrawPath dp;

    private int screenWidth, screenHeight;//控件传进来的宽高，用来表示该tuyaView的宽高

    private int currentPanitColor = Color.RED;
    private float currentPanitSize = 5;//默认画笔大小
    private float currentEraserSize = 25;
    private int currentStyle = 1;

    private int[] paintColor;//颜色集合
    private Bitmap srcBitmap;//传递过来的背景图转换成的bitmap

    //设置画图样式
    private static final int DRAW_PATH = 01;
    private static final int DRAW_CIRCLE = 02;
    private static final int DRAW_RECTANGLE = 03;
    private static final int DRAW_ARROW = 04;
    private int[] graphics = new int[]{DRAW_PATH,DRAW_CIRCLE,DRAW_RECTANGLE,DRAW_ARROW};
    private  int currentDrawGraphics = graphics[0];//默认画线

    private class DrawPath {
        public Path path;// 路径
        public Paint paint;// 画笔
    }


    public GraffitiView(Context context, int w, int h) {
        super(context);
        this.context = context;
        screenWidth = w;
        screenHeight = h;
        paintColor = new int[]{
                Color.parseColor("#E372FF"), Color.parseColor("#FE7C2E"), Color.parseColor("#6CD685"), Color.parseColor("#FFB42B"), Color.parseColor("#000000")
        };//画笔颜色的数组

        setLayerType(LAYER_TYPE_SOFTWARE, null);//设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
        initCanvas();

        srcBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());

        savePath = new ArrayList<DrawPath>();//存储画下的路径
        deletePath = new ArrayList<DrawPath>();//存储删除的路径，用来恢复，此方法已弃
    }

    //初始化画笔画板
    public void initCanvas() {

        setPaintStyle();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        //不能在ondraw()上的canvas直接作画，需要单独一套的bitmap以及canvas记录路径
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0, 0, 0, 0));//橡皮擦的设置
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas.drawColor(Color.TRANSPARENT);//设置透明是为了之后的添加背景，防止盖住背景
    }

    //初始化画笔样式
    private void setPaintStyle() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        if (currentStyle == 1) {//普通画笔功能
            mPaint.setStrokeWidth(currentPanitSize);
            mPaint.setColor(currentPanitColor);
        } else {//橡皮擦
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//这两个方法一起使用才能出现橡皮擦效果
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setStrokeWidth(currentEraserSize);
            currentDrawGraphics = DRAW_PATH;//使用橡皮擦时默认用线的方式擦除
        }
    }


    @Override
    public void onDraw(Canvas canvas) {

        // 在之前画板上画过得显示出来
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);// 实时的显示
        }
    }

    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }


    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(mY - y);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也可以)
            if(currentDrawGraphics == DRAW_PATH){//画线
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            }else if(currentDrawGraphics == DRAW_CIRCLE){
                mPath.reset();//清空以前的路径，否则会出现无数条从起点到末位置的线
                RectF rectF = new RectF(startX,startY,x,y);
                mPath.addOval(rectF, Path.Direction.CCW);//画椭圆
               // mPath.addCircle((startX + x) / 2, (startY + y) / 2, (float) Math.sqrt(Math.pow(newx, 2) + Math.pow(newy, 2)) / 2, Path.Direction.CCW);
            }else if(currentDrawGraphics == DRAW_RECTANGLE){
                mPath.reset();
                RectF rectF = new RectF(startX,startY,x,y);
                mPath.addRect(rectF, Path.Direction.CCW);
            }else if (currentDrawGraphics == DRAW_ARROW){
                mPath.reset();
                drawAL((int) startX, (int) startY, (int) x, (int) y);

            }
            isEmpty = false;//此时证明有path存在，即涂鸦板不会为空
            //能连起来
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        if(currentDrawGraphics == DRAW_PATH){
            mPath.lineTo(mX, mY);
        }

        mCanvas.drawPath(mPath, mPaint);
        //将一条完整的路径保存下来(相当于入栈操作)
        savePath.add(dp);
        mPath = null;// 重新置空
    }

    private float startX;
    private float startY;

    private boolean isEmpty = true;//用来处理点击了涂鸦板未生成路径但保存时已不再报空的情况
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //记录最初始的点，画圆矩形箭头会用到
                startX = event.getX();
                startY = event.getY();
                Log.e("zhangbin----","startx:"+startX+"y:"+startY);
                // 每次down下去重新new一个Path
                mPath = new Path();
                //每一次记录的路径对象是不一样的
                dp = new DrawPath();
                dp.path = mPath;
                dp.paint = mPaint;
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                Log.e("zhangbin----","x:"+x+"y:"+y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                if(isEmpty){
                    savePath.clear();
                }
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 撤销
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo() {
        if (savePath != null && savePath.size() > 0) {
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);
            redrawOnBitmap();
        }
    }

    /**
     * 重做
     */
    public void redo() {
        if (savePath != null && savePath.size() > 0) {
            savePath.clear();
            redrawOnBitmap();
        }
    }

    //将剩下的path重绘
    private void redrawOnBitmap() {
        /*mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
                Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);// 重新设置画布，相当于清空画布*/
        initCanvas();
        Iterator<DrawPath> iter = savePath.iterator();
        while (iter.hasNext()) {
            DrawPath drawPath = iter.next();

            mCanvas.drawPath(drawPath.path, drawPath.paint);
        }
        invalidate();// 刷新
    }


    /**
     * 恢复，恢复的核心就是将删除的那条路径重新添加到savapath中重新绘画即可
     */
    public void recover() {

        if (deletePath.size() > 0) {

            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            //将取出的路径重绘在画布上
            mCanvas.drawPath(dp.path, dp.paint);

            //将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
    }



    /**
     * 以下为样式修改内容
     * */

    //设置画笔样式
    public void selectPaintStyle(int which) {
        if (which == 0) {
            currentStyle = 1;
            setPaintStyle();
        }else if (which == 1) { //which为1时，选择的是橡皮擦
            currentStyle = 2;
            setPaintStyle();
        }
    }

//----------------------------------------------------------------------------------

    /**
     * 设置画笔大小
     * @param paintSize 大小
     */
    public void setPaintSize(float paintSize) {
        currentPanitSize = paintSize;
        setPaintStyle();
    }
    /**
     * 设置橡皮大小
     * @param eraserSize 大小
     */
    public void setEraserSize(float eraserSize) {
        currentEraserSize = eraserSize;
        setPaintStyle();
    }

    /**
     * 设置画笔的颜色
     * @param paintColor
     */
    public void setPaintColor(int paintColor) {
        currentPanitColor = paintColor;
        setPaintStyle();
    }
    //设置背景图
    public void setSrcBitmap(Bitmap bitmap){
        this.srcBitmap = bitmap;
    }
    public void setReaserPath(String uuid,List<OrderBean.DataBean> lst) {
        if (mPath == null) {
            mPath = new Path();
        }
        dp = new DrawPath();
        dp.path = mPath;
        dp.paint = mPaint;
        mPaint.setAlpha(0);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//这两个方法一起使用才能出现橡皮擦效果
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(currentEraserSize);
        currentDrawGraphics = DRAW_PATH;//使用橡皮擦时默认用线的方式擦除
        OrderBean.DataBean start = lst.get(0);

        mPath.moveTo(start.x, start.y);
        for (int i = 1; i < lst.size() - 1; i++) {
            OrderBean.DataBean end = lst.get(i);
            mPath.lineTo(end.x, end.y);
        }
        mCanvas.drawPath(mPath, mPaint);
        //将一条完整的路径保存下来(相当于入栈操作)
        savePath.add(dp);
        invalidate();
        mPath = null;// 重新置空
    }
    public void orderDraw(List<OrderBean.DataBean> lst) {
        if (mPath == null) {
            mPath = new Path();
        }
        dp = new DrawPath();
        dp.path = mPath;
        dp.paint = mPaint;

        OrderBean.DataBean start = lst.get(0);

        mPath.moveTo(start.x, start.y);
        for (int i = 1; i < lst.size() - 1; i++) {
            OrderBean.DataBean end = lst.get(i);
            mPath.lineTo(end.x, end.y);
        }

        mCanvas.drawPath(mPath, mPaint);
        //将一条完整的路径保存下来(相当于入栈操作)
        savePath.add(dp);
        invalidate();
        mPath = null;// 重新置空
    }
    public void orderDrawLIne(String uuid,boolean isDrag,float x1, float y1, float x2, float y2) {
        if (mPath == null) {
            mPath = new Path();
        }

        dp = new DrawPath();
        dp.path = mPath;
        dp.paint = mPaint;
        mPath.moveTo(x1, y1);
        mPath.quadTo(x1, y1, x2, y2);
        mCanvas.drawPath(mPath, mPaint);
        savePath.add(dp);
        invalidate();
        if (!isDrag) {
            savePath.add(dp);
            mPath = null;// 重新置空
        }else{
            Log.e("itemorderorder","---"+savePath.size()+"--"+deletePath.size());
            mPath = null;// 重新置空
        }
    }
    public void orderDrawDashLine(String uuid,boolean isDrag,float x1, float y1, float x2, float y2) {
        if (mPath == null) {
            mPath = new Path();
        }
        dp = new DrawPath();
        dp.path = mPath;
        dp.paint = mPaint;

        mPaint.setARGB(255, 0, 0, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));

        mPath.quadTo(x1, y1, x2, y2);
        mCanvas.drawPath(mPath, mPaint);

        invalidate();
        if (!isDrag) {
            savePath.add(dp);
            mPath = null;// 重新置空
        }else{
            Log.e("itemorderorder","---"+savePath.size()+"--"+deletePath.size());
            mPath = null;// 重新置空
        }

    }
    public void orderDrawCircle(String uuid,boolean isDrag,float x1, float y1, float x2, float y2) {

        if (mPath == null) {
            mPath = new Path();
        }
        dp = new DrawPath();
        dp.path = mPath;
        dp.paint = mPaint;
        mPath.moveTo(x1, y1);
        mPath.reset();//清空以前的路径，否则会出现无数条从起点到末位置的线
        RectF rectF = new RectF(x1, y1, x2, y2);
        mPath.addOval(rectF, Path.Direction.CCW);//画椭圆
        mCanvas.drawPath(mPath, mPaint);
        invalidate();
        if (!isDrag) {
            savePath.add(dp);
            mPath = null;// 重新置空
        }else{
            Log.e("itemorderorder","---"+savePath.size()+"--"+deletePath.size());
            mPath = null;// 重新置空
        }
    }



    public void orderDrawLRectangle(String uuid,boolean isDrag,float x1, float y1, float x2, float y2) {
        if (mPath == null) {
            mPath = new Path();
        }
        dp = new DrawPath();
        dp.path = mPath;
        dp.paint = mPaint;
        mPath.moveTo(x1, y1);
        mPath.reset();
        RectF rectF = new RectF(x1, y1, x2, y2);
        mPath.addRect(rectF, Path.Direction.CCW);
        mCanvas.drawPath(mPath, mPaint);
        invalidate();
        if (!isDrag) {
            savePath.add(dp);
            mPath = null;// 重新置空
        }else{
            Log.e("itemorderorder","---"+savePath.size()+"--"+deletePath.size());
            mPath = null;// 重新置空
        }
    }
    /**
     * 以下为画图方式
     * @param which 通过传过来的int类型来修改画图样式
     */
    //画线，圆，矩形，以及箭头
    public void drawGraphics(int which){
        currentDrawGraphics = graphics[which];
    }


    /**
     * 画箭头
     * @param startX 开始位置x坐标
     * @param startY 开始位置y坐标
     * @param endX 结束位置x坐标
     * @param endY 结束位置y坐标
     */
    public void drawAL(int startX, int startY, int endX, int endY)
    {
        double lineLength = Math.sqrt(Math.pow(Math.abs(endX-startX),2) + Math.pow(Math.abs(endY-startY),2));//线当前长度
        double H = 0;// 箭头高度
        double L = 0;// 箭头长度
        if(lineLength < 320){//防止箭头开始时过大
            H = lineLength/4 ;
            L = lineLength/6;
        }else { //超过一定线长箭头大小固定
            H = 80;
            L = 50;
        }

        double arrawAngle = Math.atan(L / H); // 箭头角度
        double arraowLen = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] pointXY1 = rotateAndGetPoint(endX - startX, endY - startY, arrawAngle, true, arraowLen);
        double[] pointXY2 = rotateAndGetPoint(endX - startX, endY - startY, -arrawAngle, true, arraowLen);
        int x3 = (int) (endX - pointXY1[0]);//(x3,y3)为箭头一端的坐标
        int y3 = (int) (endY - pointXY1[1]);
        int x4 = (int) (endX - pointXY2[0]);//(x4,y4)为箭头另一端的坐标
        int y4 = (int) (endY - pointXY2[1]);
        // 画线
        mPath.moveTo(startX,startY);
        mPath.lineTo(endX,endY);
        mPath.moveTo(x3, y3);
        mPath.lineTo(endX, endY);
        mPath.lineTo(x4, y4);
        // mPath.close();闭合线条
    }

    /**
     * 矢量旋转函数，计算末点的位置
     * @param x  x分量
     * @param y  y分量
     * @param ang  旋转角度
     * @param isChLen  是否改变长度
     * @param newLen   箭头长度长度
     * @return    返回末点坐标
     */
    public double[] rotateAndGetPoint(int x, int y, double ang, boolean isChLen, double newLen)
    {
        double pointXY[] = new double[2];
        double vx = x * Math.cos(ang) - y * Math.sin(ang);
        double vy = x * Math.sin(ang) + y * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            pointXY[0] = vx / d * newLen;
            pointXY[1] = vy / d * newLen;
        }
        return pointXY;
    }


    public List getSavePath(){
        return savePath;
    }
}


