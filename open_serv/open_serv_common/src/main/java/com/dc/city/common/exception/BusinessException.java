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
package com.dc.city.common.exception;

/**
 * 自定义业务异常类
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年5月26日 下午5:07:41
 *          Copyright 2016 by DigitalChina
 */
public class BusinessException extends Exception {

    
    private static final long serialVersionUID = 1L;

    public BusinessException(String msg) {
        super(msg);
    }

}
