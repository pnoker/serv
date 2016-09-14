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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.stereotype.Controller;

import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.common.datasource.DynamicDataSource.DbContextHolder;
import com.dc.city.common.exception.BusinessException;
import com.dc.city.common.utils.NetworkUtils;
import com.dc.city.common.vo.BusinessVo;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.service.database.ServeDataBaseExcuteService;
import com.dc.city.service.database.ServeProcessService;
import com.dc.city.vo.database.ServiceBaseVo;

/**
 * 服务处理 controller
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月8日 下午4:38:17
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/process/v1")
@Produces({ "application/json" })
public class ServeProcessController {

    @Resource(name = "serveDataBaseExcuteService")
    private ServeDataBaseExcuteService dataBaseService;

    @Resource
    private ServeProcessService processService;

    @Resource
    private DynamicDataSource dataSource;

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    /**
     * GET方法执行服务请求
     *
     * @param request
     * @param serviceCode 服务代码
     * @return
     * @author zhongdt 2016年3月25日
     * @throws BusinessException
     */
    @GET
    @Path("/{serviceCode}")
    public ServiceBaseVo doGetService(@Context HttpServletRequest request, @PathParam("serviceCode") String serviceCode)
            throws BusinessException {
        // 将参数转换成Map，包含ipaddress
        Map<String, String> requestMap = getRequestParam(request);

        // 根据serviceCode得到serviceConfig
        ServeConfig config;

        // 正在初始化数据时可能会抛出异常
        config = processService.getLocalConfig(serviceCode);

        // 状态不正常的服务，不允许调用
        if (config == null || config.getServiceStatus() != 3) {
            return new ServiceBaseVo("-1", "服务异常");
        }
        // 判断此服务是否支持post or get
        if (!validRequestMethod(config.getRequestMethod(), METHOD_GET)) {
            return new ServiceBaseVo("-1", "当前服务不支持GET方法 ");
        }

        // 判断是否存在
        String dataSourceKey = config.getDataSourceId() + "";
        if (!dataSource.isExists(dataSourceKey)) {
            return new ServiceBaseVo("-1", "数据源未设置");
        }
        // 判断数据源是否可用 暂时不监控服务端处理数据源状态
        // if (!dataSource.isValid(dataSourceKey)) {
        // return new ServiceBaseVo("-1", "数据源配置或状态不正常,请联系管理员");
        // }

        // 切换数据源
        DbContextHolder.setDbType(dataSourceKey);

        return processService.handleService(config, requestMap);

    }

    /**
     * 以post方式执行服务处理请求
     *
     * @param request
     * @param serviceCode 服务代码
     * @return
     * @author zhongdt 2016年5月11日
     * @throws BusinessException
     */
    @POST
    @Path("/{serviceCode}")
    public BusinessVo doPostService(@Context HttpServletRequest request, @PathParam("serviceCode") String serviceCode)
            throws BusinessException {
        Map<String, String> requestMap = getRequestParam(request);
        // 根据serviceCode得到serviceConfig
        ServeConfig config;

        // 从本地缓存读取config配置
        config = processService.getLocalConfig(serviceCode);

        if (config == null) {
            return new ServiceBaseVo("-1", "服务异常");
        }
        // 判断此服务是否支持post or get
        if (!validRequestMethod(config.getRequestMethod(), METHOD_POST)) {
            return new ServiceBaseVo("-1", "当前服务不支持POST方法 ");
        }

        // 判断是否存在
        String dataSourceKey = config.getDataSourceId() + "";
        if (!dataSource.isExists(dataSourceKey)) {
            return new ServiceBaseVo("-1", "数据源未设置");
        }

        // 服务处理端不监控数据源信息
        // if (!dataSource.isValid(dataSourceKey)) {
        // return new ServiceBaseVo("-1", "数据源配置或状态不正常,请联系管理员");
        // }
        // 切换数据源，准备开启事务
        DbContextHolder.setDbType(dataSourceKey);

        return processService.handleService(config, requestMap);

    }

    /**
     * 获取request对象中的参数转换成Map<String,String>
     *
     * @param request 参数全部转成小写
     * @return
     * @author zhongdt 2016年3月11日
     */
    private Map<String, String> getRequestParam(HttpServletRequest request) {
        Map<String, String> param = new HashMap<String, String>();
        // 全部参数转为小写并且去空格
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            param.put(entry.getKey().toLowerCase().trim(), entry.getValue()[0].trim());
        }
        try {
            String ip = NetworkUtils.getIpAddress(request);
            param.put("ip", ip);
        } catch (IOException e) {
            param.put("ip", "");
        }
        return param;
    }

    /**
     * 判断是否存在此方法
     *
     * @param 服务支持的方法
     * @param 前端调用方法
     * @return true 可以调用：false 不能
     * @author zhongdt 2016年3月11日
     */
    private boolean validRequestMethod(String serviceMethod, String requestMethod) {
        if (serviceMethod == null || requestMethod == null) {
            return false;
        }
        return serviceMethod.toUpperCase().indexOf(requestMethod.toUpperCase()) >= 0 ? true : false;
    }

}
