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
package com.dc.city.common.exception;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dc.city.common.log.domain.Citylogger;
import com.dc.city.common.log.mapper.CityloggerMapper;
import com.dc.city.common.utils.StringUtils;

/**
 * 记录错误日志与访问日志
 * 
 * @author xutaog
 * @version V1.0 创建时间：2015年5月18日 上午10:04:05
 *          Copyright 2015 by DigitalChina
 */
@Component
public class LoggerInterceptor implements ThrowsAdvice {
    private static final Log logger = LogFactory.getLog(LoggerInterceptor.class);

    @Resource(name = "masterSqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;
    private ThreadLocal<String> reqUrl = new ThreadLocal<String>();// 请求方的URL
    private ThreadLocal<String> userInfo = new ThreadLocal<String>();// 请求方的用户
    private ThreadLocal<String> remoteAdd = new ThreadLocal<String>();// 请求地址
    private ThreadLocal<String> host = new ThreadLocal<String>();// 请求方域名
    private ThreadLocal<String> reqParams = new ThreadLocal<String>();// 请求的参数
    private ThreadLocal<Object> responseResult = new ThreadLocal<Object>();// 返回的结果
    private ThreadLocal<String> exceptionResult = new ThreadLocal<String>();// 产生的错误结果集

    /**
     * 记录访问的日志拦截器
     * 访问控制器时进入此方法
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     * @author xutaog 2015年5月25日
     */
    public Object visitedLoggerInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        reqUrl.set((String) request.getRequestURI());// 请求方的URL
        userInfo.set((String) request.getRemoteUser());// 请求方的用户
        remoteAdd.set((String) request.getRemoteAddr());// 请求地址
        host.set((String) request.getRemoteHost());// 请求方域名
        Map<String, String[]> params = request.getParameterMap();
        Set<Entry<String, String[]>> parmSet = params.entrySet();
        Iterator<Entry<String, String[]>> it = parmSet.iterator();
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();
            logger.debug(entry.getKey() + ":\t" + entry.getValue());
            String[] paramVal = entry.getValue();
            String valStr = "";
            for (int j = 0; j < paramVal.length; j++) {
                if (j == 0) {
                    valStr += paramVal[j];
                } else {
                    valStr += "," + paramVal[j];
                }
            }
            // 判断是否为空，然后对参数进行转码
            if (!StringUtils.isNullOrEmpty(valStr)) {
                valStr = new String(valStr.trim().getBytes("ISO-8859-1"), "UTF-8");
            }
            if (i == 0) {
                buffer.append(entry.getKey() + ":" + valStr);
            } else {
                buffer.append(";" + entry.getKey() + ":" + valStr);
            }
            i++;
        }
        reqParams.set(buffer.toString());
        responseResult.set(joinPoint.proceed());// responseResult的值就是被拦截方法的返回值

        return responseResult.get();
    }

    /**
     * 出控制器时操作控制类
     *
     * @param jointPoint
     * @author xutaog 2015年5月25日
     */
    public void afterOpreation(JoinPoint jointPoint) {

        Citylogger citylogger = new Citylogger();
        String reqParamsStr = reqParams.get();
        //请求参数 大于1024 截断后再保存
        if(reqParamsStr.length()>1000){
            reqParamsStr= reqParamsStr.substring(0,1000);
        }
        citylogger.setParamters(reqParamsStr);
        citylogger.setVisitedtime(new Date());
        citylogger.setVisitedip(remoteAdd.get());
        citylogger.setVisitedurl(reqUrl.get());
        citylogger.setVisiteduser(userInfo.get());
        citylogger.setVisitedhost(host.get());
        String result = "";
        if (responseResult != null) {// 当不为空的时候,就不会出现报错
            try {
                if (responseResult instanceof List || responseResult instanceof Map) {// 如果是数组或对象
                    result = JSONArray.fromObject(responseResult).toString().trim();
                } else {
                    result = JSONObject.fromObject(responseResult).toString().trim();
                }
            } catch (JSONException jsonException) {// 如果是Json转换错误
                logger.debug("Json转换抛出的错误" + jsonException.getMessage());
                result = responseResult.toString();
            }

            exceptionResult.set("");// 如果不为空就就将错误信息置为空
            if (queryWordCount(result) > 4000) {// 如果字节长度超过4000那就保存到长文本Clob中
                citylogger.setResponseText(result);
            } else {
                citylogger.setResponseparams(result);
            }
        }
        citylogger.setErrinfo(exceptionResult.get());
        try (SqlSession session = sqlSessionFactory.openSession()) {// 保存日志记录
            CityloggerMapper cityloggerMapper = session.getMapper(CityloggerMapper.class);
            cityloggerMapper.insert(citylogger);
            session.commit();
        }
    }

    /**
     * 抛出错时记录发生的错误
     *
     * @param jointPoint
     * @param throwable
     * @author xutaog 2015年5月25日
     */
    public void afterThrowing(JoinPoint jointPoint, Throwable throwable) {
        StringBuffer errMsg = new StringBuffer();
        errMsg.append("报  错  方  法：" + jointPoint.getTarget().getClass() + "." + jointPoint.getSignature().getName()
                + "()  ");
        exceptionResult.set(errMsg.toString() + "==错误信息：" + throwable.getMessage());
        logger.error(errMsg.toString());
        logger.error("错  误  信  息  如  下:", throwable);
    }

    /**
     * 获取字节的长度
     *
     * @param s
     * @return
     * @author xutaog 2015年6月25日
     */
    private int queryWordCount(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255) {// 如果不是中文
                length++;
            } else {
                length += 2;

            }
        }
        return length;
    }
}
