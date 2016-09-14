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

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dc.city.common.utils.StringUtils;
import com.dc.city.dao.master.securitymanage.blacklist.BlackListManageMapper;
import com.dc.city.domain.securitymanage.blacklist.ServeBlackList;
import com.dc.city.listener.securitymanage.BlackListManager;
import com.dc.city.pojo.securitymanage.blacklist.BlackListManagePo;
import com.dc.city.service.cache.PubCacheService;
import com.dc.city.vo.securitymanage.SecurityManageVo;
import com.dc.city.vo.securitymanage.blacklist.BlackListManageVo;

/**
 * 黑名单管理Service
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月7日 下午5:37:35
 *          Copyright 2016 by DigitalChina
 */
@Service
public class BlackListManageService {

    private static Log log = LogFactory.getLog("BlackListManageService");

    @Resource
    private BlackListManageMapper blackListManageMapper;

    // 发布更新请求service
    @Resource
    private PubCacheService cacheService;

    /**
     * 查询黑名单
     *
     * @param blackListManageQueryVo 查询黑名单请求参数封装对象
     * @return 返回安全管理vo对象，里面的datas对象就是黑名单列表信息
     * @author zuoyue 2016年3月8日
     */
    public SecurityManageVo queryBlackList(BlackListManageVo blackListManageQueryVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // copy属性
        BlackListManagePo blackListManageQueryPo = new BlackListManagePo();
        BeanUtils.copyProperties(blackListManageQueryVo, blackListManageQueryPo);

        // 首先查询总条数
        int totalCount = getTotalCount(blackListManageQueryPo);
        securityManageVo.setTotalCount(totalCount + "");
        // 总条数为0 直接返回封装一个空的datas
        if (totalCount == 0) {
            securityManageVo.setDatas(new ArrayList<BlackListManageVo>());
            return securityManageVo;
        }

        // 查询数据库后的结果
        List<BlackListManagePo> blackListManagePoList = blackListManageMapper.queryBlackList(blackListManageQueryPo);
        // 返回给前端的
        List<BlackListManageVo> blackListManageVoList = new ArrayList<BlackListManageVo>();
        for (BlackListManagePo blackListManagePo : blackListManagePoList) {
            if (blackListManagePo != null) {
                // copy属性
                BlackListManageVo blackListManageVo = new BlackListManageVo();
                BeanUtils.copyProperties(blackListManagePo, blackListManageVo);
                blackListManageVoList.add(blackListManageVo);
            }
        }
        securityManageVo.setDatas(blackListManageVoList);
        return securityManageVo;
    }

    /**
     * 新增黑名单
     *
     * @param blackListManageVo
     * @return
     * @author zuoyue 2016年3月8日
     */
    public SecurityManageVo createBlackList(BlackListManageVo blackListManageVo) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // copy属性
        BlackListManagePo blackListManagePo = new BlackListManagePo();
        BeanUtils.copyProperties(blackListManageVo, blackListManagePo);
        // 入库
        int size = blackListManageMapper.createBlackList(blackListManagePo);
        if (size == 0) {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(SecurityManageVo.CREATE_BLACK_LIST_ERROR_MSG);
            log.error("黑名单管理接口-新增黑名单,数据库操作失败");
        } else {
            // 入库成功也要放入缓存

            // 现在本地添加
            BlackListManager.getInstance().addBlackListToMap(blackListManagePo.getIpAddress().hashCode(),
                    blackListManagePo.getIpAddress());
            // 发送消息通知外部系统添加
            cacheService.publishBlackList(PubCacheService.OPERATE_CREATE, blackListManagePo.getIpAddress());
        }
        return securityManageVo;
    }

    /**
     * 删除黑名单
     *
     * @param blackListId
     * @return
     * @author zuoyue 2016年3月8日
     */
    public SecurityManageVo removeBlackList(String blackListId) {
        SecurityManageVo securityManageVo = new SecurityManageVo();
        // 入库成功也要删除缓存,可能多条
        String[] ids = blackListId.split(",");
        for (String idStr : ids) {
            //遍历删除黑名单
            if (StringUtils.isNullOrEmpty(idStr)) {
                continue;
            }
            ServeBlackList black = selectByPrimaryKey(Long.valueOf(idStr));
            if (black == null) {
                continue;
            }
            // 现在本地移除
            BlackListManager.getInstance().removeBlackListFromMap(black.getIpAddress().hashCode());
            //远程删除
            cacheService.publishBlackList(PubCacheService.OPERATE_REMOVE, black.getIpAddress());
        }

        int size = blackListManageMapper.removeBlackList(blackListId.split(","));
        if (size == 0) {
            securityManageVo.setResultCode(SecurityManageVo.ERROR_CODE);
            securityManageVo.setResultInfo(SecurityManageVo.REMOVE_BLACK_LIST_ERROR_MSG);
            log.error("黑名单管理接口-删除黑名单时,数据库操作失败");
        }
        return securityManageVo;
    }

    // 按id查询
    public ServeBlackList selectByPrimaryKey(long id) {
        return this.blackListManageMapper.selectByPrimaryKey(id);
    }

    // 获取总条数
    public int getTotalCount(BlackListManagePo blackListManageQueryPo) {
        // TODO Auto-generated method stub
        return this.blackListManageMapper.getTotalCount(blackListManageQueryPo);
    }

}
