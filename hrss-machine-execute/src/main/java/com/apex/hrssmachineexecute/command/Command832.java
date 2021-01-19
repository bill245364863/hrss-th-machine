package com.apex.hrssmachineexecute.command;

import com.apex.hrssmachineexecute.constant.EnumDeviceModel;
import com.apex.hrssmachineexecute.convert.FaceFeatureConvertor;
import com.apex.hrssmachineexecute.convert.IrisFeatureConvertor;
import com.apex.hrssmachineexecute.domain.CustomDevice;
import com.apex.hrssmachineexecute.domain.CustomFeature;
import com.apex.hrssmachineexecute.domain.DockDataParam;
import com.apex.hrssmachineexecute.domain.DockDataResult;
import com.apex.hrssmachineexecute.provider.DeviceServiceProvider;
import com.apex.hrssmachineexecute.utils.ByteArrayBuilder;
import com.apex.hrssmachineexecute.utils.CommandUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 上传采集结果[832]
 *
 * @author: liuzhimin
 * @date: 2019年7月15日 上午8:38:25
 * @version: 1.0
 */
public class Command832 extends AbstractCommand {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(Command832.class);

    public Command832(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        CustomDevice device = this.getDevice();
        String deviceCode = device.getDeviceCode();
        EnumDeviceModel deviceModel = device.getDeviceModel();
        CustomFeature feature = this.covert(param, deviceModel);

        ByteArrayBuilder content = new ByteArrayBuilder();
        // 0 返回结果 1 HEX 0:成功，1:失败
        // 1-30 失败原因 30 UTF-8 失败原因
        // 31-40 保留 10 HEX 保留字段，暂时没用
        // 41 校验和 1 HEX Xor校验运算

        DockDataResult result = new DockDataResult();
        if (null == feature) {
            content.append(CommandUtils.toBytes(1, 1));
            content.append(CommandUtils.adjust("解析特征失败".getBytes(StandardCharsets.UTF_8), 30));

            logger.info("deviceCode:[{}]=解析特征失败", deviceCode);
        } else {
            boolean isDone = provider.processFeature(device, feature);

            logger.info("deviceCode:[{}]=保存特征{}", deviceCode, isDone ? "成功" : "失败");

            if (isDone) {
                content.append(CommandUtils.toBytes(1, 0));
                content.append(new byte[30]);
            } else {
                content.append(CommandUtils.toBytes(1, 1));
                content.append(CommandUtils.adjust("保存特征失败".getBytes(StandardCharsets.UTF_8), 30));
            }
        }
        content.append(new byte[10]);

        byte[] all = content.toByteArray();
        content.append(CommandUtils.getXor(all));

        result.setContent(content.toByteArray());
        result.setSuccess(true);
        return result;
    }

    private CustomFeature covert(DockDataParam param, EnumDeviceModel deviceModel) {
        byte[] body = param.getContent();
        switch (deviceModel) {
            case TENFINE_ONE:
                return null;
            case TENFINE_TWO:
                return FaceFeatureConvertor.decodeTenfineTwo(body);
            case HQ:
                return FaceFeatureConvertor.decodeHaiqing(body);
            case HM:
                return IrisFeatureConvertor.decode(body);
            default:
                return null;
        }
    }

}
