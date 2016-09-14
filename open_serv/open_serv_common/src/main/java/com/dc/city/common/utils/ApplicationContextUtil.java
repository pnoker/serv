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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Path;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.dc.city.common.vo.UrlRelationshipVo;

/**
 * 获取ApplicationContext
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年6月18日 下午9:40:07
 *          Copyright 2015 by DigitalChina
 */
public class ApplicationContextUtil implements ApplicationContextAware {

    private static Log log = LogFactory.getLog("ApplicationContextUtil");

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    // 页面提交的url与类，方法的对照关系map
    private static Map<String, UrlRelationshipVo> relationshipMap;

    public void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @param beanId
     * @return
     * @throws BeansException
     * @author zuoyue 2015年6月22日
     */
    public static Object getBean(String beanId) throws BeansException {
        return applicationContext.getBean(beanId);
    }

    /**
     * 页面提交的url与类，方法的对照关系
     *
     * @param formUrl
     * @return
     * @author zuoyue 2015年7月2日
     */
    public static UrlRelationshipVo acquireClassAndMethodMap(String formUrl) {
        UrlRelationshipVo urlRelationshipVo = null;
        if (relationshipMap == null || relationshipMap.size() == 0) {
            log.debug("初始化classAndMethodMap");
            relationshipMap = new HashMap<String, UrlRelationshipVo>();
            // 获取spring装配的bean
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            for (String beanName : beanNames) {
                // 输入页面各模块的controller
                if (beanName.contains("InputController")) {
                    // 得到class对象
                    Class<?> beanClass = AopTargetUtils.getTarget(applicationContext.getBean(beanName)).getClass();
                    if(beanClass != null){
                        // 是否包含注解
                        boolean isClassPathExist = beanClass.isAnnotationPresent(Path.class);
                        // 如果包含指定注解
                        if (isClassPathExist) {
                            // 得到类的注解
                            String classPath = beanClass.getAnnotation(Path.class).value();
                            // 得到方法名
                            Method[] methods = beanClass.getMethods();
                            String postUrl = "";
                            for (Method method : methods) {
                                method.setAccessible(true);
                                boolean isMethodPathExist = method.isAnnotationPresent(Path.class);
                                if (isMethodPathExist) {
                                    postUrl = classPath + method.getAnnotation(Path.class).value();
                                    // 方法名
                                    String methodName = method.getName();
                                    // 方法参数类型
                                    String methodParamType = ReadExcel.PARAM_TYPE_DEFAULT;
                                    boolean isMethodTypeExist = method.isAnnotationPresent(RealMethodAnnotation.class);
                                    if (isMethodTypeExist) {
                                        methodParamType = method.getAnnotation(RealMethodAnnotation.class).methodType()
                                                .toString();
                                    }
                                    UrlRelationshipVo urlRelationship = new UrlRelationshipVo();
                                    urlRelationship.setBeanName(beanName);
                                    urlRelationship.setMethodName(methodName);
                                    urlRelationship.setMethodParamType(methodParamType);
                                    relationshipMap.put(postUrl, urlRelationship);
                                }
                            }
                        }
                    }  
                }
            }
            urlRelationshipVo = relationshipMap.get(formUrl);
        } else {
            log.debug("直接获取");
            urlRelationshipVo = relationshipMap.get(formUrl);
        }
        return urlRelationshipVo;
    }

}
