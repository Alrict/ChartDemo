package com.ihypnus.ihypnuscare.bean;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description: 设备信息
 * @date: 2018/5/29 15:34
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class DeviceInfoVO {
    private String deviceModel;
    private String deviceNo;

    public DeviceInfoVO() {
    }

    public DeviceInfoVO(String deviceModel, String deviceNo) {
        this.deviceModel = deviceModel;
        this.deviceNo = deviceNo;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
}
