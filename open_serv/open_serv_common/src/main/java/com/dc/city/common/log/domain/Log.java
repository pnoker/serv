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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 日志类注解
 *
 * @author chenzpa
 * @version V1.0 创建时间：2015年7月16日 上午10:08:47
 *          Copyright 2015 by Dcits
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface Log {
    /**
     * 
     * 日志信息
     * @return
     */
    String message();
    
    /**
     * 
     * 日志操作类型
     * @return
     */
    LogOperType operType() ; 
}
