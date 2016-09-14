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
package com.dc.city.controller.mongo.log;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Controller;

import com.dc.city.common.utils.DateUtils;
import com.dc.city.controller.config.ServeConfigQueryController;
import com.dc.city.controller.securitymanage.user.UserManageController;
import com.dc.city.service.log.ServLogService;
import com.dc.city.service.mongo.log.ServeVisitLogService;
import com.dc.city.vo.catalog.CataLogQueryVo;
import com.dc.city.vo.catalog.CatalogBaseVo;
import com.dc.city.vo.config.ServeConfigQueryVo;
import com.dc.city.vo.config.ServeConfigVo;
import com.dc.city.vo.mongo.log.AccessLogListVo;
import com.dc.city.vo.securitymanage.SecurityManageVo;
import com.dc.city.vo.securitymanage.user.UserManageVo;

/**
 * Mongodb数据库中，接口日志查询和分析Controller层
 *
 * @author liuppa
 * @version V1.0 创建时间：2016年3月15日 下午1:12:41
 *          Copyright 2016 by DC
 */
@Controller
@Path("log/v1")
@Produces({ "application/json", "application/xml" })
public class ServeVisitLogController {

    @Resource
    ServeVisitLogService service;

    @Resource
    UserManageController userManageController;

    @Resource
    ServeConfigQueryController serveConfigQueryController;
    
    @Resource
    ServLogService logService;

    @GET
    @Path("/querylist")
    public ServeConfigVo serviceList(@BeanParam ServeConfigQueryVo queryVo, @QueryParam("key") String key,
            @QueryParam("serviceCode") String serviceCode, @QueryParam("serviceName") String serviceName) {
        return serveConfigQueryController.queryServiceList(queryVo, key, serviceCode, serviceName);
    }

    @GET
    @Path("/querycatalogtree")
    public CatalogBaseVo cataLogTree(@BeanParam CataLogQueryVo vo) {
        return serveConfigQueryController.cataLogTree(vo);
    }

    /**
     * 查询用户
     *
     * @param userManageVo
     * @return
     * @author zuoyue 2016年3月10日
     */
    @GET
    @Path("/queryuser")
    public SecurityManageVo queryUser(@BeanParam UserManageVo userManageQueryVo, @QueryParam("userName") String userName) {
        return userManageController.queryUser(userManageQueryVo, userName);
    }

    /**
     * 总概图，统计某个时间段中，每天的服务接口被访问的总量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @author liuppa 2016年3月15日
     */
    @GET
    @Path("/allstatbyday")
    public AccessLogListVo allImplStatByDay(@QueryParam("beginDate") String beginDate,
            @QueryParam("endDate") String endDate) {
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        AccessLogListVo vo = logService.allImplStatByDay(beginDate, endDate);
        return vo;
    }

    /**
     * 总概图，统计某个时间段中，每个服务接口被访问的总量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月15日
     */
    @GET
    @Path("/allstatbyservice")
    public AccessLogListVo allImplStatByService(@QueryParam("beginDate") String beginDate,
            @QueryParam("endDate") String endDate) {
        // type=1表示按服务名称分组统计
        int type = 1;
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        AccessLogListVo vo = logService.allImplStatByField(type, beginDate, endDate);
        return vo;
    }

    /**
     * 总概图，统计某个时间段中，每个用户调用服务接口的总次数
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月15日
     */
    @GET
    @Path("/allimplstatbyuser")
    public AccessLogListVo allImplStatByUser(@QueryParam("beginDate") String beginDate,
            @QueryParam("endDate") String endDate) {
        // type=2表示按用户名称分组统计
        int type = 2;
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        AccessLogListVo vo = logService.allImplStatByField(type, beginDate, endDate);
        return vo;
    }

    /**
     * 总概图，统计某个时间段中，每个IP地址访问接口的总次数
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月15日
     */
    @GET
    @Path("/allimplstatbyip")
    public AccessLogListVo allImplStatByIp(@QueryParam("beginDate") String beginDate,
            @QueryParam("endDate") String endDate) {
        // type=3表示按IP地址分组统计
        int type = 3;
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        AccessLogListVo vo = logService.allImplStatByField(type, beginDate, endDate);
        return vo;
    }

    /**
     * 用户统计图，统计某个时间段中，某用户在每一天访问服务接口的数量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月15日
     */
    @GET
    @Path("/daystatbyuser")
    public AccessLogListVo dayStatByUser(@QueryParam("username") String username,
            @QueryParam("beginDate") String beginDate, @QueryParam("endDate") String endDate) {
        AccessLogListVo vo = new AccessLogListVo();
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        vo = logService.dayStatByUser(username, beginDate, endDate);
        return vo;
    }

    /**
     * 用户统计图，统计某个时间段中，某用户访问每个服务接口的数量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月15日
     */
    @GET
    @Path("/servnamestatbyuser")
    public AccessLogListVo ServNameStatByUser(@QueryParam("username") String username,
            @QueryParam("beginDate") String beginDate, @QueryParam("endDate") String endDate) {
        AccessLogListVo vo = new AccessLogListVo();
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        vo = logService.ServNameStatByUser(username, beginDate, endDate);
        return vo;
    }

    /******************* 按接口统计，待写code *************************/

    /**
     * 接口统计图，近一周中，统计某服务接口在每天中被访问的次数
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @param serviceId 服务ID
     * @return
     * @author liuppa 2016年4月19日
     */
    @GET
    @Path("/daystatbyimpl")
    public AccessLogListVo dayStatByImpl(@QueryParam("beginDate") String beginDate,
            @QueryParam("endDate") String endDate, @QueryParam("serviceId") int serviceId) {
        AccessLogListVo vo = new AccessLogListVo();
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        vo = logService.dayStatByImpl(serviceId, beginDate, endDate);
        return vo;
    }

    /**
     * 接口统计图，近一周中，统计某个服务接口被各个用户访问的数量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @param serviceId 服务ID
     * @return
     * @author liuppa 2016年4月19日
     */
    @GET
    @Path("/servicestatbyimpl")
    public AccessLogListVo serviceStatByImpl(@QueryParam("beginDate") String beginDate,
            @QueryParam("endDate") String endDate, @QueryParam("serviceId") int serviceId) {
        AccessLogListVo vo = new AccessLogListVo();
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        vo = logService.serviceStatByImpl(serviceId, beginDate, endDate);
        return vo;
    }

    /**
     * 接口统计图，近一周中，统计某个服务接口被各个IP地址访问的数量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @param serviceId 服务ID
     * @return
     * @author liuppa 2016年4月19日
     */
    @GET
    @Path("/ipstatbyimpl")
    public AccessLogListVo ipStatByImpl(@QueryParam("beginDate") String beginDate,
            @QueryParam("endDate") String endDate, @QueryParam("serviceId") int serviceId) {
        AccessLogListVo vo = new AccessLogListVo();
        // 给时间参数beginDate和endDate添加时分秒
        beginDate += DateUtils.MIN_SECONDS;
        endDate += DateUtils.MAX_SECONDS;
        vo = logService.ipStatByImpl(serviceId, beginDate, endDate);
        return vo;
    }
}
