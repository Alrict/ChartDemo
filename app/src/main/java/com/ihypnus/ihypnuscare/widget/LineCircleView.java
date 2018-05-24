package com.ihypnus.ihypnuscare.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Package com.ihypnus.ihypnuscare.widget
 * @author: llw
 * @Description:
 * @date: 2018/5/24 10:42
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class LineCircleView extends ViewGroup {
    /**
     * 绘制弧线的画笔
     */
    private Paint mPaint;
    /**
     * 进度条所占用的角度
     */
    private static final int ARC_FULL_DEGREE = 360;
    //进度条个数
    private static final int COUNT = 100;
    //每个进度条所占用角度
    private static final float ARC_EACH_PROGRESS = ARC_FULL_DEGREE * 1.0f / (COUNT - 1);
    /**
     * 弧线细线条的长度
     */
    private int ARC_LINE_LENGTH;
    /**
     * 弧线细线条的宽度
     */
    private int ARC_LINE_WIDTH;


    /**
     * 组件的宽，高
     */
    private int width, height;
    /**
     * 进度条最大值和当前进度值
     */
    private float max, progress;


    private int mLinerCircleRadiu;
    private int centerX;
    private int centerY;

    public LineCircleView(Context context) {
        super(context);
        init();
    }

    public LineCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (width == 0 || height == 0) {
            width = getWidth();
            height = getHeight();


            //计算圆弧半径和圆心点
            mLinerCircleRadiu = Math.min(width, height) / 2;
            ARC_LINE_LENGTH = mLinerCircleRadiu / 8;
            ARC_LINE_WIDTH = ARC_LINE_LENGTH / 8;

            centerX = width / 2;
            centerY = height / 2;

            // 绘制进度颜色显示
            mPaint.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float start = (360 - ARC_FULL_DEGREE) >> 1; //进度条起始角度   >> 1即除以2
        float sweep1 = ARC_FULL_DEGREE * (progress / max); //进度划过的角度


        //绘制进度条
        mPaint.setColor(Color.parseColor(calColor(progress / max, "#ffff0000", "#ff00ff00")));
        mPaint.setStrokeWidth(ARC_LINE_WIDTH);

        float drawDegree = 1.6f;
        while (drawDegree <= ARC_FULL_DEGREE) {
            double a = (start + drawDegree) / 180 * Math.PI;
            float lineStartX = centerX - mLinerCircleRadiu * (float) Math.sin(a);
            float lineStartY = centerY + mLinerCircleRadiu * (float) Math.cos(a);
            float lineStopX = lineStartX + ARC_LINE_LENGTH * (float) Math.sin(a);
            float lineStopY = lineStartY - ARC_LINE_LENGTH * (float) Math.cos(a);


            if (drawDegree > sweep1) {
                //绘制进度条背景
                mPaint.setColor(Color.parseColor("#88aaaaaa"));
                mPaint.setStrokeWidth(ARC_LINE_WIDTH >> 1);
            }
            canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, mPaint);
            // 绘制圆圈，进度条背景
            drawDegree += ARC_EACH_PROGRESS;
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        int childMeasureWidth = 0;
        int childMeasureHeight = 0;
        int layoutWidth = ARC_LINE_LENGTH;    // 容器已经占据的宽度
        int layoutHeight = ARC_LINE_LENGTH;   // 容器已经占据的宽度

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                //注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
                childMeasureWidth = child.getMeasuredWidth();
                childMeasureHeight = child.getMeasuredHeight();
                left = layoutWidth;
                right = left + childMeasureWidth;
                top = layoutHeight;
                bottom = top + childMeasureHeight;
                child.layout(left, top, right, bottom);
            }
        }
    }


    /**
     * 计算渐变效果中间的某个颜色值。
     * 仅支持 #aarrggbb 模式,例如 #ccc9c9b2
     */
    public String calColor(float fraction, String startValue, String endValue) {
        int start_a, start_r, start_g, start_b;
        int end_a, end_r, end_g, end_b;


        //start
        start_a = getIntValue(startValue, 1, 3);
        start_r = getIntValue(startValue, 3, 5);
        start_g = getIntValue(startValue, 5, 7);
        start_b = getIntValue(startValue, 7, 9);


        //end
        end_a = getIntValue(endValue, 1, 3);
        end_r = getIntValue(endValue, 3, 5);
        end_g = getIntValue(endValue, 5, 7);
        end_b = getIntValue(endValue, 7, 9);


        return "#" + getHexString((int) (start_a + fraction * (end_a - start_a)))
                + getHexString((int) (start_r + fraction * (end_r - start_r)))
                + getHexString((int) (start_g + fraction * (end_g - start_g)))
                + getHexString((int) (start_b + fraction * (end_b - start_b)));
    }


    //从原始#AARRGGBB颜色值中指定位置截取，并转为int.
    private int getIntValue(String hexValue, int start, int end) {
        return Integer.parseInt(hexValue.substring(start, end), 16);
    }


    private String getHexString(int value) {
        String a = Integer.toHexString(value);
        if (a.length() == 1) {
            a = "0" + a;
        }


        return a;
    }
}
