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
package com.dc.city.common.log.domain;

/**
 * 日志参数
 *
 * @author chenzpa
 * @version V1.0 创建时间：2015年7月16日 上午11:01:56
 *          Copyright 2015 by Dcits
 */
public class LogMessageObject {
    // message对象参数
    private Object[] objects;

    /**
     * 构造函数
     */
    private LogMessageObject(Object... objects) {
        this.objects = objects;
    }

    public static LogMessageObject instance(Object... objects) {
        return new LogMessageObject(objects);
    }

    /**
     * 返回 objects 的值
     * 
     * @return objects
     */
    public Object[] getObjects() {
        return objects;
    }

    /**
     * 设置 objects 的值
     * 
     * @param objects
     */
    public LogMessageObject setObjects(Object... objects) {
        this.objects = objects;
        return this;
    }
}
