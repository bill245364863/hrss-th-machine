package com.apex.hrss.constant;

public enum EnumDeviceModel {
    /**
     * 腾晖拓展1代
     */
    TENFINE_ONE("101"),
    /**
     * 腾晖拓展2代
     */
    TENFINE_TWO("102"),
    /**
     * 海清人脸
     */
    HQ("103"),
    /**
     * 虹膜
     */
    HM("104");

    private String code;

    private EnumDeviceModel(String code) {
        this.code = code;
    }

    public static EnumDeviceModel formCode(String code) {
        for (EnumDeviceModel v : values()) {
            if (v.code.equals(code)) {
                return v;
            }
        }
        return null;
    }
}
