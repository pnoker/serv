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
package com.dc.city.service.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dc.city.common.exception.BusinessException;
import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.common.utils.ConfigUtils;
import com.dc.city.common.utils.CookieUtils;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.common.vo.FuzzyQueryVo;
import com.dc.city.dao.master.config.ServeConfigMapper;
import com.dc.city.dao.master.config.ServeConfigQueryMapper;
import com.dc.city.dao.master.config.ServeExtendSpaceMapper;
import com.dc.city.dao.master.config.ServeGisDictionaryMapper;
import com.dc.city.dao.master.config.ServeModifyLogMapper;
import com.dc.city.dao.master.config.ServeResultExampleMapper;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.config.ServeConfigWhiteList;
import com.dc.city.domain.config.ServeDataRange;
import com.dc.city.domain.config.ServeExtendSpace;
import com.dc.city.domain.config.ServeGisDictionary;
import com.dc.city.domain.config.ServeInputParam;
import com.dc.city.domain.config.ServeInputParamNav;
import com.dc.city.domain.config.ServeModifyLog;
import com.dc.city.domain.config.ServeOutputParam;
import com.dc.city.domain.config.ServeOutputParamNav;
import com.dc.city.domain.config.ServeResultExample;
import com.dc.city.domain.config.ServeSegment;
import com.dc.city.domain.securitymanage.user.ServeUser;
import com.dc.city.listener.config.InitConfigerListener;
import com.dc.city.pojo.serve.config.ServeAuthUserPo;
import com.dc.city.pojo.serve.config.ServeConfigQueryPo;
import com.dc.city.pojo.serve.config.ServeCount;
import com.dc.city.service.cache.PubCacheService;
import com.dc.city.service.database.ServeProcessService;
import com.dc.city.service.securitymanage.UserManageService;
import com.dc.city.vo.BaseVo;
import com.dc.city.vo.PageVo;
import com.dc.city.vo.config.ConfigerVo;
import com.dc.city.vo.config.ServeAuthUserVo;
import com.dc.city.vo.config.ServeConfigQueryVo;
import com.dc.city.vo.config.ServeConfigVo;
import com.dc.city.vo.database.ServiceBaseVo;
import com.dc.city.vo.securitymanage.user.ServeUserAuthorityVo;

/**
 * 服务配置service
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年3月10日 下午4:51:42
 *          Copyright 2016 by DigitalChina
 */
@Service
public class ConfigerService {

    @Resource
    ServeConfigMapper serveConfigMapper;

    @Resource
    ServeExtendSpaceMapper serveExtendSpaceMapper;

    @Resource
    ServeConfigQueryMapper queryMapper;

    @Resource
    ServeResultExampleMapper resultMapper;

    @Resource
    UserManageService userManageService;

    @Resource
    ServeGisDictionaryMapper serveGisDictionaryMapper;

    @Resource
    ServeModifyLogMapper serveModifyLogMapper;

    @Resource
    ServeProcessService processService;
    @Resource
    PubCacheService pubCacheService;
    private static Log LOG = LogFactory.getLog(ConfigerService.class);

    /**
     * 添加一条服务配置 主表的一条记录
     *
     * @param conf
     * @return
     * @author chenzpa 2016年3月10日
     */
    public long createConfiger(ServeConfig conf) {
        String sql = conf.getQuerySql();
        // SQL 长度太长就存为clob
        if (sql != null && sql.length() > 3800) {
            conf.setQuerySqlClob(sql);
            conf.setQuerySql(null);
        }
        serveConfigMapper.insertSelective(conf);
        return conf.getId();
    }

    /**
     * 添加输出参数列表
     *
     * @param outputArgs
     * @return
     * @author chenzpa 2016年3月10日
     */
    public int createOutputParams(List<ServeOutputParam> outputArgs) {
        if (CollectionUtils.isEmpty(outputArgs)) {
            return 0;
        }
        return serveConfigMapper.createOutputParams(outputArgs);
    }

    /**
     * 添加输入参数列表
     *
     * @param inputArgs
     * @return
     * @author chenzpa 2016年3月10日
     */
    public int createInputParams(List<ServeInputParam> inputArgs) {
        if (CollectionUtils.isEmpty(inputArgs)) {
            return 0;
        }
        return serveConfigMapper.createInputParams(inputArgs);
    }

    /**
     * 添加SQL 片段
     *
     * @param segments
     * @return
     * @author chenzpa 2016年3月10日
     */
    public int createSegments(List<ServeSegment> segments) {
        if (CollectionUtils.isEmpty(segments)) {
            return 0;
        }
        return serveConfigMapper.createSegments(segments);
    }

    /**
     * 通过ID查询服务配置的基本信息
     *
     * @param configerId 服务ID
     * @return
     * @author chenzpa 2016年3月11日
     */
    public ServeConfig queryBaseConfiger(String configerId) {

        ServeConfig config = serveConfigMapper.selectByPrimaryKey(Long.parseLong(configerId));
        return config;
    }

    /**
     * 通过ID查询服务配置的全部信息
     *
     * @param configerId 服务ID
     * @return
     * @author chenzpa 2016年3月11日
     */
    public ServeConfig queryConfiger(String configerId) {

        ServeConfig config = serveConfigMapper.selectByPrimaryKey(Long.parseLong(configerId));
        if (config == null) {
            return null;
        }
        // 服务接口的实际输入输出参数
        config.setOutputArgs(queryOutputArgs(configerId));
        config.setInputArgs(queryInputArgs(configerId));
        config.setSegments(querySegments(configerId));
        // 白名单
        config.setWhiteList(queryWhiteList(configerId));

        // 向导参数
        config.setOutputArgsNav(queryOutputArgsNav(configerId));
        config.setInputArgsNavs(queryInputArgsNavs(configerId));
        config.setDateRangeArgs(queryDateRangeArgs(configerId));
        // JSON和XML 示例
        config.setExampleList(queryExampleList(configerId));
        return config;
    }

