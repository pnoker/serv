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
package com.dc.city.domain.datasource;

import java.util.Date;

/**
 * 
 * 数据源实体类
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月28日 下午4:50:19
 *          Copyright 2016 by DigitalChina
 */

public class ServeDataSource {
    
    private long id;

    private String sourceName;

    //数据库类型(1:oracle,2:sqlserver,3:mysql)
    private Integer sourceType;
    
    //是否删除 ， 0:否，1:是
    private Integer isDeleted;
    //连接驱动
    private String sourceClass;
    //访问地址
    private String sourceUrl;
    //账号
    private String sourceUser;
    //密码
    private String sourcePass;

    private String sourceRemark;

    private Date updatetime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}