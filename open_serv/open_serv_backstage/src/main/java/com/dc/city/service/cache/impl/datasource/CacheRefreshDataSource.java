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
package com.dc.city.service.cache.impl.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.common.exception.BusinessException;
import com.dc.city.domain.datasource.ServeDataSource;
import com.dc.city.service.cache.handler.ICacheRefresh;
import com.dc.city.service.datasource.ServeDataSourceService;

/**
 * 通过订阅通知的形式刷新数据源本地信息
 * data:数据源ID
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月28日 上午11:06:15
 *          Copyright 2016 by DigitalChina
 */
public class CacheRefreshDataSource implements ICacheRefresh {

    private Logger logger = LoggerFactory.getLogger(CacheRefreshDataSource.class);

    @Resource
    private DynamicDataSource dynamicSource;

    @Resource
    private ServeDataSourceService dataSourceService;

    /**
     * 动态加载数据源信息
     * data:数据源id
     */
    @Override
    public void cacheAdd(String data) {
        Integer id = 0;
        try {
            id = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            logger.error("添加数据源失败:数据源id类型不正常");
            return;
        }
        // 小于0，则直接返回不进行任何处理
        if (id <= 0) {
            return;
        }
        // 查询数据源对象
        ServeDataSource sourceBean = dataSourceService.find(id);
        if (sourceBean == null) {
            return;
        }
        // 封装连接属性properties
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("jdbcUrl", sourceBean.getSourceUrl());
        properties.put("driverClassName", sourceBean.getSourceClass());
        properties.put("username", sourceBean.getSourceUser());
        properties.put("password", sourceBean.getSourcePass());

        try {
            dynamicSource.addDataSource(sourceBean.getId() + "", properties);
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 更新数据源消息
     * data:数据源id
     */
    @Override
    public void cacheEdit(String data) {
        Integer id = 0;
        try {
            id = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            logger.error("编辑数据源失败:数据源id类型不正常");
            return;
        }

        if (id <= 0) {
            return;
        }
        // 获取到数据源配置信息
        ServeDataSource sourceBean = dataSourceService.find(id);
        if (sourceBean == null) {
            return;
        }

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("jdbcUrl", sourceBean.getSourceUrl());
        properties.put("driverClassName", sourceBean.getSourceClass());
        properties.put("username", sourceBean.getSourceUser());
        properties.put("password", sourceBean.getSourcePass());
        try {
            dynamicSource.modifyDataSource(sourceBean.getId() + "", properties);
        } catch (BusinessException e) {
            logger.warn("刷新数据源失败:" + e.getMessage());
        }

    }

    /**
     * 删除数据源
     * key:数据源uniqueKey
     */
    @Override
    public void cacheDelete(String key) {
        try {
            dynamicSource.removeDataSourceByKey(key);
        } catch (BusinessException e) {
            logger.error("远程服务器刷新数据源失败:" + e.getMessage());
        }

    }

}
