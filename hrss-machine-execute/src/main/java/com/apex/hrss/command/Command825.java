package com.apex.hrss.command;

import com.apex.hrss.domain.CustomAttendance;
import com.apex.hrss.domain.CustomDevice;
import com.apex.hrss.domain.DockDataParam;
import com.apex.hrss.domain.DockDataResult;
import com.apex.hrss.provider.DeviceServiceProvider;
import com.apex.hrss.utils.CommandUtils;
import io.netty.channel.Channel;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 上传考勤数据
 *
 * @author: liuzhimin
 * @date: 2019年7月18日 下午6:46:21
 * @version: 1.0
 */
public class Command825 extends AbstractCommand {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(Command825.class);

    public Command825(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) throws Exception {
        byte[] body = param.getContent();
        int position = 0;
        int recordLength = 12;

        List<CustomAttendance> list = new ArrayList<>();

        while (body.length - position >= recordLength) {
            // 0-3 工人编号(seq_no) 4 HEX
            int workerId = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 4));
            // 4-10 刷卡时间 7 HEX BCD码，年占2字节，月、日、时、分、秒各占一个字节
            Date attendDate = CommandUtils.toDate(ArrayUtils.subarray(body, position, position += 7));
            // 11 刷卡模式 1 HEX 1表示虹膜，2表示二代证，3表示IC卡
            int checkType = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 1));

            CustomAttendance attn = new CustomAttendance();
            attn.setWorkerId(workerId);
            attn.setAttendDate(attendDate);
            list.add(attn);
        }

        CustomDevice customDevice = this.getDevice();
        Map<String, Object> resultMap = provider.processAttendance(customDevice, list);
        Object msg = resultMap.get("msg");
        boolean isDone = Boolean.parseBoolean(resultMap.get("isDone").toString());
        String deviceCode = customDevice.getDeviceCode();
        logger.info("deviceCode:[{}]==处理:{},错误信息:{}", deviceCode, isDone ? "成功" : "失败", msg);

        DockDataResult result = new DockDataResult();
        result.setSuccess(isDone);
        return result;
    }
}
