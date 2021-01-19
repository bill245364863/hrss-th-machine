package com;

import com.apex.hrss.utils.AESApopUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Test1 {
    @Test
    public void test2(){
        log.debug("debug");
        log.info("info");
        log.trace("trace");
        log.error("error");
        log.warn("warn");
    }
    @Test
    public void test3(){
        String encrypt = AESApopUtil.encrypt("ahsjkdhajkshdakjsdh","2a29801f65c739bf77ce5347143b92fa");
        String encrypt2 = AESApopUtil.encrypt("ahsjkdhaadsdasdasdfasfdasdjkshdakjsdh","2a29801f65c739bf77ce5347143b92fa");
        System.out.println(encrypt.length());
        System.out.println(encrypt2.length());

    }
}
