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

/**
 * 装载授权操作类型类
 * 当有某个权限存在时将值置为1,默认为0
 * 
 * @author xutaog
 * @version V1.0 创建时间：2015年7月24日 下午1:20:29
 *          Copyright 2015 by DigitalChina
 */
public class AuthOpreateTypesVo {

    /**
     * 资源url
     */
    private String url;

    /**
     * 增加权限
     */
    private int add = 0;

    /**
     * 删除权限
     */
    private int del = 0;

    /**
     * 更新权限
     */
    private int upd = 0;

    /**
     * 查询权限
     */
    private int find = 0;

    /**
     * 授权权限
     */
    private int auth = 0;

    /**
     * 是否是cxf框架
     */
    private boolean isCxf;
    
    
    public boolean isCxf() {
        return isCxf;
    }

    public void setCxf(boolean isCxf) {
        this.isCxf = isCxf;
    }

    public int getAdd() {
        return add;
    }

    public void setAdd(int add) {
        this.add = add;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public int getUpd() {
        return upd;
    }

    public void setUpd(int upd) {
        this.upd = upd;
    }

    public int getFind() {
        return find;
    }

    public void setFind(int find) {
        this.find = find;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
