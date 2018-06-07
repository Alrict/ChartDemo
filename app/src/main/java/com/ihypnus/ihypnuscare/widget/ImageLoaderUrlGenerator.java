package com.ihypnus.ihypnuscare.widget;

import android.text.TextUtils;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version V1.0
 * @createAuthor yzw
 * @createDate 2017/5/17 16:58
 * @updateAuthor
 * @updateDate
 * @company 跨越速运
 * @description imageLoader可以加载本地，网络，资源图片,他是通过uri前缀来做区分。目前这里做了drawable，网络www，本地文件file
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public final class ImageLoaderUrlGenerator {

//    /**
//     * 资源图
//     */
//    private static String PREFIX_R = "R.";

    private static String PREFIX_NET = "www";

    private static String PREFIX_FILE = "file";


    /**
     * 获取imageLoader要求的uri
     * @param tUri uri
     * @return 返回符合要求的uri
     */
    public String getImageLoaderWrapUrl(String tUri) {
        if(TextUtils.isEmpty(tUri)){
            return "";
        }
        String resultUrl = TextUtils.isEmpty(tUri) ? "" : tUri;
        ImageDownloader.Scheme scheme = null;
        if (isNumeric(tUri)) {
            //纯数字为 资源图
            scheme = ImageDownloader.Scheme.DRAWABLE;

        } else if (tUri.startsWith(PREFIX_NET)) {
            // 网络图
            scheme = ImageDownloader.Scheme.HTTP;

        } else if (new File(tUri).exists()) {
            // 本地图
            if (!tUri.startsWith(PREFIX_FILE)) {
                scheme = ImageDownloader.Scheme.FILE;
            }
        }
        if (scheme != null) {
            resultUrl = scheme.wrap(tUri);
        }

        return resultUrl;
    }

    /**
     * 获取imageLoader要求的uri
     * @param tUri uri
     * @return 返回符合要求的uri数组
     */
    public ArrayList<String> getImageLoaderWrapUrList(List<String> tUri) {

        ArrayList<String> urlList = new ArrayList<>(tUri.size() + 1);
        if (tUri == null || tUri.isEmpty()) {
            return urlList;
        }
        for (String url : tUri) {
            urlList.add(getImageLoaderWrapUrl(url));
        }
        return urlList;
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     * @return
     */
    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
