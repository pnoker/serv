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

import java.io.Serializable;

/**
 * UsersCachePo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月11日 下午4:12:00
 *          Copyright 2016 by DigitalChina
 */
public class UsersCachePo implements Serializable {

    private static final long serialVersionUID = -7028100978760304126L;

    private long id;

    private String userName;

    private String appKey;

    public String getNoViewPermissions() {
        return noViewPermissions;
    }

    public void setNoViewPermissions(String noViewPermissions) {
        this.noViewPermissions = noViewPermissions;
    }

    public String getNoAccessPermissions() {
        return noAccessPermissions;
    }

    public void setNoAccessPermissions(String noAccessPermissions) {
        this.noAccessPermissions = noAccessPermissions;
    }

    private int userChannel;

    private String viewPermissions;

    private String accessPermissions;
    
    private String noViewPermissions;
    
    private String noAccessPermissions;

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

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

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

    public int getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(int userChannel) {
        this.userChannel = userChannel;
    }

}
