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
package com.dc.city.quartz;

import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.dao.master.securitymanage.user.UserManageMapper;
import com.dc.city.pojo.securitymanage.user.UsersCachePo;

/**
 * 查询用户信息放入redis
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月9日 下午1:54:18
 *          Copyright 2016 by DigitalChina
 */
public class QueryUserInfoQtzJob {

    private static Log log = LogFactory.getLog("QueryUserInfoQtzJob");

    @Resource
    private UserManageMapper userManageMapper;

    public void queryUserInfo() {
        log.info("----------------加载活跃用户信息-------------");
        // 这里只查询活跃用户
        List<UsersCachePo> usersCachePoList = userManageMapper.queryActiveUsers();
        if (usersCachePoList != null && usersCachePoList.size() > 0) {
            for (UsersCachePo usersCachePo : usersCachePoList) {
                if (usersCachePo != null) {
                    String appKey = usersCachePo.getAppKey();
                    RedisUtil.setObject(appKey, usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
                }
            }
        } else {
            log.info("--------------没有查询到活跃用户信息----------------");
        }
    }

}
