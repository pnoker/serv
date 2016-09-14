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
package com.dc.city.service.securitymanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.common.security.AesUtils;
import com.dc.city.common.utils.Base64EncodeUtils;
import com.dc.city.common.utils.BusinessUtils;
import com.dc.city.common.utils.RegexUtils;
import com.dc.city.dao.master.securitymanage.user.UserManageMapper;
import com.dc.city.domain.config.ServeConfigAuth;
import com.dc.city.domain.securitymanage.user.ServeUser;
import com.dc.city.domain.securitymanage.user.ServeUserAuthority;
import com.dc.city.listener.config.InitConfigerListener;
import com.dc.city.pojo.securitymanage.user.ServeConfigPo;
import com.dc.city.pojo.securitymanage.user.UserAuthorizationPo;
import com.dc.city.pojo.securitymanage.user.UserManagePo;
import com.dc.city.pojo.securitymanage.user.UsersCachePo;
import com.dc.city.pojo.serve.catalog.CatalogAuthPo;
import com.dc.city.pojo.serve.catalog.CatalogPo;
import com.dc.city.service.catalog.CatalogService;
import com.dc.city.vo.catalog.CataLogQueryVo;
import com.dc.city.vo.catalog.CatalogBaseVo;
import com.dc.city.vo.securitymanage.SecurityManageVo;
import com.dc.city.vo.securitymanage.user.ServeUserAuthorityVo;
import com.dc.city.vo.securitymanage.user.UserManageVo;

/**
 * 用户管理Service
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月9日 下午2:24:07
 *          Copyright 2016 by DigitalChina
 */
@Service
public class UserManageService {

    private static Log log = LogFactory.getLog("UserManageService");

    @Resource
    private UserManageMapper userManageMapper;

    @Resource
    private CatalogService catalogService;

