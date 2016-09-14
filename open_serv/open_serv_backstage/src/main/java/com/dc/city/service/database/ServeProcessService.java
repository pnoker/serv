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
package com.dc.city.service.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dc.city.common.exception.BusinessException;
import com.dc.city.common.utils.DateUtils;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.dao.master.config.ServeConfigMapper;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.config.ServeConfigWhiteList;
import com.dc.city.domain.config.ServeDataRange;
import com.dc.city.domain.config.ServeInputParam;
import com.dc.city.domain.config.ServeSegment;
import com.dc.city.listener.support.ServeConfigCache;
import com.dc.city.service.config.ConfigerService;
import com.dc.city.service.securitymanage.UserManageService;
import com.dc.city.vo.database.ServiceBaseVo;
import com.dc.city.vo.securitymanage.SecurityManageVo;
import com.dc.city.vo.securitymanage.user.ServeUserAuthorityVo;

/**
 * 服务处理service
 * 负责处理外部系统调用接口请求
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月11日 下午4:31:55
 *          Copyright 2016 by DigitalChina
 */
@Service
public class ServeProcessService {
    @Resource
    ServeConfigMapper configMapper;

    @Resource
    UserManageService userService;

    @Resource
    ConfigerService configService;

    @Resource
    NamedParameterJdbcTemplate jdbcTemplate;

    // 不验证权限
    public static final int NON_VERIFY_ACCESS = 0;

    /**
     * 执行服务配置代码
     *
     * @param config 服务配置java对象
     * @param requestMap 请求参数Map对象
     * @return 服务请求vo对象
     * @author zhongdt 2016年5月3日
     * @throws BusinessException 
     * @throws Exception
     */
    public ServiceBaseVo excuteService(ServeConfig config, Map<String, String> requestMap) throws BusinessException {
        // 判断是否有必填参数未输入
        if (!verifyRequiredParam(config, requestMap)) {
            return new ServiceBaseVo("-1", "必填参数未传");
        }

        // 构造服务所需请求参数
        Map<String, Object> paramMap;

        paramMap = handleParam(config, requestMap);

        // 生成动态SQL
        String querySql;

        querySql = handleQuerySql(config, requestMap);

        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        // 执行sql语句
        datas = jdbcTemplate.queryForList(querySql, paramMap);

        ServiceBaseVo vo = new ServiceBaseVo();
        vo.setDatas(datas);
        return vo;
    }

    /**
     * 收到服务请求，执行服务处理之前的验证工作，验证通过后执行excute
     *
     * @param config
     * @param requestMap
     * @return
     * @throws Exception
     * @author zhongdt 2016年5月3日
     * @throws BusinessException 
     */

    public ServiceBaseVo handleService(ServeConfig config, Map<String, String> requestMap) throws BusinessException {
        // 判断判断服务访问权限
        String appKey = requestMap.get("appkey") == null ? "" : requestMap.get("appkey").toString().trim();
        if (config.getVerifyAccess() != NON_VERIFY_ACCESS) {
            if (!verifyAccess(appKey, config.getServiceCode())) {
                return new ServiceBaseVo("-1", "无权限访问");
            }
        }

        // 验证白名单
        String ipAddress = requestMap.containsKey("ip") ? requestMap.get("ip") : null;
        if (!verifyIpAddress(config, ipAddress)) {
            // 状态不正常或者未发布的
            return new ServiceBaseVo("-1", "IP地址不在白名单");
        }

        return excuteService(config, requestMap);
    }

    /**
     * 根据code，状态 获取config配置，从本地缓存中读取，不读redis，不读数据库，
     * 
     * @param serviceCode
     * @param isDeleted
     * @param status
     * @return
     * @author zhongdt 2016年3月16日
     * @throws BusinessException
     * @throws Exception
     */
    public ServeConfig getLocalConfig(String serviceCode) throws BusinessException {
        ServeConfig config = ServeConfigCache.getInstance().getConfig(serviceCode);
        return config;
    }

    /**
     * 验证用户权限
     *
     * @param appKey 用户appKey
     * @param serviceCode 服务配置serviceCoed
     * @return true:可以访问 ，false:不能访问
     * @author zhongdt 2016年3月11日
     */
    private boolean verifyAccess(String appKey, String serviceCode) {
        if (StringUtils.isNullOrEmpty(appKey)) {
            return false;
        }
        // 调用用户接口，获取用户权限信息
        SecurityManageVo vo = userService.queryServiceAuth(appKey, serviceCode);
        if (!vo.getResultCode().equals("0")) {
            return false;
        }
        // 得到用户权限对象
        ServeUserAuthorityVo auth = (ServeUserAuthorityVo) vo.getObject();
        return auth == null ? false : (auth.getAccessPermission() == 1 ? true : false);
    }

