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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.dc.city.common.utils.ConfigUtils;
import com.dc.city.common.utils.ReadPropertiesUtil;
import com.dc.city.common.utils.StringUtils;

/**
 * Redis支撑类
 *
 * @author sunjl
 * @version V1.0 创建时间：2015年5月5日 上午9:31:46
 *          Copyright 2015 by DigitalChina
 */
@Component
public class RedisSupport implements InitializingBean {

    protected static JedisPool pool = new JedisPool(new JedisPoolConfig(),
            ConfigUtils.getString("redis.server.ip", ""), Integer.valueOf(ConfigUtils.getString("redis.server.port",
                    "6379")));
    // 3 month 60 * 60 * 24 * 30 * 3
    protected final static int defaultExpireSecond = 7776000;
    // 是否存活
    public static boolean isAlive = true;

    // 是否启动持久化线程
    public static boolean isStart = false;
    // 删除队列
    protected static Queue<String> needDelQueue = new LinkedTransferQueue<>();
    protected static Queue<String> needDelPatternsQueue = new LinkedTransferQueue<>();
    protected static RedisService redisService;
    static {
        RedisService redisServiceTemp = new RedisServiceImpl();
        InvocationHandler redisUtilProxy = new RedisUtilProxy(redisServiceTemp);
        redisService = (RedisService) Proxy.newProxyInstance(redisUtilProxy.getClass().getClassLoader(),
                redisServiceTemp.getClass().getInterfaces(), redisUtilProxy);
        String startStr = ReadPropertiesUtil.acquirePropertiesByKey("redis.persistence.start");
        if (!StringUtils.isNullOrEmpty(startStr)) {
            isStart = Boolean.parseBoolean(startStr);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