    /**
     * 查询输出参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月11日
     */
    public List<ServeOutputParam> queryOutputArgs(String configerId) {

        return serveConfigMapper.queryOutputArgs(configerId);
    }

    /**
     * 查询输入参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月11日
     */
    public List<ServeInputParam> queryInputArgs(String configerId) {

        return serveConfigMapper.queryInputArgs(configerId);
    }

    /**
     * 查询SQL 片段
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月11日
     */
    public List<ServeSegment> querySegments(String configerId) {

        return serveConfigMapper.querySegments(configerId);
    }

    /**
     * 向导输出参数
     *
     * @param outputArgs
     * @return
     * @author chenzpa 2016年3月15日
     */
    public int createOutputParamsNav(List<ServeOutputParamNav> outputArgs) {
        if (CollectionUtils.isEmpty(outputArgs)) {
            return 0;
        }
        return serveConfigMapper.createOutputParamsNav(outputArgs);
    }

    /**
     * 向导输入参数
     *
     * @param inputArgs
     * @return
     * @author chenzpa 2016年3月15日
     */
    public int createInputParamsNav(List<ServeInputParamNav> inputArgs) {
        if (CollectionUtils.isEmpty(inputArgs)) {
            return 0;
        }
        return serveConfigMapper.createInputParamsNav(inputArgs);
    }

    /**
     * 向导时间区间参数
     *
     * @param dateRanges
     * @return
     * @author chenzpa 2016年3月15日
     */
    public int createDateRangeArgs(List<ServeDataRange> dateRanges) {
        if (CollectionUtils.isEmpty(dateRanges)) {
            return 0;
        }
        return serveConfigMapper.createDateRangeArgs(dateRanges);
    }

    /**
     * 查询向导输出参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月15日
     */
    public List<ServeOutputParamNav> queryOutputArgsNav(String configerId) {

        return serveConfigMapper.queryOutputArgsNav(configerId);
    }

    /**
     * 查询向导输入参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月15日
     */
    public List<ServeInputParamNav> queryInputArgsNavs(String configerId) {

        return serveConfigMapper.queryInputArgsNavs(configerId);
    }

    /**
     * 查询向导时间区间参数
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月15日
     */
    public List<ServeDataRange> queryDateRangeArgs(String configerId) {

        return serveConfigMapper.queryDateRangeArgs(configerId);
    }

    /**
     * 查询一个服务的白名单
     *
     * @param configerId
     * @return
     * @author chenzpa 2016年3月16日
     */
    public List<ServeConfigWhiteList> queryWhiteList(String configerId) {

        return serveConfigMapper.queryWhiteList(configerId);
    }

    /**
     * 查询当前服务的返回示例
     *
     * @param configerId
     * @return
     * @author zhongdt 2016年4月8日
     */
    public List<ServeResultExample> queryExampleList(String configerId) {
        return resultMapper.queryByServiceId(Long.valueOf(configerId));
    }

    /**
     * 从缓存里面去一个配置
     *
     * @param serverCode
     * @return
     * @author chenzpa 2016年3月16日
     */
    public ServeConfig getServerConfigFromCache(String serverCode) {
        ServeConfig conf = (ServeConfig) RedisUtil.getObject(InitConfigerListener.CONFIG_KEY_EXEC + serverCode);
        // 缓存为空的时候就去数据库里面取
        if (conf == null) {
            List<Long> configIds = serveConfigMapper.queryConfigIDbyCode(serverCode);
            if (configIds == null || configIds.isEmpty()) {
                return null;
            }
            String confId = configIds.get(0).toString();
            ServeConfig config = queryConfiger(confId);

            return config;
        }
        return conf;
    }

