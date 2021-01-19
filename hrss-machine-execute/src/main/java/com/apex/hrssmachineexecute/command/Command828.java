package com.apex.hrssmachineexecute.command;

import com.apex.hrssmachineexecute.domain.CustomAttendance;
import com.apex.hrssmachineexecute.domain.CustomDevice;
import com.apex.hrssmachineexecute.domain.DockDataParam;
import com.apex.hrssmachineexecute.domain.DockDataResult;
import com.apex.hrssmachineexecute.provider.DeviceServiceProvider;
import com.apex.hrssmachineexecute.utils.CommandUtils;
import io.netty.channel.Channel;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Command828 extends AbstractCommand {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(Command828.class);

    public Command828(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        byte[] body = param.getContent();
        int position = 0;

        List<CustomAttendance> list = new ArrayList<>();
        while (body.length - position >= 16) {
            // 工人编号(seq_no) 4 HEX 低位在前
            int workerId = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 4));
            // 刷卡时间 7 HEX BCD码，年占2字节，月、日、时、分、秒各占一个字节
            Date attendDate = CommandUtils.toDate(ArrayUtils.subarray(body, position, position += 7));
            // 刷卡模式 1 HEX 1表示虹膜，2表示二代证，3表示IC卡，4表示联名卡，5表示IC卡，6表示人脸
            int checkType = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 1));
            // 照片长度(PL) 4 HEX 低位在前
            int picLength = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 4));
            // 照片信息 PL HEX 考勤照片的字节数组
            byte[] checkPhoto = ArrayUtils.subarray(body, position, position += picLength);

            CustomAttendance attn = new CustomAttendance();
            attn.setWorkerId(workerId);
            attn.setAttendDate(attendDate);
            attn.setAttendImage(checkPhoto);
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
