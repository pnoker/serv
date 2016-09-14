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
package com.dc.city.vo.database;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.dc.city.common.vo.BusinessVo;

/**
 * 服务处理baseVo
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月11日 下午3:44:43
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "serviceBaseVo")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServiceBaseVo extends BusinessVo {
    public ServiceBaseVo(){
        super();
    }
    public ServiceBaseVo(String resultCode,String resultInfo){
        super();
        super.setResultCode(resultCode);
        super.setResultInfo(resultInfo);
    }
}
