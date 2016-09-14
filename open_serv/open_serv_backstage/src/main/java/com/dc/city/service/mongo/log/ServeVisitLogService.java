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
package com.dc.city.service.mongo.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;

import com.dc.city.common.utils.DateUtils;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.dao.mongo.log.ServeVisitLogDao;
import com.dc.city.domain.log.ServeVisitLog;
import com.dc.city.pojo.mongo.log.AccServTotalPo;
import com.dc.city.vo.mongo.log.AccessLogListVo;
import com.dc.city.vo.mongo.log.AccessLogQuery;

/**
 * 日志管理相关的service
 * modified by liuppa 2016-03-10
 * 修改内容：重命名Service类名，修改实体类名称
 * 
 * @author xutaog
 * @version V1.0 创建时间：2016年3月7日 下午4:22:35
 *          Copyright 2016 by DigitalChina
 */
public class ServeVisitLogService {

    private static Log logger = LogFactory.getLog(ServeVisitLogService.class);

    private ServeVisitLogDao serveVisitLogDao;

    /**
     * 创建访问日志记录表
     *
     * @param record
     * @author xutaog 2016年3月7日
     */
    public void createAccessLog(ServeVisitLog record) {
        serveVisitLogDao.createAccessLog(record);
    }

    /**
     * 批量创建日志记录表
     *
     * @param records
     * @author xutaog 2016年3月7日
     */
    public void createAccessLogs(List<ServeVisitLog> records) {
        serveVisitLogDao.createBathAccessLogs(records);
    }

    /**
     * 根据日志id查询日志详细信息
     *
     * @param id
     * @return
     * @author xutaog 2016年3月7日
     */
    public ServeVisitLog findAccessLogById(String id) {
        return serveVisitLogDao.findAccessLogById(id);
    }

    /**
     * 根据查询条件查询日志条数
     *
     * @param criteria
     * @return
     * @author xutaog 2016年3月8日
     */
    public long findAccessLogCount(Criteria criteria) {
        return serveVisitLogDao.findAccessLogCount(criteria);
    }

    /**
     * 根据查询条件查询日志列表
     *
     * @param accessLogQuery 前台需要传输的日志列表类
     * @return
     * @author xutaog 2016年3月8日
     */
    public AccessLogListVo findAccessLogList(AccessLogQuery accessLogQuery) {
        logger.debug("开始查询日志");
        // 开始封装查询条件
        Criteria criteria = new Criteria();
        List<Criteria> criterias = new ArrayList<Criteria>();
        // 判断用户名是否为空,不为空就将条件加上
        if (!StringUtils.isNullOrEmpty(accessLogQuery.getUserName())) {
            criterias.add(Criteria.where("userName").regex(".*?" + accessLogQuery.getUserName() + ".*"));
        }
        // 判断服务名称是否为空,不为空就将条件加上
        if (!StringUtils.isNullOrEmpty(accessLogQuery.getServName())) {
            criterias.add(Criteria.where("serviceName").regex(".*?" + accessLogQuery.getServName() + ".*"));
        }
        // 判断用户的内外网标识是否为空，不为空就将条件加上
        if (!StringUtils.isNullOrEmpty(accessLogQuery.getUserChannel())) {
            criterias.add(Criteria.where("userChannel").is(Integer.parseInt(accessLogQuery.getUserChannel())));
        }
        // 判断服务的内外网标识是否为空，不为空将条件加上
        if (!StringUtils.isNullOrEmpty(accessLogQuery.getServType())) {
            criterias.add(Criteria.where("serviceType").is(Integer.parseInt(accessLogQuery.getServType())));
        }
        // 判断服务访问的IP是否为空，不为空就将条件加上
        if (!StringUtils.isNullOrEmpty(accessLogQuery.getIp())) {
            criterias.add(Criteria.where("visitIpAddress").is(accessLogQuery.getIp()));
        }
        // 当起始时间 不为空的，将时间条件加上
        if (!StringUtils.isNullOrEmpty(accessLogQuery.getBeginTime())
                && StringUtils.isNullOrEmpty(accessLogQuery.getEndTime())) {
            criterias.add(Criteria.where("visitDate")
                    .gte(DateUtils.parse(accessLogQuery.getBeginTime(), DateUtils.DATE_FORMAT_2))
                    .lte(Calendar.getInstance().getTime()));
        }
        // 当 结束时间 不为空的，将时间条件加上
        if (StringUtils.isNullOrEmpty(accessLogQuery.getBeginTime())
                && !StringUtils.isNullOrEmpty(accessLogQuery.getEndTime())) {
            criterias.add(Criteria.where("visitDate")
                    .gte(DateUtils.parse("1970-01-01 00:00:00", DateUtils.DATE_FORMAT_2))
                    .lte(DateUtils.parse(accessLogQuery.getEndTime(), DateUtils.DATE_FORMAT_2)));
        }
        // 当起始时间与结束时间都不为空的，将时间条件加上
        if (!StringUtils.isNullOrEmpty(accessLogQuery.getBeginTime())
                && !StringUtils.isNullOrEmpty(accessLogQuery.getEndTime())) {
            criterias.add(Criteria.where("visitDate")
                    .lte(DateUtils.parse(accessLogQuery.getEndTime() + " 23:59:59", DateUtils.DATE_FORMAT_2))
                    .gte(DateUtils.parse(accessLogQuery.getBeginTime() + " 00:00:00", DateUtils.DATE_FORMAT_2)));
        }
        if (criterias.size() > 0) {
            criteria.andOperator(criterias.toArray(new Criteria[] {}));
        }
        // 组装排序条件
        Sort sort = new Sort(Direction.DESC, "visitDate");
        // 调用查询方法得到查询结果
        List<ServeVisitLog> accessLog = serveVisitLogDao.findAccessLogsByStartLenth(accessLogQuery.getStart(),
                accessLogQuery.getLength(), criteria, sort);
        for (int i = 0; i < accessLog.size(); i++) {
            accessLog.get(i).setNumber(i + 1);
        }
        // 获取总条数
        long totalNum = findAccessLogCount(criteria);
        // 求总页数
        // long pageCount = 0;
        // if ((totalNum % accessLogQuery.getPageSize()) > 0) {// 如果总条数与每页多少条余数大于0
        // pageCount = (totalNum / accessLogQuery.getPageSize()) + 1;
        // } else {
        // pageCount = totalNum / accessLogQuery.getPageSize();
        // }
        // 组装返回结果
        AccessLogListVo result = new AccessLogListVo();
        result.setPageSize(String.valueOf(accessLogQuery.getLength()));
        result.setDatas(accessLog);
        // result.setCurrentPage(String.valueOf(accessLogQuery.getCurrentPage()));
        result.setTotalCount(String.valueOf(totalNum));
        // result.setPageCount(String.valueOf(pageCount));
        return result;
    }

