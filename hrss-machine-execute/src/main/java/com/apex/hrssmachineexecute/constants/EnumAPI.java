package com.apex.hrssmachineexecute.constants;

public enum EnumAPI {
    TIME("/api/device/time", 3, "获取时间"),
    ITEM("/api/device/item", 1, "获取项目信息"),
    COLLECT_FACE("/api/device/collect-face", 30, "采集人脸上传"),
    WORKERS("/api/device/workers", 4, "获取人员"),
    COMMIT("/api/device/commit", 4, "提交获取人员状态"),
    ATTENDANCE("/api/device/attendance", 20, "考勤上传");
    private String url;
    private int limit;
    private String description;

    public String getUrl() {
        return url;
    }

    public int getLimit() {
        return limit;
    }

    public String getDescription() {
        return description;
    }

    private EnumAPI(String url, int limit, String description) {
        this.url = url;
        this.limit = limit;
        this.description = description;
    }

    public static EnumAPI byUrl(String url) {
        for (EnumAPI e : values()) {
            if (e.url.equals(url)) {
                return e;
            }
        }
        return null;
    }
}
