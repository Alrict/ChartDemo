package com.ihypnus.ihypnuscare.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.widget.LanguageChangableView;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 自定义环形进度条
 */
public class CircleProgressBarView extends View implements LanguageChangableView {
    /**
     * 进度条所占用的角度
     */
    private static final int ARC_FULL_DEGREE = 360;

    /**
     * 进度条最大值和当前进度值
     */
    private float max = 100, progress;


    /**
     * 绘制弧线的画笔
     */
    private Paint progressPaint;
    /**
     * 绘制顶部文字的画笔
     */
    private Paint mTopTextPaint;


    /**
     * 绘制文字背景圆形的画笔
     */
    private Paint textBgPaint;

    /**
     * 圆弧圆心位置
     */
    private int centerX, centerY;
    private RectF mRectF;
    private Paint mOutterCirclePaint;
    private Paint mInnerCirclePain;
    private String mSleepStatus = "";
    private String[] statusArray = {getContext().getString(R.string.tv_no_datas), getContext().getString(R.string.tv_stutus_not_good), getContext().getString(R.string.tv_status_is_good), getContext().getString(R.string.tv_status_very_good)};
    private int mOutterCircleRadius;
    private int mTrackBarColor;
    private int mTrackBarWidth;
    private int mTrackBarLength;
    private int mInnerCircleRadius;
    private int mCircularRingBgColor;
    private int mStartCircularRingColor;
    private int mEndCircularRingColor;
    private int mTrackBarCounts;
    private float ARC_EACH_PROGRESS = 0;
    private int mTopTextColor;
    private int mTopTextSize;
    private String mTopText;
    private int mMiddleTextColor;
    private int mMiddleTextSize;
    private String mMiddleText;
    private int mBottomTextColor;
    private int mBottomTextSize;
    private String mBottomText;
    private Paint mMiddleTextPaint;
    private Paint mBottomTextPaint;
    private int mProgressWidth;
    private int mProgressDefaultWidth;
    private int mMiddleRightTextSize;
    private float mProgress;


    public CircleProgressBarView(Context context) {
        this(context, null);
    }


