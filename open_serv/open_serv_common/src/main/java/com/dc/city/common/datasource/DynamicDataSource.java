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
package com.dc.city.common.datasource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosSQLException;
import com.dc.city.common.exception.BusinessException;
import com.dc.city.common.utils.StringUtils;

/**
 * 动态数据源
 * 提供动态重置添加数据源功能
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月7日 下午3:08:00
 *          Copyright 2016 by DigitalChina
 */

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static Log logger = LogFactory.getLog(DynamicDataSource.class);
    // 数据源Map对象
    private Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

    // 能够正常访问的数据源列表
    private List<String> avaiableKeys = new ArrayList<String>();

    // 不可用数据源key列表，在连接不成功后保存key
    private List<String> disableKeys = new ArrayList<String>();

    public List<String> getDisableKeys() {
        return disableKeys;
    }

    public void setDisableKeys(List<String> disableKeys) {
        this.disableKeys = disableKeys;
    }

    public List<String> getAvaiableKeys() {
        return avaiableKeys;
    }

    /**
     * 获取当前需要使用的数据源unique
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }

    /**
     * 批量设置数据源信息
     *
     * @param 数据源信息map
     * @throws Exception
     * @author zhongdt 2016年3月7日
     */
    public void setDataSource(Map<String, Map<String, String>> dataSourceInfo) throws BusinessException {

        if (dataSourceInfo == null || dataSourceInfo.isEmpty()) {
            logger.warn("数据源列为空");
            throw new BusinessException("数据源列为空");
        }
        // 清空数据源列表对象
        targetDataSources.clear();

        // 根据数据源列表重新构造数据源列表对象
        for (Map.Entry<String, Map<String, String>> entry : dataSourceInfo.entrySet()) {
            String sourceKey = entry.getKey();

            Map<String, String> property = entry.getValue();
            // 必要逻辑判断
            if (property == null || property.isEmpty()) {
                logger.warn("数据源属性为空:" + entry.getKey());
                throw new BusinessException("数据源属性为空:" + entry.getKey());
            }
            if (!property.containsKey("jdbcUrl") || StringUtils.isNullOrEmpty(property.get("jdbcUrl"))) {
                throw new BusinessException("数据源url为空:" + entry.getKey());
            }
            if (!property.containsKey("driverClassName") || StringUtils.isNullOrEmpty(property.get("driverClassName"))) {
                throw new BusinessException("数据源连接驱动为空:" + entry.getKey());
            }
            if (!property.containsKey("username") || StringUtils.isNullOrEmpty(property.get("username"))) {
                throw new BusinessException("数据源用户名为空:" + entry.getKey());
            }
            if (!property.containsKey("password") || StringUtils.isNullOrEmpty(property.get("password"))) {
                throw new BusinessException("数据源密码为空:" + entry.getKey());
            }
            String jdbcUrl = property.get("jdbcUrl").trim();
            String driverClassName = property.get("driverClassName").trim();
            String username = property.get("username").trim();
            String password = property.get("password").trim();

            // 构造数据源对象atomiksBean
            AtomikosDataSourceBean sourceBean = new AtomikosDataSourceBean();
            sourceBean.setUniqueResourceName(sourceKey);
            sourceBean.setXaDataSourceClassName(driverClassName);
            sourceBean.setMaxPoolSize(99);
            Properties xaProperties = new Properties();
            xaProperties.setProperty("URL", jdbcUrl);
            xaProperties.setProperty("user", username);
            xaProperties.setProperty("password", password);
            sourceBean.setXaProperties(xaProperties);
            // 构造数据源对象map
            this.targetDataSources.put(sourceKey, sourceBean);
        }
        // 调用父类方法重新设置数据源列表
        super.setTargetDataSources(targetDataSources);

        // 调用次方法告诉spring，需要刷新数据源列表
        afterPropertiesSet();
        // 初始化数据源状态
        initDataSourceStatus();
    }

    /**
     * 添加单个 数据源到动态数据源
     * 
     * @param key:数据源 uniquerKey
     * @param property:连接属性map对象
     * @throws Exception
     * @author zhongdt 2016年3月7日
     */
    public void addDataSource(String sourceKey, Map<String, String> property) throws BusinessException {
        // 必要的逻辑判断
        if (property == null || property.isEmpty()) {
            throw new BusinessException("数据源属性为空");
        }
        // url不能为空
        if (!property.containsKey("jdbcUrl") || property.get("jdbcUrl") == null
                || StringUtils.isNullOrEmpty(property.get("jdbcUrl"))) {
            throw new BusinessException("添加数据源失败，数据源url为空:" + sourceKey);
        }
        // 连接驱动不能为空
        if (!property.containsKey("driverClassName") || property.get("driverClassName") == null
                || StringUtils.isNullOrEmpty(property.get("driverClassName"))) {
            throw new BusinessException("添加数据源失败，数据源驱动为空:" + sourceKey);
        }
        // 登录名不能为空
        if (!property.containsKey("username") || property.get("username") == null
                || StringUtils.isNullOrEmpty(property.get("username"))) {
            throw new BusinessException("添加数据源失败，数据源用户名为空:" + sourceKey);
        }
        // 密码不能为空
        if (!property.containsKey("password") || property.get("password") == null
                || StringUtils.isNullOrEmpty(property.get("password"))) {
            throw new BusinessException("添加数据源失败，数据源密码为空:" + sourceKey);
        }
        String jdbcUrl = property.get("jdbcUrl").trim();
        String driverClassName = property.get("driverClassName").trim();
        String username = property.get("username").trim();
        String password = property.get("password").trim();

        // 组装datasource对象
        AtomikosDataSourceBean sourceBean = new AtomikosDataSourceBean();
        sourceBean.setUniqueResourceName(sourceKey);
        sourceBean.setXaDataSourceClassName(driverClassName);
        Properties xaProperties = new Properties();
        // atomisk必填属性
        xaProperties.setProperty("URL", jdbcUrl);
        xaProperties.setProperty("user", username);
        xaProperties.setProperty("password", password);
        sourceBean.setXaProperties(xaProperties);

        // 添加新的数据源到数据源列表
        this.targetDataSources.put(sourceKey, sourceBean);
        // 调用父类方法重新设置数据源列表
        super.setTargetDataSources(targetDataSources);
        // 刷新数据源信息
        afterPropertiesSet();
    }

    /**
     * 修改谋个 数据源信息，并加载到内存中
     *
     * @throws Exception
     * @author zhongdt 2016年3月7日
     * @throws BusinessException 业务异常
     */
    public void modifyDataSource(String sourceKey, Map<String, String> property) throws BusinessException {
        // 必填业务逻辑判断
        if (property == null || property.isEmpty()) {
            throw new BusinessException("修改数据源失败，数据源属性为空:" + sourceKey);
        }
        if (!property.containsKey("jdbcUrl") || property.get("jdbcUrl") == null
                || StringUtils.isNullOrEmpty(property.get("jdbcUrl"))) {
            throw new BusinessException("修改数据源失败，数据源url为空:" + sourceKey);
        }
        if (!property.containsKey("driverClassName") || property.get("driverClassName") == null
                || StringUtils.isNullOrEmpty(property.get("driverClassName"))) {
            throw new BusinessException("修改数据源失败，数据源驱动为空:" + sourceKey);
        }
        if (!property.containsKey("username") || property.get("username") == null
                || StringUtils.isNullOrEmpty(property.get("username"))) {
            throw new BusinessException("修改数据源失败，数据源用户名为空:" + sourceKey);
        }
        if (!property.containsKey("password") || property.get("password") == null
                || StringUtils.isNullOrEmpty(property.get("password"))) {
            throw new BusinessException("修改数据源失败，数据源密码为空:" + sourceKey);
        }
        String jdbcUrl = property.get("jdbcUrl").trim();
        String driverClassName = property.get("driverClassName").trim();
        String username = property.get("username").trim();
        String password = property.get("password").trim();
        // 切换数据源
        DbContextHolder.setDbType(sourceKey);
        // 将已经存在的数据源取出
        AtomikosDataSourceBean sourceBean = (AtomikosDataSourceBean) determineTargetDataSource();
        // 关闭数据源连接
        sourceBean.close();
        // 情况xa对象，如果不清空的话，数据源修改不成功
        sourceBean.setXaDataSource(null);
        sourceBean.setUniqueResourceName(sourceKey);
        sourceBean.setXaDataSourceClassName(driverClassName);
        Properties xaProperties = new Properties();
        xaProperties.setProperty("URL", jdbcUrl);
        xaProperties.setProperty("user", username);
        xaProperties.setProperty("password", password);
        sourceBean.setXaProperties(xaProperties);
        // 重新初始化数据源
        try {
            sourceBean.init();
        } catch (AtomikosSQLException e) {
            throw new BusinessException("重新加载数据源失败:"+e.getMessage());
        }
        // 添加新的数据源到数据源列表
        this.targetDataSources.put(sourceKey, sourceBean);
        // 调用父类方法重新设置数据源列表
        super.setTargetDataSources(targetDataSources);
        // 刷新数据源信息
        afterPropertiesSet();

        // 初始化修改的数据源
        initDataSourceStatus(sourceKey);
    }

    /**
     * 删除数据源
     *
     * @param key 数据源 uniqueKey
     * @throws Exception
     * @author zhongdt 2016年3月10日
     */
    public void removeDataSourceByKey(String key) throws BusinessException {
        if (key == null || StringUtils.isNullOrEmpty(key)) {
            return;
        }
        // 判断是否存在
        if (!targetDataSources.containsKey(key)) {
            throw new BusinessException("数据源不存在");
        }
        // 同时删除数据源状态列表
        this.avaiableKeys.remove(key);
        this.disableKeys.remove(key);

        targetDataSources.remove(key);
        // 重新刷新数据源
        super.setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }

    /**
     * 设置可用的数据源key列表
     * 此方法供监控线程调用
     * 
     * @param keys
     * @author zhongdt 2016年3月25日
     */
    public void setAvaiableKeys(List<String> keys) {
        this.avaiableKeys = keys;
    }

    public void removeAvaiableKey(String key) {
        if (this.avaiableKeys.contains(key)) {
            this.avaiableKeys.remove(key);
        }
    }

    public void removeDisableKey(String key) {
        if (this.disableKeys.contains(key)) {
            this.disableKeys.remove(key);
        }
    }

    /**
     * 验证数据源是否存在
     *
     * @param 数据源唯一标识
     * @return true or false
     * @author zhongdt 2016年3月8日
     */
    public boolean isExists(String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            return false;
        }
        return targetDataSources.containsKey(key);
    }

    /**
     * 判断数据库是否正常
     *
     * @param key
     * @return true false
     * @author zhongdt 2016年3月21日
     */
    public boolean isValid(String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            return false;
        }
        return avaiableKeys.contains(key) ? true : false;
    }

    public Map<Object, Object> getTargets() {
        return this.targetDataSources;
    }
    
    /**
     * 
     * 解析数据库连接失败时返回的最底层的原因
     *
     * @param e
     * @return 数据库抛出的exception
     * @author zhongdt 2016年5月19日
     */
    public  Exception getCause(Exception e) {
        Exception cause = new Exception();
        //递归取最后一层的cause
        while (e != null && (e = (Exception) e.getCause()) != null) {
            cause = e;
            continue;
        }
        return cause;
    }

    /**
     * 初始化单个数据源
     * 方法的注释
     *
     * @param sourcekey
     * @author zhongdt 2016年5月3日
     */
    private void initDataSourceStatus(String sourcekey) {
        // 删除可用 及不可用标识，重新加载
        disableKeys.remove(sourcekey);
        avaiableKeys.remove(sourcekey);
        List<String> keys = new ArrayList<String>();
        // 遍历数据源对象
        for (Entry<Object, Object> entry : this.targetDataSources.entrySet()) {

            String key = entry.getKey() + "";

            if (!sourcekey.equals(key)) {
                continue;
            }
            // 先切换一次数据源，然后再取连接，如果能取到，说明连接正常
            DbContextHolder.setDbType(entry.getKey() + "");
            try (Connection conn = getConnection()) {
                // 取出一个连接，异常时候会报错
                keys.add(key);
            } catch (Exception e) {
                // 为了避免数据库被锁
                // 只要是有报错，就把数据源信息放到不可用列表中，不在监控，需要在前端通过手动方式查看
                disableKeys.add(entry.getKey() + "");
                logger.error("获取数据源conn失败, uniqueKey =:" + entry.getKey() + ",reason="+ getCause(e).getMessage());
                return;
            }
            avaiableKeys.add(key);
        }

    }

   

    /**
     * 初始化所有数据源连接状态
     *
     * @author zhongdt 2016年5月6日
     */
    private void initDataSourceStatus() {
        List<String> keys = new ArrayList<String>();
        for (Entry<Object, Object> entry : this.targetDataSources.entrySet()) {

            String key = entry.getKey() + "";
            // 如果数据源在不可用的列表中则不进行数据库连接，原因是重复操作会锁用户
            if (disableKeys.contains(key)) {
                continue;
            }
            // 切换数据源
            DbContextHolder.setDbType(entry.getKey() + "");
            // 获取连接，获取成功则数据源正常，失败则不正常，失败后为了避免锁账户，不再重试
            try (Connection conn = getConnection()) {
                // 取出一个连接，异常时候会报错
                keys.add(key);
            } catch (Exception e) {
                // 为了避免数据库被锁
                // 只要是有报错，就把数据源信息放到不可用列表中，不在监控，需要在前端通过手动方式查看
                disableKeys.add(entry.getKey() + "");
                logger.error("获取数据源conn失败, uniqueKey =:" + entry.getKey() + ",reason=" + getCause(e).getMessage());

            }
        }
        // 重新设置 可用数据源列表
        setAvaiableKeys(keys);
    }

    // 数据源切换context
    public static class DbContextHolder {
        // 现成安全对象，保存当前线程中使用的数据源
        private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

        public static void setDbType(String dbType) {
            contextHolder.set(dbType);
        }

        public static String getDbType() {
            return (String) contextHolder.get();
        }

        public static void clearDbType() {
            contextHolder.remove();
        }
    }

}
