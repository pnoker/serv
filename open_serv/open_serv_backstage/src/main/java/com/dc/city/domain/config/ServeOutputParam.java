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
public class ServeOutputParam implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -379441734874929761L;
    String id;
    Long serviceId;
    // 参数名
    String paramCode;
    // 参数描述
    String paramDesc;
    // 列类型0:int,1:string,2:float,3:date,4:datetime, 时间格式为标准格式（yyyy-MM-dd HH:mm:ss)
    Integer paramType;

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

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public Integer getParamType() {
        return paramType;
    }

    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }
}
