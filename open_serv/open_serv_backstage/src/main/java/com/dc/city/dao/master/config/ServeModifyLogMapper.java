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

import com.dc.city.domain.config.ServeModifyLog;

public interface ServeModifyLogMapper {
    int deleteByPrimaryKey(long id);

    int insert(ServeModifyLog record);

    int insertSelective(ServeModifyLog record);

    ServeModifyLog selectByPrimaryKey(long id);

    int updateByPrimaryKeySelective(ServeModifyLog record);

    int updateByPrimaryKey(ServeModifyLog record);

    /**
     * 更新记录 查询
     *
     * @param parseLong
     * @return
     * @author chenzpa 2016年4月14日
     */
    List<ServeModifyLog> queryUpdateRecode(long id);
}