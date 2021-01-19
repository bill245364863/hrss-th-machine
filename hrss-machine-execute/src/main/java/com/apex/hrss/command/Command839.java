package com.apex.hrss.command;

import com.apex.hrss.domain.*;
import com.apex.hrss.provider.DeviceServiceProvider;
import com.apex.hrss.utils.ByteArrayBuilder;
import com.apex.hrss.utils.CommandUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 839命令-请求白名单--判断是否有更新
 *
 * @author: liuzhimin
 * @date: 2019年7月22日 上午9:47:54
 * @version: 1.0
 */
public class Command839 extends AbstractCommand {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(Command839.class);

    public Command839(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        DockDataResult result = new DockDataResult();
        CustomDevice device = this.getDevice();

        CustomProject project = provider.loadProject(device);
        if (null == project) {
            result.setSuccess(false);
            return result;
        }
        String projectCode = project.getProjectCode();
        String projectName = project.getProjectName();
        ByteArrayBuilder content = new ByteArrayBuilder();
//        content.append(CommandUtils.toBytes(projectCode, 4));
        content.append(CommandUtils.adjust(projectCode.getBytes(StandardCharsets.UTF_8), 4));
        content.append(CommandUtils.adjust(projectName.getBytes(StandardCharsets.UTF_8), 100));

//        List<CustomWorker> list = provider.thLoadInWorkWorkers(device);
        List<CustomWorker> list = null;

        String deviceCode = device.getDeviceCode();
        logger.info("deviceCode:[{}]=在职人员:{}", deviceCode, list.size());

        for (CustomWorker worker : list) {
            // 105-108 工人编号 4 HEX 刷卡成功上传时需要用到
            byte[] workerId = CommandUtils.toBytes(worker.getWorkerId(), 4);
            content.append(workerId);
            // 109-118 工人姓名 10 UTF-8
            byte[] workerName = CommandUtils.adjust(worker.getWorkerName().getBytes(StandardCharsets.UTF_8), 10);
            content.append(workerName);
            // 119-136 身份证号码 18 ASCII
            byte[] idcardNo = CommandUtils.adjust(worker.getIdcardNo().getBytes(StandardCharsets.UTF_8), 18);
            content.append(idcardNo);
            // 特征长度
            content.append(CommandUtils.toBytes(worker.getFeature().length, 4));
        }

        byte[] all = content.toByteArray();
        content.append(CommandUtils.getXor(all));
        result.setSuccess(true);
        result.setContent(content.toByteArray());
        return result;
    }

}
