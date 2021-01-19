package com.apex.hrss.runner;

import com.apex.hrss.filter.TcpClientDecoder;
import com.apex.hrss.filter.TcpClientEncoder;
import com.apex.hrss.filter.TcpServerHandler;
import com.apex.hrss.provider.DeviceServiceProvider;
import com.apex.hrss.utils.ThLogger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;

public class DeviceServer {
    private Logger logger = ThLogger.getLogger(DeviceServer.class);

    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    //	bossgroup
    private EventLoopGroup boss = new NioEventLoopGroup();
    //	workgroup
    private EventLoopGroup work = new NioEventLoopGroup();
    private DeviceServiceProvider provider;

    public void start(int port) {
        Assert.notNull(provider, "must setting provider");
        try {
            serverBootstrap
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new TcpClientDecoder());
                            pipeline.addLast(new TcpClientEncoder());
                            pipeline.addLast(new TcpServerHandler(provider));
                        }
                    })
                    .bind(port)
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (InterruptedException e) {
            logger.error("netty服务器异常");
            logger.error(ExceptionUtils.getStackTrace(e));
            Thread.currentThread().interrupt();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    /**
     * 停止服务
     */
    public void close() {
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }

    public DeviceServer provider(DeviceServiceProvider provider) {
        this.provider = provider;
        return this;
    }
}
