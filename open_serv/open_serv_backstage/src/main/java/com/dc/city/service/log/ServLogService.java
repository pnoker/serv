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
package com.dc.city.service.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.dc.city.dao.master.servicelog.ServiceLogMapper;
import com.dc.city.domain.log.ServeVisitLog;
import com.dc.city.pojo.mongo.log.AccServTotalPo;
import com.dc.city.vo.mongo.log.AccessLogListVo;
import com.dc.city.vo.mongo.log.AccessLogQuery;

/**
 * 服务管理日志操作
 *
 * @author ligen
 * @version V1.0 创建时间：2016年8月31日 下午1:35:42
 *          Copyright 2016 by DigitalChina
 */
@Service
public class ServLogService {
    private static Log logger = LogFactory.getLog(ServLogService.class);
    @Resource
    private ServiceLogMapper logMapper;
    
    
    /**
     * 创建访问日志记录表
     *
     * @param record
     * @author ligen 2016年9月1日
     */
    public void createAccessLog(ServeVisitLog record) {
        logMapper.createAccessLog(record);
    }
    

    /**
     * 查询日志列表方法
     *
     * @param accessLogQuery
     * @return
     * @author ligen 2016年8月31日
     */
    public AccessLogListVo findAccessLogList(AccessLogQuery accessLogQuery) {
        logger.debug("开始查询日志");
        // 调用查询方法得到查询结果
        List<ServeVisitLog> accessLog = logMapper.findAccessLogsByPageSize(accessLogQuery);
        if (accessLog.size() > 0) {
            // 获取总条数
            long totalNum = accessLog.get(0).getId();
            // 去除第一行的数据（总数）
            accessLog.remove(0);
            // 给日志赋序列号
            for (int i = 0; i < accessLog.size(); i++) {
                accessLog.get(i).setNumber(i + 1);
            }
            // 组装返回结果
            AccessLogListVo result = new AccessLogListVo();
            result.setPageSize(String.valueOf(accessLogQuery.getLength()));
            result.setDatas(accessLog);
            result.setTotalCount(String.valueOf(totalNum));
            return result;
        } else {
            AccessLogListVo resultTmp = new AccessLogListVo();
            resultTmp.setTotalCount("0");
            return resultTmp;
        }
    }

    /**
     * 总概图，统计某个时间段中，每天的服务接口被访问的总量
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author ligen 2016年9月1日
     */
    public AccessLogListVo allImplStatByDay(String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = logMapper.allImplStatByDay(beginDate, endDate);
        vo.setDatas(generalNormalDate(beginDate, endDate, list));
        return vo;
    }