    /**
     * 发布服务 时保存一些信息
     *
     * @param configerId
     * @param demoUrl
     * @param serverMark
     * @param jsonExample
     *            [{\"resultType\": "json",\"resultExample\": "XSSSSSSSSSSS","serviceId":"1
     *            "},{\"resultType\": "XML
     *            ",\"resultExample\": "XMLSSSSSSS","serviceId":"2"}]
     * @param verifyAccess
     * @param verifyView
     * @param verifyIp
     * @param otherInfo
     * @param serviceStatus
     * @param viewUsers "1,2,3,4"
     * @param accessUsers "1,2,3"
     * @param ipList "192.168.1.1,192.168.4.1,127.0.0.1"
     * @author chenzpa 2016年3月17日
     * @param editRemark 发布记录
     */
    public BaseVo modifyPublishInfo(Long configerId, String demoUrl, String publishRemark, String jsonExampleStr,
            String xmlExampleStr, Integer verifyAccess, Integer verifyView, Integer verifyIp, String otherInfo,
            Integer serviceStatus, String viewUsers, String accessUsers, String ipList, String editRemark) {
        BaseVo vo = new BaseVo();
        ServeConfig config = queryConfiger(configerId.toString());
        if (config == null) {
            vo.setResultCode("-1");
            vo.setResultInfo("服务不存在，发布失败");
        }
        serviceStatus = ("false".equalsIgnoreCase(ConfigUtils.getString("serve.isOpenReview", "")) && 4 == serviceStatus) ? 3
                : serviceStatus;
        // 更新主表基本信息
        serveConfigMapper.updatePublishInfo(configerId, demoUrl, publishRemark, verifyAccess, verifyView, verifyIp,
                otherInfo, serviceStatus);
        /*************************** 更新json示例 **********************************/
        resultMapper.deleteByServiceId(configerId);

        if (!StringUtils.isNullOrEmpty(jsonExampleStr)) {
            ServeResultExample jsonExample = new ServeResultExample();
            jsonExample.setResultType("json");
            jsonExample.setServiceId(configerId);
            jsonExample.setResultExample(jsonExampleStr);
            resultMapper.createResultExample(jsonExample);
        }

        if (!StringUtils.isNullOrEmpty(xmlExampleStr)) {
            ServeResultExample xmlExample = new ServeResultExample();
            xmlExample.setResultType("xml");
            xmlExample.setServiceId(configerId);
            xmlExample.setResultExample(xmlExampleStr);
            resultMapper.createResultExample(xmlExample);
        }

        /*************************** 生成白名单ip ***********************************/
        serveConfigMapper.deleteWhites(configerId);
        if (1 == verifyIp) {
            List<ServeConfigWhiteList> whites = new ArrayList<ServeConfigWhiteList>();
            for (String ipAddress : ipList.split(",")) {
                if (StringUtils.isNullOrEmpty(ipAddress)) {
                    continue;
                }
                ServeConfigWhiteList white = new ServeConfigWhiteList();
                white.setServiceId(configerId);
                white.setIpAddress(ipAddress);
                whites.add(white);
            }
            serveConfigMapper.createWhiteList(whites);
        }

        /************************************** 生成权限数据开始 ***********************************************************/
        // 既不用验证view和access，则调用权限接口生成权限记录
        if (verifyView != 1) {
            viewUsers = "";
        }
        if (verifyAccess != 1) {
            accessUsers = "";
        }

        // 有数据的时候，才调用赋权接口
        if (!StringUtils.isNullOrEmpty(viewUsers) || !StringUtils.isNullOrEmpty(accessUsers)) {
            List<Map<String, Object>> authList = buildAuthObject(configerId, viewUsers, accessUsers);
            String userIds = "";
            List<ServeUserAuthorityVo> list = new ArrayList<ServeUserAuthorityVo>();
            for (Map<String, Object> map : authList) {
                Long userId = (Long) map.get("userId");
                userIds += userId + ",";
                ServeUserAuthorityVo avo = new ServeUserAuthorityVo();
                avo.setUserId(userId);
                avo.setServiceId(configerId);
                avo.setAccessPermission((int) map.get("accessPermission"));
                avo.setViewPermission((int) map.get("viewPermission"));
                list.add(avo);
            }
            // 去除最后一个","
            if (userIds.endsWith(",")) {
                userIds = userIds.substring(0, userIds.length() - 1);
            }

            try {
                if (!StringUtils.isNullOrEmpty(userIds) && list.size() > 0) {
                    userManageService.createUserAuthorizationByServiceId(userIds, configerId, list);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (config.getServiceType() == 1) {
            if (serviceStatus == 3) {
                // 重新查一次，然后刷新到缓存
                config = queryConfiger(configerId + "");
                // 刷新缓存
                RedisUtil.setObject(InitConfigerListener.CONFIG_KEY_EXEC + config.getServiceCode(), config,
                        Integer.MAX_VALUE);

                pubCacheService.publishServeConfig(PubCacheService.OPERATE_MODIFY, config.getServiceCode());

            } else {
                // 删除服务时刷新缓存
                pubCacheService.publishServeConfig(PubCacheService.OPERATE_REMOVE, config.getServiceCode());
            }
        }

        // 写更新日志，
        createEditLog(configerId, CookieUtils.acquireUserNameFromRequest(), editRemark);
        return vo;

    }

    /**
     * 添加GIS服务配置
     *
     * @param serveExt
     * @return
     * @author chenzpa 2016年3月17日
     */
    public int createGISConfig(ServeExtendSpace serveExt) {

        return serveExtendSpaceMapper.insertSelective(serveExt);
    }

    /**
     * 删除配置主表
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    public void removeConfigerById(String configId) {
        serveConfigMapper.removeConfigerById(configId);
    }

    /**
     * 删除旧的输出参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    public void removeOutputParams(String configId) {
        serveConfigMapper.removeOutputParams(configId);
    }

    /**
     * 删除旧的输入参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    public void removeInputParams(String configId) {
        serveConfigMapper.removeInputParams(configId);
    }

    /**
     * 删除旧的SQL片段
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    public void removeSegments(String configId) {
        serveConfigMapper.removeSegments(configId);
    }

    /**
     * 删除旧的向导输出参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    public void removeOutputParamsNav(String configId) {
        serveConfigMapper.removeOutputParamsNav(configId);
    }

    /**
     * 删除旧的向导输入参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    public void removeInputParamsNav(String configId) {
        serveConfigMapper.removeInputParamsNav(configId);
    }

    /**
     * 删除旧的向导区间参数
     *
     * @param configId
     * @author chenzpa 2016年3月18日
     */
    public void removeDateRangeArgs(String configId) {
        serveConfigMapper.removeDateRangeArgs(configId);
    }

    /**
     * 修改一条配置记录
     *
     * @param conf
     * @author chenzpa 2016年3月18日
     */
    public void modifyConfiger(ServeConfig conf) {
        String sql = conf.getQuerySql();
        // SQL 长度太长就存为clob
        if (sql != null && sql.length() > 3800) {
            conf.setQuerySqlClob(sql);
            conf.setQuerySql(null);
        }

        serveConfigMapper.updateByPrimaryKeySelective(conf);

    }

    /**
     * 记录日志 修改记录表
     *
     * @param configerId
     * @param operationId
     * @param editMark
     * @author chenzpa 2016年3月18日
     */
    public void createEditLog(long configerId, String operationName, String editMark) {
        serveConfigMapper.createEditLog(configerId, operationName, editMark);

    }

    /**
     * 批量查询 不支持分页
     *
     * @param record
     * @return
     * @author zhongdt 2016年3月28日
     */
    public List<ServeConfig> selectBySelective(ServeConfig record) {
        return this.serveConfigMapper.selectBySelective(record);
    }

    /**
     * 服务列表 分页查询
     *
     * @param po 封装条件 及分页参数
     * @return
     * @author zhongdt 2016年3月28日
     */
    public ServeConfigVo queryPage(ServeConfigQueryVo queryVo) {
        ServeConfigVo vo = new ServeConfigVo();

        ServeConfigQueryPo queryPo = new ServeConfigQueryPo();
        BeanUtils.copyProperties(queryVo, queryPo);

        long totalCount = this.queryMapper.getTotalCount(queryPo);
        if (totalCount == 0) {
            vo.setDatas(new ArrayList<ServeConfigQueryPo>());
            vo.setTotalCount("0");
            return vo;
        }

        // 每页记录数
        int pageSize = queryVo.getLength();
        pageSize = (pageSize == 0) ? PageVo.DEFAULT_PAGE_SIZE : pageSize;
        // 当前页码
        int start = queryVo.getStart();
        start = (start <= 0) ? PageVo.DEFAULT_CURRENT_PAGE : ++start;
        // 起始行数
        queryPo.setBeginRowNum(start);
        // 截止行数
        queryPo.setEndRowNum(start + pageSize);

        List<ServeConfigQueryPo> datas = this.queryMapper.queryPage(queryPo);
        // chenzpa 前端新需求 需要在列表的详情里面增加输入输出参数
        for (ServeConfig serv : datas) {
            // 空间GIS服务 独有的信息
            if (serv.getServiceType() == 3) {
                serv.setServeExtendSpace(serveExtendSpaceMapper.selectByPrimaryKey(serv.getId()));
                continue;
            }
            serv.setOutputArgs(queryOutputArgs(serv.getId() + ""));
            serv.setInputArgs(queryInputArgs(serv.getId() + ""));

        }
        vo.setTotalCount(totalCount + "");
        vo.setDatas(datas);
        return vo;
    }

    /**
     * 模糊查询
     *
     * @param query 查询条件
     * @param matchNum 匹配条数
     * @param tableName 表名字
     * @param propertys 属性名字
     * @return
     * @author chenzpa 2016年7月29日
     */
    public FuzzyQueryVo fuzzyQuery(String query, String matchNum, String tableName, String[] propertys) {
        List<String> tablePropertys = new ArrayList<String>();
        for (String p : propertys) {
            if (!com.dc.city.common.utils.StringUtils.isNullOrEmpty(p)) {
                tablePropertys.add(p);
            }
        }
        FuzzyQueryVo vo = new FuzzyQueryVo();
        if (propertys.length == 0) {
            vo.setDatas(new ArrayList<Map<String, String>>());
        } else {
            if (matchNum == "0" || matchNum == "") {
                matchNum = null;
            }
            List<String> resultsList = queryMapper.fuzzyQuery(query, matchNum, tableName, tablePropertys);
            List<Map<String, String>> datasList = new ArrayList<Map<String, String>>();
            int id = 1;
            for (String q : resultsList) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", "" + (id++));
                map.put("matchName", q);
                datasList.add(map);
            }
            vo.setDatas(datasList);
        }
        return vo;
    }

    /**
     * 在服务发布页面，获取当前接口的所有授权用户信息列表
     *
     * @param serviceId
     * @return
     * @author zhongdt 2016年4月8日
     */
    public ServeAuthUserVo queryConfigAuthUser(long serviceId) {
        List<Map<String, Object>> auths = this.queryMapper.queryAuthUserByConfig(serviceId);
        List<ServeAuthUserPo> access = new ArrayList<ServeAuthUserPo>();
        List<ServeAuthUserPo> view = new ArrayList<ServeAuthUserPo>();
        for (Map<String, Object> auth : auths) {
            if (((BigDecimal) auth.get("ACCESSPER")).intValue() == 1) {
                ServeAuthUserPo accessPo = new ServeAuthUserPo();
                accessPo.setUserId(((BigDecimal) auth.get("USERID")).longValue());
                accessPo.setUserName((String) auth.get("USERNAME"));
                access.add(accessPo);
            }
            if (((BigDecimal) auth.get("VIEWPER")).intValue() == 1) {
                ServeAuthUserPo viewPo = new ServeAuthUserPo();
                viewPo.setUserId(((BigDecimal) auth.get("USERID")).longValue());
                viewPo.setUserName((String) auth.get("USERNAME"));
                view.add(viewPo);
            }
        }
        ServeAuthUserVo vo = new ServeAuthUserVo();
        vo.setAccess(access);
        vo.setViews(view);
        return vo;
    }

    /**
     * 根据可访问用户，可查看用户构造权限访问list
     *
     * @param serviceId
     * @param viewUsers
     * @param accessUsers
     * @return
     * @author zhongdt 2016年4月6日
     */
    private List<Map<String, Object>> buildAuthObject(Long serviceId, String viewUsers, String accessUsers) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String[] viewArr = viewUsers.split(",");
        String[] accessArr = accessUsers.split(",");
        List<String> viewIds = new ArrayList<String>();
        List<String> accessIds = new ArrayList<String>();
        for (String view : viewArr) {
            if (!StringUtils.isNullOrEmpty(view)) {
                viewIds.add(view);
            }
        }

        for (String access : accessArr) {
            if (!StringUtils.isNullOrEmpty(access)) {
                accessIds.add(access);
            }
        }

        List<String> userIds = new ArrayList<String>();
        // 合并两个list到一个list中
        userIds.addAll(viewIds);
        userIds.addAll(accessIds);
        // 去掉重复
        removeDuplicate(userIds);
        // 循环读取用户，并构造权限信息
        for (String userId : userIds) {
            if (StringUtils.isNullOrEmpty(userId)) {
                continue;
            }
            Map<String, Object> auth = new HashMap<String, Object>();
            ServeUser userBean = userManageService.findByPk(Long.valueOf(userId));
            if (userBean == null) {
                continue;
            }

            auth.put("userId", userBean.getId());
            auth.put("serviceId", serviceId);
            auth.put("accessPermission", accessIds.contains(userId) ? 1 : 0);
            auth.put("viewPermission", viewIds.contains(userId) ? 1 : 0);

            result.add(auth);
        }
        return result;
    }

    // 冲掉重复
    private void removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<String>(list);
        list.clear();
        list.addAll(h);
    }

