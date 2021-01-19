package com.apex.hrss.constants;

public class ReturnCode {

    private ReturnCode() {
    }

    /**
     * 成功
     */
    public static final Integer SUCCESS = 0;
    /**
     * 失败
     */
    public static final Integer FAILURE = -1;
    /**
     * 参数校验失败
     */
    public static final Integer FIELD_INVILID = -2;
    /**
     * 设备不存在
     */
    public static final Integer DEVICE_NOT_FOUND = -3;
}
