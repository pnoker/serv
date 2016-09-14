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
package com.dc.city.vo.config;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.dc.city.vo.BaseVo;

/**
 * 服务查询列表 统一返回vo
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月28日 上午9:47:11
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "serveConfigVo")
@XmlSeeAlso({ ServeConfigQueryVo.class })
public class ServeConfigVo extends BaseVo {
    
    public ServeConfigVo() {
        super();
    }
    
    public ServeConfigVo(String resultCode,String resultInfo) {
        super();
        super.setResultCode(resultCode);
        super.setResultInfo(resultInfo);
    }
    
    private List<?> datas;

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
    }
}