    /**
     * 添加GIS服务的数据字典
     *
     * @param inputArgs
     * @author chenzpa 2016年4月11日
     */
    public void createGisDictionaryParams(List<ServeGisDictionary> inputArgs) {
        for (ServeGisDictionary serveGisDictionary : inputArgs) {
            serveGisDictionaryMapper.insertSelective(serveGisDictionary);
        }
    }

    /**
     * 将数据字典删掉
     *
     * @param configerId 配置ID
     * @author chenzpa 2016年4月12日
     */
    public void removeGisDictionaryParams(long configerId) {
        serveGisDictionaryMapper.removeGisDictionaryParams(configerId);

    }

    /**
     * 修改GIS服务配置
     *
     * @param serveExt
     * @return
     * @author chenzpa 2016年4月12日
     */
    public int modifyGISConfig(ServeExtendSpace serveExt) {

        return serveExtendSpaceMapper.updateByPrimaryKeySelective(serveExt);
    }

    /**
     * 删除一条GIS服务
     *
     * @param gisConfigId
     * @author chenzpa 2016年4月12日
     */
    public void deleteGISConfig(long gisConfigId) {
        // 删除 GIS 字典
        serveGisDictionaryMapper.removeGisDictionaryParams(gisConfigId);
        // 删除GIS 独有的服务
        serveExtendSpaceMapper.deleteByPrimaryKey(gisConfigId);
        // 标记服务主表为删除
        serveConfigMapper.removeConfigerById(gisConfigId + "");
    }

