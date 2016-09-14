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
package com.dc.city.domain.log;

import java.util.Date;

/**
 * 访问日志记录表
 * modified by liuppa 2016-03-10
 * 修改内容：重命名返回给前端数据类的名称及字段名称，增删字段
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年3月7日 下午3:45:42
 *          Copyright 2016 by DigitalChina
 */
public class ServeVisitLog {

    private int number;
    /**
     * 日志Id
     */

    private int id;

    /**
     * 用户id
     */

    private long userId;

    /**
     * 用户名
     */

    private String userName;

    /**
     * 用户内外网标识(1:内网，2:外网)
     */

    private int userChannel;

    /**
     * 服务ID
     */

    private long serviceId;

    /**
     * 服务类型,1:配置接口，2:外部接口，3:空间数据库
     */

    private int serviceType;

    /**
     * 服务名称
     */

    private String serviceName;

    /**
     * 访问参数
     */

    private String logParams;

    /**
     * 返回代码
     */

    private String retCode;

    /**
     * 日志信息
     */

    private String retMsg;

    /**
     * 访问时长
     */

    private long costTime;

    /**
     * 访问服务的ip
     */

    private String visitIpAddress;

    /**
     * 访问开始时间
     */

    private Date startTime;

    /**
     * 访问结束时间
     */

    private Date endTime;

    /**
     * 记录更新时间
     */

    private Date updateTime;

    /**
     * 访问日期
     */
    private Date visitDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(int userChannel) {
        this.userChannel = userChannel;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLogParams() {
        return logParams;
    }

    public void setLogParams(String logParams) {
        this.logParams = logParams;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getVisitIpAddress() {
        return visitIpAddress;
    }

    public void setVisitIpAddress(String visitIpAddress) {
        this.visitIpAddress = visitIpAddress;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
