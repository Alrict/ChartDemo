package com.ihypnus.ihypnuscare.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ViewGroup;


public class ClipZoomImageView extends android.support.v7.widget.AppCompatImageView {

    public static float SCALE_MAX = 4.0f;
    private static float SCALE_MID = 2.0f;

    private float initScale = 1.0f;

    private final float[] matrixValues = new float[9];

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private boolean isAutoScale;

    private float mLastX;
    private float mLastY;

    private boolean mIsCanDrag;
    /**
     * 水平方向中心圆环(剪切区域)距离屏幕的距离
     */
    private int mHorizontalPadding;
    /**
     * 竖直方向中心圆环(剪切区域)距离屏幕的距离
     */
    private int mVerticalPadding;

    //  是否有图片
    private boolean hasDrawable;
    //    是否布局完成
    private boolean isLayout;
    //    中心点的位置
    private PointF mScreenCenter = new PointF();

    /**
     * 基础矩阵
     */
    private Matrix mBaseMatrix = new Matrix();
    /**
     * 缩放矩阵
     */
    private Matrix mScaleMatrix = new Matrix();

    /**
     * 综合矩阵
     */
    private Matrix mSynthesisMatrix = new Matrix();

    /**
     * 基础的矩形
     */
    private RectF mBaseRect = new RectF();
    /**
     * 图片的矩形
     */
    private RectF mImageRect = new RectF();
    /**
     * 触碰点的数量
     */
    private int mLastPointerCount;


    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (isAutoScale == true)
                return true;

            float x = e.getX();
            float y = e.getY();
            if (getScale() < SCALE_MID) {
                ClipZoomImageView.this.postDelayed(
                        new AutoScaleRunnable(SCALE_MID, x, y), 16);
                isAutoScale = true;
            } else {
                ClipZoomImageView.this.postDelayed(
                        new AutoScaleRunnable(initScale, x, y), 16);
                isAutoScale = true;
            }

            return true;
        }
    };

    /**
     * 缩放手势监听
     */
    private OnScaleGestureListener mOnScaleGestureListener = new OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = getScale();
            float scaleFactor = detector.getScaleFactor();

            if (getDrawable() == null)
                return true;

            if ((scale < SCALE_MAX && scaleFactor > 1.0f)
                    || (scale > initScale && scaleFactor < 1.0f)) {

                if (scaleFactor * scale < initScale) {
                    scaleFactor = initScale / scale;
                }
                if (scaleFactor * scale > SCALE_MAX) {
                    scaleFactor = SCALE_MAX / scale;
                }

                mScaleMatrix.postScale(scaleFactor, scaleFactor,
                        detector.getFocusX(), detector.getFocusY());
                checkBorder();
                exculteTranslate();
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    };

    public ClipZoomImageView(Context context) {
        this(context, null);
    }


    public ClipZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
//        手势
        mGestureDetector = new GestureDetector(getContext(), mOnGestureListener);
//        缩放手势
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), mOnScaleGestureListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!hasDrawable) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        Drawable drawable = getDrawable();
        int drawableW = getDrawableWidth(drawable);
        int drawableH = getDrawableHeight(drawable);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int pWidth = MeasureSpec.getSize(widthMeasureSpec);
        int pHeight = MeasureSpec.getSize(heightMeasureSpec);

        ViewGroup.LayoutParams p = getLayoutParams();

        int width = 0;
        int height = 0;
//        父类布局对宽的约束为MATCH_PARENT
        if (p.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                width = drawableW;
            } else {
                width = pWidth;
            }
        } else {
            if (widthMode == MeasureSpec.EXACTLY) {
                width = pWidth;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                width = drawableW < pWidth ? drawableW : pWidth;
            } else {
                width = drawableW;
            }
        }

