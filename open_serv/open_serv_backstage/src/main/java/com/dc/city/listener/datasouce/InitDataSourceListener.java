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
package com.dc.city.listener.datasouce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.common.datasource.support.DataSourceMonitorThread;
import com.dc.city.common.exception.BusinessException;
import com.dc.city.domain.datasource.ServeDataSource;
import com.dc.city.service.datasource.ServeDataSourceService;

/**
 * 从数据库表中读取数据源信息，并加载到服务器
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月7日 下午4:42:02
 *          Copyright 2016 by DigitalChina
 */
public class InitDataSourceListener {

    @Resource
    private DynamicDataSource dynamicSource;    // 动态数据源bean

    @Resource
    private ServeDataSourceService dataSourceService;    // 数据源管理service

    private Log logger = LogFactory.getLog(InitDataSourceListener.class);

    /**
     * 初始化方法
     * 读取数据库正常的数据源信息，并加载到内存中
     * @author zhongdt 2016年3月7日
     */
    @PostConstruct
    public void init() {
        // 构造查询入参对象
        ServeDataSource queryParam = new ServeDataSource();
        queryParam.setIsDeleted(0);
        
        //从数据库获取数据源信息列表
        List<ServeDataSource> dataSourceList = dataSourceService.allList(queryParam);
        if (dataSourceList == null || dataSourceList.isEmpty()) {
            return;
        }
        Map<String, Map<String, String>> dataSourcesMap = new HashMap<String, Map<String, String>>();
        List<String> availableSourceKeys = new ArrayList<String>();
        
        //遍历数据，组装数据源对象map信息
        for (ServeDataSource dbInfo : dataSourceList) {
            // 加载数据源需要的map对象
            Map<String, String> infoMap = new HashMap<String, String>();
            infoMap.put("jdbcUrl", dbInfo.getSourceUrl());
            infoMap.put("driverClassName", dbInfo.getSourceClass());
            infoMap.put("username", dbInfo.getSourceUser());
            infoMap.put("password", dbInfo.getSourcePass());
            dataSourcesMap.put(dbInfo.getId() + "", infoMap);
            availableSourceKeys.add(dbInfo.getId() + "");
        }

        try {
            //加载到动态数据源对象中
            dynamicSource.setDataSource(dataSourcesMap);
        } catch (BusinessException e) {
            logger.error("数据源加载失败:"+e.getMessage());
        }
        //启动数据源监控
        startDataSouceMonitor();

    }

    /**
     * 
     * 开启新线程进行数据源连接状态监控
     *
     * @author zhongdt 2016年5月27日
     */
    private void startDataSouceMonitor() {
        // 新建监控线程池
        DataSourceMonitorThread dataSourceMonitorThread = new DataSourceMonitorThread();
        // 启动监控线程
        new Thread(dataSourceMonitorThread).start();
    }
}
