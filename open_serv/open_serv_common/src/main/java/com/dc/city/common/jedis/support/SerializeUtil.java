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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;

/**
 * 将list与自定义对象进行序列化
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年9月18日 下午4:13:37
 *          Copyright 2015 by DigitalChina
 */
public class SerializeUtil {
    private static final Log LOG = LogFactory.getLog(SerializeUtil.class);

    /**
     * 将序列化对象转换成序列的byte型
     *
     * @param object
     * @return
     * @author xutaog 2015年9月18日
     */
    public static byte[] serialize(Serializable object) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            // 序列化
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            LOG.error("serialize fail", e);
        }
        return null;
    }

    /**
     * 将对象反序列成对象
     *
     * @param bytes
     * @return
     * @author xutaog 2015年9月18日
     */
    public static Object unserialize(byte[] bytes) {
        if (null != bytes) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
                // 反序列化
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                LOG.error("unserialize fail", e);
            }
        }
        return null;
    }
}