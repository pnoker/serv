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
package com.dc.city.service.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.common.datasource.DynamicDataSource.DbContextHolder;
import com.dc.city.common.exception.BusinessException;
import com.dc.city.common.utils.StringUtils;
import com.dc.city.dao.master.datasource.ServeDataSourceMapper;
import com.dc.city.domain.config.ServeConfig;
import com.dc.city.domain.datasource.ServeDataSource;
import com.dc.city.pojo.datasource.DataSourceManagePo;
import com.dc.city.service.cache.PubCacheService;
import com.dc.city.service.config.ConfigerService;
import com.dc.city.vo.PageVo;
import com.dc.city.vo.database.datasource.DataSourceBaseVo;
import com.dc.city.vo.database.datasource.DataSourceManageVo;

/**
 * 数据源管理service层
 * 负责数据源crud
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月7日 下午4:26:56
 *          Copyright 2016 by DigitalChina
 */
@Service
public class ServeDataSourceService {

    private Logger logger = LoggerFactory.getLogger(ServeDataSourceService.class);

    // 错误码
    public static final String ERROR_CODE = "-1";

    @Resource
    private ServeDataSourceMapper dataSourceMapper;

    @Resource
    private PubCacheService cacheService;

    @Resource
    private ConfigerService configService;

    @Resource
    private DynamicDataSource dynamicSource;

    /**
     * 未分页查询
     *
     * @param source 数据源请求参数封装对象
     * @return 返回数据源对象列表
     * @author zhongdt 2016年3月23日
     */
    public List<ServeDataSource> allList(ServeDataSource source) {
        return this.dataSourceMapper.queryDataSources(source);
    }

