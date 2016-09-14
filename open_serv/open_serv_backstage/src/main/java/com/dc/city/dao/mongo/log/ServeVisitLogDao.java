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
package com.dc.city.dao.mongo.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.dc.city.common.utils.DateUtils;
import com.dc.city.domain.log.ServeVisitLog;
import com.dc.city.pojo.mongo.log.AccServTotalPo;
import com.mongodb.CommandResult;

/**
 * 访问日志记录dao层
 * modified by liuppa 2016-03-10
 * 修改内容：重命名Dao类名，修改实体类名称
 * 
 * @author xutaog
 * @version V1.0 创建时间：2016年3月7日 下午3:45:42
 *          Copyright 2016 by DigitalChina
 */
public class ServeVisitLogDao {
    // 数据库中查询的集合
    private static final String collection = "serveVisitLog";
    // 数据库中，聚合查询，按visitDate，即访问时间分组查询
    private static final String type_visitDate = "visitDate";
    // 数据库中，聚合查询，按userName，即用户名分组查询
    private static final String type_userName = "userName";
    // 数据库中，聚合查询，按serviceName，即服务名称分组查询
    private static final String type_serviceName = "serviceName";
    // 数据库中，聚合查询，按visitIpAddress，即IP分组查询
    private static final String type_visitIpAddress = "visitIpAddress";
    // 数据库中，聚合查询，按serviceName，即服务名称分组查询
    private static final String type_serviceId = "serviceId";

    private MongoTemplate mongoTemplate;

    /**
     * 创建日志记录文件
     * 用于单条记录插入操作，如果是首次插入，数据库中没有此集合，数据库会自动创建对应集合
     * modified by liuppa ,2016-03-14
     * 修改内容：细化注释
     *
     * @param record
     * @author xutaog 2016年3月7日
     */
    public void createAccessLog(ServeVisitLog record) {
        mongoTemplate.insert(record);
    }

    /**
     * 批量插入日志
     *
     * @param records
     * @author xutaog 2016年3月7日
     */
    public void createBathAccessLogs(List<ServeVisitLog> records) {
        mongoTemplate.insertAll(records);
    }

    /**
     * 根据日志id获取日志详细信息
     * 
     * @param id 日志Id
     * @return
     * @author xutaog 2016年3月7日
     */
    public ServeVisitLog findAccessLogById(String id) {

        return mongoTemplate.findById(id, ServeVisitLog.class);
    }

    /**
     * 根据查询条件进行分页查询语句
     *
     * @param pageSize 每页多少条数
     * @param pageIndex 当前第几页
     * @param criteria 查询条件
     * @return
     * @author xutaog 2016年3月8日
     */
    public List<ServeVisitLog> findAccessLogsByPageSize(int pageSize, int pageIndex, Criteria criteria, Sort sort) {
        Query query = new Query();
        query.skip((pageIndex - 1) * pageSize);
        query.limit(pageSize);
        query.addCriteria(criteria);
        query.with(sort);
        return mongoTemplate.find(query, ServeVisitLog.class);
    }

    public List<ServeVisitLog> findAccessLogsByStartLenth(int start, int lenth, Criteria criteria, Sort sort) {
        Query query = new Query();
        query.skip(start);
        query.limit(lenth);
        query.addCriteria(criteria);
        query.with(sort);
        return mongoTemplate.find(query, ServeVisitLog.class);
    }

    /**
     * 测试是否能够直接执行Mongo原始语句，******************************************
     *
     * @param beginDate
     * @param endDate
     * @author xutao 2016年3月14日
     */
    public void findActiveUserListInTimeInterval(Date beginDate, Date endDate) {

        CommandResult result = mongoTemplate.executeCommand("db.serveVisitLog.distinct( 'userId', { visitDate:{$gte:"
                + beginDate + ",$lte:" + endDate + "}  } )");
        Iterator<?> iterators = result.values().iterator();
        while (iterators.hasNext()) {
            Object object = (Object) iterators.next();
            System.out.println("=====" + object.toString());
        }
        // return mongoTemplate.find(query, ServeVisitLog.class);

    }

