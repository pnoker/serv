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
package com.dc.city.controller.config;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Controller;

/**
 * 添加服务的接口
 *
 * @author chenzpa
 * @version V1.0 创建时间：2016年4月15日 上午10:19:37
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/createconfig/v1")
@Produces({ "application/xml", "application/json" })
public class AddConfigerController extends ConfigerController {

}
