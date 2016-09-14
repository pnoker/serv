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
package com.dc.city.vo.securitymanage.user;

import javax.ws.rs.FormParam;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.dc.city.vo.PageVo;

/**
 * UserManageVo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月9日 下午2:25:13
 *          Copyright 2016 by DigitalChina
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserManageVo extends PageVo {

    public static final String QUERY_USER_ERROR_MSG = "查询用户失败";

    public static final String QUERY_USER_NOT_FOUND_MSG = "未查询到用户";

    public static final String CREATE_USER_ERROR_MSG = "添加用户失败";

    public static final String REMOVE_USER_ERROR_MSG = "删除用户失败";

    public static final String MODIFY_USER_ERROR_MSG = "编辑用户失败";

    public static final String PASSWORD_NOT_SAME_ERROR_MSG = "两次输入的密码不一致";

    public static final String USER_IS_EXIST_MSG = "该用户名已存在";

    public static final String AUTH_ERROR_MSG = "保存失败";
    
    public static final String QUERY_USER_AUTH_ERROR_MSG = "查询用户服务权限失败";
    
    public static final String REBUILD_APPKEY_ERROR_MSG ="更新appKey失败";

    public UserManageVo() {}

    public UserManageVo(String appKey) {
        this.appKey = appKey;
    }

    /**
     * 入参：json字符串,用户授权使用
     */
    @FormParam("serveUserAuthorityVoListJson")
    private String serveUserAuthorityVoListJson;

    /**
     * 入参：id,删除的时候用
     */
    @FormParam("id")
    private long id;

    /**
     * 入参：用户名
     */
    @FormParam("userName")
    private String userName;

    /**
     * tokenId xutao add 20160329 用于用户登录
     */
    @FormParam("tokenId")
    private String tokenId;

    /**
     * 是否被选中 xutao add 20160329 用于用户登录
     */
    @FormParam("checkCode")
    private String checkCode;

    /**
     * 前端的验证码
     */
    @FormParam("validCode")
    private String validCode;
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
     * 入参：网络标识（1.内网 ,2:外网）
     */
    @FormParam("userChannel")
    private int userChannel;

    /**
     * 出参：网络标识中文（1.内网 ,2:外网）
     */
    private String userChannelName;

    /**
     * 入参：用户key
     */
    @FormParam("appKey")
    private String appKey;

    /**
     * 入参：旧的key(在用户编辑的时候使用，
     * 没有点重新生成key则appKey与originalKey一致，
     * 不一致的话就表示点了重新生成 )
     */
    @JsonIgnore
    @FormParam("originalKey")
    private String originalKey;

    /**
     * 入参：昵称
     */
    @FormParam("nickName")
    private String nickName;

    /**
     * 入参：密码
     */
    @JsonIgnore
    @FormParam("userPass")
    private String userPass;

    /**
     * 入参：2次确认密码
     */
    @JsonIgnore
    @FormParam("rePassword")
    private String rePassword;

    /**
     * 入参：邮箱
     */
    @FormParam("userEmail")
    private String userEmail;

    /**
     * 入参：手机号
     */
    @FormParam("userMobile")
    private String userMobile;

    /**
     * 入参：备注
     */
    @FormParam("userRemark")
    private String userRemark;

    /**
     * 创建时间
     */
    private String createTime;

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

     public String getBeginDate() {
        return beginDate;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
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

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserChannelName() {
        return userChannelName;
    }

    public void setUserChannelName(String userChannelName) {
        this.userChannelName = userChannelName;
    }

    public String getServeUserAuthorityVoListJson() {
        return serveUserAuthorityVoListJson;
    }

    public void setServeUserAuthorityVoListJson(String serveUserAuthorityVoListJson) {
        this.serveUserAuthorityVoListJson = serveUserAuthorityVoListJson;
    }

    public String getOriginalKey() {
        return originalKey;
    }

    public void setOriginalKey(String originalKey) {
        this.originalKey = originalKey;
    }
    
    private String viewPermissions;

    private String accessPermissions;

    public String getViewPermissions() {
        return viewPermissions;
    }

    public void setViewPermissions(String viewPermissions) {
        this.viewPermissions = viewPermissions;
    }

    public String getAccessPermissions() {
        return accessPermissions;
    }

    public void setAccessPermissions(String accessPermissions) {
        this.accessPermissions = accessPermissions;
    }

}
