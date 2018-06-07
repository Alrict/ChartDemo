package com.ihypnus.ihypnuscare.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.ihypnus.ihypnuscare.R;


public class MaxHeightScrollView extends ScrollView {
    public MaxHeightScrollView(Context context) {
        super(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), (int) Math.min(getMeasuredHeight(), getResources().getDimension(R.dimen.w948)));
    }
}
