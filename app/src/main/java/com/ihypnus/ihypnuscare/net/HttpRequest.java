package com.ihypnus.ihypnuscare.net;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ihypnus.ihypnuscare.utils.HttpLog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Package com.ihypnus.ihypnuscare.net
 * @author: llw
 * @Description:
 * @date: 2018/5/17 15:18
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HttpRequest<T> extends Request<T> {

    private Response.ErrorListener mErrorListener;
    private boolean isCallBacked;
    private StringBuilder mRequestLog = new StringBuilder();
    private static final Gson gson = new Gson();
    private HashMap mParams;
    private Map<String, String> mHeaderParams;
    private Type mResponseJavaBean;
    private Class<T> clazz;
    private Response.Listener<T> listener;


    public HttpRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mParams = new HashMap();
    }

    public HttpRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Map<String, String> params,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
        this.mParams = new HashMap();
        mParams.putAll(params);

    }

    /**
     * 设置请求头部门
     */
    public void putHeaders(Map<String, String> headerMap) {
        this.mHeaderParams = headerMap;

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeaderParams != null) {
            return mHeaderParams;
        }
        return super.getHeaders();

    }


    @Override
    public HashMap getParams() {
        return mParams;
    }

    private void appendkyeNetLog(String parsedData, String exception) {
        if (HttpLog.LOG_FLAG) {
            this.mRequestLog.setLength(0);
            this.mRequestLog.append("requestUrl :" + this.getOriginUrl() + "\n");
            this.mRequestLog.append("requestData: " + gson.toJson(this.mParams) + "\n");
            HashMap heads = new HashMap();

            try {
                heads.putAll(this.getHeaders());
            } catch (AuthFailureError var6) {
                var6.printStackTrace();
            }

            this.mRequestLog.append("requestHead: ");
            Iterator var4 = heads.keySet().iterator();

            while (var4.hasNext()) {
                String key = (String) var4.next();
                if (!TextUtils.isEmpty(key)) {
                    this.mRequestLog.append(key + ": " + (String) heads.get(key) + " ");
                }
            }

            this.mRequestLog.append("\n");
            this.mRequestLog.append("responseData : " + parsedData + "\n");
            this.mRequestLog.append("responseException : " + exception + "\n");
            HttpLog.w("request:", mRequestLog.toString());
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            /**
             * 得到返回的数据
             */
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            appendkyeNetLog(jsonStr, "");
            /**
             * 转化成对象
             */
            return Response.success(gson.fromJson(jsonStr, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    public String getBodyContentType() {
        return "application/json; charset=" + this.getParamsEncoding();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Map<String, String> params = this.getParams();
        return params != null && params.size() > 0 ? this.encodeParameters(params, this.getParamsEncoding()) : null;
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        try {
            String data = gson.toJson(params);
            return data.getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, var4);
        }
    }
}
