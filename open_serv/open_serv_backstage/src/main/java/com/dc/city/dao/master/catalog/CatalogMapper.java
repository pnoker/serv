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
package com.dc.city.dao.master.catalog;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dc.city.pojo.serve.catalog.CatalogPo;

/**
 * 服务目录管理dao
 *
 * @author ligen
 * @version V1.0 创建时间：2016年3月9日 下午4:38:15
 *          Copyright 2016 by DigitalChina
 */
public interface CatalogMapper {

    int deleteCatalogById(long id);

    int addCatalogo(CatalogPo catolog);

    List<CatalogPo> selectAllCatalog();

    /**
     * 根据子节点，查询父目录
     *
     * @param sonId 子节点id
     * @return
     * @author ligen 2016年5月9日
     */
    List<CatalogPo> queryParentCataLogById(@Param("childId") Integer childId);

    int updateCatalogById(CatalogPo record);

    CatalogPo queryCatalogById(int id);

    /**
     *  根据条件生成目录树状菜单
     *
     * @param po 目录条件（cataloPo对象）
     * @return
     * @author ligen 2016年5月9日
     */
    List<CatalogPo> queryCatalogTree(CatalogPo po);

    /**
     * 根据服务目录id查询服务目录具体信息
     *
     * @param id
     * @return
     * @author ligen 2016年5月9日
     */
    CatalogPo selectByPrimaryKey(long id);

    /**
     * 查询子节点数量
     *
     * @param cataLogId 服务目录id
     * @return
     * @author ligen 2016年5月9日
     */
    long queryChildNum(@Param("cataLogId") long cataLogId);

}
