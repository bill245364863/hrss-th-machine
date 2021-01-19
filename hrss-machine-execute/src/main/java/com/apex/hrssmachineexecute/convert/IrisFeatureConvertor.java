package com.apex.hrssmachineexecute.convert;

import com.apex.hrssmachineexecute.domain.CustomFeature;
import com.apex.hrssmachineexecute.domain.CustomWorker;
import com.apex.hrssmachineexecute.utils.ByteArrayBuilder;
import com.apex.hrssmachineexecute.utils.CommandUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * @Description:虹膜采集转换
 * @author: liuzhimin
 * @date: 2019年7月15日 上午10:05:39
 * @version: 1.0
 */
public class IrisFeatureConvertor {
    private IrisFeatureConvertor() {
    }

    public static CustomFeature decode(byte[] body) {
        int position = 0;
        // 0-17 身份证号码 18 ASCII
        String cardNo = new String(ArrayUtils.subarray(body, position, position += 18), StandardCharsets.US_ASCII)
                .trim();
        // 18-47 姓名 30 UTF8
        String name = new String(ArrayUtils.subarray(body, position, position += 30), StandardCharsets.UTF_8).trim();
        // 48-49 性别 2 ASCII 1：男：0：女
        String sex = new String(ArrayUtils.subarray(body, position, position += 2), StandardCharsets.US_ASCII).trim();
        // 50-53 民族 4 ASCII 代号
        String nation = CommandUtils.getNationByCode(
                new String(ArrayUtils.subarray(body, position, position += 4), StandardCharsets.US_ASCII).trim());
        // 54-69 出生日期 16 ASCII yyyyMMdd(20170409)
        String birthday = new String(ArrayUtils.subarray(body, position, position += 16), StandardCharsets.US_ASCII)
                .trim();
        // 70-209 住址 140 UTF8
        String address = new String(ArrayUtils.subarray(body, position, position += 140), StandardCharsets.UTF_8)
                .trim();
        // 210-2257 虹膜 2048 HEX
        byte[] feature = ArrayUtils.subarray(body, position, position += 4096);
        // 2258-2258 虹膜识别度 1 HEX 0:高，1：低
        int featureLev = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 1));
        // 2259-2259 是否重采 1 HEX 0:是，1:不是
        int isRepick = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 1));
        // 2260-2319 身份证发证机关 60 UTF8
        String idCardDept = new String(ArrayUtils.subarray(body, position, position += 60), StandardCharsets.UTF_8)
                .trim();
        // 2320-2383 身份证有效期 64 UTF8
        String idCardDate = new String(ArrayUtils.subarray(body, position, position += 64), StandardCharsets.UTF_8)
                .trim();
        // 2384 是否手工 1 HEX 0表示刷身份证采集，1表示手工录入资料
        int isManual = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 1));
        // 2385-2483 保留 99 HEX 保留字段，暂时没用
        byte[] backup = ArrayUtils.subarray(body, position, position += 99);
        // 2484-2487 身份证照片长度（PL） 4 HEX
        int idcardPhotoLenght = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 4));
        // 2488-(2488+PL-1) 身份证照片 PL HEX
        byte[] idcardPhoto = ArrayUtils.subarray(body, position, position += idcardPhotoLenght);
        // (2488+PL)- (2488+PL+3) 采集照片长度(GL) 4 HEX
        int collectPhotoLength = CommandUtils.toInt(ArrayUtils.subarray(body, position, position += 4));
        // (2488+PL+4)-(LEN-2) 采集照片 GL HEX 采集照片，根据实际长度发送
        byte[] collectPhoto = ArrayUtils.subarray(body, position, position += collectPhotoLength);
        // LEN-1 校验和 1 HEX Xor校验运算
        byte[] xor = ArrayUtils.subarray(body, position, position += 1);

        CustomFeature f = new CustomFeature();
        f.setCardNo(cardNo);
        f.setName(name);
        f.setSex(sex);
        f.setNation(nation);
        f.setBirthday(birthday);
        f.setAddress(address);
        f.setFeature(feature);
        f.setFeatureLev(featureLev);
        f.setIsRepick(isRepick);
        f.setIdCardDept(idCardDept);
        f.setIdCardDate(idCardDate);
        f.setIsManual(isManual);
        f.setBackup(backup);
        f.setHeadImage(idcardPhoto);
        f.setCollectPhoto(collectPhoto);
        f.setXor(xor);
        return f;
    }

    /**
     * @param content
     * @param list
     */
    public static void encode(ByteArrayBuilder content, List<CustomWorker> list) {
        for (CustomWorker worker : list) {
            if (worker.getStatus() == 0) {
                content.append(CommandUtils.toBytes(1, 1));
                byte[] workerId = CommandUtils.toBytes(worker.getWorkerId(), 4);
                content.append(workerId);
            } else {
                // 105-108 工人编号 4 HEX 刷卡成功上传时需要用到
                content.append(CommandUtils.toBytes(0, 1));
                byte[] workerId = CommandUtils.toBytes(worker.getWorkerId(), 4);
                content.append(workerId);
                // 109-118 工人姓名 10 UTF-8
                byte[] workerName = CommandUtils.adjust(worker.getWorkerName().getBytes(StandardCharsets.UTF_8), 10);
                content.append(workerName);
                // 119-136 身份证号码 18 ASCII
                byte[] idcardNo = CommandUtils.adjust(worker.getIdcardNo().getBytes(StandardCharsets.UTF_8), 18);
                content.append(idcardNo);
                // 137-166 工作岗位 30 UTF-8
                byte[] workType = CommandUtils.adjust(worker.getWorkType().getBytes(StandardCharsets.UTF_8), 30);
                content.append(workType);
                // 167-173 最后培训时间 7 HEX BCD码，年占2字节，月、日、时、分、秒各占一个字节
                Date trainTime = worker.getLastTrainTime();
                byte[] lastTrainTime = CommandUtils.toBytes(trainTime);
                content.append(lastTrainTime);
                // 虹膜信息 4096 Hex
                byte[] feature = worker.getFeature();

                byte[] featureByte = CommandUtils.toBytes(new String(feature, StandardCharsets.US_ASCII));
                content.append(featureByte);

                // RFID卡号 8 HEX 手机卡的RFID卡号，用于手机刷卡
                byte[] rfid = new byte[8];
                content.append(rfid);
                // 手机卡状态 1 HEX 0:正常，1：停机
                byte[] rfidStatus = new byte[]{1};
                content.append(rfidStatus);
                // 备份 1 备份
                byte[] backup = new byte[1];
                content.append(backup);
            }
        }
    }
}
