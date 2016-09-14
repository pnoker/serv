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

import java.io.Serializable;

/**
 * 输入参数
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月10日 下午2:12:32
 *          Copyright 2016 by DigitalChina
 */
public class ServeInputParamNav implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1343844084414260100L;
    long id;
    long serviceId;
    // 表列名
    String columnCode;
    // 参数名
    String paramCode;
    // 列描述
    String columnDesc;
    // 运算符
    String operator;
    // 是否必填(0:否 1:是)
    Integer isRequired;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        if (isRequired.equalsIgnoreCase("true") || isRequired.equalsIgnoreCase("1")) {
            this.isRequired = 1;
        } else {
            this.isRequired = 0;
        }
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