    /**
     * 按主键查询数据源
     *
     * @param id 数据源id
     * @return 返回指定id的数据源java对象
     * @author zhongdt 2016年3月23日
     */
    public ServeDataSource find(long id) {
        return this.dataSourceMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     *
     * @param sourcePo 数据源前端查询封装参数，包含分页信息
     * @return 返回数据源列表，包含数据源状态，状态通过dynamic
     * @author zhongdt 2016年3月23日
     */
    public List<DataSourceManagePo> queryForPage(DataSourceManagePo paramSourcePo) {
        List<DataSourceManagePo> sourceList = new ArrayList<DataSourceManagePo>();
        for (ServeDataSource source : this.dataSourceMapper.queryDataSourceList(paramSourcePo)) {
            DataSourceManagePo sourcePo = new DataSourceManagePo();
            BeanUtils.copyProperties(source, sourcePo);
            // 设置数据源连接属性
            sourcePo.setConnectionStatus(getConnectionStatus(sourcePo.getId() + ""));
            sourceList.add(sourcePo);
        }
        return sourceList;
    }

    /**
     * 分页查询数据源列表
     *
     * @param sourceVo
     * @return
     * @author zhongdt 2016年3月10日
     */
    public DataSourceBaseVo queryForPage(DataSourceManageVo sourceVo) {
        DataSourceBaseVo vo = new DataSourceBaseVo();
        if (sourceVo == null) {
            return vo;
        }
        // 取总条数，如果为0 ，直接返回
        Long totalCount = getTotalCount(sourceVo);
        if (totalCount == 0) {
            vo.setTotalCount("0");
            return vo;
        }
        // 封装入参po及分页条件
        DataSourceManagePo sourcePo = new DataSourceManagePo();
        BeanUtils.copyProperties(sourceVo, sourcePo);
        // 每页记录数
        int pageSize = sourceVo.getLength();
        pageSize = (pageSize == 0) ? PageVo.DEFAULT_PAGE_SIZE : pageSize;
        // 当前页码
        int start = sourceVo.getStart();
        start = (start <= 0) ? PageVo.DEFAULT_CURRENT_PAGE : ++start;
        // 起始行数
        sourcePo.setBeginRowNum(start);
        // 截止行数
        sourcePo.setEndRowNum(start + pageSize);

        List<DataSourceManagePo> datas = queryForPage(sourcePo);
        vo.setPageSize(pageSize + "");
        vo.setDatas(datas);
        vo.setTotalCount(totalCount + "");
        return vo;
    }

    /**
     * 获取数据源总条数
     *
     * @param sourceVo
     * @return
     * @author zhongdt 2016年3月10日
     */
    public long getTotalCount(DataSourceManageVo sourceVo) {
        if (sourceVo == null) {
            return 0;
        }
        DataSourceManagePo sourcePo = new DataSourceManagePo();
        BeanUtils.copyProperties(sourceVo, sourcePo);
        return this.getTotalCount(sourcePo);
    }

    public long getTotalCount(DataSourceManagePo sourcePo) {
        if (sourcePo == null) {
            return 0;
        }
        return this.dataSourceMapper.getTotalCount(sourcePo);
    }

    /**
     * 新建数据源
     *
     * @param sourceVo
     * @return
     * @author zhongdt 2016年3月10日
     */
    public DataSourceBaseVo createDataSource(DataSourceManageVo sourceVo) {
        if (sourceVo == null) {
            return new DataSourceBaseVo(ERROR_CODE, "参数不合法");
        }
        DataSourceBaseVo vo = new DataSourceBaseVo();
        DataSourceManagePo sourcePo = new DataSourceManagePo();
        BeanUtils.copyProperties(sourceVo, sourcePo);
        // 根据type生成连接驱动class
        sourcePo.setSourceClass(buildDriverClassName(sourcePo.getSourceType()));

        if (!validCreateDataSource(sourcePo)) {
            return new DataSourceBaseVo(ERROR_CODE, "参数不合法");
        }
        // 插入到数据库中
        int result = this.dataSourceMapper.createDataSource(sourcePo);
        // 保存成功，添加数据源
        if (result == 1 && sourcePo.getId() > 0) {
            // 组装atomiskbean
            Map<String, String> property = new HashMap<String, String>();
            property.put("jdbcUrl", sourcePo.getSourceUrl());
            property.put("driverClassName", sourcePo.getSourceClass());
            property.put("username", sourcePo.getSourceUser());
            property.put("password", sourcePo.getSourcePass());

            // 发布数据源更新操作远程通知
            cacheService.publishDataSource(PubCacheService.OPERATE_CREATE, sourcePo.getId() + "");

            // 添加到本地内存
            try {
                dynamicSource.addDataSource(sourcePo.getId() + "", property);
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         
        }
        return vo;
    }

    /**
     * 修改数据源，并重新load
     *
     * @param sourceVo
     * @return
     * @author zhongdt 2016年3月10日
     */
    public DataSourceBaseVo modifyDataSource(DataSourceManageVo sourceVo) {
        DataSourceBaseVo vo = new DataSourceBaseVo();
        if (sourceVo == null || sourceVo.getId() <= 0) {
            return new DataSourceBaseVo(ERROR_CODE, "数据源ID不能为空");
        }

        ServeDataSource source = this.dataSourceMapper.selectByPrimaryKey(sourceVo.getId());

        BeanUtils.copyProperties(sourceVo, source);
        if (source == null) {
            return new DataSourceBaseVo(ERROR_CODE, "数据源ID不能为空");
        }
        // 构造driveClassName
        source.setSourceClass(buildDriverClassName(source.getSourceType()));

        // 调用更新接口更新到数据库中
        int result = this.dataSourceMapper.updateByPrimaryKey(source);
        if (result == 1) {
            Map<String, String> property = new HashMap<String, String>();
            property.put("jdbcUrl", source.getSourceUrl());
            property.put("driverClassName", source.getSourceClass());
            property.put("username", source.getSourceUser());
            property.put("password", source.getSourcePass());

            try {
                dynamicSource.modifyDataSource(source.getId() + "", property);
            } catch (BusinessException e) {
                logger.warn("重新加载数据源失败", e);
            }
            // 添加动态数据源远程通知
            cacheService.publishDataSource(PubCacheService.OPERATE_MODIFY, source.getId() + "");// 修改本地内存
        }

        return vo;
    }

    /**
     * 删除数据源的方法,假删
     *
     * @param sourceVo
     * @return
     * @author zhongdt 2016年3月10日
     */
    public DataSourceBaseVo removeDataSource(DataSourceManageVo sourceVo) {
        DataSourceBaseVo vo = new DataSourceBaseVo();
        if (sourceVo == null || sourceVo.getId() <= 0) {
            return new DataSourceBaseVo(ERROR_CODE, "数据源ID不能为空");
        }
        ServeDataSource source = this.dataSourceMapper.selectByPrimaryKey(sourceVo.getId());

        if (source == null) {
            return new DataSourceBaseVo(ERROR_CODE, "数据源ID不能为空");
        }

        if (!isSourceUsed(source.getId())) {
            return new DataSourceBaseVo(ERROR_CODE, "数据源已经被使用,不能删除");
        }

        source.setIsDeleted(1);

        int result = this.dataSourceMapper.updateByPrimaryKey(source);
        if (result != 1) {
            return new DataSourceBaseVo(ERROR_CODE, "删除数据源失败");
        }

        try {
            // 在本地删除dataSource数据源
            dynamicSource.removeDataSourceByKey(source.getId() + "");
        } catch (BusinessException e) {
            logger.warn("jvm中删除动态数据源失败", e);
        }
        // 发送远程消息通知其他订阅服务器，数据源需要刷新
        cacheService.publishDataSource(PubCacheService.OPERATE_REMOVE, source.getId() + "");

        return vo;
    }

    /**
     * 新增时验证数据源有效性，验证通过后才能保存
     *
     * @param sourcePo
     * @return
     * @author zhongdt 2016年3月10日
     */
    private boolean validCreateDataSource(DataSourceManagePo sourcePo) {
        if (sourcePo == null) {
            return false;
        }
        if (sourcePo.getSourceName() == null || StringUtils.isNullOrEmpty(sourcePo.getSourceName())) {
            return false;
        }
        if (sourcePo.getSourceUrl() == null || StringUtils.isNullOrEmpty(sourcePo.getSourceUrl())) {
            return false;
        }
        if (sourcePo.getSourceType() == null) {
            return false;
        }

        if (sourcePo.getSourceClass() == null || StringUtils.isNullOrEmpty(sourcePo.getSourceClass())) {
            return false;
        }

        if (sourcePo.getSourceUser() == null || StringUtils.isNullOrEmpty(sourcePo.getSourceUser())) {
            return false;
        }

        if (sourcePo.getSourcePass() == null || StringUtils.isNullOrEmpty(sourcePo.getSourcePass())) {
            return false;
        }

        // 新增时，is_deleted 必须为0，否则新增失败
        if (sourcePo.getIsDeleted() != 0) {
            return false;
        }

        return true;
    }

    public static final int DB_TYPE_ORACLE = 1;
    public static final int DB_TYPE_SQL_SERVER = 2;
    public static final int DB_TYPE_MY_SQL = 3;

    private String buildDriverClassName(int sourceType) {
        switch (sourceType) {
            case DB_TYPE_ORACLE:
                return "oracle.jdbc.xa.client.OracleXADataSource";
            case DB_TYPE_SQL_SERVER:
                return "com.microsoft.sqlserver.jdbc.SQLServerXADataSource";
            case DB_TYPE_MY_SQL:
                return "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
            default:
                return "";
        }
    }

    /**
     * 判断是否被服务所使用，如果是的话，则不能删除
     *
     * @param dataSourceId
     * @return
     * @author zhongdt 2016年3月10日
     */
    private boolean isSourceUsed(Long dataSourceId) {
        // 此处还需要调用服务配置接口来判断数据源是否能删除
        ServeConfig record = new ServeConfig();
        record.setDataSourceId(dataSourceId);
        record.setIsDeleted(0);
        List<ServeConfig> configs = configService.selectBySelective(record);
        return CollectionUtils.isEmpty(configs) ? true : false;
    }

    /**
     * @param key 数据源远uniqueKey
     * @return 1：可连接 0 正在初始化，-1：连接中断
     * @author zhongdt 2016年3月21日
     */
    private int getConnectionStatus(String key) {
        return dynamicSource.isValid(key) ? 1 : (dynamicSource.getDisableKeys().contains(key) ? -1 : 0);
    }

    /**
     * 手动检查数据源连接状态
     * 用于在数据源监控未成功后，用户在服务后台调用，并通知到其他系统
     * 
     * @param key
     * @return
     * @author zhongdt 2016年4月29日
     */
    public DataSourceBaseVo checkDataSource(String key) {
        DbContextHolder.setDbType(key);
        try (Connection conn = dynamicSource.getConnection()) {
            // 添加到可用集合中
            dynamicSource.getAvaiableKeys().add(key);
            // 添加到不可用集合中
            dynamicSource.getDisableKeys().remove(key);
        } catch (SQLException e) {
            return new DataSourceBaseVo("-1", dynamicSource.getCause(e).getMessage());
        }
        return new DataSourceBaseVo();
    }

}
