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
package com.dc.city.listener.securitymanage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例BlackListManager
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月8日 下午5:47:05
 *          Copyright 2016 by DigitalChina
 */
public class BlackListManager {

    private Map<Integer, String> blackListMap = new ConcurrentHashMap<Integer, String>();

    private BlackListManager() {

    }

    private static class BlackListManagerInstance {
        private static final BlackListManager instance = new BlackListManager();
    }

    public static BlackListManager getInstance() {
        return BlackListManagerInstance.instance;
    }

    /**
     * 将黑名单放入map集合
     *
     * @param id
     * @param key
     * @author zuoyue 2016年3月8日
     */
    public void addBlackListToMap(Integer id, String key) {
        blackListMap.put(id, key);
    }

    /**
     * 得到黑名单的map集合
     *
     * @return
     * @author zuoyue 2016年3月8日
     */
    public Map<Integer, String> findBlackListMap() {
        return blackListMap;
    }

    /**
     * 从黑名单的map集合中删除一条记录
     *
     * @return
     * @author zuoyue 2016年3月8日
     */
    public void removeBlackListFromMap(Integer id) {
        blackListMap.remove(id);
    }

}