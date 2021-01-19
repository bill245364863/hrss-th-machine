package com.apex.hrssmachineexecute.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串转换类
 *
 * @Description:
 * @author: liuzhimin
 * @date: 2019年5月8日 下午7:42:59
 * @version: 1.0
 */
public class StringParser {
    private StringParser() {
        // do nothing
    }

    public static BigDecimal toBigDecimal(Object value) {
        return toBigDecimal(value, null);
    }

    public static BigDecimal toBigDecimal(Object value, BigDecimal defvalue) {
        if (value == null) {
            return defvalue;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return defvalue;
        }
    }

    public static Integer toInteger(Object value) {
        return toInteger(value, null);
    }

    public static Integer toInteger(Object value, Integer defvalue) {
        if (value != null && StringParser.isNumeric(value)) {
            return new Integer(value.toString());
        }
        return defvalue;
    }

    public static Long toLong(Object value) {
        return toLong(value, null);
    }

    public static Long toLong(Object value, Long defvalue) {
        if (value == null) {
            return defvalue;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        try {
            return new Long(value.toString());
        } catch (Exception e) {
            return defvalue;
        }
    }

    public static Double toDouble(Object value) {
        return toDouble(value, null);
    }

    public static Double toDouble(Object value, Double defvalue) {
        if (value == null) {
            return defvalue;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        try {
            return new Double(value.toString());
        } catch (Exception e) {
            return defvalue;
        }
    }

    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        char[] chars = obj.toString().toCharArray();
        int length = chars.length;
        if (length < 1) {
            return false;
        }

        int i = 0;
        String splitTmp = "-";
        String s = String.valueOf(chars[0]);
        if (length > 1 && splitTmp.equals(s)) {
            i = 1;
        }

        for (; i < length; i++) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }
        return true;
    }

    public static Boolean toBoolean(Object value) {
        return toBoolean(value, null);
    }

    public static Boolean toBoolean(Object value, Boolean defvalue) {
        if (value == null) {
            return defvalue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        try {
            return new Boolean(value.toString());
        } catch (Exception e) {
            return defvalue;
        }
    }

    public static String join(Object... objs) {
        StringBuilder result = new StringBuilder();
        for (Object obj : objs) {
            result.append(obj == null ? "" : obj.toString());
        }
        return result.toString();
    }

    public static String joinWithSplit(String split, Object[] arg) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arg) {
            String str = toString(obj);
            if (isEmpty(str)) {
                continue;
            }
            sb.append(obj);
            sb.append(split);
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        String nullStr = "null";
        if (nullStr.equals(value)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(value.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static String toString(Object value) {
        if (value == null) {
            return "";
        } else {
            return value.toString().trim();
        }
    }

    public static String leftPad(int value, int length) {
        String v = toString(value);
        if (v.length() >= length) {
            return v;
        } else {
            return String.format(join("%1$0", (length - v.length()), "d"), 0) + value;
        }
    }

    /**
     * 工具-字符串-转换Sql查询IN中使用的格式
     * 效果：a,b==>'a','b'
     *
     * @param str
     * @return
     */
    public static String strToDbin(String str) {
        if (StringUtils.isNotBlank(str)) {
            return String.format("\"%s\"", StringUtils.join(str.split(","), "\",\""));
        } else {
            return "";
        }
    }


    /**
     * 工具-List<String>-转换Sql查询IN中使用的格式
     * 效果：'a','b'
     *
     * @param str
     * @return
     */
    public static String listToDbin(List<String> list) {
        if (list != null && list.size() > 0) {
            return String.format("\"%s\"", StringUtils.join(list.toArray(), "\",\""));
        } else {
            return "";
        }
    }


    /**
     * 判断给定字串是否符合给定正则
     *
     * @param regex
     * @param str
     * @return
     */
    public static boolean test(String regex, String str) {
        if (isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
