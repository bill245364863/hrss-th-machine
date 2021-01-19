package com.apex.hrss.command;

import com.apex.hrss.domain.DockDataParam;
import com.apex.hrss.domain.DockDataResult;
import com.apex.hrss.provider.DeviceServiceProvider;
import io.netty.channel.Channel;

public class Command65535 extends AbstractCommand {

    /**
     * 心跳
     *
     * @param provider
     */
    public Command65535(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        DockDataResult result = new DockDataResult();
        result.setSuccess(true);
        return result;
    }

}
