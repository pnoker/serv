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
 * 属性注解
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年6月19日 上午10:31:46
 *          Copyright 2015 by DigitalChina
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RealFieldAnnotation {
    /**
     * 数据库字段名
     *
     * @return
     * @author zuoyue 2015年6月19日
     */
    String columnName() default "";

    /**
     * 类型枚举
     * 时间类型：若实体类定义的string，columnType则根据情况使用TIMESTAMPSTRING或DATESTRING
     * 若实体类定义的date，columnType则根据情况使用DATE或TIMESTAMP
     *
     * @author zuoyue
     * @version V1.0 创建时间：2015年6月22日 下午5:48:48
     *          Copyright 2015 by DigitalChina
     */
    public enum ColumnType {
        STRING, LONG, INT, FLOAT, DOUBLE, DATE, DATESTRING, TIMESTAMP, TIMESTAMPSTRING, BOOLEAN, BYTE, SHORT, CHAR, NULL
    };

    /**
     * 类型
     *
     * @return
     * @author zuoyue 2015年6月19日
     */
    ColumnType columnType() default ColumnType.NULL;

    /**
     * insert_sql是否需要该字段
     *
     * @return
     * @author zuoyue 2015年6月24日
     */
    boolean isSave() default true;

    /**
     * 是否是关联字段
     *
     * @return
     * @author zuoyue 2015年6月24日
     */
    boolean isCorrelation() default false;

}
