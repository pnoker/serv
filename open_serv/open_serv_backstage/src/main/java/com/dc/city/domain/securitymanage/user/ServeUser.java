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
package com.dc.city.domain.securitymanage.user;

import java.util.Date;
/**
 * 
 * 访问用户实体类
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月28日 下午4:56:47
 *          Copyright 2016 by DigitalChina
 */
public class ServeUser {

    private long id;

    private String userName;

    //曾经的用户名，只有当用户被删除后，此字段才会有内容
    private String userNameEver;

    private String nickName;

    private String userPass;

    private String encryptSalt;

    private String appKey;

    private String userEmail;

    private String userMobile;

    //网络标识（1.内网 ,2:外网）
    private int userChannel;

    private String userRemark;

    //是否删除
    private int isDeleted;

    private Date createTime;

    private Date updatetime;

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

    public String getUserNameEver() {
        return userNameEver;
    }

    public void setUserNameEver(String userNameEver) {
        this.userNameEver = userNameEver;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getEncryptSalt() {
        return encryptSalt;
    }

    public void setEncryptSalt(String encryptSalt) {
        this.encryptSalt = encryptSalt;
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

    public int getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(int userChannel) {
        this.userChannel = userChannel;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

}