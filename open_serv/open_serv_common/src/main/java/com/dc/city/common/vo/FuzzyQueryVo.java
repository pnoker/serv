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
package com.dc.city.common.vo;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 模糊查询
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年7月29日 下午3:12:18
 *          Copyright 2016 by Dcits
 */
@XmlRootElement(name = "fuzzyQueryVo")
public class FuzzyQueryVo extends BaseVo {

    /**
     * 数据
     */
    private List<Map<String, String>> datas;

    public List<Map<String, String>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }
}
