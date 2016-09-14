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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.dc.city.common.context.ApplicationContextUtils;
import com.dc.city.common.utils.ConfigUtils;


/**
 * Redis监控线程
 *
 * @author sunjl
 * @version V1.0 创建时间：2015年5月5日 上午9:31:46
 *          Copyright 2015 by DigitalChina
 */
@Component
@Scope("prototype")
public class RedisMonitorThread implements Runnable {
    private static final Log LOG = LogFactory.getLog(RedisMonitorThread.class);

    private long sleepMillis = 30000;

    // 持久化线程
    public static RedisPersistenceThread redisPersistenceThread;

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    LOG.error("RedisMonitorThread sleep Interrupted!", e);
                }
                // redis监控线程
                this.serverMonit();
                // 判断是佛需要启动持久化线程
                if (RedisSupport.isAlive == true && RedisSupport.isStart) {
                    LOG.info("Redis Monitor pulse, server is alive, ip=="
                            + ConfigUtils.getString("redis.server.ip", ""));
                    this.persistenceMonit();
                }

            }
        } catch (Exception e) {
            LOG.error("RedisMonitorThread dead, Restarting!", e);
            this.shutdownAll();
            new Thread((RedisMonitorThread) ApplicationContextUtils.getService("redisMonitorThread")).start();
        }
    }

    /**
     * 关闭线程池中线程
     *
     * @author ligen 2015年8月10日
     */
    private void shutdownAll() {
        if (redisPersistenceThread != null) {
            redisPersistenceThread.isRunning = false;
            redisPersistenceThread = null;
        }
    }

    /**
     * 当客户端发送请求给redis服务器时，经过此方法监控，
     * 未连接上的请求加入删除队列等待处理
     *
     * @author ligen 2015年8月10日
     */
    private void serverMonit() {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            if (jedis == null) {
                RedisSupport.isAlive = false;
                this.sleepMillis = 3000;
                LOG.error("can't reach Redis server");
                return;
            }
            jedis.setex("testAlive", 3, "testAlive");

            if (RedisSupport.isAlive == false) {
                this.needDelQueue();
                this.needDelPatternsQueue();
                RedisSupport.isAlive = true;
                this.sleepMillis = 30000;
            }
        } catch (JedisConnectionException e) {
            LOG.error("can't reach Redis server, JedisConnectionException");
            RedisSupport.isAlive = false;
            this.sleepMillis = 3000;
            return;
        }
    }

    /**
     * 检测线程池的数量并启动持久化线程保存线程队列中的实体
     *
     * @author xutaog 2015年9月21日
     */
    private void persistenceMonit() {
        if (redisPersistenceThread == null) {
            redisPersistenceThread = (RedisPersistenceThread) ApplicationContextUtils
                    .getService("redisPersistenceThread");
            new Thread(redisPersistenceThread).start();
        } else if (redisPersistenceThread != null && redisPersistenceThread.isRunning == false) {
            redisPersistenceThread.isRunning = true;
        }

    }

    /**
     * 删除队列
     *
     * @author ligen 2015年8月10日
     */
    private void needDelQueue() {
        RedisService redisService = new RedisServiceImpl();
        String keyStr = RedisSupport.needDelQueue.poll();
        while (keyStr != null) {
            redisService.delByKey(keyStr);
            keyStr = RedisSupport.needDelQueue.poll();
        }
    }

    /**
     * 多行删除队列
     *
     * @author ligen 2015年8月10日
     */
    private void needDelPatternsQueue() {
        RedisService redisService = new RedisServiceImpl();
        String keyStr = RedisSupport.needDelPatternsQueue.poll();
        while (keyStr != null) {
            redisService.delByKeys(keyStr);
            keyStr = RedisSupport.needDelPatternsQueue.poll();
        }
    }

}
