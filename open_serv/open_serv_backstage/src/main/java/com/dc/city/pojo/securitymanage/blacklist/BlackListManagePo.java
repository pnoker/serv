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
package com.dc.city.pojo.securitymanage.blacklist;

import com.dc.city.pojo.securitymanage.PagePo;

/**
 * BlackListManagePo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月7日 下午5:41:09
 *          Copyright 2016 by DigitalChina
 */
public class BlackListManagePo extends PagePo{

    /**
     * 出参：id
     */
    private long id;

    /**
     * 出参：IP地址
     */
    private String ipAddress;

    /**
     * 出参：备注
     */
    private String banReason;

    /**
     * 出参：创建时间
     */
    private String updatetime;

    /**
     * 入参：查询开始时间
     */
    private String beginDate;

    /**
     * 入参：查询结束时间
     */
    private String endDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
