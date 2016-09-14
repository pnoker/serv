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
package com.dc.city.common.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * SpringBean的示例化
 *
 * @author ligen
 * @version V1.0 创建时间：2015年4月20日 下午4:11:45
 *          Copyright 2015 by DigitalChina
 */
@Lazy(false)
@Component
// 获得ApplicationContext对象。
// 如果我们需要在Spring容器完成Bean的实例化，配置和其他的初始化后添加一些自己的逻辑处理，我们就可以定义一个或者多个BeanPostProcessor接口的实现。
// 使用了多个的BeanPostProcessor的实现类，那么如何确定处理顺序呢？只要实现Ordered接口，设置order属性就可以确定不同实现类的处理顺序了。
public class ApplicationContextUtils implements ApplicationContextAware, Ordered, BeanPostProcessor {
    private static ApplicationContext applicationContext;

    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getService(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getService(Class<T> type) {
        return applicationContext.getBean(type);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
