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
     * 关闭指定的页面
     */
    public static class CloseActivityEvent {
        private Class<?> cls;

        public CloseActivityEvent(Class<?> cls) {
            this.cls = cls;
        }

        public Class<?> getCls() {
            return cls;
        }

        public void setCls(Class<?> cls) {
            this.cls = cls;
        }
    }

    /**
     * 刷新数据
     */
    public static class RefreshAllEvent {

    }

    /**
     * 刷新报告页面数据
     */
    public static class RefreshReportInfoEvent {
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

    public static class RefreshDeviceListInfoEvent {
        public RefreshDeviceListInfoEvent() {

        }
    }

    public static class CheckFragment {
        private int type;

        public CheckFragment(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    /**
     * 更新语言
     */
    public static class UpdateLanguageEvent {
    }
}
