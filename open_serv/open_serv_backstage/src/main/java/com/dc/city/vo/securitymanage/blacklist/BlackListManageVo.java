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
package com.dc.city.vo.securitymanage.blacklist;

import javax.ws.rs.FormParam;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.dc.city.vo.PageVo;

/**
 * BlackListManageVo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月7日 下午5:12:17
 *          Copyright 2016 by DigitalChina
 */
public class BlackListManageVo extends PageVo {

    /**
     * 出参：id
     */
    @FormParam("id")
    private long id;

    /**
     * 出参：IP地址
     */
    @FormParam("ipAddress")
    private String ipAddress;

    /**
     * 出参：备注
     */
    @FormParam("banReason")
    private String banReason;

    /**
     * 出参：创建时间
     */
    @FormParam("updatetime")
    private String updatetime;

    /**
     * 入参：查询开始时间
     */
    @JsonIgnore
    @FormParam("beginDate")
    private String beginDate;

    /**
     * 入参：查询结束时间
     */
    @JsonIgnore
    @FormParam("endDate")
    private String endDate;

    /**
     * id
     */
    @JsonIgnore
    @FormParam("blackListId")
    private String blackListId;

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

    public String getBlackListId() {
        return blackListId;
    }

    public void setBlackListId(String blackListId) {
        this.blackListId = blackListId;
    }

}