    /**
     * 验证白名单地址
     *
     * @param config 服务配置java对象
     * @param ipAddress 请求地址，前端传入
     * @return true 可以访问，false:不能访问
     * @author zhongdt 2016年3月16日
     */
    private boolean verifyIpAddress(ServeConfig config, String ipAddress) {
        // 等于0时表示不验证白名单，其他则验证
        if (config.getVerifyIp() == null || config.getVerifyIp() == 0) {
            return true;
        }
        if (StringUtils.isNullOrEmpty(ipAddress)) {
            return false;
        }
        // 读取白名单列表，匹配IPaddress，匹配到则返回true，其他情况下false，表示ip地址不在此白名单内，不能访问
        for (ServeConfigWhiteList white : config.getWhiteList()) {
            if (ipAddress.matches(white.getIpAddress())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是否有必填参数未传
     *
     * @param config 服务配置java对象
     * @param param 请求参数map对象
     * @return true:必填参数都传，false:有必填参数没有传递
     * @author zhongdt 2016年3月11日
     */
    private boolean verifyRequiredParam(ServeConfig config, Map<String, String> paramMap) {
        List<ServeInputParam> inputs = config.getInputArgs();
        for (ServeInputParam input : inputs) {
            String paramCode = input.getParamCode().toLowerCase().trim();
            // params中如果没有必填的参数，说明有必填参数未传
            if (input.getIsRequired() != null && input.getIsRequired() == 1 && !paramMap.containsKey(paramCode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 组装SQL需要用到的参数
     *
     * @param config 服务配置java对象
     * @param param 请求参数map对象
     * @return SQL查询时需要用到的参数
     * @author zhongdt 2016年3月14日
     * @throws BusinessException 
     */
    private Map<String, Object> handleParam(ServeConfig config, Map<String, String> requestMap) throws BusinessException  {
        // 如果配置为空，或者不用传入参数，则组装空的map
        if (config == null || config.getInputArgs() == null || config.getInputArgs().isEmpty()) {
            return new HashMap<String, Object>();
        }
        // 遍历参数列表
        Map<String, Object> paramMap = new HashMap<String, Object>();
        for (ServeInputParam input : config.getInputArgs()) {
            String paramCode = input.getParamCode().toLowerCase().trim();
            if (paramCode == null) {
                continue;
            }
            // request中如果传，则构造指定类型的参数
            if (!requestMap.containsKey(paramCode)) {
                continue;
            }
            String paramValueStr = requestMap.get(paramCode);
            // 根据参数类型获取指定类型的参数
            Object paramValue = getParamDataType(paramValueStr, input.getParamType());
            paramMap.put(paramCode, paramValue);
        }

        // 组装时间区间参数
        if (config.getIsDateRange() == 1) {
            List<ServeDataRange> ranges = config.getDateRangeArgs();
            for (ServeDataRange range : ranges) {
                Date endDate = DateUtils.getStartTime(new Date());
                Integer dateRange = 0;
                try {
                    dateRange = Integer.parseInt(range.getDataRange());
                } catch (Exception e) {

                }
                Date beginDate = DateUtils.getEndTime(DateUtils.getBeforeDateTime(24 * dateRange));
                paramMap.put(range.getColumnCode().toLowerCase().trim() + "_begindate", beginDate);
                paramMap.put(range.getColumnCode().toLowerCase().trim() + "_enddate", endDate);
            }
        }

        return paramMap;
    }

    /**
     * 动态瓶装SQL
     *
     * @param config 服务配置java对象
     * @param param 服务请求参数map
     * @return
     * @author zhongdt 2016年3月14日
     * @throws BusinessException
     * @throws Exception
     */
    private String handleQuerySql(ServeConfig config, Map<String, String> param) throws BusinessException {
        String querySql = config.getQuerySql();
        if (querySql == null || StringUtils.isNullOrEmpty(querySql)) {
            throw new BusinessException("服务查询语句配置错误");
        }
        // 获取片段信息
        List<ServeSegment> segments = config.getSegments();

        // 拼装动态参数
        for (ServeSegment segment : segments) {
            String segStr = "{" + segment.getSegmentCode() + "}";
            String replaceSql = "";
            String paramCode = segment.getParamCode().toLowerCase().trim();
            // 如果sql中有次片段,并且传递了参数，则替换为指定sql，没有则替换为""
            if (querySql.indexOf(segStr) >= 0 && param.containsKey(paramCode)) {
                replaceSql = segment.getReplaceSql() + "";
            }
            querySql = querySql.replace(segStr, replaceSql);
        }

        // 拼装时间区间参数
        StringBuffer rangesBuffer = new StringBuffer("");
        if (config.getIsDateRange() == 1) {
            List<ServeDataRange> ranges = config.getDateRangeArgs();
            for (ServeDataRange range : ranges) {
                String columnCode = range.getColumnCode().toLowerCase().trim();
                rangesBuffer.append(" AND " + columnCode + ">=:" + columnCode + "_begindate");
                rangesBuffer.append(" AND " + columnCode + "<=:" + columnCode + "_enddate");
            }
        }
        // 替换{range}为对应sql
        querySql = querySql.replace("{range}", rangesBuffer.toString());
        // 保证参数统一，全部转小写
        return querySql.toLowerCase().trim();
    }

    // 列类型0:int,1:string,2:float,3:date,4:datetime, 时间格式为标准格式（yyyy-MM-dd HH:mm:ss)
    public static final int DATA_TYPE_INTEGER = 0;
    public static final int DATA_TYPE_STRING = 1;
    public static final int DATA_TYPE_FLOAT = 2;
    public static final int DATA_TYPE_DATE = 3;
    public static final int DATA_TYPE_DATETIME = 4;

    /**
     * 将字符串的值，强转成对应的类型
     *
     * @param value 参数值字符
     * @param paramType 参数类型
     * @return 返回java类型的参数值
     * @author zhongdt 2016年3月14日
     * @throws ParseException
     * @throws BusinessException
     */
    private Object getParamDataType(String value, Integer paramType) throws BusinessException {
        if (paramType == null) {
            throw new BusinessException("服务参数类型未设置");
        }
        try {
            switch (paramType) {
                case DATA_TYPE_INTEGER:
                    return Integer.parseInt(value);
                case DATA_TYPE_STRING:
                    return value;
                case DATA_TYPE_FLOAT:
                    return Float.parseFloat(value);
                case DATA_TYPE_DATE:
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return dateFormat.parse(value);
                case DATA_TYPE_DATETIME:
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    return dateTimeFormat.parse(value);
                default:
                    return null;
            }
        } catch (ParseException e) {
            throw new BusinessException("参数类型转换错误，type=" + paramType + "&value=" + value + "");
        }
    }

}
