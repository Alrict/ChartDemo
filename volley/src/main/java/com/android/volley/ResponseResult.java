package com.android.volley;

import org.json.JSONObject;

/**
 * @Package com.android.volley
 * @author: llw
 * @Description:
 * @date: 2018/6/20 18:46
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ResponseResult {

    private String content;
    private String result;
    private String errorCode;
    private String type;
    private Object responseData;

    public static ResponseResult parseResponseResult(JSONObject jObject) {
        if(jObject == null) {
            return null;
        } else {
            ResponseResult responseResult = new ResponseResult();
            responseResult.content = jObject.optString("content", "");
            responseResult.type = jObject.optString("type", "");
            responseResult.errorCode = jObject.optString("errorCode", "");
            responseResult.result = jObject.optString("result", "");
            return responseResult;
        }
    }


    public Object getResultObject() {
        return this.responseData;
    }

    public void setResultObject(Object responseData) {
        this.responseData = responseData;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
