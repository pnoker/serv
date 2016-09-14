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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例ParamManager
 *
 * @author zy
 * @version V1.0 创建时间：2015年5月13日 上午11:12:15
 *          Copyright 2015 by DigitalChina
 */
public class DictManager {

    private Map<String, Object> initParamMap = new ConcurrentHashMap<String, Object>();

    private DictManager() {

    }

    private static class ParamManagerInstance {
        private static final DictManager instance = new DictManager();
    }

    public static DictManager getInstance() {
        return ParamManagerInstance.instance;
    }

    /**
     * 将码表信息放入map集合
     *
     * @param targetMap
     * @author zuoyue 2015年9月23日
     */
    public void addTargetMapToGlobalMap(Map<String, Object> targetMap) {
        if (targetMap != null && !targetMap.isEmpty()) {
            for (Entry<String, Object> entry : targetMap.entrySet()) {
                initParamMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 将码表信息放入map集合
     *
     * @param type
     * @param obj
     * @author zy 2015年4月28日
     */
    public void addParamsToMap(String type, Object obj) {
        initParamMap.put(type, obj);
    }

    /**
     * 通过type与value从内存中获取code，或者通过type与code从内存中获取value
     *
     * @param param1
     * @param codeOrValue
     * @return
     * @author zy 2015年4月28日
     */
    public String findInitValue(String param1, String codeOrValue) {
        return (String) initParamMap.get(param1 + codeOrValue);
    }

    /**
     * 通过type从内存中获取code，value的map集合
     *
     * @param type
     * @return
     * @author zy 2015年4月28日
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> findInitMapByType(String type) {
        return (initParamMap.get(type) instanceof Map<?, ?>) ? (Map<String, String>) initParamMap.get(type) : null;
    }

    /**
     * 得到码表信息的map集合
     *
     * @return
     * @author zy 2015年4月28日
     */
    public Map<String, Object> findInitParamsMap() {
        return initParamMap;
    }

}