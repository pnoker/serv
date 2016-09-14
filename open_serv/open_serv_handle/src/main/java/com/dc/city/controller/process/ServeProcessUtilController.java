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
package com.dc.city.controller.process;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Controller;

import com.dc.city.common.exception.BusinessException;
import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.listener.config.InitConfigerListener;
import com.dc.city.listener.support.ServeConfigCache;
import com.dc.city.service.config.ConfigerService;
import com.dc.city.service.database.ServeProcessService;
import com.dc.city.vo.database.ServiceBaseVo;

/**
 * 服务处理对外工具类，目前只提供reload服务处理配置功能
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月8日 下午4:38:17
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/util/v1")
@Produces({ "application/json" })
public class ServeProcessUtilController {

    @Resource
    private ConfigerService configerService;

    @Resource
    private ServeProcessService processService;

    /**
     * 重新加载服务配置信息
     *
     * @param serviceCode 服务代码
     * @return
     * @author zhongdt 2016年5月16日
     */
    @GET
    @Path("/reload/{serviceCode}")
    public ServiceBaseVo checkServiceConfig(@PathParam("serviceCode") String serviceCode) {
        ServeConfig config;
        try {
            config = processService.getLocalConfig(serviceCode);
        } catch (BusinessException e) {
            return new ServiceBaseVo("-1", e.getMessage());
        }
        if (config != null) {
            // 在本地缓存中命中
            return new ServiceBaseVo("0", "当前配置信息以在缓存中");
        } else {
            if (!isExists(serviceCode)) {
                return new ServiceBaseVo("-1", "redis中不存在此服务配置");
            }
            // 本地缓存中没有加载到，说明有可能状态不正常或者没有收到消息
            config = configerService.getServerConfigFromCache(serviceCode);
            if (config != null && config.getIsDeleted() == 0 && config.getServiceStatus() == 3) {
                ServeConfigCache.getInstance().addConfigCache(serviceCode, config);
                return new ServiceBaseVo("0", "加载redis服务配置 success");
            } else {
                return new ServiceBaseVo("-1", "加载redis服务配置失败,请检查服务状态或者重启服务管理");
            }
        }
    }

    /**
     * 判断缓存里面是否包含
     * 过滤不存在的serviceCode时重复请求数据库造成数据库压力
     *
     * @param serviceCode
     * @return
     * @author zhongdt 2016年5月16日
     */
    private boolean isExists(String serviceCode) {
        ServeConfig conf = (ServeConfig) RedisUtil.getObject(InitConfigerListener.CONFIG_KEY_EXEC + serviceCode);
        return conf == null ? false : true;
    }

}
