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
package com.dc.city.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.dc.city.common.exception.BusinessException;
import com.dc.city.common.utils.NetworkUtils;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.log.ServeVisitLog;
import com.dc.city.filter.support.WrapperResponse;
import com.dc.city.listener.securitymanage.BlackListManager;
import com.dc.city.service.database.ServeProcessService;
import com.dc.city.service.log.ServLogService;
import com.dc.city.service.mongo.log.ServeVisitLogService;
import com.dc.city.service.securitymanage.UserManageService;
import com.dc.city.vo.securitymanage.SecurityManageVo;
import com.dc.city.vo.securitymanage.user.UserManageVo;

/**
 * 服务处理过滤器
 * 负责:过滤服务配置合法性
 * 过滤黑名单
 * 记录服务日志
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月15日 下午2:13:22
 *          Copyright 2016 by DigitalChina
 */
public class ServeProcessFilter implements Filter {

    ServeVisitLogService visitLogService;

    UserManageService userService;

    ServeProcessService processService;
    
    ServLogService logService;

    /**
     * 初始化lister方法,得到service
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        ServletContext sc = fConfig.getServletContext();
        XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils
                .getWebApplicationContext(sc);
        // 初始化service
        if (cxt != null && cxt.getBean("serveVisitLogService") != null && logService == null) {
            logService = (ServLogService) cxt.getBean("servLogService");
        }
        if (cxt != null && cxt.getBean("userManageService") != null && userService == null) {
            userService = (UserManageService) cxt.getBean("userManageService");
        }

        if (cxt != null && cxt.getBean("serveProcessService") != null && processService == null) {
            processService = (ServeProcessService) cxt.getBean("serveProcessService");
        }
    }

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {
        long current = System.currentTimeMillis();

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 设置response字符集
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");

        // 执行服务请求，用于拦截到请求返回信息，判断处理状态
        WrapperResponse wrapperResponse = new WrapperResponse(response);

        // 检查serviceCode，并从缓存获取
        String serviceCode = getServiceCode(request);
        ServeConfig config = null;
        if (!StringUtils.isNullOrEmpty(serviceCode)) {
            // 获取config对象
            config = getConfig(serviceCode);
        }

        // 未从缓存读取到配置信息,直接返回
        if (config.getId() == -1) {
            response.getWriter().write(buildErrorInfo("服务未被加载"));
            return;
        }

        // 检查ip地址黑名单
        String ipAddress = NetworkUtils.getIpAddress(request);
        if (verifyBlackList(ipAddress)) {
            response.getWriter().write(buildErrorInfo("当前IP:" + ipAddress + ",禁止访问"));
            return;
        }

        chain.doFilter(request, wrapperResponse);

        // 构造log对象
        ServeVisitLog log = new ServeVisitLog();
        log.setStartTime(new Date());
        log.setLogParams(getRequestParamStr(request));
        log.setVisitIpAddress(NetworkUtils.getIpAddress(request));

        // 构造返回信息
        log = setResultInfo(wrapperResponse, log);

        // 执行请求回到过滤器
        long costTime = (System.currentTimeMillis() - current) / 1000;
        log.setEndTime(new Date());
        log.setVisitDate(new Date());
        log.setCostTime(costTime);

        // 构造用户信息
        String appKey = request.getParameter("appkey") == null ? "" : request.getParameter("appkey");// 从request中取到appKey
        log = setCustomerInfo(appKey, log);
        log = setServiceInfo(config, log);
        // 调用保存方法
        logService.createAccessLog(log);
        // 再将json数据返回前端
        response.getWriter().write(new String(wrapperResponse.getResponseData(), "UTF-8"));

    }

    public static String getRequestParamStr(HttpServletRequest request) {
        Map<String, String> param = new HashMap<String, String>();
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            param.put(entry.getKey().toLowerCase().trim(), entry.getValue()[0].trim());
        }
        return param.toString();
    }

    /**
     * 根据路径获取serviceCode
     * /serv_handle/process/v1/handle/test?id=1&source=2s1测试&xx=1&sourceType=1
     *
     * @param request
     * @return
     * @author zhongdt 2016年3月15日
     */
    private String getServiceCode(HttpServletRequest request) {
        String url = request.getRequestURI();
        int index = url.lastIndexOf("/");
        String serviceCode = null;
        if (index > 0) {
            serviceCode = url.substring(index + 1).replace(".json", "").replace(".JSON", "");
        }
        return serviceCode;
    }

