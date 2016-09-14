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

import org.springframework.util.CollectionUtils;

import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.dao.master.config.ServeConfigMapper;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.config.ServeConfigAuth;
import com.dc.city.domain.config.ServeConfigWhiteList;
import com.dc.city.domain.config.ServeDataRange;
import com.dc.city.domain.config.ServeInputParam;
import com.dc.city.domain.config.ServeInputParamNav;
import com.dc.city.domain.config.ServeOutputParam;
import com.dc.city.domain.config.ServeOutputParamNav;
import com.dc.city.domain.config.ServeSegment;

/**
 * 初始化配置表信息到redis，configuration工程启动
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月16日 上午10:27:32
 *          Copyright 2016 by DigitalChina
 */
public class InitConfigerListener {

    // 验证权限的时候缓存的key前缀
    public static final String CONFIG_KEY_PREEXT = "configerationzuoyuedeqianzui_zhenximeiyitian";

    // 执行配置服务时的缓存key前缀
    public static final String CONFIG_KEY_EXEC = "zhongyuyouyitiannihuiweicuoguo_dejihuieryihan";
    // private Log logger = LogFactory.getLog(InitConfigerListener.class);

    @Resource
    ServeConfigMapper serveConfigMapper;

    /**
     * listener初始化配置表信息
     *
     * @author chenzpa 2016年3月7日
     */
    @PostConstruct
    public void init() {
        initConfigForAuth();
        initConfigForExecute();
    }

    private void initConfigForAuth() {
        //先清空缓存，
        cleanRedis(CONFIG_KEY_PREEXT+"*");
        
        List<ServeConfigAuth> listAuth = serveConfigMapper.queryConfigbyAuth();
        if (listAuth != null && listAuth.size() > 0) {
            for (ServeConfigAuth c : listAuth) {
                RedisUtil.setObject(CONFIG_KEY_PREEXT + c.getServiceCode(), c, Integer.MAX_VALUE);
            }
        }
    }

    private void initConfigForExecute() {
        //先清空缓存，
        cleanRedis(CONFIG_KEY_EXEC+"*");
        
        ServeConfig queryParam = new ServeConfig();
        //查询状态正常的且未删除的服务
        queryParam.setIsDeleted(0);
        queryParam.setServiceStatus(3);
        queryParam.setIsService(1);
        
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
            // 放入redis缓存中
            RedisUtil.setObject(CONFIG_KEY_EXEC + conf.getServiceCode(), conf, Integer.MAX_VALUE);
        }
    }
    //set之前，先清空
    private void cleanRedis(String keyPattern){
        //先判断是否需要清空
        Set<String> keys = RedisUtil.getKeys(keyPattern);
        if(CollectionUtils.isEmpty(keys)){
            return;
        }
       RedisUtil.delByKeys(keyPattern);
    }
}
