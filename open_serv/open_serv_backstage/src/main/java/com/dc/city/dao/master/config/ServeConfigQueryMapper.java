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
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dc.city.pojo.serve.config.ServeConfigQueryPo;
import com.dc.city.pojo.serve.config.ServeCount;

/**
 * 服务配置 查询专用
 * 提供分页列表及总条数查询
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月28日 上午9:52:01
 *          Copyright 2016 by DigitalChina
 */
public interface ServeConfigQueryMapper {

    long getTotalCount(ServeConfigQueryPo po);

    // 列表查询
    List<ServeConfigQueryPo> queryPage(ServeConfigQueryPo po);

    List<Map<String, Object>> queryAuthUserByConfig(@Param("serviceId") long serviceId);

    /**
     * 查询不同服务的已发布条数 和 其他状态条数
     *
     * @return
     * @author chenzpa 2016年8月15日
     */
    List<ServeCount> queryServiceListNum();

    /**
     * 模糊查询
     *
     * @param query 查询条件
     * @param matchNum 匹配条数
     * @param tableName 表名字
     * @param propertys 属性名字
     * @return
     * @author chenzpa 2016年7月29日
     */
    List<String> fuzzyQuery(@Param("query") String query, @Param("matchNum") String matchNum,
            @Param("tableName") String tableName, @Param("list") List<String> tablePropertys);
}