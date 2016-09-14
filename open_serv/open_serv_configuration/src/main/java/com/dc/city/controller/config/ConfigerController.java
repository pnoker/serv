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
package com.dc.city.controller.config;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;

import com.dc.city.common.datasource.DynamicDataSource.DbContextHolder;
import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.common.utils.JsonUtil;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.controller.catalogmanage.CatalogController;
import com.dc.city.controller.database.DataBaseInfoController;
import com.dc.city.controller.database.datasource.DataSourceManageController;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.config.ServeDataRange;
import com.dc.city.domain.config.ServeExtendSpace;
import com.dc.city.domain.config.ServeGisDictionary;
import com.dc.city.domain.config.ServeInputParam;
import com.dc.city.domain.config.ServeInputParamNav;
import com.dc.city.domain.config.ServeOutputParam;
import com.dc.city.domain.config.ServeOutputParamNav;
import com.dc.city.domain.config.ServeSegment;
import com.dc.city.listener.config.InitConfigerListener;
import com.dc.city.service.cache.PubCacheService;
import com.dc.city.service.config.ConfigerService;
import com.dc.city.vo.BaseVo;
import com.dc.city.vo.catalog.CataLogQueryVo;
import com.dc.city.vo.catalog.CatalogBaseVo;
import com.dc.city.vo.config.ConfigerVo;
import com.dc.city.vo.config.ServeAuthUserVo;
import com.dc.city.vo.database.ServiceBaseVo;
import com.dc.city.vo.database.datasource.DataSourceBaseVo;
import com.dc.city.vo.database.datasource.DataSourceManageVo;

