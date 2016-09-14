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
package com.dc.city.controller.securitymanage.user;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.dc.city.common.utils.JsonUtil;
import com.dc.city.controller.catalogmanage.CatalogController;
import com.dc.city.controller.config.ServeConfigQueryController;
import com.dc.city.domain.securitymanage.user.ServeUser;
import com.dc.city.service.securitymanage.UserManageService;
import com.dc.city.vo.PageVo;
import com.dc.city.vo.catalog.CataLogQueryVo;
import com.dc.city.vo.catalog.CatalogBaseVo;
import com.dc.city.vo.config.ServeConfigQueryVo;
import com.dc.city.vo.config.ServeConfigVo;
import com.dc.city.vo.securitymanage.SecurityManageVo;
import com.dc.city.vo.securitymanage.user.ServeUserAuthorityVo;
import com.dc.city.vo.securitymanage.user.UserManageVo;

/**
 * 用户管理Controller
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月9日 下午2:21:46
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/usermanage/v1")
@Produces({ "application/xml", "application/json" })
public class UserManageController {

    private static Log log = LogFactory.getLog("UserManageController");

    @Resource
    private UserManageService userManageService;
    @Resource
    private CatalogController catalogController;
    @Resource
    private ServeConfigQueryController serviceListController;

    /**
     * 通过appKey查询用户
     *
     * @param appKey
     * @return
     * @author zuoyue 2016年3月16日
     */
    @GET
    @Path("/queryuserbyappkey")
    public SecurityManageVo queryUserByAppKey(@QueryParam("appKey") String appKey) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (StringUtils.isNotBlank(appKey)) {
            securityManageVo = userManageService.queryUserByAppKey(appKey);
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.QUERY_USER_ERROR_MSG);
            log.error("用户管理接口-通过appKey查询用户时,appKey为空,appKey:" + appKey);
        }
        return securityManageVo;
    }

    /**
     * 查询用户是否具有该服务的查看访问权限
     *
     * @param appKey
     * @param serviceCode
     * @return
     * @author zuoyue 2016年3月13日
     */
    @GET
    @Path("/queryserviceauth")
    public SecurityManageVo queryServiceAuth(@QueryParam("appKey") String appKey,
            @QueryParam("serviceCode") String serviceCode) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (StringUtils.isNotBlank(appKey) && StringUtils.isNotBlank(serviceCode)) {
            securityManageVo = userManageService.queryServiceAuth(appKey, serviceCode);
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.QUERY_USER_AUTH_ERROR_MSG);
            log.error("用户管理接口-查询用户是否具有该服务的查看访问权限时,appKey或serviceCode为空,appKey:" + appKey + ",serviceCode:"
                    + serviceCode);
        }
        return securityManageVo;
    }

    /**
     * 查询用户
     *
     * @param userManageVo
     * @return
     * @author zuoyue 2016年3月10日
     */
    @GET
    @Path("/queryuser")
    public SecurityManageVo queryUser(@BeanParam UserManageVo userManageQueryVo,@QueryParam("userName")String userName) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (userManageQueryVo != null) {
            userManageQueryVo.setUserName(userName);
            // 每页记录数
            int pageSize = userManageQueryVo.getLength();
            pageSize = (pageSize == 0) ? PageVo.DEFAULT_PAGE_SIZE : pageSize;
            // 当前页码
            int start = userManageQueryVo.getStart();
            //start = (start == 0) ? PageVo.DEFAULT_CURRENT_PAGE : start;
            // 起始行数
            userManageQueryVo.setBeginRowNum(start);
            // 截止行数
            userManageQueryVo.setEndRowNum(start + pageSize);
            // 开始查询
            securityManageVo = userManageService.queryUser(userManageQueryVo);
            // 没有发生异常
            if (SecurityManageVo.SUCCESS_CODE.equals(securityManageVo.getResultCode())) {
                // 当前页码
                // // securityManageVo.setCurrentPage(String.valueOf(currentPage));
                // // 每页条数
                // securityManageVo.setPageSize(String.valueOf(pageSize));
                // // 总页数
                // securityManageVo.setPageCount(String.valueOf((int)
                // Math.ceil(securityManageVo.getDatas().size() / 1.0
                // / pageSize / 1.0)));
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.QUERY_USER_ERROR_MSG);
            log.error("用户管理接口-查询用户时,userManageVo为空，无法获得参数,userManageQueryVo:" + userManageQueryVo);
        }
        return securityManageVo;
    }

    /**
     * 删除用户
     *
     * @param userManageVo
     * @return
     * @author zuoyue 2016年3月10日
     */
    @POST
    @Path("/removeuser")
    public SecurityManageVo removeUser(@BeanParam UserManageVo userManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (userManageVo != null) {
            if (userManageVo.getId() > 0 && StringUtils.isNotBlank(userManageVo.getAppKey())) {
                securityManageVo = userManageService.removeUser(userManageVo.getId(), userManageVo.getAppKey());
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.REMOVE_USER_ERROR_MSG);
                log.error("用户管理接口-删除用户时,Id或AppKey为空。id:" + userManageVo.getId() + ",appKey:" + userManageVo.getAppKey());
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.REMOVE_USER_ERROR_MSG);
            log.error("用户管理接口-删除用户时,userManageVo为空，无法获得参数,userManageVo:" + userManageVo);
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
    @POST
    @Path("/createuser")
    public SecurityManageVo createUser(@BeanParam UserManageVo userManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (userManageVo != null) {
            // 验证
            securityManageVo = userManageService.validateCreateUser(userManageVo, "create");
            // 没有发生异常
            if (SecurityManageVo.SUCCESS_CODE.equals(securityManageVo.getResultCode())) {
                securityManageVo = userManageService.createUser(userManageVo);
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.CREATE_USER_ERROR_MSG);
            log.error("用户管理接口-新增用户时,userManageVo为空，无法获得参数,userManageVo:" + userManageVo);
        }
        return securityManageVo;
    }

    /**
     * 编辑用户
     *
     * @param userManageVo
     *            如果不修改密码，密码字段传空串
     * @return
     * @author zuoyue 2016年3月9日
     */
    @POST
    @Path("/modifyuser")
    public SecurityManageVo modifyUser(@BeanParam UserManageVo userManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (userManageVo != null) {
            if (userManageVo.getId() > 0) {
                // 首先判断用户是否存在
                ServeUser origUser = userManageService.findByPk(userManageVo.getId());
                if (origUser == null) {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(UserManageVo.QUERY_USER_NOT_FOUND_MSG);
                    return securityManageVo;
                }
                // 如果密码为空，则说明不修改密码
                if (StringUtils.isBlank(userManageVo.getUserPass())||("******").equals(userManageVo.getUserPass())) {
                        userManageVo.setUserPass("");
                        userManageVo.setRePassword("");  
                }
                // 为了兼容modifyUser ，需要自动把密码从用户中带出来
                userManageVo.setAppKey(origUser.getAppKey());
                userManageVo.setOriginalKey(origUser.getAppKey());

                // 验证
                securityManageVo = userManageService.validateCreateUser(userManageVo, "modify");
                // 没有发生异常
                if (SecurityManageVo.SUCCESS_CODE.equals(securityManageVo.getResultCode())) {
                    securityManageVo = userManageService.modifyUser(userManageVo);
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.MODIFY_USER_ERROR_MSG);
                log.error("用户管理接口-修改用户时,id为空或appKey获取失败。currentAppKey：" + userManageVo.getAppKey() + "，originalKey："
                        + userManageVo.getOriginalKey() + ",id:" + userManageVo.getId());
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.MODIFY_USER_ERROR_MSG);
            log.error("用户管理接口-修改用户时,userManageVo为空，无法获得参数,userManageVo:" + userManageVo);
        }
        return securityManageVo;
    }

    /**
     * 编辑用户
     *
     * @param userManageVo
     *            如果不修改密码，密码字段传空串
     * @return
     * @author zuoyue 2016年3月9日
     */
    @POST
    @Path("/modifyuserkey")
    public SecurityManageVo modifyUserKey(@BeanParam UserManageVo userManageVo) {
        if (userManageVo.getId() <= 0) {
            return new SecurityManageVo();
        }
        SecurityManageVo vo = this.userManageService.modifyBuildAppKey(userManageVo);
        vo.setResultInfo(userManageVo.getAppKey());
        return vo;
    }

    /**
     * 用户服务接口与目录授权
     *
     * @param userManageVo
     * @return
     * @author zuoyue 2016年3月10日
     */
    @POST
    @Path("/createuserauthorization")
    public SecurityManageVo createUserAuthorization(@BeanParam UserManageVo userManageVo,
            @FormParam("serviceId") Integer serviceId) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (userManageVo != null) {
            if (userManageVo.getId() > 0 && StringUtils.isNotBlank(userManageVo.getAppKey())) {
                // json字符串,格式："[{\"userId\": 11,\"serviceId\": 2,\"viewPermission\": 1,\"accessPermission\": 1}]";
                String serveUserAuthorityVoListJson = userManageVo.getServeUserAuthorityVoListJson();
                // 如果serveUserAuthorityVoListJson不为空，则表示勾选了
                if (StringUtils.isNotBlank(serveUserAuthorityVoListJson)) {
                    // json to list
                    List<?> list = JsonUtil.jsonToList(serveUserAuthorityVoListJson, ServeUserAuthorityVo.class);
                    if (list != null && list.size() > 0) {
                        securityManageVo = userManageService.createUserAuthorization(userManageVo.getId(),
                                userManageVo.getAppKey(), serviceId, list);
                    } else {
                        securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                        securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
                        log.error("用户管理接口-用户服务接口与目录授权时,解析json数据失败,serveUserAuthorityVoListJson:"
                                + serveUserAuthorityVoListJson);
                    }
                } else {
                    /**
                     * 如果serveUserAuthorityVoListJson为空，则表示
                     * 1.第一次赋权，但是没有勾选就点击了保存
                     * 2.已经付了权限，但是想撤销所赋予的权限，然后点保存
                     */
                    securityManageVo = userManageService.removeUserAuthorization(userManageVo.getId(),
                            userManageVo.getAppKey());
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
                log.error("用户管理接口-用户服务接口与目录授权时,Id或AppKey为空。id:" + userManageVo.getId() + ",appKey:"
                        + userManageVo.getAppKey());
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
            log.error("用户管理接口-用户服务接口与目录授权时,userManageVo为空，无法获得参数,userManageVo:" + userManageVo);
        }
        return securityManageVo;
    }

    /**
     * 通过serviceId授权
     *
     * @param userManageVo
     * @return
     * @author zuoyue 2016年3月10日
     */
    @POST
    @Path("/createuserauthorizationbyserviceid")
    public SecurityManageVo createUserAuthorizationByServiceId(@BeanParam UserManageVo userManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (userManageVo != null) {
            if (StringUtils.isNotBlank(userManageVo.getServeUserAuthorityVoListJson())) {
                // json字符串,格式："[{\"userId\": 11,\"serviceId\": 2,\"viewPermission\": 1,\"accessPermission\": 1}]";
                String serveUserAuthorityVoListJson = userManageVo.getServeUserAuthorityVoListJson();
                // json to list
                List<?> list = JsonUtil.jsonToList(serveUserAuthorityVoListJson, ServeUserAuthorityVo.class);
                if (list != null && list.size() > 0) {
                    long serviceId = 0;
                    String userIds = "-1";
                    long userId = 0;
                    //遍历
                    for (Object obj : list) {
                        ServeUserAuthorityVo serveUserAuthorityVo = (ServeUserAuthorityVo) obj;
                        serviceId = serveUserAuthorityVo.getServiceId();
                        userId = serveUserAuthorityVo.getUserId();
                        //如果有userId,serviceId
                        if (serviceId > 0 && userId > 0) {
                            userIds += "," + userId;
                        } else {
                            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                            securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
                            log.error("用户管理接口-通过serviceId授权时,获取serviceId或userId失败,serviceId:" + serviceId + ",userId:"
                                    + userId);
                            return securityManageVo;
                        }
                    }
                    // 成功获取
                    securityManageVo = userManageService.createUserAuthorizationByServiceId(userIds.replace("-1,", ""),
                            serviceId, list);
                } else {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
                    log.error("用户管理接口-通过serviceId授权时,解析json数据失败,serveUserAuthorityVoListJson:"
                            + serveUserAuthorityVoListJson);
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
                log.error("用户管理接口-通过serviceId授权时,Json为空。Json:" + userManageVo.getServeUserAuthorityVoListJson());
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(UserManageVo.AUTH_ERROR_MSG);
            log.error("用户管理接口-通过serviceId授权时,userManageVo为空，无法获得参数,userManageVo:" + userManageVo);
        }
        return securityManageVo;
    }

    /**
     * 
     * 用户模块查询服务目录树
     *
     * @param vo
     * @return
     * @author zhongdt 2016年5月5日
     */
    @GET
    @Path("/querycatalogtree")
    public CatalogBaseVo cataLogTree(@BeanParam CataLogQueryVo vo) {
        return catalogController.queryCatalogTree(vo);
    }

    /**
     * 
     * 用户模块查询服务列表
     *
     * @param queryVo
     * @param key 查询时为了避免中文乱码把这几个字段单独处理
     * @param serviceCode
     * @param serviceName
     * @return
     * @author zhongdt 2016年5月5日
     */
    @GET
    @Path("/servicelist")
    public ServeConfigVo serviceList(@BeanParam ServeConfigQueryVo queryVo,@QueryParam("key")String key,@QueryParam("serviceCode")String serviceCode, @QueryParam("serviceName")String serviceName) {
        return serviceListController.queryServiceList(queryVo,key,serviceCode,serviceName);
    }

    /**
     * 
     * 查询用户目录权限
     *
     * @param userId 用户id
     * @param parentId 父节点id
     * @return
     * @author zhongdt 2016年5月5日
     */
    @GET
    @Path("/querycatalogauth")
    public CatalogBaseVo catalogAuths(@FormParam("userId") Integer userId, @FormParam("parentId") Integer parentId) {
        return userManageService.queryCatalogAuths(userId, parentId);
    }

}
