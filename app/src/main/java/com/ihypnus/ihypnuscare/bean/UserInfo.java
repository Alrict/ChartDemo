package com.ihypnus.ihypnuscare.bean;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description:
 * @date: 2018/6/20 11:29
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class UserInfo extends BaseModel {
    private String phone;
    private String password;

    public UserInfo(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
