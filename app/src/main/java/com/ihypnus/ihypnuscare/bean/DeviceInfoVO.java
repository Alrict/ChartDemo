package com.ihypnus.ihypnuscare.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description: 设备信息
 * @date: 2018/5/29 15:34
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class DeviceInfoVO implements Parcelable{
    private String deviceModel;
    private String deviceNo;
    private int isChecked;

    public DeviceInfoVO() {
    }

    public DeviceInfoVO(String deviceModel, String deviceNo) {
        this.deviceModel = deviceModel;
        this.deviceNo = deviceNo;
    }

    protected DeviceInfoVO(Parcel in) {
        deviceModel = in.readString();
        deviceNo = in.readString();
        isChecked = in.readInt();
    }

    public static final Creator<DeviceInfoVO> CREATOR = new Creator<DeviceInfoVO>() {
        @Override
        public DeviceInfoVO createFromParcel(Parcel in) {
            return new DeviceInfoVO(in);
        }

        @Override
        public DeviceInfoVO[] newArray(int size) {
            return new DeviceInfoVO[size];
        }
    };

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

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(deviceModel);
        parcel.writeString(deviceNo);
        parcel.writeInt(isChecked);
    }
}
