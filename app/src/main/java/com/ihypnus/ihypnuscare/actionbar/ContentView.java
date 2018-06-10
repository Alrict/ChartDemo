package com.ihypnus.ihypnuscare.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @version V1.0
 * @createAuthor yzw
 * @createDate 2017/3/29 17:10
 * @updateAuthor
 * @updateDate
 * @company 跨越速运
 * @description
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ContentView extends FrameLayout {

    /**
     * 是否向下面分发事件 true 分发
     */
    private boolean mCanDispatchTouchEvent = true;

    public ContentView(Context context) {
        super(context);
    }

    public ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCanDispatchTouchEvent(boolean canDispatchTouchEvent) {
        mCanDispatchTouchEvent = canDispatchTouchEvent;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mCanDispatchTouchEvent) {
            return super.dispatchTouchEvent(ev);
        } else {
            return false;
        }
    }
}
