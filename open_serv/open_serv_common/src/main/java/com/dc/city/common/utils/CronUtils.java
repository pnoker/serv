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

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * 解析cron表达式
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年1月11日 下午3:35:48
 *          Copyright 2016 by DigitalChina
 */
public class CronUtils {

    /**
     * 从cron表达式中生成开始与结束时间
     *
     * @param cronExpression
     * @return
     * @author zuoyue 2016年1月11日
     */
    public static String[] obtainQueryTimeInterval(String beanId) {
        String[] queryTimeInterval = null;
        String cronExpression = obtainCronExpression(beanId);
        int[] analyzeCron = analyzeCron(cronExpression);
        if (analyzeCron != null && analyzeCron.length == 2) {
            queryTimeInterval = new String[2];
            int type = analyzeCron[0];
            int interval = analyzeCron[1];
            if (interval != 0) {
                // 查询开始时间（x秒/分钟/小时/日之前的时间）
                queryTimeInterval[0] = DateUtils.format(DateUtils.dealDay(new Date(), type, -interval),
                        DateUtils.DATE_FORMAT_2);
                //
                // 查询截止时间（当前时间）
                queryTimeInterval[1] = DateUtils.format(new Date(), DateUtils.DATE_FORMAT_2);
            }
        }
        return queryTimeInterval;
    }

    /**
     * 从cron表达式中解析出时间模式与时间区间
     *
     * @param cronExpression
     * @return
     * @author zuoyue 2016年1月11日
     */
    public static int[] analyzeCron(String cronExpression) {
        int[] timeInterval = null;
        if (StringUtils.isNotBlank(cronExpression)) {
            timeInterval = new int[2];
            String timeArr[] = cronExpression.trim().split(" +");
            breakPoint:
            for (int i = 0; i < timeArr.length; i++) {
                if (StringUtils.isNotBlank(timeArr[i]) && timeArr[i].indexOf("/") > 0) {
                    String interval = timeArr[i].split("/")[1];
                    if (StringUtils.isNotBlank(interval)) {
                        switch (i) {
                            case 0:
                                // 秒
                                timeInterval[0] = Calendar.SECOND;
                                timeInterval[1] = Integer.parseInt(interval);
                                break;
                            case 1:
                                // 分钟
                                timeInterval[0] = Calendar.MINUTE;
                                timeInterval[1] = Integer.parseInt(interval);
                                break;
                            case 2:
                                // 小时
                                timeInterval[0] = Calendar.HOUR;
                                timeInterval[1] = Integer.parseInt(interval);
                                break;
                            case 3:
                                // 日
                                timeInterval[0] = Calendar.DATE;
                                timeInterval[1] = Integer.parseInt(interval);
                                break;
                            default:
                                break;
                        }
                    }
                    break breakPoint;
                }
            }
        }
        return timeInterval;
    }

    /**
     * 获取quartz中配置的Cron表达式
     *
     * @param beanId
     * @return
     * @author zuoyue 2016年1月11日
     */
    public static String obtainCronExpression(String beanId) {
        String cronExpression = "";
        if (StringUtils.isNotBlank(beanId)) {
            CronTriggerImpl object = (CronTriggerImpl) ApplicationContextUtil.getBean(beanId);
            cronExpression = object.getCronExpression();
        }
        return cronExpression;
    }

}
