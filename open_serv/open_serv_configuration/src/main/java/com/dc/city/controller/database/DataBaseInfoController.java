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
package com.dc.city.controller.database;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Controller;

import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.domain.datasource.ServeDataSource;
import com.dc.city.pojo.datasource.DataBaseColumnPo;
import com.dc.city.service.cache.PubCacheService;
import com.dc.city.service.database.ServeDataBaseInfoService;
import com.dc.city.service.datasource.ServeDataSourceService;
import com.dc.city.vo.database.datasource.DataSourceBaseVo;

/**
 * 数据库信息获取接口
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月8日 上午9:46:38
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/dbinfo/v1")
@Produces({ "application/json" })
public class DataBaseInfoController {

    // 数据源service
    @Resource
    private ServeDataSourceService dataSourceService;

    // 获取数据表，列信息
    @Resource
    private ServeDataBaseInfoService dbInfoService;

    // 动态数据源bean
    @Resource
    private DynamicDataSource dymanicDataSource;

    // 消息发布service
    @Resource
    private PubCacheService pubService;

    /**
     * 查询指定数据源所有的用户表
     *
     * @param dataSourceId
     * @param tableName 支持模糊查找
     * @return
     * @author zhongdt 2016年3月25日
     */
    @GET
    @Path("/queryAllTables")
    public DataSourceBaseVo queryTableList(@FormParam("dataSource") String dataSourceKey,
            @FormParam("tableName") String tableName) {
        if (StringUtils.isNullOrEmpty(dataSourceKey)) {
            return new DataSourceBaseVo("-1", "请输入数据源");
        }
        // 在内存中验证数据源信息
        if (!dymanicDataSource.isExists(dataSourceKey)) {
            return new DataSourceBaseVo("-1", "无效的数据源");
        }
        // 验证数据源连接状态
        if (!dymanicDataSource.isValid(dataSourceKey)) {
            return new DataSourceBaseVo("-1", "数据源连接状态不正常");
        }

        // 获取数据源信息的详细信息
        ServeDataSource sourceBean = dataSourceService.find(Long.valueOf(dataSourceKey));
        if (sourceBean == null) {
            return new DataSourceBaseVo("-1", "无效的数据源");
        }

        // 获取当前数据源下面的所有用户表
        List<String> tableNames = new ArrayList<String>();
        try {
            tableNames = dbInfoService.queryDbTables(tableName, sourceBean);
        } catch (Exception e) {
            return new DataSourceBaseVo("-1", e.getMessage());
        }
        DataSourceBaseVo vo = new DataSourceBaseVo();
        vo.setDatas(tableNames);
        return vo;

    }

    /**
     * 获取指定表的列信息
     *
     * @param dataSourceId 必填
     * @param tableName 必填
     * @return
     * @author zhongdt 2016年3月25日
     * @throws Exception
     */
    @GET
    @Path("/queryTableColumn")
    public DataSourceBaseVo queryTableColumnList(@FormParam("dataSource") String dataSourceKey,
            @FormParam("tableName") String tableName) {

        // 验证前端传递的数据源参数是否正确
        if (StringUtils.isNullOrEmpty(dataSourceKey)) {
            return new DataSourceBaseVo("-1", "请输入数据源");
        }
        // 验证数据源存在
        if (!dymanicDataSource.isExists(dataSourceKey)) {
            return new DataSourceBaseVo("-1", "无效的数据源");
        }
        // 验证数据源连接状态
        if (!dymanicDataSource.isValid(dataSourceKey)) {
            return new DataSourceBaseVo("-1", "数据源连接状态不正常");
        }

        ServeDataSource sourceBean = dataSourceService.find(Long.valueOf(dataSourceKey));
        if (sourceBean == null) {
            return new DataSourceBaseVo("-1", "无效的数据源");
        }
        if (!StringUtils.isNullOrEmpty(tableName)) {
            tableName = tableName.toUpperCase();
        }
        List<DataBaseColumnPo> columns = new ArrayList<DataBaseColumnPo>();
        // 表列信息
        columns = dbInfoService.queryTableColumnInfo(tableName, sourceBean);

        DataSourceBaseVo vo = new DataSourceBaseVo();
        vo.setDatas(columns);
        return vo;
    }

}
