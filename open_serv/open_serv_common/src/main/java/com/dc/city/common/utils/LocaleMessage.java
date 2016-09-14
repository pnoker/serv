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

import java.util.ResourceBundle;

/**
 * 读取配置文件
 *
 * @author zy
 * @version V1.0 创建时间：2015年5月6日 下午9:08:57
 *          Copyright 2015 by DigitalChina
 */
public class LocaleMessage {

    private static final String BASE_BUNDLE_NAME = "config";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BASE_BUNDLE_NAME);

    /**
     * 通过key获取配置文件里面的值
     *
     * @param key
     * @return
     * @author zy 2015年5月13日
     */
    public static String getValueByKey(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * 通过key获取配置文件里面的值，未获取到则返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     * @author zy 2015年5月13日
     */
    public static String getValueByKey(String key, String defaultValue) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (Exception e) {
        }
        return defaultValue;
    }

}