    /**
     * 查询GIS服务 独有的属性
     *
     * @param gisConfigId
     * @return
     * @author chenzpa 2016年4月12日
     */
    public ConfigerVo queryGisConfiger(String gisConfigId) {
        ConfigerVo vo = new ConfigerVo();
        ServeConfig config = queryConfiger(gisConfigId);
        vo.setConfig(config);
        ServeExtendSpace gisConfig = serveExtendSpaceMapper.selectByPrimaryKey(Long.parseLong(gisConfigId));
        vo.setServeExtendSpace(gisConfig);
        if (gisConfig != null) {
            // GIS服务的数据字典
            gisConfig.setGisDictionaries(queryGisConfigerDictionary(gisConfigId));
        }
        // 服务的更新记录列表
        vo.setDatas(queryUpdateRecode(gisConfigId));
        return vo;
    }

    /**
     * 查询GIS服务 中的字典列表信息
     *
     * @param gisConfigId
     * @return
     * @author chenzpa 2016年4月12日
     */
    public List<ServeGisDictionary> queryGisConfigerDictionary(String gisConfigId) {

        return serveGisDictionaryMapper.queryGisConfigerDictionary(Long.parseLong(gisConfigId));
    }

    /**
     * 插入JSON 示例
     *
     * @param configerId
     * @param jsonExamp
     * @author chenzpa 2016年4月13日
     * @param type 表示JSON还是XML 等
     */
    public void createJSONexamp(String type, long configerId, String jsonExamp) {
        serveConfigMapper.createJSONexamp(type, configerId, jsonExamp);
    }

    /**
     * 修改 JSON 示例
     *
     * @param configerId
     * @param jsonExamp
     * @author chenzpa 2016年4月13日
     * @param type 表示JSON还是XML 等
     */
    public void modifyJSONexamp(String type, long configerId, String jsonExamp) {
        serveConfigMapper.modifyJSONexamp(type, configerId, jsonExamp);
    }

    /**
     * 更新记录 查询
     *
     * @param configId
     * @author chenzpa 2016年4月14日
     * @return
     */
    public List<ServeModifyLog> queryUpdateRecode(String configId) {
        return serveModifyLogMapper.queryUpdateRecode(Long.parseLong(configId));
    }

    /**
     * 发布GIS服务
     *
     * @param configerId
     * @param serviceStatus
     * @author chenzpa 2016年4月14日
     */
    public void modifyGisPublishInfo(Long configerId, int serviceStatus) {
        serveConfigMapper.modifyGisPublishInfo(configerId, serviceStatus);
    }

