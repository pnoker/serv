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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private StringUtils() {

    }

    private static final String EMPTY = "";
    // HQL等号
    public static final String HQL_EQ = "=";
    // HQL模糊查询
    public static final String HQL_LIKE = "like";
    // HQL in
    public static final String HQL_IN = "in";
    // HQL is
    public static final String HQL_IS = "is";
    // HQL 空白
    public static final String HQL_EMPTY = "";

    /**
     * 判断是否为null或空字符串。如果不为null，在判断是否为空字符串之前会调用trim()。
     * 
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().equals(EMPTY);
    }

    /**
     * 判断多个字符串是否为空
     *
     * @param strs
     * @return
     * @author xutaog 2015年8月4日
     */
    public static boolean strsIsNotNullOrEmpty(String... strs) {
        boolean ret = true;
        for (int i = 0; i < strs.length; i++) {
            if (isNullOrEmpty(strs[i])) {
                return false;
            }
        }
        return ret;
    }

    /**
     * 消除html标签。
     * 
     * @param src
     * @return
     */
    public static String escapeHtml(String src) {
        if (src == null)
            return null;
        return src.replaceAll("<[a-zA-Z/][.[^<]]*>", "");
    }

    /**
     * 消除html特殊标记。如&nbsp;
     * 
     * @param src
     * @return
     */
    public static String escapeBlank(String src) {
        if (src == null)
            return null;
        return src.replaceAll("&[a-zA-Z]+;", "");
    }

    /**
     * 消除img标签
     * 
     * @param src
     * @return
     */
    public static String escapeImg(String src) {
        if (src == null)
            return null;
        return src.replaceAll("<img.*/>", "");
    }

    /**
     * 消除html标签和特殊标记
     * 
     * @param src
     * @return
     */
    public static String escapeHtmlAndBlank(String src) {
        return escapeBlank(escapeHtml(src));
    }

    /**
     * 复制字符串
     * 
     * @param src
     *            源字符串
     * @param times
     *            复制次数
     * @return
     */
    public static String repeat(String src, int times) {
        StringBuffer buffer = new StringBuffer(src);
        for (int i = 0; i < times - 1; i++)
            buffer.append(src);
        return buffer.toString();
    }

    public static String[] printSegmentation(String str, int numberOfCharacters) {
        double number0 = str.length() / numberOfCharacters;
        int quantity;
        if (str.length() % numberOfCharacters == 0)
            quantity = (int) number0;
        else
            quantity = (int) (number0 + 1);
        String[] results = new String[quantity];
        for (int i = 0; i < quantity; i++) {
            String str0;
            if (i == quantity - 1)
                str0 = str;
            else {
                str0 = str.substring(0, numberOfCharacters);
                str = str.substring(numberOfCharacters);
            }
            results[i] = str0;
        }
        return results;
    }

    /**
     * 去掉脚本信息
     * 
     * @param str
     * @return
     */
    public static String removeScript(String str) {
        if (!StringUtils.isNullOrEmpty(str)) {
            Pattern p = Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = p.matcher(str);
            return matcher.replaceAll("");
        }
        return null;
    }

    /**
     * 转义html特殊标签
     * 
     * @param src
     * @return
     */
    public static String escapeSpecialLabel(String src) {
        if (isNullOrEmpty(src))
            return null;
        return src.replaceAll("&", "&amp;").replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;")
                .replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;")
                .replaceAll("\"", "&quot;");
    }

