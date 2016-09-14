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
package com.dc.city.service.login;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.common.security.AesUtils;
import com.dc.city.common.utils.Base64EncodeUtils;
import com.dc.city.common.utils.DateUtils;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.dao.master.login.ServLoginLogMapper;
import com.dc.city.dao.master.login.ServLoginMapper;
import com.dc.city.dao.master.securitymanage.user.UserManageMapper;
import com.dc.city.domain.dataset.DatasetServ;
import com.dc.city.domain.log.ServLoginLog;
import com.dc.city.domain.securitymanage.user.ServeUser;
import com.dc.city.pojo.securitymanage.user.UsersCachePo;
import com.dc.city.vo.login.ServLoginUserVo;
import com.dc.city.vo.login.ServLoginVo;
import com.dc.city.vo.login.ServUserCookieVo;
import com.dc.city.vo.securitymanage.user.UserManageVo;

/**
 * 数据汇总登陆相关的服务层
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年3月28日 下午3:04:54
 *          Copyright 2016 by DigitalChina
 */
@Service
public class ServLoginService {

    @Resource
    private ServLoginMapper servLoginMapper;

    @Resource
    private ServLoginLogMapper servLoginLogMapper;

    @Resource
    private UserManageMapper userManageMapper;

    /**
     * 根据tokeId获取日志
     *
     * @param tokenId
     * @return
     * @author xutaog 2016年3月29日
     */
    public ServLoginLog queryServLoginLogByTokenId(String tokenId) {
        return servLoginLogMapper.queryServLoginLog(tokenId);
    }

    /**
     * 创建登录日志也记录注册日志
     *
     * @param record
     * @author xutaog 2016年4月7日
     */
    public void createServLoginLog(ServLoginLog record) {
        servLoginLogMapper.createServLoginLog(record);
    }

    /**
     * 登陆验证并记录登录日志
     *
     * @param userManageVo
     * @param log
     * @return
     * @author xutaog 2016年3月29日
     */
    public ServLoginVo validateLogin(UserManageVo userManageVo, ServLoginLog log) {
        ServLoginVo result = new ServLoginVo();
        // 默认是没有成功
        int isSucc = 1;
        // 备注
        String remark = "";
        // 先判断传输过来的用户对象是否为空
        if (userManageVo != null) {
            // 开始判断验证码是否输入正确
            String validateCode = userManageVo.getValidCode();
            // 判断验证码是否为空
            if (!StringUtils.isNullOrEmpty(validateCode)) {
                // 开始从redis中去获取验证码，首先判断redis是否挂掉
                String sessionValidCode = RedisUtil.getString(userManageVo.getTokenId());
                // 如果没有从redis中获取出验证码
                if (StringUtils.isNullOrEmpty(sessionValidCode)) {
                    // 如果redis挂掉就需要从数据库中获取验证码
                    ServLoginLog servLoginLog = queryServLoginLogByTokenId(userManageVo.getTokenId());
                    if (servLoginLog != null) {
                        sessionValidCode = servLoginLog.getRemark();
                    }
                }
                // 取出服务中的验证码之后就进行验证码判断
                if (validateCode.equalsIgnoreCase(sessionValidCode)) {
                    String qdUserPass = userManageVo.getUserPass();
                    String userName = userManageVo.getUserName();
                    // 判断用户名是否为空
                    if (StringUtils.strsIsNotNullOrEmpty(qdUserPass, userName)) {
                        ServeUser user = servLoginMapper.queryServeUserByUserName(null, userName);
                        // 判断找出来的用户是否为空
                        if (user != null) {
                            // 将前端传过来的密码反编译
                            String decryptPass = new String(Base64EncodeUtils.decode(qdUserPass));
                            // // 再将密码加密
                            String encryptPwd = AesUtils.aecEncryptBySalt(decryptPass, user.getEncryptSalt());
                            // 在判断两个密码是否相同
                            if (encryptPwd.equalsIgnoreCase(user.getUserPass())) {
                                // 登录成功将用户的基本信息返回给前台，并让前台将其缓存在cookie中
                                ServUserCookieVo cookieUser = new ServUserCookieVo();
                                cookieUser.setId(user.getId());
                                cookieUser.setUserName(user.getUserName());
                                List<ServUserCookieVo> arrCookieVos = new ArrayList<ServUserCookieVo>();
                                arrCookieVos.add(cookieUser);
                                result.setDatas(arrCookieVos);
                                result.setResultCode(ServLoginVo.SUCCESS_CODE);
                                result.setResultInfo(ServLoginVo.LOGIN_SUSSESS_INFO);
                                isSucc = 0;
                            } else {
                                result.setResultCode(ServLoginVo.ERROR_CODE);
                                result.setResultInfo(ServLoginVo.LOGIN_FAILURE_INFO);
                            }
                        } else {
                            result.setResultCode(ServLoginVo.ERROR_CODE);
                            result.setResultInfo(ServLoginVo.LOGIN_FAILURE_INFO);
                        }
                    } else {
                        result.setResultCode(ServLoginVo.ERROR_CODE);
                        result.setResultInfo("用户名与密码为空");
                    }
                } else {
                    result.setResultCode(ServLoginVo.ERROR_CODE);
                    result.setResultInfo("验证码输入有误，请重新输入！");
                }
            } else {
                result.setResultCode(ServLoginVo.ERROR_CODE);
                result.setResultInfo("验证码为空，请输入验证码！");
            }

        } else {
            result.setResultCode(ServLoginVo.ERROR_CODE);
            result.setResultInfo("登录信息为空！");
        }
        // 封装登录日志信息
        remark = result.getResultInfo();
        log.setTokeId(userManageVo.getTokenId());
        log.setRemark(remark);
        log.setIsSucc(isSucc);
        log.setLoginTime(new Date());
        log.setUserName(userManageVo.getUserName());
        // 调用保存方法进行保存
        servLoginLogMapper.createServLoginLog(log);
        return result;
    }

