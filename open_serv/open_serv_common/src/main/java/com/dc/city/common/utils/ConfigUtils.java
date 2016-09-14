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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * 读取配置文件工具类
 *
 * @author ligen
 * @version V1.0 创建时间：2015年8月10日 下午5:26:09
 *          Copyright 2015 by dcits
 */
public class ConfigUtils {

    /**
     * 默认人的配置文件路径
     */
    private static String DEFAULT_CONFIG_FILE = "/config.properties";

    // private static final Log LOG = LogFactory.getLog(ConfigUtils.class);

    private static Map<String, String> propertiesMap;

    static {
        initProperty();
    }

    /**
     * 初始化配置文件(config.properties)<br/>
     * config.properties配置文件读取路径有优先级为 /config.properties > /conf/config.properties >
     * ./config.properties <br/>
     * 如果在/config.properties 或 /conf/config.properties配置文件中未找到相应的配置项目，将会采用默认的配置，也就是当前路径下的
     * config.properties
     */
    private static void initProperty() {
        if (propertiesMap != null)
            return;
        loadDefaultProperty();
    }

    /**
     * 加载默认文件
     *
     * @author ligen 2015年7月30日
     */
    private static void loadDefaultProperty() {
        InputStream ins = null;
        Properties properties = new Properties();
        try {
            ins = ConfigUtils.class.getResourceAsStream(DEFAULT_CONFIG_FILE);
            properties.load(ins);

        } catch (FileNotFoundException e) {
            // LOG.info("file not found." + DEFAULT_CONFIG_FILE, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            // LOG.info(e);
            throw new RuntimeException(e);
        }
        propertiesMap = new HashMap<String, String>();
        Set<Entry<Object, Object>> entrySet = properties.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            propertiesMap.put((String) entry.getKey(), ((String) entry.getValue()).trim());
        }
    }

    public static String getString(String proKey, String defaultValue) {
        String value = propertiesMap.get(proKey);
        // System.out.println("prokey:"+proKey+"   value:"+value);
        return StringUtils.isNullOrEmpty(value) ? defaultValue : value;
    }

    public static Integer getInteger(String proKey, Integer defaultValue) {
        String value = propertiesMap.get(proKey);
        try {
            Integer retValue = Integer.parseInt(value);
            return retValue == null ? defaultValue : retValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static final String getWebRoot() {

        URL url = ConfigUtils.class.getResource("/");
        String path = url.getPath();
        if (path.endsWith("/WEB-INF/classes/")) {
            int beginIndex = path.length() - "WEB-INF/classes/".length();
            return path.substring(0, beginIndex);
        }
        return path;
    }

    /**
     * 网站的绝对路径
     */
    public static final String WEB_ROOT = getWebRoot();
    /**
     * 网站上传图片的路径
     */
    public static final String UPLOAD_IMAGE_PATH = getString("upload.image.path", "/upload_file/image");

}
