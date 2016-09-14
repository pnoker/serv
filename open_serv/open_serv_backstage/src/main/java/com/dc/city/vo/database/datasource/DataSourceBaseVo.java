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
package com.dc.city.vo.database.datasource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.dc.city.pojo.datasource.DataBaseColumnPo;
import com.dc.city.pojo.datasource.DataSourceManagePo;
import com.dc.city.vo.BaseVo;

/**
 * 数据源管理前端返回vo
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月9日 下午6:03:56
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "dataSourceBaseVo")
@XmlSeeAlso({ DataSourceManagePo.class, String.class, DataBaseColumnPo.class })
public class DataSourceBaseVo extends BaseVo {

    public static final String REMOVE_ERROR_MSG = "删除数据源失败";

    public static final String ALREADY_USE_ERROR_MSG = "数据源已经被使用,不能删除";

    public DataSourceBaseVo() {
        super();
    }

    public DataSourceBaseVo(String resultCode, String resultInfo) {
        super();
        super.setResultCode(resultCode);
        super.setResultInfo(resultInfo);
    }

    /**
     * 数据
     */
    private List<?> datas = new ArrayList<>();;

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
    }
}
