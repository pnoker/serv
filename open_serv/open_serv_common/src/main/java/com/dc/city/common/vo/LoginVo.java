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
package com.dc.city.common.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 用于前端访问的类
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年7月23日 下午1:55:19
 *          Copyright 2015 by DigitalChina
 */
@XmlRootElement(name = "loginVo")
public class LoginVo extends BusinessVo {

    public static final String LOGIN_FAILURE_INFO = "用户名或密码错误！";

    public static final String LOGIN_SUSSESS_INFO = "登录成功！";

    public static final String AUTH_NULL_CODE = "-1";

    public static final String AUTH_NULL_INFO = "用户未登录！";

    public static final String AUTH_IP_FAILURE_CODE = "-2";

    public static final String AUTH_IP_FAILURE_INFO = "用户的IP地址受限！";

    public static final String AUTH_ROLE_FAILURE_CODE = "-3";

    public static final String AUTH_ROLE_FAILURE_INFO = "用户无权限访问！";

    public static final String AUTH_ROLE_NOAUTH_SYSTEM = "此用户无权限登录该系统！";

    public static final String AUTH_SUSSESS_INFO = "验证成功！";

    public static final String SAVE_LOGIN_STATU_STRING = "true";// 保存登录的状态

    // public static final String AUTH_LOGIN_MODULE_URL = "/city_authority/login/v1";//
    // 该路径和authority的系统中类路径一致

    public static final String AUTH_LOGIN_MODULE_PATH = "/login/v1";// 登录模块路径

    /**
     * 检查https证书路径
     */
    public static final String AUTH_LOGIN_VALIDATE_SSL = "/login/v1/ssl.json";
    /**
     * 验证码路径
     */
    public static final String AUTH_LOGIN_VALIDATE_URL = "/login/v1/queryvalidatecode.json";// 该路径和authority的系统中类路径一致
    /**
     * 登录MODELANDVIEW
     */
    public static final String AUTH_LOGIN_MODELANDVIEW = "/login/v1/loginView.json";

    /**
     * 超级管理员
     */
    public static final String AUTH_SUPER_ADMIN = "cityadmin";

    /**
     * 是否叶子节点
     */
    public static final int IS_LEAF_MODULE = 1;

    /**
     * 有权限为1
     */
    public static final int IS_HAVE_AUTH = 1;
    /**
     * 无权限为0
     */
    public static final int IS_NOT_HAVE_AUTH = 0;
    /**
     * 增加权限名称
     */
    public static final String ADD_AUTHORITY_NAME = "add";

    /**
     * 更新
     */
    public static final String UPDATE_AUTHORITY_NAME = "upd";

    /**
     * 删除
     */
    public static final String DELETE_AUTHORITY_NAME = "del";

    /**
     * 查询
     */
    public static final String FIND_AUTHORITY_NAME = "find";

    /**
     * 授权
     */
    public static final String AUTH_AUTHORITY_NAME = "auth";

    /**
     * tokenId
     */
    private String tokenId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 前台展示的名称
     */
    private String displayName;

    /**
     * 是否记住选择了记住我，选择了为true，没选择为false，默认为false
     */
    private boolean checkCode = false;
    /**
     * 数据集
     */
    private List<?> datas;

    public boolean isCheckCode() {
        return checkCode;
    }

    public void setCheckCode(boolean checkCode) {
        this.checkCode = checkCode;
    }

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
