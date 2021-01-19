package com.apex.hrssmachineexecute.filter;

import com.apex.hrssmachineexecute.domain.DockDataPacket;
import com.apex.hrssmachineexecute.utils.CommandUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 和设备Socket通讯编码器
 *
 * @author wangxl
 */
public class TcpClientEncoder extends MessageToByteEncoder<DockDataPacket> {

    /**
     * 编码规则
     *
     * @param ctx
     * @param dataPacket
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, DockDataPacket dataPacket, ByteBuf out) throws Exception {
        //发送内容
        byte[] content = dataPacket.getContent();

        //1添加头
        byte[] header = {0x01};
        out.writeBytes(header);

        //2添加长度
        byte[] length = CommandUtils.toBytes(content.length, 4);
        out.writeBytes(length);

        //3分包索引
        byte[] partIndex = {0x00, 0x00, 0x00, 0x00};
        out.writeBytes(partIndex);

        //4分包数量
        byte[] partCount = {0x00, 0x00, 0x00, 0x00};
        out.writeBytes(partCount);

        //5版本
        byte[] version = CommandUtils.toBytes(dataPacket.getVersion(), 1);
        out.writeBytes(version);

        //6添加命令
        byte[] cmd = CommandUtils.toBytes(dataPacket.getCommand(), 2);
        out.writeBytes(cmd);

        //7.session
        byte[] session = dataPacket.getSession().getBytes();
        out.writeBytes(session);

        //8.内容
        if (content.length > 0) {
            out.writeBytes(content);
        }

        //9.状态
        byte[] status = {dataPacket.getFlag()};
        out.writeBytes(status);

        //10.结束标记
        byte[] end = {0x01};
        out.writeBytes(end);
    }
}
