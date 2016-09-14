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

/**
 * unicode类
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年8月12日 下午7:14:05
 *          Copyright 2015 by DigitalChina
 */
public class UnicodeUtils {

    /**
     * 将unicode 字符串
     *
     * @param str 待转字符串
     * @return 普通字符串
     * @author zuoyue 2015年8月28日
     */
    public static String revert(String str) {
        str = (str == null ? "" : str);
        if (str.indexOf("\\u") == -1)// 如果不是unicode码则原样返回
            return str;

        StringBuffer sb = new StringBuffer(1000);

        for (int i = 0; i <= str.length() - 6;) {
            String strTemp = str.substring(i, i + 6);
            String value = strTemp.substring(2);
            int c = 0;
            for (int j = 0; j < value.length(); j++) {
                char tempChar = value.charAt(j);
                int t = 0;
                switch (tempChar) {
                    case 'a':
                        t = 10;
                        break;
                    case 'b':
                        t = 11;
                        break;
                    case 'c':
                        t = 12;
                        break;
                    case 'd':
                        t = 13;
                        break;
                    case 'e':
                        t = 14;
                        break;
                    case 'f':
                        t = 15;
                        break;
                    default:
                        t = tempChar - 48;
                        break;
                }

                c += t * ((int) Math.pow(16, (value.length() - j - 1)));
            }
            sb.append((char) c);
            i = i + 6;
        }
        return sb.toString();
    }

    /**
     * 将字符串转成unicode
     *
     * @param str 转字符串
     * @return unicode字符串
     * @author zuoyue 2015年8月28日
     */
    public static String convert(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>> 8); // 取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); // 取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }

}
