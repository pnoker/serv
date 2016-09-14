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
public class ServeInputParam implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7953942385706906595L;
    long id;
    long serviceId;
    // 参数名
    String paramCode;
    // 参数描述
    String paramDesc;
    // 列类型0:int,1:string,2:float,3:date,4:datetime, 时间格式为标准格式（yyyy-MM-dd HH:mm:ss)
    Integer paramType;
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
}
