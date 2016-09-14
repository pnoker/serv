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
package com.dc.city.common.log;

import org.apache.log4j.Priority;
import org.apache.log4j.RollingFileAppender;

/**
 * 自定义Appender
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年8月27日 上午11:29:12
 *          Copyright 2015 by DigitalChina
 */
public class CustomizationAppender extends RollingFileAppender {

    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {
        // 只判断是否相等，而不判断优先级
        return this.getThreshold().equals(priority);
    }
}