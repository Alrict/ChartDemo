package com.ihypnus.ihypnuscare.bean;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description: 报告界面 第一屏数据
 * @date: 2018/6/28 21:39
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class UsageInfos extends BaseModel {

    /**
     * leak : {"totalLeakVolume":0,"averageLeakVolume":"0.0"}
     * score : 100.0
     * pressure : {"tpIn":"6.0","tpEx":"5.0"}
     * usetimes : [{"mode":4,"starttime":"2018-05-31 12:00:00 12:00:00","endTime":"2018-05-31 13:10:01"},{"mode":4,"starttime":"2018-05-31 18:05:41","endTime":"2018-06-01 09:10:30"}]
     * useInfo : {"averageUseTime":"16.25","lessThan4HoursDays":0,"totalDays":1,"noUseDays":0,"totalTimes":"16.25","usedays":1,"useseconds":58490,"moreThan4HoursDays":1,"moreThan4HoursPercent":"100.0","use4days":1}
     * events : {"csa":"0.0","hi":"0.0","usesec":58490,"pb":"0.0","csr":"0.0","ahi":"0.0","ai":"0.0","snore":"0.0"}
     * useParams : {"mode":4,"cureDelay":"0 Min","yesterday":"2018-05-31 12:00:00","startPresure":"5.0 cmH2O","presure2":"6.0 cmH2O","dataVersion":"7C_V1.19","presure1":"5.0 cmH2O","today":"2018-06-01 12:00:00","model":"AU730M"}
     */

    private LeakBean leak;
    private double score;
    private PressureBean pressure;
    private UseInfoBean useInfo;
    private EventsBean events;
    private UseParamsBean useParams;
    private List<UsetimesBean> usetimes;

    public LeakBean getLeak() {
        return leak;
    }

    public void setLeak(LeakBean leak) {
        this.leak = leak;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    public List<UsetimesBean> getUsetimes() {
        return usetimes;
    }

    public void setUsetimes(List<UsetimesBean> usetimes) {
        this.usetimes = usetimes;
    }

    public static class LeakBean extends BaseModel {
        /**
         * totalLeakVolume : 0
         * averageLeakVolume : 0.0
         */

        private int totalLeakVolume;
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
    }

    public static class PressureBean extends BaseModel {
        /**
         * tpIn : 6.0
         * tpEx : 5.0
         */

        private String tpIn;
        private String tpEx;

        public String getTpIn() {
            return tpIn;
        }

        public void setTpIn(String tpIn) {
            this.tpIn = tpIn;
        }

        public String getTpEx() {
            return tpEx;
        }

        public void setTpEx(String tpEx) {
            this.tpEx = tpEx;
        }
    }

    public static class UseInfoBean extends BaseModel {
        /**
         * averageUseTime : 16.25
         * lessThan4HoursDays : 0
         * totalDays : 1
         * noUseDays : 0
         * totalTimes : 16.25
         * usedays : 1
         * useseconds : 58490
         * moreThan4HoursDays : 1
         * moreThan4HoursPercent : 100.0
         * use4days : 1
         */

        private String averageUseTime;
        private int lessThan4HoursDays;
        private int totalDays;
        private int noUseDays;
        private String totalTimes;
        private int usedays;
        private int useseconds;
        private int moreThan4HoursDays;
        private String moreThan4HoursPercent;
        private int use4days;

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

        public int getUsedays() {
            return usedays;
        }

        public void setUsedays(int usedays) {
            this.usedays = usedays;
        }

        public int getUseseconds() {
            return useseconds;
        }

        public void setUseseconds(int useseconds) {
            this.useseconds = useseconds;
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

        public int getUse4days() {
            return use4days;
        }

        public void setUse4days(int use4days) {
            this.use4days = use4days;
        }
    }

    public static class EventsBean extends BaseModel {
        /**
         * csa : 0.0
         * hi : 0.0
         * usesec : 58490
         * pb : 0.0
         * csr : 0.0
         * ahi : 0.0
         * ai : 0.0
         * snore : 0.0
         */

        private String csa;
        private String hi;
        private int usesec;
        private String pb;
        private String csr;
        private String ahi;
        private String ai;
        private String snore;

        public String getCsa() {
            return csa;
        }

        public void setCsa(String csa) {
            this.csa = csa;
        }

        public String getHi() {
            return hi;
        }

        public void setHi(String hi) {
            this.hi = hi;
        }

        public int getUsesec() {
            return usesec;
        }

        public void setUsesec(int usesec) {
            this.usesec = usesec;
        }

        public String getPb() {
            return pb;
        }

        public void setPb(String pb) {
            this.pb = pb;
        }

        public String getCsr() {
            return csr;
        }

        public void setCsr(String csr) {
            this.csr = csr;
        }

        public String getAhi() {
            return ahi;
        }

        public void setAhi(String ahi) {
            this.ahi = ahi;
        }

        public String getAi() {
            return ai;
        }

        public void setAi(String ai) {
            this.ai = ai;
        }

        public String getSnore() {
            return snore;
        }

        public void setSnore(String snore) {
            this.snore = snore;
        }
    }

    public static class UseParamsBean extends BaseModel {
        /**
         * mode : 4
         * cureDelay : 0 Min
         * yesterday : 2018-05-31 12:00:00
         * startPresure : 5.0 cmH2O
         * presure2 : 6.0 cmH2O
         * dataVersion : 7C_V1.19
         * presure1 : 5.0 cmH2O
         * today : 2018-06-01 12:00:00
         * model : AU730M
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
    }

    public static class UsetimesBean extends BaseModel {
        /**
         * mode : 4
         * starttime : 2018-05-31 12:00:00 12:00:00
         * endTime : 2018-05-31 13:10:01
         */

        private int mode;
        private String starttime;
        private String endTime;

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
