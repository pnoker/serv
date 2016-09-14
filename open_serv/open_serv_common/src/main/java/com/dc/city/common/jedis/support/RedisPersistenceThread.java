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

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.common.jedis.RedisWriteDbDao;
import redis.clients.jedis.Jedis;

/**
 * redis持久化线程，用于从队列中读出并写入数据库
 *
 * @author sunjl
 * @version V1.0 创建时间：2015年5月5日 上午9:31:46
 *          Copyright 2015 by DigitalChina
 */
@Component
@Scope("prototype")
public class RedisPersistenceThread implements Runnable {
    private static final Log LOG = LogFactory.getLog(RedisPersistenceThread.class);

    private int delayMilSec = 2500;
    protected boolean isRunning = true;

    @Resource
    private RedisWriteDbDao redisWriteDbDao;

    @Override
    public void run() {
        while (isRunning) {
            RedisPersistenceDTO redisPersistenceDTO = null;

            try (Jedis jedis = RedisUtil.pool.getResource()) {
                byte[] tempBytes = jedis.lpop(RedisUtil.REDIS_PERS_NAME.getBytes());
                if (tempBytes != null) {
                    redisPersistenceDTO = (RedisPersistenceDTO) SerializeUtil.unserialize(tempBytes);
                }
            }
            if (redisPersistenceDTO != null) {
                try {
                    redisWriteDbDao.createRedisEntity(redisPersistenceDTO);
                } catch (Exception e) {
                    LOG.error("----------redis visitor保存失败！错误原因：" + e.getMessage());
                }
            } else {
                try {
                    Thread.sleep(this.delayMilSec);
                } catch (InterruptedException e) {
                    LOG.error("", e);
                }
            }
        }
    }
}
