package com.apex.hrssmachineexecute.runner;

import com.apex.hrssmachineexecute.provider.JsgDeviceServiceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 实现ApplicationRunner接口，容器启动的时候
 * 我们需要使用CommandLineRunner或ApplicationRunner接口创建bean，spring boot会自动监测到它们。
 * 这两个接口都有一个run()方法，在实现接口时需要覆盖该方法，并使用@Component注解使其成为bean。
 */
@Component
public class DeviceRunner implements ApplicationRunner {

    @Resource
    private JsgDeviceServiceProvider provider;

    @Value("${netty.port}")
    private int port;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        new DeviceServer().provider(provider).start(port);
    }
}