    private ServeConfig getConfig(String serviceCode) {
        // 默认从缓存里面读取，如果读不到或者读取错误则设置默认值
        ServeConfig config = null;
        if (!StringUtils.isNullOrEmpty(serviceCode)) {
            // 改成从接口取数据
            try {
                config = processService.getLocalConfig(serviceCode);
            } catch (BusinessException e) {
                //这里不做任何处理。config 还是为null
            }
        }
        if (config == null) {
            config = new ServeConfig();
            config.setId(-1);
            config.setServiceCode(serviceCode);
            config.setServiceName("无效的服务名");
            config.setServiceType(3);
        }
        return config;
    }

    /**
     * 根据appkey来设置用户信息
     *
     * @param appKey
     * @author zhongdt 2016年3月15日
     */
    private ServeVisitLog setCustomerInfo(String appKey, ServeVisitLog log) {
        // 构造默认对象，查询不到用户时使用默认信息
        UserManageVo user = null;

        if (!StringUtils.isNullOrEmpty(appKey)) {
            SecurityManageVo vo = userService.queryUserByAppKey(appKey);
            // 返回0 标识成功
            if (vo != null && ("0").equals(vo.getResultCode())) {
                user = (UserManageVo) vo.getObject();
            }
        }
        //没取到，或者缓存中取出垃圾数据
        if (user == null || StringUtils.isNullOrEmpty(user.getUserName())) {
            user = new UserManageVo();
            user.setId(-1);
            user.setUserChannel(-1);
            user.setUserName("anonymous");
        }

        log.setUserChannel(user.getUserChannel());
        log.setUserId(user.getId());
        log.setUserName(user.getUserName());
        return log;
    }

    /**
     * 根据服务代码设置服务信息
     *
     * @param serviceCode
     * @author zhongdt 2016年3月15日
     */
    private ServeVisitLog setServiceInfo(ServeConfig config, ServeVisitLog log) {
        log.setServiceId(config.getId());
        log.setServiceName(config.getServiceName());
        log.setServiceType(config.getServiceType());
        return log;
    }

    private ServeVisitLog setResultInfo(WrapperResponse response, ServeVisitLog log) {
        int status = ((HttpServletResponse) response).getStatus();
        String resultCode = "";
        String resultInfo = "";
        if (status != 200) {
            resultCode = status + "";
            try {
                // 非200的话，则把response的内容取出来放到resultInfo中
                resultInfo = new String(response.getResponseData(), "UTF-8");
            } catch (IOException e) {
                resultInfo = "请求未处理完成，请参考返回码";
            }
        } else {
            // 从response中取返回结果
            resultCode = "-99";
            resultInfo = "返回结果异常";
            try {
                JSONObject json = JSONObject.fromObject(new String(response.getResponseData(), "UTF-8"));
                if (json.containsKey("resultCode")) {
                    resultCode = json.getString("resultCode");
                    resultInfo = json.containsKey("resultInfo") == false ? "" : json.getString("resultInfo");
                }
            } catch (IOException e) {
                //这里也不做任何操作
            }

        }
        log.setRetCode(resultCode);
        log.setRetMsg(resultInfo);
        return log;
    }

    private String buildErrorInfo(String errorMsg) {
        JSONObject json = new JSONObject();
        json.put("resultCode", "-1");
        json.put("resultInfo", errorMsg);
        return json.toString();
    }

    /**
     * 验证黑名单
     *
     * @param ipAddress
     * @return true 是黑名单
     * @author zhongdt 2016年3月16日
     */
    private boolean verifyBlackList(String ipAddress) {
        Map<Integer, String> blackList = BlackListManager.getInstance().findBlackListMap();
        for (Entry<Integer, String> entry : blackList.entrySet()) {
            if (entry.getValue().equals(ipAddress)) {
                return true;
            }
        }
        return false;
    }

}