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
package com.dc.city.listener.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dc.city.common.exception.BusinessException;
import com.dc.city.domain.config.ServeConfig;

/**
 * 服务配置信息，本地缓存  singleton
 * 所有服务请求用到的配置信息从本地缓存加载，减少与redis压力
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月25日 下午5:07:11
 *          Copyright 2016 by DigitalChina
 */
public class ServeConfigCache {
    public static boolean isLoading() {
        return isLoading;
    }

    public static void setLoading(boolean isLoading) {
        ServeConfigCache.isLoading = isLoading;
    }

    private static ServeConfigCache instance = new ServeConfigCache();

    private ServeConfigCache() {}

    // 配置缓存容器
    private Map<String, ServeConfig> configCache = new ConcurrentHashMap<String, ServeConfig>();
    // 缓存初始化状态 正在加载
    private static boolean isLoading = false;

    public static ServeConfigCache getInstance() {
        return instance;
    }

    /**
     * 
     * 获取本地缓存中的服务配置
     *
     * @param 服务配置code
     * @return 
     * @throws BusinessException 正在初始化时返回
     * @author zhongdt 2016年5月27日
     */
    public ServeConfig getConfig(String key) throws BusinessException {
        if (configCache.containsKey(key)) {
            return configCache.get(key);
        } else if (isLoading) {
            throw new BusinessException("服务配置信息正在初始化，请稍后");
        }
        return null;
    }

    public int getCacheSize() {
        return this.configCache.size();
    }

    public void addConfigCache(String key, ServeConfig config) {
        if (key == null) {
            return;
        }
        this.configCache.put(key, config);
    }

    public void delConfigCache(String key) {
        if (key == null) {
            return;
        }
        this.configCache.remove(key);
    }

    public void setConfigCache(Map<String, ServeConfig> configCache) {
        this.configCache = configCache;
    }
}