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
package com.dc.city.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * json null值转换类
 *
 * @author chenzpa
 * @version V1.0 创建时间：2015年9月16日 下午1:57:37
 *          Copyright 2015 by DigitalChina
 */
public class JsonNullValueSerializer extends JsonSerializer<Object> {
    // 空值
    private String nullValue = "null";

    public JsonNullValueSerializer(String nullValue) {
        this.nullValue = nullValue;
    }

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider arg2) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeString(nullValue);
    }

}