    /**
     * 获取当前登录用户的信息
     *
     * @param userName 用户名
     * @return
     * @author xutaog 2016年4月8日
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public ServLoginVo queryCurrentLoginUserInfo(String userName) throws IllegalAccessException,
            InvocationTargetException {
        // 首先获取用户的当前登录用户的基本信息
        ServeUser bsesUserInfo = servLoginMapper.queryServeUserByUserName(null, userName);
        if (bsesUserInfo != null) {

            ServLoginUserVo userInfo = new ServLoginUserVo();
            // 将信息复制到前台显示的vo中
            BeanUtils.copyProperties(userInfo, bsesUserInfo);
            // 求出上次登录信息
            List<ServLoginLog> logs = servLoginLogMapper.queryNewlySecServLoginLog(bsesUserInfo.getUserName());
            if (logs != null && logs.size() > 0) {
                // 如果有上次登录时间那么就取上次没有就取这次登录时间
                if (logs.size() == 2) {
                    userInfo.setSubLoginTime(DateUtils.format(logs.get(1).getLoginTime(), DateUtils.DATE_FORMAT_2));
                    userInfo.setIp(logs.get(1).getIp());
                    userInfo.setIsSucc(logs.get(1).getIsSucc());
                    userInfo.setRemark(logs.get(1).getRemark());
                } else {
                    userInfo.setSubLoginTime(DateUtils.format(logs.get(0).getLoginTime(), DateUtils.DATE_FORMAT_2));
                    userInfo.setIp(logs.get(0).getIp());
                    userInfo.setIsSucc(logs.get(0).getIsSucc());
                    userInfo.setRemark(logs.get(0).getRemark());
                }
            }
            userInfo.setRegistTime(DateUtils.format(bsesUserInfo.getCreateTime(), DateUtils.DATE_FORMAT_2));
            ServLoginVo servLoginVo = new ServLoginVo();
            List<ServLoginUserVo> datas = new ArrayList<ServLoginUserVo>();
            datas.add(userInfo);
            servLoginVo.setDatas(datas);
            return servLoginVo;
        } else {
            return new ServLoginVo();
        }

    }

    /**
     * 更新用户的key的方法
     *
     * @param id
     * @param appKey
     * @return
     * @author xutaog 2016年4月13日
     */
    private int modifyUserKeyByUserName(String userName, String appKey) {
        return servLoginMapper.modifyUserKeyByUserName(userName, appKey);
    }

    /**
     * 根据用户名修改用户的appkey
     *
     * @param userName 当前登录的用户名
     * @param oldAppKey 旧的服务key
     * @return
     * @author xutaog 2016年4月13日
     */
    public ServLoginVo refreshAppKeyByUserName(String userName, String oldAppKey) {
        // 生成用户的userKey
        String userkey = UUID.randomUUID().toString();
        // 调用更新方法更新数据库中用户的Key
        int ret = modifyUserKeyByUserName(userName, userkey);
        ServLoginVo result = new ServLoginVo();
        if (ret > 0) {
            // 更新成功后需要先将服务器中的redis中当前用户的缓存的数据先删除后重新添加
            // 通过oldAppKey从缓存的用户信息中获取该用户
            UsersCachePo oldeUsersCachePo = (UsersCachePo) RedisUtil.getObject(oldAppKey);
            if (oldeUsersCachePo != null) {
                RedisUtil.delByKey(oldAppKey);
            }
            // 用新的appkey获取数据
            List<UsersCachePo> usersCachePoList = userManageMapper.queryUserByAppKey(userkey);
            // 先判断是否能将数据获取成功，获取成功后就将数据缓存
            if (usersCachePoList != null && usersCachePoList.size() > 0) {
                UsersCachePo usersCachePo = usersCachePoList.get(0);
                usersCachePo.setAppKey(userkey);
                RedisUtil.setObject(userkey, usersCachePo, RedisUtil.DEFAULT_SEESION_SECONDS);
            }
            // 然后新生成的key封装到显示对象中供给前台显示
            List<String> datas = new ArrayList<String>();
            datas.add(userkey);
            result.setDatas(datas);
        } else {
            result.setResultCode("-1");
            result.setResultInfo("更新服务调用key失败！");
        }
        return result;
    }

    /**
     * 根据用户的ID获取用户都可以访问的服务列表
     *
     * @param userName
     * @return
     * @author xutaog 2016年4月19日
     */
    public ServLoginVo queryUserServByUserName(String userName) {
        List<DatasetServ> datas = servLoginMapper.queryUserServByUserName(userName);
        ServLoginVo servLoginVo = new ServLoginVo();
        servLoginVo.setDatas(datas);
        return servLoginVo;
    }
}
