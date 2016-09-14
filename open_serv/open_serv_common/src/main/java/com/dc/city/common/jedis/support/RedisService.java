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
import java.util.Set;

import redis.clients.jedis.JedisPubSub;

/**
 * 类说明 Redis服务类
 * 
 * @author sunjl
 * @version V1.0 创建时间：2015年4月20日 下午5:57:57
 *          Copyright 2015 by DigitalChina
 */
public interface RedisService {

    public String setString(String key, String value, Integer seconds);

    public String getString(String key);

    public String setObject(String key, Serializable object, Integer seconds);

    public Long delByKey(String key);

    public Long delByKeys(String keyPattern);

    public Object getObject(String key);

    public void refreshJedis(String key, Integer seconds);
    
    //发布通知
    public void publish(String channel,String message);
    //订阅通知
    public void subscribe(JedisPubSub pubSub,String channel);
    
    //模糊查找keys
    public Set<String> getKeys(String keyPattern);

}
