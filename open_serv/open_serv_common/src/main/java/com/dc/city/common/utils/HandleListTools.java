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

import java.util.ArrayList;
import java.util.List;

/**
 * 处理list相关的工具类 如截取list等
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年8月15日 上午11:43:33
 *          Copyright 2015 by DigitalChina
 */
public class HandleListTools {
    /**
     * 定义分批次入库的数量
     */
    public static final int BATCH_NUMBER = 20;

    /**
     * 拆分list
     *
     * @param listSize
     * @param originalList
     * @return
     * @author zuoyue 2015年6月16日
     */
    public static List<List<?>> splitList(int listSize, List<?> originalList) {
        List<List<?>> targetList = new ArrayList<List<?>>();
        // 拆分list
        int count = listSize / BATCH_NUMBER;
        int mod = listSize % BATCH_NUMBER;
        for (int i = 0; i < count; i++) {
            List<?> subList = originalList.subList(i * BATCH_NUMBER, (i + 1) * BATCH_NUMBER);
            targetList.add(subList);
        }
        if (mod != 0) {
            List<?> subList = originalList.subList(count * BATCH_NUMBER, (count * BATCH_NUMBER + mod));
            targetList.add(subList);
        }
        return targetList;
    }

}
