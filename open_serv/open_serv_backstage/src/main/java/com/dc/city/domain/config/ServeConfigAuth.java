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
package com.dc.city.domain.config;

import java.io.Serializable;

/**
 * 权限需要用到的字段
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月16日 上午10:51:44
 *          Copyright 2016 by DigitalChina
 */
public class ServeConfigAuth implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5672119303446230788L;
    String serviceCode;
    int isService;
    int verifyAccess;
    int verifyView;
    String allPath;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public int getIsService() {
        return isService;
    }

    public void setIsService(int isService) {
        this.isService = isService;
    }

    public int getVerifyAccess() {
        return verifyAccess;
    }

    public void setVerifyAccess(int verifyAccess) {
        this.verifyAccess = verifyAccess;
    }

    public int getVerifyView() {
        return verifyView;
    }

    public void setVerifyView(int verifyView) {
        this.verifyView = verifyView;
    }

    public String getAllPath() {
        return allPath;
    }

    public void setAllPath(String allPath) {
        this.allPath = allPath;
    }

}
