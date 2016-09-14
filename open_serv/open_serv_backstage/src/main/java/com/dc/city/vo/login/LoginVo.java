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

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.dc.city.vo.BaseVo;

/**
 * 登录实体类
 *
 * @author ligen
 * @version V1.0 创建时间：2016年8月29日 上午10:45:57
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "loginVo")
public class LoginVo extends BaseVo {
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
