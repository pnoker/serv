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
package com.dc.city.dao.master.datasource;

import java.util.List;

import com.dc.city.domain.datasource.ServeDataSource;
import com.dc.city.pojo.datasource.DataSourceManagePo;

public interface ServeDataSourceMapper {
    int deleteByPrimaryKey(long id);

    int insert(ServeDataSource record);
    
    ServeDataSource selectByPrimaryKey(long id);

    List<ServeDataSource> queryDataSources(ServeDataSource source);

    int updateByPrimaryKey(ServeDataSource record);

    /**
     * 分页查询 数据源列表
     *
     * @param sourcePo
     * @return
     * @author zhongdt 2016年3月9日
     */
    List<ServeDataSource> queryDataSourceList(DataSourceManagePo sourcePo);

    /**
     * 获取数据源总条数
     *
     * @param sourcePo
     * @return
     * @author zhongdt 2016年3月10日
     */
    long getTotalCount(DataSourceManagePo sourcePo);

    /**
     * 
     * 创建数据源
     *
     * @param sourcePo
     * @return
     * @author zhongdt 2016年3月28日
     */
    int createDataSource(DataSourceManagePo sourcePo);
    

}