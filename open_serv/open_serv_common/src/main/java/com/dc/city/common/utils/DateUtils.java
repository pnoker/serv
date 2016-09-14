/**
 * Copyright (DigitalChina) 2016-2020, DigitalChina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dc.city.common.utils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import oracle.sql.TIMESTAMP;

import org.apache.commons.lang3.StringUtils;

/**
 * 工具类
 *
 * @author zy
 * @version V1.0 创建时间：2015年5月10日 上午11:18:57
 *          Copyright 2015 by DigitalChina
 */
public class DateUtils {
    public static final String DATE_FORMAT_1 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_8 = "yyyy年MM月dd";
    public static final String DATABASE_DATE_FORMAT_1 = "yyyy-MM-dd hh24:mi:ss";
    public static final String DATE_FORMAT_3 = "HH:mm:ss";
    public static final String DATE_FORMAT_4 = "yyyy.MM.dd";
    public static final String DATE_FORMAT_5 = "yyyy-MM-dd HH";
    public static final String DATE_FORMAT_6 = "yyyyMMddHHmm";
    public static final String DATE_FORMAT_7 = "yyyy-MM-dd HH;mm";
    public static final String DATE_FORMAT_9 = "yyyy-MM";
    public static final String CONNECTOR_1 = ",";
    public static final String CONNECTOR_2 = ":";
    public static final String MAX_SECONDS = " 23:59:59";
    public static final String MIN_SECONDS = " 00:00:00";

