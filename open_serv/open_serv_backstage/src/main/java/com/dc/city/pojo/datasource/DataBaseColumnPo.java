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
package com.dc.city.pojo.datasource;

/**
 * 获取数据表字段信息po
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月9日 下午5:36:33
 *          Copyright 2016 by DigitalChina
 */

public class DataBaseColumnPo {

    private String columnName;
    private String columnType;
    @SuppressWarnings("unused")
    private int columnTypeCode;

    // 列类型0:int,1:string,2:float,3:date,4:datetime, 时间格式为标准格式（yyyy-MM-dd HH:mm:ss),5 大字段
    private static final int COLUMN_TYPE_INT = 0;
    private static final int COLUMN_TYPE_STRING = 1;
    private static final int COLUMN_TYPE_FLOAT = 2;
    private static final int COLUMN_TYPE_DATE = 3;
    private static final int COLUMN_TYPE_TIME = 4;
    // 大字段
    private static final int COLUMN_TYPE_DATA = 5;

    public int getColumnTypeCode() {
        switch (this.columnType.toUpperCase()) {
            // 字符串类型
            case "VARCHAR2":
            case "VARCHAR":
            case "CHAR":
                return COLUMN_TYPE_STRING;
                // 时间戳类型
            case "TIMESTAMP(6)":
            case "DATETIME":
                return COLUMN_TYPE_TIME;
            case "DATE":
                return COLUMN_TYPE_DATE;
                // 整形
            case "NUMBER":
            case "INT":
                return COLUMN_TYPE_INT;
                // 浮点型
            case "FLOAT":
                return COLUMN_TYPE_FLOAT;
                // 大字段类型
            case "CLOB":
            case "BLOB":
                return COLUMN_TYPE_DATA;
            default:
                break;
        }
        return COLUMN_TYPE_STRING;
    }

    public void setColumnTypeCode(int columnTypeCode) {}

    private String columnDesc;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

}
