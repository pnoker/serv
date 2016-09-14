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
package com.dc.city.dao.master.config;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.config.ServeConfigAuth;
import com.dc.city.domain.config.ServeConfigWhiteList;
import com.dc.city.domain.config.ServeDataRange;
import com.dc.city.domain.config.ServeInputParam;
import com.dc.city.domain.config.ServeInputParamNav;
import com.dc.city.domain.config.ServeOutputParam;
import com.dc.city.domain.config.ServeOutputParamNav;
import com.dc.city.domain.config.ServeSegment;

public interface ServeConfigMapper {
    int deleteByPrimaryKey(long id);

    int insert(ServeConfig record);

    int insertSelective(ServeConfig record);

    List<ServeConfig> selectBySelective(ServeConfig record);

    ServeConfig selectByPrimaryKey(long id);

    int updateByPrimaryKeySelective(ServeConfig record);

    int updateByPrimaryKey(ServeConfig record);

    /**
     * 添加输出参数列表
     *
     * @param outputArgs
     * @return
     * @author chenzpa 2016年3月10日
     */
    int createOutputParams(List<ServeOutputParam> outputArgs);

    /**
     * 添加输ru参数列表
     *
     * @param inputArgs
     * @return
     * @author chenzpa 2016年3月10日
     */
    int createInputParams(List<ServeInputParam> inputArgs);

    /**
     * 添加SQL 片段
     *
     * @param segments
     * @return
     * @author chenzpa 2016年3月10日
     */
    int createSegments(List<ServeSegment> segments);

    /**
     * 查询输出参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月11日
     */
    List<ServeOutputParam> queryOutputArgs(@Param("configerId") String configerId);

    /**
     * 查询输入参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月11日
     */
    List<ServeInputParam> queryInputArgs(@Param("configerId") String configerId);

    /**
     * 查询SQL 片段
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月11日
     */
    List<ServeSegment> querySegments(@Param("configerId") String configerId);

    /**
     * 向导输出参数
     *
     * @param outputArgs
     * @return
     * @author chenzpa 2016年3月15日
     */
    int createOutputParamsNav(List<ServeOutputParamNav> outputArgs);

    /**
     * 向导输入参数
     *
     * @param inputArgs
     * @return
     * @author chenzpa 2016年3月15日
     */
    int createInputParamsNav(List<ServeInputParamNav> inputArgs);

    /**
     * 向导时间区间参数
     *
     * @param dateRanges
     * @return
     * @author chenzpa 2016年3月15日
     */
    int createDateRangeArgs(List<ServeDataRange> dateRanges);

    /**
     * 查询向导输出参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月15日
     */
    List<ServeOutputParamNav> queryOutputArgsNav(@Param("configerId") String configerId);

    /**
     * 查询向导输入参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月15日
     */
    List<ServeInputParamNav> queryInputArgsNavs(@Param("configerId") String configerId);

    /**
     * 查询向导时间区间参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月15日
     */
    List<ServeDataRange> queryDateRangeArgs(@Param("configerId") String configerId);

    /**
     * 配置表中权限需要用到的字段 缓存所用
     *
     * @return
     * @author chenzpa 2016年3月16日
     */
    List<ServeConfigAuth> queryConfigbyAuth();

    /**
     * 查询一个服务的白名单
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月16日
     */
    List<ServeConfigWhiteList> queryWhiteList(@Param("configerId") String configerId);

    /**
     * 批量创建白名单
     *
     * @param list
     * @author zhongdt 2016年4月6日
     */
    int createWhiteList(List<ServeConfigWhiteList> list);

    int deleteWhites(@Param("serviceId") long serviceId);

    /**
     * 通过serverCode 取到 配置的ID
     *
     * @param serverCode
     * @return
     * @author chenzpa 2016年3月16日
     */
    List<Long> queryConfigIDbyCode(String serverCode);

    /**
     * 发布服务 时保存一些信息
     *
     * @param configerId
     * @param demoUrl
     * @param serverMark
     * @param verifyAccess
     * @param verifyView
     * @param verifyIp
     * @param otherInfo
     * @param serviceStatus
     * @author chenzpa 2016年3月17日
     */
    void updatePublishInfo(@Param("configerId") Long configerId, @Param("demoUrl") String demoUrl,
            @Param("publishRemark") String publishRemark, @Param("verifyAccess") Integer verifyAccess,
            @Param("verifyView") Integer verifyView, @Param("verifyIp") Integer verifyIp,
            @Param("otherInfo") String otherInfo, @Param("serviceStatus") Integer serviceStatus);

    /**
     * 删除配置主表
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    void removeConfigerById(String configId);

    /**
     * 删除旧的输出参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    void removeOutputParams(String configId);

    /**
     * 删除旧的输入参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    void removeInputParams(String configId);

    /**
     * 删除旧的SQL片段
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    void removeSegments(String configId);

    /**
     * 删除旧的向导输出参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    void removeOutputParamsNav(String configId);

    /**
     * 删除旧的向导输入参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    void removeInputParamsNav(String configId);

    /**
     * 删除旧的向导区间参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    void removeDateRangeArgs(String configId);

    /**
     * 记录日志 修改记录表
     *
     * @param configerId
     * @param operationId
     * @param editMark
     * @author chenzpa 2016年3月18日
     */
    void createEditLog(@Param("configerId") long configerId, @Param("operationName") String operationName,
            @Param("editMark") String editMark);

    /**
     * 插入JSON 示例
     *
     * @param configerId
     * @param jsonExamp
     * @author chenzpa 2016年4月13日
     * @param type
     */
    void createJSONexamp(@Param("type") String type, @Param("configerId") long configerId,
            @Param("jsonExamp") String jsonExamp);

    /**
     * 修改 JSON 示例
     *
     * @param configerId
     * @param jsonExamp
     * @author chenzpa 2016年4月13日
     */
    void modifyJSONexamp(@Param("type") String type, @Param("configerId") long configerId,
            @Param("jsonExamp") String jsonExamp);

    /**
     * 发布GIS服务
     *
     * @param configerId
     * @param serviceStatus
     * @author chenzpa 2016年4月14日
     */
    void modifyGisPublishInfo(@Param("id") Long configerId, @Param("serviceStatus") int serviceStatus);
    
    /**
     * 服务审核
     *
     * @param configerId
     * @param serviceStatus
     * @author zuoyue 2016年8月16日
     */
    void modifyServeReviewState(@Param("id") Long configerId, @Param("serviceStatus") int serviceStatus);

}