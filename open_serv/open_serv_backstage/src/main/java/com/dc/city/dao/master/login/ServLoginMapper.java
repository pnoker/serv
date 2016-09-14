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
package com.dc.city.dao.master.login;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dc.city.domain.dataset.DatasetServ;
import com.dc.city.domain.securitymanage.user.ServeUser;

/**
 * 登陆相关的dao层
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年3月28日 下午3:06:43
 *          Copyright 2016 by DigitalChina
 */
public interface ServLoginMapper {

    /**
     * 根据用户名称获取用户信息
     *
     * @param userName 用户名
     * @return
     * @author xutaog 2016年3月28日
     */
    ServeUser queryServeUserByUserName(@Param("id") Long id, @Param("userName") String userName);

    /**
     * 更新用户的Userkey
     *
     * @param userName 用户的名称
     * @param appKey 新的appkey
     * @return
     * @author xutaog 2016年4月12日
     */
    int modifyUserKeyByUserName(@Param("userName") String userName, @Param("appKey") String appKey);

    /**
     * 根据用户的id获取用户的所有可以访问的服务
     *
     * @param userName 用户UserName
     * @return
     * @author xutaog 2016年4月19日
     */
    List<DatasetServ> queryUserServByUserName(@Param("userName") String userName);
}
