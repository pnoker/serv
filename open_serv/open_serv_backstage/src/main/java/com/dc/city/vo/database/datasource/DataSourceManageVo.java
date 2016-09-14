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
package com.dc.city.vo.database.datasource;

import java.util.Date;

import javax.ws.rs.FormParam;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.dc.city.vo.PageVo;

/**
 * 数据源查询 数据封装
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月9日 下午6:03:56
 *          Copyright 2016 by DigitalChina
 */
public class DataSourceManageVo extends PageVo {

    @FormParam("sourceName")
    private String sourceName;

    @FormParam("sourceType")
    private Integer sourceType;

    @FormParam("isDeleted")
    private Integer isDeleted;

    @FormParam("id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String sourceClass;

    @FormParam("sourceUrl")
    private String sourceUrl;
    @FormParam("sourceUser")
    private String sourceUser;
    @FormParam("sourcePass")
    private String sourcePass;
    @FormParam("sourceRemark")
    private String sourceRemark;

    @JsonIgnore
    private Date updatetime;

    public String getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public String getSourcePass() {
        return sourcePass;
    }

    public void setSourcePass(String sourcePass) {
        this.sourcePass = sourcePass;
    }

    public String getSourceRemark() {
        return sourceRemark;
    }

    public void setSourceRemark(String sourceRemark) {
        this.sourceRemark = sourceRemark;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

}
