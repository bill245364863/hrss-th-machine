package com.apex.hrssmachineexecute.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName: CommandUtils
 * @Description: Socket通讯时来辅助方法类
 * @author: wangxl
 * @date: 2019/5/25 13:25
 */
public class CommandUtils {
    /**
     * 一个节字里面有8位
     */
    private static final int EIGHT = 8;

    /**
     * 字节数组转换整形
     *
     * @param bytes
     * @return
     */
    public static int toInt(byte[] bytes) {
        int num = 0;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * EIGHT;
            num = num | (0xff << position & (bytes[i] << position));
        }
        return num;
    }

    /**
     * 按整形转换为指定长度的字节数组
     *
     * @param data
     * @param size
     * @return
     */
    public static byte[] toBytes(int data, int size) {
        byte[] bytes = new byte[size];

        for (int i = 0; i < bytes.length; i++) {
            int position = i * EIGHT;
            bytes[i] = (byte) ((data & (0xff << position)) >> position);
        }
        return bytes;
    }

    /**
     * 转换日期为字节数组
     *
     * @param data
     * @param size
     * @return
     */
    public static byte[] toBytes(Date d) {
        if (null == d) {
            return new byte[7];
        }

        Instant i = d.toInstant();
        ZonedDateTime time = i.atZone(ZoneId.systemDefault());

        ByteArrayBuilder b = new ByteArrayBuilder();

        String year = StringParser.toString(time.getYear());
        String month = StringParser.toString(time.getMonthValue());
        if (month.length() != 2) {
            month = "0" + month;
        }
        String day = StringParser.toString(time.getDayOfMonth());
        if (day.length() != 2) {
            day = "0" + day;
        }
        String hour = StringParser.toString(time.getHour());
        if (hour.length() != 2) {
            hour = "0" + hour;
        }
        String minute = StringParser.toString(time.getMinute());
        if (minute.length() != 2) {
            minute = "0" + minute;
        }
        String second = StringParser.toString(time.getSecond());
        if (second.length() != 2) {
            second = "0" + second;
        }

        byte[] year1 = toBytes(year.substring(0, 2));
        byte[] year2 = toBytes(year.substring(2));
        b.append(year1);
        b.append(year2);
        b.append(toBytes(month));
        b.append(toBytes(day));
        b.append(toBytes(hour));
        b.append(toBytes(minute));
        b.append(toBytes(second));
        return b.toByteArray();
    }

    /**
     * 转换日期为字节数组
     *
     * @param data
     * @param size
     * @return
     */
    public static String toYMD(Date d) {
        if (null == d) {
            return "";
        }
        Instant i = d.toInstant();
        ZonedDateTime time = i.atZone(ZoneId.systemDefault());

        String year = StringParser.toString(time.getYear());
        String month = StringParser.toString(time.getMonthValue());
        if (month.length() != 2) {
            month = "0" + month;
        }
        String day = StringParser.toString(time.getDayOfMonth());
        if (day.length() != 2) {
            day = "0" + day;
        }

        return StringUtils.join(year, month, day);
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * 合并数组
     *
     * @param values
     * @return
     */
    public static byte[] merge(byte[]... values) {
        int length = 0;
        for (byte[] val : values) {
            length += val.length;
        }
        byte[] all = new byte[length];

        int pos = 0;
        for (byte[] v : values) {
            System.arraycopy(v, 0, all, pos, v.length);
            pos += v.length;
        }
        return all;
    }

    /**
     * 转16进字符串
     *
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public static Date toDate(byte[] bytes) {
        int position = 0;
        String yearStr = CommandUtils.toHexString(ArrayUtils.subarray(bytes, position, position += 2));
        String monthStr = CommandUtils.toHexString(ArrayUtils.subarray(bytes, position, position += 1));
        String dayStr = CommandUtils.toHexString(ArrayUtils.subarray(bytes, position, position += 1));
        String hourStr = CommandUtils.toHexString(ArrayUtils.subarray(bytes, position, position += 1));
        String minuteStr = CommandUtils.toHexString(ArrayUtils.subarray(bytes, position, position += 1));
        String secondStr = CommandUtils.toHexString(ArrayUtils.subarray(bytes, position, position += 1));

        int year = StringParser.toInteger(yearStr);
        int month = StringParser.toInteger(monthStr);
        int day = StringParser.toInteger(dayStr);
        int hour = StringParser.toInteger(hourStr);
        int minute = StringParser.toInteger(minuteStr);
        int second = StringParser.toInteger(secondStr);

        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        ZonedDateTime ld = dateTime.atZone(ZoneId.systemDefault());
        return Date.from(ld.toInstant());
    }

    /**
     * 调整数组长度
     *
     * @param bytes
     * @param length
     * @return
     */
    public static byte[] adjust(byte[] bytes, int length) {
        if (bytes.length < length) {
            return ArrayUtils.addAll(bytes, new byte[length - bytes.length]);
        } else if (bytes.length > length) {
            return ArrayUtils.subarray(bytes, 0, length);
        } else {
            return bytes;
        }
    }

    /**
     * 校验和
     *
     * @param datas
     * @return
     */
    public static byte getXor(byte[] datas) {
        byte temp = datas[0];
        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
        }
        return temp;
    }

    public static String getNationByCode(String code) {
        String nation = "汉族";
        if (StringUtils.isNotEmpty(code)) {
            String key = null;
            if (code.length() == 1) {
                key = "0" + code;
            } else if (code.length() == 2) {
                key = code;
            }
            if (key != null) {
                for (String nationName : nationMap.keySet()) {
                    if (key.equals(nationMap.get(nationName))) {
                        nation = nationName;
                        break;
                    }
                }
            }
        }
        return nation;
    }


    /**
     * 根据民族名字获取编号
     *
     * @param nation 民族， 如：汉
     * @return
     */
    public static String getNationCodeByName(String nation) {
        String code = "01";
        if (nationMap.containsKey(nation)) {
            code = nationMap.get(nation);
        } else if (nationMap.containsKey(nation + "族")) {
            code = nationMap.get(nation + "族");
        }
        return code;
    }

    private static final Map<String, String> nationMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("汉族", "01");
            put("蒙古族", "02");
            put("回族", "03");
            put("藏族", "04");
            put("维吾尔族", "05");
            put("苗族", "06");
            put("彝族", "07");
            put("壮族", "08");
            put("布依族", "09");
            put("朝鲜族", "10");
            put("满族", "11");
            put("侗族", "12");
            put("瑶族", "13");
            put("白族", "14");
            put("土家族", "15");
            put("哈尼族", "16");
            put("哈萨克族", "17");
            put("傣族", "18");
            put("黎族", "19");
            put("傈僳族", "20");
            put("佤族", "21");
            put("畲族", "22");
            put("高山族", "23");
            put("拉祜族", "24");
            put("水族", "25");
            put("东乡族", "26");
            put("纳西族", "27");
            put("景颇族", "28");
            put("柯尔克孜族", "29");
            put("土族", "30");
            put("达斡尔族", "31");
            put("仫佬族", "32");
            put("羌族", "33");
            put("布朗族", "34");
            put("撒拉族", "35");
            put("毛难族", "36");
            put("仡佬族", "37");
            put("锡伯族", "38");
            put("阿昌族", "39");
            put("普米族", "40");
            put("塔吉克族", "41");
            put("怒族", "42");
            put("乌孜别克族", "43");
            put("俄罗斯族", "44");
            put("鄂温克族", "45");
            put("崩龙族", "46");
            put("保安族", "47");
            put("裕固族", "48");
            put("京族", "49");
            put("塔塔尔族", "50");
            put("独龙族", "51");
            put("鄂伦春族", "52");
            put("赫哲族", "53");
            put("门巴族", "54");
            put("珞巴族", "55");
            put("基诺族", "56");
            put("其他", "97");
            put("外国血统", "98");
        }
    };
}
