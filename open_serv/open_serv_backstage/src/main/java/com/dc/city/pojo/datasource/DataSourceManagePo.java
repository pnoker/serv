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

import com.dc.city.domain.datasource.ServeDataSource;

/**
 * 数据源管理po
 * 供数据源列表使用
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月9日 下午5:36:33
 *          Copyright 2016 by DigitalChina
 */

public class DataSourceManagePo extends ServeDataSource {

    //连接状态
    private int connectionStatus;
    
    public int getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(int connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    /**
     * 起始行数
     */
    private int beginRowNum;

    /**
     * 截止行数
     */
    private int endRowNum;

    public int getBeginRowNum() {
        return beginRowNum;
    }

    public void setBeginRowNum(int beginRowNum) {
        this.beginRowNum = beginRowNum;
    }

    public int getEndRowNum() {
        return endRowNum;
    }

    public void setEndRowNum(int endRowNum) {
        this.endRowNum = endRowNum;
    }

}
