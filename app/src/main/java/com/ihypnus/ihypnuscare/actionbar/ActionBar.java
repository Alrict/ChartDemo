package com.ihypnus.ihypnuscare.actionbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.kye.smart.multi_image_selector_library.callback.OnRippleCompleteListener;

public class ActionBar extends FrameLayout implements OnRippleCompleteListener {
    private LinearRippleView mMiddleView;
    private MarqueeTextView mTitleView;
    private ImageView mMiddleIcon;
    //
    private RippleView mLeftView;
    private ImageView mLeftIcon;
    private TextView mLeftText;
    //
    private RippleView mRightView;
    private RippleView mRightView2;
    private ImageView mRightIcon;
    private TextView mRightText;
    private TextView mRightText2;
    private OnActionBarListener onActionBarListener;
    /**
     * Index used to indicate the ActionBar home item has been clicked.
     */
    public static final int LEFT_ITEM = -1;
    public static final int MIDDLE_ITEM = 0;
    public static final int RIGHT_ITEM = 1;

    /**
     * Interface definition for a callback to be invoked when a user is
     * interacting with an {@link ActionBar}.
     *
     * @author Cyril Mottier
     */
    public interface OnActionBarListener {
        /**
         * Clients may listen to this method in order to be notified the user
         * has clicked on an item.
         *
         * @param position The position of the item in the action bar.
         *                 {@link OnActionBarListener# } means the user
         *                 pressed the "Home" button. 0 means the user clicked the
         *                 first {@link  } (the leftmost item) and so on.
         */
        void onActionBarItemClicked(int position);
    }

    @SuppressLint("NewApi")
    public ActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionBar(Context context) {
        super(context);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.action_bar_normal, this);
        setBackgroundColor(getResources().getColor(R.color.title_layout_color));
        mMiddleView = (LinearRippleView) findViewById(R.id.action_bar_middle);
        mTitleView = (MarqueeTextView) findViewById(R.id.action_bar_title);
//		mTitleView.setTextSize(ViewUtils.getDimenPx(R.dimen.w50));
        mMiddleIcon = (ImageView) findViewById(R.id.action_bar_middle_img);
        mLeftText = (TextView) findViewById(R.id.action_left_text);
        mLeftView = (RippleView) findViewById(R.id.action_bar_left);
        mLeftIcon = (ImageView) findViewById(R.id.action_left_img);
        mRightView = (RippleView) findViewById(R.id.action_bar_right);
        mRightView2 = (RippleView) findViewById(R.id.action_bar_right2);
        mRightText = (TextView) findViewById(R.id.action_right_txt);
        mRightText2 = (TextView) findViewById(R.id.action_right_txt2);
        mRightIcon = (ImageView) findViewById(R.id.action_right_img);
        mMiddleView.setEnabled(false);
        mRightView.setEnabled(false);
        mLeftView.setOnRippleCompleteListener(this);
        mMiddleView.setOnRippleCompleteListener(this);
        mRightView.setOnRippleCompleteListener(this);
    }

    public void setOnActionBarListener(OnActionBarListener onActionBarListener) {
        this.onActionBarListener = onActionBarListener;
    }

    public void setTitle(@StringRes int strId , Drawable drawable) {
        // float titleSize =
        // getContext().getResources().getDimension(R.dimen.gd_arrow_offset);
        if (mTitleView != null) {
            mTitleView.setText(strId);

            // drawable.setBounds(left, top, right, bottom)
            if (drawable != null)
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTitleView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void setTitle(CharSequence title, Drawable drawable) {
        // float titleSize =
        // getContext().getResources().getDimension(R.dimen.gd_arrow_offset);
        if (mTitleView != null) {
            mTitleView.setText(title);
            // drawable.setBounds(left, top, right, bottom)
            if (drawable != null)
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTitleView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public void setTitle(@StringRes int strId) {
        mTitleView.setTextById(strId);
    }

    public void setLeftDrawable(int drawableId) {
        mLeftIcon.setVisibility(View.VISIBLE);
        // mLeftText.setVisibility(View.GONE);
        mLeftIcon.setImageResource(drawableId);
    }

    public void setLeftDrawableVisibility(int visibility) {
        mLeftIcon.setVisibility(visibility);
        // mLeftText.setVisibility(View.GONE);
    }

    public void setRightDrawable(int drawableId) {
        mRightView.setEnabled(true);
        mRightIcon.setVisibility(View.VISIBLE);
        // mRightText.setVisibility(View.GONE);
        mRightIcon.setImageResource(drawableId);
    }

    public ImageView getLeftImage() {
        return mLeftIcon;
    }

    public ImageView getRightImage() {
        return mRightIcon;
    }

    public ImageView getMiddleImage() {
        return mMiddleIcon;
    }

    public void setLeftText(int textId) {
        mLeftText.setVisibility(View.VISIBLE);
        mLeftText.setText(textId);
        // mLeftIcon.setVisibility(View.GONE);
    }

    public void setLeftText(String text) {
        mLeftText.setVisibility(View.VISIBLE);
        mLeftText.setText(text);
        // mLeftIcon.setVisibility(View.GONE);
    }

    public void setRightText(int textId) {
        mRightView.setEnabled(true);
        mRightIcon.setVisibility(View.GONE);
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(textId);
    }

    public void setRightText(String text) {
        mRightView.setEnabled(true);
        mRightIcon.setVisibility(View.GONE);
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(text);
    }

    public View getMiddleView() {
        return mMiddleView;
    }

    public View getLeftView() {
        return mLeftView;
    }

    public View getRightView() {
        return mRightView;
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public TextView getLeftText() {
        return mLeftText;
    }

    public TextView getRightText() {
        return mRightText;
    }

    @Override
    public void onComplete(View v) {

        if (onActionBarListener != null) {
            if (v.getId() == R.id.action_bar_right) {
                onActionBarListener.onActionBarItemClicked(RIGHT_ITEM);
            } else if (v.getId() == R.id.action_bar_middle) {
                // mMiddleView.setEnabled(enabled);
                onActionBarListener.onActionBarItemClicked(MIDDLE_ITEM);
            } else if (v.getId() == R.id.action_bar_left) {
                onActionBarListener.onActionBarItemClicked(LEFT_ITEM);
            }
        }
    }

    public void setRightText2(String text) {
        mRightView2.setVisibility(View.VISIBLE);
        mRightText2.setText(text);
    }

    public TextView getRightText2() {
        return mRightText2;
    }

    public View getRightView2() {
        return mRightView2;
    }
}
