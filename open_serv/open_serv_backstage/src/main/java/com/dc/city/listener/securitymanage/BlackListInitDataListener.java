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
package com.dc.city.listener.securitymanage;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dc.city.dao.master.securitymanage.blacklist.BlackListManageMapper;
import com.dc.city.pojo.securitymanage.blacklist.BlackListManagePo;

/**
 * 查询当前的黑名单放入缓存
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月8日 下午5:39:47
 *          Copyright 2016 by DigitalChina
 */
public class BlackListInitDataListener {

    private static Log log = LogFactory.getLog("BlackListInitDataListener");

    @Resource
    private BlackListManageMapper blackListManageMapper;

    /**
     * 查询黑名单放入缓存
     *
     * @author zuoyue 2016年3月8日
     */
    @PostConstruct
    public void init() {
        int initParamListSize = 0;
        // 查询数据库后的结果
        List<BlackListManagePo> blackListManagePoList = blackListManageMapper.queryBlackListAll();
        if (blackListManagePoList != null && blackListManagePoList.size() > 0) {
            initParamListSize = blackListManagePoList.size();
            for (BlackListManagePo blackListManagePo : blackListManagePoList) {
                if(blackListManagePo != null){
                    String ipAddress = blackListManagePo.getIpAddress();
                    if (StringUtils.isNotBlank(ipAddress) && blackListManagePo.getId() != 0) {
                        BlackListManager.getInstance().addBlackListToMap(ipAddress.hashCode(), ipAddress);
                    }
                } 
            }
            log.info("**********当前数据库黑名单条数为***************：" + initParamListSize);
        } else {
            log.info("当前数据库没有黑名单");
        }
    }

}
