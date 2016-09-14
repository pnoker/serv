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
 * 日期区间
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月10日 下午2:33:14
 *          Copyright 2016 by DigitalChina
 */
public class ServeDataRange implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8054714936331101130L;
    String id;
    long serviceId;
    // 列名
    String columnCode;
    // 近xx天
    String dataRange;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getDataRange() {
        return dataRange;
    }

    public void setDataRange(String dataRange) {
        this.dataRange = dataRange;
    };

}