/**
 * 服务配置控制层
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月8日 下午3:21:20
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/configer/v1")
@Produces({ "application/xml", "application/json" })
public class ConfigerController {
    @Resource
    private ConfigerService configerService;
    @Resource
    private PubCacheService pubCacheService;

    @Resource
    private DataBaseInfoController dbInfoController;
    @Resource
    private DataSourceManageController dataSourceController;
    @Resource
    private CatalogController catalogController;

    /**
     * 添加接口配置
     *
     * @param parentId 父id
     * @param dataSourceId 数据源ID
     * @param serviceName 接口名称
     * @param serviceCode 服务接口代码或者服务请求地址
     * @param serviceType 接口类型（3:空间数据，2::外部接口，1:配置接口）
     * @param configRemark 服务配置备注
     * @param requestMethod 请求方法,保存格式为,get,post,用逗号分隔,注意
     * @param querySql 生成的SQL
     * @param resultFormat 返回格式(xml/json) 保存格式为 ,xml,json用逗号分隔,注意
     * @param inputArgsJson 输入参数 列表的JSON串:-paramCode 参数名 -paramDesc 参数描述 -paramType 列类型 -isRequired
     *            是否必填
     * @param sqlSegmentJson sql 片段的JSON串 segmentCode -片段代码 -paramCode -入参代码 -replaceSql 实际需要替换的SQL
     * @param outputArgsJson 输出参数 列表的JSON串 -paramCode 参数名 -paramDesc 参数描述 -paramType 列类型
     * @param navInputParamJson 向导输入参数列表的JSON串；columnCode 表列名；paramCode
     *            参数名；columnDesc 列描述；operator 运算符；isRequired 是否必填(0:否 1:是)
     * @param navOutputParamJson 向导输出参数列表的JSON串:columnCode 字段名；columnDesc
     *            字段备注；columnLias 参数别名；sortOrder 排序类型(desc asc)
     * @param navDateRangeJson 向导 时间区间参数列表的JSON串；columnCode 列名；dataRange 近xx天 @return
     * @param editMark 添加服务的备注
     * @param operationId 操作人员ID
     * @author chenzpa 2016年3月10日
     */
    @POST
    @Path("/addconfiger")
    public ConfigerVo addConfiger(@FormParam("parentId") String parentId,
            @FormParam("dataSourceId") String dataSourceId, @FormParam("serviceName") String serviceName,
            @FormParam("serviceCode") String serviceCode, @FormParam("serviceType") String serviceType,
            @FormParam("configRemark") String configRemark, @FormParam("requestMethod") String requestMethod,
            @FormParam("querySql") String querySql, @FormParam("resultFormat") String resultFormat,
            @FormParam("inputArgsJson") String inputArgsJson, @FormParam("sqlSegmentJson") String sqlSegmentJson,
            @FormParam("outputArgsJson") String outputArgsJson, @FormParam("datatableName") String datatableName,
            @FormParam("navInputParamJson") String navInputParamJson,
            @FormParam("navOutputParamJson") String navOutputParamJson,
            @FormParam("navDateRangeJson") String navDateRangeJson, @FormParam("editMark") String editMark) {

        return editConfig(null, parentId, dataSourceId, serviceName, serviceCode, serviceType, configRemark,
                requestMethod, querySql, resultFormat, inputArgsJson, sqlSegmentJson, outputArgsJson, datatableName,
                navInputParamJson, navOutputParamJson, navDateRangeJson, editMark);

    }

    /**
     * 修改接口配置
     * 
     * @param configId 要修改的配置ID
     * @param parentId 父id
     * @param dataSourceId 数据源ID
     * @param serviceName 接口名称
     * @param serviceCode 服务接口代码或者服务请求地址
     * @param serviceType 接口类型（3:空间数据，2::外部接口，1:配置接口）
     * @param configRemark 服务配置备注
     * @param requestMethod 请求方法,保存格式为,get,post,用逗号分隔,注意
     * @param querySql 生成的SQL
     * @param resultFormat 返回格式(xml/json) 保存格式为 ,xml,json用逗号分隔,注意
     * @param inputArgsJson 输入参数 列表的JSON串:-paramCode 参数名 -paramDesc 参数描述 -paramType 列类型 -isRequired
     *            是否必填
     * @param sqlSegmentJson sql 片段的JSON串 segmentCode -片段代码 -paramCode -入参代码 -replaceSql 实际需要替换的SQL
     * @param outputArgsJson 输出参数 列表的JSON串 -paramCode 参数名 -paramDesc 参数描述 -paramType 列类型
     * @param navInputParamJson 向导输入参数列表的JSON串；columnCode 表列名；paramCode
     *            参数名；columnDesc 列描述；operator 运算符；isRequired 是否必填(0:否 1:是)
     * @param navOutputParamJson 向导输出参数列表的JSON串:columnCode 字段名；columnDesc
     *            字段备注；columnLias 参数别名；sortOrder 排序类型(desc asc)
     * @param navDateRangeJson 向导 时间区间参数列表的JSON串；columnCode 列名；dataRange 近xx天 @return
     * @param editMark 修改服务的备注
     * @param datatableName 数据表名字
     * @param operationId 操作人员ID
     * @author chenzpa 2016年3月10日
     */

    @POST
    @Path("/modifyconfiger")
    public ConfigerVo modifyConfiger(@FormParam("configId") String configId, @FormParam("parentId") String parentId,
            @FormParam("dataSourceId") String dataSourceId, @FormParam("serviceName") String serviceName,
            @FormParam("serviceCode") String serviceCode, @FormParam("serviceType") String serviceType,
            @FormParam("configRemark") String configRemark, @FormParam("requestMethod") String requestMethod,
            @FormParam("querySql") String querySql, @FormParam("resultFormat") String resultFormat,
            @FormParam("datatableName") String datatableName, @FormParam("inputArgsJson") String inputArgsJson,
            @FormParam("sqlSegmentJson") String sqlSegmentJson, @FormParam("outputArgsJson") String outputArgsJson,
            @FormParam("navInputParamJson") String navInputParamJson,
            @FormParam("navOutputParamJson") String navOutputParamJson,
            @FormParam("navDateRangeJson") String navDateRangeJson, @FormParam("editMark") String editMark,
            @FormParam("operationId") String operationId) {
        // 服务ID 不能为空
        if (checkArgIsNull("configId", configId).length() > 0) {
            ConfigerVo vo = new ConfigerVo();
            vo.setResultCode(BaseVo.ERROR_CODE);
            vo.setResultInfo("配置的ID　为空：configId");
            return vo;
        }

        return editConfig(configId, parentId, dataSourceId, serviceName, serviceCode, serviceType, configRemark,
                requestMethod, querySql, resultFormat, inputArgsJson, sqlSegmentJson, outputArgsJson, datatableName,
                navInputParamJson, navOutputParamJson, navDateRangeJson, editMark);
    }

    /**
     * 删除一条内部配置
     *
     * @param configId
     * @return
     * @author chenzpa 2016年4月12日
     */
    @POST
    @Path("/deleteconfiger")
    public ConfigerVo deleteConfiger(@FormParam("configId") String configId,
            @FormParam("serviceCode") String serviceCode) {
        ConfigerVo vo = new ConfigerVo();
        configerService.removeConfigerById(configId);
        // 刷新缓存
        RedisUtil.delByKey(InitConfigerListener.CONFIG_KEY_EXEC + serviceCode);

        // 发布订阅消息，通知其他服务器删除服务配置
        pubCacheService.publishServeConfig(PubCacheService.OPERATE_REMOVE, serviceCode);

        return vo;
    }

    /**
     * 通过ID查询内部服务
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年5月6日
     */
    @GET
    @Path("/queryconfiger")
    public ConfigerVo queryConfiger(@QueryParam("configerId") String configerId) {
        ConfigerVo vo = new ConfigerVo();
        // 服务主表信息
        ServeConfig config = configerService.queryConfiger(configerId);

        vo.setConfig(config);

        return vo;
    }

    /**
     * 查询当前config下，有权限的用户列表信息
     *
     * @param configerId
     * @return
     * @author zhongdt 2016年4月7日
     */
    @GET
    @Path("/queryconfigerAuthUsers")
    public ServeAuthUserVo queryConfigerAuthUsers(@QueryParam("configerId") Long configerId) {
        return this.configerService.queryConfigAuthUser(configerId);
    }

    /**
     * 单独添加向导配置的参数
     *
     * @param navInputParamJson 向导输入参数列表的JSON串:serviceId 服务ID；columnCode 表列名；paramCode
     *            参数名；columnDesc 列描述；operator 运算符；isRequired 是否必填(0:否 1:是)
     * @param navOutputParamJson 向导输出参数列表的JSON串:serviceId 服务ID；columnCode 字段名；columnDesc
     *            字段备注；columnLias 参数别名；sortOrder 排序类型(desc asc)
     * @param navDateRangeJson 向导 时间区间参数列表的JSON串；serviceId 服务ID；columnCode 列名；dataRange 近xx天
     * @return
     * @author chenzpa 2016年3月15日
     */
    // @SuppressWarnings("unchecked")
    // @POST
    // @Path("/addnavconfiger")
    // public BaseVo addNavConfiger(@FormParam("navInputParamJson") String navInputParamJson,
    // @FormParam("navOutputParamJson") String navOutputParamJson,
    // @FormParam("navDateRangeJson") String navDateRangeJson) {
    // BaseVo vo = new BaseVo();
    //
    // List<ServeInputParamNav> inputArgs = (List<ServeInputParamNav>)
    // JsonUtil.jsonToList(navInputParamJson,
    // ServeInputParamNav.class);
    // List<ServeDataRange> dateRanges = (List<ServeDataRange>)
    // JsonUtil.jsonToList(navDateRangeJson,
    // ServeDataRange.class);
    // List<ServeOutputParamNav> outputArgs = (List<ServeOutputParamNav>)
    // JsonUtil.jsonToList(navOutputParamJson,
    // ServeOutputParamNav.class);
    // int oNum = configerService.createOutputParamsNav(outputArgs);
    // int iNum = configerService.createInputParamsNav(inputArgs);
    // int deNum = configerService.createDateRangeArgs(dateRanges);
    // logger.debug("向导输出参数个数：" + oNum);
    // logger.debug("向导输入参数个数：" + iNum);
    // logger.debug("时间区间个数：" + deNum);
    // return vo;
    //
    // }

    /**
     * 删除一条GIS服务
     *
     * @param gisConfigId 服务ID
     * @return
     * @author chenzpa 2016年4月12日
     */
    @POST
    @Path("/deletegisconfig")
    public ConfigerVo deleteGISConfig(@FormParam("gisConfigId") String gisConfigId) {
        ConfigerVo vo = new ConfigerVo();
        configerService.deleteGISConfig(Long.parseLong(gisConfigId));
        return vo;
    }

    /**
     * 查询GIS服务 一条
     *
     * @param gisConfigId
     * @return
     * @author chenzpa 2016年4月12日
     */
    @GET
    @Path("/querygisconfig")
    public ConfigerVo queryGISConfig(@FormParam("gisConfigId") String gisConfigId) {

        return configerService.queryGisConfiger(gisConfigId);
    }

    /**
     * 添加或修改GIS服务配置
     * 
     * @param gisConfigId 修改时才有的配置ID，没有就是添加
     * @param parentId 目录ID
     * @param serviceName 服务名称
     * @param configRemark 服务备注
     * @param urlTest 测试地址
     * @param urlFormal 正式地址
     * @param geoFeature 几何特征(1点，2线，3面，4null)
     * @param maxLength 最大记录数
     * @param isVisible 图层可见性(1:可见，0 不可见)
     * @param layerType 图层类型(1:FEATURE,2:RASTER)
     * @param resultFormat 支持格式
     * @param displayScaleMax 最大显示比例
     * @param displayScaleMin 最小显示比例
     * @param isLabel 是否有标注(1:是,0:否)
     * @param gisServer ARCGIS_SERVER
     *            数据字典(1:M+K,2:M+K+WCS,3:M+K+MDB,4:M+K+WMS,5:M+K+WFS,6:M+K+FA.7:M+K+S,8:M+K+NA])
     * @param coordinateSystem 坐标系统:eg:gcs_wgs_1984
     * @param coordinateCode 坐标代码:eg:4326
     * @return
     * @author chenzpa 2016年3月17日
     */
    @SuppressWarnings({ "unchecked" })
    @POST
    @Path("/editgisconfig")
    public ConfigerVo addGISConfig(@FormParam("gisConfigId") String gisConfigId,
            @FormParam("parentId") String parentId, @FormParam("serviceName") String serviceName,
            @FormParam("configRemark") String configRemark, @FormParam("urlTest") String urlTest,
            @FormParam("urlFormal") String urlFormal, @FormParam("geoFeature") String geoFeature,
            @FormParam("maxLength") String maxLength, @FormParam("isVisible") String isVisible,
            @FormParam("layerType") String layerType, @FormParam("resultFormat") String resultFormat,
            @FormParam("displayScaleMax") String displayScaleMax, @FormParam("displayScaleMin") String displayScaleMin,
            @FormParam("isLabel") String isLabel, @FormParam("gisServer") String gisServer,
            @FormParam("coordinateSystem") String coordinateSystem, @FormParam("coordinateCode") String coordinateCode,
            @FormParam("inputArgsJson") String inputArgsJson, @FormParam("editMark") String editMark) {
        ConfigerVo vo = new ConfigerVo();
        String checkStr = "";
        checkStr += checkArgIsNull("parentId", parentId);
        checkStr += checkArgIsNull("configRemark", configRemark);
        checkStr += checkArgIsNull("serviceName", serviceName);
        checkStr += checkArgIsNull("urlTest", urlTest);
        checkStr += checkArgIsNull("urlFormal", urlFormal);
        checkStr += checkArgIsNull("geoFeature", geoFeature);

        // GIS 服务的数据字典 添加
        List<ServeGisDictionary> inputArgs = (List<ServeGisDictionary>) JsonUtil.jsonToList(inputArgsJson,
                ServeGisDictionary.class);

        // 如果有空参数
        if (checkStr.length() > 1) {
            vo.setResultCode(BaseVo.ERROR_CODE);
            vo.setResultInfo(checkStr);
            return vo;
        }

        ServeConfig conf = new ServeConfig();
        conf.setIsDeleted(0);
        conf.setParentId(Long.parseLong(parentId));
        conf.setServiceName(serviceName);
        // 接口类型（3:空间数据，2::外部接口，1:配置接口）
        conf.setServiceType(3);
        conf.setConfigRemark(configRemark);
        conf.setResultFormat(resultFormat);
        // 是否是服务(1：是 0 是目录)
        conf.setIsService(1);
        // 空间服务参数的 bean
        ServeExtendSpace serveExt = new ServeExtendSpace();
        serveExt.setCoordinateCode(coordinateCode);
        serveExt.setCoordinateSystem(coordinateSystem);

        if (!StringUtils.isNullOrEmpty(displayScaleMax)) {
            serveExt.setDisplayScaleMax(Double.parseDouble(displayScaleMax));
        }
        if (!StringUtils.isNullOrEmpty(displayScaleMin)) {
            serveExt.setDisplayScaleMin(Double.parseDouble(displayScaleMin));
        }
        if (!StringUtils.isNullOrEmpty(geoFeature)) {
            serveExt.setGe0Features(Integer.parseInt(geoFeature));
        }
        if (!StringUtils.isNullOrEmpty(gisServer)) {
            serveExt.setGisServer(Integer.parseInt(gisServer));
        }
        if (!StringUtils.isNullOrEmpty(isLabel)) {
            serveExt.setIsLabel(Integer.parseInt(isLabel));
        }
        if (!StringUtils.isNullOrEmpty(isVisible)) {
            serveExt.setIsVisible(Integer.parseInt(isVisible));
        }
        if (!StringUtils.isNullOrEmpty(layerType)) {
            serveExt.setLayerType(Integer.parseInt(layerType));
        }
        if (!StringUtils.isNullOrEmpty(maxLength)) {
            serveExt.setMaxLength(Integer.parseInt(maxLength));
        }
        serveExt.setUrlFormal(urlFormal);
        serveExt.setUrlTest(urlTest);

        return configerService.editGisConfiguer(gisConfigId, inputArgs, conf, serveExt, editMark);

    }

    /**
     * 发布服务 时保存一些信息
     *
     * @param configerId 服务ID
     * @param demoUrl 请求实例的URL
     * @param serverMark 服务备注
     * @param example 返回示例
     *            [{\"type\": "json",\"example\": "XSSSSSSSSSSS","serviceId":"1"},{\"type\": "
     *            XML",\"example\": "XMLSSSSSSS","serviceId":"2"}]
     * @param verifyAccess 是否验证访问
     * @param verifyView 是否验证查看
     * @param verifyIp 是否验证IP
     * @param otherInfo 其他信息
     * @param serviceStatus 服务接口的状态 如果是暂时保存的话就是未发布
     * @return
     * @author chenzpa 2016年3月16日
     */
    @POST
    @Path("/configerpublish")
    public BaseVo configerPublish(@FormParam("configerId") Long configerId,
            @FormParam("requestExampleUrl") String demoUrl, @FormParam("publishRemark") String publishRemark,
            @FormParam("resultExampleJson") String jsonExample, @FormParam("resultExampleXml") String xmlExample,
            @FormParam("verifyAccess") int verifyAccess, @FormParam("verifyView") int verifyView,
            @FormParam("verifyIp") int verifyIp, @FormParam("otherInfo") String otherInfo,
            @FormParam("serviceStatus") int serviceStatus, @FormParam("viewUsers") String viewUsers,
            @FormParam("accessUsers") String accessUsers, @FormParam("ipAddresses") String ipAddresses,
            @FormParam("editRemark") String editRemark) {

        return configerService.modifyPublishInfo(configerId, demoUrl, publishRemark, jsonExample, xmlExample,
                verifyAccess, verifyView, verifyIp, otherInfo, serviceStatus, viewUsers, accessUsers, ipAddresses,
                editRemark);

    }

    /**
     * 发布GIS服务
     *
     * @param configerId
     * @param serviceStatus
     * @param editRemark
     * @return
     * @author chenzpa 2016年4月14日
     */
    @POST
    @Path("/gisconfigerpublish")
    public BaseVo gisConfigerPublish(@FormParam("configerId") Long configerId,
            @FormParam("serviceStatus") int serviceStatus, @FormParam("editRemark") String editRemark) {

        return configerService.gisConfigerPublish(configerId, serviceStatus, editRemark);

    }
    
    /**
     * 服务审核
     *
     * @param configerId
     * @param serviceStatus
     * @param editRemark
     * @return
     * @author zuoyue 2016年8月16日
     */
    @POST
    @Path("/modifyservereviewstate")
    public BaseVo modifyServeReviewState(@FormParam("configerId") Long configerId,
            @FormParam("serviceStatus") int serviceStatus, @FormParam("editRemark") String editRemark) {
        return configerService.modifyServeReviewState(configerId, serviceStatus, editRemark);
    }

    /**
     * 删除一条外部配置
     *
     * @param configId
     * @return
     * @author chenzpa 2016年4月12日
     */
    @POST
    @Path("/deleteoutconfiger")
    public ConfigerVo deleteOutConfiger(@FormParam("outConfigId") String outConfigId) {
        ConfigerVo vo = new ConfigerVo();
        configerService.removeConfigerById(outConfigId);
        return vo;
    }

    /**
     * 查询外部服务
     *
     * @param outConfigId 服务ID
     * @return
     * @author chenzpa 2016年4月13日
     */
    @GET
    @Path("/queryoutconfig")
    public ConfigerVo queryOutConfig(@FormParam("outConfigId") String outConfigId) {
        return configerService.queryOutConfig(outConfigId);
    }

    /**
     * 增加和修改 外部服务配置
     *
     * @param outConfigId 外部服务的ID 为空是时候表示 添加，有值的时候就修啊
     * @param parentId 父目录
     * @param serviceCode 接口地址
     * @param serviceName 服务名称
     * @param requestExampleUrl 示例地址
     * @param configRemark 服务备注
     * @param requestMethod 请求方式
     * @param resultFormat 返回格式
     * @param inputArgsJson 输入参数的JSON数组 -paramCode 参数名 -paramDesc 参数描述 -paramType 列类型 -isRequired
     *            是否必填
     * @param outputArgsJson 返回参数的JSON数组 -paramCode 参数名 -paramDesc 参数描述 -paramType 列类型
     * @param jsonExamp JSON 示例
     * @param otherInfo 其他相关信息
     * @return
     * @author chenzpa 2016年4月12日
     */

    @SuppressWarnings("unchecked")
    @POST
    @Path("/editoutconfig")
    public ConfigerVo editOutConfig(@FormParam("outConfigId") String outConfigId,
            @FormParam("parentId") String parentId, @FormParam("serviceCode") String serviceCode,
            @FormParam("serviceName") String serviceName, @FormParam("requestExampleUrl") String requestExampleUrl,
            @FormParam("configRemark") String configRemark, @FormParam("requestMethod") String requestMethod,
            @FormParam("resultFormat") String resultFormat, @FormParam("inputArgsJson") String inputArgsJson,
            @FormParam("outputArgsJson") String outputArgsJson, @FormParam("jsonExamp") String jsonExamp,
            @FormParam("xmlExamp") String xmlExamp, @FormParam("otherInfo") String otherInfo,
            @FormParam("editMark") String editMark) {
        ConfigerVo vo = new ConfigerVo();
        String checkStr = "";
        checkStr += checkArgIsNull("parentId", parentId);
        checkStr += checkArgIsNull("configRemark", configRemark);
        checkStr += checkArgIsNull("serviceName", serviceName);
        checkStr += checkArgIsNull("serviceCode", serviceCode);
        // 如果参数为空
        if (checkStr.length() > 1) {
            vo.setResultCode(BaseVo.ERROR_CODE);
            vo.setResultInfo(checkStr);
            return vo;
        }

        ServeConfig conf = new ServeConfig();
        conf.setIsDeleted(0);
        conf.setParentId(Long.parseLong(parentId));
        conf.setServiceName(serviceName);
        conf.setServiceCode(serviceCode);
        conf.setRequestExampleUrl(requestExampleUrl);
        conf.setRequestMethod(requestMethod);
        conf.setOtherInfo(otherInfo);

        // 接口类型（3:空间数据，2::外部接口，1:配置接口）
        conf.setServiceType(2);
        conf.setConfigRemark(configRemark);
        conf.setResultFormat(resultFormat);
        // 是否是服务(1：是 0 是目录)
        conf.setIsService(1);

        List<ServeInputParam> inputArgs = (List<ServeInputParam>) JsonUtil.jsonToList(inputArgsJson,
                ServeInputParam.class);
        List<ServeOutputParam> outputArgs = (List<ServeOutputParam>) JsonUtil.jsonToList(outputArgsJson,
                ServeOutputParam.class);

        return configerService.editOutConfig(outConfigId, conf, inputArgs, outputArgs, jsonExamp, xmlExamp, editMark);

    }

    /**
     * 动态生成服务配置json示例
     *
     * @param 服务id
     * @param 访问地址示例(主要是解析配置参数)
     * @return
     * @author zhongdt 2016年4月14日
     * @throws Exception
     */
    @GET
    @Path("/inner/buildexample")
    public ServiceBaseVo buildJsonExample(@FormParam("configId") String configId,
            @FormParam("resultExampleUrl") String exampleUrl) throws Exception {

        ServeConfig config = this.configerService.queryConfiger(configId);
        if (config == null || config.getDataSourceId() == null) {
            return new ServiceBaseVo("-1", "配置错误,请检查");
        }
        DbContextHolder.setDbType(config.getDataSourceId() + "");
        return configerService.buildJson(config, exampleUrl);

    }

    /**
     * 服务管理模块，数据源列表
     */
    @GET
    @Path("/querydatasource")
    public DataSourceBaseVo dataSourceList(@BeanParam DataSourceManageVo sourceVo) {
        return dataSourceController.queryDataSourceList(sourceVo);
    }

    @GET
    @Path("/queryAllTables")
    public DataSourceBaseVo queryTableList(@FormParam("dataSource") String dataSourceId,
            @FormParam("tableName") String tableName) {
        return dbInfoController.queryTableList(dataSourceId, tableName);
    }

    @GET
    @Path("/queryTableColumn")
    public DataSourceBaseVo dataTableColumnList(@FormParam("dataSource") String dataSourceId,
            @FormParam("tableName") String tableName) {
        return dbInfoController.queryTableColumnList(dataSourceId, tableName);
    }

    @GET
    @Path("/querycatalogtree")
    public CatalogBaseVo cataLogTree(@BeanParam CataLogQueryVo vo) {
        return catalogController.queryCatalogTree(vo);
    }

    @POST
    @Path("/cancelpublish")
    public BaseVo cancelPublish(Long configId) {

        return configerService.cancelPublish(configId);

    }

    public static void main(String[] args) {
        JSONArray arraylArray = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("type", "JSON");
        obj.put("serviceId", 1);
        obj.put("example", "xxxx");

        JSONObject obj1 = new JSONObject();
        obj1.put("type", "JSON");
        obj1.put("serviceId", 1);
        obj1.put("example", "xxxx1");

        arraylArray.add(obj1);
        arraylArray.add(obj);
        System.out.println(arraylArray.toString());

    }

    @SuppressWarnings("unchecked")
    // hasConfigId 如果有ID 就是修改数据库中的记录，如果没有为NULL 就是新增一条记录
    private ConfigerVo editConfig(String hasConfigId, String parentId, String dataSourceId, String serviceName,
            String serviceCode, String serviceType, String configRemark, String requestMethod, String querySql,
            String resultFormat, String inputArgsJson, String sqlSegmentJson, String outputArgsJson,
            String datatableName, String navInputParamJson, String navOutputParamJson, String navDateRangeJson,
            String editMark) {
        ConfigerVo vo = new ConfigerVo();
        String checkStr = "";
        checkStr += checkArgIsNull("parentId", parentId);
        checkStr += checkArgIsNull("dataSourceId", dataSourceId);
        checkStr += checkArgIsNull("serviceName", serviceName);
        checkStr += checkArgIsNull("serviceCode", serviceCode);
        checkStr += checkArgIsNull("serviceType", serviceType);
        checkStr += checkArgIsNull("requestMethod", requestMethod);
        checkStr += checkArgIsNull("querySql", querySql);
        checkStr += checkArgIsNull("resultFormat", resultFormat);
        checkStr += checkArgIsNull("outputArgsJson", outputArgsJson);
        // 如果有空参数
        if (checkStr.length() > 1) {
            vo.setResultCode(BaseVo.ERROR_CODE);
            vo.setResultInfo(checkStr);
            return vo;
        }
        // 将JSON 字符串 列表转换为Java类
        List<ServeInputParam> inputArgs = (List<ServeInputParam>) JsonUtil.jsonToList(inputArgsJson,
                ServeInputParam.class);
        List<ServeSegment> segments = (List<ServeSegment>) JsonUtil.jsonToList(sqlSegmentJson, ServeSegment.class);
        List<ServeOutputParam> outputArgs = (List<ServeOutputParam>) JsonUtil.jsonToList(outputArgsJson,
                ServeOutputParam.class);
        List<ServeInputParamNav> inputArgsNav = (List<ServeInputParamNav>) JsonUtil.jsonToList(navInputParamJson,
                ServeInputParamNav.class);
        List<ServeDataRange> dateRanges = (List<ServeDataRange>) JsonUtil.jsonToList(navDateRangeJson,
                ServeDataRange.class);
        List<ServeOutputParamNav> outputArgsNav = (List<ServeOutputParamNav>) JsonUtil.jsonToList(navOutputParamJson,
                ServeOutputParamNav.class);
        ServeConfig conf = new ServeConfig();
        conf.setParentId(Long.parseLong(parentId));
        conf.setIsDeleted(0);
        conf.setDataSourceId(Long.parseLong(dataSourceId));
        conf.setServiceName(serviceName);
        conf.setServiceCode(serviceCode);
        // 接口类型（3:空间数据，2::外部接口，1:配置接口）
        conf.setServiceType(Integer.parseInt(serviceType));
        conf.setConfigRemark(configRemark);
        conf.setRequestMethod(requestMethod);
        conf.setDataTableName(datatableName);
        conf.setQuerySql(querySql);
        conf.setResultFormat(resultFormat);

        return configerService.editConfig(hasConfigId, conf, inputArgs, segments, outputArgs, inputArgsNav, dateRanges,
                outputArgsNav, editMark);

    }

    /**
     * 检查字段是否为空
     *
     * @param argName 参数名
     * @param argVal 参数值
     * @return
     * @author chenzpa 2016年5月9日
     */
    private String checkArgIsNull(String argName, String argVal) {
        if (StringUtils.isNullOrEmpty(argVal)) {
            return "参数的值不能为空：" + argName + "----\r\n";
        }
        return "";
    }

}
