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
package com.dc.city.common.datasource.support;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dc.city.common.context.ApplicationContextUtils;
import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.common.datasource.DynamicDataSource.DbContextHolder;

/**
 * dataSource监控线程
 *
 * @author zhongdt
 * @version V1.0 创建时间：2015年5月5日 上午9:31:46
 *          Copyright 2015 by DigitalChina
 */
@Component

//non-singleton 对象
@Scope("prototype") 
public class DataSourceMonitorThread implements Runnable {
    private static final Log logger = LogFactory.getLog(DataSourceMonitorThread.class);
    // 循环时间间隔
    private long sleepMillis = 60*1000;

    private static DynamicDataSource dataSource;

    public DataSourceMonitorThread() {
        super();
        dataSource = (DynamicDataSource) ApplicationContextUtils.getService("dynamicDataSource");
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    logger.error("dataSourceMonitorThread sleep Interrupted!", e);
                }
                // 数据源监控进程
                dataSourceMonitor();
            }
        } catch (Exception e) {
            logger.error("dataSourceMonitorThread dead, Restarting!", e);
            new Thread((DataSourceMonitorThread) ApplicationContextUtils.getService("dataSourceMonitorThread")).start();
        }
    }

    /**
     * 目前考虑每次从各个数据源取出connection，未报错就是正常
     *
     * @author zhongdt 2016年3月21日
     * @throws SQLException
     */
    private void dataSourceMonitor() {
        List<String> keys = new ArrayList<String>();
        for (Entry<Object, Object> entry : dataSource.getTargets().entrySet()) {
            
            String key = entry.getKey()+"";
            //如果数据源在不可用的列表中则不进行数据库连接，原因是重复操作会锁用户
            if(dataSource.getDisableKeys().contains(key)){
                continue;
            }
            
            DbContextHolder.setDbType(entry.getKey() + "");
            
            //jdk1.7 新特性 自动关闭连接
            try( Connection conn = dataSource.getConnection()) {
                keys.add(key);
            } catch (Exception e) {
                //为了避免数据库被锁只要是有报错，就把数据源信息放到不可用列表中，不在监控，需要在前端通过手动方式查看
                dataSource.getDisableKeys().add(entry.getKey()+"");
                logger.error("checkErrorDataSource, uniqueKey =:"+entry.getKey(),e);
            }
        }
        //重新设置 可用数据源列表
        dataSource.setAvaiableKeys(keys);
    }
}
