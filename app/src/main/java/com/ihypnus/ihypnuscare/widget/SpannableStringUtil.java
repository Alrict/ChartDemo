package com.ihypnus.ihypnuscare.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

/**
 * 文字处理工具类
 */
public class SpannableStringUtil {

    /**
     * 超链接
     */
    public static SpannableString addUrlSpan(String text, String url, int start, int end) {
        SpannableString spanString = new SpannableString(text);
        URLSpan span = new URLSpan(url);
        spanString.setSpan(span, start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 文字背景颜色
     */
    public static SpannableString addBackColorSpan(String text, int start, int end , int color) {
        SpannableString spanString = new SpannableString(text);
        BackgroundColorSpan span = new BackgroundColorSpan(color);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 文字颜色
     */
    public static SpannableStringBuilder addForeColorSpanBuilder(String text, int start, int end , int color) {
        SpannableStringBuilder spanString = new SpannableStringBuilder(text);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 文字颜色及下划线
     */
    public static SpannableString addForeColorSpan(String text, int start, int end , int color) {
        SpannableString spanString = new SpannableString(text);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spanString.setSpan(underlineSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 字体大小
     */
    public static SpannableString addFontSpan(String text, int start, int end , int textsize) {
        SpannableString spanString = new SpannableString(text);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(textsize);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 粗体，斜体
     */
    public static SpannableString addStyleSpan(String text, int start, int end) {
        SpannableString spanString = new SpannableString(text);
        StyleSpan span = new StyleSpan(Typeface.BOLD_ITALIC);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 删除线
     */
    public static SpannableString addStrikeSpan(String text, int start, int end) {
        SpannableString spanString = new SpannableString(text);
        StrikethroughSpan span = new StrikethroughSpan();
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 下划线
     */
    public static SpannableString addUnderLineSpan(String text, int start, int end) {
        SpannableString spanString = new SpannableString(text);
        UnderlineSpan span = new UnderlineSpan();
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }



    /**
     * 图片
     */
    public static SpannableString addImageSpan(Context context, String text, int start, int end, int drawable) {
        SpannableString spanString = new SpannableString(text);
        Drawable d = context.getResources().getDrawable(drawable);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }
}
