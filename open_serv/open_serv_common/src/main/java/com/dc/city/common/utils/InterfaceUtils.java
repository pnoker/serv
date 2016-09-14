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

/**
 * 接口状态码
 *
 * @author zy
 * @version V1.0 创建时间：2015年5月18日 上午9:55:09
 *          Copyright 2015 by DigitalChina
 */
public class InterfaceUtils {

    /**
     * 接口调用成功状态码
     */
    public static final String SUCCESS_CODE = "0";
    /**
     * 接口调用成功信息
     */
    public static final String SUCCESS_MSG = "查询成功";
    /**
     * 接口调用失败状态码
     */
    public static final String ERROR_CODE = "1";
    /**
     * 网状图findIndicatorStructure接口错误信息
     */
    public static final String INDI_FILL_ARE_ERROR_MSG = "填充区域值失败";
    /**
     * 网状图findIndicatorStructure接口错误信息
     */
    public static final String INDI_NOT_FIND_ARE_ERROR_MSG = "未查询到区域值";
    /**
     * 网状图findIndicatorStructure接口错误信息
     */
    public static final String INDI_NOT_FIND_TREE_ERROR_MSG = "未查询到指标组织结构";
    /**
     * 网状图允许查询多少天的记录数
     */
    public static final String EXCEED_DAY = LocaleMessage.getValueByKey("indicator.exceedDay", "400");
    /**
     * 网状图findIndicatorStructure接口错误信息
     */
    public static final String INDI_EXCEED_DAY_ERROR_MSG = "只能查询" + EXCEED_DAY + "天以内的数据";

}
