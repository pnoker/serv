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
package com.dc.city.vo.catalog;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.dc.city.pojo.serve.catalog.CatalogAuthPo;
import com.dc.city.pojo.serve.catalog.CatalogPo;
import com.dc.city.vo.BaseVo;

/**
 * 服务目录返回类
 *
 * @author ligen
 * @version V1.0 创建时间：2016年3月9日 下午4:31:11
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "catalogBaseVo")
@XmlSeeAlso({CatalogPo.class,CatalogAuthPo.class})
public class CatalogBaseVo extends BaseVo {
    private List<?> datas;

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
    }
}
