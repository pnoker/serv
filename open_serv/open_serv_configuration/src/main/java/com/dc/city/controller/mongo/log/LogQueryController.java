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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Controller;

import com.dc.city.common.utils.StringUtils;
import com.dc.city.service.log.ServLogService;
import com.dc.city.vo.PageVo;
import com.dc.city.vo.mongo.log.AccessLogListVo;
import com.dc.city.vo.mongo.log.AccessLogQuery;

/**
 * 日志查询Controller
 *
 * @author liuppa
 * @version V1.0 创建时间：2016年4月14日 下午2:38:16
 *          Copyright 2016 by DC
 */

@Controller
@Path("/logquery/v1")
@Produces({ "application/json", "application/xml" })
public class LogQueryController {
    @Resource
    ServLogService service;

    /**
     * 分页查询日志
     *
     * @param serviceName 服务名称
     * @param userName 用户名
     * @param userChannel 用户内外网标识 (1:内网，2:外网)
     * @param ip IP地址
     * @param beginTime 访问时间
     * @param endTime 访问时间
     * @param servType 服务内外网标识 1:配置接口，2:外部接口，3:空间数据库
     * @return
     * @author chenzpa 2016年3月29日
     */
    @GET
    @Path("/queryloglists")
    public AccessLogListVo queryLogLists(@QueryParam("serviceName") String serviceName,
            @QueryParam("userName") String userName, @QueryParam("userChannel") String userChannel,
            @QueryParam("ip") String ip, @QueryParam("beginTime") String beginTime,
            @QueryParam("endTime") String endTime, @QueryParam("servType") String servType,
            @QueryParam("start") String startStr, @QueryParam("length") String length) {
        AccessLogQuery query = new AccessLogQuery();

        // 每页记录数
        int pageSize = Integer.valueOf(length);
        pageSize = (pageSize == 0) ? PageVo.DEFAULT_PAGE_SIZE : pageSize;
        // 当前页码
        int start = Integer.valueOf(startStr);
        // start = (start <= 0) ? PageVo.DEFAULT_CURRENT_PAGE : ++start;
        // 起始行数
        query.setBeginRowNum(start);
        // 截止行数
        query.setEndRowNum(start + pageSize);
        query.setStart(start);
        query.setLength(pageSize);
        query.setServName(serviceName);
        query.setBeginTime(StringUtils.isNullOrEmpty(beginTime) ? beginTime : beginTime + " 00:00:00");
        query.setEndTime(StringUtils.isNullOrEmpty(endTime) ? endTime : endTime + " 23:59:59");
        query.setIp(ip);
        query.setServType(servType);
        query.setUserChannel(userChannel);
        query.setUserName(userName);

        return service.findAccessLogList(query);
    }
}
