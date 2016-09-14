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
package com.dc.city.service.database;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.sql.CLOB;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dc.city.common.datasource.DynamicDataSource;
import com.dc.city.common.utils.DateUtils;
import com.dc.city.vo.database.DataBaseExcuteVo;

/**
 * 数据库执行service
 * module：数据库操作模块
 * 负责处理 纯SQL语句执行
 * 
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月7日 下午4:26:56
 *          Copyright 2016 by DigitalChina
 */
@Service
public class ServeDataBaseExcuteService {

    // 默认返回前端条数
    public static int TOTAL_LENGTH = 100;

    @Resource
    private NamedParameterJdbcTemplate jdbcT;

    @Resource
    // 动态数据源bean
    private DynamicDataSource dynamicDataSource;

    /**
     * 数据库执行sql的默认方法
     *
     * @param sql 数据库SQL语句
     * @return 返回数据库执行结果，select 返回查询结果，其他返回执行结果
     * @author zhongdt 2016年3月25日
     * @throws Exception 
     */
    public DataBaseExcuteVo doDbExcuteVo(String sql) throws Exception {
        return doExcute(sql, TOTAL_LENGTH);
    }

    /**
     * 执行sql语句service方法
     *
     * @param sql 数据库SQL语句
     * @param fetchSize 返回数据条数：当sql为query时，才有用
     * @return 返回数据库执行结果，select 返回查询结果，其他返回执行结果
     * @author zhongdt 2016年3月25日
     */
    public DataBaseExcuteVo doExcute(String sql, final int fetchSize) throws Exception {
        final long currentTime = System.currentTimeMillis();
        final DataBaseExcuteVo vo = new DataBaseExcuteVo();

        if (sql.trim().toUpperCase().startsWith("SELECT")) {
            // 执行query方法
            return jdbcT.query(sql, new ResultSetExtractor<DataBaseExcuteVo>() {
                public DataBaseExcuteVo extractData(ResultSet rs) throws SQLException, DataAccessException {
                    // 生成columns列信息（表头）
                    buildColumnsInfo(vo, rs);
                    // 生成数据 list
                    buildDataInfo(vo, rs, fetchSize);
                    // 设置执行时长(hs)
                    vo.setCostTime((System.currentTimeMillis() - currentTime) / 1000.0);
                    return vo;
                }
            });
        } else {
            // 执行select 以为的SQL语句
            int result = jdbcT.getJdbcOperations().update(sql);
            //执行成功0条时候。返回0
            if (result < 0 ) {
                return new DataBaseExcuteVo("-1", "sql执行失败，请检查语句");
            }
            // 设置执行时长
            vo.setCostTime((System.currentTimeMillis() - currentTime) / 1000);
            return vo;
        }
    }

    /**
     * 根据resultSet 构造返回类信息
     *
     * @param vo
     * @param rs
     * @author zhongdt 2016年3月8日
     */
    private void buildColumnsInfo(DataBaseExcuteVo vo, ResultSet rs) throws SQLException {
            // 得到表头对象
            ResultSetMetaData meta = rs.getMetaData();
            // 获取列长度
            int length = meta.getColumnCount();
            if (length < 0) {
                vo.setResultCode("-1");
                vo.setResultInfo("返回列信息失败");
                return;
            }
            // 循环取出列名
            List<Map<String, String>> columns = new ArrayList<Map<String, String>>();
            for (int i = 1; i <= length; i++) {
                //封装data对象
                Map<String, String> column = new HashMap<String, String>();
                column.put("data", meta.getColumnName(i));
                columns.add(column);
            }
            vo.setColumns(columns);
    }

    /**
     * 根据resultSet 封装数据
     *
     * @param vo 数据库执行vo对象，传进来对象再传出去
     * @param rs 数据库返回结果集
     * @param fetchSize 数据返回条数
     * @author zhongdt 2016年3月8日
     */
    private void buildDataInfo(DataBaseExcuteVo vo, ResultSet rs, int fetchSize) {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        try {
            // 由于没分页，默认只返回前面10条
            while (rs.next() && fetchSize-- > 0) {
                Map<String, Object> result = new HashMap<String, Object>();
                for (Map<String, String> column : vo.getColumns()) {
                    String key = column.get("data");
                    // 特殊数据类型转换
                    Object obj = processSpecialData(rs.getObject(key));
                    result.put(key, obj);
                }
                results.add(result);
            }
        } catch (SQLException e) {
            vo.setResultCode("-1");
            vo.setResultCode("解析返回结果失败");
            return;
        }
        vo.setDataSize(results.size());
        vo.setDatas(results);
    }

    // 时间戳格式
    public static String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 特殊类型转换
     * 目前发现timestamp需要进行处理，要不然json转换会报错，
     *
     * @param object 数据库返回列值 ，根据不同类型再进行特殊处理
     * @return 返回处理之后的值
     * @author zhongdt 2016年3月8日
     */
    private Object processSpecialData(Object object) {
        // 时间戳
        if (object instanceof oracle.sql.TIMESTAMP) {
            // 将timestamp转换为string 类型
            return DateUtils.getDateByTimestamp(object, TIMESTAMP_FORMAT);
        }

        // blob
        if (object instanceof oracle.sql.BLOB) {
            try {
                return new String(((oracle.sql.BLOB) object).getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
        // clob
        if (object instanceof oracle.sql.CLOB) {
            return ClobToString((CLOB) object);

        }

        return object;
    }

    /**
     * CLOB转string
     *
     * @param  clob对象
     * @return 返回clob字符串
     * @author zhongdt 2016年5月5日
     */
    private String ClobToString(CLOB clob) {

        try {
            String reString = "";
            Reader is = clob.getCharacterStream();
            // 得到流
            BufferedReader br = new BufferedReader(is);
            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            while (s != null) {
                sb.append(s);
                s = br.readLine();
            }
            reString = sb.toString();
            return reString;
        } catch (Exception e) {
            return "";
        }

    }

}