    /**
     * 根据查询条件查询条数
     *
     * @param criteria
     * @return
     * @author xutaog 2016年3月8日
     */
    public long findAccessLogCount(Criteria criteria) {
        Query query = new Query();
        query.addCriteria(criteria);
        return mongoTemplate.count(query, ServeVisitLog.class);
    }

    /**
     * 根据group函数聚合统计，*********************************************
     *
     * @return
     * @author xutao 2016年3月14日
     */
    public GroupByResults<AccServTotalPo> findLogGroup() {

        GroupByResults<AccServTotalPo> results = mongoTemplate.group(
                Criteria.where("visitDate").gte(DateUtils.parse("2016-03-02", DateUtils.DATE_FORMAT_2))
                        .lte(DateUtils.parse("2016-03-09", DateUtils.DATE_FORMAT_2)), "serveVisitLog",
                GroupBy.keyFunction(
                        "function(doc){var date = new Date(doc.visitDate);"
                                + " var dateKey = ''+date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();"
                                + "return {'statKey':dateKey};" + "}").initialDocument("{totalNum:0}")
                        .reduceFunction("function(doc,out){" + "if(doc.visitDate){" + "out.totalNum+=1;}" + "}"),
                AccServTotalPo.class);

        return results;

    }

    /**
     * 创建日志表的集合，并加上索引
     * 创建之前需要集合判断是否存在
     *
     * @author xutaog 2016年3月11日
     */
    public void createServeVisitLogCollection() {
        // 首先验证ServeVisitLogCollection是否存在
        boolean isExits = mongoTemplate.collectionExists(ServeVisitLog.class);
        // 如果不存在就创建集合，如果存在就跳过
        if (!isExits) {
            mongoTemplate.createCollection(ServeVisitLog.class);
            // 创建完之后需要为字段加上索引
            createServeVisitLogIndex();
        }
    }

    /**
     * 创建日志表索引
     *
     * @author xutaog 2016年3月11日
     */
    private void createServeVisitLogIndex() {
        IndexOperations io = mongoTemplate.indexOps(ServeVisitLog.class);
        // 为用户名加上索引
        Index index = new Index();
        index.on("userName", Direction.DESC); // 为userName属性加上 索引
        io.ensureIndex(index);
        // 为用户Id创建索引
        index = new Index();
        index.on("userId", Direction.DESC); // 为userId属性加上 索引
        io.ensureIndex(index);
        // 为用户内外网标识创建索引
        index = new Index();
        index.on("userChannel", Direction.DESC); // 为userChannel属性加上 索引
        io.ensureIndex(index);
        // 为服务Id创建索引
        index = new Index();
        index.on("serviceId", Direction.DESC); // 为serviceId属性加上 索引
        io.ensureIndex(index);
        // 为服务名称创建索引
        index = new Index();
        index.on("serviceName", Direction.DESC); // 为serviceName属性加上 索引
        io.ensureIndex(index);
        // 为访问时间创建索引
        index = new Index();
        index.on("visitDate", Direction.DESC); // 为visitDate属性加上 索引
        io.ensureIndex(index);
        // 为记录更新时间创建索引
        index = new Index();
        index.on("updateTime", Direction.DESC); // 为visitDate属性加上 索引
        io.ensureIndex(index);

    }