    /**
     * 通过appKey查询用户
     *
     * @param appKey
     * @return
     * @author zuoyue 2016年3月16日
     */
    public SecurityManageVo queryUserByAppKey(String appKey) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // 通过appKey从缓存的用户信息中获取该用户
        UsersCachePo usersCachePo = (UsersCachePo) RedisUtil.getObject(appKey);
        if (usersCachePo != null) {
            // 查到了用户信息
            UserManageVo userManageVo = new UserManageVo();
            // copy属性
            BeanUtils.copyProperties(usersCachePo, userManageVo);
            securityManageVo.setObject(userManageVo);
        } else {
            // 查数据库
            List<UsersCachePo> usersCachePoList = userManageMapper.queryUserByAppKey(appKey);
            if (usersCachePoList != null && usersCachePoList.size() > 0) {
                UsersCachePo usersCachePoByDataBase = usersCachePoList.get(0);
                if (usersCachePoByDataBase != null) {
                    // copy属性
                    UserManageVo userManageVo = new UserManageVo();
                    BeanUtils.copyProperties(usersCachePoByDataBase, userManageVo);
                    securityManageVo.setObject(userManageVo);
                } else {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(UserManageVo.QUERY_USER_NOT_FOUND_MSG);
                    log.error("用户管理接口-通过appKey查询用户时,获取用户对象为空");
                    return securityManageVo;
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.QUERY_USER_NOT_FOUND_MSG);
                log.error("用户管理接口-通过appKey查询用户时,获取用户list为空");
                return securityManageVo;
            }
        }
        return securityManageVo;
    }

    /**
     * 更新APPkey
     *
     * @param userManageVo
     * @return
     * @author zhongdt 2016年4月5日
     */
    public SecurityManageVo modifyBuildAppKey(UserManageVo userManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (userManageVo == null || userManageVo.getId() <= 0) {
            securityManageVo.setResultCode(UserManageVo.AUTH_ERROR_MSG);
            securityManageVo.setResultInfo(UserManageVo.REBUILD_APPKEY_ERROR_MSG);
            return securityManageVo;
        }
        // 验证
        ServeUser user = this.findByPk(userManageVo.getId());
        BeanUtils.copyProperties(user, userManageVo);

        userManageVo.setOriginalKey(userManageVo.getAppKey());
        userManageVo.setAppKey(UUID.randomUUID().toString());
        return this.modifyUser(userManageVo);
    }

    /**
     * 查询用户是否具有该服务的查看访问权限
     *
     * @param appKey
     * @param serviceCode
     * @return
     * @author zuoyue 2016年3月13日
     */
    public SecurityManageVo queryServiceAuth(String appKey, String serviceCode) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // 是否具有服务访问权限（1为具有）
        int viewPermission = 0;
        // 是否具有服务调用权限
        int accessPermission = 0;
        // 通过serviceCode从缓存的SERVE_CONFIG中获取配置信息
        ServeConfigAuth serveConfigAuth = (ServeConfigAuth) RedisUtil.getObject(InitConfigerListener.CONFIG_KEY_PREEXT
                + serviceCode);
        String allPath = "";
        // 是否需要验证访问权限（1为需要验证）
        int verifyView = 0;
        // 是否需要验证调用权限
        int verifyAccess = 0;
        // 查到了配置信息
        if (serveConfigAuth != null) {
            allPath = serveConfigAuth.getAllPath();
            verifyView = serveConfigAuth.getVerifyView();
            verifyAccess = serveConfigAuth.getVerifyAccess();
        } else {
            // 查数据库
            List<ServeConfigPo> serveConfigPoList = userManageMapper.queryServeConfigByServiceCode(serviceCode);
            if (serveConfigPoList != null && serveConfigPoList.size() > 0) {
                ServeConfigPo serveConfigPo = serveConfigPoList.get(0);
                // 查到了数据
                if (serveConfigPo != null) {
                    allPath = serveConfigPo.getAllPath();
                    verifyView = serveConfigPo.getVerifyView();
                    verifyAccess = serveConfigPo.getVerifyAccess();
                } else {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(UserManageVo.QUERY_USER_AUTH_ERROR_MSG);
                    log.error("用户管理接口-查询用户是否具有该服务的查看访问权限时,服务配置查询对象为空");
                    return securityManageVo;
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.QUERY_USER_AUTH_ERROR_MSG);
                log.error("用户管理接口-查询用户是否具有该服务的查看访问权限时,服务配置查询的list为空");
                return securityManageVo;
            }
        }
        // 如果该服务需要验证权限
        if (verifyAccess == 1 || verifyView == 1) {
            // 通过appKey从缓存的用户信息中获取该用户具有的权限
            UsersCachePo usersCachePo = (UsersCachePo) RedisUtil.getObject(appKey);
            // 用户具有的查询权限列表
            String viewPermissions = "";
            // 用户具有的调用权限列表
            String accessPermissions = "";
            if (usersCachePo != null) {
                // 查到了用户信息
                viewPermissions = usersCachePo.getViewPermissions();
                accessPermissions = usersCachePo.getAccessPermissions();
            } else {
                // 查数据库
                List<UsersCachePo> usersCachePoList = userManageMapper.queryActiveUsersByAppKey(appKey);
                if (usersCachePoList != null && usersCachePoList.size() > 0) {
                    UsersCachePo usersCachePoByDataBase = usersCachePoList.get(0);
                    // 查到了数据
                    if (usersCachePoByDataBase != null) {
                        viewPermissions = usersCachePoByDataBase.getViewPermissions();
                        accessPermissions = usersCachePoByDataBase.getAccessPermissions();
                    } else {
                        securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                        securityManageVo.setResultInfo(UserManageVo.QUERY_USER_AUTH_ERROR_MSG);
                        log.error("用户管理接口-查询用户是否具有该服务的查看访问权限时,获取用户服务权限对象为空");
                        return securityManageVo;
                    }
                } else {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(UserManageVo.QUERY_USER_AUTH_ERROR_MSG);
                    log.error("用户管理接口-查询用户是否具有该服务的查看访问权限时,获取用户服务权限的list为空");
                    return securityManageVo;
                }
            }
            // 验证访问权限
            if (verifyView == 1) {
                viewPermission = validateServiceAuth(viewPermissions, allPath);
            } else {
                viewPermission = 1;
            }
            // 验证调用权限
            if (verifyAccess == 1) {
                accessPermission = validateServiceAuth(accessPermissions, allPath);
            } else {
                accessPermission = 1;
            }
        } else {
            viewPermission = 1;
            accessPermission = 1;
        }
        ServeUserAuthorityVo serveUserAuthorityVo = new ServeUserAuthorityVo();
        serveUserAuthorityVo.setAccessPermission(accessPermission);
        serveUserAuthorityVo.setViewPermission(viewPermission);
        securityManageVo.setObject(serveUserAuthorityVo);
        return securityManageVo;
    }

    /**
     * 验证用户服务权限
     *
     * @param userAuths 用户具有访问或调用权限的服务ids
     * @param servicePath 服务所在的目录路径
     * @return
     * @author zuoyue 2016年3月13日
     */
    private int validateServiceAuth(String userAuths, String servicePath) {
        int verifyAuth = 0;
        // 配置了权限
        if (StringUtils.isNotBlank(userAuths)) {
            breakPoint:
            // 目录路径按逗号拆分
            for (String path : servicePath.split(",")) {
                // 服务ids按逗号拆分
                for (String userAuth : userAuths.split(",")) {
                    // 匹配到了，表示有该目录的权限
                    if (path.equals(userAuth)) {
                        verifyAuth = 1;
                        break breakPoint;
                    }
                }
            }
        }
        return verifyAuth;
    }

    /**
     * 查询用户
     *
     * @param blackListManageQueryVo
     * @return
     * @author zuoyue 2016年3月8日
     */
    public SecurityManageVo queryUser(UserManageVo userManageQueryVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // copy属性
        UserManagePo userManageQueryPo = new UserManagePo();
        BeanUtils.copyProperties(userManageQueryVo, userManageQueryPo);
        // 取总条数
        long totalCount = userManageMapper.getTotalCount(userManageQueryPo);
        if (totalCount == 0) {
            securityManageVo.setResultCode(SecurityManageVo.SUCCESS_CODE);
            securityManageVo.setResultInfo(UserManageVo.QUERY_USER_NOT_FOUND_MSG);
            securityManageVo.setTotalCount("0");
            securityManageVo.setDatas(new ArrayList<UserManageVo>());
            return securityManageVo;
        }// 设置总条数
        securityManageVo.setTotalCount(totalCount + "");
        // 查询数据库后的结果
        List<UserManagePo> userManagePoList = userManageMapper.queryUser(userManageQueryPo);
        if (userManagePoList != null && userManagePoList.size() > 0) {
            // 返回给前端的
            List<UserManageVo> userManageVoList = new ArrayList<UserManageVo>();
            for (UserManagePo userManagePo : userManagePoList) {
                if (userManagePo != null) {
                    // copy属性
                    UserManageVo userManageVo = new UserManageVo();
                    BeanUtils.copyProperties(userManagePo, userManageVo);
                    userManageVo.setUserChannelName(userManagePo.getUserChannel() == 1 ? "内网" : "外网");
                    userManageVoList.add(userManageVo);
                }
            }
            securityManageVo.setDatas(userManageVoList);
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.QUERY_USER_NOT_FOUND_MSG);
        }
        return securityManageVo;
    }

    /**
     * 保存用户服务接口与目录授权
     *
     * @param userId
     * @param appKey
     * @param userAuthList
     * @return
     * @author zuoyue 2016年3月11日
     */
    public SecurityManageVo createUserAuthorization(long userId, String appKey, Integer serviceId, List<?> userAuthList) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // 先删除
        UserAuthorizationPo userAuthorizationPo = new UserAuthorizationPo();
        userAuthorizationPo.setUserId(userId);
        userAuthorizationPo.setServiceId(serviceId);
        userManageMapper.removeUserAuthorization(userAuthorizationPo);
        // 再新增
        int createSize = userManageMapper.createUserAuthorization(userAuthList);
        if (createSize == 0) {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
            log.error("用户管理接口-用户服务接口与目录授权时,数据库操作失败");
        } else {
            // 入库成功也要修改缓存
            // json字符串,格式："[{\"userId\": 11,\"serviceId\": 2,\"viewPermission\": 1,\"accessPermission\": 1}]";
            String viewPermissions = ",";
            String accessPermissions = ",";
            // 遍历权限列表
            for (Object object : userAuthList) {
                ServeUserAuthorityVo serveUserAuthorityVo = (ServeUserAuthorityVo) object;
                // 用户有该服务的查看权限
                if (serveUserAuthorityVo.getViewPermission() == 1) {
                    viewPermissions += serveUserAuthorityVo.getServiceId() + ",";
                }
                // 用户有该服务的访问权限
                if (serveUserAuthorityVo.getAccessPermission() == 1) {
                    accessPermissions += serveUserAuthorityVo.getServiceId() + ",";
                }
            }
            // 通过appKey从redis中取出用户信息
            UsersCachePo usersCachePo = (UsersCachePo) RedisUtil.getObject(appKey);
            if (usersCachePo == null) {
                usersCachePo = new UsersCachePo();
                usersCachePo.setAppKey(appKey);
            }
            // 重新设置新的viewPermission与accessPermissions
            usersCachePo.setViewPermissions(viewPermissions.substring(1));
            usersCachePo.setAccessPermissions(accessPermissions.substring(1));
            RedisUtil.setObject(appKey, usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
        }
        return securityManageVo;
    }


    /**
     * 通过serviceId授权
     *
     * @param userIds
     * @param serviceId
     * @param userAuthList
     * @return
     * @author zuoyue 2016年5月27日
     */
    public SecurityManageVo createUserAuthorizationByServiceId(String userIds, long serviceId, List<?> userAuthList) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // 先通过serviceId查询出当前数据库中哪些用户有该serviceId的权限
        List<UserManagePo> userManagePoList = userManageMapper.queryUserByServiceId(serviceId);
        // 数据库是否有该serviceId的记录
        boolean isServiceIdExist = false;
        // 如果没有查到记录，直接新增，如果查到了记录，则要先删除再新增
        if (userManagePoList != null && userManagePoList.size() > 0) {
            UserAuthorizationPo userAuthorizationPo = new UserAuthorizationPo();
            userAuthorizationPo.setServiceId(serviceId);
            // 先删除
            userManageMapper.removeUserAuthorization(userAuthorizationPo);
            isServiceIdExist = true;
        }
        // 再新增
        int createSize = userManageMapper.createUserAuthorization(userAuthList);
        if (createSize == 0) {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
            log.error("用户管理接口-通过serviceId授权时,数据库操作失败");
        } else {
            // 入库成功也要修改缓存,json字符串,格式："[{\"userId\": 11,\"serviceId\": 2,\"viewPermission\": 1,\"accessPermission\": 1}]";
            if (isServiceIdExist) {
                // 待移除权限的user
                for (UserManagePo user : userManagePoList) {
                    // 取用户id
                    long hasServiceIdAuthUserId = user.getId();
                    boolean flag = true;
                    for (String userId : userIds.split(",")) {
                        // 有该服务权限的用户id
                        if (hasServiceIdAuthUserId == Long.parseLong(userId)) {
                            flag = false;
                            break;
                        }
                    }
                    // 如果有需要删的
                    if (flag) {
                        // userManageRemovePoList.add(user);
                        // 从Redis中移除该用户的该serviceId的权限
                        UsersCachePo usersCachePo = (UsersCachePo) RedisUtil.getObject(user.getAppKey());
                        if (usersCachePo != null) {
                            // 得到原始的权限
                            String viewPermissions = usersCachePo.getViewPermissions();
                            String accessPermissions = usersCachePo.getAccessPermissions();
                            // 重新设置新的viewPermission与accessPermissions
                            usersCachePo.setViewPermissions(analysisStr(viewPermissions, serviceId));
                            usersCachePo.setAccessPermissions(analysisStr(accessPermissions, serviceId));
                            RedisUtil.setObject(user.getAppKey(), usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
                        }
                    }
                }
            }
            // 通过userIds查到appkey
            List<UserManagePo> usersList = userManageMapper.queryUserByUserIds(userIds.split(","));
            // 用户权限放入Redis
            setUserAuthorizationToRedis(usersList, userAuthList);
        }
        return securityManageVo;
    }


    /**
     * 用户权限放入Redis
     *
     * @param usersList
     * @param userAuthList
     * @author zuoyue 2016年5月27日
     */
    private void setUserAuthorizationToRedis(List<UserManagePo> usersList, List<?> userAuthList) {
        String viewPermissions = "";
        String accessPermissions = "";
        for (UserManagePo user : usersList) {
            // 通过appKey从redis中取出用户信息
            UsersCachePo usersCachePo = (UsersCachePo) RedisUtil.getObject(user.getAppKey());
            if (usersCachePo == null) {
                usersCachePo = new UsersCachePo();
                usersCachePo.setAppKey(user.getAppKey());
            } else {
                // 得到原始的权限
                viewPermissions = usersCachePo.getViewPermissions();
                accessPermissions = usersCachePo.getAccessPermissions();
            }
            // 得到这个用户这次新的权限
            for (Object object : userAuthList) {
                ServeUserAuthorityVo serveUserAuthorityVo = (ServeUserAuthorityVo) object;
                if (user.getId() == serveUserAuthorityVo.getUserId()) {
                    // 查看权限
                    if (serveUserAuthorityVo.getViewPermission() == 1) {
                        viewPermissions = StringUtils.isNotBlank(viewPermissions) ? viewPermissions + ","
                                + serveUserAuthorityVo.getServiceId() : serveUserAuthorityVo.getServiceId() + "";
                    }
                    // 访问权限
                    if (serveUserAuthorityVo.getAccessPermission() == 1) {
                        accessPermissions = StringUtils.isNotBlank(accessPermissions) ? accessPermissions + ","
                                + serveUserAuthorityVo.getServiceId() : serveUserAuthorityVo.getServiceId() + "";
                    }
                    // 重新设置新的viewPermission与accessPermissions
                    usersCachePo.setViewPermissions(viewPermissions);
                    usersCachePo.setAccessPermissions(accessPermissions);
                    RedisUtil.setObject(user.getAppKey(), usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
                    break;
                }
            }
        }
    }

    /**
     * 删除用户服务接口与目录授权
     *
     * @param id
     * @param appKey
     * @return
     * @author zuoyue 2016年3月12日
     */
    public SecurityManageVo removeUserAuthorization(long id, String appKey) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        UserAuthorizationPo userAuthorizationPo = new UserAuthorizationPo();
        userAuthorizationPo.setUserId(id);
        int size = userManageMapper.removeUserAuthorization(userAuthorizationPo);
        if (size == 0) {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
            log.error("用户管理接口-删除用户服务接口与目录授权时,数据库操作失败");
        } else {
            // 入库成功也要删除缓存中的权限信息,首先通过appKey取出用户信息，再将权限字段清空
            UsersCachePo usersCachePo = (UsersCachePo) RedisUtil.getObject(appKey);
            usersCachePo.setViewPermissions("");
            usersCachePo.setAccessPermissions("");
            RedisUtil.setObject(appKey, usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
        }
        return securityManageVo;
    }

    /**
     * 删除用户
     *
     * @param blackListId
     * @return
     * @author zuoyue 2016年3月8日
     */
    public SecurityManageVo removeUser(long id, String appKey) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        int size = userManageMapper.removeUser(id);
        if (size == 0) {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.REMOVE_USER_ERROR_MSG);
            log.error("用户管理接口-删除用户时,数据库操作失败");
        } else {
            // 入库成功也要删除缓存
            RedisUtil.delByKey(appKey);
        }
        return securityManageVo;
    }

    /**
     * 编辑用户
     *
     * @param userManageVo
     * @return
     * @author zuoyue 2016年3月10日
     */
    public SecurityManageVo modifyUser(UserManageVo userManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        boolean isPass = false;
        // 若密码不为空，表示修改了密码
        String passWord = userManageVo.getUserPass();
        // 用户
        String userName = userManageVo.getUserName();
        UserManagePo userManagePo = new UserManagePo();
        // 复制属性
        BeanUtils.copyProperties(userManageVo, userManagePo);
        if (StringUtils.isNotBlank(passWord)) {
            // 生成salt
            String encryptSalt = userName + UUID.randomUUID().toString();
            // 将前端传过来的密码进行解密
            // String decryptPass = new String(Base64EncodeUtils.decode(passWord));
            String decryptPass = passWord;
            if (StringUtils.isNotBlank(decryptPass)) {
                // 再将密码加密
                String encryptPwd = AesUtils.aecEncryptBySalt(decryptPass, encryptSalt);
                if (StringUtils.isNotBlank(encryptPwd)) {
                    // 密码放进authUserPo
                    userManagePo.setUserPassWord(encryptPwd);
                    // 设置salt
                    userManagePo.setEncryptSalt(encryptSalt);
                    isPass = true;
                } else {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(UserManageVo.MODIFY_USER_ERROR_MSG);
                    log.error("用户管理接口-修改用户时,密码加密失败，加密前的密码为：" + decryptPass + "，加密后的密码为：" + encryptPwd);
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.MODIFY_USER_ERROR_MSG);
                log.error("用户管理接口-修改用户时,密码解密失败，解密前的密码为：" + userManageVo.getUserPass() + "，解密后的密码为：" + decryptPass);
            }
        } else {
            isPass = true;
        }
        // 验证通过
        if (isPass) {
            // 修改条数
            int size = userManageMapper.modifyUser(userManagePo);
            if (size == 0) {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.MODIFY_USER_ERROR_MSG);
                log.error("用户管理接口-修改用户时,数据库操作失败");
            } else {
                // 入库成功也要放入缓存
                // 旧的originalKey
                String originalKey = userManageVo.getOriginalKey();
                // 当前的appKey
                String currentAppKey = userManageVo.getAppKey();
                if (!currentAppKey.equals(originalKey)) {
                    // 如果重新生成了appKey,需要先通过原来的appKey获取用户信息，再删除原来的，最后放入新的
                    UsersCachePo usersCachePo = (UsersCachePo) RedisUtil.getObject(originalKey);
                    if (usersCachePo != null) {
                        usersCachePo.setAppKey(currentAppKey);
                        RedisUtil.setObject(currentAppKey, usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
                        RedisUtil.delByKey(originalKey);
                    }
                }
            }
        }
        return securityManageVo;
    }

    /**
     * 新增用户
     *
     * @param userManageVo
     * @return
     * @author zuoyue 2016年3月9日
     */
    public SecurityManageVo createUser(UserManageVo userManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        String userName = userManageVo.getUserName();
        // 验证用户是否存在(查询数据库)
        securityManageVo = checkUserIsExist(userName);
        if (SecurityManageVo.SUCCESS_CODE.equals(securityManageVo.getResultCode())) {
            // 生成salt
            String encryptSalt = userName + UUID.randomUUID().toString();
            // 用户key
            String appKey = UUID.randomUUID().toString();
            // 验证通过后将前端传过来的密码进行解密
            // String decryptPass = new
            // String(Base64EncodeUtils.decode(userManageVo.getUserPass()));
            String decryptPass = userManageVo.getUserPass();
            if (StringUtils.isNotBlank(decryptPass)) {
                // 再将密码加密
                String encryptPwd = AesUtils.aecEncryptBySalt(decryptPass, encryptSalt);
                if (StringUtils.isNotBlank(encryptPwd)) {
                    UserManagePo userManagePo = new UserManagePo();
                    // 复制属性
                    BeanUtils.copyProperties(userManageVo, userManagePo);
                    // 密码放进authUserPo
                    userManagePo.setUserPassWord(encryptPwd);
                    // 设置salt
                    userManagePo.setEncryptSalt(encryptSalt);
                    // 设置用户key
                    userManagePo.setAppKey(appKey);
                    // 设置可用
                    userManagePo.setIsDeleted(0);
                    // 设置网络标识
                    userManagePo.setUserChannel(1);
                    // 插入条数
                    int size = userManageMapper.createUser(userManagePo);
                    if (size == 0) {
                        securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                        securityManageVo.setResultInfo(UserManageVo.CREATE_USER_ERROR_MSG);
                        log.error("用户管理接口-新增用户时,数据库操作失败");
                    } else {
                        securityManageVo.setObject(new UserManageVo(appKey));
                        // 入库成功也要放入缓存
                        long id = userManagePo.getId();
                        UsersCachePo usersCachePo = new UsersCachePo();
                        usersCachePo.setId(id);
                        usersCachePo.setAppKey(appKey);
                        usersCachePo.setUserName(userName);
                        // 设置用户渠道
                        usersCachePo.setUserChannel(1);
                        RedisUtil.setObject(appKey, usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
                    }
                } else {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(UserManageVo.CREATE_USER_ERROR_MSG);
                    log.error("用户管理接口-新增用户时,密码加密失败，加密前的密码为：" + decryptPass + "，加密后的密码为：" + encryptPwd);
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.CREATE_USER_ERROR_MSG);
                log.error("用户管理接口-新增用户时,密码解密失败，解密前的密码为：" + userManageVo.getUserPass() + "，解密后的密码为：" + decryptPass);
            }
        }
        return securityManageVo;
    }

    /**
     * 用户注册代码
     * 需要将用户生成的appkey等信息返回给前台
     * copy左岳的新增用户的代码,改成服务数据汇总平台注册的代码
     *
     * @param userManageVo 用户信息
     * @return
     * @author xutaog 2016年4月7日
     */
    public SecurityManageVo registerUser(UserManageVo userManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        String userName = userManageVo.getUserName();
        // 验证用户是否存在(查询数据库)
        securityManageVo = checkUserIsExist(userName);
        if (SecurityManageVo.SUCCESS_CODE.equals(securityManageVo.getResultCode())) {
            // 生成salt
            String encryptSalt = userName + UUID.randomUUID().toString();
            // 用户key
            String appKey = UUID.randomUUID().toString();
            // 验证通过后将前端传过来的密码进行解密
            String decryptPass = new String(Base64EncodeUtils.decode(userManageVo.getUserPass()));
            if (StringUtils.isNotBlank(decryptPass)) {
                // 再将密码加密
                String encryptPwd = AesUtils.aecEncryptBySalt(decryptPass, encryptSalt);
                if (StringUtils.isNotBlank(encryptPwd)) {
                    UserManagePo userManagePo = new UserManagePo();
                    // 复制属性
                    BeanUtils.copyProperties(userManageVo, userManagePo);
                    // 密码放进authUserPo
                    userManagePo.setUserPassWord(encryptPwd);
                    // 设置salt
                    userManagePo.setEncryptSalt(encryptSalt);
                    // 设置用户key
                    userManagePo.setAppKey(appKey);
                    // 设置可用
                    userManagePo.setIsDeleted(0);
                    // 2外网标识
                    userManagePo.setUserChannel(2);
                    // 插入条数
                    int size = userManageMapper.createUser(userManagePo);
                    if (size == 0) {
                        securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                        securityManageVo.setResultInfo(UserManageVo.CREATE_USER_ERROR_MSG);
                        log.error("数据汇总-注册用户时,数据库操作失败");
                    } else {
                        securityManageVo.setObject(new UserManageVo(appKey));
                        // 入库成功也要放入缓存
                        long id = userManagePo.getId();
                        UsersCachePo usersCachePo = new UsersCachePo();
                        usersCachePo.setId(id);
                        usersCachePo.setAppKey(appKey);
                        usersCachePo.setUserName(userName);
                        usersCachePo.setUserChannel(2);
                        RedisUtil.setObject(appKey, usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
                        List<UsersCachePo> datas = new ArrayList<UsersCachePo>();
                        datas.add(usersCachePo);
                        securityManageVo.setDatas(datas);
                        securityManageVo.setResultInfo("用户" + userManagePo.getNickName() + "您好，您已成功注册账号！您的服务访问密钥："
                                + appKey + ",请妥善保管！");
                    }
                } else {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(UserManageVo.CREATE_USER_ERROR_MSG);
                    log.error("数据汇总-注册用户时,数据库操作失败,密码加密失败，加密前的密码为：" + decryptPass + "，加密后的密码为：" + encryptPwd);
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.CREATE_USER_ERROR_MSG);
                log.error("数据汇总-注册用户时,数据库操作失败，解密前的密码为：" + userManageVo.getUserPass() + "，解密后的密码为：" + decryptPass);
            }
        }
        return securityManageVo;
    }

    /**
     * 检查用户名是否存在
     *
     * @param userName
     * @return
     * @author zuoyue 2016年3月10日
     */
    private SecurityManageVo checkUserIsExist(String userName) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        Map<String, String> map = userManageMapper.queryUserIsExist(userName);
        String userExistCount = map.get("USER_EXIST_COUNT");
        if (!"0".equals(userExistCount)) {
            // 用户名已经存在
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.USER_IS_EXIST_MSG);
        }
        return securityManageVo;
    }

    /**
     * 新增或编辑用户时的验证
     *
     * @param userManageVo
     * @return
     * @author zuoyue 2016年3月10日
     */
    public SecurityManageVo validateCreateUser(UserManageVo userManageVo, String operType) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // 名称的数组
        String[] paramNameArr = { "用户名", "昵称", "密码", "确认密码", "用户邮箱", "联系电话", "备注" };
        // 错误原因数组
        String[] errorMsgArr = { "验证通过", "请输入：", "格式错误，请检查", "不能超过%s个字符" };
        // 值的数组
        String[] paramValueArr = { userManageVo.getUserName(), userManageVo.getNickName(), userManageVo.getUserPass(),
                userManageVo.getRePassword(), userManageVo.getUserEmail(), userManageVo.getUserMobile(),
                userManageVo.getUserRemark() };
        // 是否验证空串
        int[] isValidateNullArr = null;
        if ("create".equalsIgnoreCase(operType)) {
            isValidateNullArr = new int[] { 1, 0, 1, 1, 0, 0, 0 };
        } else if ("modify".equalsIgnoreCase(operType)) {
            isValidateNullArr = new int[] { 1, 0, 0, 0, 0, 0, 0 };
        } else {

        }
        // 是否验证格式
        Pattern[] isValidateFormatArr = { null, null, null, null, RegexUtils.REGEX_EMAIL, RegexUtils.REGEX_PHONE, null };
        // 是否验证长度
        int[] isValidateLengthArr = { 120, 120, 1000, 1000, 64, 32, 512 };
        breakCycle:
        for (int i = 0; i < paramValueArr.length; i++) {
            // 字段验证结果
            int flag = BusinessUtils.validateParam(isValidateNullArr[i], isValidateFormatArr[i],
                    isValidateLengthArr[i], paramValueArr[i]);
            switch (flag) {
                case 1:
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(errorMsgArr[flag] + paramNameArr[i]);
                    break breakCycle;
                case 2:
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(paramNameArr[i] + errorMsgArr[flag]);
                    break breakCycle;
                case 3:
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(String.format(paramNameArr[i] + errorMsgArr[flag],
                            isValidateLengthArr[i]));
                    break breakCycle;
                default:
                    break;
            }
        }
        // 没有发生异常
        if (SecurityManageVo.SUCCESS_CODE.equals(securityManageVo.getResultCode())) {
            // 再进行业务验证
            if (!userManageVo.getUserPass().equals(userManageVo.getRePassword())) {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.PASSWORD_NOT_SAME_ERROR_MSG);
            }
        }
        return securityManageVo;
    }

    /**
     * 字符串删掉指定的字符
     *
     * @param originalStrs
     * @param id
     * @return
     * @author zuoyue 2016年4月8日
     */
    private String analysisStr(String originalStrs, long id) {
        String currentStrs = "";
        if (StringUtils.isNotBlank(currentStrs)) {
            for (String originalStr : originalStrs.split(",")) {
                if (!originalStr.equals(String.valueOf(id))) {
                    currentStrs += "," + originalStr;
                }
            }
            currentStrs = currentStrs.substring(1);
        }
        return currentStrs;
    }

    public ServeUser findByPk(long id) {
        return this.userManageMapper.selectByPrimaryKey(id);
    }

    @SuppressWarnings("unchecked")
    public CatalogBaseVo queryCatalogAuths(Integer userId, Integer parentId) {
        CataLogQueryVo catalogVo = new CataLogQueryVo();
        catalogVo.setPid(parentId);
        catalogVo.setIsDeleted(0);

        List<CatalogAuthPo> datas = new ArrayList<CatalogAuthPo>();
        List<CatalogPo> catalogs = (List<CatalogPo>) catalogService.queryCatalogTree(catalogVo).getDatas();
        List<ServeUserAuthority> usersCachePoList = userManageMapper.queryUserAuths(userId);
        for (CatalogPo po : catalogs) {
            CatalogAuthPo auth = new CatalogAuthPo();
            BeanUtils.copyProperties(po, auth);
            setPermission(catalogs, usersCachePoList, auth, auth.getId());
            datas.add(auth);
        }
        CatalogBaseVo vo = new CatalogBaseVo();
        vo.setDatas(datas);
        return vo;
    }

    // 设置当前po的访问调用权限
    private void setPermission(List<CatalogPo> catalogs, List<ServeUserAuthority> auths, CatalogAuthPo po, Integer poId) {
        if (auths.isEmpty()) {
            po.setAccessPermission(false);
            po.setViewPermission(false);
            return;
        }
        po.setAccessPermission(getAccessPermission(poId, auths, catalogs));
        po.setViewPermission(getViewPermission(poId, auths, catalogs));
    }

    /**
     * 递归判断用户当前目录权限
     *
     * @param poId 当前目录id
     * @param auths 用户权限树
     * @param catalogs 目录树
     * @return
     * @author zhongdt 2016年5月11日
     */
    private boolean getAccessPermission(Integer poId, List<ServeUserAuthority> auths, List<CatalogPo> catalogs) {
        boolean isVerify = false;
        // 当前平行级别节点
        for (ServeUserAuthority auth : auths) {
            // 得到当前父节点
            CatalogPo parentPo = getPoById(catalogs, poId);
            while (parentPo != null) {
                // 如果当前id == 用户权限id
                if (parentPo.getId().intValue() == (int) auth.getServiceId()) {
                    // 如果当前id=权限表id，直接返回用户权限
                    if (auth.getServiceId() == poId) {
                        return auth.getAccessPermission() == 1 ? true : false;
                    } else {
                        // 否则得到父节点状态，但是不一定，便利数据看最后的结果
                        isVerify = auth.getAccessPermission() == 1 ? true : false;
                    }

                }
                // 层层往上
                parentPo = getPoById(catalogs, parentPo.getPid());
            }
        }
        return isVerify;
    }

    private boolean getViewPermission(Integer poId, List<ServeUserAuthority> auths, List<CatalogPo> catalogs) {
        boolean isVerify = false;
        // 当前平行级别节点
        for (ServeUserAuthority auth : auths) {
            CatalogPo parentPo = getPoById(catalogs, poId);
            while (parentPo != null) {
                if (parentPo.getId().intValue() == (int) auth.getServiceId()) {
                    if (auth.getServiceId() == poId) {
                        return auth.getViewPermission() == 1 ? true : false;
                    } else {
                        isVerify = auth.getViewPermission() == 1 ? true : false;
                    }
                }
                parentPo = getPoById(catalogs, parentPo.getPid());
            }
        }
        return isVerify;
    }

    private CatalogPo getPoById(List<CatalogPo> catalogs, Integer poId) {
        if (poId == null) {
            return null;
        }
        for (CatalogPo catalog : catalogs) {
            if (catalog.getId().intValue() == poId.intValue()) {
                return catalog;
            }
        }
        return null;
    }

}
