package com.apex.hrssmachineexecute.domain;

import java.util.Date;

public class CustomAttendance {
    /**
     * 工人标识(seq_no)
     */
    private Integer workerId;
    /**
     * 考勤时间
     */
    private Date attendDate;

    /**
     * 考勤方向
     */
    private Integer attendDir;

    /**
     * 考勤照片
     */
    private byte[] attendImage;

    /**
     * 经度
     */
    private String lng;
    /**
     * 维度
     */
    private String lat;
    /**
     * 体温
     */
    private String temperature;
    /**
     * 是否高温（1=高温，0=正常）
     */
    private int temperatureFlag;

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Date getAttendDate() {
        return attendDate;
    }

    public void setAttendDate(Date attendDate) {
        this.attendDate = attendDate;
    }

    public Integer getAttendDir() {
        return attendDir;
    }

    public void setAttendDir(Integer attendDir) {
        this.attendDir = attendDir;
    }

    public byte[] getAttendImage() {
        return attendImage;
    }

    public void setAttendImage(byte[] attendImage) {
        this.attendImage = attendImage;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getTemperatureFlag() {
        return temperatureFlag;
    }

    public void setTemperatureFlag(int temperatureFlag) {
        this.temperatureFlag = temperatureFlag;
    }
}
