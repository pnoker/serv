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

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Controller;

import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.common.datasource.DynamicDataSource.DbContextHolder;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.controller.database.datasource.DataSourceManageController;
import com.dc.city.service.database.ServeDataBaseExcuteService;
import com.dc.city.vo.database.DataBaseExcuteVo;
import com.dc.city.vo.database.datasource.DataSourceBaseVo;
import com.dc.city.vo.database.datasource.DataSourceManageVo;

/**
 * SQL数据库操作controller
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月8日 上午9:46:38
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/sqloperation/v1")
@Produces({ "application/json" })
public class SqlOperationController {

    // 为了控制事务，这里使用service的代理
    @Resource
    private ServeDataBaseExcuteService dataBaseService;

    @Resource
    private DynamicDataSource dataSourceBean;

    //数据库查询操作时，如果返回条数过多，容易造成系统性能下降，因此设定此参数，默认返回条数
    public static final int FETCH_SIZE = 100;
    @Resource
    private DataSourceManageController dataSourceController;

    /**
     * 
     * 执行数据库操作controller
     *
     * @param dataSource 数据源id
     * @param excuteSql sql语句
     * @param fetchSize select时，获取条数
     * @return
     * @author zhongdt 2016年5月5日
     */
    @POST
    @Path("/excute")
    public DataBaseExcuteVo excuteSql(@FormParam("dataSource") String dataSourceKey, @FormParam("sql") String excuteSql,
            @FormParam("fetchSize") int fetchSize) {
        DataBaseExcuteVo vo = new DataBaseExcuteVo("-1", "数据库操作异常");
        if (StringUtils.isNullOrEmpty(dataSourceKey)) {
            vo.setResultInfo("数据源错误");
            return vo;
        }

        if (StringUtils.isNullOrEmpty(excuteSql)) {
            vo.setResultInfo("操作语句为空");
            return vo;
        }
        //默认15条
        fetchSize = fetchSize == 0 ? 15 : fetchSize;

        // 判断是否存在数据源
        if (!dataSourceBean.isExists(dataSourceKey)) {
            vo.setResultInfo("数据源不存在");
            return vo;
        }

        // 删除最后一个";"并返回删除之后的
        excuteSql = verifySql(excuteSql);
        if (StringUtils.isNullOrEmpty(excuteSql)) {
            vo.setResultInfo("不支持多条SQL语句");
            return vo;
        }
        // 验证对应数据源状态
        if (!dataSourceBean.isValid(dataSourceKey)) {
            vo.setResultInfo("数据源配置或状态不正常,请联系管理员");
            return vo;
        }
        // 切换数据源
        DbContextHolder.setDbType(dataSourceKey);

        //执行sql操作，返回vo
        try {
            return dataBaseService.doExcute(excuteSql, fetchSize);
        } catch (Exception e) {
            return new  DataBaseExcuteVo("-1", e.getMessage());
        }

    }
    
    /**
     * 
     * 获取数据源列表
     *
     * @param sourceVo
     * @return
     * @author zhongdt 2016年5月5日
     */

    @GET
    @Path("/datasourcelist")
    public DataSourceBaseVo dataSourceList(@BeanParam DataSourceManageVo sourceVo) {
        return dataSourceController.queryDataSourceList(sourceVo);

    }

    /**
     * 验证SQL语句是否满足，业务规定
     * 
     * @remark 一次只能执行一个语句，
     * @param sql
     * @return
     * @author zhongdt 2016年3月8日
     */
    private String verifySql(String sql) {
        //如果不存在;直接返回
        if (sql.indexOf(";") < 0) {
            return sql;
        } else {
            //分号在最后也可以执行
            String[] arrs = sql.split(";");
            if (arrs.length == 1) {
                return arrs[0];
            }
            //逗号后面如果不为空则表示不止一个语句 
            if (!StringUtils.isNullOrEmpty(arrs[1])) {
                return null;
            } else {
                return arrs[0];
            }
        }
    }
}
