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
package com.dc.city.service.dataset;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dc.city.common.jedis.RedisUtil;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.dao.master.dataset.DatasetMapper;
import com.dc.city.dao.master.login.ServLoginMapper;
import com.dc.city.dao.master.securitymanage.user.UserManageMapper;
import com.dc.city.domain.dataset.DatasetMenu;
import com.dc.city.domain.dataset.DatasetServ;
import com.dc.city.domain.dataset.ServErrorCode;
import com.dc.city.domain.dataset.ServExample;
import com.dc.city.domain.dataset.ServModifyLog;
import com.dc.city.domain.dataset.ServRequestParams;
import com.dc.city.domain.dataset.ServResponseField;
import com.dc.city.domain.securitymanage.user.ServeUser;
import com.dc.city.pojo.securitymanage.user.UsersCachePo;
import com.dc.city.vo.datasets.DataSetVo;
import com.dc.city.vo.datasets.DatasetMenuVo;

/**
 * 数据汇总service
 *
 * @author xutaog
 * @version V1.0 创建时间：2016年4月13日 下午4:10:29
 *          Copyright 2016 by DigitalChina
 */
@Service
public class DatasetService {

    @Resource
    private DatasetMapper datasetMapper;

    @Resource
    private ServLoginMapper servLoginMapper;

    @Resource
    private UserManageMapper userManageMapper;

    /**
     * 此方法用于数据汇总平台生成当用户没有登录时生成menu
     *
     * @return
     * @author xutaog 2016年4月13日
     */
    public DataSetVo queryNoVerifyDatasetMenus() {
        List<DatasetMenu> menus = datasetMapper.queryNoVerifyDatasetMenus();
        // 调用公共的方法将从数据库中取出的数据解析成前台需要的格式
        DatasetMenuVo onlyMenuVo = menuClassify(menus);
        List<DatasetMenuVo> datas = new ArrayList<DatasetMenuVo>();
        datas.add(onlyMenuVo);
        DataSetVo ret = new DataSetVo();
        ret.setDatas(datas);
        return ret;
    }

    /**
     * 获取需要进行验证的数据汇总菜单
     * 分几步
     * 首先获取当前登录用户的信息
     * 其次根据用户的Appkey获取出用户能访问的服务ID
     * 然后根据服务ID查询出能访问的菜单
     * 
     * @param userName 用户名
     * @return
     * @author xutaog 2016年4月15日
     */
    public DataSetVo queryVerifyDatasetMenus(String userName) {
        // 首先根据ID获取当前用户
        ServeUser user = servLoginMapper.queryServeUserByUserName(null, userName);
        // 获取当前用户的AppKye
        String appKey = user.getAppKey();
        // 首先从缓存中去获取访问权限
        UsersCachePo usersCachePo = (UsersCachePo) RedisUtil.getObject(appKey);
        String viewPermissions = "";
        if (usersCachePo != null) {
            viewPermissions = usersCachePo.getViewPermissions();
        } else {
            // 获取不到之后才从数据库中获取
            // 查数据库
            List<UsersCachePo> usersCachePoList = userManageMapper.queryUserByAppKey(appKey);
            if (usersCachePoList != null && usersCachePoList.size() > 0) {
                UsersCachePo usersCachePoByDataBase = usersCachePoList.get(0);
                viewPermissions = usersCachePoByDataBase.getViewPermissions();
            }
        }
        String[] ids = null;
        if (!StringUtils.isNullOrEmpty(viewPermissions)) {
            ids = viewPermissions.split(",");
        }
        // 然后调用数据库中取出当前用户可以查看的接口
        List<DatasetMenu> menus = datasetMapper.queryVerifyDatasetMenus(ids);
        DatasetMenuVo onlyMenuVo = menuClassify(menus);
        List<DatasetMenuVo> datas = new ArrayList<DatasetMenuVo>();
        datas.add(onlyMenuVo);
        DataSetVo ret = new DataSetVo();
        ret.setDatas(datas);
        return ret;
    }

    /**
     * 分类型将菜单分别存到对应的等级中
     * 分三级，1级菜单、2级菜单、3级菜单，其他级数据不要
     *
     * @param menuList
     * @return
     * @author xutaog 2016年4月13日
     */
    private DatasetMenuVo menuClassify(List<DatasetMenu> menuList) {
        // 第0级菜单数据
        List<DatasetMenu> levelZero = new ArrayList<DatasetMenu>();
        // 第一级菜单数据
        List<DatasetMenu> levelOne = new ArrayList<DatasetMenu>();
        // 第二级菜单数据
        List<DatasetMenu> levelTwo = new ArrayList<DatasetMenu>();
        int len = menuList.size();// 分类型将菜单分别存到对应的等级中
        for (int i = 0; i < len; i++) {
            // 当等级为1
            if (1 == menuList.get(i).getLevel()) {
                levelZero.add(menuList.get(i));
                // 当等级为2
            } else if (2 == menuList.get(i).getLevel()) {
                levelOne.add(menuList.get(i));
                // 当等级为3
            } else if (3 == menuList.get(i).getLevel()) {
                levelTwo.add(menuList.get(i));
            }
        }
        DatasetMenuVo result = new DatasetMenuVo();
        result.setLevelOne(levelOne);
        result.setLevelTwo(levelTwo);
        result.setLevelZero(levelZero);
        return result;
    }