//        父类布局对高的约束为MATCH_PARENT
        if (p.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            if (heightMode == MeasureSpec.UNSPECIFIED) {
                height = drawableH;
            } else {
                height = pHeight;
            }
        } else {
            if (heightMode == MeasureSpec.EXACTLY) {
                height = pHeight;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                height = drawableH < pHeight ? drawableH : pHeight;
            } else {
                height = drawableH;
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mScreenCenter.set((right - left) / 2, (bottom - top) / 2);
        if (hasDrawable && !isLayout) {
            isLayout = true;
            initBase();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 图片适应控件,以宽度优先
     */
    private void initBase() {
//        没有图片
        if (!hasDrawable)
            return;
        if (!isLayout)
            return;
        mBaseMatrix.reset();
        mScaleMatrix.reset();
        Drawable drawable = getDrawable();
        int dH = getDrawableHeight(drawable);
        int dW = getDrawableWidth(drawable);
        mBaseRect.set(0, 0, dW, dH);

        int pW = getWidth();
        int pH = getHeight();
//      获取圆环(剪切区域)的直径
        int circleValue = pW - 2 * mHorizontalPadding;
//        获得竖直方向剪切区域到到屏幕边缘的距离
        mVerticalPadding = (pH - circleValue) / 2;
//        图片中心居中,需要平移的距离
        int tx = (pW - dW) / 2;
        int ty = (pH - dH) / 2;
//        View已经布局完成,长,宽不为0
        float scale = 1.0f;
        if (circleValue >= 0) {
//            图片的宽小于高
            if (dW < dH) {
                scale = (float) circleValue / dW;
            } else {
                scale = (float) circleValue / dH;
            }
        }
        mBaseMatrix.reset();
        mBaseMatrix.postTranslate(tx, ty);
        mBaseMatrix.postScale(scale, scale, mScreenCenter.x, mScreenCenter.y);
        mBaseMatrix.mapRect(mBaseRect);
        exculteTranslate();
    }

    private void exculteTranslate() {
//        将基础矩阵x缩放矩阵并赋值给综合矩阵
        setSynthesisMatrix();
        setImageMatrix(mSynthesisMatrix);

//        将基础矩形映射一份给图片矩形
        mBaseMatrix.mapRect(mImageRect, mBaseRect);

    }

    private void setSynthesisMatrix() {
        mSynthesisMatrix.set(mBaseMatrix);
        mSynthesisMatrix.postConcat(mScaleMatrix);
    }

    @Override
    public void setImageResource(int resId) {
        Drawable drawable = null;
        try {
            drawable = getResources().getDrawable(resId);
        } catch (Exception e) {
        }

        setImageDrawable(drawable);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);

        if (drawable == null) {
            hasDrawable = false;
            return;
        }

        if (!hasSize(drawable))
            return;

        if (!hasDrawable) {
            hasDrawable = true;
        }

        initBase();

    }

    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;//模式,BIGGER放大;SMALLER;缩小


        private float x;
        private float y;


        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }

        }

        @Override
        public void run() {
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorder();
            exculteTranslate();
            final float currentScale = getScale();

            if (((tmpScale > 1f) && (currentScale < mTargetScale))
                    || ((tmpScale < 1f) && (mTargetScale < currentScale))) {
                ClipZoomImageView.this.postDelayed(this, 16);
            } else {
                final float deltaScale = mTargetScale / currentScale;
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                checkBorder();
                exculteTranslate();
                isAutoScale = false;
            }

        }
    }

    /**
     * 获得图片的矩形
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mSynthesisMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, getDrawableWidth(d), getDrawableHeight(d));
            matrix.mapRect(rect);
        }
        return rect;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);
//        控件本身的触碰事件
        return doTouchEvent(event);
    }

    private boolean doTouchEvent(MotionEvent event) {
        final int pointerCount = event.getPointerCount();
        float currentX = 0;
        float currentY = 0;
        for (int index = 0; index < pointerCount; index++) {
            currentX += event.getX();
            currentY += event.getY();
        }
        currentX = currentX / pointerCount;
        currentY = currentY / pointerCount;

        if (pointerCount != mLastPointerCount) {
            mIsCanDrag=false;
            mLastX = currentX;
            mLastY = currentY;
        }
        mLastPointerCount = pointerCount;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = currentX - mLastX;
                float dy = currentY - mLastY;
                if (!mIsCanDrag){
                    mIsCanDrag = isCanDrag(dx, dy);
                }
                if (mIsCanDrag) {
                    if (hasDrawable) {
//                      获得图片的矩行
                        RectF rectF = getMatrixRectF();
//                      图片的宽小于圆环的直径时,禁止左右滑动
                        if (rectF.width() <= getWidth() - mHorizontalPadding * 2) {
                            dx = 0;
                        }
//                      图片的高小于圆环的直径,禁止上下滑动
                        if (rectF.height() <= getHeight() - mVerticalPadding * 2) {
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorder();
                        exculteTranslate();
                    }
                    mLastX = currentX;
                    mLastY = currentY;
                    break;
                }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }
        return true;
    }


    public final float getScale() {
        mScaleMatrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }


    public Bitmap clip() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
//        裁剪一个矩形
        return Bitmap.createBitmap(bitmap, mHorizontalPadding,
                mVerticalPadding, getWidth() - 2 * mHorizontalPadding,
                getWidth() - 2 * mHorizontalPadding);
//        裁剪一个圆
//        return getCircleBitmap(Bitmap.createBitmap(bitmap, mHorizontalPadding,
//                mVerticalPadding, getWidth() - 2 * mHorizontalPadding,
//                getWidth() - 2 * mHorizontalPadding));
    }

    /**
     * 检查是否超出图片范围,若超出范围就进行修正
     */
    private void checkBorder() {
//        scaleMartix变动后,重新生成SynthesisMatrix
        setSynthesisMatrix();
//        获得图片的矩形
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
//        控件的长宽
        int width = getWidth();
        int height = getHeight();
//        当图片的宽大于圆环的直径
        if (rect.width() + 0.01 >= width - 2 * mHorizontalPadding) {
            if (rect.left > mHorizontalPadding) {
                deltaX = -rect.left + mHorizontalPadding;
            }
            if (rect.right < width - mHorizontalPadding) {
                deltaX = width - mHorizontalPadding - rect.right;
            }
        }
        if (rect.height() + 0.01 >= height - 2 * mVerticalPadding) {
            if (rect.top > mVerticalPadding) {
                deltaY = -rect.top + mVerticalPadding;
            }
            if (rect.bottom < height - mVerticalPadding) {
                deltaY = height - mVerticalPadding - rect.bottom;
            }
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 图片是否可以拖动
     */
    private boolean isCanDrag(float dx, float dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) >= 0;
    }

    /**
     * 水平方向中心圆环(剪切区域)距离屏幕的距离
     * @param horizontalPadding
     */
    public void setHorizontalPadding(int horizontalPadding) {
        this.mHorizontalPadding = horizontalPadding;
    }


    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 图片是否符合规格
     *
     * @param d
     * @return
     */
    private boolean hasSize(Drawable d) {
        if ((d.getIntrinsicHeight() <= 0 || d.getIntrinsicWidth() <= 0)
                && (d.getMinimumWidth() <= 0 || d.getMinimumHeight() <= 0)
                && (d.getBounds().width() <= 0 || d.getBounds().height() <= 0)) {
            return false;
        }
        return true;
    }

    /**
     * 获取图片宽度
     */
    private static int getDrawableWidth(Drawable d) {
        int width = d.getIntrinsicWidth();
        if (width <= 0) width = d.getMinimumWidth();
        if (width <= 0) width = d.getBounds().width();
        return width;
    }

    /**
     * 获取图片的高度
     * @param d
     * @return
     */
    private static int getDrawableHeight(Drawable d) {
        int height = d.getIntrinsicHeight();
        if (height <= 0) height = d.getMinimumHeight();
        if (height <= 0) height = d.getBounds().height();
        return height;
    }
}
