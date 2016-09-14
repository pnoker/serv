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
 * 返回示例
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月16日 上午11:20:36
 *          Copyright 2016 by DigitalChina
 */
public class ServeResultExample implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 243211056507385283L;
    Long id;
    Long serviceId;
    String resultExample;
    String resultType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getResultExample() {
        return resultExample;
    }

    public void setResultExample(String resultExample) {
        this.resultExample = resultExample;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

}
