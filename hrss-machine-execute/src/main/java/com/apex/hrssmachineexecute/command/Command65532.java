package com.apex.hrssmachineexecute.command;

import com.apex.hrssmachineexecute.domain.DockDataParam;
import com.apex.hrssmachineexecute.domain.DockDataResult;
import com.apex.hrssmachineexecute.provider.DeviceServiceProvider;
import com.apex.hrssmachineexecute.utils.DateUtils;
import io.netty.channel.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 同步服务器时间
 *
 * @author: liuzhimin
 * @date: 2019年7月18日 下午6:45:43
 * @version: 1.0
 */
public class Command65532 extends AbstractCommand {

    public Command65532(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        DockDataResult result = new DockDataResult();
        String now = DateUtils.format(new Date(), "yyyyMMddHHmmss");
        result.setSuccess(true);
        result.setContent(now.getBytes(StandardCharsets.UTF_8));
        return result;
    }

}