    public CircleProgressBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }


    public CircleProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView);

        //外层刻度条颜色
        mTrackBarColor = a.getColor(R.styleable.CircleProgressBarView_trackBarColor, Color.parseColor("#88236fa1"));
        //外层刻度条width
        mTrackBarWidth = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_trackBarWidth, (int) getResources().getDimension(R.dimen.w4)));
        //外层刻度条length
        mTrackBarLength = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_trackBarLength, (int) getResources().getDimension(R.dimen.h20)));
        //外层刻度条 刻度总数量
        mTrackBarCounts = a.getInteger(R.styleable.CircleProgressBarView_trackBarCounts, 100);
        //外圆半径
        mOutterCircleRadius = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_outterRadius, (int) getResources().getDimension(R.dimen.w300)));

        //进度条宽度
        mProgressWidth = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_progressWidth, (int) getResources().getDimension(R.dimen.w30)));
        //背景圆环宽度
        mProgressDefaultWidth = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_progressDefaultWidth, (int) getResources().getDimension(R.dimen.w30)));
        //内圆半径
        mInnerCircleRadius = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_innerRadius, (int) getResources().getDimension(R.dimen.w300)));
        //圆环进度条默认颜色
        mCircularRingBgColor = a.getColor(R.styleable.CircleProgressBarView_circularRingBgColor, Color.GRAY);
        //圆环进度条默认颜色
        mStartCircularRingColor = a.getColor(R.styleable.CircleProgressBarView_circularRingStartColor, Color.RED);
        //圆环进度条默认颜色
        mEndCircularRingColor = a.getColor(R.styleable.CircleProgressBarView_circularRingEndColor, Color.GREEN);
        //圆环进度顶部文字颜色
        mTopTextColor = a.getColor(R.styleable.CircleProgressBarView_topTextColor, Color.RED);
        //圆环进度顶部文字大小
        mTopTextSize = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_topTextSize, (int) getResources().getDimension(R.dimen.h50)));
        //圆环进度顶部文字内容
        mTopText = a.getString(R.styleable.CircleProgressBarView_topText);
        //圆环进度中间文字颜色
        mMiddleTextColor = a.getColor(R.styleable.CircleProgressBarView_middleTextColor, Color.BLUE);
        //圆环进度中间文字大小
        mMiddleTextSize = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_middleTextSize, (int) getResources().getDimension(R.dimen.w68)));
        //圆环进度中间偏右文字大小
        mMiddleRightTextSize = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_middleRightTextSize, (int) getResources().getDimension(R.dimen.w68)));
        //圆环进度中间文字内容
        mMiddleText = a.getString(R.styleable.CircleProgressBarView_topText);
        //圆环进度底部文字颜色
        mBottomTextColor = a.getColor(R.styleable.CircleProgressBarView_bottomTextColor, Color.BLUE);
        //圆环进度底部文字大小
        mBottomTextSize = AutoUtils.getPercentWidthSize(a.getDimensionPixelSize(R.styleable.CircleProgressBarView_bottomTextSize, (int) getResources().getDimension(R.dimen.w44)));
        //圆环进度底部文字内容
        mBottomText = a.getString(R.styleable.CircleProgressBarView_bottomText);
        //每个进度条所占用角度
        ARC_EACH_PROGRESS = ARC_FULL_DEGREE * 1.0f / (mTrackBarCounts - 1);
        a.recycle();

        //外层刻度条 paint
        progressPaint = new Paint();
        progressPaint.setColor(mTrackBarColor);
        progressPaint.setStrokeWidth(mTrackBarWidth);
        progressPaint.setAntiAlias(true);

        //外圆彩色画笔
        mOutterCirclePaint = new Paint();
        mOutterCirclePaint.setStrokeWidth((float) (mProgressWidth));
        mOutterCirclePaint.setStyle(Paint.Style.STROKE);
//        mOutterCirclePaint.setStrokeCap(Paint.Cap.ROUND);//设置线条两端为圆形
        mOutterCirclePaint.setAntiAlias(true);

        //內灰色圆画笔
        mInnerCirclePain = new Paint();
        mInnerCirclePain.setAntiAlias(true);
        mInnerCirclePain.setColor(mCircularRingBgColor);
        mInnerCirclePain.setStrokeWidth((float) (mProgressDefaultWidth));
        mInnerCirclePain.setStyle(Paint.Style.STROKE);

        //绘制环形内顶部文字大小,颜色 paint
        mTopTextPaint = new Paint();
        mTopTextPaint.setColor(mTopTextColor);
        mTopTextPaint.setAntiAlias(true);

        //绘制环形内中间文字大小,颜色 paint
        mMiddleTextPaint = new Paint();
        mMiddleTextPaint.setColor(mMiddleTextColor);
        mMiddleTextPaint.setAntiAlias(true);

        //绘制环形内顶部文字大小,颜色 paint
        mBottomTextPaint = new Paint();
        mBottomTextPaint.setColor(mBottomTextColor);
        mBottomTextPaint.setAntiAlias(true);

        //环形內园背景颜色 paint
        textBgPaint = new Paint();
        textBgPaint.setStyle(Paint.Style.FILL);//设置填充
        textBgPaint.setColor(Color.TRANSPARENT);
        textBgPaint.setAntiAlias(true);


        mRectF = new RectF();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        Log.d("llw", "onMeasure");

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = Math.max(mOutterCircleRadius * 2, heightSize);
        } else {
            width = Math.max(mOutterCircleRadius * 2, widthSize);
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = Math.min(mOutterCircleRadius * 2, widthSize);
        } else {
            height = Math.min(mOutterCircleRadius * 2, heightSize);
        }
