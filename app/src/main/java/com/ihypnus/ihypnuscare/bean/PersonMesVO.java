package com.ihypnus.ihypnuscare.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description:
 * @date: 2018/7/1 22:29
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class PersonMesVO extends BaseModel implements Parcelable{
    /**
     * birthday :
     * address :
     * gender :
     * phone : 18923862913
     * headPath :
     * weight : 56
     * account : 18923862913
     * email :
     * height : 1
     */

    private String birthday;
    private String address;
    private int gender;//性别： 0或者null——未知  1-男  2-女
    private String phone;
    private String headPath;
    private int weight;
    private String account;
    private String email;
    private int height;

    protected PersonMesVO(Parcel in) {
        birthday = in.readString();
        address = in.readString();
        gender = in.readInt();
        phone = in.readString();
        headPath = in.readString();
        weight = in.readInt();
        account = in.readString();
        email = in.readString();
        height = in.readInt();
    }

    public static final Creator<PersonMesVO> CREATOR = new Creator<PersonMesVO>() {
        @Override
        public PersonMesVO createFromParcel(Parcel in) {
            return new PersonMesVO(in);
        }

        @Override
        public PersonMesVO[] newArray(int size) {
            return new PersonMesVO[size];
        }
    };

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(birthday);
        dest.writeString(address);
        dest.writeInt(gender);
        dest.writeString(phone);
        dest.writeString(headPath);
        dest.writeInt(weight);
        dest.writeString(account);
        dest.writeString(email);
        dest.writeInt(height);
    }
}
