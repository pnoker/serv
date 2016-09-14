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
 * 服务的例子
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月19日 上午10:27:17
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "ServExample")
public class ServExample {

    /**
     * 服务Id
     */
    private long servId;

    /**
     * 返回类型
     */
    private String resultType;

    /**
     * 返回结果
     */
    private String resultExample;

    public long getServId() {
        return servId;
    }

    public void setServId(long servId) {
        this.servId = servId;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultExample() {
        return resultExample;
    }

    public void setResultExample(String resultExample) {
        this.resultExample = resultExample;
    }

}
