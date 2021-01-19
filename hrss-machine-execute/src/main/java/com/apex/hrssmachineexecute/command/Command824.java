package com.apex.hrssmachineexecute.command;

import com.apex.hrssmachineexecute.constant.EnumDeviceModel;
import com.apex.hrssmachineexecute.convert.FaceFeatureConvertor;
import com.apex.hrssmachineexecute.convert.IrisFeatureConvertor;
import com.apex.hrssmachineexecute.domain.*;
import com.apex.hrssmachineexecute.provider.DeviceServiceProvider;
import com.apex.hrssmachineexecute.utils.ByteArrayBuilder;
import com.apex.hrssmachineexecute.utils.CommandUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 请求白名单
 *
 * @author: liuzhimin
 * @date: 2019年7月16日 下午4:08:28
 * @version: 1.0
 */
public class Command824 extends AbstractCommand {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(Command824.class);

    public Command824(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        DockDataResult result = new DockDataResult();
        // byte[] body = param.getContent();
        // int version = param.getVersion();
        // int position = 0;
        // 0-31 设备的唯一标识码 32 ASCII 每台设备的唯一标识
        // String deviceCode = new String(ArrayUtils.subarray(body, position, position
        // += 32), StandardCharsets.US_ASCII);
        // 32-38 最后获取数据时间 7 HEX BCD码，年占2字节，月、日、时、分、秒各占一个字节
        // int year = CommandUtils.toInt(ArrayUtils.subarray(body, position, position +=
        // 2));
        // int month = CommandUtils.toInt(ArrayUtils.subarray(body, position, position
        // += 1));
        // int day = CommandUtils.toInt(ArrayUtils.subarray(body, position, position +=
        // 1));
        // int hour = CommandUtils.toInt(ArrayUtils.subarray(body, position, position +=
        // 1));
        // int minute = CommandUtils.toInt(ArrayUtils.subarray(body, position, position
        // += 1));
        // int second = CommandUtils.toInt(ArrayUtils.subarray(body, position, position
        // += 1));
        // 39 校验和 1 HEX Xor校验运算
        // int xor = CommandUtils.toInt(ArrayUtils.subarray(body, position, position +=
        // 1));

        CustomDevice device = this.getDevice();
        String deviceCode = device.getDeviceCode();
        EnumDeviceModel deviceModel = device.getDeviceModel();
        logger.info("设备厂商编号={}==deviceCode:[{}]", deviceModel, deviceCode);
        CustomProject project = provider.loadProject(device);
        if (null == project) {
            result.setSuccess(false);
            return result;
        }

        String projectCode = project.getProjectCode();
        String projectName = project.getProjectName();
        logger.info("设备项目信息=projectCode:[{}]===工程名称==projectName:[{}]", projectCode, projectName);
        ByteArrayBuilder content = new ByteArrayBuilder();
//        content.append(CommandUtils.toBytes(projectCode, 4));
        content.append(CommandUtils.adjust(projectCode.getBytes(StandardCharsets.UTF_8), 4));

        content.append(CommandUtils.adjust(projectName.getBytes(StandardCharsets.UTF_8), 100));
        deviceModel = EnumDeviceModel.TENFINE_TWO;
        switch (deviceModel) {
            case TENFINE_ONE: {
                break;
            }
            case TENFINE_TWO: {
                List<CustomWorker> workers = provider.thLoadAllWorkers(device);
                for (CustomWorker worker : workers) {
                    logger.info(worker.getIdcardNo() + worker.getWorkerName());
                }
                logger.info("拓展二代==deviceCode:[{}]==返回记录:{}条", deviceCode, workers.size());
                // 白名单下发时，未获取特征信息
                FaceFeatureConvertor.encodeTenfineTwo(content, workers);
                break;
            }
            case HQ: {
//                List<CustomWorker> workers = provider.loadWorkers(device);
//                logger.info("海清人脸==deviceCode:[{}]==返回记录:{}条", deviceCode, workers.size());
//                FaceFeatureConvertor.encodeHaiQing(content, workers);
                break;
            }
            case HM: {
//                List<CustomWorker> workers = provider.loadWorkers(device);
//                logger.info("虹膜==deviceCode:[{}]==返回记录:{}条", deviceCode, workers.size());
//                IrisFeatureConvertor.encode(content, workers);
                break;
            }
            default: {
                break;
            }
        }
        logger.info("获取项目人员结束即将返回===deviceCode:{}", deviceCode);
        byte[] all = content.toByteArray();
        content.append(CommandUtils.getXor(all));
        result.setSuccess(true);
        result.setContent(content.toByteArray());
        return result;
    }
}
