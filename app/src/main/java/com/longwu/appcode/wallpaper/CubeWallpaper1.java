package com.longwu.appcode.wallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * @Title: CubeWallpaper1.java
 * @Package cube1
 * @Description: 初识Android动态壁纸引擎
 * @author XiaoMa
 */

/*
 * 因为小马没接触过引擎，也无从下手学习，今天就拿官方的例子来试试刀了，
 * 吼吼，刚开始不知道，看了下代码，里面有讲到WallPaperService
 * 查询这个服务可以查到与之相关联的类与方法，如下面官方文档中讲的，大家
 * 英语不好也可以用工具看个大概，英语不好的朋友记得多用工具学习下，小马
 * 英语也不咋的....
 * A wallpaper service is responsible for showing a live wallpaper behind
 * applications that would like to sit on top of it. This service object itself
 * does very little -- its only purpose is to generate instances of
 * WallpaperService.Engine as needed. Implementing a wallpaper thus involves
 * subclassing from this, subclassing an Engine implementation, and implementing
 * onCreateEngine() to return a new instance of your engine
 * 上面讲的大体是：一个类必须是WallpaperService引擎类的子类，而且里面必须重写
 * onCreateEngine这个方法，要仔细看的话小马也看不懂英文，照着例子，看官方文档，
 * 多猜多用工具查就行了，小马英语烂的可以让你吐血...我能看懂，你肯定能看懂
 */
public class CubeWallpaper1 extends WallpaperService {

    /*常用的都是这样的，用一个handler来动态的去刷新UI，对吧？猜的，看下面代码到底是不是*/
    private final Handler mHandler = new Handler();

    /**
     * 这个方法与Activity里面的一样，当这个类的服务被第一次创建时
     * 调用，也就是说，这个方法只调用一次..
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 与上面反的，销毁时调用，这个猜下，
     * 不懂了查文档
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 这个方法在类注释中写明了
     * implementing onCreateEngine() to return a new instance of your engine
     * 必须实现这个方法来返回我们自己定义引擎的一个实例
     */
    @Override
    public Engine onCreateEngine() {
        return new CubeEngine();
    }


    /**
     *
     * @Title: CubeWallpaper1.java
     * @Package cube1
     * @Description: 自定义引擎类
     * @author XiaoMa
     */
    class CubeEngine extends Engine {

        private final Paint mPaint = new Paint();
        private float mOffset;
        /*用户触摸位置*/
        private float mTouchX = -1;
        private float mTouchY = -1;
        private long mStartTime;

        /*屏幕中心坐标，记下，是中心不是原心（0，0）*/
        private float mCenterX;
        private float mCenterY;

        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private boolean mVisible;

        CubeEngine() {

            /*下面这几行就为了在屏幕中画立方体的线条而做准备*/
            final Paint paint = mPaint;
            paint.setColor(0xffffffff);//画笔颜色
            paint.setAntiAlias(true);//抗锯齿
            paint.setStrokeWidth(2);//线条粗细，猜的，不知道对不对
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            //系统启动完之后，开始绘制壁纸的时间，这个时间里面包含有系统睡眠时间
            mStartTime = SystemClock.elapsedRealtime();
        }

