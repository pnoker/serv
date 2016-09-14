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
package com.dc.city.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dc.city.common.log.mapper.RealRegionMapper;
import com.dc.city.common.vo.BusinessVo;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * 反射类
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年6月21日 下午6:15:27
 *          Copyright 2015 by DigitalChina
 */
public class ReflectUtil {

    private static Log log = LogFactory.getLog("reflectUtil");

    /**
     * 实体类路径
     */
    public static final String DOMAIN_CLASS_PATH = LocaleMessage.getValueByKey("real.domianClassPath");

    /**
     * 通用保存表单提交项方法名字
     */
    public static final String COMMON_SAVE_DOMAIN_METHOD_NAME = "saveDomain";

    /**
     * 通用保存excel方法名字
     */
    public static final String COMMON_SAVE_EXCEL_METHOD_NAME = "saveExcel";

    /**
     * 通用保存方法service名字
     */
    public static final String COMMON_SAVE_SERVICE_NAME = "pageInputDispatcherService";

    /**
     * 公共VO
     */
    private static final String BASE_VO = "BusinessVo";

    /**
     * regionalId
     */
    private static final String REGIONAL_ID = "regionalId,regionName";

    /**
     * 是否验证字段长度标志
     */
    private static final String DATA_IS_VALIDATE_FLAG = "-1";

    /**
     * 字符串默认长度
     */
    private static final int STRING_DEFAULT_LENGTH = 4000;

    /**
     * 整型默认长度
     */
    private static final String NUMBER_DEFAULT_LENGTH = "10";

    /**
     * 浮点型默认长度
     */
    private static final String FLOAT_DEFAULT_LENGTH = "5|2";

