package com.kye.smart.multi_image_selector_library.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kye.smart.multi_image_selector_library.R;

import java.lang.reflect.Field;


/**
 * Created by xiongxiaofeng on 2016/7/20.
 * 自定义Toast
 */
public class CusttomToast extends Toast {
    private static Toast mToast;

    public CusttomToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
//        Toast result = new Toast(context);
        if (mToast == null) {
            mToast = new Toast(context);
        } else {
            mToast.cancel();
            mToast = new Toast(context);
        }

        //获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //获得屏幕的宽度
        int width = wm.getDefaultDisplay().getWidth();

        //由layout文件创建一个View对象
        View view = inflater.inflate(R.layout.mis_top_toast, null);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.leftMargin = 16;

        TextView toastTextView = (TextView) view.findViewById(R.id.tv_toast);
        //设置TextView的宽度为 屏幕宽度
        toastTextView.setLayoutParams(layoutParams);
        toastTextView.setText(text);

        mToast.setView(view);
//        int statusHeight = StatusBarUtil.getStatusBarHeight(context);
        mToast.setGravity(Gravity.TOP, 0, 0);
//        mToast.setMargin(0, statusHeight);
        mToast.setDuration(duration);
        try {
            Object mTN = null;
            mTN = getField(mToast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams != null
                        && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
//                    params.verticalMargin = statusHeight;
                    params.windowAnimations = R.style.Mis_Animtop;
                }
            }
        } catch (Exception e) {

        }
        return mToast;
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }
}
