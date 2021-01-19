package com.apex.hrss.command;

import com.apex.hrss.domain.CustomDevice;
import com.apex.hrss.domain.DockDataParam;
import com.apex.hrss.domain.DockDataResult;
import com.apex.hrss.filter.TcpServerHandler;
import com.apex.hrss.provider.DeviceServiceProvider;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

public abstract class AbstractCommand {
    protected DeviceServiceProvider provider;
    //通道
    protected Channel channel;

    public AbstractCommand(DeviceServiceProvider provider, Channel channel) {
        this.provider = provider;
        this.channel = channel;
    }

    protected CustomDevice getDevice() {
        Attribute<CustomDevice> attr = channel.attr(TcpServerHandler.DEVICE_KEY);
        return attr.get();
    }

    protected void setDevice(CustomDevice device) {
        channel.attr(TcpServerHandler.DEVICE_KEY).set(device);
    }

    public abstract DockDataResult invoke(DockDataParam param) throws Exception;
}
