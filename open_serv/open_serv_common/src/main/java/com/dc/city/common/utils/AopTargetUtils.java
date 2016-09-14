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

import java.lang.reflect.Field;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

/**
 * 获取目标对象
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年7月6日 下午5:37:35
 *          Copyright 2015 by DigitalChina
 */
public class AopTargetUtils {

    private static Log log = LogFactory.getLog("AopTargetUtils");

    /**
     * 获取 目标对象
     *
     * @param proxy
     * @return
     * @throws Exception
     * @author zuoyue 2015年7月6日
     */
    public static Object getTarget(Object proxy) {
        Object object = proxy;
        try {
            // 判断是否是aop的代理对象
            if (AopUtils.isAopProxy(proxy)) {
                log.debug("代理对象");
                // 是aop的代理对象
                if (AopUtils.isJdkDynamicProxy(proxy)) {
                    object = getJdkDynamicProxyTargetObject(proxy);
                } else {
                    // cglib
                    object = getCglibProxyTargetObject(proxy);
                }
            }else{
                log.debug("真实对象");
            }
        } catch (Exception e) {
            log.error("无法获取bean的class对象");
            object = null;
        }
        return object;
    }

    /**
     * getCglibProxyTargetObject
     *
     * @param proxy
     * @return
     * @throws Exception
     * @author zuoyue 2015年7月6日
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }

    /**
     * getJdkDynamicProxyTargetObject
     *
     * @param proxy
     * @return
     * @throws Exception
     * @author zuoyue 2015年7月6日
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }

}