    public ServeVisitLogDao getServeVisitLogDao() {
        return serveVisitLogDao;
    }

    public void setServeVisitLogDao(ServeVisitLogDao serveVisitLogDao) {
        this.serveVisitLogDao = serveVisitLogDao;
    }

    public void testAccessMongo() {
        // String[][] userNameArr = { { "ldg", "statTymInfo" }, { "lxx", "findTmwInfo" }, { "ydd",
        // "findAqiInfo" },
        // { "xtt", "findMftInfo" }, { "rlf", "findWetherInfo" }, { "hrd", "statTjwlhInfo" },
        // { "swxg", "createSwhInfo" }, { "wztx", "updateInfo" }, { "tzwy", "delUseNameById" },
        // { "xuxian", "statXzblzTime" } };
        // List<AccessLog> ret = new ArrayList<AccessLog>();
        // for (int j = 0; j < 1000; j++) {
        // Random random = new Random();
        // String iStr = String.valueOf(j);
        // int zhywLen = Integer.valueOf(iStr.substring(iStr.length() - 1, iStr.length()));
        // String userName = userNameArr[zhywLen][0];
        // String serCode = userNameArr[zhywLen][1];
        // Date statDate = DateUtils.dealDay(new Date(), Calendar.DAY_OF_MONTH, -j);
        // int len = random.nextInt(235) + 100;
        // for (int i = 0; i < len; i++) {
        // AccessLog record = new AccessLog();
        // record.setAccTime(statDate);
        // record.setAccTimeLen(300);
        // record.setErrorInfo("这是是发发空");
        // record.setInputParam("{userName:'12121',userPwd:'121323'}");
        // record.setIp("127.0.0.1");
        // record.setIsAccSucc(1);
        // record.setOutputParam("这是一个参数哈哈：" + i);
        // record.setPageNum(i + 9);
        // record.setServCode(serCode);
        // record.setServId(i);
        // record.setServName("获取降雨量");
        // record.setUserId(i + 120);
        // record.setUserName(userName);
        // ret.add(record);
        // }
        // }
        // Date startDate = new Date();
        // logger.info("开始执行保存时间：" + DateUtils.format(startDate, "yyyy-MM-dd hh:mm:ss"));
        // createAccessLogs(ret);
        // Date endDate = new Date();
        // long timePir = endDate.getTime() - startDate.getTime();
        // logger.info("条数：" + ret.size() + "====创建需要时间：" + timePir);
        // logger.info("结束保存时间：" + DateUtils.format(endDate, "yyyy-MM-dd hh:mm:ss"));
        // =========查询出所有
        // List<AccessLog> records = findAccessLogs();
        // for (int i = 0; i < records.size(); i++) {
        // AccessLog record = records.get(i);
        // logger.info("id:" + record.getId() + "==userName:" + record.getUserName() + "==statDate:"
        // + DateUtils.format(record.getAccTime(), "yyyy-MM-dd hh:mm:ss"));
        // }
        // AccessLogQuery accessLogQuery = new AccessLogQuery();
        // accessLogQuery.setPageSize(5);
        // accessLogQuery.setCurrentPage(1);
        // accessLogQuery.setServCode("r");
        // AccessLogListVo vo = findAccessLogList(accessLogQuery);
        // logger.info("总条数:" + vo.getTotalCount());
        // List<?> records = vo.getDatas();
        // for (int i = 0; i < records.size(); i++) {
        // AccessLog record = (AccessLog) records.get(i);
        // logger.info("id:" + record.getId() + "==userName:" + record.getUserName() + "==statDate:"
        // + DateUtils.format(record.getAccTime(), "yyyy-MM-dd hh:mm:ss"));
        // }
        ServeVisitLog record = findAccessLogById("56dd3e5636bf0e6353359fb7");
        logger.info("id:" + record.getId() + "==userName:" + record.getUserName() + "==statDate:"
                + DateUtils.format(record.getVisitDate(), "yyyy-MM-dd hh:mm:ss"));
        // GroupByResults<AccServTotalPo> accResults = serveVisitLogDao.findLogGroup();
        // logger.info("结果的条数:" + accResults.getCount() + "===keys的数量：" + accResults.getKeys());
        // Iterator<AccServTotalPo> iteAcc = accResults.iterator();
        // int num = 0;
        // while (iteAcc.hasNext()) {
        // AccServTotalPo accServTotalPo = (AccServTotalPo) iteAcc.next();
        // num++;
        // logger.info("编号：" + num + "===时间：" + accServTotalPo.getStatDate() + "====条数："
        // + accServTotalPo.getTotalNum());
        // }
    }

