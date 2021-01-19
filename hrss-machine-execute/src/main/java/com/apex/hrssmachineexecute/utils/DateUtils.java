package com.apex.hrssmachineexecute.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 日期转换工具
 *
 * @Description:
 * @author: liuzhimin
 * @date: 2019年5月8日 下午7:36:34
 * @version: 1.0
 */
public class DateUtils {
    private DateUtils() {
        // do nothings
    }

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_PATTERN_MONTH = "yyyy-MM";

    public static final String DATE_PATTERN_S = "yyyyMMdd";

    public static final String DATE_TIME = "HH:mm:ss";

    public static final int YESTER_DAY = -1;

    public static final int NEXT_DAY = 1;

    public static Date parse(String str, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(str, new ParsePosition(0));
        } catch (Exception e) {
            return null;
        }
    }

    public static String format(Date date, String pattern) {
        try {
            return org.apache.commons.lang3.time.DateFormatUtils.format(date, pattern);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return the current date
     *
     * @return － DATE<br>
     * @author lizhihong
     */
    public static Date getCurrentDate() {
        return toDate(LocalDateTime.now());
    }

    /**
     * 获取自定义格式的当前系统日期
     *
     * @return
     */
    public static Date getCurrentDate(String parsePattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(parsePattern);
        return parse(df.format(LocalDateTime.now()), parsePattern);
    }

    /**
     * @param date 日期
     * @Description 获取明天的日期 yyyy-MM-dd
     * @Return java.lang.String
     * @Author lizhihong
     * @Date 2020-03-09 19:36
     **/
    public static String getNextDay(Date date, int day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        return formatter.format(date);
    }

    /**
     * 获取自定义格式的当前系统日期
     *
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @return
     */
    public static Date getCurrentDate(int dayOfMonth) {
        LocalDate date = LocalDate.now();
        LocalDate localDate = LocalDate.of(date.getYear(), date.getMonth(), dayOfMonth);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date toDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取7天的日期
     *
     * @return
     */
    public static List<String> getDateScopeWithWeek(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        List<String> list = new ArrayList<>(7);
        list.add(dtf.format(now.plusDays(-6)));
        list.add(dtf.format(now.plusDays(-5)));
        list.add(dtf.format(now.plusDays(-4)));
        list.add(dtf.format(now.plusDays(-3)));
        list.add(dtf.format(now.plusDays(-2)));
        list.add(dtf.format(now.plusDays(-1)));
        list.add(dtf.format(now));
        return list;
    }

    /**
     * 获取本月的所有天数
     *
     * @param pattern 日期格式
     * @return 天数集合
     */
    public static List<String> getDayForMonth(String pattern) {
        List<String> dateScopeList = new ArrayList<>();
        LocalDate localDate = LocalDate.now().plusMonths(0);
        localDate = localDate.with(TemporalAdjusters.firstDayOfMonth());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDate endLocalDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
        dateScopeList.add(dtf.format(localDate));
        int max = 32;
        for (int i = 1; i < max; i++) {
            dateScopeList.add(dtf.format(localDate.plusDays(i)));
            if (localDate.plusDays(i).isEqual(endLocalDate)) {
                break;
            }
        }
        return dateScopeList;
    }

    public static List<String> getNearly30Day(String pattern) {
        List<String> dateScopeList = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        int maxDay = 31;
        for (int i = 1; i < maxDay; i++) {
            dateScopeList.add(dtf.format(localDate.plusDays(-i)));
        }
        Collections.reverse(dateScopeList);
        return dateScopeList;
    }

    /**
     * 获取前六个月的时间集合
     *
     * @param pattern 日期格式
     * @return 前六个月的时间集合
     */
    public static List<String> getFirstSixMonth(String pattern) {
        return getFirstMonth(6, pattern);
    }

    public static List<String> getFirstMonth(int n, String pattern) {
        List<String> dateScopeList = new ArrayList<>();
        LocalDate localDate = LocalDate.now().plusMonths(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        dateScopeList.add(dtf.format(localDate));
        for (int i = 1; i < n; i++) {
            dateScopeList.add(dtf.format(localDate.plusMonths(-i)));
        }
        Collections.reverse(dateScopeList);
        return dateScopeList;
    }

    /**
     * <Description> 获取前xxx分钟字符串时间 <br>
     *
     * @param currentDate 时间字符串
     * @param minute      分钟 <br>
     * @return java.lang.String <br>
     * @author Lizhihong <br>
     * @createDate 2019-12-16 10:55 <br>
     **/
    public static String getFirstSixMinute(String currentDate, int minute) {
        if (StringUtils.isBlank(currentDate)) {
            return currentDate;
        }
        String newStrDate = currentDate;
        long num = 1000 * 60 * minute;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            long time = sdf.parse(newStrDate).getTime();
            long newTime = time - num;
            Date date = new Date(newTime);
            newStrDate = format(date, DATE_TIME_PATTERN);
        } catch (Exception e) {
            //
        }
        return newStrDate;
    }

    public static Date getDateByAge(int age) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -age);
        Date y = c.getTime();
        return y;
    }

    public static int getAgeByDate(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }


    /**
     * 通过
     *
     * @param tempDay
     * @return
     */
    public static String getDateStrByDays(int tempDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        //                      此处修改为+1则是获取后一天
        calendar.set(Calendar.DATE, day + tempDay);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    public static Integer getMonth() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        return StringParser.toInteger(StringParser.join(year, StringParser.leftPad(month, 2)));
    }

    /**
     * @param month 偏移量 如：1 表示 上个月 0：当前月
     * @Description 根据偏移量获取年月(只可以获取 < = 当前月)
     * @Return java.lang.String
     * @Author lizhihong
     * @Date 2020-05-09 12:37
     **/
    public static String getYearMonth(int month) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_PATTERN_MONTH);
        String payoffYearMonth = getNextDay(getCurrentDate(), 0);
        Date currentDate = DateUtils.parse(payoffYearMonth, DateUtils.DATE_PATTERN_MONTH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
        return sd.format(calendar.getTime());
    }

    /**
     * @param minute 分钟
     * @Description 获取当前时间减去自定义分钟前的时间
     * @Return java.util.Date
     * @Author lizhihong
     * @Date 2020-05-14 11:49
     **/
    public static Date getCurrentDateBeforeDate(long minute) {
        long time = minute * 60 * 1000;
        return new Date(DateUtils.getCurrentDate().getTime() - time);
    }

    /**
     * @param minute 分钟
     * @Description 获取当前时间减去自定义分钟后的时间
     * @Return java.util.Date
     * @Author lizhihong
     * @Date 2020-05-14 11:49
     **/
    public static Date getCurrentDateAfterDate(long minute) {
        long time = minute * 60 * 1000;
        return new Date(DateUtils.getCurrentDate().getTime() + time);
    }


    /**
     * 获取指定年月的第一天
     *
     * @param yearMonth 2020-01
     * @return yyyy-MM-dd
     */
    public static String getFirstDayOfMonth(String yearMonth) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int index = yearMonth.indexOf("-");
        if (StringUtils.isNotBlank(yearMonth) && index == 4) {
            year = Integer.parseInt(yearMonth.substring(0, index));
            month = Integer.parseInt(yearMonth.substring(index + 1));
        }

        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        return format(cal.getTime(), DATE_PATTERN);
    }

    /**
     * 获取指定年月的最后一天
     *
     * @param yearMonth 2020-31
     * @return yyyy-MM-dd
     */
    public static String getLastDayOfMonth(String yearMonth) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int index = yearMonth.indexOf("-");
        if (StringUtils.isNotBlank(yearMonth) && index == 4) {
            year = Integer.parseInt(yearMonth.substring(0, index));
            month = Integer.parseInt(yearMonth.substring(index + 1));
        }
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        return format(cal.getTime(), DATE_PATTERN);
    }


}
