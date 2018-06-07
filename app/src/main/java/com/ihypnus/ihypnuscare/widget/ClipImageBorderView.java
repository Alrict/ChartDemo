package com.ihypnus.ihypnuscare.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ihypnus.ihypnuscare.utils.LogOut;


public class ClipImageBorderView extends View {
    private static final String TAG = ClipImageBorderView.class.getSimpleName();
    private int mHorizontalPadding;

    private int mBorderWidth = 2;

    private Paint mPaintRing;
    private Bitmap mBgBitmap;
    private Canvas mCanvas;
    private RectF mRect;
    private Paint mPaintCirle;
    private Paint mPaintRect;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


//        圆环的画笔
        mPaintRing = new Paint();
        mPaintRing.setAntiAlias(true);
        mPaintRing.setColor(Color.WHITE);
        mPaintRing.setStyle(Paint.Style.STROKE);
        mPaintRing.setStrokeWidth(2);

//        阴影画笔
        mPaintRect = new Paint();
        mPaintRect.setAntiAlias(true);
        mPaintRect.setARGB(180, 0, 0, 0);

//        实心圆画笔
        mPaintCirle = new Paint();
        mPaintCirle.setStyle(Paint.Style.FILL);
        mPaintCirle.setAntiAlias(true);
        mPaintCirle.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBgBitmap == null) {
            mBgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBgBitmap);
            mRect = new RectF(0, 0, getWidth(), getHeight());

            //      绘制阴影层
            mCanvas.drawRect(mRect, mPaintRect);
//          绘制实心圆 ，绘制完后，在mCanvas画布中，mPaintRect和mPaintCirle相交部分即被掏空
            mCanvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mPaintCirle);
        }
//      将阴影层画进本View的画布中
        LogOut.d(TAG, "绘制阴影");
        canvas.drawBitmap(mBgBitmap, null, mRect, new Paint());
//      绘制圆环
        LogOut.d(TAG, "绘制圆环");
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mPaintRing);
    }

    /**
     * 圆环到屏幕左右的距离
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;

    }

}
