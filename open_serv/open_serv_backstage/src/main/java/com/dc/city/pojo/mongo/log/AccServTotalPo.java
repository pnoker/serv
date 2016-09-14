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
package com.dc.city.pojo.mongo.log;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 服务总量统计
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年3月8日 下午3:12:25
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "AccServTotalPo")
public class AccServTotalPo {

    private String statKey;

    private long totalNum;


    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public String getStatKey() {
        return statKey;
    }

    public void setStatKey(String statKey) {
        this.statKey = statKey;
    }


}
