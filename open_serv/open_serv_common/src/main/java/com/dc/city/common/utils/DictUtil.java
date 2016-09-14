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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据字典工具类
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年5月13日 下午2:16:26
 *          Copyright 2015 by DigitalChina
 */
public class DictUtil {
    
    /**
     * 通过type从内存中获取code，value的map集合
     *
     * @param dictType
     * @return
     * @author zy 2015年5月13日
     */
    public static Map<String, String> findInitMapByType(String dictType) {
        return StringUtils.isNotBlank(dictType) ? DictManager.getInstance().findInitMapByType(dictType) : null;
    }

    /**
     * 通过type与value从内存中获取code，或者通过type与code从内存中获取value
     *
     * @param param1
     * @param param2
     * @return
     * @author zuoyue 2015年5月13日
     */
    public static String findInitValue(String param1, String param2) {
        return StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) ? DictManager.getInstance()
                .findInitValue(param1, param2) : null;
    }

}
