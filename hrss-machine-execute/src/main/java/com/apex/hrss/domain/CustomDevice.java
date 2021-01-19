package com.apex.hrss.domain;

import com.apex.hrss.constant.EnumDeviceModel;

public class CustomDevice {

    /**
     * 厂家设别码
     */
    private String deviceVerify;

    /**
     * 设备号
     */
    private String deviceCode;

    /**
     * 设备型号
     */
    private EnumDeviceModel deviceModel;

    public String getDeviceVerify() {
        return deviceVerify;
    }

    public void setDeviceVerify(String deviceVerify) {
        this.deviceVerify = deviceVerify;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public EnumDeviceModel getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(EnumDeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }
}
