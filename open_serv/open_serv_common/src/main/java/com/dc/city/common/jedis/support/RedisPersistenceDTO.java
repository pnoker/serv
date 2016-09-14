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
package com.dc.city.common.jedis.support;

import java.io.Serializable;

/**
 * redis持久化实体类
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年9月21日 上午9:31:55
 *          Copyright 2015 by DigitalChina
 */
public class RedisPersistenceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 实体或者list对象
     */
    private Object entity;
    /**
     * 调用类
     */
    private String beanName;
    /**
     * 执行的方法
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] classType;

    public RedisPersistenceDTO(Object entity, String beanName, String methodName, Class<?>[] clazzType) {
        this.entity = entity;
        this.beanName = beanName;
        this.methodName = methodName;
        this.classType = clazzType;
    }

    public Object getEntity() {
        return entity;
    }

    public Class<?>[] getClassType() {
        return classType;
    }

    public void setClassType(Class<?>[] classType) {
        this.classType = classType;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

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
}
