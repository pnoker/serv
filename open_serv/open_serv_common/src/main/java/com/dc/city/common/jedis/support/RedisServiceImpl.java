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

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Redis服务类impl
 *
 * @author sunjl
 * @version V1.0 创建时间：2015年4月20日 下午6:03:18
 *          Copyright 2015 by DigitalChina
 */
public class RedisServiceImpl implements RedisService {

    @Override
    public String setString(String key, String value, Integer seconds) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            return seconds == null ? jedis.setex(key, RedisSupport.defaultExpireSecond, value) : jedis.setex(key,
                    seconds, value);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public String getString(String key) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            return jedis.get(key);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public String setObject(String key, Serializable object, Integer seconds) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            return seconds == null ? jedis.setex(key.getBytes(), RedisSupport.defaultExpireSecond,
                    SerializeUtil.serialize(object)) : jedis.setex(key.getBytes(), seconds,
                    SerializeUtil.serialize(object));
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public Long delByKey(String key) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            Long retLong = jedis.del(key.getBytes());
            retLong += jedis.del(key);
            return retLong;
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public Long delByKeys(String keyPattern) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            Set<String> keySet = jedis.keys(keyPattern);
            Iterator<String> iterator = keySet.iterator();

            String[] keys = new String[keySet.size()];
            byte[][] keysByte = new byte[keySet.size()][];
            for (int i = 0; i < keySet.size(); i++) {
                String string = iterator.next();
                keys[i] = string;
                keysByte[i] = string.getBytes();
            }
            if (keys.length > 0) {
                Long retLong = jedis.del(keys);
                retLong += jedis.del(keysByte);
                return retLong;
            }
            return 0L;
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelPatternsQueue.add(keyPattern);
            return null;
        }
    }

    @Override
    public Object getObject(String key) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            return SerializeUtil.unserialize(jedis.get(key.getBytes()));
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public void refreshJedis(String key, Integer seconds) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            if (jedis.exists(key)) {
                jedis.expire(key, seconds);
            }
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
        }
    }

    /**
     * 发布消息 
     * 当在本系统有数据更新，需要及时通知到其他系统时，就可以调用此方法
     * @param channel:发布渠道 
     * @param message:消息
     */
    @Override
    public void publish(String channel, String message) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            jedis.publish(channel, message);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            return;
        }
        
    }

    /**
     * 订阅redis消
     * 用于服务器间进行传递消息
     * 目前应用于web后台修改数据化，通过发布消息，其他服务端收到消息后，进行数据同步
     * @param channel:渠道
     * @param pubSub:收到消息之后的处理handler
     * 
     */
    @Override
    public void subscribe(JedisPubSub pubSub, String channel) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            jedis.subscribe(pubSub,channel);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            return;
        }
        
    }

    /**
     * 返回redis 当前匹配的key集合
     */
    @Override
    public Set<String> getKeys(String keyPattern) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
           return jedis.keys(keyPattern);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
        }
        return null;
    }
}
