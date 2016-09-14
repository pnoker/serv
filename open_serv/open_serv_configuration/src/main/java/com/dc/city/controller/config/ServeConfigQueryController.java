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

import org.springframework.stereotype.Controller;

import com.dc.city.common.vo.FuzzyQueryVo;
import com.dc.city.controller.catalogmanage.CatalogController;
import com.dc.city.controller.securitymanage.user.UserManageController;
import com.dc.city.domain.config.ServeModifyLog;
import com.dc.city.service.config.ConfigerService;
import com.dc.city.vo.BaseVo;
import com.dc.city.vo.catalog.CataLogQueryVo;
import com.dc.city.vo.catalog.CatalogBaseVo;
import com.dc.city.vo.config.ConfigerVo;
import com.dc.city.vo.config.ServeAuthUserVo;
import com.dc.city.vo.config.ServeConfigQueryVo;
import com.dc.city.vo.config.ServeConfigVo;
import com.dc.city.vo.securitymanage.SecurityManageVo;
import com.dc.city.vo.securitymanage.user.UserManageVo;

/**
 * 服务列表查询controller
 *
 * @author zhongdt
 * @version V1.0 创建时间：2import org.springframework.stereotype.Controller;
 *          016年3月24日 下午4:42:20
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/configer/v1/servicelist/v1")
@Produces({ "application/xml", "application/json" })
public class ServeConfigQueryController {
    @Resource
    ConfigerService configService;
    @Resource
    private CatalogController catalogController;
    @Resource
    private UserManageController userManageController;
    @Resource
    private ConfigerController configerController;

    /**
     * 服务列表查询 获取服务列表，key，code，name由于get方法时取不到中文，单独通过queryparam穿进去再set到bean中
     * 
     * @param queryVo
     * @param key 关键字
     * @param serviceCode 服务代码
     * @param serviceName 服务名称
     * @return
     * @author zhongdt 2016年5月6日
     */
    @GET
    @Path("/querylist")
    public ServeConfigVo queryServiceList(@BeanParam ServeConfigQueryVo queryVo, @QueryParam("query") String key,
            @QueryParam("serviceCode") String serviceCode, @QueryParam("serviceName") String serviceName) {
        // 封装查询对象
        queryVo.setKey(key);
        queryVo.setServiceCode(serviceCode);
        queryVo.setServiceName(serviceName);

        return this.configService.queryPage(queryVo);

    }

    /**
     * 根据字段匹配词语
     *
     * @param query 模糊查询字段
     * @param matchNum 返回条数
     * @return
     * @author ligen 2016年6月23日
     */
    @GET
    @Path("/matchname")
    public FuzzyQueryVo matchName(@QueryParam("query") String query, @QueryParam("matchNum") String matchNum) {
        return configService.fuzzyQuery(query, matchNum, "SERVE_CONFIG",
                new String[] { "SERVICE_CODE", "SERVICE_NAME" });
    }

    /**
     * 查询三类服务的已发布条数 和 其他状态条数
     *
     * @return
     * @author chenzpa 2016年8月15日
     */
    @GET
    @Path("/querylistnum")
    public ServeConfigVo queryServiceListNum() {

        return this.configService.queryServiceListNum();

    }

    /**
     * 服务目录查询
     * 详情参考服务目录controller
     * 
     * @param vo
     * @return
     * @author zhongdt 2016年5月6日
     */
    @GET
    @Path("/querycatalogtree")
    public CatalogBaseVo cataLogTree(@BeanParam CataLogQueryVo vo) {
        return catalogController.queryCatalogTree(vo);
    }

    /**
     * 用户信息查询
     * 详情参考用户controller
     * 
     * @param userManageQueryVo
     * @param userName 避免乱码单独列出来然后在set到userManageQueryVo中
     * @return
     * @author zhongdt 2016年5月6日
     */
    @GET
    @Path("/queryuser")
    public SecurityManageVo queryUser(@BeanParam UserManageVo userManageQueryVo, @QueryParam("userName") String userName) {
        return userManageController.queryUser(userManageQueryVo, userName);
    }

    @GET
    @Path("/querygisconfig")
    public ConfigerVo queryGISConfig(@FormParam("gisConfigId") String gisConfigId) {
        return configerController.queryGISConfig(gisConfigId);
    }

    /**
     * 查询当前config下，有权限的用户列表信息，详见用户controller
     *
     * @param configerId
     * @return
     * @author zhongdt 2016年5月6日
     */
    @GET
    @Path("/queryconfigerAuthUsers")
    public ServeAuthUserVo queryConfigerAuthUsers(@QueryParam("configerId") Long configerId) {
        return configerController.queryConfigerAuthUsers(configerId);
    }

    /**
     * 内外部信息发布及保存功能
     *
     * @param configerId 服务id
     * @param demoUrl 请求示例url
     * @param publishRemark 发布备注
     * @param jsonExample json示例
     * @param xmlExample xml 示例
     * @param verifyAccess 是否验证访问权限
     * @param verifyView 是否验证查询权限
     * @param verifyIp 是否验证ip
     * @param otherInfo 其他信息
     * @param serviceStatus 服务状态
     * @param viewUsers 能查看用户id串“,”分隔
     * @param accessUsers 能调用用户id串
     * @param ipAddresses 白名单ip地址串，","分隔
     * @param editRemark 服务备注
     * @return
     * @author zhongdt 2016年5月6日
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
        return configerController.configerPublish(configerId, demoUrl, publishRemark, jsonExample, xmlExample,
                verifyAccess, verifyView, verifyIp, otherInfo, serviceStatus, viewUsers, accessUsers, ipAddresses,
                editRemark);
    }

    /**
     * 取消发布，内部，外部服务
     *
     * @param configId
     * @return
     * @author zhongdt 2016年5月6日
     */
    @POST
    @Path("/cancelpublish")
    public BaseVo configerPublish(@FormParam("configId") Long configId) {
        return configerController.cancelPublish(configId);
    }

    @POST
    @Path("/deletegisconfig")
    public ConfigerVo deleteGISConfig(@FormParam("gisConfigId") String gisConfigId) {
        return configerController.deleteGISConfig(gisConfigId);
    }

    @GET
    @Path("/querylog")
    public ConfigerVo queryEditLog(@FormParam("configerId") String configId) {
        List<ServeModifyLog> logs = configService.queryUpdateRecode(configId);
        ConfigerVo vo = new ConfigerVo();
        vo.setDatas(logs);
        return vo;
    }

}
