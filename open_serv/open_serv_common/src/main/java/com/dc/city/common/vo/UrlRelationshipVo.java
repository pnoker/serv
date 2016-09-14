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
 * url与类，方法的对照关系Vo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年7月6日 上午9:50:39
 *          Copyright 2015 by DigitalChina
 */
public class UrlRelationshipVo {

    // class名
    private String beanName;

    // 方法名
    private String methodName;

    // 方法参数类型
    private String methodParamType;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodParamType() {
        return methodParamType;
    }

    public void setMethodParamType(String methodParamType) {
        this.methodParamType = methodParamType;
    }

}