        /**
         * 大家发现这个onCreate与Activity的方法有什么不同了吧？
         * 老规矩的，还是在初始化壁纸引擎的时候调用这个方法，并设置触
         * 屏事件为可用
         */
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawCube);
        }

        /**
         * 系统壁纸状态改变时会调用这个方法，如：
         * 壁纸由隐藏转换为显示状态时会调用这个方法
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            /*下面这个判断好玩，就是说，如果屏幕壁纸状态转为显式时重新绘制壁纸，否则黑屏幕，隐藏就可以*/
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawCube);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            //下面是来保存屏幕显示立方体的，也就是你能看到的正面图的中心位置
            mCenterX = width / 2.0f;
            mCenterY = height / 2.0f;
            drawFrame();
        }

        /**
         * 下面两个方法是为了方便调用SurfaceHolder交互来重写的
         */
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawCube);
        }

        /**
         * 当手动壁纸时根据偏移量重绘壁纸
         */
        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
                                     float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            drawFrame();
        }

        /*
         * 在这个地方保存触摸的位置，我们会在绘制壁纸的时候使用触摸值
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();
            } else {
                mTouchX = -1;
                mTouchY = -1;
            }
            super.onTouchEvent(event);
        }

        /*
         * 绘制立方体方法实现
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    drawCube(c);
                    drawTouchPoint(c);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            // 在指定时间里重绘制，这个地方大家可以看效果图，如果你拖动过快的话，立方体
            //每个顶点之间会有一个短暂的未连接延迟，就是在这个地方使用了延迟来绘制的
            mHandler.removeCallbacks(mDrawCube);
            if (mVisible) {
                mHandler.postDelayed(mDrawCube, 1000 / 25);
            }
        }

        /*
         *  这个地方是以立方体某个顶点为起始端，绘制三条线
         *  一堆数字，看着好晕
         *  在这小马顺便贴在这个DEMO里面用到的基本的绘制，如下：
         *  graphics.Canvas有四种画矩形的方法。
            canvas.drawRect(new RectF(10, 10, 300, 100), paint);
            canvas.drawRect(10, 150, 300, 200, paint);
            canvas.drawRect(new Rect(10, 250, 300, 300), paint);
            第四种：画圆角的矩形
            canvas.drawRoundRect(new RectF(10, 350, 300, 450), 10, 10, paint);
            第二个和第三个参数为圆角的宽高。
            有兴趣的朋友可以改下下面这些东西
         */
        void drawCube(Canvas c) {
            c.save();
            c.translate(mCenterX, mCenterY);
            c.drawColor(0xff000000);
            drawLine(c, -400, -400, -400, 400, -400, -400);
            drawLine(c, 400, -400, -400, 400, 400, -400);
            drawLine(c, 400, 400, -400, -400, 400, -400);
            drawLine(c, -400, 400, -400, -400, -400, -400);

            drawLine(c, -400, -400, 400, 400, -400, 400);
            drawLine(c, 400, -400, 400, 400, 400, 400);
            drawLine(c, 400, 400, 400, -400, 400, 400);
            drawLine(c, -400, 400, 400, -400, -400, 400);

            drawLine(c, -400, -400, 400, -400, -400, -400);
            drawLine(c, 400, -400, 400, 400, -400, -400);
            drawLine(c, 400, 400, 400, 400, 400, -400);
            drawLine(c, -400, 400, 400, -400, 400, -400);
            c.restore();
        }

        /*
         * 在屏幕中绘制三维空间的线
         */
        void drawLine(Canvas c, int x1, int y1, int z1, int x2, int y2, int z2) {
            /*
             *因为大家都知道，壁纸是手机启动完成之后就已经开始绘制的，一般取时间什么的
             *我们都用Timer System.currentTimeMillis() Calendar来取
             *这个地方取系统级启动时间等的，记住这个类，SystemClock，方法自己查
             */
            long now = SystemClock.elapsedRealtime();
            /*取得三维坐标轴的旋转值*/
            float xrot = ((float) (now - mStartTime)) / 1000;
            float yrot = (0.5f - mOffset) * 2.0f;
            float zrot = 0;


            // rotation around X-axis  ？？？
            float newy1 = (float) (Math.sin(xrot) * z1 + Math.cos(xrot) * y1);
            float newy2 = (float) (Math.sin(xrot) * z2 + Math.cos(xrot) * y2);
            float newz1 = (float) (Math.cos(xrot) * z1 - Math.sin(xrot) * y1);
            float newz2 = (float) (Math.cos(xrot) * z2 - Math.sin(xrot) * y2);

            // rotation around Y-axis  ？？？
            float newx1 = (float) (Math.sin(yrot) * newz1 + Math.cos(yrot) * x1);
            float newx2 = (float) (Math.sin(yrot) * newz2 + Math.cos(yrot) * x2);
            newz1 = (float) (Math.cos(yrot) * newz1 - Math.sin(yrot) * x1);
            newz2 = (float) (Math.cos(yrot) * newz2 - Math.sin(yrot) * x2);

            // 3D-to-2D projection  ？？？
            float startX = newx1 / (4 - newz1 / 400);
            float startY = newy1 / (4 - newz1 / 400);
            float stopX = newx2 / (4 - newz2 / 400);
            float stopY = newy2 / (4 - newz2 / 400);

            c.drawLine(startX, startY, stopX, stopY, mPaint);
        }

        /*
         * 按位屏幕手动时绘制一个白色的圈
         */
        void drawTouchPoint(Canvas c) {
            if (mTouchX >= 0 && mTouchY >= 0) {
                c.drawCircle(mTouchX, mTouchY, 80, mPaint);
            }
        }

    }
}