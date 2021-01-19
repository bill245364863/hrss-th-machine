package com.apex.hrss.command;

import com.apex.hrss.domain.CustomWorker;
import com.apex.hrss.domain.DockDataParam;
import com.apex.hrss.domain.DockDataResult;
import com.apex.hrss.provider.DeviceServiceProvider;
import com.apex.hrss.utils.CommandUtils;
import io.netty.channel.Channel;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 判断工人是否已经参加培训[831]
 *
 * @author: liuzhimin
 * @date: 2019年7月12日 上午11:09:29
 * @version: 1.0
 */
public class Command831 extends AbstractCommand {

    public Command831(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        byte[] body = param.getContent();
        DockDataResult result = new DockDataResult();

        String idCardNo = new String(ArrayUtils.subarray(body, 0, 18));
//        CustomWorker worker = provider.loadWorkerByIdCardNo(idCardNo);
        CustomWorker worker = null;

        boolean isTrain = false;

        if (null != worker) {
            isTrain = worker.isTrain();
        }

        byte[] isTrainByte = CommandUtils.toBytes(isTrain ? 1 : 2, 1);
        // 备用
        byte[] backupByte = new byte[13];
        // 校验值
        byte xor = CommandUtils.getXor(CommandUtils.merge(isTrainByte, backupByte));
        byte[] content = CommandUtils.merge(isTrainByte, backupByte, new byte[]{xor});
        result.setContent(content);
        result.setSuccess(true);
        return result;
    }

}
