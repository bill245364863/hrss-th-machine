/**
 * Copyright (C), 2015-2019, 腾晖科技有限公司
 * FileName: Command823
 * Author:   wangxl
 */
package com.apex.hrss.command;

import com.apex.hrss.domain.CustomDevice;
import com.apex.hrss.domain.DockDataParam;
import com.apex.hrss.domain.DockDataResult;
import com.apex.hrss.provider.DeviceServiceProvider;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 823登陆
 *
 * @author liuzhimin
 */
@Slf4j
public class Command823 extends AbstractCommand {
    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(Command823.class);

    public Command823(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        byte[] body = param.getContent();
        DockDataResult result = new DockDataResult();
        try {
            // 厂家识别码，暂时不作处理
            String veryCode = new String(ArrayUtils.subarray(body, 0, 32), StandardCharsets.US_ASCII).trim();
            //截除掉空白之后的字符
            int veryCodeBlankIndex = StringUtils.indexOf(veryCode, '\0');
            if (veryCodeBlankIndex != -1) {
                veryCode = veryCode.substring(0, veryCodeBlankIndex);
            }
            // 设备唯一识别码
            String deviceCode = new String(ArrayUtils.subarray(body, 32, 64), StandardCharsets.US_ASCII).trim();
            //截除掉空白之后的字符
            int deviceCodeBlankIndex = StringUtils.indexOf(deviceCode, '\0');
            if (deviceCodeBlankIndex != -1) {
                deviceCode = deviceCode.substring(0, deviceCodeBlankIndex);
            }
            logger.info("登陆开始==厂家识别码:[{}],设备号:[{}]", veryCode, deviceCode);

            // 确认设备是否存在--现在APP移动考勤时，采集和考勤共用一个编码，这样会查出两条记录
            CustomDevice device = super.provider.loadDevice(veryCode, deviceCode);

            if (null == device) {
                logger.info("登陆失败==厂家识别码:[{}],设备号:[{}]", veryCode, deviceCode);
                result.setMessage("设备不存在");
                result.setSuccess(false);
                return result;
            } else {
                logger.info("登陆成功==厂家识别码:[{}],设备号:[{}]", veryCode, deviceCode);
                result.setSuccess(true);
            }
            this.setDevice(device);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}