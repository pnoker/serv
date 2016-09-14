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
package com.dc.city.dao.master.database.builder;

import java.util.Map;

/**
 * 数据库语句构造器接口
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月23日 下午5:32:56
 *          Copyright 2016 by DigitalChina
 */
public interface ISqlBuilder {
    /**
     * 生成查询当前数据库所有用户表sql
     *
     * @param schema (此参数部分数据库需要)
     * @param tableName 表名，支持模糊查找
     * @return
     * @author zhongdt 2016年3月25日
     */
    public String buildTableInfoSql(String schema, String tableName);

    /**
     * 生成查询某张表所有列信息的sql
     *
     * @param schema (此参数部分数据库需要)
     * @param tableName 必填参数
     * @return
     * @author zhongdt 2016年3月25日
     */
    public String buildColumnInfoSql(String schema, String tableName);

    // build表查询的参数
    public Map<String, Object> buildQueryTableParam(String schema, String tableName);

    // build列查询的参数
    public Map<String, Object> buildQueryColumnParam(String schema, String tableName);
}