    /**************************** liuppa **************************************/
    /**
     * 总概图，统计某段时间内，服务接口每天被访问的总量
     *
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月14日
     */
    public List<AccServTotalPo> allImplStatByDay(String beginDate, String endDate) {
        String time = ServeVisitLogDao.type_visitDate;
        // 聚合函数
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.project(time).andExpression("substr(visitDate,0,10)").as("statKey"),
                Aggregation.match(Criteria.where(time).gte(DateUtils.parse(beginDate, DateUtils.DATE_FORMAT_2))
                        .lte(DateUtils.parse(endDate, DateUtils.DATE_FORMAT_2))), Aggregation.group("statKey").count()
                        .as("totalNum"), Aggregation.sort(Sort.Direction.ASC, "statKey"),
                Aggregation.project("statKey", "totalNum").and("statKey").previousOperation());

        // 聚合结果
        AggregationResults<AccServTotalPo> result = mongoTemplate.aggregate(agg, ServeVisitLogDao.collection,
                AccServTotalPo.class);

        // 存储到List列表中
        List<AccServTotalPo> resultList = result.getMappedResults();
        return resultList;
    }

    /**
     * 总概图，统计某段时间内，统计每个服务接口被访问的总量
     *
     * @type 统计关键字，1:按服务名称统计，2:按用户名统计，3:访问IP地址统计
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月14日
     */
    public List<AccServTotalPo> allImplStatByField(int type, String beginDate, String endDate) {
        String time = ServeVisitLogDao.type_visitDate;
        String key = "";
        switch (type) {
        // 按服务名称统计
            case 1:
                key = ServeVisitLogDao.type_serviceName;
                break;
            // 按用户名统计
            case 2:
                key = ServeVisitLogDao.type_userName;
                break;
            // 访问IP地址统计
            case 3:
                key = ServeVisitLogDao.type_visitIpAddress;
                break;
        }
        if ("".equals(key)) {
            return null;
        }
        // 聚合函数
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(time).gte(DateUtils.parse(beginDate, DateUtils.DATE_FORMAT_2))
                        .lte(DateUtils.parse(endDate, DateUtils.DATE_FORMAT_2))),
                Aggregation.project(key).and(key).as("statKey"), Aggregation.group("statKey").count().as("totalNum"),
                Aggregation.sort(Sort.Direction.DESC, "totalNum"),
                Aggregation.project("statKey", "totalNum").and("statKey").previousOperation());
        // 聚合结果
        AggregationResults<AccServTotalPo> result = mongoTemplate.aggregate(agg, ServeVisitLogDao.collection,
                AccServTotalPo.class);
        // 存储到List列表中
        List<AccServTotalPo> resultList = result.getMappedResults();
        return resultList;
    }

    /**
     * 用户统计图，统计某段时间内，某用户在每一天访问服务接口的数量
     *
     * @param username 用户名
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月15日
     */
    public List<AccServTotalPo> dayStatByUser(String username, String beginDate, String endDate) {
        String time = ServeVisitLogDao.type_visitDate;
        String name = ServeVisitLogDao.type_userName;
        // 聚合函数
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.project(time, name).andExpression("substr(visitDate,0,10)").as("statKey"),
                Aggregation.match(Criteria.where(time).gte(DateUtils.parse(beginDate, DateUtils.DATE_FORMAT_2))
                        .lte(DateUtils.parse(endDate, DateUtils.DATE_FORMAT_2)).and(name).is(username)), Aggregation
                        .group("statKey").count().as("totalNum"), Aggregation.sort(Sort.Direction.ASC, "statKey"),
                Aggregation.project("statKey", "totalNum").and("statKey").previousOperation());

        // 聚合结果
        AggregationResults<AccServTotalPo> result = mongoTemplate.aggregate(agg, ServeVisitLogDao.collection,
                AccServTotalPo.class);
        // 存储到List列表中
        List<AccServTotalPo> resultList = result.getMappedResults();
        return resultList;
    }

    /**
     * 用户统计图，统计某段时间内，某用户访问每个服务接口的数量
     *
     * @param username 用户名
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年3月16日
     */
    public List<AccServTotalPo> ServNameStatByUser(String username, String beginDate, String endDate) {
        String time = ServeVisitLogDao.type_visitDate;
        String name = ServeVisitLogDao.type_userName;
        String key = ServeVisitLogDao.type_serviceName;
        // 聚合函数
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(time).gte(DateUtils.parse(beginDate, DateUtils.DATE_FORMAT_2))
                        .lte(DateUtils.parse(endDate, DateUtils.DATE_FORMAT_2)).and(name).is(username)), Aggregation
                        .project(key, name).and(key).as("statKey"),
                Aggregation.group("statKey").count().as("totalNum"), Aggregation.sort(Sort.Direction.DESC, "totalNum"),
                Aggregation.project("statKey", "totalNum").and("statKey").previousOperation());

        // 聚合结果
        AggregationResults<AccServTotalPo> result = mongoTemplate.aggregate(agg, ServeVisitLogDao.collection,
                AccServTotalPo.class);
        // 存储到List列表中
        List<AccServTotalPo> resultList = result.getMappedResults();
        // 如果用户这段时间未访问接口，返回对应提示信息
        if (resultList != null && resultList.size() == 0) {
            AccServTotalPo apAccServTotalPo = new AccServTotalPo();
            apAccServTotalPo.setStatKey("最近没有调用接口");
            apAccServTotalPo.setTotalNum(0);
            List<AccServTotalPo> list = new ArrayList<AccServTotalPo>();
            list.add(apAccServTotalPo);
            return list;
        }
        return resultList;
    }

    /**
     * 接口统计图，统计某段时间内，统计某服务接口在每天中被访问的次数
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年4月19日
     */
    public List<AccServTotalPo> dayStatByImpl(int serviceId, String beginDate, String endDate) {
        String time = ServeVisitLogDao.type_visitDate;
        String name = ServeVisitLogDao.type_serviceId;
        // 聚合函数
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.project(time, name).andExpression("substr(visitDate,0,10)").as("statKey"),
                Aggregation.match(Criteria.where(time).gte(DateUtils.parse(beginDate, DateUtils.DATE_FORMAT_2))
                        .lte(DateUtils.parse(endDate, DateUtils.DATE_FORMAT_2)).and(name).is(serviceId)), Aggregation
                        .group("statKey").count().as("totalNum"), Aggregation.sort(Sort.Direction.ASC, "statKey"),
                Aggregation.project("statKey", "totalNum").and("statKey").previousOperation());

        // 聚合结果
        AggregationResults<AccServTotalPo> result = mongoTemplate.aggregate(agg, ServeVisitLogDao.collection,
                AccServTotalPo.class);
        // 存储到List列表中
        List<AccServTotalPo> resultList = result.getMappedResults();
        return resultList;
    }

    /**
     * 接口统计图，统计某段时间内，统计某服务接口在每天中被访问的次数
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年4月19日
     */
    public List<AccServTotalPo> serviceStatByImpl(int serviceId, String beginDate, String endDate) {
        String time = ServeVisitLogDao.type_visitDate;
        String name = ServeVisitLogDao.type_serviceId;
        String key = ServeVisitLogDao.type_userName;
        // 聚合函数
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(time).gte(DateUtils.parse(beginDate, DateUtils.DATE_FORMAT_2))
                        .lte(DateUtils.parse(endDate, DateUtils.DATE_FORMAT_2)).and(name).is(serviceId)), Aggregation
                        .project(key, name).and(key).as("statKey"),
                Aggregation.group("statKey").count().as("totalNum"), Aggregation.sort(Sort.Direction.DESC, "totalNum"),
                Aggregation.project("statKey", "totalNum").and("statKey").previousOperation());

        // 聚合结果
        AggregationResults<AccServTotalPo> result = mongoTemplate.aggregate(agg, ServeVisitLogDao.collection,
                AccServTotalPo.class);
        // 存储到List列表中
        List<AccServTotalPo> resultList = result.getMappedResults();
        // 如果用户这段时间未访问接口，返回对应提示信息
        if (resultList != null && resultList.size() == 0) {
            AccServTotalPo apAccServTotalPo = new AccServTotalPo();
            apAccServTotalPo.setStatKey("最近没有用户访问");
            apAccServTotalPo.setTotalNum(0);
            List<AccServTotalPo> list = new ArrayList<AccServTotalPo>();
            list.add(apAccServTotalPo);
            return list;
        }

        return resultList;
    }

    /**
     * 接口统计图，统计某段时间内，统计某个服务接口被各个IP地址访问的数量
     *
     * @param serviceId 服务ID
     * @param beginDate 统计的开始时间
     * @param endDate 统计的结束时间
     * @return
     * @author liuppa 2016年4月19日
     */
    public List<AccServTotalPo> ipStatByImpl(int serviceId, String beginDate, String endDate) {
        String time = ServeVisitLogDao.type_visitDate;
        String name = ServeVisitLogDao.type_serviceId;
        String key = ServeVisitLogDao.type_visitIpAddress;
        // 聚合函数
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(time).gte(DateUtils.parse(beginDate, DateUtils.DATE_FORMAT_2))
                        .lte(DateUtils.parse(endDate, DateUtils.DATE_FORMAT_2)).and(name).is(serviceId)), Aggregation
                        .project(key, name).and(key).as("statKey"),
                Aggregation.group("statKey").count().as("totalNum"), Aggregation.sort(Sort.Direction.DESC, "totalNum"),
                Aggregation.project("statKey", "totalNum").and("statKey").previousOperation());
        // 聚合结果
        AggregationResults<AccServTotalPo> result = mongoTemplate.aggregate(agg, ServeVisitLogDao.collection,
                AccServTotalPo.class);
        // 存储到List列表中
        List<AccServTotalPo> resultList = result.getMappedResults();
        // 如果用户这段时间未访问接口，返回对应提示信息
        if (resultList != null && resultList.size() == 0) {
            AccServTotalPo apAccServTotalPo = new AccServTotalPo();
            apAccServTotalPo.setStatKey("最近没有IP访问");
            apAccServTotalPo.setTotalNum(0);
            List<AccServTotalPo> list = new ArrayList<AccServTotalPo>();
            list.add(apAccServTotalPo);
            return list;
        }
        return resultList;
    }

    /**
     * 测试方法
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author liuppa 2016年3月14日
     */
    /*
     * public List<AccServTotalPo> implStatByService(String beginDate, String endDate) {
     * //近一周的总量
     * Aggregation agg = Aggregation.newAggregation(
     * Aggregation.match(Criteria.where("visitDate").gte(DateUtils.parse(beginDate,
     * DateUtils.DATE_FORMAT_2))
     * .lte(DateUtils.parse(endDate, DateUtils.DATE_FORMAT_2))),
     * Aggregation.project("serviceName").and("serviceName").as("statKey"),
     * Aggregation.group("statKey").count().as("totalNum"),
     * Aggregation.sort(Sort.Direction.DESC, "totalNum"),
     * Aggregation.project("statKey","totalNum").and("statKey").previousOperation());
     * //聚合结果
     * AggregationResults<AccServTotalPo> result = mongoTemplate.aggregate(agg,
     * ServeVisitLogDao.collection, AccServTotalPo.class);
     * //System.out.println("agg++++++++++" + agg.toString());
     * //System.out.println("size++++++++++" + result.getMappedResults().size());
     * //存储到List列表中
     * List<AccServTotalPo> resultList = result.getMappedResults();
     * AccServTotalPo vo = new AccServTotalPo();
     * for (AccServTotalPo list : resultList) {
     * String date = list.getStatKey();
     * long count = list.getTotalNum();
     * vo.setTotalNum(count);
     * vo.setStatKey(date);
     * System.out.println(vo.getStatKey() + "," + vo.getTotalNum());
     * }
     * return resultList;
     * }
     */

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
