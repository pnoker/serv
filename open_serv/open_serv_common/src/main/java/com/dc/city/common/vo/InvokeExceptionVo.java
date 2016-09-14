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
package com.dc.city.common.vo;

/**
 * 调用的错误日志返回的格式vo
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年6月24日 下午4:38:33
 *          Copyright 2015 by DigitalChina
 */
public class InvokeExceptionVo {

    private String COMMON_FAULT = "连接超时";
    private String ERROR_CODE = "-1";
    /**
     * 返回码：0：成功；非0：失败；默认值为0
     */
    private String resultCode = ERROR_CODE;
    /**
     * 返回码信息，默认值为:查询成功
     */
    private String resultInfo = COMMON_FAULT;

    /**
     * 调用出错的方法
     */
    private String methodName;

    /**
     * 出错行数
     */
    private String lineNumber;

    /**
     * 出错的类
     */
    private String className;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

}
