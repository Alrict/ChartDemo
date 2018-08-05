package com.ihypnus.ihypnuscare.bean;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description:
 * @date: 2018/8/5 17:10
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class CountryCodeVO extends BaseModel {
    /**
     * content :
     * result : ["86","33"]
     * errorCode : 0000
     * type : success
     */

    private String content;
    private String errorCode;
    private String type;
    private List<String> result;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
