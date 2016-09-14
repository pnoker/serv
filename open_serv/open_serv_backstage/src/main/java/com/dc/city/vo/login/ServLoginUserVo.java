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
package com.dc.city.vo.login;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 汇总平台展示个人信息类
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月8日 下午5:08:48
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "ServLoginVo")
public class ServLoginUserVo {

    private long id;

    private String userName;

    private String nickName;

    private String appKey;

    private String userEmail;

    private String userMobile;

    private String userRemark;

    private String registTime;

    /**
     * 上次是否登录成功
     */
    private int isSucc;

    /**
     * 登录失败备注
     */
    private String remark;

    /**
     * 上一次登录时间
     */
    private String subLoginTime;

    /**
     * 上次登录ip
     */
    private String ip;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public String getSubLoginTime() {
        return subLoginTime;
    }

    public void setSubLoginTime(String subLoginTime) {
        this.subLoginTime = subLoginTime;
    }

}
