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
package com.dc.city.common.vo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 接口返回vo对象的基类
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年5月26日 下午4:12:29
 *          Copyright 2015 by DigitalChina
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BaseVo {
    
    private String iTotalDisplayRecords;
    
    private String iTotalRecords;

    /**
     * 接口调用成功状态码
     */
    public static final String SUCCESS_CODE = "0";
    /**
     * 接口调用成功信息
     */
    private static final String SUCCESS_MSG = "处理成功";
    /**
     * 接口调用失败状态码
     */
    public static final String ERROR_CODE = "-1";

    /**
     * 返回码：0：成功；-1：失败,默认值为:0
     */
    private String resultCode = SUCCESS_CODE;
    /**
     * 返回码信息，默认值为:查询成功
     */
    private String resultInfo = SUCCESS_MSG;
    /**
     * 响应完成时间：yyyMMddhh
     */
    private String finishTime;
    /**
     * 响应消耗时间(毫秒)
     */
    private double useTime;

    /**
     * 总条数
     */
    private String totalCount;
    /**
     * 每页记录数
     */
    private String pageSize;
    /**
     * 总页数
     */
    private String pageCount;
    /**
     * 当前页码
     */
    private String currentPage;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public double getUseTime() {
        return useTime;
    }

    public void setUseTime(double useTime) {
        this.useTime = useTime;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
    
    public String getiTotalDisplayRecords() {
        this.iTotalDisplayRecords=this.totalCount;
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getiTotalRecords() {
        this.iTotalRecords=this.totalCount;
        return iTotalRecords;
    }

    public void setiTotalRecords(String iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

}
