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
package com.dc.city.pojo.securitymanage.user;

import com.dc.city.pojo.securitymanage.PagePo;

/**
 * UserManagePo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月9日 下午2:25:55
 *          Copyright 2016 by DigitalChina
 */
public class UserManagePo extends PagePo {

    /**
     * id,删除的时候用
     */
    private long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 网络标识（1.内网 ,2:外网）
     */
    private int userChannel;

    /**
     * 用户key
     */
    private String appKey;

    /**
     * 入参：昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String userPassWord;

    /**
     * 加密因子
     */
    private String encryptSalt;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 手机号
     */
    private String userMobile;

    /**
     * 备注
     */
    private String userRemark;

    /**
     * 是否已删除（1:是,0:否）
     */
    private int isDeleted;

    /**
     * 创建时间
     */
    private String createTime;

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

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
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

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEncryptSalt() {
        return encryptSalt;
    }

    public void setEncryptSalt(String encryptSalt) {
        this.encryptSalt = encryptSalt;
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
