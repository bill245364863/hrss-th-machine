package com.apex.hrssmachineexecute.constant;

import com.apex.hrssmachineexecute.command.*;

/**
 * 设备通讯协议命令
 *
 * @author wangxl
 */
public enum EnumCommand {
    /**
     * 同步服务器时间
     */
    SYNC_TIME(65532, Command65532.class, "同步时间"),

    /**
     * 心跳确认，考勤设备发送
     */
    HEARTBEAT(65535, Command65535.class, "心跳确认"),

    /**
     * 设备登录，考勤和采集设备都会发送
     */
    LOGIN(823, Command823.class, "设备登录"),

    /**
     * 获取项目人员名单
     */
    GET_WORKERS(824, Command824.class, "获取项目人员"),

    /**
     * 上传考勤数据，考勤设备发送
     */
    UPLOAD_ATTENDANCE(825, Command825.class, "上传考勤数据"),

    /**
     * 判断设备是否需要更新，考勤设备发送
     */
    IS_UPDATED(826, Command826.class, "判断设备是否需要更新"),

    /**
     * 上传考勤数据
     */
    UPLOAD_LED(828, Command828.class, "上传考勤数据"),
    /**
     * 虹膜采集设备，判断工人是否已经参加培训
     */
    IS_TRAIN(831, Command831.class, "虹膜采集设备，判断工人是否已经参加培训"),
    /**
     * 上传采集资料，采集设备发送
     */
    UPLOAD_FEATURE(832, Command832.class, "上传采集资料"),

    /**
     * 虹膜采集设备，获取项目名称
     */
    GET_PROJECT_NAME(834, Command834.class, "获取项目名称"),

    /**
     * 获取设备安装位置，考勤设备发送
     */
    GET_DEVICE_BIND(838, null, "获取设备安装位置"),

    /**
     * 获取人员特征信息
     */
    GET_NAME_LIST(839, Command839.class, "获取人员特征信息"),

    /**
     * 获取人员特征信息，考勤设备发送
     */
    GET_USER_INFO(841, Command841.class, "获取人员特征信息"),

    /**
     * 拓展APP，上传考勤记录和图片
     */
    UPLOAD_ATTENDANCE_FILE(842, Command842.class, "上传考勤记录和图片"),

    /**
     * 拓展APP，上传包含勤照片及体温的考勤信息
     */
    UPLOAD_ATTENDANCE_FILE_TEMPERATURE(844, Command844.class, "上传包含勤照片及体温的考勤信息");

    private Integer code;
    private Class<? extends AbstractCommand> command;
    private String description;

    EnumCommand(Integer code, Class<? extends AbstractCommand> command, String description) {
        this.code = code;
        this.command = command;
        this.description = description;
    }

    public static EnumCommand fromCode(int code) {
        for (EnumCommand command : values()) {
            if (command.code != code) {
                continue;
            }
            return command;
        }
        return null;
    }

    public Class<? extends AbstractCommand> getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
