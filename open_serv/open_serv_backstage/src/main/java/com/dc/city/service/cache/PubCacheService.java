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
package com.dc.city.service.cache;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.dc.city.common.jedis.RedisUtil;

/**
 * 发布更新缓存通知
 * 通过redis发布订阅信息接口
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月25日 下午3:14:32
 *          Copyright 2016 by DigitalChina
 */
@Service
public class PubCacheService {

    // 服务处理模块黑名单数据源服务配置监听渠道
    public static final String CACHE_CHANNEL = "ServeConfigurationBDS";

    // 模块名称 暂时只支持这么多模块
    public static final String MODULE_BLACK_LIST = "blackList";
    public static final String MODULE_DATA_SOURCE = "dataSource";
    public static final String MODULE_SERVE_CONFIG = "serveConfig";

    // 操作类型
    public static final String OPERATE_CREATE = "create";
    public static final String OPERATE_MODIFY = "modify";
    public static final String OPERATE_REMOVE = "remove";

    /**
     * 发布黑名单已更新消息
     *
     * @param operate crud
     * @param data 黑名单地址
     * @author zhongdt 2016年3月25日
     */
    public void publishBlackList(String operate, String data){
        // 构造json格式数据
        JSONObject msgMap = wrapperMsg(operate, data);
        msgMap.put("module", MODULE_BLACK_LIST);
        // 通过redis发送通知
        RedisUtil.publish(CACHE_CHANNEL, JSONObject.fromObject(msgMap).toString());
    }

    /**
     * 通知其他系统，数据源已更新
     *
     * @param operate 操作类型
     * @param data 数据信息
     * @author zhongdt 2016年3月28日
     */
    public void publishDataSource(String operate, String data){
        JSONObject msgMap = wrapperMsg(operate, data);
        // 写入模块名称
        msgMap.put("module", MODULE_DATA_SOURCE);
        RedisUtil.publish(CACHE_CHANNEL, msgMap.toString());
    }

    /**
     * 通知其他系统，服务配置已更新
     *
     * @param operate 操作类型
     * @param data 数据信息
     * @author zhongdt 2016年3月28日
     */
    public void publishServeConfig(String operate, String data){
        JSONObject msgMap = wrapperMsg(operate, data);
        // 传入固定的模块名称
        msgMap.put("module", MODULE_SERVE_CONFIG);
        // 通过redis发布订阅通知
        RedisUtil.publish(CACHE_CHANNEL, msgMap.toString());
    }

    /**
     * 封装redis发布消息的消息体
     *
     * @param operate 操作类型
     * @param data 传递的数据
     * @return
     * @author zhongdt 2016年5月6日
     */
    public JSONObject wrapperMsg(String operate, String data) {
        JSONObject msgMap = new JSONObject();
        // 封装data
        msgMap.put("data", data == null ? "" : data);
        //封装操作类型
        msgMap.put("operation", operate == null ? "" : operate);
        return msgMap;
    }
}
