package com.ihypnus.ihypnuscare.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.ihypnus.ihypnuscare.utils.HttpLog;

import org.json.JSONException;

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
public class HttpRequest extends Request {

    private Response.ErrorListener mErrorListener;
    private boolean isCallBacked;
    private StringBuilder mRequestLog = new StringBuilder();
    private static final Gson gson = new Gson();
    private HashMap mParams;
    private Map<String, String> mHeaderParams;
    private Type mResponseJavaBean;
    private Response.Listener<Object> listener;
    private int mResponseDataType;//请求结果解析类型 0:请求结果是string,1:请求结果是object
    private String mDecodeCharset = "UTF-8";

    public HttpRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mParams = new HashMap();
    }

    public HttpRequest(int method, String url, Response.Listener<Object> listener, Map<String, String> params,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
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
//            HttpLog.w("request:", mRequestLog.toString());
        }
    }

    public HttpRequest setResponseDataType(int responseDataType) {
        this.mResponseDataType = responseDataType;
        return this;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        byte[] data = response.data;
        String parsedData = "";
        Object ex = null;
        boolean var14 = false;
        Response var6;

        label121:
        {
            Response var7;
            String exceptionStr = "";
            label122:
            {
                try {
                    var14 = true;
                    parsedData = new String(data, this.mDecodeCharset);
                    Object obj = this.getResponseData(parsedData);
                    var6 = Response.success(obj, HttpHeaderParser.parseCacheHeaders(response));
                    var14 = false;
                    break label121;
                } catch (UnsupportedEncodingException var15) {
                    HttpLog.printStackTrace(var15);
                    ex = var15;
                    var7 = Response.error(new ParseError(var15));
                    var14 = false;
                    break label122;
                } catch (Exception var16) {
                    ex = var16;
                    HttpLog.printStackTrace(var16);
                    var7 = Response.error(new ParseError(var16));
                    var14 = false;
                } finally {
                    if (var14) {
                        if (HttpLog.LOG_FLAG) {
                            if (ex != null) {
                                exceptionStr = HttpLog.getExceptionMsg((Throwable) ex);
                            }

                            this.appendkyeNetLog(parsedData, exceptionStr);
                            HttpLog.w("volley_request", this.mRequestLog.toString());
                        }

                    }
                }

                if (HttpLog.LOG_FLAG) {
                    exceptionStr = "";
                    if (ex != null) {
                        exceptionStr = HttpLog.getExceptionMsg((Throwable) ex);
                    }

                    this.appendkyeNetLog(parsedData, exceptionStr);
                    HttpLog.w("volley_request", this.mRequestLog.toString());
                }

                return var7;
            }

            if (HttpLog.LOG_FLAG) {
                exceptionStr = "";
                if (ex != null) {
                    exceptionStr = HttpLog.getExceptionMsg((Throwable) ex);
                }

                this.appendkyeNetLog(parsedData, exceptionStr);
                HttpLog.w("volley_request", this.mRequestLog.toString());
            }

            return var7;
        }
        if (HttpLog.LOG_FLAG) {
            String exceptionStr = "";
            if (ex != null) {
                exceptionStr = HttpLog.getExceptionMsg((Throwable) ex);
            }

            this.appendkyeNetLog(parsedData, exceptionStr);
            HttpLog.w("volley_request", this.mRequestLog.toString());
        }

        return var6;
    }

    /**
     * 根据定义的返回类型解析请求结果
     *
     * @param parsedData
     * @return
     * @throws JSONException
     */
    public Object getResponseData(String parsedData) throws JSONException {
        Object obj = null;
        if (this.mResponseDataType == 3) {
            //解析成normal string
            obj = parsedData;
        } else if (this.mResponseDataType == 4) {
            //解析成普通的javaBean
            obj = gson.fromJson(parsedData, this.mResponseJavaBean);
        }
        return obj;
    }

    @Override
    protected void deliverResponse(Object response) {
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

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }

    /**
     * 设置请求结果返回的bean
     *
     * @param responseType
     * @return
     */
    public HttpRequest setResponseJavaBean(Type responseType) {
        this.mResponseJavaBean = responseType;
        return this;
    }


    /**
     * 设置返回结果类型
     */
    public static class ResponseDataType {
        //        public static final int RESULT_STRING = 1;
//        public static final int RESULT_JAVA_BEAN = 2;
        public static final int NORMAL_STRING = 3;
        public static final int NORMAL_JAVABEAN = 4;

        public ResponseDataType() {
        }
    }
}