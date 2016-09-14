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
 * 登录日志表
 * 专门记录数据汇总的登录日志信息
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年3月29日 上午11:19:52
 *          Copyright 2016 by DigitalChina
 */
public class ServLoginLog {

    private String userName;

    private String tokeId;

    private String ip;

    private String remark;

    private int isSucc;

    private Date loginTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTokeId() {
        return tokeId;
    }

    public void setTokeId(String tokeId) {
        this.tokeId = tokeId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsSucc() {
        return isSucc;
    }

    public void setIsSucc(int isSucc) {
        this.isSucc = isSucc;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

}
