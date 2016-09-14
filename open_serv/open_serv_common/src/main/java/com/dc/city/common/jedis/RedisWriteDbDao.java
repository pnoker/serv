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
package com.dc.city.common.jedis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.dc.city.common.jedis.support.RedisPersistenceDTO;
import com.dc.city.common.utils.ApplicationContextUtil;


/**
 * redis持久化写入数据库的方法
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年9月21日 上午10:50:34
 *          Copyright 2015 by DigitalChina
 */
@Repository
public class RedisWriteDbDao {

    private static final Log logger = LogFactory.getLog(RedisWriteDbDao.class);

    /**
     * 反射调用方法执行保存的方法
     *
     * @param dto
     * @author xutaog 2015年9月21日
     */
    public void createRedisEntity(RedisPersistenceDTO dto) {
        try {
            Object object = ApplicationContextUtil.getBean(dto.getBeanName());
            Method method = object.getClass().getDeclaredMethod(dto.getMethodName(), dto.getClassType());
            method.invoke(object, dto.getEntity());
        } catch (NoSuchMethodException | SecurityException e) {
            logger.error("保存错误！错误原因：" + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error("保存错误！错误原因：" + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.error("保存错误！错误原因：" + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.error("保存错误！错误原因：" + e.getMessage(), e);
        }
    }

}
