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
package com.dc.city.domain.dataset;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 服务更新记录
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月19日 下午1:17:39
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "ServModifyLog")
public class ServModifyLog {

    /**
     * 服务ID
     */
    private long servId;

    /**
     * 更新内容
     */
    private String updateContent;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 操作ID
     */
    private String operatorId;

    /**
     * 操作名称
     */
    private String operatorName;

    public long getServId() {
        return servId;
    }

    public void setServId(long servId) {
        this.servId = servId;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

}
