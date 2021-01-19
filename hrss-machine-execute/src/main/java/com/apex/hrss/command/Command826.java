package com.apex.hrss.command;

import com.apex.hrss.domain.DockDataParam;
import com.apex.hrss.domain.DockDataResult;
import com.apex.hrss.provider.DeviceServiceProvider;
import com.apex.hrss.utils.CommandUtils;
import io.netty.channel.Channel;

import java.nio.charset.StandardCharsets;

/**
 * 设备是否需要更新
 *
 * @author: liuzhimin
 * @date: 2019年7月16日 下午3:29:19
 * @version: 1.0
 */
public class Command826 extends AbstractCommand {

    public Command826(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        // 是否需要更新	1	ASCII	0：不需要更新，1：提示更新，倒计时10秒，可手动选择，默认不需要更新2：强制自动更新，不需要提示
        byte[] isUpdate = "0".getBytes(StandardCharsets.US_ASCII);
        // 本次更新内容描述	100	UTF-8	本次更新内容描述
        byte[] message = CommandUtils.adjust("不需要更新".getBytes(StandardCharsets.UTF_8), 100);
        // 校验和	1 HEX Xor校验运算
        byte xor = CommandUtils.getXor(CommandUtils.merge(isUpdate, message));

        DockDataResult result = new DockDataResult();
        byte[] content = CommandUtils.merge(isUpdate, message, new byte[]{xor});
        result.setContent(content);
        result.setSuccess(true);
        return result;
    }

}
