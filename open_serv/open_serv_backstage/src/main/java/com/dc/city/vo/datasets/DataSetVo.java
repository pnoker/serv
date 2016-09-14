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

import com.dc.city.domain.dataset.DatasetServ;
import com.dc.city.domain.dataset.ServErrorCode;
import com.dc.city.domain.dataset.ServExample;
import com.dc.city.domain.dataset.ServModifyLog;
import com.dc.city.domain.dataset.ServRequestParams;
import com.dc.city.domain.dataset.ServResponseField;
import com.dc.city.vo.BaseVo;

/**
 * 数据汇总平台集合公共dao类
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年3月28日 下午1:31:32
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "DataSetVo")
@XmlSeeAlso({ DatasetMenuVo.class, ServExample.class, ServModifyLog.class, ServRequestParams.class,
        ServResponseField.class, DatasetServ.class, ServErrorCode.class })
public class DataSetVo extends BaseVo {

    private List<?> datas;

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
    }

}