    /**
     * 是否必填
     */
    public static final String IS_NECESSARY = "1";
    
    
    /**
     * json反射生成对象,返回BusinessVo，属性List<Object>
     * 
     * @param invokeClassPath
     * @param pageFieldValues
     * @return
     * @author zuoyue 2015年6月10日
     */
    public static BusinessVo reflectObject(Class<?> cla, String pageFieldValues) {
        BusinessVo businessVo = new BusinessVo();
        List<?> objectList = null;
        try {
            ObjectMapper mapper = new ObjectMapper(); 
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);   
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, cla);
            objectList = mapper.readValue(pageFieldValues, javaType);
        }catch (JsonParseException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        }
        businessVo.setDatas(objectList);
        return businessVo;
    }
    

    /**
     * 解析得到JsonNode
     *
     * @param jsonValue
     * @return
     * @author zuoyue 2015年8月13日
     */
    public static JsonNode analyticalParentNode(String jsonValue) {  
        JsonNode parentNode = null;
        try {
            // 解析json
            ObjectMapper mapper = new ObjectMapper();
            parentNode = mapper.readTree(jsonValue);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return parentNode;
    }
    
    

    /**
     * json反射生成对象,返回BusinessVo，属性List<Object>
     * 
     * @param invokeClassPath
     * @param pageFieldValues
     * @return
     * @author zuoyue 2015年6月10日
     */
    public static BusinessVo reflectObject(String invokeClassPath, String pageFieldValues) {
        BusinessVo businessVo = new BusinessVo();
        List<?> objectList = null;
        try {
            Class<?> beanClass = Class.forName(DOMAIN_CLASS_PATH + invokeClassPath);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat(DateUtils.DATE_FORMAT_2));
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanClass);
            objectList = mapper.readValue(pageFieldValues, javaType);
        } catch (ClassNotFoundException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (JsonParseException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        }
        businessVo.setDatas(objectList);
        return businessVo;
    }

    /**
     * list反射生成对象
     *
     * @param dataList
     * @param invokeClassPath
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @author zuoyue 2015年6月22日
     */
    public static List<Object> reflectObject(List<Map<String, Object>> dataList, String invokeClassPath)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException,
            SecurityException {
        List<Object> objectList = new ArrayList<Object>();
        Class<?> beanClass = Class.forName(DOMAIN_CLASS_PATH + invokeClassPath);
        for (Map<String, Object> map : dataList) {
            // 每一行
            if (map != null && map.size() > 0) {
                // 创建对象
                Object object = beanClass.newInstance();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String fieldName = entry.getKey();
                    Object fieldValue = entry.getValue();
                    Field f = beanClass.getDeclaredField(fieldName);
                    /**** add by chenzpa 将获取到的值类型设置成bean里面原本的类型 *****/
                    String typeString = f.getGenericType().toString().toLowerCase();
                    switch (typeString) {
                        case "int":
                            fieldValue = Integer.valueOf(String.valueOf(fieldValue));
                            break;
                        case "long":
                            fieldValue = Long.valueOf(String.valueOf(fieldValue));
                            break;
                        case "double":
                            fieldValue = Double.valueOf(String.valueOf(fieldValue));
                            break;
                        case "float":
                            fieldValue = Float.valueOf(String.valueOf(fieldValue));
                            break;
                        case "class java.lang.string":
                            fieldValue = String.valueOf(fieldValue);
                            break;
                        case "class java.util.date":
                            fieldValue = DateUtils.parse(
                                    String.valueOf(fieldValue).contains(DateUtils.CONNECTOR_2) ? String
                                            .valueOf(fieldValue) : String.valueOf(fieldValue) + DateUtils.MIN_SECONDS,
                                    DateUtils.DATE_FORMAT_2);
                            break;
                        default:
                            break;
                    }
                    /********************* end *******************************/
                    f.setAccessible(true);
                    f.set(object, fieldValue);
                }
                objectList.add(object);
            }
        }
        return objectList;
    }

    /**
     * 反射调用方法
     *
     * @param beanName
     * @param methodName
     * @param classType
     * @param params
     * @return
     * @author zuoyue 2015年6月22日
     */
    public static BusinessVo reflectInvokeMethod(String beanName, String methodName, Class<?>[] classType,
            Object[] params) {
        BusinessVo businessVo = new BusinessVo();
        try {
            Object object = ApplicationContextUtil.getBean(beanName);
            Method method = object.getClass().getDeclaredMethod(methodName, classType);
            method.setAccessible(true);
            if (method.getReturnType().toString().contains(BASE_VO)) {
                businessVo = (BusinessVo) method.invoke(object, params);
            } else {
                int size = (int) method.invoke(object, params);
                if (size == 0) {
                    businessVo.setResultCode(BusinessVo.SQL_ERROR_CODE);
                    businessVo.setResultInfo(BusinessVo.SQL_ERROR_MSG);
                }
            }
        } catch (NoSuchMethodException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (SecurityException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        }
        return businessVo;
    }

    /**
     * 验证数据类型与长度是否正确
     *
     * @param list
     * @param fieldColumnMap
     * @return BusinessVo,datas:List<Map<String, Object>>
     * @author zuoyue 2015年6月22日
     */
    public static BusinessVo validateData(String pageFieldValues, String fieldTypeArr, String fieldLengthArr,
            String headerEnglishArr, String headerChineseArr, String isNecessaryArr) {
        BusinessVo businessVo = new BusinessVo();
        try {
            Map<String, String> fieldTypeMap = ReadExcel.acquireContrastMap(headerEnglishArr, fieldTypeArr);
            Map<String, String> fieldLengthMap = ReadExcel.acquireContrastMap(headerEnglishArr, fieldLengthArr);
            Map<String, String> fieldChineseMap = ReadExcel.acquireContrastMap(headerEnglishArr, headerChineseArr);
            Map<String, String> fieldNecessaryMap = ReadExcel.acquireContrastMap(headerEnglishArr, isNecessaryArr);
            Map<String, Pattern> fieldPatternMap = acquireFieldPatternMap(fieldTypeMap, fieldLengthMap);
            // 解析json
            ObjectMapper mapper = new ObjectMapper();
            JsonNode parentNode = mapper.readTree(pageFieldValues);
            // 行数
            int lineNumber = 1;
            breakCycle:
            for (int i = 0; i < parentNode.size(); i++) {
                JsonNode childNode = parentNode.get(i);
                // 遍历map
                for (Map.Entry<String, String> entry : fieldTypeMap.entrySet()) {
                    // 属性名
                    String fieldName = entry.getKey();
                    // 属性类型
                    String fieldType = entry.getValue();
                    // 属性长度
                    String fieldLengthStr = fieldLengthMap.get(fieldName);
                    // 属性中文名
                    String fieldChinese = fieldChineseMap.get(fieldName);
                    // 是否必填
                    String isNecessary = fieldNecessaryMap.get(fieldName);
                    // 属性Pattern
                    Pattern pattern = fieldPatternMap.get(fieldName);
                    // 属性值
                    Object value = RegexUtils.REGEX_QUOTES.matcher(String.valueOf(childNode.get(fieldName)))
                            .replaceAll("");
                    // 非必填不验证
                    if (isMustValidate(String.valueOf(value), isNecessary)) {
                        businessVo = isPassValidation(fieldName, fieldType, fieldLengthStr, fieldChinese, pattern,
                                String.valueOf(value), lineNumber);
                        // 验证不通过
                        if (!BusinessVo.SUCCESS_CODE.equals(businessVo.getResultCode())) {
                            break breakCycle;
                        }
                    }
                }
                lineNumber++;
            }
        } catch (IllegalArgumentException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (JsonProcessingException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        }
        return businessVo;
    }

    /**
     * 是否为必须验证的字段
     *
     * @param value
     * @param isNecessary
     * @return
     * @author zuoyue 2015年7月2日
     */
    public static boolean isMustValidate(String value, String isNecessary) {
        boolean isMustValidateFlag = true;
        if (!IS_NECESSARY.equals(isNecessary) && !isArrayNotNull(value)) {
            isMustValidateFlag = false;
        }
        return isMustValidateFlag;
    }

    /**
     * 验证数据类型与长度是否正确，并映射数据库字段与对应的值
     *
     * @param list
     * @param fieldColumnMap
     * @param fieldTypeArr
     * @param fieldLengthArr
     * @param headerEnglishArr
     * @param headerChineseArr
     * @return BusinessVo,datas:List<Map<String, Object>>
     * @author zuoyue 2015年6月22日
     */
    public static BusinessVo validateAndMappingColumnValue(List<?> list, Map<String, String> fieldColumnMap,
            String fieldTypeArr, String fieldLengthArr, String headerEnglishArr, String headerChineseArr) {
        List<Map<String, Object>> columnValueList = null;
        BusinessVo businessVo = new BusinessVo();
        try {
            Map<String, String> fieldTypeMap = ReadExcel.acquireContrastMap(headerEnglishArr, fieldTypeArr);
            Map<String, String> fieldLengthMap = ReadExcel.acquireContrastMap(headerEnglishArr, fieldLengthArr);
            Map<String, String> fieldChineseMap = ReadExcel.acquireContrastMap(headerEnglishArr, headerChineseArr);
            Map<String, Pattern> fieldPatternMap = acquireFieldPatternMap(fieldTypeMap, fieldLengthMap);
            // 字段值list
            columnValueList = new ArrayList<Map<String, Object>>();
            // 行数
            int lineNumber = 1;
            breakCycle:
            for (Object object : list) {
                Class<?> clazz = object.getClass();
                // 获得属性
                Field[] fields = clazz.getDeclaredFields();
                Map<String, Object> columnValueMap = new HashMap<String, Object>();
                for (Field field : fields) {
                    // 获得属性名
                    String fieldName = field.getName();
                    // 获得字段名
                    String columnName = fieldColumnMap.get(fieldName);
                    // 若取得了字段名
                    if (StringUtils.isNotBlank(columnName)) {
                        PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
                        // 获得get方法
                        Method getterMethod = pd.getReadMethod();
                        // 执行get方法返回一个Object
                        Object value = getterMethod.invoke(object);
                        // 属性类型
                        String fieldType = fieldTypeMap.get(fieldName);
                        // 属性长度
                        String fieldLengthStr = fieldLengthMap.get(fieldName);
                        // 属性中文名
                        String fieldChinese = fieldChineseMap.get(fieldName);
                        // 属性Pattern
                        Pattern pattern = fieldPatternMap.get(fieldName);
                        // 验证数据
                        businessVo = isPassValidation(fieldName, fieldType, fieldLengthStr, fieldChinese, pattern,
                                String.valueOf(value), lineNumber);
                        // 验证不通过
                        if (!BusinessVo.SUCCESS_CODE.equals(businessVo.getResultCode())) {
                            break breakCycle;
                        }
                        columnValueMap.put(columnName, value);
                    }
                }
                columnValueList.add(columnValueMap);
                lineNumber++;
            }
        } catch (IllegalAccessException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IntrospectionException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        }
        businessVo.setDatas(columnValueList);
        return businessVo;
    }

    /**
     * 各字段是否通过验证
     *
     * @param fieldType
     * @param fieldLengthStr
     * @param fieldChinese
     * @param pattern
     * @param value
     * @param lineNumber
     * @return
     * @author zuoyue 2015年6月29日
     */
    public static BusinessVo isPassValidation(String fieldName, String fieldType, String fieldLengthStr,
            String fieldChinese, Pattern pattern, String value, int lineNumber) {
        BusinessVo businessVo = new BusinessVo();
        // 获取到了属性类型与长度
        if (isArrayNotNull(fieldType, fieldLengthStr, fieldChinese)) {
            if (isArrayNotNull(value)) {
                // 校验属性类型与长度
                String errorMsg = "";
                if (REGIONAL_ID.contains(fieldName)) {
                    errorMsg = validateRegion(value.trim());
                } else {
                    errorMsg = CheckField(fieldType, fieldLengthStr, value, pattern);
                }
                if (StringUtils.isNotBlank(errorMsg)) {
                    // 属性类型或长度验证失败
                    businessVo.setResultCode(BusinessVo.IS_DATA_VALIDATE_ERROR_CODE);
                    businessVo.setResultInfo(String.format(BusinessVo.DATA_COLUMN_MSG, lineNumber, fieldChinese,
                            errorMsg));
                    log.error("属性类型或长度验证失败");
                }
            } else {
                businessVo.setResultCode(BusinessVo.DATA_BLANK_ERROR_CODE);
                businessVo.setResultInfo(String.format(BusinessVo.DATA_BLANK_ERROR_MSG, lineNumber, fieldChinese));
                log.error("没有填写数据");
            }
        } else {
            businessVo.setResultCode(BusinessVo.IS_DATA_VALIDATE_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.IS_DATA_VALIDATE_ERROR_MSG);
            log.error("参数不完整");
        }
        return businessVo;
    }

    /**
     * 校验某一个属性类型与长度
     *
     * @param fieldType
     * @param fieldLengthStr
     * @param value
     * @param pattern
     * @return
     * @author zuoyue 2015年6月28日
     */
    public static String CheckField(String fieldType, String fieldLengthStr, String value, Pattern pattern) {
        String errorMsg = "";
        // 校验属性类型与长度
        switch (fieldType) {
            case "String":
                errorMsg = getWordLength(value, null) > (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr) ? STRING_DEFAULT_LENGTH
                        : Integer.parseInt(fieldLengthStr)) == true ? BusinessVo.CONTENT_TOO_LONG_MSG : "";
                break;
            case "int":
                fieldLengthStr = (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr)) ? NUMBER_DEFAULT_LENGTH
                        : fieldLengthStr;
                errorMsg = pattern.matcher(value).matches() == true ? "" : String.format(BusinessVo.NUMBER_ERROR_MSG,
                        fieldLengthStr);
                break;
            case "Date":
                errorMsg = RegexUtils.REGEX_DATE.matcher(value).matches() == true ? "" : BusinessVo.DATE_ERROR_MSG;
                break;
            case "float":
                fieldLengthStr = (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr)) ? FLOAT_DEFAULT_LENGTH : fieldLengthStr;
                // 按精度四舍五入,如excel中填写的18030.78，但是得到的是18030.77999999999883584678173065185546875这类数字
                value = accuracyRetention(Float.parseFloat(value), fieldLengthStr.split("\\|")[1]);
                errorMsg = pattern.matcher(value).matches() == true ? "" : String.format(BusinessVo.FLOAT_ERROR_MSG,
                        fieldLengthStr.split("\\|")[0], fieldLengthStr.split("\\|")[1]);
                break;
            case "long":
                fieldLengthStr = (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr)) ? NUMBER_DEFAULT_LENGTH
                        : fieldLengthStr;
                errorMsg = pattern.matcher(value).matches() == true ? "" : String.format(BusinessVo.NUMBER_ERROR_MSG,
                        fieldLengthStr);
                break;
            case "double":
                fieldLengthStr = (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr)) ? FLOAT_DEFAULT_LENGTH : fieldLengthStr;
                // 按精度四舍五入,如excel中填写的18030.78，但是得到的是18030.77999999999883584678173065185546875这类数字
                value = accuracyRetention(Double.parseDouble(value), fieldLengthStr.split("\\|")[1]);
                errorMsg = pattern.matcher(value).matches() == true ? "" : String.format(BusinessVo.FLOAT_ERROR_MSG,
                        fieldLengthStr.split("\\|")[0], fieldLengthStr.split("\\|")[1]);
                break;
            default:
                break;
        }
        return errorMsg;
    }

    /**
     * 属性与数据库字段,数据库字段与列类型对照关系
     *
     * @param classPath
     * @return
     * @author zuoyue 2015年6月22日
     */
    public static Map<String, Object> mappingAttributeField(String classPath) {
        Map<String, Object> fieldColumnTypeMap = null;
        Map<String, String> fieldColumnMap = null;
        Map<String, String> columnTypeMap = null;
        Map<String, String> columnCorrelationMap = null;
        try {
            fieldColumnTypeMap = new HashMap<String, Object>();
            fieldColumnMap = new HashMap<String, String>();
            columnTypeMap = new HashMap<String, String>();
            columnCorrelationMap = new HashMap<String, String>();
            // 得到class
            Class<?> beanClass = Class.forName(classPath);
            String tableName = (beanClass.getAnnotation(RealClassAnnotation.class) != null) ? beanClass.getAnnotation(
                    RealClassAnnotation.class).tableName() : convertCharacter(beanClass.getSimpleName());
            log.debug("得到的表名为：" + tableName);
            fieldColumnTypeMap.put(classPath, tableName);
            // 获得属性列表
            Field fieldlist[] = beanClass.getDeclaredFields();
            for (Field field : fieldlist) {
                String columnName = "";
                String columnType = "";
                String fieldName = field.getName();
                if (!"id".equalsIgnoreCase(fieldName) && !"updatetime".equalsIgnoreCase(fieldName)) {
                    // 若配置了注解
                    if (field.getAnnotation(RealFieldAnnotation.class) != null) {
                        if (field.getAnnotation(RealFieldAnnotation.class).isSave()) {
                            columnName = StringUtils.isNotBlank(field.getAnnotation(RealFieldAnnotation.class)
                                    .columnName()) ? field.getAnnotation(RealFieldAnnotation.class).columnName()
                                    : convertCharacter(fieldName);
                            columnType = stringIsNotNull(String.valueOf(field.getAnnotation(RealFieldAnnotation.class)
                                    .columnType())) ? String.valueOf(field.getAnnotation(RealFieldAnnotation.class)
                                    .columnType()) : String.valueOf(field.getType()).toUpperCase();
                        }
                    } else {
                        columnName = convertCharacter(fieldName);
                        columnType = String.valueOf(field.getType()).toUpperCase();
                    }
                    if (isArrayNotNull(columnName, columnType, fieldName)) {
                        fieldColumnMap.put(fieldName, columnName);
                        columnTypeMap.put(columnName, columnType);
                    }
                }
                // 若配置了关联字段注解
                if (field.getAnnotation(RealFieldAnnotation.class) != null) {
                    if (field.getAnnotation(RealFieldAnnotation.class).isCorrelation()) {
                        columnCorrelationMap.put(columnName, columnName);
                    }
                }
            }
            fieldColumnTypeMap.put("fieldColumnMap", fieldColumnMap);
            fieldColumnTypeMap.put("columnTypeMap", columnTypeMap);
            fieldColumnTypeMap.put("columnCorrelationMap", columnCorrelationMap);
            log.debug("属性与数据库字段对照关系：" + fieldColumnMap.entrySet());
            log.debug("数据库字段与列类型对照关系：" + columnTypeMap.entrySet());
            log.debug("关联字段：" + columnCorrelationMap.entrySet());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return fieldColumnTypeMap;
    }

    /**
     * 数据库字段与对应值
     *
     * @param list
     * @param fieldColumnMap
     * @return BusinessVo,datas:List<Map<String, Object>>
     * @author zuoyue 2015年6月22日
     */
    public static BusinessVo mappingColumnValue(List<?> list, Map<String, String> fieldColumnMap) {
        List<Map<String, Object>> columnValueList = null;
        BusinessVo businessVo = new BusinessVo();
        try {
            columnValueList = new ArrayList<Map<String, Object>>();
            for (Object object : list) {
                Class<?> clazz = object.getClass();
                // 获得属性
                Field[] fields = clazz.getDeclaredFields();
                Map<String, Object> columnValueMap = new HashMap<String, Object>();
                for (Field field : fields) {
                    // 获得属性名
                    String fieldName = field.getName();
                    // 获得字段名
                    String columnName = fieldColumnMap.get(fieldName);
                    // 若取得了字段名
                    if (StringUtils.isNotBlank(columnName)) {
                        PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
                        // 获得get方法
                        Method getMethod = pd.getReadMethod();
                        // 执行get方法返回一个Object
                        Object value = getMethod.invoke(object);
                        columnValueMap.put(columnName, value);
                    }
                }
                columnValueList.add(columnValueMap);
            }
        } catch (IllegalAccessException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        } catch (IntrospectionException e) {
            businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
            log.error(e.getMessage(), e);
        }
        businessVo.setDatas(columnValueList);
        return businessVo;
    }

    /**
     * Field与Pattern的对照关系
     *
     * @param fieldTypeMap
     * @param fieldLengthMap
     * @return
     * @author zuoyue 2015年6月29日
     */
    public static Map<String, Pattern> acquireFieldPatternMap(Map<String, String> fieldTypeMap,
            Map<String, String> fieldLengthMap) {
        Map<String, Pattern> fieldPatternMap = new HashMap<String, Pattern>();
        for (Map.Entry<String, String> entry : fieldTypeMap.entrySet()) {
            // 属性名
            String fieldName = entry.getKey();
            // 属性类型
            String fieldType = entry.getValue();
            // 属性长度
            String fieldLengthStr = fieldLengthMap.get(fieldName);
            // 正则
            String regex = "";
            Pattern pattern = null;
            switch (fieldType) {
                case "int":
                    fieldLengthStr = (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr)) ? NUMBER_DEFAULT_LENGTH
                            : fieldLengthStr;
                    regex = String.format(RegexUtils.REGEX_WHOLE_NUMBER, Integer.parseInt(fieldLengthStr));
                    break;
                case "float":
                    fieldLengthStr = (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr)) ? FLOAT_DEFAULT_LENGTH
                            : fieldLengthStr;
                    regex = String.format(RegexUtils.REGEX_FLOATING_NUMBER,
                            Integer.parseInt(fieldLengthStr.split("\\|")[0]),
                            Integer.parseInt(fieldLengthStr.split("\\|")[1]));
                    break;
                case "long":
                    fieldLengthStr = (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr)) ? NUMBER_DEFAULT_LENGTH
                            : fieldLengthStr;
                    regex = String.format(RegexUtils.REGEX_WHOLE_NUMBER, Integer.parseInt(fieldLengthStr));
                    break;
                case "double":
                    fieldLengthStr = (DATA_IS_VALIDATE_FLAG.equals(fieldLengthStr)) ? FLOAT_DEFAULT_LENGTH
                            : fieldLengthStr;
                    regex = String.format(RegexUtils.REGEX_FLOATING_NUMBER,
                            Integer.parseInt(fieldLengthStr.split("\\|")[0]),
                            Integer.parseInt(fieldLengthStr.split("\\|")[1]));
                    break;
                default:
                    break;
            }
            pattern = Pattern.compile(regex);
            fieldPatternMap.put(fieldName, pattern);
        }
        return fieldPatternMap;
    }

    /**
     * 通过key获取fieldColumnTypeMap的value
     *
     * @param classPath
     * @param key
     * @return
     * @author zuoyue 2015年6月22日
     */
    public static Object obtainObject(String classPath, String key) {
        return mappingAttributeField(classPath).get(key);
    }

    /**
     * 字符串判断
     *
     * @param str
     * @return
     * @author zuoyue 2015年6月18日
     */
    public static boolean stringIsNotNull(String str) {
        return StringUtils.isNotBlank(str) && !"null".equalsIgnoreCase(str);

    }

    /**
     * 字符串判断
     *
     * @param str
     * @return
     * @author zuoyue 2015年6月18日
     */
    public static boolean stringIsNull(String str) {
        return StringUtils.isBlank(str) || "null".equalsIgnoreCase(str);
    }

    /**
     * Sring[]里面的各项是否为null
     *
     * @param params
     * @return
     * @author zuoyue 2015年6月12日
     */
    public static boolean isArrayNotNull(String... params) {
        boolean isArrayNotNullFlag = true;
        if (params != null && params.length > 0) {
            for (String str : params) {
                if (StringUtils.isBlank(str) || "null".equalsIgnoreCase(str)) {
                    isArrayNotNullFlag = false;
                    break;
                }
            }
        }
        return isArrayNotNullFlag;
    }

    /**
     * 字符转换
     *
     * @param originalCharacter
     * @return
     * @author zuoyue 2015年6月24日
     */
    private static String convertCharacter(String originalCharacter) {
        String targetCharacter = "";
        StringBuffer sbuffer = new StringBuffer();
        for (int i = 0; i < originalCharacter.length(); i++) {
            if (Character.isUpperCase(originalCharacter.charAt(i))) {
                sbuffer.append("_").append(originalCharacter.charAt(i));
            } else {
                sbuffer.append(originalCharacter.charAt(i));
            }
        }
        targetCharacter = sbuffer.toString();
        if (targetCharacter.startsWith("_")) {
            targetCharacter = targetCharacter.substring(1);
        }
        return targetCharacter.toUpperCase();
    }

    /**
     * 特定的编码格式获取长度
     *
     * @param str
     * @param code
     * @return
     * @author zuoyue 2015年6月24日
     */
    public static int getWordLength(String value, String code) {
        int length = 10000;
        try {
            length = value.getBytes(StringUtils.isNotBlank(code) ? code : "UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return length;
    }

    /**
     * 是否包含字符
     *
     * @param character
     * @return
     * @author zuoyue 2015年6月24日
     */
    @SuppressWarnings("unused")
    private static boolean checkCharacter(String character) {
        boolean isCharacterFlag = false;
        for (int i = 0; i < character.length(); i++) {
            int ascii = Character.codePointAt(character, i);
            if ((ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122)) {
                isCharacterFlag = true;
                break;
            }
        }
        return isCharacterFlag;
    }

    /**
     * 验证地市名称
     *
     * @param regionName
     * @return
     * @author zuoyue 2015年6月30日
     */
    private static String validateRegion(String regionName) {
        String errorMsg = "";
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) ApplicationContextUtil
                .getBean("masterSqlSessionFactory");
        SqlSession session = sqlSessionFactory.openSession();
        RealRegionMapper regionMapper = session.getMapper(RealRegionMapper.class);
        String regions = regionMapper.queryRegions();
        errorMsg = regions.contains(regionName) ? "" : String.format(BusinessVo.REGION_ERROR_MSG, regions);
        return errorMsg;
    }

    /**
     * 按精度保留
     *
     * @param originalValue
     * @param precision
     * @return
     * @author zuoyue 2015年7月6日
     */
    private static String accuracyRetention(Object originalValue, String precision) {
        return String.format(new StringBuffer().append("%.").append(precision).append("f").toString(), originalValue);

    }

}
