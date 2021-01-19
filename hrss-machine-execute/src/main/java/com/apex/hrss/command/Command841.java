package com.apex.hrss.command;

import com.apex.hrss.domain.CustomDevice;
import com.apex.hrss.domain.CustomWorker;
import com.apex.hrss.domain.DockDataParam;
import com.apex.hrss.domain.DockDataResult;
import com.apex.hrss.provider.DeviceServiceProvider;
import com.apex.hrss.utils.ByteArrayBuilder;
import com.apex.hrss.utils.CommandUtils;
import com.apex.hrss.utils.StringParser;
import io.netty.channel.Channel;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 请求特征信息
 *
 * @author: liuzhimin
 * @date: 2019年7月22日 上午10:12:55
 * @version: 1.0
 */
public class Command841 extends AbstractCommand {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(Command841.class);

    public Command841(DeviceServiceProvider provider, Channel channel) {
        super(provider, channel);
    }

    @Override
    public DockDataResult invoke(DockDataParam param) {
        DockDataResult result = new DockDataResult();
        byte[] body = param.getContent();

        int position = 0;
        logger.info(new String(body, StandardCharsets.US_ASCII));
        //报文验证码
        final String validCode = "FACESOCKETACCESS";
        String uCode = new String(ArrayUtils.subarray(body, position, position += 16), StandardCharsets.US_ASCII);
        if (!validCode.equals(uCode)) {
            result.setMessage("验证票据错误");
            result.setSuccess(false);
            return result;
        }

        int projectId = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 4));
        int isAll = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 1));
        int idCardCount = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 1));

        List<String> idCardList = new ArrayList<>(idCardCount);


        int i = 0;
        while (i < idCardCount) {
            String idCard = new String(ArrayUtils.subarray(body, position, position += 18), StandardCharsets.US_ASCII);
            idCardList.add(idCard);
            i++;
        }

        //logger.info(idCardList.toArray().toString());

        CustomDevice device = this.getDevice();
        String deviceCode = device.getDeviceCode();
        logger.info("deviceCode:[{}]=收到份身证号条目:{}条,身份证号码：{}", deviceCode, idCardList.size(), idCardList.toString());
        ByteArrayBuilder content = new ByteArrayBuilder();
        List<CustomWorker> list = provider.thLoadWorkerFeature(device, idCardList);

        logger.info("deviceCode:[{}]=更新特征:{}条", deviceCode, list.size());
        // 数据列表大小，占2字节
        content.append(CommandUtils.toBytes(list.size(), 2));

        for (CustomWorker worker : list) {
            //0-9	姓名	10	UTF-8	姓名
            String workerName = worker.getWorkerName();
            content.append(CommandUtils.adjust(workerName.getBytes(StandardCharsets.UTF_8), 10));
            //10-27	身份证号码	18	ASCII	身份证号码
            String idCardNo = worker.getIdcardNo();
            content.append(CommandUtils.adjust(idCardNo.getBytes(StandardCharsets.US_ASCII), 18));
            //28	民族	1	HEX	代号
            String nation = CommandUtils.getNationCodeByName(worker.getNation());

            byte[] nationBs = CommandUtils.toBytes(StringParser.toInteger(nation), 1);
            content.append(nationBs);
            //29-30	性别	2	ASCII	1：男：0：女
            Integer gender = worker.getGender();
            content.append(CommandUtils.adjust((gender == 0 ? "1" : "0").getBytes(StandardCharsets.US_ASCII), 2));
            //31-170	身份证地址	140	UTF8	身份证地址
            String address = worker.getAddress();
            content.append(CommandUtils.adjust(address.getBytes(StandardCharsets.UTF_8), 140));
            //171-186	出生年月日	16	ASCII	yyyyMMdd(20170409)
            Date birthday = worker.getBirthday();
            content.append(CommandUtils.adjust(CommandUtils.toYMD(birthday).getBytes(StandardCharsets.US_ASCII), 16));
            //187-246	发证机关	60	UTF8	发证机关
            String grantOrg = worker.getGrantOrg();
            content.append(CommandUtils.adjust(grantOrg.getBytes(StandardCharsets.UTF_8), 60));
            //247-310	有效期	64	UTF8	有效期
            Date startDate = worker.getStartDate();
            Date expiryDate = worker.getExpiryDate();
            String validityPeriod = CommandUtils.toYMD(startDate) + "-" + CommandUtils.toYMD(expiryDate);
            content.append(CommandUtils.adjust(validityPeriod.getBytes(StandardCharsets.UTF_8), 64));
            //311-314	项目编号	4	HEX	项目编号
            content.append(CommandUtils.toBytes(projectId, 4));
            //315-318	班组编号	4	HEX	班组编号
            Integer teamId = worker.getTeamId();
            content.append(CommandUtils.toBytes(teamId, 4));
            //319-322	工种编号	4	HEX	工种编号
            Integer workerRole = worker.getWorkerRole();
            content.append(CommandUtils.toBytes(workerRole, 4));
            //323-326	采集照片长度（GL）	4	HEX	采集照片长度
            //326-(326+GL-1)	采集照片	GL	HEX	采集照片
            byte[] feature = worker.getFeature();
            byte[] featureLength = CommandUtils.toBytes(feature.length, 4);
            content.append(featureLength);
            content.append(feature);
            //(326+GL)- (326+GL+3)	身份证照片长度（PL）	4	HEX	身份证照片长度
            //(326+GL+4)-( 316+GL+4+PL-1)	身份证照片	PL	HEX	身份证照片
            byte[] headImage = worker.getHeadImage();
            byte[] headImageLength = CommandUtils.toBytes(headImage.length, 4);
            content.append(headImageLength);
            content.append(headImage);
            //(326+GL+4+PL)-( 326+GL+4+PL+3)	红外照片长度（HL）	4	HEX	红外照片长度
            //(326+GL+4+PL+4)- (326+GL+4+PL+4+HL-1)	红外照片	HL	HEX	红外照片
            content.append(CommandUtils.toBytes(0, 4));
            content.append(new byte[0]);
        }
        byte[] all = content.toByteArray();
        content.append(CommandUtils.getXor(all));
        result.setSuccess(true);
        result.setContent(content.toByteArray());
        return result;
    }
}
