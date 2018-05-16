package com.ihypnus.ihypnuscare.net;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @Package com.ihypnus.ihypnuscare.net
 * @author: llw
 * @Description:
 * @date: 2018/5/16 18:54
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class NetRequestHelper {

    private static NetRequestHelper instance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private NetRequestHelper() {
    }

    public void init(Application application) {
        if(application != null) {
            mContext = application;
            this.mRequestQueue = Volley.newRequestQueue(mContext);
        } else {
            throw new RuntimeException("Application is null ? or IKyeVolleyInit is null ?");
        }
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public static NetRequestHelper getInstance() {
        if(instance == null) {
            syncInit();
        }

        return instance;
    }

    private static synchronized void syncInit() {
        if(instance == null) {
            instance = new NetRequestHelper();
        }

    }

    public static void setContext(Context context) {
        mContext = context;
    }



    public void add(Request request, Object tag) {
        if(tag != null) {
            request.setTag(tag);
        }
            this.mRequestQueue.add(request);


    }

    public void cancelAll(Object tag) {
        if(tag != null) {
            this.mRequestQueue.cancelAll(tag);
        }

    }
}
