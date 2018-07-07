package com.ihypnus.ihypnuscare.bean;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description: 报告界面 第一屏数据
 * @date: 2018/6/28 21:39
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class UsageInfos {
    private LeakBean leak;
    private PressureBean pressure;
    private UseInfoBean useInfo;
    private EventsBean events;
    private UseParamsBean useParams;
    //分数
    private float score;
    //使用时间段信息
    private String usetimes;

    public LeakBean getLeak() {
        return leak;
    }

    public void setLeak(LeakBean leak) {
        this.leak = leak;
    }

    public PressureBean getPressure() {
        return pressure;
    }

    public void setPressure(PressureBean pressure) {
        this.pressure = pressure;
    }

    public UseInfoBean getUseInfo() {
        return useInfo;
    }

    public void setUseInfo(UseInfoBean useInfo) {
        this.useInfo = useInfo;
    }

    public EventsBean getEvents() {
        return events;
    }

    public void setEvents(EventsBean events) {
        this.events = events;
    }

    public UseParamsBean getUseParams() {
        return useParams;
    }

    public void setUseParams(UseParamsBean useParams) {
        this.useParams = useParams;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getUsetimes() {
        return usetimes;
    }

    public void setUsetimes(String usetimes) {
        this.usetimes = usetimes;
    }

    public static class LeakBean {
        /**
         * totalLeakVolume : 0
         * averageLeakVolume : 0
         */

        private int totalLeakVolume;
        //平均漏气
        private String averageLeakVolume;

        public int getTotalLeakVolume() {
            return totalLeakVolume;
        }

        public void setTotalLeakVolume(int totalLeakVolume) {
            this.totalLeakVolume = totalLeakVolume;
        }

        public String getAverageLeakVolume() {
            return averageLeakVolume;
        }

        public void setAverageLeakVolume(String averageLeakVolume) {
            this.averageLeakVolume = averageLeakVolume;
        }

        @Override
        public String toString() {
            return "LeakBean{" +
                    "totalLeakVolume=" + totalLeakVolume +
                    ", averageLeakVolume='" + averageLeakVolume + '\'' +
                    '}';
        }
    }

    public static class PressureBean {
        /**
         * ninetyPercentPresure2 : 0.0
         * ninetyPercentPresure1 : 0.0
         */

        private double ninetyPercentPresure2;//无效
        private double ninetyPercentPresure1;//无效
        private double tpEx;//90%呼气压力
        private double tpIn;//90%吸气压力

        public double getNinetyPercentPresure2() {
            return ninetyPercentPresure2;
        }

        public void setNinetyPercentPresure2(double ninetyPercentPresure2) {
            this.ninetyPercentPresure2 = ninetyPercentPresure2;
        }

        public double getNinetyPercentPresure1() {
            return ninetyPercentPresure1;
        }

        public void setNinetyPercentPresure1(double ninetyPercentPresure1) {
            this.ninetyPercentPresure1 = ninetyPercentPresure1;
        }

        public double getTpEx() {
            return tpEx;
        }

        public void setTpEx(double tpEx) {
            this.tpEx = tpEx;
        }

        public double getTpIn() {
            return tpIn;
        }

        public void setTpIn(double tpIn) {
            this.tpIn = tpIn;
        }
    }

    public static class UseInfoBean {
        /**
         * averageUseTime : 0
         * lessThan4HoursDays : 0
         * totalDays : 1
         * noUseDays : 1
         * totalTimes : 0
         * moreThan4HoursDays : 0
         * moreThan4HoursPercent : 0
         */

        private String averageUseTime;//使用时长 单位小时
        private int lessThan4HoursDays;
        private int totalDays;
        private int noUseDays;
        private int use4days;
        private int usedays;
        private String totalTimes;//单位小时
        private String useseconds;//单位小时
        private int moreThan4HoursDays;
        private String moreThan4HoursPercent;

        public String getAverageUseTime() {
            return averageUseTime;
        }

        public void setAverageUseTime(String averageUseTime) {
            this.averageUseTime = averageUseTime;
        }

        public int getLessThan4HoursDays() {
            return lessThan4HoursDays;
        }

        public void setLessThan4HoursDays(int lessThan4HoursDays) {
            this.lessThan4HoursDays = lessThan4HoursDays;
        }

        public int getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(int totalDays) {
            this.totalDays = totalDays;
        }

        public int getNoUseDays() {
            return noUseDays;
        }

        public void setNoUseDays(int noUseDays) {
            this.noUseDays = noUseDays;
        }

        public String getTotalTimes() {
            return totalTimes;
        }

        public void setTotalTimes(String totalTimes) {
            this.totalTimes = totalTimes;
        }

        public int getMoreThan4HoursDays() {
            return moreThan4HoursDays;
        }

        public void setMoreThan4HoursDays(int moreThan4HoursDays) {
            this.moreThan4HoursDays = moreThan4HoursDays;
        }

        public String getMoreThan4HoursPercent() {
            return moreThan4HoursPercent;
        }

        public void setMoreThan4HoursPercent(String moreThan4HoursPercent) {
            this.moreThan4HoursPercent = moreThan4HoursPercent;
        }
    }

    public static class EventsBean {
        /**
         * csa : 0
         * hi : 0
         * usesec :
         * pb : 0
         * csr : 0
         * ahi : 0
         * ai : 0
         * snore : 0
         */

        private int csa;
        private int hi;
        private String usesec;
        private int pb;
        private int csr;
        //AHI
        private int ahi;
        private int ai;
        private int snore;

        public int getCsa() {
            return csa;
        }

        public void setCsa(int csa) {
            this.csa = csa;
        }

        public int getHi() {
            return hi;
        }

        public void setHi(int hi) {
            this.hi = hi;
        }

        public String getUsesec() {
            return usesec;
        }

        public void setUsesec(String usesec) {
            this.usesec = usesec;
        }

        public int getPb() {
            return pb;
        }

        public void setPb(int pb) {
            this.pb = pb;
        }

        public int getCsr() {
            return csr;
        }

        public void setCsr(int csr) {
            this.csr = csr;
        }

        public int getAhi() {
            return ahi;
        }

        public void setAhi(int ahi) {
            this.ahi = ahi;
        }

        public int getAi() {
            return ai;
        }

        public void setAi(int ai) {
            this.ai = ai;
        }

        public int getSnore() {
            return snore;
        }

        public void setSnore(int snore) {
            this.snore = snore;
        }

        @Override
        public String toString() {
            return "EventsBean{" +
                    "csa=" + csa +
                    ", hi=" + hi +
                    ", usesec='" + usesec + '\'' +
                    ", pb=" + pb +
                    ", csr=" + csr +
                    ", ahi=" + ahi +
                    ", ai=" + ai +
                    ", snore=" + snore +
                    '}';
        }
    }

    public static class UseParamsBean {
        /**
         * mode : 100
         * cureDelay : comflict
         * yesterday : 2018-06-2712:00:00
         * startPresure : comflict
         * presure2 : comflict
         * dataVersion : unknow
         * presure1 : comflict
         * today : 2018-06-2812:00:00
         * model : unknow
         * modeInfo : Modes:
         */

        /**
         * 设备模式 0 --”CPAP”，1--”APAP”, 2--"BPAP-S", 3--"AutoBPAP-S", 4--"BPAP-T",
         // 5--"BPAP-ST"   100 -- “No Data”  200--”多种模式”需要读取参数 “modeInfo”，
         // 根据邝勇最新需求，当放回值为200时，默认显示最长使用时间段的模式，也就是从usetimes中最长的那条读取”mode”显示出来
         */
        private int mode;
        private String cureDelay;
        private String yesterday;
        private String startPresure;
        private String presure2;
        private String dataVersion;
        private String presure1;
        private String today;
        private String model;
        private String modeInfo;

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getCureDelay() {
            return cureDelay;
        }

        public void setCureDelay(String cureDelay) {
            this.cureDelay = cureDelay;
        }

        public String getYesterday() {
            return yesterday;
        }

        public void setYesterday(String yesterday) {
            this.yesterday = yesterday;
        }

        public String getStartPresure() {
            return startPresure;
        }

        public void setStartPresure(String startPresure) {
            this.startPresure = startPresure;
        }

        public String getPresure2() {
            return presure2;
        }

        public void setPresure2(String presure2) {
            this.presure2 = presure2;
        }

        public String getDataVersion() {
            return dataVersion;
        }

        public void setDataVersion(String dataVersion) {
            this.dataVersion = dataVersion;
        }

        public String getPresure1() {
            return presure1;
        }

        public void setPresure1(String presure1) {
            this.presure1 = presure1;
        }

        public String getToday() {
            return today;
        }

        public void setToday(String today) {
            this.today = today;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getModeInfo() {
            return modeInfo;
        }

        public void setModeInfo(String modeInfo) {
            this.modeInfo = modeInfo;
        }
    }
}
