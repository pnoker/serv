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
package com.dc.city.dao.master.database.builder.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.city.common.utils.StringUtils;
import com.dc.city.dao.master.database.builder.ISqlBuilder;

/**
 * ORACLE 类型数据库语法构造器
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月23日 下午5:34:54
 *          Copyright 2016 by DigitalChina
 */
@Component
public class SqlBuilderForOracle implements ISqlBuilder {

    @Override
    public String buildTableInfoSql(String schema, String tableName) {
        StringBuffer sqlBuffer = new StringBuffer("Select table_name from user_tables where 1 = 1");
        if (!StringUtils.isNullOrEmpty(tableName)) {
            sqlBuffer.append(" and upper(table_name) like :tableName");
        }
        return sqlBuffer.toString();
    }

    @Override
    public String buildColumnInfoSql(String schema, String tableName) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT ");
        sqlBuffer.append(" USER_TAB_COLS.COLUMN_NAME as columnName,");
        sqlBuffer.append(" USER_TAB_COLS.DATA_TYPE as columnType,");
        sqlBuffer.append(" user_col_comments.comments as columnDesc");
        sqlBuffer.append(" FROM USER_TAB_COLS");
        sqlBuffer.append(" inner join user_col_comments on");
        sqlBuffer.append(" user_col_comments.TABLE_NAME=USER_TAB_COLS.TABLE_NAME");
        sqlBuffer.append(" and user_col_comments.COLUMN_NAME=USER_TAB_COLS.COLUMN_NAME");
        sqlBuffer.append(" where   UPPER(USER_TAB_COLS.Table_Name)=UPPER(:tableName)");
        return sqlBuffer.toString();
    }

    @Override
    public Map<String, Object> buildQueryTableParam(String schema, String tableName) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (!StringUtils.isNullOrEmpty(tableName)) {
            param.put("tableName", tableName.toUpperCase() + "%");
        }
        return param;
    }

    @Override
    public Map<String, Object> buildQueryColumnParam(String schema, String tableName) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableName", tableName);
        return param;
    }
}
