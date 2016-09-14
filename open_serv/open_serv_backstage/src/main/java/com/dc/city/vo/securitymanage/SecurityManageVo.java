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
package com.dc.city.vo.securitymanage;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.dc.city.vo.BaseVo;
import com.dc.city.vo.securitymanage.blacklist.BlackListManageVo;
import com.dc.city.vo.securitymanage.user.ServeUserAuthorityVo;
import com.dc.city.vo.securitymanage.user.UserManageVo;

/**
 * SecurityManageVo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2016年3月7日 下午5:20:39
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "securityManageVo")
@XmlSeeAlso({ BlackListManageVo.class, UserManageVo.class, ServeUserAuthorityVo.class })
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SecurityManageVo extends BaseVo {

    public static final String QUERY_BLACK_LIST_NOT_FOUND_MSG = "未查询到黑名单";

    public static final String QUERY_BLACK_LIST_ERROR_MSG = "查询黑名单失败";

    public static final String REMOVE_BLACK_LIST_ERROR_MSG = "删除黑名单失败";

    public static final String CREATE_BLACK_LIST_ERROR_MSG = "新增黑名单失败";

    public static final String IP_NOT_NULL_MSG = "IP地址不能为空";

    public static final String IP_FORM_ERROR_MSG = "IP地址格式错误";

    public static final String IP_EXIST_ERROR_MSG = "该IP地址已存在";

    public static final String BANREASON_ERROR_MSG = "备注不能超过40个字";

    /**
     * 数据List
     */
    private List<?> datas;

    /**
     * 数据Object
     */
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
    }

}
