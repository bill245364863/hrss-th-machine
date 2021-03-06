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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 上传考勤数据
 *
 * @author: liuzhimin
 * @date: 2019年7月22日 下午4:27:42
 * @version: 1.0
 */
public class Command842 extends AbstractCommand {

    private static final Logger logger = LoggerFactory.getLogger(Command842.class);

    public Command842(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        byte[] body = param.getContent();
        int position = 0;


        List<CustomAttendance> list = new ArrayList<>();
        while (body.length - position >= 16) {
            // 工人编号 4 HEX 低位在前
            int workerId = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 4));
            // 刷卡时间 7 HEX BCD码，年占2字节，月、日、时、分、秒各占一个字节
            Date attendDate = CommandUtils.toDate(ArrayUtils.subarray(body, position, position += 7));
            // 刷卡模式 1 HEX 1表示虹膜，2表示二代证，3表示IC卡，4表示联名卡，5表示IC卡，6表示人脸, 7
            int checkType = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 1));
            // 照片长度(PL) 4 HEX 低位在前
            int picLength = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 4));
            // 照片信息 PL HEX 考勤照片的字节数组
            byte[] attendImage = ArrayUtils.subarray(body, position, position += picLength);
            // lng 10 ASCII 百度地图经度
            String lng = new String(ArrayUtils.subarray(body, position, position += 10), StandardCharsets.US_ASCII).trim();
            // lat 10 ASCII 百度地图维度
            String lat = new String(ArrayUtils.subarray(body, position, position += 10), StandardCharsets.US_ASCII).trim();

            CustomAttendance attn = new CustomAttendance();
            attn.setWorkerId(workerId);
            attn.setAttendDate(attendDate);
            attn.setAttendImage(attendImage);
            attn.setLng(lng);
            attn.setLat(lat);
            //拓展7代表副机：出场
            if (checkType == 7) {
                attn.setAttendDir(0);
            }
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
