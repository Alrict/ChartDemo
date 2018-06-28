package com.ihypnus.ihypnuscare.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description: 设备列表bean
 * @date: 2018/6/28 15:02
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class DeviceListVO implements Parcelable {
    /**
     * content : [{"i_id":80,"device_id":"CP70100516S","userPhone":"18122017377","productDate":"","cus_id":706066129239408640,"factory_id":"","d_modify_date":"2018-06-27 17:50:25","phone ":"","sn_id ":"393035393436470 b0024002d ","name ":"","model ":"","d_create_date ":""}]
     * total  : 1
     * pageNo  : 1
     * pageSize  : 20
     * totalPages  : 1
     */

    private int total;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private List<ContentBean> content;

    protected DeviceListVO(Parcel in) {
        total = in.readInt();
        pageNo = in.readInt();
        pageSize = in.readInt();
        totalPages = in.readInt();
        content = in.createTypedArrayList(ContentBean.CREATOR);
    }

    public static final Creator<DeviceListVO> CREATOR = new Creator<DeviceListVO>() {
        @Override
        public DeviceListVO createFromParcel(Parcel in) {
            return new DeviceListVO(in);
        }

        @Override
        public DeviceListVO[] newArray(int size) {
            return new DeviceListVO[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeInt(pageNo);
        dest.writeInt(pageSize);
        dest.writeInt(totalPages);
        dest.writeTypedList(content);
    }

    public static class ContentBean implements Parcelable {
        /**
         * i_id : 80
         * device_id : CP70100516S
         * userPhone : 18122017377
         * productDate :
         * cus_id : 706066129239408640
         * factory_id :
         * d_modify_date : 2018-06-27 17:50:25
         * phone  :
         * sn_id  : 393035393436470 b0024002d
         * name  :
         * model  :
         * d_create_date  :
         */

        private int i_id;
        private String device_id;
        private String userPhone;
        private String productDate;
        private long cus_id;
        private String factory_id;
        private String d_modify_date;
        private String phone;
        private String sn_id;
        private String name;
        private String model;
        private String d_create_date;
        private int isChecked;

        protected ContentBean(Parcel in) {
            i_id = in.readInt();
            device_id = in.readString();
            userPhone = in.readString();
            productDate = in.readString();
            cus_id = in.readLong();
            factory_id = in.readString();
            d_modify_date = in.readString();
            phone = in.readString();
            sn_id = in.readString();
            name = in.readString();
            model = in.readString();
            d_create_date = in.readString();
            isChecked = in.readInt();
        }

        public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
            @Override
            public ContentBean createFromParcel(Parcel in) {
                return new ContentBean(in);
            }

            @Override
            public ContentBean[] newArray(int size) {
                return new ContentBean[size];
            }
        };

        public int getI_id() {
            return i_id;
        }

        public void setI_id(int i_id) {
            this.i_id = i_id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getProductDate() {
            return productDate;
        }

        public void setProductDate(String productDate) {
            this.productDate = productDate;
        }

        public long getCus_id() {
            return cus_id;
        }

        public void setCus_id(long cus_id) {
            this.cus_id = cus_id;
        }

        public String getFactory_id() {
            return factory_id;
        }

        public void setFactory_id(String factory_id) {
            this.factory_id = factory_id;
        }

        public String getD_modify_date() {
            return d_modify_date;
        }

        public void setD_modify_date(String d_modify_date) {
            this.d_modify_date = d_modify_date;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSn_id() {
            return sn_id;
        }

        public void setSn_id(String sn_id) {
            this.sn_id = sn_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getD_create_date() {
            return d_create_date;
        }

        public void setD_create_date(String d_create_date) {
            this.d_create_date = d_create_date;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "i_id=" + i_id +
                    ", device_id='" + device_id + '\'' +
                    ", userPhone='" + userPhone + '\'' +
                    ", productDate='" + productDate + '\'' +
                    ", cus_id=" + cus_id +
                    ", factory_id='" + factory_id + '\'' +
                    ", d_modify_date='" + d_modify_date + '\'' +
                    ", phone='" + phone + '\'' +
                    ", sn_id='" + sn_id + '\'' +
                    ", name='" + name + '\'' +
                    ", model='" + model + '\'' +
                    ", d_create_date='" + d_create_date + '\'' +
                    '}';
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
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(i_id);
            dest.writeString(device_id);
            dest.writeString(userPhone);
            dest.writeString(productDate);
            dest.writeLong(cus_id);
            dest.writeString(factory_id);
            dest.writeString(d_modify_date);
            dest.writeString(phone);
            dest.writeString(sn_id);
            dest.writeString(name);
            dest.writeString(model);
            dest.writeString(d_create_date);
            dest.writeInt(isChecked);
        }
    }
}
