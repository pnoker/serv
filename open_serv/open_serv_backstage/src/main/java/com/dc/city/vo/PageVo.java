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
package com.dc.city.vo;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 分页PageVo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年9月16日 上午9:13:33
 *          Copyright 2015 by DigitalChina
 */
public class PageVo {

    /**
     * 默认的每页记录数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /**
     * 默认的当前页码
     */
    public static final int DEFAULT_CURRENT_PAGE = 1;

    /**
     * 每页记录数
     */
    @JsonIgnore
    @FormParam("pageSize")
    private int pageSize;

    /**
     * 当前页码
     */
    @JsonIgnore
    @FormParam("currentPage")
    private int currentPage;
    
    
    /**
     * datatables 分页专用
     */
    @JsonIgnore
    @FormParam("start")
    private int start;

    /**
     * datatables 分页专用
     */
    @JsonIgnore
    @FormParam("length")
    private int length;
    
    /**
     * 起始行数
     */
    @JsonIgnore
    private int beginRowNum;
    /**
     * 截止行数
     */
    @JsonIgnore
    private int endRowNum;

    @XmlTransient
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @XmlTransient
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @XmlTransient
    public int getBeginRowNum() {
        return beginRowNum;
    }

    public void setBeginRowNum(int beginRowNum) {
        this.beginRowNum = beginRowNum;
    }

    @XmlTransient
    public int getEndRowNum() {
        return endRowNum;
    }

    public void setEndRowNum(int endRowNum) {
        this.endRowNum = endRowNum;
    }

}
