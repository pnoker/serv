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
package com.dc.city.pojo.serve.catalog;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 服务目录实体类
 *
 * @author ligen
 * @version V1.0 创建时间：2016年3月9日 下午4:36:14
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "catalogPo")
public class CatalogPo {
    /*
     * 目录id
     */
    @FormParam("id")
    private Integer id;
    /*
     * 目录父id
     */
    @FormParam("parentId")
    private Integer pid;
    /*
     * 目录名称
     */
    @FormParam("catalogName")
    private String catalogName;
    /*
     * 目录备注
     */
    @FormParam("catalogRemark")
    private String catalogRemark;
    /*
     * 目录级数
     */
    private Integer treeLevel;
    /*
     * 是否能删除
     */
    @FormParam("isDeleted")
    private Integer isDeleted;

    @FormParam("isInner")
    private Integer isInner;

    private Integer serviceType;

    public Integer getIsInner() {
        return isInner;
    }

    public void setIsInner(Integer isInner) {
        this.isInner = isInner;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogRemark() {
        return catalogRemark;
    }

    public void setCatalogRemark(String catalogRemark) {
        this.catalogRemark = catalogRemark;
    }

    public Integer getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

}
