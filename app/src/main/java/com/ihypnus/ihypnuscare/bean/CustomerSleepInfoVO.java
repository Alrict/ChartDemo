package com.ihypnus.ihypnuscare.bean;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description:
 * @date: 2018/5/18 16:11
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class CustomerSleepInfoVO {

    private ApneaBean apnea;
    private CsaBean csa;
    private HypopneaBean hypopnea;
    private PbBean pb;
    private CsrBean csr;

    public ApneaBean getApnea() {
        return apnea;
    }

    public void setApnea(ApneaBean apnea) {
        this.apnea = apnea;
    }

    public CsaBean getCsa() {
        return csa;
    }

    public void setCsa(CsaBean csa) {
        this.csa = csa;
    }

    public HypopneaBean getHypopnea() {
        return hypopnea;
    }

    public void setHypopnea(HypopneaBean hypopnea) {
        this.hypopnea = hypopnea;
    }

    public PbBean getPb() {
        return pb;
    }

    public void setPb(PbBean pb) {
        this.pb = pb;
    }

    public CsrBean getCsr() {
        return csr;
    }

    public void setCsr(CsrBean csr) {
        this.csr = csr;
    }

    @Override
    public String toString() {
        return "CustomerSleepInfoVO{" +
                "apnea=" + apnea +
                ", csa=" + csa +
                ", hypopnea=" + hypopnea +
                ", pb=" + pb +
                ", csr=" + csr +
                '}';
    }

    public static class ApneaBean {
        private List<String> eventList;
        private List<String> dateList;

        public List<String> getEventList() {
            return eventList;
        }

        public void setEventList(List<String> eventList) {
            this.eventList = eventList;
        }

        public List<String> getDateList() {
            return dateList;
        }

        public void setDateList(List<String> dateList) {
            this.dateList = dateList;
        }
    }

    public static class CsaBean {
        private List<String> eventList;
        private List<String> dateList;

        public List<String> getEventList() {
            return eventList;
        }

        public void setEventList(List<String> eventList) {
            this.eventList = eventList;
        }

        public List<String> getDateList() {
            return dateList;
        }

        public void setDateList(List<String> dateList) {
            this.dateList = dateList;
        }
    }

    public static class HypopneaBean {
        private List<String> eventList;
        private List<String> dateList;

        public List<String> getEventList() {
            return eventList;
        }

        public void setEventList(List<String> eventList) {
            this.eventList = eventList;
        }

        public List<String> getDateList() {
            return dateList;
        }

        public void setDateList(List<String> dateList) {
            this.dateList = dateList;
        }
    }

    public static class PbBean {
        private List<String> eventList;
        private List<String> dateList;

        public List<String> getEventList() {
            return eventList;
        }

        public void setEventList(List<String> eventList) {
            this.eventList = eventList;
        }

        public List<String> getDateList() {
            return dateList;
        }

        public void setDateList(List<String> dateList) {
            this.dateList = dateList;
        }
    }

    public static class CsrBean {
        private List<String> eventList;
        private List<String> dateList;

        public List<String> getEventList() {
            return eventList;
        }

        public void setEventList(List<String> eventList) {
            this.eventList = eventList;
        }

        public List<String> getDateList() {
            return dateList;
        }

        public void setDateList(List<String> dateList) {
            this.dateList = dateList;
        }
    }
}
