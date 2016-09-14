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
package com.dc.city.service.cache.impl.blacklist;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.city.common.utils.StringUtils;
import com.dc.city.listener.securitymanage.BlackListManager;
import com.dc.city.service.cache.handler.ICacheRefresh;
import com.dc.city.service.cache.impl.serve.CacheRefreshServeConfig;
import com.dc.city.service.securitymanage.BlackListManageService;

/**
 * 重新加载黑名单到内存缓存
 * data未ip地址
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月28日 上午11:06:15
 *          Copyright 2016 by DigitalChina
 */
public class CacheRefreshBlackList implements ICacheRefresh {

    @Resource
    private BlackListManageService blackListService;

    private Logger logger = LoggerFactory.getLogger(CacheRefreshServeConfig.class);

    /**
     * 新增黑名单到本地缓存
     * data:ip地址
     */
    @Override
    public void cacheAdd(String data) {

        String ipAddress = data;
        if (StringUtils.isNullOrEmpty(ipAddress)) {
            logger.info("加载黑名单地址失败，ip地址为空" + "");
        }

        BlackListManager.getInstance().addBlackListToMap(ipAddress.hashCode(), ipAddress);
        logger.info("加载黑名单:" + ipAddress + "成功");

    }

    /**
     * 同步编辑后的黑名单信息
     * 不支持黑名单编辑
     */
    @Override
    public void cacheEdit(String data) {
        // TODO Auto-generated method stub
    }

    /**
     * 从本地缓存删除黑名单
     */
    @Override
    public void cacheDelete(String ipAddress) {

        BlackListManager.getInstance().removeBlackListFromMap(ipAddress.hashCode());
        logger.info("刷新黑名单成功");

    }
}