    /**
     * 获取不需要验证的服务
     * 
     * @param servName 模糊查询的时候传的名称 只有点击查询按钮的时候才使用
     * @param parentId 父级目录ID
     * @param startRow 起始条数
     * @param endRow 结束条数
     * @return
     * @author xutaog 2016年4月18日
     */
    public DataSetVo queryNoVerifyDatasetServ(String servName, Long parentId, int startRow, int endRow) {
        // dao层获取数据
        List<DatasetServ> datas = datasetMapper.queryNoVerifyDatasetServByPageSizeAndParentId(servName, parentId,
                startRow, endRow);
        DataSetVo ret = new DataSetVo();
        ret.setDatas(datas);
        return ret;
    }

    /**
     * 获取用户不需要验证的权限的接口列表
     * 分几步
     * 现根据菜单id或servname查询出没有删除的服务，
     * 然后在根据用户名称查询出当前用户可以访问的服务，
     * 将前后查询出的服务联合查询，取出用户的授权与不需要经过验证就可以访问的服务
     * 
     * @param servName 模糊查询的时候传的名称 只有点击查询按钮的时候才使用
     * @param parentId 父级Id
     * @param startRow 起始行数
     * @param endRow 结束行数
     * @param userName 用户名
     * @return
     * @author xutaog 2016年4月18日
     */
    public DataSetVo queryVerifyDatasetServ(String servName, Long parentId, int startRow, int endRow, String userName) {
        // dao层获取数据
        List<DatasetServ> datas = datasetMapper.queryVerifyDatasetServByPageSizeAndParentIdAndUserName(servName,
                parentId, startRow, endRow, userName);
        DataSetVo ret = new DataSetVo();
        ret.setDatas(datas);
        return ret;
    }

    /**
     * 根据servId获取服务的基本信息
     *
     * @param servId 服务ID
     * @param userName 当前登录的用户名
     * @return
     * @author xutaog 2016年4月19日
     */
    public DataSetVo queryDatasetServBasicInfoByServId(Long servId, String userName) {
        // 服务的基本信息
        DatasetServ servBasicInfo = datasetMapper.queryDatasetServBasicInfoById(servId, userName);
        // 获取返回值
        List<ServResponseField> fields = datasetMapper.queryServResponseFieldsByServId(servId);
        // 获取需求参数
        List<ServRequestParams> params = datasetMapper.queryServRequestParamsByServId(servId);
        // 获取例子
        List<ServExample> examples = datasetMapper.queryServExampleByServId(servId);
        // 将返回值与需求参数设置到基本信息类
        servBasicInfo.setParams(params);
        servBasicInfo.setFields(fields);
        servBasicInfo.setExamples(examples);
        List<DatasetServ> datas = new ArrayList<DatasetServ>();
        datas.add(servBasicInfo);
        DataSetVo ret = new DataSetVo();
        ret.setDatas(datas);
        return ret;
    }

    /**
     * 封装服务的错误码，暂时没有数据来源，造的假数据
     *
     * @param servId 服务ID
     * @return
     * @author xutaog 2016年4月19日
     */
    public DataSetVo queryDatasetServErrorCodeByServId(Long servId) {
        List<ServErrorCode> datas = new ArrayList<ServErrorCode>();
        ServErrorCode single = new ServErrorCode();
        single.setId(servId);
        single.setErrorCode("0");
        single.setDescription("成功");
        datas.add(single);
        single = new ServErrorCode();
        single.setErrorCode("-1");
        single.setDescription("失败");
        datas.add(single);
        DataSetVo ret = new DataSetVo();
        ret.setDatas(datas);
        return ret;
    }

    /**
     * 根据服务的id获取服务的更新记录
     *
     * @param servId 服务ID
     * @return
     * @author xutaog 2016年4月19日
     */
    public DataSetVo queryServModifyLogByServId(Long servId) {
        List<ServModifyLog> datas = datasetMapper.queryServModifyLogByServId(servId);
        DataSetVo ret = new DataSetVo();
        ret.setDatas(datas);
        return ret;
    }

}
