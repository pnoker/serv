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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 读取配置文件的公共类
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年7月27日 上午10:20:09
 *          Copyright 2015 by DigitalChina
 */
public class ReadPropertiesUtil {
    private static final Log logger = LogFactory.getLog(ReadPropertiesUtil.class);

    private static Properties CONFIG_PROPERTIES = null;
    /**
     * 定义配置文件的路径
     */
    private static String filePath = "/config.properties";

    /**
     * 读取错误信息
     *
     * @return
     * @author xutaog 2015年7月27日
     */
    static {

        try (InputStreamReader in = new InputStreamReader(ReadPropertiesUtil.class.getResourceAsStream(filePath),
                "UTF-8")) {
            CONFIG_PROPERTIES = new Properties();

            CONFIG_PROPERTIES.load(in);
        } catch (IOException e) {
            logger.debug("错误信息：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取配置文件by key
     *
     * @param key
     * @return
     * @author xutaog 2015年8月17日
     */
    public static String acquirePropertiesByKey(String key) {
        String pro = "";
        try {
            pro = CONFIG_PROPERTIES.getProperty(key);
        } catch (Exception e) {
            logger.error("错误信息：" + e.getMessage());
            // TODO: handle exception
        }
        return pro;
    }

    /**
     * 取得验证权限没有通过的跳转Url地址
     *
     * @return
     * @author xutaog 2015年7月27日
     */
    public static String acquireAuthFailureRedirectUrl() {

        if (CONFIG_PROPERTIES != null) {
            return CONFIG_PROPERTIES.getProperty("authFailureRedirectUrl");
        }
        return null;
    }

    /**
     * 取得验证权限没有通过的跳转Url地址
     *
     * @return
     * @author xutaog 2015年7月27日
     */
    public static String acquireLoginUrl() {
        if (CONFIG_PROPERTIES != null) {
            String loginUrl = CONFIG_PROPERTIES.getProperty("chickLoginUrl");// 这部分是可变的IP与服务端口
            return loginUrl;
        }
        return null;
    }

    /**
     * 获取配置文件
     *
     * @param key
     * @return
     * @author xutaog 2015年8月4日
     */
    public static String acquirePropertyValueByKey(String key) {
        try {
            String valStr = CONFIG_PROPERTIES.getProperty(key);// 这部分是可变的IP与服务端口
            return valStr;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 获取有效的登录IP数组
     *
     * @return
     * @author xutaog 2015年7月30日
     */
    public static String[] acquireEffectiveLoginIp() {
        if (CONFIG_PROPERTIES != null) {
            String logionIps = CONFIG_PROPERTIES.getProperty("auth.login.ip");// 有效的ip
            String[] effectiveIpArr = StringUtils.isNullOrEmpty(logionIps) ? null : logionIps.split(";");
            return effectiveIpArr;
        }
        return null;
    }
}
