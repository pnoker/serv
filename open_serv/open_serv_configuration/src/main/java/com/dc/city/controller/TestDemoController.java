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
package com.dc.city.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

/**
 * 测试类
 *
 * @author ligen
 * @version V1.0 创建时间：2016年2月24日 上午11:11:04
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/testdemo")
@Produces({ "application/xml", "application/json" })
public class TestDemoController {
    private static Log logger = LogFactory.getLog(TestDemoController.class);

    @GET
    @Path("/getuser")
    public Map<String, String> getUser(@Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        logger.info("测试成功了！");
        Map<String, String> map = new HashMap<String, String>();
        map.put("resultInfo", "测试成功");
        return map;
    }

}
