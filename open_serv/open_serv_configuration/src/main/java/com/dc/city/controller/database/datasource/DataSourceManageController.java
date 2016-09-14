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
package com.dc.city.controller.database.datasource;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Controller;

import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.domain.datasource.ServeDataSource;
import com.dc.city.service.datasource.ServeDataSourceService;
import com.dc.city.vo.database.datasource.DataSourceBaseVo;
import com.dc.city.vo.database.datasource.DataSourceManageVo;

/**
 * 数据源管理controller
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月10日 上午11:30:20
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/datasource/v1")
@Produces({ "application/xml", "application/json" })
public class DataSourceManageController {

    @Resource
    private ServeDataSourceService dataSourceService;

    @Resource
    private DynamicDataSource dymanicDataSource;

    /**
     * 
     * 分页查询数据源列表
     *
     * @param sourceVo 参数封装对象，详情DataSourceManageVo
     * @return
     * @author zhongdt 2016年5月5日
     */
    @GET
    @Path("/query")
    public DataSourceBaseVo queryDataSourceList(@BeanParam DataSourceManageVo sourceVo) {
        return dataSourceService.queryForPage(sourceVo);
    }

    /**
     * 
     * 创建数据源
     *
     * @param sourceVo 参数封装对象，详情DataSourceManageVo
     * @return
     * @author zhongdt 2016年5月5日
     */
    @POST
    @Path("/create")
    public DataSourceBaseVo editDataSource(@BeanParam DataSourceManageVo sourceVo) {
        return dataSourceService.createDataSource(sourceVo);
    }

    /**
     * 
     * 修改数据源
     *
     * @param sourceVo 参数封装对象，详情DataSourceManageVo
     * @return
     * @author zhongdt 2016年5月5日
     */
    @POST
    @Path("/modify")
    public DataSourceBaseVo createDataSource(@BeanParam DataSourceManageVo sourceVo) {
        return dataSourceService.modifyDataSource(sourceVo);
    }

    /**
     * 
     * 删除数据源
     *
     * @param sourceVo
     * @return
     * @author zhongdt 2016年5月5日
     */
    @POST
    @Path("/remove")
    public DataSourceBaseVo deleteDataSource(@BeanParam DataSourceManageVo sourceVo) {
        return dataSourceService.removeDataSource(sourceVo);
    }

    /**
     * 
     * 手动检查数据源连接状态，用于在数据源监控失败后进行
     *
     * @param dataSourceId
     * @return
     * @author zhongdt 2016年4月29日
     */
    @POST
    @Path("/recheck")
    public DataSourceBaseVo checkDataSource(@FormParam("dataSourceId") long dataSourceId) {
        //check数据源是否存在
        ServeDataSource dataSource = dataSourceService.find(dataSourceId);
        if (dataSource != null && dataSource.getIsDeleted() != 0) {
            return new DataSourceBaseVo("-1", "数据源已被删除");
        }
        
        //调用service方法，check
        return dataSourceService.checkDataSource(dataSourceId+"");
    }
}