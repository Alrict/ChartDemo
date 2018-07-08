package com.ihypnus.ihypnuscare.bean;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.bean
 * @author: llw
 * @Description:
 * @date: 2018/6/28 21:54
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HistogramData extends BaseModel {

    private List<Double> scoreValues;
    private List<Double> ahiValues;
    private List<String> dateIndex;
    private List<Double> tpInValues;
    private List<Double> usedTimeSecond;
    private List<Double> tpExValues;

    public List<Double> getScoreValues() {
        return scoreValues;
    }

    public void setScoreValues(List<Double> scoreValues) {
        this.scoreValues = scoreValues;
    }

    public List<Double> getAhiValues() {
        return ahiValues;
    }

    public void setAhiValues(List<Double> ahiValues) {
        this.ahiValues = ahiValues;
    }

    public List<String> getDateIndex() {
        return dateIndex;
    }

    public void setDateIndex(List<String> dateIndex) {
        this.dateIndex = dateIndex;
    }

    public List<Double> getTpInValues() {
        return tpInValues;
    }

    public void setTpInValues(List<Double> tpInValues) {
        this.tpInValues = tpInValues;
    }

    public List<Double> getUsedTimeSecond() {
        return usedTimeSecond;
    }

    public void setUsedTimeSecond(List<Double> usedTimeSecond) {
        this.usedTimeSecond = usedTimeSecond;
    }

    public List<Double> getTpExValues() {
        return tpExValues;
    }

    public void setTpExValues(List<Double> tpExValues) {
        this.tpExValues = tpExValues;
    }
}
