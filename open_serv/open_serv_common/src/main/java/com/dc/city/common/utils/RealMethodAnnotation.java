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
package com.dc.city.common.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法注解
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年7月6日 上午10:20:14
 *          Copyright 2015 by DigitalChina
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RealMethodAnnotation {

    /**
     * 方法参数枚举
     *
     * @author zuoyue
     * @version V1.0 创建时间：2015年7月6日 上午9:57:39
     *          Copyright 2015 by DigitalChina
     */
    public enum MethodType {
        LIST_OBJECT, LIST_MAP, NULL
    }

    /**
     * 方法参数类型
     *
     * @return
     * @author zuoyue 2015年7月6日
     */
    MethodType methodType() default MethodType.LIST_OBJECT;

}
