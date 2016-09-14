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
package com.dc.city.common.filters;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;

/**
 * 处理中文乱码
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年8月31日 下午3:11:05
 *          Copyright 2015 by DigitalChina
 */
public class GetHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String charset = "GBK";
    private final static String DEFAULT_URI_ENCODE = "ISO-8859-1";

    public GetHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        // TODO Auto-generated constructor stub
    }

    /**
     * 获得被装饰对象的引用和采用的字符编码
     *
     * @param request
     * @param charset
     * @author xutaog 2015年8月31日
     */
    public GetHttpServletRequestWrapper(HttpServletRequest request, String charset) {
        super(request);
        this.charset = charset;
    }

    /**
     * 调用被包装的请求对象的getParameter方法获得参数，然后再进行编码转换
     */
    public String getParameter(String name) {
        String value = super.getParameter(name);
        value = StringUtils.isBlank(value) ? null : convert(value);
        return value;
    }

    /**
     * 获取参数的所有值
     */
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                values[i] = convert(values[i]);
            }
        }
        return values;
    }

    /**
     * 获取参数
     */
    public Object getAttribute(String name) {
        Object value = super.getAttribute(name);
        if (value instanceof String) {
            value = convert(String.valueOf(value));
        }
        return value;
    }

    /**
     * 转码
     *
     * @param target
     * @return
     * @author xutaog 2015年8月31日
     */
    public String convert(String target) {
        try {
            return new String(target.trim().getBytes(DEFAULT_URI_ENCODE), charset);
        } catch (UnsupportedEncodingException e) {
            return target;
        }
    }

}
