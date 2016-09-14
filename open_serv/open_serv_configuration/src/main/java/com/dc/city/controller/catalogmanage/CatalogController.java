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
package com.dc.city.controller.catalogmanage;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Controller;

import com.dc.city.common.utils.StringUtils;
import com.dc.city.pojo.serve.catalog.CatalogPo;
import com.dc.city.service.catalog.CatalogService;
import com.dc.city.vo.BaseVo;
import com.dc.city.vo.catalog.CataLogQueryVo;
import com.dc.city.vo.catalog.CatalogBaseVo;

/**
 * 服务的目录
 *
 * @author ligen
 * @version V1.0 创建时间：2016年3月8日 下午3:21:20
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/catalog/v1")
@Produces({ "application/xml", "application/json" })
public class CatalogController {
    @Resource
    private CatalogService catalogService;

    /**
     * 查询目录树
     * 
     * @param CataLogQueryVo:参数封装对象
     * @return
     * @author ligen 2016年3月8日
     */
    @GET
    @Path("/querycatalogtree")
    public CatalogBaseVo queryCatalogTree(@BeanParam CataLogQueryVo vo) {
        return catalogService.queryCatalogTree(vo);
    }

    /**
     * 根据目id查询目录信息
     *
     * @param catalogId 目录id
     * @return
     * @author ligen 2016年3月8日
     */
    @GET
    @Path("/querycatalogbyid")
    public CatalogBaseVo queryCatalogById(@QueryParam("catalogId") String catalogId) {
        int id = Integer.parseInt(catalogId);
        CatalogBaseVo vo = catalogService.queryCatalogById(id);
        return vo;
    }

    /**
     * 根据子节点id查询父目录
     *
     * @param childId 查询锁有父级节点信息
     * @return
     * @author zhongdt 2016年3月8日
     */
    @GET
    @Path("/queryparentcatalogbyid")
    public CatalogBaseVo queryCatalogById(@QueryParam("childId") Integer childId) {
        if (childId <= 0) {
            return new CatalogBaseVo();
        }
        List<CatalogPo> datas = catalogService.queryParentCataLogs(childId);
        CatalogBaseVo vo = new CatalogBaseVo();
        vo.setDatas(datas);
        return vo;
    }

    /**
     * 新增子目录
     *
     * @param catalogName 名称
     * @param parentCatalogId 父节点
     * @param catologRemarks 备注
     * @return
     * @author ligen 2016年3月8日
     */
    @POST
    @Path("/addcatalog")
    public CatalogBaseVo addCatalog(@FormParam("catalogName") String catalogName,
            @FormParam("parentId") Integer parentId, @FormParam("catalogRemark") String catalogRemark) {
        CatalogBaseVo baseVo = new CatalogBaseVo();
        CatalogPo catalogPo = new CatalogPo();
        // 对前端传来的字段判断
        if (StringUtils.isNullOrEmpty(catalogName)) {
            String erro = "目录名称不能为空";
            baseVo.setResultInfo(erro);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
            return baseVo;
        } else if (parentId == null) {
            String erro = "父目录不能为空";
            baseVo.setResultInfo(erro);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
            return baseVo;
        }
        // 需要判断外键parentId是否存在于数据库中
        CatalogPo parent = catalogService.selectByPrimaryKey(parentId);
        if (parent == null) {
            String erro = "父目录不能为空";
            baseVo.setResultInfo(erro);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
            return baseVo;
        }
        // 设置父类的type
        catalogPo.setServiceType(parent.getServiceType());

        catalogPo.setCatalogName(catalogName.trim());
        catalogPo.setPid(parentId);
        // 根据父节点level取到当前节点level
        int level = parent.getTreeLevel() == null ? 0 : parent.getTreeLevel().intValue() + 1;
        catalogPo.setTreeLevel(level);
        catalogPo.setCatalogRemark(catalogRemark == null ? "" : catalogRemark.trim());
        catalogPo.setIsDeleted(0);
        catalogPo.setServiceType(parent.getServiceType());
        baseVo = catalogService.addCatalog(catalogPo);
        return baseVo;
    }

    /**
     * 修改目录信息
     * 
     * @param catalogId 节点id
     * @param catalogName 名称
     * @param parentCatalogId 父节点id
     * @param catologRemarks 备注
     * @return
     * @author ligen 2016年3月8日
     */
    @POST
    @Path("/updatecatalog")
    public CatalogBaseVo updateCatalog(@FormParam("catalogId") Integer catalogId,
            @FormParam("catalogName") String catalogName, @FormParam("parentId") Integer parentId,
            @FormParam("catalogRemark") String catalogRemark) {
        CatalogBaseVo baseVo = new CatalogBaseVo();
        CatalogPo catalogPo = new CatalogPo();
        // 判断前端传来的参数是否合理
        if (StringUtils.isNullOrEmpty(catalogName)) {
            String erro = "目录名称不能为空";
            baseVo.setResultInfo(erro);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
            return baseVo;
        } else if (parentId == null) {
            String erro = "父目录不能为空";
            baseVo.setResultInfo(erro);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
            return baseVo;
        } else if (catalogId == null || catalogId < 0) {
            String erro = "没有选择目录";
            baseVo.setResultInfo(erro);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
            return baseVo;
        }
        // 需要判断外键parentId是否存在于数据库中
        CatalogPo parent = catalogService.selectByPrimaryKey(parentId);
        if (parent == null) {
            String erro = "父目录不能为空";
            baseVo.setResultInfo(erro);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
            return baseVo;
        }

        catalogPo.setCatalogName(catalogName.trim());
        catalogPo.setId(catalogId);
        catalogPo.setPid(parentId);
        int level = parent.getTreeLevel() == null ? 0 : parent.getTreeLevel().intValue() + 1;
        catalogPo.setTreeLevel(level);
        catalogPo.setCatalogRemark(catalogRemark == null ? "" : catalogRemark.trim());
        baseVo = catalogService.modifyCatalog(catalogPo);
        return baseVo;
    }

    /**
     * 删除选定目录
     *
     * @param catalogId 节点id
     * @return
     * @author ligen 2016年3月8日
     */
    @POST
    @Path("/delcatalog")
    public CatalogBaseVo delCatalog(@FormParam("catalogId") Long catalogId) {
        CatalogBaseVo baseVo = new CatalogBaseVo();
        if (catalogId == null || "".equals(catalogId)) {
            String erro = "没有选中具体目录";
            baseVo.setResultInfo(erro);
            baseVo.setResultCode(BaseVo.SUCCESS_CODE);
            return baseVo;
        }
        // 判断当前节点下面是否有正常的服务或者有目录
        if (!catalogService.checkWhetherCanDelete(catalogId)) {
            baseVo.setResultCode("-1");
            baseVo.setResultInfo("当前父节点目录不能删除");
            return baseVo;
        }
        baseVo = catalogService.delCatalog(catalogId);
        return baseVo;
    }

}
