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

public class ServeModifyLog {
    private long id;

    private Short serviceId;

    private Short operatorId;

    private String operatorName;

    private String modifyRemark;

    private Date updatetime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Short getServiceId() {
        return serviceId;
    }

    public void setServiceId(Short serviceId) {
        this.serviceId = serviceId;
    }

    public Short getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Short operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getModifyRemark() {
        return modifyRemark;
    }

    public void setModifyRemark(String modifyRemark) {
        this.modifyRemark = modifyRemark;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}