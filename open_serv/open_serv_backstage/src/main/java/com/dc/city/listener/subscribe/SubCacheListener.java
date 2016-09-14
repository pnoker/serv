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
package com.dc.city.listener.subscribe;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;

import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.service.cache.PubCacheService;
import com.dc.city.service.cache.handler.ICacheRefresh;

/**
 * 监听数据信息变化listener，
 * 并调用相应模块的handler，处理刷新本地缓存
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月25日 下午3:11:34
 *          Copyright 2016 by DigitalChina
 */
public class SubCacheListener {

    private Logger logger = LoggerFactory.getLogger(SubCacheListener.class);

    // 处理服务handler map xml注入方式
    private Map<String, ICacheRefresh> handlers = new HashMap<String, ICacheRefresh>();

    public Map<String, ICacheRefresh> getHandlers() {
        return handlers;
    }

    public void setHandlers(Map<String, ICacheRefresh> handlers) {
        this.handlers = handlers;
    }

    @PostConstruct
    public void init() {
        new Thread(new SubscribeCacheThread()).start();
    }

    /**
     * 根据收到的消息进行相应的处理
     *
     * @param message :{"operation":"create","module":"blackList","data":1}
     * @author zhongdt 2016年3月25日
     */
    private void handleMessage(String message) {
        if (StringUtils.isNullOrEmpty(message)) {
            return;
        }
        //通知转成json对象
        JSONObject json = JSONObject.fromObject(message);
        String module = json.getString("module");
        String operation = json.getString("operation");
        String data = json.getString("data");
        if (StringUtils.isNullOrEmpty(module) || StringUtils.isNullOrEmpty(operation)) {
            logger.error("消息参数:" + json);
            return;
        }
        //根据模块获取到对应的处理类
        ICacheRefresh cacheModule = handlers.get(module);
        if (cacheModule == null) {
            logger.info("没有对应的handler,模块名称="+module);
            return;
        }
        
        //根据操作类型调用不同处理类的方法
        if (operation.equals(PubCacheService.OPERATE_CREATE)) {
            // 调用新增缓存操作
            cacheModule.cacheAdd(data);
        } else if (operation.equals(PubCacheService.OPERATE_MODIFY)) {
            // 调用修改缓存操作
            cacheModule.cacheEdit(data);
        } else if (operation.equals(PubCacheService.OPERATE_REMOVE)) {
            // 调用删除缓存操作
            cacheModule.cacheDelete(data);
        } else {
            logger.error("更新缓存操作类型错误,操作类型="+operation);
        }

    }

    class SubscribeCacheThread implements Runnable {
        @Override
        public void run() {
            try {
                subscribe();
            } catch (Exception e) {
                logger.error("订阅线程出错，重启中...", e);
                subscribe();
            }

        }
    }

    /**
     * 订阅redis消息方法
     * 此方法负责监听redis发布的消息
     * 
     * @author zhongdt 2016年3月25日
     */
    public void subscribe() {
        MySubListener listener = new MySubListener();
        RedisUtil.subscribe(listener, PubCacheService.CACHE_CHANNEL);
        // 取消发布
        // listener.unsubscribe();
    }

    class MySubListener extends JedisPubSub {
        @Override
        // 收到通知回调方法
        public void onMessage(String channel, String message) {
            super.onMessage(channel, message);
            // 处理通知
            handleMessage(message);
            logger.info("收到消息:" + message);
        }
    }

}
