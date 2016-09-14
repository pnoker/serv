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

import com.dc.city.domain.log.ServLoginLog;

/**
 * 专门记录汇总平台的登录日志
 * 
 * @author xutaog
 * @version V1.0 创建时间：2016年3月29日 上午11:24:02
 *          Copyright 2016 by DigitalChina
 */
public interface ServLoginLogMapper {

    /**
     * 创建登录日志
     *
     * @param record
     * @return
     * @author xutaog 2016年3月29日
     */
    int createServLoginLog(ServLoginLog record);

    /**
     * 根据tokenId获取服务日志
     *
     * @param tokenId
     * @return
     * @author xutaog 2016年3月29日
     */
    ServLoginLog queryServLoginLog(String tokenId);

    /**
     * 获取用户最近2次登录的时间
     *
     * @param userName
     * @return
     * @author xutaog 2016年4月8日
     */
    List<ServLoginLog> queryNewlySecServLoginLog(String userName);
}