    /**
     * 生成内部服务json示例
     *
     * @param configId 服务id
     * @param exampleUrl 示例服务请求地址(只需要解析参数)
     * @return
     * @author zhongdt 2016年4月14日
     * @throws BusinessException
     * @throws Exception
     */
    public ServiceBaseVo buildJson(ServeConfig config, String exampleUrl) throws BusinessException {

        if (config == null) {
            return new ServiceBaseVo();
        }
        if (StringUtils.isNullOrEmpty(exampleUrl)) {
            return new ServiceBaseVo();
        }

        Map<String, String> params = getQueryParams(exampleUrl);
        return processService.excuteService(config, params);

    }

    /**
     * 根据url截取传递的参数
     *
     * @param str
     * @return
     * @author zhongdt 2016年4月14日
     */
    private Map<String, String> getQueryParams(String str) {
        int beginIndex = str.indexOf("?");
        Map<String, String> params = new HashMap<String, String>();
        if (beginIndex < 0) {
            return params;
        }
        String paramStrs = str.substring(beginIndex + 1);
        String[] arrs = paramStrs.split("&");
        for (String paramStr : arrs) {
            if (StringUtils.isNullOrEmpty(paramStr)) {
                continue;
            }
            String[] arr = paramStr.split("=");

            // 只有参数，没有值
            if (arr.length == 1) {
                params.put(arr[0], "");
                continue;
            }
            // 为了统一强制转换为小写

            params.put(arr[0].toLowerCase(), arr[1]);
        }
        return params;
    }

    /**
     * 查询外部服务
     *
     * @param outConfigId
     * @return
     * @author chenzpa 2016年5月26日
     */
    public ConfigerVo queryOutConfig(String outConfigId) {
        ConfigerVo vo = new ConfigerVo();
        ServeConfig config = queryConfiger(outConfigId);
        config.setOutputArgs(queryOutputArgs(outConfigId));
        config.setInputArgs(queryInputArgs(outConfigId));
        config.setExampleList(queryExampleList(outConfigId));
        vo.setConfig(config);
        return vo;
    }

    /**
     * 取消发布
     *
     * @param configId 服务ID
     * @return
     * @author chenzpa 2016年5月26日
     */
    public BaseVo cancelPublish(Long configId) {
        ServeConfig conf = this.queryConfiger(configId.toString());
        BaseVo vo = new BaseVo();
        if (conf == null) {
            vo.setResultCode("-1");
            vo.setResultInfo("取消服务发布失败");
        }

        // 更新时间及状态
        conf.setServiceStatus(2);
        conf.setUpdatetime(new Date());
        this.modifyConfiger(conf);

        if (conf.getServiceType() == 1) {

            pubCacheService.publishServeConfig(PubCacheService.OPERATE_REMOVE, conf.getServiceCode());

        }
        // 自动填写操作记录
        String userName = CookieUtils.acquireUserNameFromRequest();
        String remark = userName + "进行服务撤销发布操作";
        createEditLog(configId, userName, remark);

        return vo;
    }

    /**
     * 添加或修改GIS 服务
     *
     * @return
     * @author chenzpa 2016年5月26日
     * @param serveExt GIS 服务的扩展参数
     * @param conf 服务的基本信息
     * @param inputArgs GIS服务的数据字典 输入列表
     * @param gisConfigId 服务ID 为空 就表示新增
     * @param editMark 修改标记
     */
    public ConfigerVo editGisConfiguer(String gisConfigId, List<ServeGisDictionary> inputArgs, ServeConfig conf,
            ServeExtendSpace serveExt, String editMark) {
        ConfigerVo vo = new ConfigerVo();
        boolean hasConfigId = !StringUtils.isNullOrEmpty(gisConfigId);
        long configerId = 0;

        // 传入服务ID 就表示修改 否则表示添加
        if (hasConfigId) {
            // 修改服务的时候就要把原来的状态赋值
            ServeConfig oldConfig = queryBaseConfiger(gisConfigId);
            conf.setServiceStatus(oldConfig.getServiceStatus());
            configerId = Long.parseLong(gisConfigId);
            conf.setId(configerId);
            // 修改基础配置
            modifyConfiger(conf);
            // 将数据字典删掉 后面重新添加
            removeGisDictionaryParams(configerId);
        }
        // 服务ID为空表示新增
        else {
            // 新增 服务状态为0
            conf.setServiceStatus(0);
            conf.setCreateUser(CookieUtils.acquireUserNameFromRequest());
            // 添加，服务代码必须唯一
            try {
                configerId = createConfiger(conf);
            } catch (DuplicateKeyException e) {
                if (e.getMessage().indexOf("UK_SERVICE_CODE") >= 0) {
                    vo.setResultCode(BaseVo.ERROR_CODE);
                    vo.setResultInfo("服务代码不唯一！");
                    return vo;
                } else {
                    return new ConfigerVo(BaseVo.ERROR_CODE, "添加配置信息出错" + e);
                }
            }
        }
        // 数据字典 的服务ID 设置进去
        for (ServeGisDictionary ip : inputArgs) {
            ip.setConfigId(configerId);
        }
        // 添加数据字典
        createGisDictionaryParams(inputArgs);

        serveExt.setId(configerId);
        // 传入服务ID 就表示修改 否则表示添加
        if (hasConfigId) {
            // 修改GIS服务独有的信息
            int k = modifyGISConfig(serveExt);
            LOG.debug("修改GIS服务配置：" + k);
        } else {
            // 添加GIS服务独有的信息
            int k = createGISConfig(serveExt);
            LOG.debug("添加添加GIS服务配置：" + k);
        }
        // 记录日志 修改记录表
        createEditLog(configerId, CookieUtils.acquireUserNameFromRequest(),
                StringUtils.isNullOrEmpty(editMark) ? (hasConfigId ? "修改一下GIS服务" : "新增的GIS服务哦") : editMark);

        vo.setConfigId(configerId);
        return vo;
    }

