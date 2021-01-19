package com.apex.hrss.command;

import com.apex.hrss.domain.CustomDevice;
import com.apex.hrss.domain.CustomProject;
import com.apex.hrss.domain.DockDataParam;
import com.apex.hrss.domain.DockDataResult;
import com.apex.hrss.provider.DeviceServiceProvider;
import com.apex.hrss.utils.CommandUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 虹膜设备获取项目信息
 *
 * @author: liuzhimin
 * @date: 2019年7月12日 上午9:40:32
 * @version: 1.0
 */
public class Command834 extends AbstractCommand {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(Command834.class);

    public Command834(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        DockDataResult result = new DockDataResult();
        CustomDevice device = this.getDevice();
        CustomProject project = provider.loadProject(device);

        if (null == project) {
            String deviceCode = device.getDeviceCode();
            logger.info("deviceCode:[{}]=项目信息不存在", deviceCode);

            result.setMessage("项目信息不存在");
            result.setSuccess(false);
            return result;
        }

        // 项目
        byte[] projectNameByte = CommandUtils.adjust(project.getProjectName().getBytes(StandardCharsets.UTF_8), 100);
        // 备用
        byte[] backup = new byte[99];
        // 校验值
        byte xor = CommandUtils.getXor(CommandUtils.merge(projectNameByte, backup));

        byte[] content = CommandUtils.merge(projectNameByte, backup, new byte[]{xor});

        result.setContent(content);
        result.setSuccess(true);
        return result;
    }
}
