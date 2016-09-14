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

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 业务处理公用方法类
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年5月31日 下午3:58:39
 *          Copyright 2015 by DigitalChina
 */
public class BusinessUtils {

    public static final String PERCENT = "100";

    /**
     * 处理百分比占比
     *
     * @param totalPercentStr
     * @param map
     * @return
     * @author zuoyue 2015年5月31日
     */
    public static String[] dealPercent(String totalPercentStr, Map<String, String> map) {
        String[] result = new String[2];
        float totalPercent = Float.parseFloat(totalPercentStr);
        float intactPercent = Float.parseFloat(PERCENT);
        for (Entry<String, String> entry : map.entrySet()) {
            float eventPercent = Float.parseFloat(entry.getKey());
            String indexString = entry.getValue();
            if (indexString.indexOf(",") < 0) {
                result[0] = indexString;
                result[1] = String.format("%.1f", eventPercent + intactPercent - totalPercent);
                break;
            }
        }
        return result;
    }

    /**
     * 生成指定范围内的随机数
     *
     * @return
     * @author zuoyue 2015年5月30日
     */
    public static String sRandomGet(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

    /**
     * 判断list是否有记录
     *
     * @param list
     * @return
     * @author zuoyue 2015年5月30日
     */
    public static boolean isListNotNull(List<?> list) {
        return list != null && list.size() > 0;
    }

    /**
     * 字段验证
     *
     * @param isValidateNull
     * @param pattern
     * @param vaLength
     * @param value
     * @return
     * @author zuoyue 2016年3月10日
     */
    public static int validateParam(int isValidateNull, Pattern pattern, int vaLength, String value) {
        int flag = 0;
        // 需要验证是否为空
        if (isValidateNull == 1) {
            if (StringUtils.isBlank(value)) {
                return 1;
            }
        }
        // 需要验证格式
        if (pattern != null) {
            if (!pattern.matcher(value).matches()) {
                return 2;
            }
        }
        // 需要验证长度
        if (vaLength > 0) {
            if (ReflectUtil.getWordLength(value, null) > vaLength) {
                return 3;
            }
        }
        return flag;
    }

}
