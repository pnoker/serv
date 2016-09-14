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
package com.dc.city.dao.master.config;

import java.util.List;

import com.dc.city.domain.config.ServeGisDictionary;

public interface ServeGisDictionaryMapper {
    int deleteByPrimaryKey(long id);

    int insert(ServeGisDictionary record);

    int insertSelective(ServeGisDictionary record);

    ServeGisDictionary selectByPrimaryKey(long id);

    int updateByPrimaryKeySelective(ServeGisDictionary record);

    int updateByPrimaryKey(ServeGisDictionary record);

    /**
     * 将数据字典删掉
     *
     * @param configerId
     * @author chenzpa 2016年4月12日
     */
    void removeGisDictionaryParams(long configerId);

    /**
     * 查询GIS服务 中的字典列表信息
     *
     * @param gisConfigId
     * @return
     * @author chenzpa 2016年4月12日
     */
    List<ServeGisDictionary> queryGisConfigerDictionary(long gisConfigId);
}