package com.ihypnus.ihypnuscare.eventbusfactory;

/**
 * @Package com.ihypnus.ihypnuscare
 * @author: llw
 * @Description: EventBus 处理类
 * @date: 2018/6/28 16:56
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class BaseFactory {
    /**
     * 关闭全局事件
     */
    public static class CloseAllEvent {
    }

    /**
     * 刷新数据
     */
    public static class RefreshAllEvent {

    }

    /**
     * 刷新报告页面数据
     */
    public static class RefreshReportInfoEvent{
        String deviceId;

        public RefreshReportInfoEvent(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }
}
