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

import java.util.regex.Pattern;

/**
 * 正则表达式
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年6月30日 上午9:05:51
 *          Copyright 2015 by DigitalChina
 */
public class RegexUtils {

    /**
     * 带长度验证的整数正则表达式
     */
    public static final String REGEX_WHOLE_NUMBER = "^-?\\d{1,%s}$";

    /**
     * 带长度验证的浮点数正则表达式
     */
    public static final String REGEX_FLOATING_NUMBER = "^-?\\d{1,%s}(?:\\.\\d{1,%s})?$";

    /**
     * 日期格式正则表达式
     */
    public static final Pattern REGEX_DATE = Pattern
            .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s+[0-5][0-9]:[0-5][0-9]:[0-5][0-9])?");

    /**
     * 去除引号正则表达式
     */
    public static final Pattern REGEX_QUOTES = Pattern.compile("\t|\r|\n|^[\"]*|[\"]*$");

    /**
     * 单纯的浮点数正则表达式
     */
    public static final Pattern REGEX_NUMBER_WITHOUT_LENGTH = Pattern.compile("^(-?\\d+)(\\.\\d+)?");

    /**
     * 去除回车空格换行正则表达式
     */
    public static final Pattern REGEX_BLANK = Pattern.compile("\t|\r|\n|^[　 ]*|[　 ]*$");

    public static final Pattern REGEX_CHI = Pattern.compile("[\u4e00-\u9fa5]");

    public static final Pattern REGEX_NUM = Pattern.compile("[0-9]*");

    public static final Pattern REGEX_NUMBER = Pattern.compile("-?[0-9]*");

    public static final Pattern REGEX_NUM_FOUR = Pattern.compile("^-?\\d{1,4}.0$");

    /**
     * IP地址
     */
    public static final Pattern REGEX_IP_ADDRESS = Pattern
            .compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");

    /**
     * 电话
     */
    public static final Pattern REGEX_PHONE = Pattern
            .compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");

    /**
     * 邮箱
     */
    public static final Pattern REGEX_EMAIL = Pattern
            .compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

}