//        Log.d("llw", "onMeasure,width:" + width + ",height:" + height);
        setMeasuredDimension(width, height);
    }


    private Rect textBounds = new Rect();


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AutoUtils.autoSize(this);
        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;
        // 圆环位置
        mRectF.left = centerX - mInnerCircleRadius; // 左上角x
        mRectF.top = centerY - mInnerCircleRadius; // 左上角y
        mRectF.right = centerX + mInnerCircleRadius; // 右下角x
        mRectF.bottom = centerY + mInnerCircleRadius; // 右下角y
        Log.d("llw", "centerX:" + centerX + ",centerY:" + centerY + ",内圆半径:" + mInnerCircleRadius + ",外圆半径:" + mOutterCircleRadius);
        if (mProgress >= 80) {
//            progressPaint.setColor(Color.parseColor( "#ff85c229"));
            progressPaint.setColor(Color.parseColor(calColor(progress / max, "#ff0093dd", "#ff85C226")));
            mOutterCirclePaint.setColor(Color.parseColor(calColor(progress / max, "#ff0093dd", "#ff85C226")));
        } else if (mProgress >= 60) {
//            progressPaint.setColor(Color.parseColor("#ff00ff00"));
            progressPaint.setColor(Color.parseColor(calColor(progress / max, "#ffE67817", "#ff0093dd")));
            mOutterCirclePaint.setColor(Color.parseColor(calColor(progress / max, "#ffE67817", "#ff0093dd")));
        } else if (mProgress >= 0) {
//            progressPaint.setColor(Color.parseColor("#ffff5500"));
            progressPaint.setColor(Color.parseColor(calColor(progress / max, "#ffD4380E", "#ffE67817")));
            mOutterCirclePaint.setColor(Color.parseColor(calColor(progress / max, "#ffD4380E", "#ffE67817")));
        }
        //绘制外围刻度
        float sweep1 = ARC_FULL_DEGREE * (progress / max); //进度划过的角度
        drawOutterLine(canvas, ARC_FULL_DEGREE);
        //绘制内圆
        drawInnerGrayCircle(canvas);


        if (mProgress < 0) {
            //绘制文字背景圆形
            drawTextBg(canvas);
            //绘制"无数据"内容
            drawErrorText(canvas);

        } else {
            //绘制进度条
            canvas.drawArc(mRectF, -90, sweep1, false, mOutterCirclePaint);
            //绘制文字背景圆形
            drawTextBg(canvas);
            //绘制圆环內的文字
            drawText(canvas);
        }


    }

    /**
     * 绘制无数据
     *
     * @param canvas
     */
    private void drawErrorText(Canvas canvas) {
        //中间文字:分数值
        mMiddleTextPaint.setTextSize(mMiddleTextSize);
        mMiddleTextPaint.setColor(Color.GRAY);
        String text = statusArray[0];
        float textLen = mMiddleTextPaint.measureText(text);
        //计算文字高度
        mMiddleTextPaint.getTextBounds(text, 0, 1, textBounds);
        float h1 = textBounds.height();
        canvas.drawText(text, centerX - textLen / 2, centerY + h1 / 2, mMiddleTextPaint);
    }

    /**
     * 绘制有数据时候的文本信息
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        //中间文字:分数值
        mMiddleTextPaint.setTextSize(mMiddleTextSize);
        mMiddleTextPaint.setColor(Color.YELLOW);
        String text = (int) (100 * progress / max) + "";
        float textLen = mMiddleTextPaint.measureText(text);
        //计算文字高度
        mMiddleTextPaint.getTextBounds("8", 0, 1, textBounds);
        float h1 = textBounds.height();
        canvas.drawText(text, centerX - textLen / 2, centerY + h1 / 2, mMiddleTextPaint);

        //分
        mMiddleTextPaint.setColor(Color.WHITE);
        mMiddleTextPaint.setTextSize((float) mMiddleRightTextSize);
        mMiddleTextPaint.getTextBounds(getContext().getString(R.string.tv_fraction), 0, 1, textBounds);
        float h11 = textBounds.height();
        canvas.drawText(getContext().getString(R.string.tv_fraction), centerX + textLen / 2 + 5, centerY + h1 / 2 + h11 / 2 + 5, mMiddleTextPaint);


        //最上面文字:睡眠分数
        mTopTextPaint.setTextSize(mTopTextSize);
        String topText = getContext().getString(R.string.tv_circle_sleep_score);
        float topTextLen = mTopTextPaint.measureText(topText);
        //计算文字高度
        mTopTextPaint.getTextBounds(topText, 0, 3, textBounds);
        float topTextH1 = textBounds.height();
        canvas.drawText(topText, centerX - topTextLen / 2, centerY - h1 / 2 - topTextH1, mTopTextPaint);


        //底部文字
        if (mProgress >= 80) {
            mBottomTextPaint.setColor(Color.parseColor("#ff85C226"));
        } else if (mProgress >= 60) {
            mBottomTextPaint.setColor(Color.parseColor("#ff0093dd"));
        } else {
            mBottomTextPaint.setColor(Color.parseColor("#ffE67817"));
        }
        mBottomTextPaint.setTextSize(mBottomTextSize);

        String bottomText = "";
        if (progress == -1) {
            //网络异常/无数据
            //, , , getContext().getString(R.string.tv_status_very_good)
            bottomText = getContext().getString(R.string.tv_no_datas);
        } else if (progress >= 80) {
            bottomText = getContext().getString(R.string.tv_status_very_good);
        } else if (progress > 60) {
            bottomText = getContext().getString(R.string.tv_status_is_good);
        } else {
            bottomText = getContext().getString(R.string.tv_stutus_not_good);
        }

        textLen = mBottomTextPaint.measureText(bottomText);
        canvas.drawText(bottomText, centerX - textLen / 2, centerY + centerY / 2 + textBounds.height() / 4, mBottomTextPaint);
    }

    private void drawTextBg(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, mInnerCircleRadius, textBgPaint);
    }

    private void drawOutterLine(Canvas canvas, float sweep1) {
        float start = 30; //进度条起始角度   >> 1即除以2
        float drawDegree = 1.2f;
        while (drawDegree <= ARC_FULL_DEGREE) {
            double a = (start + drawDegree) / 180 * Math.PI;
            float lineStartX = centerX - mOutterCircleRadius * (float) Math.sin(a);
            float lineStartY = centerY + mOutterCircleRadius * (float) Math.cos(a);
            float lineStopX = lineStartX + mTrackBarLength * (float) Math.sin(a);
            float lineStopY = lineStartY - mTrackBarLength * (float) Math.cos(a);


            if (drawDegree > sweep1) {
                //绘制进度条背景
                progressPaint.setColor(Color.parseColor("#88236fa1"));
                progressPaint.setStrokeWidth(mTrackBarWidth >> 1);
            }
            canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, progressPaint);

            // 绘制圆圈，进度条背景
            drawDegree += ARC_EACH_PROGRESS;
        }
    }

    private void drawInnerGrayCircle(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, mInnerCircleRadius, mInnerCirclePain);
    }


    public void setMax(int max) {
        this.max = max;
        invalidate();
    }


    //动画切换进度值(异步)
    public void setProgress(final float progress) {
        AutoUtils.autoSize(this);
        mProgress = progress;
        if (progress == -1) {
            //网络异常/无数据
            mSleepStatus = statusArray[0];
        } else if (progress >= 80) {
            mSleepStatus = statusArray[3];
        } else if (progress > 60) {
            mSleepStatus = statusArray[2];
        } else {
            mSleepStatus = statusArray[1];
        }

        if (CircleProgressBarView.this.progress == progress) {
            return;
        }
        CircleProgressBarView.this.progress = progress;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }


    //直接设置进度值（同步）
    public void setProgressSync(float progress) {
        this.progress = progress;
        invalidate();
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

    @Override
    public void setTextById(int id) {

    }

    @Override
    public void setTextWithString(String text) {

    }

    @Override
    public void setTextByArrayAndIndex(int arrId, int arrIndex) {

    }

    @Override
    public void reLoadLanguage() {

    }
}