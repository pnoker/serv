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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 解决get请求的乱码，只需在web。xml中配置如下代码就可以解决乱码
 * <!--resolve url Chinese filter -->
 * <filter>
 * <filter-name>resloveMessyCodeFilter</filter-name>
 * <filter-class>com.dc.city.common.filters.ResloveMessyCodeFilter</filter-class>
 * <init-param>
 * <param-name>DEFAULT_URI_ENCODE</param-name>
 * <param-value>UTF-8</param-value>
 * </init-param>
 * </filter>
 * <filter-mapping>
 * <filter-name>resloveMessyCodeFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年8月31日 下午3:19:00
 *          Copyright 2015 by DigitalChina
 */
public class ResloveMessyCodeFilter implements Filter {

    private final static String DEFAULT_URI_ENCODE = "GBK";
    private final static String METHOD = "GET";
    private String encode = null;

    @Override
    public void init(FilterConfig config) throws ServletException {
        encode = config.getInitParameter("DEFAULT_URI_ENCODE");
        if (StringUtils.isBlank(this.encode)) {
            encode = DEFAULT_URI_ENCODE;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        // 设置请求响应字符编码
        request.setCharacterEncoding(encode);
        // 解决返回值的乱码
        response.setCharacterEncoding(encode);
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getMethod().equalsIgnoreCase(METHOD)) {
            req = new GetHttpServletRequestWrapper(req, encode);
        }
        // 传递包装器对象的引用
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
}
