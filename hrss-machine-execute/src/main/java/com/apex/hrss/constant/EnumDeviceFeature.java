package com.apex.hrss.constant;

public enum EnumDeviceFeature {

    /**
     * 人脸
     */
    FACE("001"),
    /**
     * 虹膜
     */
    IRIS("002");

    private String code;

    private EnumDeviceFeature(String code) {
        this.code = code;
    }

    public static EnumDeviceFeature fromCode(String code) {
        for (EnumDeviceFeature e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
