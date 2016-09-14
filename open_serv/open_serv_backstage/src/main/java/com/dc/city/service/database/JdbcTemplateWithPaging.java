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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * JdbcTemplate增加分页查询查询
 * demo
 */
@Service
public class JdbcTemplateWithPaging {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateWithPaging.class);

    @Resource
    private NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateWithPaging() {}

    /**
     * 分页查询
     *
     * @param sql
     *            查询的sql语句
     * @param args
     *            参数
     * @param start
     *            起始行
     * @param limit
     *            获取的行数
     * @return
     * @throws DataAccessException
     */
    public List<Map<String, Object>> queryPage(String sql, Map<String, Object> args, int start, int limit) {
        if (start <= 0 && limit <= 0) {
            return (List<Map<String, Object>>) jdbcTemplate.queryForList(sql, args);
        }
        if (start <= 1) {
            sql = getLimitString(sql, false);
            args.put("beginRowNum", limit);
        } else {
            sql = getLimitString(sql, true);
            args.put("beginRowNum", start);
            args.put("endRowNum", start + limit);
        }

        logger.info("paging sql : \n" + sql);
        return (List<Map<String, Object>>) jdbcTemplate.queryForList(sql, args);
    }

    /**
     * 分页查询
     *
     * @param sql
     *            查询的sql语句
     * @param start
     *            起始行
     * @param limit
     *            获取的行数
     * @return
     */
    public List<Map<String, Object>> queryPage(String sql, int start, int limit) {
        Map<String, Object> args = new HashMap<String, Object>();
        return this.queryPage(sql, args, start, limit);
    }

    /**
     * 分页查询
     *
     * @param sql
     *            查询的sql语句
     * @param start
     *            起始行
     * @param limit
     *            获取的行数
     * @param RowMapper
     * @return
     */
    public <T> List<T> queryPage(String sql, Map<String, Object> args, int start, int limit, RowMapper<T> rowMapper)
            throws DataAccessException {
        if (start <= 0 && limit <= 0) {
            return jdbcTemplate.query(sql, rowMapper);
        }

        if (start <= 1) {
            args.put("beginRowNum", start);
        } else {
            sql = getLimitString(sql, true);
            args.put("beginRowNum", start);
            args.put("endRowNum", start + limit);
        }

        logger.info("paging sql : \n" + sql);
        return jdbcTemplate.query(sql, args, rowMapper);
    }

    private String getLimitString(String sql, boolean hasOffset) {
        sql = sql.trim();
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        if (hasOffset) {
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("select * from ( ");
        }
        pagingSelect.append(sql);
        if (hasOffset) {
            pagingSelect.append(" ) row_ where rownum <= :endRowNum) where rownum_ > :beginRowNum");
        } else {
            pagingSelect.append(" ) where rownum <= :endRowNum");
        }

        if (isForUpdate) {
            pagingSelect.append(" for update");
        }
        return pagingSelect.toString();
    }

}