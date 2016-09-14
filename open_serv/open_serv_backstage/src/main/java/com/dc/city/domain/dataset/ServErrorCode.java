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
 * 接口对应的错误码实体类
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月18日 上午11:33:02
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "ServErrorCode")
public class ServErrorCode {

    private long id;
    /*
     * 错误码编号
     */
    private String errorCode;
    /*
     * 错误码描述
     */
    private String description;
    /*
     * 错误码创建时间
     */
    private String updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
