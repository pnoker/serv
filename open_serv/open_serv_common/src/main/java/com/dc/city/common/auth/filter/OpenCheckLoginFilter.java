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
package com.dc.city.common.auth.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dc.city.common.auth.tools.AuthTools;

/**
 * 开源中使用到的登录过滤器
 *
 * @author ligen
 * @version V1.0 创建时间：2016年8月25日 上午10:58:41
 *          Copyright 2016 by DigitalChina
 */
public class OpenCheckLoginFilter implements Filter {
    //排除登录页面
    private static String LOGIN = "/open_serv_configuration/NewLogin.html";
    //截取path
    private static String LOPATH ="/login/v1";
    public void init(FilterConfig filterConfig) throws ServletException {  
        
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        HttpServletRequest req = (HttpServletRequest) request;        
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        String url = req.getRequestURI();
        url = URLDecoder.decode(url, "UTF-8");
        if (url.equals(LOGIN)|| AuthTools.subModelPath(url).equalsIgnoreCase(LOPATH)) {
            chain.doFilter(req, res);            
        }else{
        //从session里取的用户名信息          
        String userName = (String) session.getAttribute("userName");
        //判断如果没有取到用户信息,就跳转到登陆页面          
        if ((userName == null) || "".equals(userName)) {            
            //跳转到登陆页面              
            res.getWriter().write(errorMessage("302", "对不起，没有登录，请先登录！"));

            return;
            } else {            
                //已经登陆,继续此次请求              
                chain.doFilter(req, res);        
                } 
        }
        }
        

    public void destroy() {  
        
    }
    
    /**
     * 执行出现错误时，返回错误信息
     *
     * @param code 错误代码，203:用户无权限访问,302:用户未登陆,404:调用后台路径错误(路径书写错误，或权限系统中未配置此功能模块)
     * @param error 输出错误信息
     * @return 把错误代码和错误信息,以JSON格式字符串的形式返回
     * @author liuppa 2016年4月22日
     */
    private String errorMessage(String code, String error) {
        String mesg = "{\"resultCode\": \"" + code + "\",\"resultInfo\": \"" + error
                + "\",\"datas\":[],\"iTotalDisplayRecords\":\"0\",\"totalCount\":\"0\",\"iTotalRecords\":\"0\"}";
        return mesg;
    }
    
}
