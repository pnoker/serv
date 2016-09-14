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

import java.sql.SQLException;
import java.util.Locale;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.springframework.jdbc.BadSqlGrammarException;

import com.dc.city.common.vo.InvokeExceptionVo;

/**
 * 将错误信息映射成固定的格式
 * 
 * @author xutaog
 * @version V1.0 创建时间：2015年6月24日 上午10:04:05
 *          Copyright 2015 by DigitalChina
 */
@Provider
public class InvokeFaultExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger log = Logger.getLogger(InvokeFaultExceptionMapper.class);

    /**
     * 组装反射信息
     */
    public Response toResponse(Throwable ex) {

        StackTraceElement[] trace = new StackTraceElement[1];
        trace[0] = ex.getStackTrace()[0];// 获取出错的类，方法名称等信息
        // 开始组织错误信息
        InvokeExceptionVo vo = new InvokeExceptionVo();
        vo.setClassName(trace[0].getClassName());
        vo.setLineNumber(String.valueOf(trace[0].getLineNumber()));
        vo.setMethodName(trace[0].getMethodName());
        vo.setResultInfo(ex.getMessage());

        // 特殊判断开始捕获sql操作类异常
        if (isSQLException(ex)) {
            vo.setResultInfo(getSQLExceptionMessage(ex));
            log.error("数据库操作异常错误转换", ex);
        } else {
            ex.printStackTrace();// 打印出错误日志
        }

        // 调用返回创建类 固定的值
        ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.type("application/json;charset=UTF-8");
        rb.entity(vo);
        rb.language(Locale.SIMPLIFIED_CHINESE);
        Response r = rb.build();
        return r;
    }

    /**
     * 是否为SQLException类异常
     *
     * @param throwable controller层抛出的异常
     * @return true or false
     * @author zhongdt 2016年6月23日
     * @remark :所有继承自SQLException 的异常
     *         以及BadSqlGrammarException:此异常认定为sql语法错误
     *         后续其他类似的异常发现后在补充
     */
    public boolean isSQLException(Throwable ex) {
        // 语法错误，也需要过滤
        if (ex instanceof BadSqlGrammarException) {
            return true;
        }
        // 其他需要认定为数据库异常的异常判断在此处添加
        // TODO

        // 判断是否是sqlexception继承过来的
        return SQLException.class.isAssignableFrom(ex.getClass());
    }

    /**
     * 是否为SQLException类异常
     *
     * @param throwable controller层抛出的异常
     * @return 返回错误信息
     * @author zhongdt 2016年6月23日
     * @remark SQLexception 为了过过滤掉具体的信息，因此获取cause的msg
     */
    public String getSQLExceptionMessage(Throwable ex) {
        return ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
    }
}