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
package com.dc.city.vo.mongo.log;

import com.dc.city.vo.PageVo;

/**
 * 日志的查询条件
 * modified by liuppa,2016-03-10
 * 修改内容：增删字段
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年3月8日 上午10:46:30
 *          Copyright 2016 by DigitalChina
 */
public class AccessLogQuery extends PageVo {
    private int id;
    /**
     * 服务名称
     */
    private String servName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 访问服务的ip
     */
    private String ip;

    /**
     * 用户内外网标识
     */
    private String userChannel;

    /**
     * 服务类型
     */
    private String servType;

    /**
     * 起始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(String userChannel) {
        this.userChannel = userChannel;
    }

    public String getServType() {
        return servType;
    }

    public void setServType(String servType) {
        this.servType = servType;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
