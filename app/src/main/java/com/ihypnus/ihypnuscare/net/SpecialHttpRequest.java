package com.ihypnus.ihypnuscare.net;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.ResponseCallback;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.ihypnus.ihypnuscare.utils.HttpLog;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Package com.ihypnus.ihypnuscare.net
 * @author: llw
 * @Description:
 * @date: 2018/6/20 16:07
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class SpecialHttpRequest extends HttpRequest {
    private final Map<String, Object> mParams = new HashMap();
    protected static final Gson gson = new Gson();


    public SpecialHttpRequest(int method, String url, Response.Listener<Object> listener, Map<String, Object> params, Response.ErrorListener errorListener) {
        super(method, url,listener, errorListener);
        this.mParams.putAll(params);
    }

    public SpecialHttpRequest(int method, String url, Map<String, Object> params, ResponseCallback callback) {
        super(method, url,callback);
        this.mParams.putAll(params);
    }

    public String getBodyContentType() {
        return "application/json; charset=" + this.getParamsEncoding();
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap();
        String accessToken = this.createIdentifier();
        if (accessToken != null) {
            headers.put("access-token", accessToken);
        }
        return headers;
    }

    public byte[] getBody() throws AuthFailureError {
        Map<String, Object> params = this.getSpecialParams();
        return params != null && params.size() > 0?this.encodeParameters(this.getParamsEncoding()):null;
    }

    protected Map<String, Object> getSpecialParams() {

        return this.mParams;
    }

    private byte[] encodeParameters(String paramsEncoding) {
        try {
            String data = gson.toJson(this.mParams);
            if(VolleyLog.DEBUG) {
                HttpLog.w("volley_request", "data:  " + data);
            }

            return data.getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, var3);
        }
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        try {
            String data = gson.toJson(params);
            if(VolleyLog.DEBUG) {
                HttpLog.w("volley_request", "data:  " + data);
            }
            return data.getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, var4);
        }
    }

    protected String createIdentifier() {
        if(mParams == null) {
            return null;
        } else {
            Set<String> keySet = mParams.keySet();
            String[] keys = new String[keySet.size()];
            mParams.keySet().toArray(keys);
            Arrays.sort(keys);
            StringBuilder sb = new StringBuilder();
            sb.append(K);
            String[] var5 = keys;
            int var6 = keys.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String key = var5[var7];
                Object value = mParams.get(key);
                if(value != null && value instanceof String) {
                    if(!TextUtils.isEmpty(((String)value).trim())) {
                        sb.append(key + value);
                    }
                } else if(value != null) {
                    sb.append(key + gson.toJson(value));
                }
            }

            return encryptionMD5(sb.toString()).toUpperCase();
        }
    }

    /**
     * 传入的字符窜是空格
     * @param content
     * @return
     */
    private boolean stringIsEmpty(String content) {
        if(TextUtils.isEmpty(content)) {
            return true;
        } else {
            int length = content.length();

            for(int i = 0; i < length; ++i) {
                char c = content.charAt(i);
                if(c != 32) {
                    return false;
                }
            }

            return true;
        }
    }
}
