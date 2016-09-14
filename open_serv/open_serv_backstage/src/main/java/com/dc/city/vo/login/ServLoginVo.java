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
package com.dc.city.vo.login;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.dc.city.vo.BaseVo;

/**
 * 用于登陆使用的基础类
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年3月28日 下午3:00:19
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "ServLoginVo")
@XmlSeeAlso({ ServLoginUserVo.class })
public class ServLoginVo extends BaseVo {

    public static final String LOGIN_FAILURE_INFO = "用户名或密码错误！";

    public static final String LOGIN_SUSSESS_INFO = "登录成功！";

    private List<?> datas;

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
    }

}
