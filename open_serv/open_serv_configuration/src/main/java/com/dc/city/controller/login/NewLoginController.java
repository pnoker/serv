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
package com.dc.city.controller.login;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.common.utils.JsonUtil;
import com.dc.city.vo.login.LoginVo;


/**
 * 新登录系统
 *
 * @author ligen
 * @version V1.0 创建时间：2016年8月25日 下午2:56:05
 *          Copyright 2016 by DigitalChina
 */
@Controller
@Path("/login/v1")
@Produces({ "application/json" })
public class NewLoginController {    
    private static int DEFAULT_SEESION_SECONDS = 1800;
    
    private static int REMENBER_SEESION_SECONDS = 20 * 24 * 60 * 60;
    
    @GET
    @Path("/test")
    public String test(){
        System.out.println("成功了");
        return "success";
    }
    
    

    @POST
    @Path("/querylogin")
    public String login(@FormParam("userName") String userName,
            @FormParam("userPass") String userPass,@FormParam("checkCode") String checkCodeStr,
            @Context HttpServletRequest request,@Context HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        Boolean checkCode = Boolean.parseBoolean(checkCodeStr);
        String erro ="";
        LoginVo loginVo =new LoginVo();
        if(!StringUtils.isNotEmpty(userName)){
            erro ="用户名不能为空";
            loginVo.setResultCode("-1");
            loginVo.setResultInfo(erro);
            return JsonUtil.objectToJson(loginVo);
            
        }else if(!StringUtils.isNotEmpty(userPass)){
            erro ="密码不能为空";
            loginVo.setResultCode("-1");
            loginVo.setResultInfo(erro);
            return JsonUtil.objectToJson(loginVo);
        } 
        if(userName.equals("admin")&&userPass.equals("admin")){
            loginVo.setResultCode("0");
            loginVo.setUsername(userName);
            request.getSession(true).setAttribute("userName", userName);
            String tokenId = queryTokenId(userName,checkCode ? REMENBER_SEESION_SECONDS : DEFAULT_SEESION_SECONDS);
            RedisUtil.setObject(tokenId, "admin",checkCode ? REMENBER_SEESION_SECONDS : DEFAULT_SEESION_SECONDS);
            loginVo.setTokenId(tokenId);
            loginVo.setCheckCode(checkCode);
        }else{
            erro ="用户名密码不匹配";
            loginVo.setResultCode("-1");
            loginVo.setResultInfo(erro);
         }     
        return JsonUtil.objectToJson(loginVo);    
    }
    
    /**
     * 生成包含用户名的tokenId
     *
     * @param userName 用户名
     * @return
     * @author ligen 2016年8月29日
     */
    private String queryTokenId(String userName, int sessionTime) {
        UUID uuid = UUID.randomUUID();
        String tokenId = "auth:login:" + userName + ":" + uuid.toString() + ":" + sessionTime + "" ;
        return tokenId;
    }

}
