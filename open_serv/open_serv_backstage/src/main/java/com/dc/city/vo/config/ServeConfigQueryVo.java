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
package com.dc.city.vo.config;

import javax.ws.rs.FormParam;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.dc.city.pojo.serve.config.ServeConfigQueryPo;

/**
 * 服务列表查询参数封住类
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月24日 下午5:30:27
 *          Copyright 2016 by DigitalChina
 */
public class ServeConfigQueryVo extends ServeConfigQueryPo {

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
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 每页记录数
     */
    @JsonIgnore
    @FormParam("pageSize")
    private int pageSize;
    
    
    //datatables 分页字段
    @JsonIgnore
    @FormParam("start")
    private int start;
    
    //datatables 分页字段
    @JsonIgnore
    @FormParam("length")
    private int length;
    

    /**
     * 当前页码
     */
    @JsonIgnore
    @FormParam("currentPage")
    private int currentPage;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}
