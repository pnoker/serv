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
 * 接口返回的字段的实体类
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月18日 上午11:33:34
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "ServResponseField")
public class ServResponseField {

    private int id;
    /*
     * 接口返回字段名称
     */
    private String fieldName;
    /*
     * 接口返回字段的类型
     */
    private String fieldType;
    /*
     * 接口返回字段的说明
     */
    private String fieldDescription;
    /*
     * 接口返回字段的创建时间
     */
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
