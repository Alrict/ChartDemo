package com.ihypnus.ihypnuscare.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetResponseHandler;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseCallback;
import com.android.volley.ResponseResult;
import com.android.volley.TimeoutError;
import com.android.volley.UnknownVolleyError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ihypnus.ihypnuscare.IhyApplication;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.HttpLog;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.multilanguage.MultiLanguageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @Package com.ihypnus.ihypnuscare.net
 * @author: llw
 * @Description:
 * @date: 2018/5/17 15:18
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HttpRequest extends Request {
    public static String K = "99B22B64D53D2234RTY6A7360ABAEB82";
    private Response.ErrorListener mHttpErrorListenr;
    private NetResponseHandler mNetResponseHandler;
    private Response.ErrorListener mErrorListener;
    private ResponseCallback mResponseCallback;
    private boolean isCallBacked;
    private StringBuilder mRequestLog = new StringBuilder();
    private static final Gson gson = new Gson();
    private HashMap mParams;
    private Map<String, String> mHeaderParams;
    private Type mResponseJavaBean;
    private Response.Listener<Object> listener;
    private int mResponseDataType;//请求结果解析类型 0:请求结果是string,1:请求结果是object
    private String mDecodeCharset = "UTF-8";
    private ApiResponseSuccessListener mSuccessListener;

    public HttpRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mParams = new HashMap();
    }

    public HttpRequest(int method, String url, Response.Listener<Object> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.mParams = new HashMap();
    }

    public HttpRequest(int method, String url, ResponseCallback callback) {
        super(method, url);
        this.mParams = new HashMap();
        this.mHttpErrorListenr = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error == null) {
                        error = new UnknownVolleyError();
                    }

                    String errorMsg = "";
                    if (!(error instanceof NoConnectionError) && !(error instanceof TimeoutError)) {
                        if (((VolleyError) error).networkResponse != null) {
                            int statusCode = ((VolleyError) error).getHttpStatusCode();
                            errorMsg = errorMsg + statusCode + IhyApplication.mInstance.getString(R.string.tv_net_connect_error);
                        }
                    } else {
                        errorMsg = IhyApplication.mInstance.getString(R.string.tv_net_connet_error01);
                    }

                    HttpRequest.this.doErrorCallback(HttpRequest.this, (VolleyError) error, "", errorMsg, HttpRequest.this.mResponseCallback);
                } catch (Exception var7) {
                    HttpLog.printStackTrace(var7);
                    HttpRequest.this.doErrorCallback(HttpRequest.this, new VolleyError((Throwable) error), "", "", HttpRequest.this.mResponseCallback);
                } finally {
                    if (HttpLog.LOG_FLAG) {
                        HttpRequest.this.appendkyeNetLog("", HttpLog.getExceptionMsg((Throwable) error));
                        HttpLog.w("volley_request", HttpRequest.this.mRequestLog.toString());
                    }

                }

            }
        };
        this.mErrorListener = this.mHttpErrorListenr;
        this.mResponseCallback = callback;
        init();
    }

    public HttpRequest(int method, String url, Response.Listener<Object> listener, Map<String, String> params,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.mParams = new HashMap();
        mParams.putAll(params);
    }

    public HttpRequest(int method, String url, Map<String, String> params,
                       ResponseCallback callback) {
        super(method, url);
        this.listener = listener;
        this.mParams = new HashMap();
        mParams.putAll(params);
        this.mHttpErrorListenr = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error == null) {
                        error = new UnknownVolleyError();
                    }

                    String errorMsg = "";
                    if (!(error instanceof NoConnectionError) && !(error instanceof TimeoutError)) {
                        if (((VolleyError) error).networkResponse != null) {
                            int statusCode = ((VolleyError) error).getHttpStatusCode();
                            errorMsg = errorMsg + statusCode + IhyApplication.mInstance.getString(R.string.tv_net_connect_error);
                        }
                    } else {
                        errorMsg = IhyApplication.mInstance.getString(R.string.tv_net_connet_error01);
                    }

                    HttpRequest.this.doErrorCallback(HttpRequest.this, (VolleyError) error, "", errorMsg, HttpRequest.this.mResponseCallback);
                } catch (Exception var7) {
                    HttpLog.printStackTrace(var7);
                    HttpRequest.this.doErrorCallback(HttpRequest.this, new VolleyError((Throwable) error), "", "", HttpRequest.this.mResponseCallback);
                } finally {
                    if (HttpLog.LOG_FLAG) {
                        HttpRequest.this.appendkyeNetLog("", HttpLog.getExceptionMsg((Throwable) error));
                        HttpLog.w("volley_request", HttpRequest.this.mRequestLog.toString());
                    }

                }

            }
        };
        this.mErrorListener = this.mHttpErrorListenr;
        this.mResponseCallback = callback;
        init();
    }

    private void init() {
        this.setErrorListener(this.mErrorListener);
        this.mSuccessListener = new HttpRequest.ApiResponseSuccessListener();
    }

    private class ApiResponseSuccessListener implements Response.Listener {
        private ApiResponseSuccessListener() {
        }

        public void onResponse(Object response) {
            try {
                if (HttpRequest.this.mResponseDataType == 3) {
                    HttpRequest.this.doSuccessCallback(HttpRequest.this, response.toString(), "", "", HttpRequest.this.mResponseCallback);
                } else if (HttpRequest.this.mResponseDataType == 4) {
                    HttpRequest.this.doSuccessCallback(HttpRequest.this, response, "", "", HttpRequest.this.mResponseCallback);
                } else {
                    ResponseResult responseResult = (ResponseResult) response;
                    String errMsg = responseResult.getContent();
                    String errCode = responseResult.getErrorCode();
                    Object object = responseResult.getResultObject();
                    String type = responseResult.getType();
                    errMsg = handlerErrorMsg(errCode, errMsg);
                    if (!StringUtils.isNullOrEmpty(type) && type.equals("success")) {
                        HttpRequest.this.doSuccessCallback(HttpRequest.this, object, errCode, errMsg, HttpRequest.this.mResponseCallback);
                    } else {
                        HttpRequest.this.doErrorCallback(HttpRequest.this, new VolleyError(), errCode, errMsg, HttpRequest.this.mResponseCallback);
                    }
                }
            } catch (Exception var7) {
                HttpLog.printStackTrace(var7);
                HttpRequest.this.doErrorCallback(HttpRequest.this, new VolleyError(var7), "", "", HttpRequest.this.mResponseCallback);
            }

        }
    }

    @SuppressLint("StringFormatInvalid")
    private String handlerErrorMsg(String errorCode, String errorMsg) {
        if (errorCode.equals("0000")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_success,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("0001")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_send_success,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("0013")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error0013,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1000")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1000,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1119")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1119,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1120")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_pwd_error,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1121")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_account_locked,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1300")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1300,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1301")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1301,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1302")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1302,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1304")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1304,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1305")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1305,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }

        if (errorCode.equals("1306")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1036,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1307")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1037,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1308")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1038,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1310")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error1310,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1311")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error_1311,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1312")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_error_1312,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }
        if (errorCode.equals("1400")) {
            return IhyApplication.mInstance.getString(R.string.tv_toast_value_ilegal,
                    getAppLanguage(IhyApplication.mInstance).getDisplayLanguage());
        }

        return errorMsg;
    }

    private Locale getAppLanguage(Context context) {
        MultiLanguageUtil.init(context);
        return MultiLanguageUtil.getInstance().getLanguageLocale();
    }

    @Override
    public Request<?> setRequestQueue(RequestQueue requestQueue) {
        this.mNetResponseHandler = requestQueue.getNetResponseHandler();
        return super.setRequestQueue(requestQueue);

    }

    private void doErrorCallback(Request request, VolleyError error, String errorCode, String errMsg, ResponseCallback responseCallback) {
        try {
            if (this.mNetResponseHandler == null) {
                HttpLog.e("volley_request", "NetResponseHandler is null !");
                return;
            }

            if (this.isCallBacked) {
                return;
            }

            this.isCallBacked = true;
            this.mNetResponseHandler.error(request, error, errorCode, errMsg, responseCallback);
        } catch (Exception var7) {
            HttpLog.printStackTrace(var7);
        }

    }

    private void doSuccessCallback(Request request, Object responseData, String successCode, String successMsg, ResponseCallback responseCallback) {
        if (this.mNetResponseHandler == null) {
            HttpLog.e("volley_request", "NetResponseHandler is null !");
        } else if (!this.isCallBacked) {
            this.isCallBacked = true;
            this.mNetResponseHandler.success(request, responseData, successCode, successMsg, responseCallback);
        }
    }

    /**
     * 设置请求头
     */
    public void putHeaders(Map<String, String> headerMap) {
        this.mHeaderParams = headerMap;

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap();
        String accessToken = this.createIdentifier();
        if (accessToken != null) {
            headers.put("access-token", accessToken);
//            headers.put("kye", ORGANIZATION);
        }
        if (mHeaderParams != null) {
            headers.putAll(mHeaderParams);
        }

        headers.putAll(Volley.me.staticInitRequestHead);
        return headers;
    }

    protected String createIdentifier() {
        Map<String, String> parmas = this.getParams();
        if (parmas == null) {
            return null;
        } else {
            Set<String> keySet = parmas.keySet();
            String[] keys = new String[keySet.size()];
            parmas.keySet().toArray(keys);
            Arrays.sort(keys);
            StringBuilder sb = new StringBuilder();
            sb.append(K);
            String[] var5 = keys;
            int var6 = keys.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String key = var5[var7];
                String value = (String) parmas.get(key);
                if (!TextUtils.isEmpty(value) && !this.stringIsEmpty(value)) {
                    sb.append(key + value);
                }
            }

            return encryptionMD5(sb.toString()).toUpperCase();
        }
    }


    /**
     * 传入的字符窜是空格
     *
     * @param content
     * @return
     */
    private boolean stringIsEmpty(String content) {
        if (TextUtils.isEmpty(content)) {
            return true;
        } else {
            int length = content.length();

            for (int i = 0; i < length; ++i) {
                char c = content.charAt(i);
                if (c != 32) {
                    return false;
                }
            }

            return true;
        }
    }

    public static String encryptionMD5(String content) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(content.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();

            for (byte aByteArray : byteArray) {
                if (Integer.toHexString(255 & aByteArray).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(255 & aByteArray));
                } else {
                    md5StrBuff.append(Integer.toHexString(255 & aByteArray));
                }
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        return md5StrBuff.toString();
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
        } else {
            JSONObject jObject = new JSONObject(parsedData);
            ResponseResult responseResult = ResponseResult.parseResponseResult(jObject);
            String type = responseResult.getType();
            String result = responseResult.getResult();
            Object resultObj = null;
            if (this.mResponseDataType == 2) {

                if (type.equals("success") && !TextUtils.isEmpty(result) && this.mResponseJavaBean != null) {
                    resultObj = gson.fromJson(result, this.mResponseJavaBean);
                    responseResult.setResultObject(resultObj);
                    responseResult.setType("success");
                } else {
                    responseResult.setResultObject("");
                    responseResult.setType("error");
                    HttpLog.e("volley_request", "type: " + type + "\nresult: " + result + "\nResponseJavaBean: " + (this.mResponseJavaBean == null ? null : this.mResponseJavaBean.getClass().getSimpleName()));
                }
            } else {
                if (this.mResponseDataType != 1) {
                    throw new RuntimeException("invalid ResponseDataType");
                }

                if (type.equals("success")) {
                    responseResult.setResultObject(result);
                    responseResult.setType("success");
                }
            }

            obj = responseResult;
        }
        return obj;
    }

    @Override
    protected void deliverResponse(Object response) {
        if (listener != null) {
            listener.onResponse(response);
        }
        if (mSuccessListener != null) {
            this.mSuccessListener.onResponse(response);
        }
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
            if (VolleyLog.DEBUG) {
                HttpLog.w("volley_request", "data:  " + data);
            }
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
        public static final int RESULT_STRING = 1;
        public static final int RESULT_JAVA_BEAN = 2;
        public static final int NORMAL_STRING = 3;
        public static final int NORMAL_JAVABEAN = 4;

        public ResponseDataType() {
        }
    }
}
