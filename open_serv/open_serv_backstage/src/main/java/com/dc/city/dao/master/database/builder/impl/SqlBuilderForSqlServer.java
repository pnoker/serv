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
 * sqlserver2005 语法构造器
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月23日 下午5:34:54
 *          Copyright 2016 by DigitalChina
 */
@Component
public class SqlBuilderForSqlServer implements ISqlBuilder {

    @Override
    public String buildTableInfoSql(String schema, String tableName) {
        StringBuffer sqlBuffer = new StringBuffer("select name from sys.tables where 1 = 1");
        if (!StringUtils.isNullOrEmpty(tableName)) {
            sqlBuffer.append(" and upper(NAME) like :tableName");
        }
        return sqlBuffer.toString();
    }

    @Override
    public String buildColumnInfoSql(String schema, String tableName) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT");
        sqlBuffer.append(" [ColumnName] = [Columns].name,");
        sqlBuffer.append(" [ColumnType] = [Types].name,");
        sqlBuffer.append(" [ColumnDesc] = CAST ([Properties].VALUE AS VARCHAR (500))");
        sqlBuffer.append(" FROM sys.tables AS [Tables]");
        sqlBuffer.append(" INNER JOIN sys.columns AS [Columns] ");
        sqlBuffer.append(" ON [Tables].object_id = [Columns].object_id");
        sqlBuffer.append(" INNER JOIN sys.types AS [Types] ");
        sqlBuffer.append(" ON [Columns].system_type_id = [Types].system_type_id");
        sqlBuffer.append(" AND is_user_defined = 0 AND [Types].name <> 'sysname'");
        sqlBuffer.append(" LEFT OUTER JOIN sys.extended_properties AS [Properties]"); 
        sqlBuffer.append(" ON [Properties].major_id = [Tables].object_id");
        sqlBuffer.append(" AND [Properties].minor_id = [Columns].column_id");
        sqlBuffer.append(" AND [Properties].name ='MS_Description'");
        sqlBuffer.append(" WHERE [Tables].name = :tableName");
        sqlBuffer.append(" ORDER BY [Columns].column_id");
        return sqlBuffer.toString();

    }

    @Override
    public Map<String, Object> buildQueryTableParam(String schema, String tableName) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (!StringUtils.isNullOrEmpty(tableName)) {
            param.put("tableName", tableName + "%");
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
