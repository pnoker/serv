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

/**
 * ServeUserAuthorityVo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月11日 上午10:51:22
 *          Copyright 2016 by DigitalChina
 */
public class ServeUserAuthorityVo {

    private long userId;

    private long serviceId;

    private int viewPermission;

    private int accessPermission;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public int getViewPermission() {
        return viewPermission;
    }

    public void setViewPermission(int viewPermission) {
        this.viewPermission = viewPermission;
    }

    public int getAccessPermission() {
        return accessPermission;
    }

    public void setAccessPermission(int accessPermission) {
        this.accessPermission = accessPermission;
    }

}