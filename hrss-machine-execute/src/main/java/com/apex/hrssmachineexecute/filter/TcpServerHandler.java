package com.apex.hrssmachineexecute.filter;

import com.apex.hrssmachineexecute.command.AbstractCommand;
import com.apex.hrssmachineexecute.constant.EnumCommand;
import com.apex.hrssmachineexecute.domain.CustomDevice;
import com.apex.hrssmachineexecute.domain.DockDataPacket;
import com.apex.hrssmachineexecute.domain.DockDataParam;
import com.apex.hrssmachineexecute.domain.DockDataResult;
import com.apex.hrssmachineexecute.factory.CommandFactory;
import com.apex.hrssmachineexecute.provider.DeviceServiceProvider;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Socket协议接口实现
 *
 * @author wangxinlin
 * @date 2019-05-23
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<DockDataPacket> {

    public static final AttributeKey<CustomDevice> DEVICE_KEY = AttributeKey.newInstance("ATTR_DEVICE");

    /**
     * 日志记录器
     */
    private Logger logger = LoggerFactory.getLogger(TcpServerHandler.class);

    private DeviceServiceProvider provider;

    public TcpServerHandler(DeviceServiceProvider provider) {
        this.provider = provider;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        logger.error(ExceptionUtils.getStackTrace(cause));
        channelHandlerContext.close();
    }

    private void merge(DockDataPacket data, DockDataResult result) {
        boolean isSuccess = result.isSuccess();

        byte flag = isSuccess ? (byte) 0x0 : (byte) 0x01;

        byte[] content;
        if (!isSuccess) {
            String message = result.getMessage();
            if (StringUtils.isEmpty(message)) {
                content = null;
            } else {
                content = message.getBytes(StandardCharsets.UTF_8);
            }
        } else {
            content = result.getContent();
        }
        if (null == content) {
            content = new byte[0];
        }
        data.setFlag(flag);
        data.setContent(content);
    }

    /**
     * 在接收到服务器数据后调用
     * @param ctx
     * @param data
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DockDataPacket data) throws Exception {
        Channel channel = ctx.channel();
        Attribute<CustomDevice> attr = channel.attr(DEVICE_KEY);
        CustomDevice device = null;
        if (null != attr) {
            device = attr.get();
        }

        DockDataResult result;
        int commandCode = data.getCommand();
        //获取对应的枚举类
        EnumCommand commandType = EnumCommand.fromCode(commandCode);
        if (EnumCommand.LOGIN != commandType && device == null) {
            result = new DockDataResult();
            result.setSuccess(false);
            result.setMessage("设备未登录");
            logger.info("设备未登录command:{}", commandCode);
        } else {
            AbstractCommand command = CommandFactory.getInstance(commandType, provider, channel);
            if (null == command) {
                result = new DockDataResult();
                result.setSuccess(false);
                result.setMessage("不支持的命令: " + commandCode);
                logger.info("不支持的命令command:{}", commandCode);
            } else {
                String deviceVerify = "";
                String deviceCode = "";
                if (null != device) {
                    deviceVerify = device.getDeviceVerify();
                    deviceCode = device.getDeviceCode();
                }
                String desc = commandType.getDescription();
                logger.info("{}{}=开始=厂家设别码{},设备号{}", commandCode, desc, deviceVerify, deviceCode);
                try {
                    result = command.invoke(new DockDataParam(data.getContent(), data.getVersion()));
                } catch (Exception e) {
                    logger.error("{}{}=异常=厂家设别码{},设备号{}=={}", commandCode, desc, deviceVerify, deviceCode, ExceptionUtils.getStackFrames(e));
                    result = new DockDataResult();
                    result.setSuccess(false);
                }
                logger.info("{}{}=结束=厂家设别码{},设备号{}", commandCode, desc, deviceVerify, deviceCode);
            }

            provider.refresh(device);
        }

        this.merge(data, result);
        ctx.channel().writeAndFlush(data);
    }
}
