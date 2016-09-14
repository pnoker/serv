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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dc.city.common.vo.LoginVo;

/**
 * 操作cookie的工具类
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年7月30日 下午3:42:20
 *          Copyright 2015 by DigitalChina
 */
/**
 * 类说明
 *
 * @author chenzpa
 * @version V1.0 创建时间：2015年8月20日 上午10:45:32
 *          Copyright 2015 by Dcits
 */
/**
 * 类说明
 *
 * @author chenzpa
 * @version V1.0 创建时间：2015年8月20日 上午10:45:38
 *          Copyright 2015 by Dcits
 */
public class CookieUtils {
    /**
     * tokenid的cookie名称
     */
    public static final String COOKIE_TOKEN_ID = "tokenId";

    /**
     * 验证码的cookie名称
     */
    public static final String COOKIE_VALIDATE_CODE = "validateCode";

    /**
     * 登录系统有效的ip地址有效
     */
    public static final String COOKIE_LOGIN_MODULE_EFFECTIVE_IP = "effectiveIp";
    /**
     * 不保存的编码
     */
    public static final String COOKIE_IS_NOT_SAVE_CODE = "0";

    /**
     * 获取SpringMVC中HttpServletRequest
     *
     * @return
     * @author xutaog 2015年8月7日
     */
    public static HttpServletRequest acquireRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return request;
    }

    /**
     * 获取request中的用户名
     *
     * @param request 前台的request
     * @return
     * @author xutaog 2015年8月6日
     */
    public static String acquireUserNameFromRequest() {
        String tokenId = acquireCookieValueFromRequest(acquireRequest(), COOKIE_TOKEN_ID);
        if (!StringUtils.isNullOrEmpty(tokenId)) {
            String[] args = tokenId.split(":");
            if (args.length < 3) {
                return null;
            }
            String userName = args[2];
            return userName;
        }
        return null;
    }

    /**
     * 创建cookie
     * 
     * @param cookieName 创建cookie的名称
     * @param cookieValue 值
     * @param checkCode 是否保存
     * @return
     * @throws UnsupportedEncodingException
     * @author xutaog 2015年7月30日
     */
    public static Cookie createCookie(String cookieName, String cookieValue, String checkCode)
            throws UnsupportedEncodingException {
        URLEncoder.encode(cookieValue, "utf-8");// 将值转换成中文
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        if (LoginVo.SAVE_LOGIN_STATU_STRING.equals(checkCode)) {// 如果设置了登录的状态
            int day = 60 * 60 * 24 * 20;
            cookie.setMaxAge(day);// 保存两周
        } else {// 浏览器关闭时将cookie清除
            cookie.setMaxAge(-1);
        }
        return cookie;
    }

    /**
     * 主页中 request中没有token 这创建这个tokenid并添加到response中
     *
     * @param request
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxAge
     * @return
     * @throws UnsupportedEncodingException
     * @author chenzpa 2015年8月20日
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
            String cookieValue, boolean isChecked) throws UnsupportedEncodingException {
        // URLEncoder.encode(cookieValue, "utf-8");// 将值转换成中文
        boolean isHaveToken = false;
        Cookie[] cookies = request.getCookies();// 取得所有的cookie
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equalsIgnoreCase(cookieName)) {// 判断是否与Cookie相等
                    isHaveToken = true;
                    break;
                }
            }
        }

        if (!isHaveToken) {
            int cookieMaxAge = 1800;
            try {
                cookieMaxAge = Integer.valueOf(cookieValue.split(":")[4]);
            } catch (Exception e) {
            }
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            Cookie cookie = new Cookie(cookieName, cookieValue);
            cookie.setPath("/");
            if (isChecked) {// 如果设置了登录的状态
                cookie.setMaxAge(cookieMaxAge);// 保存两周
            } else {// 浏览器关闭时将cookie清除
                cookie.setMaxAge(-1);
            }
            response.addCookie(cookie);
        }
    }

    public static String acquireTookenId() {
        return acquireCookieValueFromRequest(acquireRequest(), CookieUtils.COOKIE_TOKEN_ID);
    }

    /**
     * 从HttpServeltRequest获取cookieValue
     * 新版本后 没有cookie 就从参数里面取一下
     * 
     * @param request
     * @param cookieName cookie名称
     * @return
     * @author xutaog 2015年7月30日
     */
    public static String acquireCookieValueFromRequest(HttpServletRequest request, String cookieName) {

        String cookieValue = "";
        Cookie[] cookies = request.getCookies();// 取得所有的cookie
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equalsIgnoreCase(cookieName)) {// 判断是否与Cookie相等
                    cookieValue = cookie.getValue();
                    try {
                        cookieValue = URLDecoder.decode(cookieValue, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        // 新版本后 没有cookie 就从参数里面取一下
        if (StringUtils.isNullOrEmpty(cookieValue)) {
            cookieValue = request.getParameter(cookieName);
        }
        return cookieValue;
    }

    /**
     * 删除某个cookie
     *
     * @param request
     * @param response
     * @param cookieName
     * @author xutaog 2015年7月30日
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();// 取得所有的cookie
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equalsIgnoreCase(cookieName)) {// 判断是否与Cookie相等
                    cookie.setMaxAge(0);
                    if (response != null) {
                        response.addCookie(cookie);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 删除 cookie
     *
     * @param servletResponse
     * @author xutaog 2015年9月6日
     */
    public static void removeCookieToken(HttpServletResponse servletResponse) {
        Cookie ck = new Cookie(COOKIE_TOKEN_ID, null);
        ck.setMaxAge(0);
        ck.setPath("/");
        servletResponse.addCookie(ck);
    }
}
