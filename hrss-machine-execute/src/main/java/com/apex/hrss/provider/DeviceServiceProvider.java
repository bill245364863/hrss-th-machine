package com.apex.hrss.provider;


import com.apex.hrss.domain.*;

import java.util.List;
import java.util.Map;

public interface DeviceServiceProvider {
    /**
     * 获取设备信息
     *
     * @param deviceCode2
     * @return
     */
    CustomDevice loadDevice(String deviceVerify, String deviceCode);

  /*  *//**
     * 获取工人信息
     *
     * @param idCardNo
     * @return
     *//*
    CustomWorker loadWorkerByIdCardNo(String idCardNo);*/

    /**
     * 获取项目信息
     *
     * @return
     */
    CustomProject loadProject(CustomDevice customDevice);

    /**
     * 处理采集信息
     *
     * @param feature
     * @return
     */
    boolean processFeature(CustomDevice customDevice, CustomFeature feature);

   /* *//**
     * @return
     *//*
    List<CustomWorker> loadWorkers(CustomDevice customDevice);*/

    /**
     * 处理考勤数据
     *
     * @param attendanceList
     * @return
     */
    Map<String, Object> processAttendance(CustomDevice customDevice, List<CustomAttendance> attendanceList) ;

    /**
     * 全部人员(腾晖拓展专用)
     *
     * @return
     */
    List<CustomWorker> thLoadAllWorkers(CustomDevice customDevice);

   /* *//**
     * 在职人员(腾晖拓展专用)
     *
     * @return
     *//*
    List<CustomWorker> thLoadInWorkWorkers(CustomDevice customDevice);*/

    /**
     * 获取人员特征
     *
     * @param idCardList
     * @return
     */
    List<CustomWorker> thLoadWorkerFeature(CustomDevice customDevice, List<String> idCardList) ;

    /**
     * 刷设备
     */
    void refresh(CustomDevice customDevice);
}
