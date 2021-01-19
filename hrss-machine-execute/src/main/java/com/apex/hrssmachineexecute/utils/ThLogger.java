package com.apex.hrssmachineexecute.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工厂
 *
 * @author liuzhiming
 */
public class ThLogger {
    private ThLogger() {
        //do nothings
    }

    private static final Logger normal = LoggerFactory.getLogger(ThLogger.class);

    private static final String PREFIX_API = "api";

    private static final Logger api = LoggerFactory.getLogger(PREFIX_API);

    public static Logger getLogger(String key) {
        if (PREFIX_API.equals(key)) {
            return api;
        }
        return normal;
    }

    public static Logger getLogger() {
        return normal;
    }

    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
