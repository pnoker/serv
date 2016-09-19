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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dc.city.common.utils.AopTargetUtils;
import com.dc.city.common.utils.OperationMethod;
import com.dc.city.common.utils.OperationMethod.MethodType;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.common.vo.AuthOpreateTypesVo;

/**
 * 主要是获取访问路径中操作类型类
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年7月24日 下午1:29:41
 *          Copyright 2015 by DigitalChina
 */
public class AuthApplicationContentTool implements ApplicationContextAware {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    /**
     * 装载操作类型的map,key为访问的url地址
     */
    private static Map<String, AuthOpreateTypesVo> _typesMap = new HashMap<String, AuthOpreateTypesVo>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        AuthApplicationContentTool.applicationContext = applicationContext;
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    /**
     * 获取访问的操作类型
     *
     * @param url
     * @return
     * @author xutaog 2015年7月24日
     */
    public static AuthOpreateTypesVo acquireMehtodOpreateType(String url) {
        AuthOpreateTypesVo vo = null;
        if (_typesMap == null || _typesMap.size() == 0 || _typesMap.isEmpty()) {// 如果访问的操作类型为空
            // 初始化类型操作类型Map
            // 获取所有spring装配的bean
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            for (String beanName : beanNames) {
                // 判断是否是控制成的bean
                if (beanName.contains("Controller")) {
                    // 得到class对象
                    Class<?> beanClass = AopTargetUtils.getTarget(applicationContext.getBean(beanName)).getClass();
                    // 如果取出的类不为空
                    if (beanClass != null) {
                        fillTypesMap(beanClass);
                    }
                }
            }
            // 从中获取
            vo = _typesMap.get(url);
        } else {// 不为空就直接用url获取
            vo = _typesMap.get(url);
        }
        return vo;
    }

    /**
     * 填充类型Map
     *
     * @param beanClass
     * @author xutaog 2015年7月28日
     */
    private static void fillTypesMap(Class<?> beanClass) {
        AuthOpreateTypesVo vo;
        // 是否包含path注解
        boolean isPathAnnotationExist = beanClass.isAnnotationPresent(Path.class);
        boolean isRequestMapExist = beanClass.isAnnotationPresent(RequestMapping.class);
        if (isPathAnnotationExist || isRequestMapExist) {// 如果存在
            String classPath = "";
            if (isPathAnnotationExist) {
                classPath = addUrlStart(beanClass.getAnnotation(Path.class).value());
            } else {
                classPath = addUrlStart(beanClass.getAnnotation(RequestMapping.class).value()[0]);
            }
            // 得到方法名
            Method[] methods = beanClass.getMethods();
            String postUrl = "";
            for (Method method : methods) {
                method.setAccessible(true);
                boolean isMethodPathExist = method.isAnnotationPresent(Path.class);
                boolean isMethodRequestMapExist = method.isAnnotationPresent(RequestMapping.class);
                if (isMethodPathExist || isMethodRequestMapExist) {
                    if (isMethodPathExist) {// 判断是否是spring-mvc或cxf
                        postUrl = classPath + addUrlStart(method.getAnnotation(Path.class).value());
                    } else {
                        postUrl = classPath + addUrlStart(method.getAnnotation(RequestMapping.class).value()[0]);
                    }
                    // 方法参数类型
                    MethodType[] methodParamType = new MethodType[0];
                    // 是否存在权限定义注解
                    boolean isMethodTypeExist = method.isAnnotationPresent(OperationMethod.class);
                    int del = 0;// 删除权限
                    int add = 0;// 增加权限
                    int upd = 0;// 修改权限
                    int auth = 0;// 授权
                    int find = 0;// 查询
                    if (isMethodTypeExist) {
                        methodParamType = method.getAnnotation(OperationMethod.class).methodType();
                        if (methodParamType.length > 0) {// 如果操作类型长度大于0
                            for (MethodType type : methodParamType) {// 循环验证有无相应的权限
                                String typeStr = type.toString();
                                if (typeStr.equalsIgnoreCase(MethodType.ADD.toString())) {
                                    add = 1;
                                } else if (typeStr.equalsIgnoreCase(MethodType.DEL.toString())) {
                                    del = 1;
                                } else if (typeStr.equalsIgnoreCase(MethodType.AUTH.toString())) {
                                    auth = 1;
                                } else if (typeStr.equalsIgnoreCase(MethodType.UPD.toString())) {
                                    upd = 1;
                                } else {
                                    find = 1;
                                }
                            }
                        }
                    } else {// 没有注解就将查询的值置为1
                        find = 1;
                    }
                    // 权限匹配完之后获取相应的权限
                    vo = new AuthOpreateTypesVo();
                    vo.setAdd(add);
                    vo.setAuth(auth);
                    vo.setDel(del);
                    vo.setUpd(upd);
                    vo.setFind(find);
                    vo.setUrl(postUrl);
                    vo.setCxf(isMethodPathExist);//表示是否是cxf框架
                    _typesMap.put(postUrl, vo);
                }
            }
        }
    }

    /**
     * 给没有开始“/”的加上“/”
     *
     * @param url
     * @return
     * @author xutaog 2015年7月29日
     */
    private static String addUrlStart(String url) {
        if (!StringUtils.isNullOrEmpty(url)) {
            String retUrl = url.startsWith("/") ? url : "/" + url;
            return retUrl;
        }
        return null;
    }
}
