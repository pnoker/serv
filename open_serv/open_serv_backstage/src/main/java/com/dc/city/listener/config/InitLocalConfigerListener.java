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
package com.dc.city.listener.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.dao.master.config.ServeConfigMapper;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.config.ServeConfigWhiteList;
import com.dc.city.domain.config.ServeDataRange;
import com.dc.city.domain.config.ServeInputParam;
import com.dc.city.domain.config.ServeInputParamNav;
import com.dc.city.domain.config.ServeOutputParam;
import com.dc.city.domain.config.ServeOutputParamNav;
import com.dc.city.domain.config.ServeSegment;
import com.dc.city.listener.support.ServeConfigCache;

/**
 * 初始化服务配置信息到本地缓存
 * 由handle工程初始化，加载到内存中
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月29日 上午10:10:38
 *          Copyright 2016 by DigitalChina
 */
public class InitLocalConfigerListener {

    @Resource
    ServeConfigMapper serveConfigMapper;

    @PostConstruct
    public void initLocalCache() {

        // 设置为正在加载状态
        ServeConfigCache.setLoading(true);
        // 从redis中取出所有服务的key，然后在遍历
        Set<String> keys = RedisUtil.getKeys(InitConfigerListener.CONFIG_KEY_EXEC + "*");
        //keys的非空判断（当redis挂掉的情况下）@20160803 修复redis挂掉后，报空指针的问题
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                // 从redis中取出对象
                ServeConfig config = (ServeConfig) RedisUtil.getObject(key);
                if (config == null) {
                    continue;
                }
                String serviceCode = config.getServiceCode();
                // 服务代码为空或者状态不正常的不加载到内存中
                if (serviceCode == null || config.getServiceStatus() != 3 || config.getIsDeleted() == 1) {
                    continue;
                }
                ServeConfigCache.getInstance().addConfigCache(serviceCode, config);
            }
        }

        // 如果本次缓存中服务配置数量为0(可能就是redis挂)，重新从数据库加载一次
        if (ServeConfigCache.getInstance().getCacheSize() == 0) {
            // 从本地数据库读取
            initConfigFromDb();
        }

        // 设置为初始化成功状态，可以进行正常的系统服务
        ServeConfigCache.setLoading(false);

    }

    private void initConfigFromDb() {
        ServeConfig queryParam = new ServeConfig();
        // 查询状态正常的且未删除的服务
        queryParam.setIsDeleted(0);
        queryParam.setServiceStatus(3);

        List<ServeConfig> allConf = serveConfigMapper.selectBySelective(queryParam);

        List<ServeInputParam> inputArgs = serveConfigMapper.queryInputArgs(null);

        List<ServeSegment> segments = serveConfigMapper.querySegments(null);

        List<ServeOutputParam> outputArgs = serveConfigMapper.queryOutputArgs(null);

        List<ServeInputParamNav> inputArgsNavs = serveConfigMapper.queryInputArgsNavs(null);

        List<ServeDataRange> dateRangeArgs = serveConfigMapper.queryDateRangeArgs(null);

        List<ServeOutputParamNav> outputArgsNav = serveConfigMapper.queryOutputArgsNav(null);

        List<ServeConfigWhiteList> whiteList = serveConfigMapper.queryWhiteList(null);

        for (ServeConfig conf : allConf) {
            conf.setInputArgs(new ArrayList<ServeInputParam>());
            conf.setOutputArgs(new ArrayList<ServeOutputParam>());
            conf.setSegments(new ArrayList<ServeSegment>());
            conf.setInputArgsNavs(new ArrayList<ServeInputParamNav>());
            conf.setDateRangeArgs(new ArrayList<ServeDataRange>());
            conf.setOutputArgsNav(new ArrayList<ServeOutputParamNav>());
            conf.setWhiteList(new ArrayList<ServeConfigWhiteList>());
            long sevrId = conf.getId();

            for (ServeConfigWhiteList w : whiteList) {
                if (w.getServiceId() == sevrId) {
                    conf.getWhiteList().add(w);
                }
            }
            for (ServeOutputParamNav o : outputArgsNav) {
                if (o.getServiceId() == sevrId) {
                    conf.getOutputArgsNav().add(o);
                }
            }
            for (ServeDataRange d : dateRangeArgs) {
                if (d.getServiceId() == sevrId) {
                    conf.getDateRangeArgs().add(d);
                }
            }
            for (ServeInputParamNav i : inputArgsNavs) {
                if (i.getServiceId() == sevrId) {
                    conf.getInputArgsNavs().add(i);
                }
            }
            for (ServeOutputParam o : outputArgs) {
                if (o.getServiceId() == sevrId) {
                    conf.getOutputArgs().add(o);
                }
            }
            for (ServeInputParam ia : inputArgs) {
                if (ia.getServiceId() == sevrId) {
                    conf.getInputArgs().add(ia);
                }
            }
            for (ServeSegment s : segments) {
                if (s.getServiceId() == sevrId) {
                    conf.getSegments().add(s);
                }
            }

            ServeConfigCache.getInstance().addConfigCache(conf.getServiceCode(), conf);
        }
    }
}
