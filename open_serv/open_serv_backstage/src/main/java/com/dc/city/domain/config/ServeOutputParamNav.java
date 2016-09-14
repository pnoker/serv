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
 * 输出参数
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月10日 下午2:12:32
 *          Copyright 2016 by DigitalChina
 */
public class ServeOutputParamNav implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2653017834309310400L;
    String id;
    Long serviceId;
    // 字段名
    String columnCode;
    // 字段备注
    String columnDesc;
    // 参数别名
    String columnLias;
    // 排序类型(desc asc)
    String sortOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
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

    public String getColumnLias() {
        return columnLias;
    }

    public void setColumnLias(String columnLias) {
        this.columnLias = columnLias;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

}
