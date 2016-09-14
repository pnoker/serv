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
package com.dc.city.dao.master.dataset;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dc.city.domain.dataset.DatasetMenu;
import com.dc.city.domain.dataset.DatasetServ;
import com.dc.city.domain.dataset.ServExample;
import com.dc.city.domain.dataset.ServModifyLog;
import com.dc.city.domain.dataset.ServRequestParams;
import com.dc.city.domain.dataset.ServResponseField;

/**
 * 数据汇总平台dao层
 * 
 * @author xutaog
 * @version V1.0 创建时间：2016年4月13日 下午3:17:03
 *          Copyright 2016 by DigitalChina
 */
public interface DatasetMapper {

    /**
     * 获取不需要经过权限验证的菜单
     *
     * @return
     * @author xutaog 2016年4月13日
     */
    List<DatasetMenu> queryNoVerifyDatasetMenus();

    /**
     * 获取需要经过权限验证的菜单，用于用户登录之后使用
     *
     * @param ids 当前用户可以访问的菜单或接口ID数组
     * @return
     * @author xutaog 2016年4月15日
     */
    List<DatasetMenu> queryVerifyDatasetMenus(@Param("ids") String[] ids);

    /**
     * 根据服务的父级ID获取当前不需要验证权限的服务
     * 
     * @param servName 模糊查询的服务名称
     * @param parentId 父级ID
     * @param startRow 起始行
     * @param endRow 结束行
     * @return
     * @author xutaog 2016年4月18日
     */
    List<DatasetServ> queryNoVerifyDatasetServByPageSizeAndParentId(@Param("servName") String servName,
            @Param("parentId") Long parentId, @Param("startRow") int startRow, @Param("endRow") int endRow);

    /**
     * 根据用户名称获取用户在当前下级节点下能访问的服务列表
     *
     * @param servName 模糊查询的服务名称
     * @param parentId 父级ID
     * @param startRow 起始页
     * @param endRow 结束页
     * @param userName 当前用户名
     * @return
     * @author xutaog 2016年4月18日
     */
    List<DatasetServ> queryVerifyDatasetServByPageSizeAndParentIdAndUserName(@Param("servName") String servName,
            @Param("parentId") Long parentId, @Param("startRow") int startRow, @Param("endRow") int endRow,
            @Param("userName") String userName);

    /**
     * 根据用户ID与用户的服务ID获取服务的基本信息
     *
     * @param id 服务id
     * @param userName 用户usename
     * @return
     * @author xutaog 2016年4月18日
     */
    DatasetServ queryDatasetServBasicInfoById(@Param("id") Long id, @Param("userName") String userName);

    /**
     * 根据服务的Id获取请求参数
     *
     * @param servId
     * @return
     * @author xutaog 2016年4月18日
     */
    List<ServRequestParams> queryServRequestParamsByServId(long servId);

    /**
     * 根据服务Id获取服务的返回参数
     *
     * @param servId
     * @return
     * @author xutaog 2016年4月18日
     */
    List<ServResponseField> queryServResponseFieldsByServId(long servId);

    /**
     * 根据服务的ID获取服务的例子
     *
     * @param servId
     * @return
     * @author xutaog 2016年4月19日
     */
    List<ServExample> queryServExampleByServId(long servId);

    /**
     * 根据服务id获取服务的更新记录
     *
     * @param servId
     * @return
     * @author xutaog 2016年4月19日
     */
    List<ServModifyLog> queryServModifyLogByServId(long servId);

}
