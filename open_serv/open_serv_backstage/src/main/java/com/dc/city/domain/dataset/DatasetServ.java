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
package com.dc.city.domain.dataset;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 数据汇总平台-服务
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月18日 上午11:27:46
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "DatasetServ")
public class DatasetServ {

    private String servId;

    private int id;
    /*
     * 菜单接口名称
     */
    private String name;

    /**
     * 访问的url地址
     */
    private String url;

    /*
     * 菜单接口的父节点ID
     */
    private int parentId;

    /**
     * 服务的类型
     */
    private int servType;

    /**
     * 服务类型名称
     */
    private String servTypeName;

    /**
     * 描述
     */
    private String description;

    /**
     * 服务编码
     */
    private String servCode;

    /*
     * 菜单接口的父节点名称
     */
    private String parentName;
    /*
     * 菜单接口的目录级别
     */
    private int level;
    /*
     * 接口请求示例链接
     */
    private String requestDemoUrl;
    /*
     * 菜单接口的请求方式
     */
    private String methods;
    /*
     * 菜单接口的备注
     */
    private String remark;

    /*
     * 接口是否可见，可见（1）或不可见（0）
     */
    private int isVisible;
    /*
     * 接口更新的内容
     */
    private String updateContent;
    
    /*
     * 接口的状态，正常（1）或不正常（0）
     */
    private int state;
    /*
     * 接口创建时间
     */
    private String updateTime;
    /**
     * 接口请求参数，可能含多个
     */
    private List<ServRequestParams> params;
    /**
     * 接口返回字段，可能含多个
     */
    private List<ServResponseField> fields;

    /**
     * 服务例子
     */
    private List<ServExample> examples;

    /*
     * 接口显示的图片
     */
    private byte[] picture;
    /*
     * 转换成base64字符串的图片
     */
    private String picStr;

    public String getServTypeName() {
        return servTypeName;
    }

    public void setServTypeName(String servTypeName) {
        this.servTypeName = servTypeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ServExample> getExamples() {
        return examples;
    }

    public void setExamples(List<ServExample> examples) {
        this.examples = examples;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRequestDemoUrl() {
        return requestDemoUrl;
    }

    public void setRequestDemoUrl(String requestDemoUrl) {
        this.requestDemoUrl = requestDemoUrl;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<ServRequestParams> getParams() {
        return params;
    }

    public void setParams(List<ServRequestParams> params) {
        this.params = params;
    }

    public List<ServResponseField> getFields() {
        return fields;
    }

    public void setFields(List<ServResponseField> fields) {
        this.fields = fields;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPicStr() {
        return picStr;
    }

    public void setPicStr(String picStr) {
        this.picStr = picStr;
    }

    public int getServType() {
        return servType;
    }

    public void setServType(int servType) {
        this.servType = servType;
    }

    public String getServCode() {
        return servCode;
    }

    public void setServCode(String servCode) {
        this.servCode = servCode;
    }

    public String getServId() {
        return servId;
    }

    public void setServId(String servId) {
        this.servId = servId;
    }

}
