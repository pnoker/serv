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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.type.JavaType;

/**
 * json工具类
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年7月19日 下午4:58:18
 *          Copyright 2015 by DigitalChina
 */
public class JsonUtil {

    private static Log log = LogFactory.getLog("JsonUtil");

    private static final String JSON_ERROR_CODE = "2";
    private static final String JSON_ERROR_MSG = "处理发生异常";

    /**
     * object转json
     *
     * @param object
     * @return
     * @author zuoyue 2015年7月19日
     */
    public static String objectToJson(Object object) {
        String targetJson = "";
        try {
            ObjectMapper mapper = ObjectMapperManager.getInstance();
            mapper.setDateFormat(new SimpleDateFormat(DateUtils.DATE_FORMAT_2));
            mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException,
                        JsonProcessingException {
                    jg.writeString("");
                }
            });
            targetJson = mapper.writeValueAsString(object);
        } catch (IOException e) {
            targetJson = dealError();
            log.error(e.getMessage(), e);
        }
        return targetJson;
    }

    /**
     * json转list
     *
     * @param json
     * @param cla
     * @return
     * @author zuoyue 2015年8月6日
     */
    public static List<?> jsonToList(String json, Class<?> cla) {

        return jsonToList(json, cla, DateUtils.DATE_FORMAT_2);
    }

    /**
     * json转list
     *
     * @param json
     * @param cla
     * @return
     * @author zuoyue 2015年8月6日
     */
    public static List<?> jsonToList(String json, Class<?> cla, String dateFrmt) {
        List<?> list = null;
        try {
            ObjectMapper mapper = ObjectMapperManager.getInstance();
            mapper.setDateFormat(new SimpleDateFormat(dateFrmt));
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, cla);
            list = mapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private static String dealError() {
        return String.format("{\"%s\": \"%s\",\"%s\":\"%s\"}", "resultCode", JSON_ERROR_CODE, "resultInfo",
                JSON_ERROR_MSG);
    }

}
