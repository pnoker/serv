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
package com.dc.city.common.jedis.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * 
 * 类说明 动态代理
 *
 * @author sunjl
 * @version V1.0 创建时间：2015年5月5日 上午9:31:33
 *          Copyright 2015 by DigitalChina
 */
public class RedisUtilProxy implements InvocationHandler {
    private Object subject;

    public RedisUtilProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (RedisSupport.isAlive == false) {
            RedisSupport.needDelQueue.add((String) args[0]);
            return null;
        }

        Object retObj = method.invoke(subject, args);
        return retObj;
    }

}
