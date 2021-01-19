package com.apex.hrssmachineexecute.factory;

import com.apex.hrssmachineexecute.command.AbstractCommand;
import com.apex.hrssmachineexecute.constant.EnumCommand;
import com.apex.hrssmachineexecute.provider.DeviceServiceProvider;
import io.netty.channel.Channel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class CommandFactory {
    /**
     * 日志记录器
     */
    private static Logger LOGGER = LoggerFactory.getLogger(CommandFactory.class);

    private CommandFactory() {
    }

    public static AbstractCommand getInstance(EnumCommand command, DeviceServiceProvider provider, Channel channel) {
        if (null == command) {
            return null;
        }
        Class<? extends AbstractCommand> clazz = command.getCommand();

        if (null == clazz) {
            return null;
        }

        try {
            Constructor<? extends AbstractCommand> c = clazz.getConstructor(DeviceServiceProvider.class, Channel.class);
            return c.newInstance(provider, channel);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }
}