    /**
     * 删除服务的 除了基本配置以外的 其他参数
     *
     * @param configId 服务ID
     * @author chenzpa 2016年5月26日
     */
    public void removeConfigerOtherParams(String configId) {
        // 删除旧的输出参数
        removeOutputParams(configId);
        // 删除旧的输入参数
        removeInputParams(configId);
        // 删除旧的SQL片段
        removeSegments(configId);
        // 删除旧的向导输出参数
        removeOutputParamsNav(configId);
        // 删除旧的向导输入参数
        removeInputParamsNav(configId);
        // 删除旧的向导区间参数
        removeDateRangeArgs(configId);
    }

    /**
     * 增加或修改内部服务
     *
     * @return
     * @author chenzpa 2016年5月26日
     * @param outputArgsNav 向导输出参数
     * @param dateRanges 向导时间区间
     * @param inputArgsNav 向导输入参数
     * @param outputArgs 实际输出参数
     * @param segments 实际SQL 片段
     * @param inputArgs 实际输入参数
     * @param conf 服务配置的基本信息
     * @param hasConfigId 服务ID 新增服务时为空
     * @param editMark 服务添加或修改的标记
     */
    public ConfigerVo editConfig(String hasConfigId, ServeConfig conf, List<ServeInputParam> inputArgs,
            List<ServeSegment> segments, List<ServeOutputParam> outputArgs, List<ServeInputParamNav> inputArgsNav,
            List<ServeDataRange> dateRanges, List<ServeOutputParamNav> outputArgsNav, String editMark) {
        ConfigerVo vo = new ConfigerVo();
        // 没值表示新增， 有值表示修改
        if (StringUtils.isNullOrEmpty(hasConfigId)) {
            // 服务状态，新增（现在修改为新增服务后状态为未发布 代码2）
            conf.setServiceStatus(2);
            conf.setCreateUser(CookieUtils.acquireUserNameFromRequest());
        } else {
            // 修改之前 删掉原来的输入输出等其他参数信息
            removeConfigerOtherParams(hasConfigId);
            // 修改服务的时候就要把原来的状态赋值
            ServeConfig oldConfig = queryBaseConfiger(hasConfigId);
            conf.setServiceStatus(oldConfig.getServiceStatus());
        }
        // 是否是服务(1：是 0 是目录)
        conf.setIsService(1);
        // 是否包含时间区间(1：是，0：否)
        conf.setIsDateRange(dateRanges != null && dateRanges.size() > 0 ? 1 : 0);
        long configerId = 0;
        // hasConfigId为空表示新增
        if (StringUtils.isNullOrEmpty(hasConfigId)) {
            try {
                // 插入一条服务 主表中的信息
                configerId = createConfiger(conf);
            } catch (DuplicateKeyException e) {
                // 服务的代码必须唯一
                if (e.getMessage().indexOf("UK_SERVICE_CODE") >= 0) {
                    vo.setResultCode(BaseVo.ERROR_CODE);
                    vo.setResultInfo("服务代码不唯一！");
                    return vo;
                } else {
                    return new ConfigerVo(BaseVo.ERROR_CODE, "添加配置信息出错" + e);
                }
            }

        } else {
            // 修改服务
            configerId = Long.parseLong(hasConfigId);
            conf.setId(configerId);
            modifyConfiger(conf);
        }
        // 给参数子表的服务ID 赋值
        for (ServeOutputParam op : outputArgs) {
            op.setServiceId(configerId);
        }
        conf.setOutputArgs(outputArgs);
        for (ServeInputParam ip : inputArgs) {
            ip.setServiceId(configerId);
        }
        conf.setInputArgs(inputArgs);
        for (ServeSegment sg : segments) {
            sg.setServiceId(configerId);
        }
        conf.setSegments(segments);
        for (ServeInputParamNav op : inputArgsNav) {
            op.setServiceId(configerId);
        }
        conf.setInputArgsNavs(inputArgsNav);
        for (ServeDataRange ip : dateRanges) {
            ip.setServiceId(configerId);
        }
        conf.setDateRangeArgs(dateRanges);
        for (ServeOutputParamNav sg : outputArgsNav) {
            sg.setServiceId(configerId);
        }
        conf.setOutputArgsNav(outputArgsNav);
        int oNum = createOutputParams(outputArgs);
        int iNum = createInputParams(inputArgs);
        int sNum = createSegments(segments);
        int oNumNav = createOutputParamsNav(outputArgsNav);
        int iNumNav = createInputParamsNav(inputArgsNav);
        int deNum = createDateRangeArgs(dateRanges);
        // 记录日志 修改记录表
        String operationId = CookieUtils.acquireUserNameFromRequest();
        createEditLog(configerId, operationId, editMark);
        LOG.debug("输出参数个数：" + oNum);
        LOG.debug("输入参数个数：" + iNum);
        LOG.debug("SQL片段个数：" + sNum);
        LOG.debug("向导输出参数个数：" + oNumNav);
        LOG.debug("向导输入参数个数：" + iNumNav);
        LOG.debug("时间区间个数：" + deNum);
        // 刷新缓存
        RedisUtil.setObject(InitConfigerListener.CONFIG_KEY_EXEC + conf.getServiceCode(), conf, Integer.MAX_VALUE);

        pubCacheService.publishServeConfig(PubCacheService.OPERATE_CREATE, conf.getServiceCode());

        vo.setConfigId(configerId);
        return vo;
    }

