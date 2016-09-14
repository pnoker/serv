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
package com.dc.city.service.database;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dc.city.common.datasource.DynamicDataSource.DbContextHolder;
import com.dc.city.common.utils.BeanUtils;
import com.dc.city.dao.master.database.builder.ISqlBuilder;
import com.dc.city.domain.datasource.ServeDataSource;
import com.dc.city.pojo.datasource.DataBaseColumnPo;

/**
 * 数据库信息查询service 考虑到各种数据库的兼容性差异，生成查询sql的builder通过注入
 * 此service没有使用注解，是在spring-jdbctemplate。xml中配置
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月24日 上午10:00:43
 *          Copyright 2016 by DigitalChina
 */
public class ServeDataBaseInfoService {

    private static Logger log = LoggerFactory.getLogger(ServeDataBaseInfoService.class);

    @Resource
    private NamedParameterJdbcTemplate jdbcT;

    // SQL语句构造器对象map
    private Map<Integer, ISqlBuilder> sqlBuilders = new HashMap<Integer, ISqlBuilder>();

    /**
     * 查询指定数据源所有表信息
     *
     * @param tableName 数据库表明，空查询全部
     * @param source 数据源java对象
     * @return 返回指定数据库中所有的用户表
     * @author zhongdt 2016年3月23日
     */
    public List<String> queryDbTables(String tableName, ServeDataSource source) {
        // 根据数据库类型取到builder，builder在xml中配置
        ISqlBuilder sqlBuilder = sqlBuilders.get(source.getSourceType());
        if (sqlBuilder == null) {
            return new ArrayList<String>();
        }
        String schema = getSchema(source.getSourceUrl());
        // 调用基类方法，生成查询表sql
        String sql = sqlBuilder.buildTableInfoSql(schema, tableName);
        // 生成sql所需参数
        Map<String, Object> param = sqlBuilder.buildQueryTableParam(schema, tableName);
        // 切换数据源
        DbContextHolder.setDbType(source.getId() + "");
        try {
            return jdbcT.queryForList(sql, param, String.class);
        } catch (DataAccessException e) {
            log.warn("查询指定数据库用户表失败,type="+source.getSourceType());
            // sql报错，则返回空对象
            return new ArrayList<String>();
        }

    }

    /**
     * 查询指定数据库，数据表列名信息
     *
     * @param tableName 数据库表明
     * @param source 数据源java对象
     * @return 返回指定数据源中某一张表的所有字段信息，包块类型，描述，名称
     * @author zhongdt 2016年3月23日
     */
    public List<DataBaseColumnPo> queryTableColumnInfo(String tableName, ServeDataSource source) {
        // 根据数据库类型取到对应的sqlBuilder
        ISqlBuilder sqlBuilder = sqlBuilders.get(source.getSourceType());
        if (sqlBuilder == null) {
            return new ArrayList<DataBaseColumnPo>();
        }

        String schema = getSchema(source.getSourceUrl());
        // 生成查询指定表列信息的sql
        String sql = sqlBuilder.buildColumnInfoSql(schema, tableName);
        // 生成查询参数
        Map<String, Object> param = sqlBuilder.buildQueryColumnParam(schema, tableName);
        // 切换数据源
        DbContextHolder.setDbType(source.getId() + "");

        return queryForListBean(sql, param, DataBaseColumnPo.class);

    }

    /************************************************** 私有方法 ********************************************************************/
    /**
     * 解析访问url获取数据源的schema
     *
     * @param jdbcUrl
     *            :jdbc:mysql://172.28.1.213:3307/shh1?useUnicode=true&amp;characterEncoding=utf8
     *            &amp;
     * @return
     * @author zhongdt 2016年3月23日
     */
    private  String getSchema(String jdbcUrl) {
        int endIndex = jdbcUrl.indexOf("?");
        if (endIndex <= 0) {
            return null;
        }
        int beginIndex = jdbcUrl.substring(0, endIndex).lastIndexOf("/") + 1;
        return jdbcUrl.substring(beginIndex, endIndex);
    }

    /**
     * JdbcTemplate queryForList(String,Param,CLASS T) 不支持返回多列的方法，需要自己封装
     *
     * @param sql 查询sql
     * @param param 查询param
     * @param T 返回类型
     * @return
     * @author zhongdt 2016年3月24日
     */
    private <T> List<T> queryForListBean(String sql, Map<String, Object> param, Class<?> T) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // sql 执行报错，则返回空
        try {
            list = jdbcT.queryForList(sql, param);
        } catch (DataAccessException e) {
            return new ArrayList<T>();
        }

        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            try {
                @SuppressWarnings("unchecked")
                // 将map转换为指定bean
                T po = (T) BeanUtils.convertMap(T, map);
                result.add(po);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException
                    | IntrospectionException e) {
                log.error("数据库操作执行sql语句封装bean失败", e);
                continue;
            }
        }
        return result;
    }

    public Map<Integer, ISqlBuilder> getSqlBuilders() {
        return sqlBuilders;
    }

    public void setSqlBuilders(Map<Integer, ISqlBuilder> sqlBuilders) {
        this.sqlBuilders = sqlBuilders;
    }

//    public static void main(String[] args) {
//        System.out.println(getSchema("jdbc:mysql://172.28.1.213:3307/shh1?useUnicode=true&characterEncoding=utf8"));
//    }

}
