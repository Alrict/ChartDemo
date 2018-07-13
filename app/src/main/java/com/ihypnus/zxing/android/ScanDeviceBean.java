package com.ihypnus.zxing.android;

import com.ihypnus.ihypnuscare.bean.BaseModel;

/**
 * @Package com.ihypnus.zxing.android
 * @author: llw
 * @Description: V2版本的设备二维码信息
 * @date: 2018/7/13 17:20
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ScanDeviceBean extends BaseModel {
    /**
     * Ver : V1.19-00036
     * ID : 393035393436470b00390029
     * SN : CP70100506S
     * Model : AU730W
     */

    private String Ver;
    private String ID;
    private String SN;
    private String Model;

    public String getVer() {
        return Ver;
    }

    public void setVer(String Ver) {
        this.Ver = Ver;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }
}