    /****************************** liuppa *****************************/
    /**
     * 测试方法
     *
     * @param beginDate
     * @param endDate
     * @author liuppa 2016年5月11日
     */
    /*
     * public void test(String beginDate, String endDate) {
     * // int type=3;
     * // 最近一周，按字段分组统计接口访问总量，1:服务名称字段，2:用户名字段，3:IP字段
     * // serveVisitLogDao.implStatByField(type,beginDate,endDate);
     * // 最近一周，按日期分组统计接口访问总量
     * // serveVisitLogDao.allImplStatByDay(beginDate,endDate);
     * // serveVisitLogDao.dayStatByUser("a0", beginDate, endDate);
     * serveVisitLogDao.ServNameStatByUser("a0", beginDate, endDate);
     * }
     */

    /**
     * 总概图，某段时间内，按日期分组统计接口访问总量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月15日
     */
    public AccessLogListVo allImplStatByDay(String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = serveVisitLogDao.allImplStatByDay(beginDate, endDate);
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
     * @author liuppa 2016年3月15日
     */
    public AccessLogListVo allImplStatByField(int type, String beginDate, String endDate) {
        // 在某段时间内，统计每个服务接口被访问的总量
        AccessLogListVo vo = new AccessLogListVo();
        // 第一个参数为：1，表示按服务接口名称分组统计，2：表示按用户名分组统计，3：表示按IP分组统计
        List<AccServTotalPo> list = serveVisitLogDao.allImplStatByField(type, beginDate, endDate);
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
     * @author liuppa 2016年3月15日
     */
    public AccessLogListVo dayStatByUser(String username, String beginDate, String endDate) {
        List<AccServTotalPo> list = serveVisitLogDao.dayStatByUser(username, beginDate, endDate);
        AccessLogListVo vo = new AccessLogListVo();
        vo.setDatas(generalNormalDate(beginDate, endDate, list));
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
                if (t.getStatKey().equalsIgnoreCase(accServTotalPo.getStatKey())) {
                    accServTotalPo.setTotalNum(t.getTotalNum());
                    break;
                }
            }
        }
        return normalList;
    }

    /**
     * 用户统计图，某段时间内，统计某用户访问每个服务接口的数量
     * 
     * @param username 用户名
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月15日
     */
    public AccessLogListVo ServNameStatByUser(String username, String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = new ArrayList<AccServTotalPo>();
        list = serveVisitLogDao.ServNameStatByUser(username, beginDate, endDate);

        vo.setDatas(list);
        return vo;
    }

    /**
     * 接口统计图，某段时间内，统计某服务接口在每天中被访问的次数
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年4月19日
     */
    public AccessLogListVo dayStatByImpl(int serviceId, String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = serveVisitLogDao.dayStatByImpl(serviceId, beginDate, endDate);
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
     * @author liuppa 2016年4月19日
     */
    public AccessLogListVo serviceStatByImpl(int serviceId, String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = serveVisitLogDao.serviceStatByImpl(serviceId, beginDate, endDate);
        vo.setDatas(list);
        return vo;
    }

    /**
     * 接口统计图，某段时间内，统计某个服务接口被各个IP地址访问的数量
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年4月19日
     */
    public AccessLogListVo ipStatByImpl(int serviceId, String beginDate, String endDate) {
        // 在某段时间内，按日期分组统计接口访问总量
        AccessLogListVo vo = new AccessLogListVo();
        List<AccServTotalPo> list = serveVisitLogDao.ipStatByImpl(serviceId, beginDate, endDate);
        vo.setDatas(list);
        return vo;
    }

}
