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

import org.apache.ibatis.annotations.Param;

import com.dc.city.domain.config.ServeResultExample;

/**
 * 
 * 服务配置 查询专用
 * 提供分页列表及总条数查询
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月28日 上午9:52:01
 *          Copyright 2016 by DigitalChina
 */
public interface ServeResultExampleMapper {
   
    /**
     * 
     * 批量创建返回示例
     *
     * @param list
     * @return
     * @author zhongdt 2016年4月6日
     */
    int createResultExample(ServeResultExample example);
    
    
    List<ServeResultExample> queryAll();
    
    ServeResultExample selectByPrimaryKey(@Param("id") long id);
    
    //批量删除
    int deleteByServiceId(@Param("serviceId") long serviceId);
    
    List<ServeResultExample>  queryByServiceId(@Param("serviceId") long serviceId);
}