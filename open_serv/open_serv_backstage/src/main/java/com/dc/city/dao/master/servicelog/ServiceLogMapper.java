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
package com.dc.city.dao.master.servicelog;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dc.city.domain.log.ServeVisitLog;
import com.dc.city.pojo.mongo.log.AccServTotalPo;
import com.dc.city.vo.mongo.log.AccessLogQuery;

/**
 * 日志表操作dao层
 *
 * @author ligen
 * @version V1.0 创建时间：2016年8月30日 下午4:24:29
 *          Copyright 2016 by DigitalChina
 */
public interface ServiceLogMapper {
    /**
     * 根据分页参数查询信息
     *
     * @return
     * @author ligen 2016年8月31日
     */
    List<ServeVisitLog> findAccessLogsByPageSize(AccessLogQuery accessLogQuery);    
    
    /**
     * 总概图，统计某段时间内，服务接口每天被访问的总量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年8月31日
     */
    List<AccServTotalPo> allImplStatByDay(@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    
    /**
     * 总概图，某段时间内，根据某字段，统计每个服务接口被访问的总量
     * 
     * @param type: ,1:表示按服务接口名称分组统计，2:表示按用户名分组统计，3:表示按IP分组统计
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    List<AccServTotalPo> allImplStatByField(@Param("key")String key, @Param("beginDate")String beginDate,@Param("endDate")String endDate);
    
    /**
     * 用户统计图，某段时间内，统计某用户在每一天访问服务接口的数量
     *
     * @param username 用户名
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    List<AccServTotalPo> dayStatByUser(@Param("username")String username,@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    
    /**
     * 用户统计图，某段时间内，统计某用户访问每个服务接口的数量
     * 
     * @param username 用户名
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    List<AccServTotalPo> ServNameStatByUser(@Param("username")String username,@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    
    /**
     * 接口统计图，某段时间内，统计某服务接口在每天中被访问的次数
     *
     * @param serviceId
     * @param beginDate
     * @param endDate
     * @return
     * @author ligen 2016年9月1日
     */
    List<AccServTotalPo> dayStatByImpl(@Param("serviceId")int serviceId,@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    
    /**
     * 接口统计图，某段时间内，统计某个服务接口被各个用户访问的数量
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    List<AccServTotalPo> serviceStatByImpl(@Param("serviceId")int serviceId,@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    
    /**
     * 接口统计图，某段时间内，统计某个服务接口被各个IP地址访问的数量
     *
     * @param serviceId
     * @param beginDate
     * @param endDate
     * @return
     * @author ligen 2016年9月1日
     */
    List<AccServTotalPo> ipStatByImpl(@Param("serviceId")int serviceId,@Param("beginDate")String beginDate,@Param("endDate")String endDate);    
    /**
     * 创建日志
     *
     * @param log
     * @return
     * @author ligen 2016年9月1日
     */
    int createAccessLog(ServeVisitLog log);
    
    int createBathAccessLogs(List<ServeVisitLog> records);
    
    ServeVisitLog findAccessLogById(String id);
    
    

}
