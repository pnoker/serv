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
package com.dc.city.controller.securitymanage.blacklist;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import com.dc.city.common.utils.RegexUtils;
import com.dc.city.listener.securitymanage.BlackListManager;
import com.dc.city.service.securitymanage.BlackListManageService;
import com.dc.city.vo.BaseVo;
import com.dc.city.vo.PageVo;
import com.dc.city.vo.securitymanage.SecurityManageVo;
import com.dc.city.vo.securitymanage.blacklist.BlackListManageVo;

/**
 * 黑名单管理Controller
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月7日 下午5:01:30
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/blacklistmanage/v1")
@Produces({ "application/xml", "application/json" })
public class BlackListManageController {

    private static Log log = LogFactory.getLog("BlackListManageController");

    @Resource
    private BlackListManageService blackListManageService;

    /**
     * 查询黑名单
     *
     * @param blackListManageQueryVo
     * @return
     * @author zuoyue 2016年3月8日
     */
    @GET
    @Path("/queryblacklist")
    public SecurityManageVo queryBlackList(@BeanParam BlackListManageVo blackListManageQueryVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (blackListManageQueryVo != null) {
            // 每页记录数
            int pageSize = blackListManageQueryVo.getLength();
            pageSize = (pageSize == 0) ? PageVo.DEFAULT_PAGE_SIZE : pageSize;
            // 当前页码
            int start = blackListManageQueryVo.getStart();
            start = (start == 0) ? PageVo.DEFAULT_CURRENT_PAGE : ++start;
            // 起始行数
            blackListManageQueryVo.setBeginRowNum(start);
            // 截止行数
            blackListManageQueryVo.setEndRowNum(start + pageSize);
            // 开始查询
            securityManageVo = blackListManageService.queryBlackList(blackListManageQueryVo);
            // 没有发生异常
            if (SecurityManageVo.SUCCESS_CODE.equals(securityManageVo.getResultCode())) {
                // 当前页码
                // securityManageVo.setCurrentPage(String.valueOf(currentPage));
                // 每页条数
                securityManageVo.setPageSize(String.valueOf(pageSize));
                // 总页数
                //securityManageVo.setPageCount(String.valueOf((int) Math.ceil(securityManageVo.getDatas().size() / 1.0/ pageSize / 1.0)));
            }
        } else {
            securityManageVo.setResultCode(BaseVo.ERROR_CODE);
            securityManageVo.setResultInfo(SecurityManageVo.QUERY_BLACK_LIST_ERROR_MSG);
            log.error("黑名单管理接口-查询黑名单时,blackListManageQueryVo为空，无法获得参数,blackListManageQueryVo:" + blackListManageQueryVo);
        }
        return securityManageVo;
    }

    /**
     * 新增黑名单
     *
     * @param blackListManageVo
     * @return
     * @author zuoyue 2016年3月8日
     */
    @POST
    @Path("/createblacklist")
    public SecurityManageVo createBlackList(@BeanParam BlackListManageVo blackListManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (blackListManageVo != null) {
            // 验证
            securityManageVo = validateCreateBlackList(blackListManageVo);
            // 没有发生异常
            if (SecurityManageVo.SUCCESS_CODE.equals(securityManageVo.getResultCode())) {
                securityManageVo = blackListManageService.createBlackList(blackListManageVo);
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(SecurityManageVo.CREATE_BLACK_LIST_ERROR_MSG);
            log.error("黑名单管理接口-新增黑名单时,blackListManageVo为空，无法获得参数,blackListManageVo:" + blackListManageVo);
        }
        return securityManageVo;
    }

    /**
     * 删除黑名单
     *
     * @param blackListManageVo
     * @return
     * @author zuoyue 2016年3月8日
     */
    @POST
    @Path("/removeblacklist")
    public SecurityManageVo removeBlackList(@BeanParam BlackListManageVo blackListManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        if (blackListManageVo != null) {
            if (StringUtils.isNotBlank(blackListManageVo.getBlackListId())) {
                securityManageVo = blackListManageService.removeBlackList(blackListManageVo.getBlackListId());
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(SecurityManageVo.REMOVE_BLACK_LIST_ERROR_MSG);
                log.error("黑名单管理接口-删除黑名单时,Id为:" + blackListManageVo.getBlackListId());
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(SecurityManageVo.REMOVE_BLACK_LIST_ERROR_MSG);
            log.error("黑名单管理接口-删除黑名单时,blackListManageVo为空，无法获得参数,blackListManageVo:" + blackListManageVo);
        }
        return securityManageVo;
    }

    /**
     * 新增黑名单的字段验证
     *
     * @param blackListManageVo
     * @return
     * @author zuoyue 2016年3月8日
     */
    private SecurityManageVo validateCreateBlackList(BlackListManageVo blackListManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // IP地址
        String ipAddress = blackListManageVo.getIpAddress();
        // 验证不为空
        if (StringUtils.isNotBlank(ipAddress)) {
            // 验证IP格式正确性
            if (RegexUtils.REGEX_IP_ADDRESS.matcher(ipAddress).matches()) {
                // 验证IP没有重复
                if (BlackListManager.getInstance().findBlackListMap().containsValue(ipAddress)) {
                    securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                    securityManageVo.setResultInfo(SecurityManageVo.IP_EXIST_ERROR_MSG);
                    return securityManageVo;
                }
            } else {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(SecurityManageVo.IP_FORM_ERROR_MSG);
                return securityManageVo;
            }
        } else {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(SecurityManageVo.IP_NOT_NULL_MSG);
            return securityManageVo;
        }
        // 备注
        String banReason = blackListManageVo.getBanReason();
        if (StringUtils.isNotBlank(banReason)) {
            if (getWordLength(banReason, null) > 120) {
                securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
                securityManageVo.setResultInfo(SecurityManageVo.BANREASON_ERROR_MSG);
                return securityManageVo;
            }
        }
        return securityManageVo;
    }

    /**
     * 字符长度
     *
     * @param value
     * @param code
     * @return
     * @author zuoyue 2016年3月8日
     */
    private int getWordLength(String value, String code) {
        int length = 10000;
        try {
            length = value.getBytes(StringUtils.isNotBlank(code) ? code : "UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return length;
    }

}
