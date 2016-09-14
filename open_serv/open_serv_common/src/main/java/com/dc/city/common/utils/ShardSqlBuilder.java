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

/*@remark
 * sql builder 目前只支持字符型和int 型。其他暂不支持
 * sql builder 只支持=，大于 like 等暂不支持
 */

/**
 * 
 * 分表函数工具类，生成sql 工具类
 *
 * @author zhongdt
 * @version V1.0 创建时间：2015年9月29日 下午3:26:47
 *          Copyright 2015 by DigitalChina
 */
public class ShardSqlBuilder {
    
    /**
     * 根据条件生成查询sql
     *
     * @param param 条件集合
     * @param primaryKey 主键列明
     * @param columns 输出列字段，
     * @param tableName 分表的表名
     * @return
     * @author zhongdt 2015年9月28日
     */
    public static String buildQuerySQL(Map<String, Object> param, String primaryKey, String columns, String tableName,int shardSize) {
        // 单表查询标志
        boolean isSingle = false;
        String primaryValue = "";
        // 遍历条件，判断是否是按照单表查询
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (primaryKey.equalsIgnoreCase(entry.getKey())) {
                isSingle = true;
                primaryKey = entry.getValue() + "";
                break;
            }
        }

        StringBuffer sqlBuffer = new StringBuffer("");
        // 总共分表数量 ，目前支持2的n次方，在config.property里面配置
        if (isSingle) {
            // 通过主键，单表查询
            int index = ShardHashUtils.getHash(primaryValue, shardSize);
            sqlBuffer.append("select ").append(columns).append(" from ").append(tableName + "_" + index)
                    .append(buildParamStr(param));
        } else {
            // 通过union all的方式进行查询
            for (int i = 0; i < shardSize; i++) {
                sqlBuffer.append("select ").append(columns).append(" from ").append(tableName + "_" + i)
                        .append(buildParamStr(param));
                if (i != shardSize - 1) {
                    sqlBuffer.append(" union all ");
                }
            }
        }
        return sqlBuffer.toString();

    }

    /**
     * 拼装where条件
     *
     * @param param
     * @return
     * @author zhongdt 2015年9月28日
     */
    public static  String buildParamStr(Map<String, Object> param) {
        StringBuffer paramBuffer = new StringBuffer(" where 1 = 1");
        // 目前暂支持字符型和整形
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (entry.getValue() instanceof String) {
                paramBuffer.append(" and " + entry.getKey() + "='" + entry.getValue() + "'");
            } else {
                paramBuffer.append(" and " + entry.getKey() + "=" + entry.getValue());
            }
        }
        return paramBuffer.toString();
    }
}
