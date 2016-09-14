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
package com.dc.city.service.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dc.city.dao.master.catalog.CatalogMapper;
import com.dc.city.pojo.serve.catalog.CatalogPo;
import com.dc.city.vo.BaseVo;
import com.dc.city.vo.catalog.CataLogQueryVo;
import com.dc.city.vo.catalog.CatalogBaseVo;

/**
 * 目录管理service
 *
 * @author ligen
 * @version V1.0 创建时间：2016年3月8日 下午3:28:29
 *          Copyright 2016 by DigitalChina
 */
@Service
public class CatalogService {

    @Resource
    private CatalogMapper catalogMapper;

    /**
     * 查询全部目录树
     *
     * @return
     * @author ligen 2016年3月8日
     */
    public CatalogBaseVo queryCatalogTree() {
        List<CatalogPo> catalogList = catalogMapper.selectAllCatalog();
        CatalogBaseVo vo = new CatalogBaseVo();
        vo.setDatas(catalogList);
        return vo;
    }

    /**
     * 根据目id查询目录信息
     *
     * @param catalogId 目录id
     * @return
     * @author ligen 2016年3月8日
     */
    public CatalogBaseVo queryCatalogById(int id) {
        List<CatalogPo> catalogList = new ArrayList<CatalogPo>();
        CatalogPo catalog = catalogMapper.queryCatalogById(id);
        CatalogBaseVo vo = new CatalogBaseVo();
        catalogList.add(catalog);
        vo.setDatas(catalogList);
        return vo;
    }

    /**
     * 根据服务目录主键查询服务目录信息
     *
     * @param id
     * @return
     * @author ligen 2016年5月9日
     */
    public CatalogPo selectByPrimaryKey(long id) {
        return this.catalogMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增子目录
     *
     * @param catalogPo 新增服务目录对象
     * @return
     * @author ligen 2016年5月9日
     */
    public CatalogBaseVo addCatalog(CatalogPo catalogPo) {
        int catalogCount = catalogMapper.addCatalogo(catalogPo);
        CatalogBaseVo baseVo = new CatalogBaseVo();
        // 如果catalogCount>0就说明有数据插入（插入成功）
        if (catalogCount > 0) {
            String result = "新增成功";
            baseVo.setResultInfo(result);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
        }
        return baseVo;
    }

    /**
     * 修改服务目录
     *
     * @param catalogPo 修改服务目录对象
     * @return
     * @author ligen 2016年5月9日
     */
    public CatalogBaseVo modifyCatalog(CatalogPo catalogPo) {
        int catalogCount = catalogMapper.updateCatalogById(catalogPo);
        CatalogBaseVo baseVo = new CatalogBaseVo();
        // catalogCount表示修改条数，如果i>0表示有数据修改成功
        if (catalogCount > 0) {
            String result = "修改成功";
            baseVo.setResultInfo(result);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
        }
        return baseVo;
    }

    /**
     * 删除选定目录
     *
     * @param catalogId 服务目录id
     * @return
     * @author ligen 2016年3月8日
     */
    public CatalogBaseVo delCatalog(Long id) {
        int catalogCount = catalogMapper.deleteCatalogById(id);
        CatalogBaseVo baseVo = new CatalogBaseVo();
        // catalogCount表示刪除条数，如果i>0表示有数据删除
        if (catalogCount > 0) {
            String result = "删除成功";
            baseVo.setResultInfo(result);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
        }
        return baseVo;
    }

    /**
     * 根据父节点，获取父节下面的所有子节点
     *
     * @param config
     * @return
     * @author zhongdt 2016年3月31日
     */
    public CatalogBaseVo queryCatalogTree(CataLogQueryVo catalogVo) {
        CatalogPo catalogPo = new CatalogPo();
        BeanUtils.copyProperties(catalogVo, catalogPo);
        if (catalogPo.getPid() == null || catalogPo.getPid() < 0) {
            catalogPo.setPid(0);
        }
        List<CatalogPo> catalogList = catalogMapper.queryCatalogTree(catalogPo);
        CatalogBaseVo vo = new CatalogBaseVo();
        vo.setDatas(catalogList);
        return vo;

    }

    /**
     * 检查是否能删除当前目录
     *
     * @param catalogId
     * @return
     * @author zhongdt 2016年5月27日
     */
    public boolean checkWhetherCanDelete(Long catalogId) {
        // 查询的是是否存在目录或者是未删除的服务
        return this.catalogMapper.queryChildNum(catalogId) > 0 ? false : true;
    }

    /**
     * 根据子节点递归查询全部父节点信息
     *
     * @param sonId 子节点id
     * @return
     * @author zhongdt 2016年5月3日
     */
    public List<CatalogPo> queryParentCataLogs(Integer childId) {
        return this.catalogMapper.queryParentCataLogById(childId);
    }
}