    /**
     * 总概图，某段时间内，根据某字段，统计每个服务接口被访问的总量
     * 
     * @param type: ,1:表示按服务接口名称分组统计，2:表示按用户名分组统计，3:表示按IP分组统计
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    public AccessLogListVo allImplStatByField(int type, String beginDate, String endDate) {
        // 在某段时间内，统计每个服务接口被访问的总量
        AccessLogListVo vo = new AccessLogListVo();
        String key = "";
        switch (type) {
        // 按服务名称统计
            case 1:
                key = "service_Name";
                break;
            // 按用户名统计
            case 2:
                key = "user_Name";
                break;
            // 访问IP地址统计
            case 3:
                key = "visit_IpAddress";
                break;
        }
        if ("".equals(key)) {
            return null;
        }
        // 第一个参数为：1，表示按服务接口名称分组统计，2：表示按用户名分组统计，3：表示按IP分组统计
        List<AccServTotalPo> list = logMapper.allImplStatByField(key, beginDate, endDate);
        vo.setDatas(list);
        return vo;
    }
    
    /**
     * 用户统计图，某段时间内，统计某用户在每一天访问服务接口的数量
     *
     * @param username 用户名
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    public AccessLogListVo dayStatByUser(String username, String beginDate, String endDate) {
        List<AccServTotalPo> list = logMapper.dayStatByUser(username, beginDate, endDate);
        AccessLogListVo vo = new AccessLogListVo();
        vo.setDatas(generalNormalDate(beginDate, endDate, list));
        return vo;
    }
    
    /**
     * 用户统计图，某段时间内，统计某用户访问每个服务接口的数量
     * 
     * @param username 用户名
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    public AccessLogListVo ServNameStatByUser(String username, String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list =logMapper.ServNameStatByUser(username, beginDate, endDate);
        if (list != null && list.size() == 0) {
            AccServTotalPo apAccServTotalPo = new AccServTotalPo();
            apAccServTotalPo.setStatKey("最近没有调用接口");
            apAccServTotalPo.setTotalNum(0);
            List<AccServTotalPo> aclist = new ArrayList<AccServTotalPo>();
            aclist.add(apAccServTotalPo);
            vo.setDatas(aclist);
        }else {
            vo.setDatas(list);
        }
        return vo;
    }
    
    /**
     * 接口统计图，某段时间内，统计某服务接口在每天中被访问的次数
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    public AccessLogListVo dayStatByImpl(int serviceId, String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = logMapper.dayStatByImpl(serviceId, beginDate, endDate);
        vo.setDatas(generalNormalDate(beginDate, endDate, list));
        return vo;
    }
    
    /**
     * 接口统计图，某段时间内，统计某个服务接口被各个用户访问的数量
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    public AccessLogListVo serviceStatByImpl(int serviceId, String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = logMapper.serviceStatByImpl(serviceId, beginDate, endDate);
        if (list != null && list.size() == 0) {
            AccServTotalPo apAccServTotalPo = new AccServTotalPo();
            apAccServTotalPo.setStatKey("最近没有用户访问");
            apAccServTotalPo.setTotalNum(0);
            List<AccServTotalPo> aclist = new ArrayList<AccServTotalPo>();
            aclist.add(apAccServTotalPo);
            vo.setDatas(aclist);
        }else{
            vo.setDatas(list); 
        }
        return vo;
    }
    
    /**
     * 接口统计图，某段时间内，统计某个服务接口被各个IP地址访问的数量
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author ligen 2016年9月1日
     */
    public AccessLogListVo ipStatByImpl(int serviceId, String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = logMapper.ipStatByImpl(serviceId, beginDate, endDate);
        if (list != null && list.size() == 0) {
            AccServTotalPo apAccServTotalPo = new AccServTotalPo();
            apAccServTotalPo.setStatKey("最近没有IP访问");
            apAccServTotalPo.setTotalNum(0);
            List<AccServTotalPo> aclist = new ArrayList<AccServTotalPo>();
            aclist.add(apAccServTotalPo);
            vo.setDatas(aclist);
        }else{
            vo.setDatas(list);
        }
        return vo;
    }
    
    

    /**
     * 指定日期的数据生成格式，把空数据日期填充完整
     *
     * @param beginDate 统计开始时间
     * @param endDate 统计结束时间
     * @param list 数据日期查询结果
     * @return
     * @author liuppa 2016年5月26日
     */
    private List<AccServTotalPo> generalNormalDate(String beginDate, String endDate, List<AccServTotalPo> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<AccServTotalPo> normalList = new ArrayList<AccServTotalPo>();

        Date sDate = null;
        Date eDate = null;
        // 把字符串日期转换为日期格式数据
        try {
            sDate = sdf.parse(beginDate);
            eDate = sdf.parse(endDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 时间范围内的的默认数据

        Calendar c = Calendar.getInstance();
        c.setTime(sDate);
        // 添加空数据日期数据
        for (; eDate.compareTo(sDate) >= 0;) {
            AccServTotalPo ap = new AccServTotalPo();
            ap.setTotalNum(0);
            ap.setStatKey(sdf.format(c.getTime()));
            normalList.add(ap);
            c.add(Calendar.DAY_OF_MONTH, 1);
            sDate = c.getTime();
        }
        // 更新每天的总数量
        for (AccServTotalPo accServTotalPo : normalList) {
            for (AccServTotalPo t : list) {
                System.out.println(t.getStatKey()+"::::::"+accServTotalPo.getStatKey());
                if (t.getStatKey().equalsIgnoreCase(accServTotalPo.getStatKey()+" 00:00:00")) {
                    accServTotalPo.setTotalNum(t.getTotalNum());
                    break;
                }
            }
        }
        return normalList;
    }
    

}
