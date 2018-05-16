package com.ihypnus.ihypnuscare.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class CarSanitationListBean  {

    private int recordCount;
    private int totalCount;
    private int pageSize;

    private List<DatasBean> datas;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Serializable {
        private String ManageNo;
        private String CarNo;
        private String UploadDT;
        private String Col_006;
        private String Col_018;
        private String CZDepart;
        private String Workshop;
        private String Col_060;
        private String UploadMan;
        private String SH_user;
        private String SH_DT;
        private List<String> ImageList;

        public String getManageNo() {
            return ManageNo;
        }

        public void setManageNo(String ManageNo) {
            this.ManageNo = ManageNo;
        }

        public String getCarNo() {
            return CarNo;
        }

        public void setCarNo(String CarNo) {
            this.CarNo = CarNo;
        }

        public String getUploadDT() {
            return UploadDT;
        }

        public void setUploadDT(String UploadDT) {
            this.UploadDT = UploadDT;
        }

        public String getCol_006() {
            return Col_006;
        }

        public void setCol_006(String Col_006) {
            this.Col_006 = Col_006;
        }

        public String getCol_018() {
            return Col_018;
        }

        public void setCol_018(String Col_018) {
            this.Col_018 = Col_018;
        }

        public String getCZDepart() {
            return CZDepart;
        }

        public void setCZDepart(String CZDepart) {
            this.CZDepart = CZDepart;
        }

        public String getWorkshop() {
            return Workshop;
        }

        public void setWorkshop(String Workshop) {
            this.Workshop = Workshop;
        }

        public String getCol_060() {
            return Col_060;
        }

        public void setCol_060(String Col_060) {
            this.Col_060 = Col_060;
        }

        public String getUploadMan() {
            return UploadMan;
        }

        public void setUploadMan(String UploadMan) {
            this.UploadMan = UploadMan;
        }

        public String getSH_user() {
            return SH_user;
        }

        public void setSH_user(String SH_user) {
            this.SH_user = SH_user;
        }

        public String getSH_DT() {
            return SH_DT;
        }

        public void setSH_DT(String SH_DT) {
            this.SH_DT = SH_DT;
        }

        public List<String> getImageList() {
            return ImageList;
        }

        public void setImageList(List<String> ImageList) {
            this.ImageList = ImageList;
        }
    }
}