    /**
     * 发布GIS服务
     *
     * @param configerId 服务ID
     * @param serviceStatus 服务状态
     * @param editRemark 发布记录
     * @return
     * @author chenzpa 2016年4月14日
     */
    public BaseVo gisConfigerPublish(Long configerId, int serviceStatus, String editRemark) {
        BaseVo vo = new BaseVo();
        serviceStatus = ("false".equalsIgnoreCase(ConfigUtils.getString("serve.isOpenReview", "")) && 4 == serviceStatus) ? 3
                : serviceStatus;
        modifyGisPublishInfo(configerId, serviceStatus);
        // 写更新日志，
        createEditLog(configerId, CookieUtils.acquireUserNameFromRequest(), editRemark);
        return vo;
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
    public BaseVo modifyServeReviewState(Long configerId, int serviceStatus, String editRemark) {
        BaseVo vo = new BaseVo();
        // 首先查询下该服务的当前状态
        ServeConfig serveConfig = serveConfigMapper.selectByPrimaryKey(configerId);
        if (serveConfig.getIsDeleted() == 0) {
            if (serveConfig.getServiceStatus() == 3 || serveConfig.getServiceStatus() == 4) {
                serveConfigMapper.modifyServeReviewState(configerId, serviceStatus);
                // 写更新日志，
                createEditLog(configerId, CookieUtils.acquireUserNameFromRequest(), editRemark);
            } else {
                vo.setResultCode(BaseVo.ERROR_CODE);
                vo.setResultInfo("该服务已被撤销或删除");
            }
        } else {
            vo.setResultCode(BaseVo.ERROR_CODE);
            vo.setResultInfo("该服务已被删除");
        }
        return vo;
    }

    /**
     * 方法的注释
     *
     * @return
     * @author chenzpa 2016年5月26日
     * @param editMark 修改记录
     * @param xmlExamp XML 示例
     * @param jsonExamp JSON示例
     * @param outputArgs 输出参数
     * @param inputArgs 输入参数
     * @param conf 服务配置的基本信息
     * @param outConfigId 服务ID 新增为空
     */
    public ConfigerVo editOutConfig(String outConfigId, ServeConfig conf, List<ServeInputParam> inputArgs,
            List<ServeOutputParam> outputArgs, String jsonExamp, String xmlExamp, String editMark) {
        ConfigerVo vo = new ConfigerVo();
        boolean hasConfigId = !StringUtils.isNullOrEmpty(outConfigId);
        long configerId = 0;
        // 新增 服务状态为0
        if (!hasConfigId) {
            conf.setServiceStatus(0);// 服务状态，新增
            conf.setCreateUser(CookieUtils.acquireUserNameFromRequest());
        } else {
            // 修改服务的时候就要把原来的状态赋值
            ServeConfig oldConfig = queryBaseConfiger(outConfigId);
            conf.setServiceStatus(oldConfig.getServiceStatus());
        }
        // 如果是修改 就先删除输入和输出参数
        if (hasConfigId) {
            configerId = Long.parseLong(outConfigId);
            conf.setId(configerId);
            // 删除旧的输出参数
            removeOutputParams(outConfigId);
            // 删除旧的输入参数
            removeInputParams(outConfigId);
            // 修改 配置的主表信息
            modifyConfiger(conf);
            // 修改 JSON 示例
            modifyJSONexamp("json", configerId, jsonExamp);
            modifyJSONexamp("xml", configerId, xmlExamp);
        } else {
            try {
                configerId = createConfiger(conf);
            } catch (DuplicateKeyException e) {
                if (e.getMessage().indexOf("UK_SERVICE_CODE") >= 0) {
                    vo.setResultCode(BaseVo.ERROR_CODE);
                    vo.setResultInfo("服务代码不唯一！");
                    return vo;
                } else {
                    return new ConfigerVo(BaseVo.ERROR_CODE, "添加配置信息出错" + e);
                }
            }
            // 新增的时候返回增加的ID
            vo.setConfigId(configerId);
            // 插入JSON 示例
            if (!StringUtils.isNullOrEmpty(jsonExamp)) {
                createJSONexamp("json", configerId, jsonExamp);
            }
            // 插入XML示例
            if (!StringUtils.isNullOrEmpty(xmlExamp)) {
                createJSONexamp("xml", configerId, xmlExamp);
            }
        }
        // 插入输入输出参数
        for (ServeInputParam op : inputArgs) {
            op.setServiceId(configerId);
        }
        conf.setInputArgs(inputArgs);
        for (ServeOutputParam sg : outputArgs) {
            sg.setServiceId(configerId);
        }
        conf.setOutputArgs(outputArgs);
        createInputParams(inputArgs);
        createOutputParams(outputArgs);
        // 记录日志 修改记录表
        //
        createEditLog(configerId, CookieUtils.acquireUserNameFromRequest(),
                StringUtils.isNullOrEmpty(editMark) ? (hasConfigId ? "修改一下外部服务" : "新增的外部服务哦") : editMark);

        return vo;
    }

    /**
     * 查询不同服务的已发布条数 和 其他状态条数
     *
     * @return
     * @author chenzpa 2016年8月15日
     */
    public ServeConfigVo queryServiceListNum() {
        // select service_type serviceType,service_status serviceStatus,count(1) count from
        // serve_config where service_type in (1,2,3) group by service_type,service_status order by
        // service_type,service_status
        List<ServeCount> dataServeCounts = queryMapper.queryServiceListNum();
        ServeConfigVo vo = new ServeConfigVo();
        vo.setDatas(dataServeCounts);
        return vo;
    }

}
