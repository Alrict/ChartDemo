package com.android.volley;

/**
 * @Package com.android.volley
 * @author: llw
 * @Description:
 * @date: 2018/6/20 17:43
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class NetResponseHandler {
    private static final String TAG = "HttpRequest";
    private static final String ERROR_INFO_NO_SET_CALLBACK = "You have no settings com.kye.net.ResponseCallback !";
    private Request request;

    public NetResponseHandler() {
    }

    public final Request getRequest() {
        return this.request;
    }

    public final void setRequest(Request request) {
        this.request = request;
    }

    public final void success(Request request, Object responseData, String successCode, String successMsg, ResponseCallback responseCallback) {
        boolean isHandler = this.onSuccess(request, responseData, successCode, successMsg, responseCallback);
        if(responseCallback != null) {
            if(!isHandler) {
                responseCallback.onSuccess(responseData, successCode, successMsg);
            }
        } else {

        }

    }

    public final void error(Request request, VolleyError error, String errorCode, String errMsg, ResponseCallback responseCallback) {
        boolean isHandler = this.onError(request, error, errorCode, errMsg, responseCallback);
        if(responseCallback != null) {
            if(!isHandler) {
                responseCallback.onError(error, errorCode, errMsg);
            }
        } else {

        }

    }

    protected boolean onSuccess(Request request, Object responseData, String successCode, String successMsg, ResponseCallback responseCallback) {
        return false;
    }

    protected boolean onError(Request request, VolleyError error, String errorCode, String errMsg, ResponseCallback responseCallback) {
        return false;
    }
}
