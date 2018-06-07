package com.kye.smart.multi_image_selector_library.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * @version V1.0
 * @Author ly
 * @company 跨越速运
 * @Description
 * @Date 2017/5/24
 */
public class StatusBarUtil {

    /**
     * 设置状态栏纯色 不加半透明效果
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setColorNoTranslucent(Activity activity, int color) {
        setColor(activity, color, 0);
    }



    public static void setColor(Activity activity, int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusBarView(activity, color, statusBarAlpha);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            if (decorView.findViewById(0) == null) {
                decorView.addView(statusView);
            }
            setRootView(activity);
        }
    }


    private static View createStatusBarView(Activity activity, int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = null;
        statusBarView = activity.findViewById(0);
        if (statusBarView != null) {
            statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
            return statusBarView;
        }
        statusBarView = new View(activity);
        statusBarView.setId(0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    private static int calculateStatusColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            rootView.setFitsSystemWindows(true);
        }
        rootView.setClipToPadding(true);
    }
}
