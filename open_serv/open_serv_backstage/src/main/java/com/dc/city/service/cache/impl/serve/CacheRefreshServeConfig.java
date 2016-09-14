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
package com.dc.city.service.cache.impl.serve;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.city.domain.config.ServeConfig;
import com.dc.city.listener.support.ServeConfigCache;
import com.dc.city.service.cache.handler.ICacheRefresh;
import com.dc.city.service.config.ConfigerService;

/**
 * 刷新服务本地信息，从redis中读取配置服务，如果没取到从数据库读，最后同步到本地内存
 * 服务处理的服务查询数据源
 * 通知里面的data在服务配置更新中表示的是服务配置的serviceCode
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月28日 上午11:06:15
 *          Copyright 2016 by DigitalChina
 */
public class CacheRefreshServeConfig implements ICacheRefresh {

    private static Logger logger = LoggerFactory.getLogger(CacheRefreshServeConfig.class);

    @Resource
    private ConfigerService configService;

    /**
     * 新增服务加载到本地
     */
    @Override
    public void cacheAdd(String serviceCode) {
        //先从redis查询，查不到再到数据库查询
        ServeConfig config = configService.getServerConfigFromCache(serviceCode);
        if (config != null) {
            ServeConfigCache.getInstance().addConfigCache(serviceCode, config);
        }
        logger.info("刷新服务配置成功");
    }

    @Override
    public void cacheEdit(String serviceCode) {
        // 调用新增方法，存在就会覆盖之前的配置
        cacheAdd(serviceCode);
    }

    /**
     * 删除指定的服务信息
     */
    @Override
    public void cacheDelete(String serviceCode) {
        ServeConfigCache.getInstance().delConfigCache(serviceCode);
        logger.info("刷新服务配置成功");
    }

}
