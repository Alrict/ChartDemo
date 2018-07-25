package com.ihypnus.ihypnuscare.bean;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description: 设备固件信息
 * @date: 2018/7/25 14:31
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class DeviceModelVO extends BaseModel {
    private String deviceId;
    private String snId;
    private String model;
    private String productdate;
    private String factoryId;
    private String cusId;
    private String id;
    private String createDate;
    private String modifyDate;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProductdate() {
        return productdate;
    }

    public void setProductdate(String productdate) {
        this.productdate = productdate;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }
}
