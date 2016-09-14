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
package com.dc.city.domain.config;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;

public class ServeConfig implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3153450460570358720L;

    @FormParam("id")
    private long id;
    // 服务创建者
    private String createUser;
    @FormParam("parentId")
    private long parentId;

    @FormParam("dataSource")
    private Long dataSourceId;

    @FormParam("serviceName")
    private String serviceName;

    @FormParam("serviceCode")
    private String serviceCode;

    @FormParam("serviceType")
    private Integer serviceType;

    @FormParam("treeLevel")
    private Integer treeLevel;

    @FormParam("isService")
    private Integer isService;

    private String querySql;

    private String querySqlClob;

    private String configRemark;

    private String resultFormat;

    private String requestMethod;

    @FormParam("verifyAccess")
    private Integer verifyAccess;

    @FormParam("verifyView")
    private Integer verifyView;

    @FormParam("verifyIp")
    private Integer verifyIp;

    private String requestExampleUrl;

    private String otherInfo;

    private String publishRemark;

    private Date createTime;

    private Date publishTime;

    @FormParam("serviceStatus")
    private Integer serviceStatus;

    @FormParam("isDeleted")
    private Integer isDeleted;

    private Date updatetime;
    // 是否包含时间区间(1：是，0：否)
    private int isDateRange;
    // 向导配置的表名
    private String dataTableName;
    // 空间GIS服务 独有的信息
    private ServeExtendSpace serveExtendSpace;

    List<ServeInputParam> inputArgs;

    List<ServeSegment> segments;

    List<ServeOutputParam> outputArgs;

    List<ServeInputParamNav> inputArgsNavs;

    List<ServeDataRange> dateRangeArgs;

    List<ServeOutputParamNav> outputArgsNav;

    List<ServeConfigWhiteList> whiteList;

    List<ServeResultExample> exampleList;

    public List<ServeResultExample> getExampleList() {
        return exampleList;
    }

    public void setExampleList(List<ServeResultExample> exampleList) {
        this.exampleList = exampleList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    public Integer getIsService() {
        return isService;
    }

    public void setIsService(Integer isService) {
        this.isService = isService;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getConfigRemark() {
        return configRemark;
    }

    public void setConfigRemark(String configRemark) {
        this.configRemark = configRemark;
    }

    public String getResultFormat() {
        return resultFormat;
    }

    public void setResultFormat(String resultFormat) {
        this.resultFormat = resultFormat;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Integer getVerifyAccess() {
        return verifyAccess;
    }

    public void setVerifyAccess(Integer verifyAccess) {
        this.verifyAccess = verifyAccess;
    }

    public Integer getVerifyView() {
        return verifyView;
    }

    public void setVerifyView(Integer verifyView) {
        this.verifyView = verifyView;
    }

    public Integer getVerifyIp() {
        return verifyIp;
    }

    public void setVerifyIp(Integer verifyIp) {
        this.verifyIp = verifyIp;
    }

    public String getRequestExampleUrl() {
        return requestExampleUrl;
    }

    public void setRequestExampleUrl(String requestExampleUrl) {
        this.requestExampleUrl = requestExampleUrl;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getPublishRemark() {
        return publishRemark;
    }

    public void setPublishRemark(String publishRemark) {
        this.publishRemark = publishRemark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getUpdatetime() {
        if (updatetime == null) {
            this.updatetime = Calendar.getInstance().getTime();
        }
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public List<ServeInputParam> getInputArgs() {
        return inputArgs;
    }

    public void setInputArgs(List<ServeInputParam> inputArgs) {
        this.inputArgs = inputArgs;
    }

    public List<ServeSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<ServeSegment> segments) {
        this.segments = segments;
    }

    public List<ServeOutputParam> getOutputArgs() {
        return outputArgs;
    }

    public void setOutputArgs(List<ServeOutputParam> outputArgs) {
        this.outputArgs = outputArgs;
    }

    public List<ServeInputParamNav> getInputArgsNavs() {
        return inputArgsNavs;
    }

    public void setInputArgsNavs(List<ServeInputParamNav> inputArgsNavs) {
        this.inputArgsNavs = inputArgsNavs;
    }

    public List<ServeDataRange> getDateRangeArgs() {
        return dateRangeArgs;
    }

    public void setDateRangeArgs(List<ServeDataRange> dateRangeArgs) {
        this.dateRangeArgs = dateRangeArgs;
    }

    public List<ServeOutputParamNav> getOutputArgsNav() {
        return outputArgsNav;
    }

    public void setOutputArgsNav(List<ServeOutputParamNav> outputArgsNav) {
        this.outputArgsNav = outputArgsNav;
    }

    public List<ServeConfigWhiteList> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<ServeConfigWhiteList> whiteList) {
        this.whiteList = whiteList;
    }

    public int getIsDateRange() {
        return isDateRange;
    }

    public void setIsDateRange(int isDateRange) {
        this.isDateRange = isDateRange;
    }

    public String getDataTableName() {
        return dataTableName;
    }

    public void setDataTableName(String dataTableName) {
        this.dataTableName = dataTableName;
    }

    public String getQuerySqlClob() {
        return querySqlClob;
    }

    public void setQuerySqlClob(String querySqlClob) {
        this.querySqlClob = querySqlClob;
    }

    public ServeExtendSpace getServeExtendSpace() {
        return serveExtendSpace;
    }

    public void setServeExtendSpace(ServeExtendSpace serveExtendSpace) {
        this.serveExtendSpace = serveExtendSpace;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}