/**
	 * 反转义html特殊标签
	 * @author xujianguo
	 * @param processBracket 是否处理尖括号'<' 和'>'
	 */
    public static String unEscapeSpecialLabel(String src, boolean processBracket) {
        if (isNullOrEmpty(src))
            return null;
        src = src.replaceAll("(?i)&amp;", "&").replaceAll("(?i)<br\\s*/?\\s*>", "\r\n").replaceAll("(?i)&nbsp;", " ")
                .replaceAll("(?i)&quot;", "\"");
        if (processBracket)
            src = src.replaceAll("(?i)&lt;", "\\<").replaceAll("(?i)&gt;", "\\>");
        return src;
    }

    public static String escapeSpecialChar(String str) {
        str = escapeHtml(escapeBlank(str));
        // String
        // regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_ 《》]";
        String regEx = "[^a-zA-Z0-9\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String removeParameter(String queryString, String parameter) {
        StringBuffer buffer = new StringBuffer();
        String[] parameterPairs = queryString.split("&");
        for (int i = 0; i < parameterPairs.length; i++) {
            String[] kvs = parameterPairs[i].split("=");
            if (kvs[0].equals(parameter))
                continue;
            buffer.append(kvs[0]);
            if (kvs.length == 2)
                buffer.append("=").append(kvs[1]);
            buffer.append("&");
        }
        if (buffer.length() > 0)
            buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    /**
     * 按字节码删除字符串<br/>
     * 如果开始的字节索引是双字节的字符的中间位将被截取<br/>
     * String str = "零壹贰叁肆";
     * 如果结束的字节索引是双字节的字符的中间位将不被截取<br/>
     * String str = "零壹贰叁肆";
     * 
     * @param inputSrc
     *            被截取的字符串
     * @param startByteIndex
     *            开始的字节索引
     * @param endByteIndex
     *            结束的字节索引
     * @return
     */
    public static String deleteByteLength(String inputSrc, int startByteIndex, int endByteIndex) {
        StringBuilder sb = new StringBuilder(inputSrc);
        int inputByteLength = inputSrc.getBytes().length;
        if (startByteIndex < 0)
            startByteIndex = 0;
        if (endByteIndex > inputByteLength)
            endByteIndex = inputByteLength;
        char[] inputChars = inputSrc.toCharArray();
        int startByteLength = 0;
        int startIndex = 0;
        for (Character c : inputChars) {
            int charByteLength = c.toString().getBytes().length;
            startByteLength += charByteLength;
            if (startByteLength > startByteIndex)
                break;
            startIndex++;
        }
        int endByteLength = 0;
        int endIndex = 0;
        for (Character c : inputChars) {
            int charByteLength = c.toString().getBytes().length;
            endByteLength += charByteLength;
            if (endByteLength > endByteIndex)
                break;
            endIndex++;
        }
        return sb.delete(startIndex, endIndex).toString();
    }

    /**
     * 判断是否是中文
     * 
     * @return
     */
    public static boolean isChinese(char c) {
        return String.valueOf(c).getBytes().length == 2;
    }

    /**
     * 判断字符串中是否包含中文
     * 
     * @return
     */
    public static boolean containsChinese(String str) {
        if (isNullOrEmpty(str))
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (isChinese(str.charAt(i)))
                return true;
        }
        return false;
    }

    /**
     * 切去字符串长度
     * 
     * @param value
     * @param length
     * @param suffix 后缀表示
     * @return
     */
    public static String replace(String value, int length, String suffix) {
        String str = "";
        if (isNullOrEmpty(value)) {
            return str;
        }
        if (length > 0 && length < value.length()) {
            str = value.substring(0, length);
            if (!isNullOrEmpty(suffix)) {
                str += suffix;
            }
        } else {
            str = value;
        }

        return str;
    }

    /**
     * Internet类型变量非空处理
     *
     * @param teger
     * @return
     * @author lin 2014年8月26日
     */
    public static Integer noIntegerEmpty(Integer paramInt) {
        Integer resultInteger = paramInt == null ? 0 : paramInt;
        return resultInteger;

    }

    /**
     * Shortl类型变量非空处理
     *
     * @param paramShort
     * @return
     * @author lin 2014年8月26日
     */
    public static Short noShortEmpty(Short paramShort) {
        Short resultShort = paramShort == null ? 0 : paramShort;
        return resultShort;
    }
}
