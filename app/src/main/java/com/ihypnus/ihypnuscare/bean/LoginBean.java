package com.ihypnus.ihypnuscare.bean;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description:
 * @date: 2018/6/28 11:35
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class LoginBean extends BaseModel {


    /**
     * userInfo : {"birthday":"2001-07-10","address":"","gender":2,"phone":"18923862913","defaultDeviceId":"CP70100520S","headPath":"uri:xxxxxxx","weight":66,"account":"18923862913","email":"","height":180}
     * JSESSIONID : 2022E1F22BCC7DD66CED56D0897AD8B5
     */

    private UserInfoBean userInfo;
    private String JSESSIONID;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public String getJSESSIONID() {
        return JSESSIONID;
    }

    public void setJSESSIONID(String JSESSIONID) {
        this.JSESSIONID = JSESSIONID;
    }

    public static class UserInfoBean extends BaseModel {
        /**
         * birthday : 2001-07-10
         * address :
         * gender : 2
         * phone : 18923862913
         * defaultDeviceId : CP70100520S
         * headPath : uri:xxxxxxx
         * weight : 66
         * account : 18923862913
         * email :
         * height : 180
         */

        private String birthday;
        private String address;
        private int gender;
        private String phone;
        private String defaultDeviceId;
        private String headPath;
        private int weight;
        private String account;
        private String email;
        private int height;

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

        public String getDefaultDeviceId() {
            return defaultDeviceId;
        }

        public void setDefaultDeviceId(String defaultDeviceId) {
            this.defaultDeviceId = defaultDeviceId;
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
    }
}
