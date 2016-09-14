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
package com.dc.city.dao.master.securitymanage.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dc.city.domain.securitymanage.user.ServeUser;
import com.dc.city.domain.securitymanage.user.ServeUserAuthority;
import com.dc.city.pojo.securitymanage.user.ServeConfigPo;
import com.dc.city.pojo.securitymanage.user.UserAuthorizationPo;
import com.dc.city.pojo.securitymanage.user.UserManagePo;
import com.dc.city.pojo.securitymanage.user.UsersCachePo;

public interface UserManageMapper {

    int deleteByPrimaryKey(Short id);

    int insert(ServeUser record);

    int insertSelective(ServeUser record);

    ServeUser selectByPrimaryKey(long id);

    int updateByPrimaryKeySelective(ServeUser record);

    int updateByPrimaryKey(ServeUser record);

    /**
     * 查询用户(分页)
     *
     * @param blackListManageQueryPo
     * @return
     * @author zuoyue 2016年3月8日
     */
    List<UserManagePo> queryUser(UserManagePo UserManageQueryPo);

    // 获取总条数
    Long getTotalCount(UserManagePo UserManageQueryPo);

    /**
     * 查询活跃用户
     *
     * @return
     * @author zuoyue 2016年3月11日
     */
    List<UsersCachePo> queryActiveUsers();

    /**
     * 通过appKey查询用户权限
     * 
     * @param appKey
     * @return
     * @author zuoyue 2016年3月11日
     */
    List<UsersCachePo> queryActiveUsersByAppKey(@Param("appKey") String appKey);

    /**
     * 通过appKey查询用户
     *
     * @param appKey
     * @return
     * @author zuoyue 2016年3月16日
     */
    List<UsersCachePo> queryUserByAppKey(String appKey);

    /**
     * 通过serviceCode查询配置表
     *
     * @param serviceCode
     * @return
     * @author zuoyue 2016年3月13日
     */
    List<ServeConfigPo> queryServeConfigByServiceCode(String serviceCode);

    /**
     * 新增用户
     *
     * @param userManagePo
     * @return
     * @author zuoyue 2016年3月9日
     */
    int createUser(UserManagePo userManagePo);

    /**
     * 编辑用户
     *
     * @param userManagePo
     * @return
     * @author zuoyue 2016年3月10日
     */
    int modifyUser(UserManagePo userManagePo);

    /**
     * 删除用户
     *
     * @param blackListIds
     * @return
     * @author zuoyue 2016年3月8日
     */
    int removeUser(long id);

    /**
     * 用户是否存在
     *
     * @param userName
     * @return
     * @author zuoyue 2016年3月10日
     */
    Map<String, String> queryUserIsExist(String userName);

    /**
     * 用户服务接口与目录授权
     *
     * @param list
     * @return
     * @author zuoyue 2016年3月11日
     */
    int createUserAuthorization(List<?> list);

    /**
     * 删除用户具有的服务权限
     *
     * @param userId
     * @return
     * @author zuoyue 2016年3月11日
     */
    int removeUserAuthorization(UserAuthorizationPo userAuthorizationPo);

    /**
     * 通过serviceId查询user
     *
     * @param serviceId
     * @return
     * @author zuoyue 2016年4月7日
     */
    List<UserManagePo> queryUserByServiceId(long serviceId);

    /**
     * 通过userIds查到appkey
     *
     * @param userIds
     * @return
     * @author zuoyue 2016年4月8日
     */
    List<UserManagePo> queryUserByUserIds(@Param("userIds") String[] userIds);
    
    List<ServeUserAuthority> queryUserAuths(@Param("userId")Integer userId);
}