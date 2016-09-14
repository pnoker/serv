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
 * 数据汇总平台菜单
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月13日 下午3:20:44
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "DatasetMenu")
public class DatasetMenu {

    private int id;
    /*
     * 菜单接口名称
     */
    private String name;

    /*
     * 菜单接口的父节点ID
     */
    private int parentId;

    /*
     * 菜单接口的目录级别
     */
    private int level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
