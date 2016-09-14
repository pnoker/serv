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
 * 
 * 分表hash函数工具类，根据主键及分表数量生成分表序号
 *
 * @author zhongdt
 * @version V1.0 创建时间：2015年9月29日 下午3:26:47
 *          Copyright 2015 by DigitalChina
 */
public class ShardHashUtils {
    
    /**
     * 
     * 根据主键及分表数量，生成该主键所存储的分表序号
     *
     * @param str
     * @param shardSize
     * @return
     * @author zhongdt 2015年9月29日
     */
    public static int getHash(String str,int shardSize){
        int hashCode = buildHashCode(str.toUpperCase());
        if (hashCode < 0) {
            //小于0的时候 ，取反。避免给造成取负数的情况
            hashCode = -hashCode;
        }
        return hashCode % shardSize;
    }
    
    /**
     * 
     * 根据主键值，生成int型的code
     *
     * @param 主键字符串
     * @return
     * @author zhongdt 2015年9月29日
     */
    private static int buildHashCode(String primaryValue){
        int multiplier=1;
        int length = primaryValue.length();
        int hashCode=0;
        
        //算法规则:asc1+asc2*10+asc3*1000+asc4*10000+asc5*100000.......
        for(int i=length-1;i>=0;i--){
            char c =primaryValue.charAt(i);
            int ascii = (int)c;
            if(c>127){
                ascii = 128;
            }
            hashCode += ascii*multiplier;
            multiplier*=10;
        }
        return hashCode>=0? hashCode:-hashCode;
    }
}