    /**
     * 获得本月的开始时间，如2012-01-01 00:00:00
     *
     * @return
     * @author xutaog 2015年8月13日
     */
    public static Date getCurrentMonthStartTime() {
        Calendar calendar = Calendar.getInstance();
        Date now = null;
        try {
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            now = calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取某个小时之前的值
     *
     * @param date
     * @return
     * @author xutaog 2015年8月19日
     */
    public static Date getHourAgoStartTime(Date date) {
        // 先得到现在时间一小时以前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 得到某个日期的当前年12月的日期数组
     *
     * @param csDate
     * @return
     * @author xutaog 2015年10月19日
     */
    public static List<String> getMonthArrByDate(Date csDate) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(csDate);
            calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date date = calendar.getTime();
            result.add(format(date, DATE_FORMAT_9));
        }
        return result;
    }

    /**
     * 获取今年的当前的
     *
     * @return
     * @author xutaog 2015年10月19日
     */
    public static List<String> getCurrentYearMonth() {
        return getMonthArrByDate(new Date());
    }

    /**
     * 获取当前期之前的月份
     *
     * @return
     * @author xutaog 2015年11月17日
     */
    public static List<String> getCurrentMonth() {
        List<String> result = new ArrayList<String>();
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        for (int i = 0; i <= now.get(Calendar.MONTH); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date date = calendar.getTime();
            result.add(format(date, DATE_FORMAT_9));
        }
        return result;
    }

    /**
     * 获取当前时间
     *
     * @param day
     * @return
     * @author xutaog 2015年8月14日
     */
    public static Date getDate(int day) {
        Calendar calendar = Calendar.getInstance();
        Date now = null;
        try {
            calendar.add(Calendar.DATE, -day);
            now = calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取当前时是当前年的第几天
     *
     * @param date
     * @return
     * @author xutaog 2015年10月14日
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取当前时间之前的时间
     * 
     * @param hour
     * @return
     * @author xutaog 2015年8月14日
     */
    public static Date getBeforeDateTime(int hour) {

        Calendar calendar = Calendar.getInstance();
        Date now = null;
        try {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
            now = calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;

    }

    /**
     * 获取某个日期的起始时间
     *
     * @param date
     * @return
     * @author xutaog 2015年8月14日
     */
    public static Date getStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        Date now = null;
        try {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            now = calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取某天的结束时间结束时间
     *
     * @param date
     * @return
     * @author xutaog 2015年8月14日
     */
    public static Date getEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        Date now = null;
        try {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            now = calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 将字符串转为指定格式的日期类型
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     * @author zy 2015年5月10日
     */
    public static Date parse(String dateStr, String pattern) {
        Date date = null;
        try {
            date = StringUtils.isBlank(dateStr) ? null : new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException pe) {

        }
        return date;
    }

    /**
     * 将指定格式的日期类型转为字符串
     *
     * @param date
     * @param pattern
     * @return
     * @author Administrator 2015年5月10日
     */
    public static String format(Date date, String pattern) {
        return date == null ? "" : new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 获取指定日期的x天，x月，x年以前或以后的日期
     *
     * @param date
     * @param model: Calendar.DATE，Calendar.MONTH，
     *            Calendar.DAY_OF_YEAR,Calendar.YEAR，Calendar.DAY_OF_WEEK，Calendar.DAY_OF_MONTH
     * @param n:n为正数往后推；n为负数往前推
     * @return
     * @author zy 2015年5月11日
     */
    public static Date dealDay(Date date, int model, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(model, n);
        return calendar.getTime();
    }

    /**
     * 得到两个时间段相差的天数
     *
     * @param early
     * @param late
     * @return
     * @author zy 2015年5月17日
     */
    public static int daysBetween(String early, String late) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(parse(early, DATE_FORMAT_1));
        caled.setTime(parse(late, DATE_FORMAT_1));
        // 设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        // 得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
        return days;
    }

    /**
     * 得到两个时间段相差的天数
     *
     * @param early
     * @param late
     * @return
     * @author zy 2015年5月17日
     */
    public static int daysBetween(Date early, Date late) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        // 设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        // 得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
        return days;
    }

    /**
     * 获取某个时间的属于第几个月
     *
     * @param date
     * @return
     * @author xutaog 2015年10月14日
     */
    public static int getMonthNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取两个日期之间的所有日期
     *
     * @param early
     * @param late
     * @return
     * @author zy 2015年5月17日
     */
    public static String betweenDays(String early, String late) {
        StringBuffer sBuff = new StringBuffer();
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(parse(early, DATE_FORMAT_1));
        endDay.setTime(parse(late, DATE_FORMAT_1));
        if (startDay.compareTo(endDay) < 0) {
            sBuff.append(early).append(CONNECTOR_1);
            // 现在的日期
            Calendar currentDay = startDay;
            while (true) {
                // 日期加一
                currentDay.add(Calendar.DATE, 1);
                // 日期加一后判断是否达到终了日，达到则终止
                if (currentDay.compareTo(endDay) == 0) {
                    break;
                }
                sBuff.append(format(currentDay.getTime(), DATE_FORMAT_1)).append(CONNECTOR_1);
            }
            sBuff.append(late);
        } else if (startDay.compareTo(endDay) == 0) {
            sBuff.append(early);
        } else {

        }
        return sBuff.toString();
    }

    /**
     * 当天处于年月周中的第几天
     *
     * @param Calendar.DATE，Calendar.MONTH，
     *            Calendar.DAY_OF_YEAR,Calendar.YEAR，Calendar.DAY_OF_WEEK，Calendar.DAY_OF_MONTH
     * @return
     * @author zuoyue 2015年6月2日
     */
    public static String calculateDate(int model) {
        return String.valueOf(Calendar.getInstance().get(model));
    }

    /**
     * 今年第一天
     *
     * @return
     * @author zuoyue 2015年10月15日
     */
    public static Date getYearFirstDay() {
        return getYearFirstDay(0);
    }

    /**
     * 今年最后一天
     *
     * @return
     * @author zuoyue 2015年10月15日
     */
    public static Date getYearLastDay() {
        return getYearLastDay(0);
    }

    /**
     * n年之前或之后的第一天
     *
     * @param n
     * @return
     * @author zuoyue 2015年10月15日
     */
    public static Date getYearFirstDay(int n) {
        return DateUtils.parse(String.valueOf(Integer.parseInt(DateUtils.calculateDate(Calendar.YEAR)) + n) + "-01-01 "
                + DateUtils.MIN_SECONDS, DateUtils.DATE_FORMAT_2);
    }

    /**
     * n年之前或之后的最后一天
     *
     * @param n
     * @return
     * @author zuoyue 2015年10月15日
     */
    public static Date getYearLastDay(int n) {
        return DateUtils.parse(String.valueOf(Integer.parseInt(DateUtils.calculateDate(Calendar.YEAR)) + n) + "-12-31 "
                + DateUtils.MAX_SECONDS, DateUtils.DATE_FORMAT_2);

    }
      
    /**
     * 计算两个日期间相差月数
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     * @author ligen 2016年1月29日
     */
    public static int monthBetween(Date date1, Date date2)
            throws ParseException {
        int result = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        return result == 0 ? 1 : Math.abs(result);

    }
    
    /**
     * 获取两个日期之间月
     *
     * @param early
     * @param late
     * @return
     * @author ligen 2015年5月17日
     */
    public static String betweenMonth(String early, String late) {
        StringBuffer sBuff = new StringBuffer();
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(parse(early, DATE_FORMAT_9));
        endDay.setTime(parse(late, DATE_FORMAT_9));
        if (startDay.compareTo(endDay) < 0) {
            sBuff.append(early).append(CONNECTOR_1);
            // 现在的日期
            Calendar currentDay = startDay;
            while (true) {
                // 日期加一
                currentDay.add(Calendar.MONTH, 1);
                // 日期加一后判断是否达到终了日，达到则终止
                if (currentDay.compareTo(endDay) == 0) {
                    break;
                }
                sBuff.append(format(currentDay.getTime(), DATE_FORMAT_9)).append(CONNECTOR_1);
            }
            sBuff.append(late);
        } else if (startDay.compareTo(endDay) == 0) {
            sBuff.append(early);
        } else {

        }
        return sBuff.toString();
    }
    
    /** 
     * 根据oracle的Timestamp获取字符串日期时间 
     * @param t Timestamp时间 
     * @param formatStr 格式化字符串，如果是null默认yyyy-MM-dd hh:mm:ss 
     * @return 格式化后的字符串 
     */  
    public static String getDateByTimestamp(Object obj, String formatStr) {  
        try {  
            TIMESTAMP t = (TIMESTAMP)obj;  
            if (formatStr == null || formatStr.equals("")) {  
                formatStr = "yyyy-MM-dd hh:mm:ss";  
            }  
            Timestamp tt;  
            tt = t.timestampValue();  
            Date date = new Date(tt.getTime());  
            SimpleDateFormat sd = new SimpleDateFormat(formatStr);  
            return sd.format(date);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return "";  
    }  
    

}
