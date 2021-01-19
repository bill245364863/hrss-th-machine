package com.apex.hrssmachineexecute.filter;

import com.apex.hrssmachineexecute.domain.DockDataPacket;
import com.apex.hrssmachineexecute.utils.CommandUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 和设备Socket通讯解码器
 *
 * @author wangxl
 */
public class TcpClientDecoder extends ByteToMessageDecoder {

    /**
     * 解码实现
     *
     * @param ctx 上下文
     * @param in  输入流
     * @param out 输出对象
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > 32) {
            in.markReaderIndex();

            //1.获取头部
            byte[] header = new byte[1];
            in.readBytes(header);

            //2.内容长度
            byte[] length = new byte[4];
            in.readBytes(length);
            int contentLength = CommandUtils.toInt(length);

            //2.1 报文头不正确
            if (header[0] != 0x01) {
                in.resetReaderIndex();
                return;
            }

            //3.分包顺序索引
            byte[] fblength = new byte[4];
            in.readBytes(fblength);

            //4.分包总数
            byte[] fblength2 = new byte[4];
            in.readBytes(fblength2);

            //5. 版本
            byte[] version = new byte[1];
            in.readBytes(version);

            //6. 命令
            byte[] cmdb = new byte[2];
            in.readBytes(cmdb);
            int cmd = CommandUtils.toInt(cmdb);

            //7.session
            byte[] sessionb = new byte[16];
            in.readBytes(sessionb);
            String session = new String(sessionb);

            //7.1 报文长度不正确
            if (in.readableBytes() < contentLength) {
                in.resetReaderIndex();
                return;
            }

            //8内容
            byte[] contentb = new byte[contentLength];
            if (contentLength > 0) {
                in.readBytes(contentb);
            }

            //9状态
            byte[] flag = new byte[1];
            in.readBytes(flag);

            //10结束
            byte[] end = new byte[1];
            in.readBytes(end);

            out.add(new DockDataPacket(contentb, CommandUtils.toInt(version), session, cmd, flag[0]));
        }
    }
}
