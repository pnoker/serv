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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.dc.city.pojo.serve.config.ServeAuthUserPo;
import com.dc.city.vo.BaseVo;

/**
 * 服务列表查询参数封住类
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月24日 下午5:30:27
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "serveAuthUserVo")
@XmlSeeAlso(ServeAuthUserPo.class)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServeAuthUserVo extends BaseVo {

    private List<ServeAuthUserPo> views;
    private List<ServeAuthUserPo> access;
    public List<ServeAuthUserPo> getViews() {
        return views;
    }
    public void setViews(List<ServeAuthUserPo> views) {
        this.views = views;
    }
    public List<ServeAuthUserPo> getAccess() {
        return access;
    }
    public void setAccess(List<ServeAuthUserPo> access) {
        this.access = access;
    }

}
