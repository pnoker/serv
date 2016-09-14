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
package com.dc.city.domain.config;

import java.util.Date;

public class ServeGisDictionary {
    private long id;

    private long configId;

    private String filedName;

    private String filedType;

    private Short filedLength;

    private Short decimalLength;

    private String isNull;

    private String filedDesc;

    private String fieldRemark;

    private Date updatetime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configerId) {
        this.configId = configerId;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getFiledType() {
        return filedType;
    }

    public void setFiledType(String filedType) {
        this.filedType = filedType;
    }

    public Short getFiledLength() {
        return filedLength;
    }

    public void setFiledLength(Short filedLength) {
        this.filedLength = filedLength;
    }

    public Short getDecimalLength() {
        return decimalLength;
    }

    public void setDecimalLength(Short decimalLength) {
        this.decimalLength = decimalLength;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public String getFiledDesc() {
        return filedDesc;
    }

    public void setFiledDesc(String filedDesc) {
        this.filedDesc = filedDesc;
    }

    public String getFieldRemark() {
        return fieldRemark;
    }

    public void setFieldRemark(String fieldRemark) {
        this.fieldRemark = fieldRemark;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}