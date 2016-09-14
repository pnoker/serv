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

import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.config.ServeExtendSpace;
import com.dc.city.vo.BaseVo;

/**
 * 服务配置vo
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月11日 上午9:29:26
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "configerVo")
public class ConfigerVo extends BaseVo {

    public ConfigerVo() {

    }

    public ConfigerVo(String code, String info) {
        this.setResultCode(code);
        this.setResultInfo(info);
    }

    // 服务主表
    ServeConfig config;
    // 空间GIS服务 独有的信息
    ServeExtendSpace serveExtendSpace;

    // 服务ID
    Long configId;

    List<?> datas;

    public ServeConfig getConfig() {
        return config;
    }

    public void setConfig(ServeConfig config) {
        this.config = config;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public ServeExtendSpace getServeExtendSpace() {
        return serveExtendSpace;
    }

    public void setServeExtendSpace(ServeExtendSpace serveExtendSpace) {
        this.serveExtendSpace = serveExtendSpace;
    }

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
    }
}
