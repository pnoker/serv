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
package com.dc.city.common.jedis.support;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.dc.city.common.context.ApplicationContextUtils;

/**
 * 监听事件 作用是启动监控线程池并执行持久化线程
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年9月22日 上午10:17:38
 *          Copyright 2015 by DigitalChina
 */
public class RedisListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 新建监控线程池
        RedisMonitorThread redisMonitorThread = new RedisMonitorThread();
        // 启动监控线程
        new Thread(redisMonitorThread).start();

        // 判断是佛需要启动持久化线程

        if (RedisSupport.isStart) {
            // 创建持久化线程
            RedisPersistenceThread persistenceThread = (RedisPersistenceThread) ApplicationContextUtils
                    .getService("redisPersistenceThread");
            // 设置到静态redis的监控程序中
            RedisMonitorThread.redisPersistenceThread = persistenceThread;
            // 启动线程
            new Thread(persistenceThread).start();
        }

    }
}
