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
package com.dc.city.common.auth.tools;

import com.dc.city.common.utils.StringUtils;
import com.dc.city.common.vo.LoginVo;

/**
 * 跟验证登录相关的工具类
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年7月30日 下午1:52:20
 *          Copyright 2015 by DigitalChina
 */
public class AuthTools {

    /**
     * 去掉系统的名称
     * 如路径是/city_output/queryuer.json
     * 转换后return /queryuer
     * 
     * @param url
     * @return
     * @author xutaog 2015年7月30日
     */
    public static String subEffectiveUrl(String url) {
        StringBuilder builder = new StringBuilder();
        String[] strArr = url.split("/");
        for (int i = 2; i < strArr.length; i++) {// 只需取出第二个
            builder.append("/").append(strArr[i]);
        }
        return builder.toString().replace(".json", "");
    }

    /**
     * 截取某个模块的url
     *
     * @param url
     * @return
     * @author xutaog 2015年7月31日
     */
    public static String subModuleUrl(String url) {
        StringBuilder builder = new StringBuilder();
        String[] strArr = url.split("/");
        if (strArr.length > 4) {
            for (int i = 1; i < 4; i++) {// 取出三个
                builder.append("/").append(strArr[i]);
            }
            return builder.toString();
        } else {
            return url;
        }

    }

    /**
     * 获取所有的url数组组合
     * 获取所有组装的模块url地址
     * 
     * @param url
     * @return
     * @author xutaog 2015年9月8日
     */
    public static String[] gainAllUrlArr(String url) {
        String[] strArr = url.split("/");
        String[] relStr = new String[strArr.length - 2];
        for (int i = 1; i < strArr.length - 1; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 1; j <= i; j++) {
                relStr[i - 1] = builder.append("/").append(strArr[j]).toString();
            }
        }
        return relStr;
    }

    /**
     * 去掉最后的详细路径，只得到模块路径
     *
     * @param path
     * @return
     * @author xutaog 2015年9月11日
     */
    public static String subModelPath(String path) {
        String[] strArr = path.split("/");
        StringBuilder builder = new StringBuilder();
        if (strArr.length > 2) {
            for (int i = 2; i < strArr.length - 1; i++) {
                builder.append("/").append(strArr[i]);
            }
        }
        return builder.toString();
    }

    /**
     * 某个系统的url
     *
     * @param url
     * @return
     * @author xutaog 2015年7月31日
     */
    public static String subSytemUrl(String url) {
        String[] strArr = url.split("/");
        return "/" + strArr[1];
    }

    /**
     * 拼接模块的全路径
     *
     * @param systemCode 系统code
     * @param url
     * @return
     * @author xutaog 2015年8月3日
     */
    public static String jointSystemUrl(String systemCode, String url) {
        String sysCode = systemCode.startsWith("/") ? systemCode : "/" + systemCode;
        if (StringUtils.isNullOrEmpty(url)) {
            return sysCode;
        }
        String urlStr = url.startsWith("/") ? url : "/" + url;
        String dbUrl = sysCode + urlStr;
        return dbUrl;
    }

    /**
     * 是否是需要进行验证的url
     * 
     * @param url
     * @return
     * @author xutaog 2015年7月30日
     */
    public static boolean isCheckUrl(String url) {
        url = url.toLowerCase();
        boolean bo = true;
        if (url.endsWith(".js") || url.endsWith(".css")) {// 如果是js或css
            bo = false;
        } else if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".xml") || url.endsWith(".gif")
                || url.endsWith(".bmp") || url.endsWith(".tif") || url.endsWith(".swf") || url.endsWith(".woff2")
                || url.endsWith(".woff") || url.endsWith(".ttf") || url.endsWith(".svg") || url.endsWith(".eot")
                || url.endsWith(".otf")) {// 是否是图片
            bo = false;
        } else if (url.endsWith(".xlsx") || url.endsWith(".xls")) {// 是否是excel
            bo = false;
        } else if (url.endsWith(".pdf") || url.endsWith(".doc") || url.endsWith(".docx")) {// 是否是word
            bo = false;
        } else if (LoginVo.AUTH_LOGIN_VALIDATE_URL.equalsIgnoreCase(subModelPath(url))) {// 如果登录验证码
            bo = false;
        } else if (LoginVo.AUTH_LOGIN_MODELANDVIEW.equalsIgnoreCase(subModelPath(url))) {// 如果登录ModelAndView
            bo = false;
        }
        // 证书验证
        else if (url.indexOf(LoginVo.AUTH_LOGIN_VALIDATE_SSL) > 0) {
            bo = false;
        }
        return bo;
    }

    /**
     * 不经过过滤的url地址
     *
     * @param url
     * @param excludedPageArray
     * @return
     * @author xutaog 2015年8月6日
     */
    public static boolean isNotFilter(String url, String[] excludedPageArray) {
        boolean ret = true;
        if (excludedPageArray != null) {
            for (int i = 0; i < excludedPageArray.length; i++) {
                if (url.equalsIgnoreCase(excludedPageArray[i])) {
                    ret = false;
                    break;
                }
            }
        }
        return ret;
    }
}
