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
package com.dc.city.vo.datasets;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.dc.city.domain.dataset.DatasetMenu;

/**
 * 菜单显示vo，将1、2、3级菜单分类显示
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月13日 下午4:17:50
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "DatasetMenuVo")
@XmlSeeAlso(DatasetMenu.class)
public class DatasetMenuVo {
    /**
     * 第0级菜单数据
     */
    private List<DatasetMenu> levelZero;

    /**
     * 第一级菜单数据
     */
    private List<DatasetMenu> levelOne;

    /**
     * 第二级菜单数据
     */
    private List<DatasetMenu> levelTwo;

    public List<DatasetMenu> getLevelZero() {
        return levelZero;
    }

    public void setLevelZero(List<DatasetMenu> levelZero) {
        this.levelZero = levelZero;
    }

    public List<DatasetMenu> getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(List<DatasetMenu> levelOne) {
        this.levelOne = levelOne;
    }

    public List<DatasetMenu> getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(List<DatasetMenu> levelTwo) {
        this.levelTwo = levelTwo;
    }

}
