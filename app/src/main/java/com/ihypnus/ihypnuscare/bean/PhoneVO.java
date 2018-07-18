package com.ihypnus.ihypnuscare.bean;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description:
 * @date: 2018/6/21 13:44
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class PhoneVO extends BaseModel{
    private String phone;

    public PhoneVO(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
