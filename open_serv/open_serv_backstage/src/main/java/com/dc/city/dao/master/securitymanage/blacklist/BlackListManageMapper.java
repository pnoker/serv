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
package com.dc.city.dao.master.securitymanage.blacklist;

import java.util.List;
import com.dc.city.domain.securitymanage.blacklist.ServeBlackList;
import com.dc.city.pojo.securitymanage.blacklist.BlackListManagePo;

public interface BlackListManageMapper {

    int deleteByPrimaryKey(long id);

    int insert(ServeBlackList record);

    int insertSelective(ServeBlackList record);

    ServeBlackList selectByPrimaryKey(long id);

    int updateByPrimaryKeySelective(ServeBlackList record);

    int updateByPrimaryKey(ServeBlackList record);

    /**
     * 查询黑名单(分页)
     *
     * @param blackListManageQueryPo
     * @return
     * @author zuoyue 2016年3月8日
     */
    List<BlackListManagePo> queryBlackList(BlackListManagePo blackListManageQueryPo);
    
    /**
     * 查询黑名单
     *
     * @param blackListManageQueryPo
     * @return
     * @author zuoyue 2016年3月8日
     */
    List<BlackListManagePo> queryBlackListAll();

    /**
     * 新增黑名单
     *
     * @param blackListManagePo
     * @return
     * @author zuoyue 2016年3月8日
     */
    int createBlackList(BlackListManagePo blackListManagePo);

    /**
     * 删除黑名单
     *
     * @param blackListIds
     * @return
     * @author zuoyue 2016年3月8日
     */
    int removeBlackList(Object[] blackListIds);
    
    int getTotalCount(BlackListManagePo blackListManageQueryPo);

}