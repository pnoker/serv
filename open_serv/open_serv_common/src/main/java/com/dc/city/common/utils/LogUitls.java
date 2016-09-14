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

import javax.servlet.http.HttpServletRequest;

import com.dc.city.common.log.domain.LogMessageObject;

/**
 * 日志工具
 * 将request放入ThreadLocal用于LOG_ARGUMENTS注入。
 *
 * @author chenzpa
 * @version V1.0 创建时间：2015年7月16日 上午11:13:33
 *          Copyright 2015 by Dcits
 */
public abstract class LogUitls {
    /**
     * 日志参数
     */
    public final static String LOG_ARGUMENTS = "log_arguments";
    
    // 用于存储每个线程的request请求
    private static final ThreadLocal<HttpServletRequest> LOCAL_REQUEST = new ThreadLocal<HttpServletRequest>();
    
    public static void putRequest(HttpServletRequest request) {
        LOCAL_REQUEST.set(request);
    }
    
    public static HttpServletRequest getRequest() {
        return LOCAL_REQUEST.get();
    }
    
    public static void removeRequest() {
        LOCAL_REQUEST.remove();
    }
    
    /**
     * 将LogMessageObject放入LOG_ARGUMENTS。
     * 描述
     * @param logMessageObject
     */
    public static void putArgs(LogMessageObject logMessageObject) {
        HttpServletRequest request = getRequest();
        request.setAttribute(LOG_ARGUMENTS, logMessageObject);
    }
    
    /**
     * 得到LogMessageObject。
     * 描述
     * @param logMessageObject
     */
    public static LogMessageObject getArgs() {
        HttpServletRequest request = getRequest();
        return (LogMessageObject)request.getAttribute(LOG_ARGUMENTS);
    }
}
