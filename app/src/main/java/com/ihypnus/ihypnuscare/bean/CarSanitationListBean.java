package com.ihypnus.ihypnuscare.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class CarSanitationListBean  {


    /**
     * errCode : 0000
     * retStatus : 1
     * errMsg : 查询成功
     * result : {"recordCount":5799,"totalCount":5799,"pageSize":10,"datas":[{"ManageNo":"180500938","CarNo":"大货粤BAF368","UploadDT":"2018-05-17 16:51","Col_006":"陈锦华","Col_018":"韦远斌","CZDepart":"龙岗车管组","Workshop":"龙岗车管部","Col_060":"华南","UploadMan":"龙斌","SH_user":"","SH_DT":"","ImageList":["22fc09f4a76e4346b7b589d4f53fde33"]},{"ManageNo":"180500916","CarNo":"面包晥HY008U","UploadDT":"2018-05-17 16:27","Col_006":"何晓运","Col_018":"余彪","CZDepart":"宁波车管组","Workshop":"宁波车管部","Col_060":"华东","UploadMan":"吴友","SH_user":"","SH_DT":"","ImageList":["313b89399bfb452eafeb48324926b7c6","e8ef37a8fc3c41aea950c7b7e7635d5d","405cba3578ad4ae59abc5d20c96e2a36"]},{"ManageNo":"180500908","CarNo":"中货粤B8D23K","UploadDT":"2018-05-17 15:59","Col_006":"周东亮沙井","Col_018":"陈起东","CZDepart":"宝安车管组","Workshop":"宝安车管部","Col_060":"华南","UploadMan":"吴熙文","SH_user":"","SH_DT":"","ImageList":["2e6705367c074ab2a44d7970a0eec9ba","ead6338f97a041ffa831379f3109ee6f","780de1d99db94c97ad480cc0ba1096ef"]},{"ManageNo":"180500906","CarNo":"中货津KYE876","UploadDT":"2018-05-17 14:50","Col_006":"李海林天津","Col_018":"殷磊","CZDepart":"武清车管组","Workshop":"天津车管部","Col_060":"京津冀","UploadMan":"田海波","SH_user":"","SH_DT":"","ImageList":["043e0355b7504855a9969ffb1eb762d0"]},{"ManageNo":"180500879","CarNo":"大货川AS2770","UploadDT":"2018-05-17 09:54","Col_006":"何夕洪成都","Col_018":"周海明","CZDepart":"成都车管组","Workshop":"成都车管部","Col_060":"华北","UploadMan":"杨星花东","SH_user":"","SH_DT":"","ImageList":["72bbba76d5954417be8de6f7cfe5bc45"]},{"ManageNo":"180500877","CarNo":"大货浙A7L120","UploadDT":"2018-05-17 09:36","Col_006":"马正伟","Col_018":"张俊杨","CZDepart":"杭州车管组","Workshop":"杭州车管部","Col_060":"华东","UploadMan":"邹言","SH_user":"","SH_DT":"","ImageList":["4c7e66bf61bf44b69e17cdb17a07be1a"]},{"ManageNo":"180500875","CarNo":"面包津KYE787","UploadDT":"2018-05-17 09:26","Col_006":"王洪伟","Col_018":"孙治营","CZDepart":"天津车管组","Workshop":"天津车管部","Col_060":"京津冀","UploadMan":"崔婷婷","SH_user":"","SH_DT":"","ImageList":["cc02d071ac024e25bc7639d7d3552993","76f0fb4703fe47b789f46b180e25f87e","e6a018a91baf4450b08d5957e1bdc7c9"]},{"ManageNo":"180500839","CarNo":"中货苏B3UB67","UploadDT":"2018-05-16 16:20","Col_006":"姚宏强","Col_018":"徐海军","CZDepart":"华新一级一栋干线组","Workshop":"华新一级中转场","Col_060":"华东","UploadMan":"唐陈港","SH_user":"","SH_DT":"","ImageList":["ed9f0227fba24b7bbcf92007153cb058"]},{"ManageNo":"180500812","CarNo":"中货苏E9L50B","UploadDT":"2018-05-16 15:38","Col_006":"张春苏州","Col_018":"宋昔春","CZDepart":"苏州车管组","Workshop":"苏州车管部","Col_060":"华东","UploadMan":"金棒棒","SH_user":"","SH_DT":"","ImageList":["747d5ad02d1e41859bfe0020f8165e7e","d83844fce5df41928da659ffecd8c1b0","59eee75732b94b81be60786ad3a81d61","8cb398e9ee1a46e5ba102c4d93d3bc61"]},{"ManageNo":"180500803","CarNo":"大货鄂AMA341","UploadDT":"2018-05-16 14:47","Col_006":"王方涛","Col_018":"盛为利","CZDepart":"武汉车管组","Workshop":"武汉车管部","Col_060":"华北","UploadMan":"华北操作培训","SH_user":"","SH_DT":"","ImageList":["67e682a439764567be6ddc92f652248b","86e577555dc24ac1b999209a6900987b","91c75541ba9945e787eb5300bad57ddf","40458d8753ab4b4bae87357a5d23d503"]}]}
     */

    private String errCode;
    private String retStatus;
    private String errMsg;
    private ResultBean result;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getRetStatus() {
        return retStatus;
    }

    public void setRetStatus(String retStatus) {
        this.retStatus = retStatus;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * recordCount : 5799
         * totalCount : 5799
         * pageSize : 10
         * datas : [{"ManageNo":"180500938","CarNo":"大货粤BAF368","UploadDT":"2018-05-17 16:51","Col_006":"陈锦华","Col_018":"韦远斌","CZDepart":"龙岗车管组","Workshop":"龙岗车管部","Col_060":"华南","UploadMan":"龙斌","SH_user":"","SH_DT":"","ImageList":["22fc09f4a76e4346b7b589d4f53fde33"]},{"ManageNo":"180500916","CarNo":"面包晥HY008U","UploadDT":"2018-05-17 16:27","Col_006":"何晓运","Col_018":"余彪","CZDepart":"宁波车管组","Workshop":"宁波车管部","Col_060":"华东","UploadMan":"吴友","SH_user":"","SH_DT":"","ImageList":["313b89399bfb452eafeb48324926b7c6","e8ef37a8fc3c41aea950c7b7e7635d5d","405cba3578ad4ae59abc5d20c96e2a36"]},{"ManageNo":"180500908","CarNo":"中货粤B8D23K","UploadDT":"2018-05-17 15:59","Col_006":"周东亮沙井","Col_018":"陈起东","CZDepart":"宝安车管组","Workshop":"宝安车管部","Col_060":"华南","UploadMan":"吴熙文","SH_user":"","SH_DT":"","ImageList":["2e6705367c074ab2a44d7970a0eec9ba","ead6338f97a041ffa831379f3109ee6f","780de1d99db94c97ad480cc0ba1096ef"]},{"ManageNo":"180500906","CarNo":"中货津KYE876","UploadDT":"2018-05-17 14:50","Col_006":"李海林天津","Col_018":"殷磊","CZDepart":"武清车管组","Workshop":"天津车管部","Col_060":"京津冀","UploadMan":"田海波","SH_user":"","SH_DT":"","ImageList":["043e0355b7504855a9969ffb1eb762d0"]},{"ManageNo":"180500879","CarNo":"大货川AS2770","UploadDT":"2018-05-17 09:54","Col_006":"何夕洪成都","Col_018":"周海明","CZDepart":"成都车管组","Workshop":"成都车管部","Col_060":"华北","UploadMan":"杨星花东","SH_user":"","SH_DT":"","ImageList":["72bbba76d5954417be8de6f7cfe5bc45"]},{"ManageNo":"180500877","CarNo":"大货浙A7L120","UploadDT":"2018-05-17 09:36","Col_006":"马正伟","Col_018":"张俊杨","CZDepart":"杭州车管组","Workshop":"杭州车管部","Col_060":"华东","UploadMan":"邹言","SH_user":"","SH_DT":"","ImageList":["4c7e66bf61bf44b69e17cdb17a07be1a"]},{"ManageNo":"180500875","CarNo":"面包津KYE787","UploadDT":"2018-05-17 09:26","Col_006":"王洪伟","Col_018":"孙治营","CZDepart":"天津车管组","Workshop":"天津车管部","Col_060":"京津冀","UploadMan":"崔婷婷","SH_user":"","SH_DT":"","ImageList":["cc02d071ac024e25bc7639d7d3552993","76f0fb4703fe47b789f46b180e25f87e","e6a018a91baf4450b08d5957e1bdc7c9"]},{"ManageNo":"180500839","CarNo":"中货苏B3UB67","UploadDT":"2018-05-16 16:20","Col_006":"姚宏强","Col_018":"徐海军","CZDepart":"华新一级一栋干线组","Workshop":"华新一级中转场","Col_060":"华东","UploadMan":"唐陈港","SH_user":"","SH_DT":"","ImageList":["ed9f0227fba24b7bbcf92007153cb058"]},{"ManageNo":"180500812","CarNo":"中货苏E9L50B","UploadDT":"2018-05-16 15:38","Col_006":"张春苏州","Col_018":"宋昔春","CZDepart":"苏州车管组","Workshop":"苏州车管部","Col_060":"华东","UploadMan":"金棒棒","SH_user":"","SH_DT":"","ImageList":["747d5ad02d1e41859bfe0020f8165e7e","d83844fce5df41928da659ffecd8c1b0","59eee75732b94b81be60786ad3a81d61","8cb398e9ee1a46e5ba102c4d93d3bc61"]},{"ManageNo":"180500803","CarNo":"大货鄂AMA341","UploadDT":"2018-05-16 14:47","Col_006":"王方涛","Col_018":"盛为利","CZDepart":"武汉车管组","Workshop":"武汉车管部","Col_060":"华北","UploadMan":"华北操作培训","SH_user":"","SH_DT":"","ImageList":["67e682a439764567be6ddc92f652248b","86e577555dc24ac1b999209a6900987b","91c75541ba9945e787eb5300bad57ddf","40458d8753ab4b4bae87357a5d23d503"]}]
         */

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

        public static class DatasBean {
            /**
             * ManageNo : 180500938
             * CarNo : 大货粤BAF368
             * UploadDT : 2018-05-17 16:51
             * Col_006 : 陈锦华
             * Col_018 : 韦远斌
             * CZDepart : 龙岗车管组
             * Workshop : 龙岗车管部
             * Col_060 : 华南
             * UploadMan : 龙斌
             * SH_user :
             * SH_DT :
             * ImageList : ["22fc09f4a76e4346b7b589d4f53fde33"]
             */

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

            @Override
            public String toString() {
                return "DatasBean{" +
                        "ManageNo='" + ManageNo + '\'' +
                        ", CarNo='" + CarNo + '\'' +
                        ", UploadDT='" + UploadDT + '\'' +
                        ", Col_006='" + Col_006 + '\'' +
                        ", Col_018='" + Col_018 + '\'' +
                        ", CZDepart='" + CZDepart + '\'' +
                        ", Workshop='" + Workshop + '\'' +
                        ", Col_060='" + Col_060 + '\'' +
                        ", UploadMan='" + UploadMan + '\'' +
                        ", SH_user='" + SH_user + '\'' +
                        ", SH_DT='" + SH_DT + '\'' +
                        ", ImageList=" + ImageList +
                        '}';
            }
        }
    }
}
