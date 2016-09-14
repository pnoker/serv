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
 * SQL 片段
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月10日 下午2:33:14
 *          Copyright 2016 by DigitalChina
 */
public class ServeSegment implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5891645228447233118L;
    String id;
    long serviceId;
    // 片段代码
    String segmentCode;
    // 入参代码
    String paramCode;
    // 实际需要替换的SQL
    String replaceSql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getReplaceSql() {
        return replaceSql;
    }

    public void setReplaceSql(String replaceSql) {
        this.replaceSql = replaceSql;
    }
}