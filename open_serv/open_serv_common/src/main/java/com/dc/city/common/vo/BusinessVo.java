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

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 业务BusinessVo
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年6月16日 下午5:18:33
 *          Copyright 2015 by DigitalChina
 */
@XmlRootElement(name = "businessVo")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BusinessVo {

    /**
     * 接口调用成功状态码
     */
    public static final String ERROR_CODE = "-1";

    /**
     * 接口调用成功状态码
     */
    public static final String SUCCESS_CODE = "0";
    /**
     * 接口调用成功信息
     */
    public static final String SUCCESS_MSG = "处理成功";

    /**
     * 默认错误
     */
    public static final String GENERAL_ERROR_CODE = "1";
    public static final String GENERAL_ERROR_MSG = "处理异常";

    /**
     * IO_ERROR
     */
    public static final String IO_ERROR_CODE = "2";
    public static final String IO_ERROR_MSG = "excel处理异常，请确认文件内容是否正确";
    /**
     * CLASS_NOT_FOUND_ERROR
     */
    public static final String CLASS_NOT_FOUND_ERROR_CODE = "3";
    public static final String CLASS_NOT_FOUND_ERROR_MSG = "excel未找到";
    /**
     * NO_SUCH_FIELD_ERROR
     */
    public static final String NO_SUCH_FIELD_ERROR_CODE = "4";
    public static final String NO_SUCH_FIELD_ERROR_MSG = "excel标题列无效";
    /**
     * SQL_ERROR
     */
    public static final String SQL_ERROR_CODE = "5";
    public static final String SQL_ERROR_MSG = "提交数据出错";

    /**
     * excel没有某列
     */
    public static final String EXCEL_NO_COLUMN_ERROR_CODE = "6";

    /**
     * excel模板与当前模块不匹配
     */
    public static final String EXCEL_NOT_MATCH_ERROR_CODE = "11";
    public static final String EXCEL_NOT_MATCH_ERROR_MSG = "excel中【%s】标题列不属于当前输入模块，请检查导入模板是否下载正确";

    /**
     * 表单或excel数据未填写
     */
    public static final String NO_DATA_ERROR_CODE = "7";
    public static final String NO_DATA_ERROR_MSG = "请录入相关信息后再提交";

    /**
     * 没有解析出数据，businessVo中datas为null
     */
    public static final String IS_LIST_NULL_ERROR_CODE = "8";
    public static final String IS_LIST_NULL_ERROR_MSG = "保存数据失败";

    /**
     * 没有获取到属性类型与长度
     */
    public static final String IS_DATA_VALIDATE_ERROR_CODE = "9";
    public static final String IS_DATA_VALIDATE_ERROR_MSG = "数据验证失败";

    /**
     * 某列为空
     */
    public static final String DATA_BLANK_ERROR_CODE = "10";
    public static final String DATA_BLANK_ERROR_MSG = "第【%s】行【%s】列不能为空，请填写后再提交";

    /**
     * 列数
     */
    public static final String DATA_COLUMN_MSG = "第【%s】行【%s】列%s";

    /**
     * 字符串过长
     */
    public static final String CONTENT_TOO_LONG_MSG = "内容过长，请修改后再提交";

    /**
     * 只能整数
     */
    public static final String NUMBER_ERROR_MSG = "只能输入【%s位】以内的整数，请修改后再提交";

    /**
     * 日期格式错误
     */
    public static final String DATE_ERROR_MSG = "，日期格式不正确，请修改后再提交";

    /**
     * 只能数字
     */
    public static final String FLOAT_ERROR_MSG = "只能输入数字，其中整数【%s位】以内，小数【%s位】以内，请修改后再提交";

    /**
     * 只能指定区域之一
     */
    public static final String REGION_ERROR_MSG = "只能为【%s】之一，请修改后再提交";

    /**
     * 返回码：0：成功；非0：失败；默认值为0
     */
    private String resultCode = SUCCESS_CODE;
    /**
     * 返回码信息，默认值为:查询成功
     */
    private String resultInfo = SUCCESS_MSG;

    /**
     * 数据
     */
    private List<?> datas;

    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas = datas;
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
