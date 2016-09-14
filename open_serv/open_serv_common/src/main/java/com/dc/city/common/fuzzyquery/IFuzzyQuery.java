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
package com.dc.city.common.fuzzyquery;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单表的模糊查询需要用到的属性
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年7月29日 上午11:15:52
 *          Copyright 2016 by Dcits
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IFuzzyQuery {
    /**
     * 模糊查询的表明
     *
     * @return
     * @author chenzpa 2016年7月29日
     */
    public String tableName();

    /**
     * 模糊查询的属性字段列表
     *
     * @return
     * @author chenzpa 2016年7月29日
     */
    public String[] propertys();
}
