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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.ByteArrayInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Base64Utils;

import com.dc.city.common.vo.BusinessVo;

/**
 * 读取excel表格
 *
 * @author ThinkPad
 * @version V1.0 创建时间：2015年4月28日 下午1:48:37
 *          Copyright 2015 by lg
 */
public class ReadExcel {

    private static Log log = LogFactory.getLog("ReadExcel");

    /**
     * base编码后的字符串
     */
    public static final String ENCODE_STRING = "ENCODE_STRING";
    /**
     * 文件路径
     */
    public static final String PATH_STRING = "PATH_STRING";

    /**
     * 批量条数
     */
    public static final int BATCH_NUMBER = 50;

    /**
     * 参数类型：list<map>
     */
    public static final String PARAM_TYPE_MAP = "LIST_MAP";

    /**
     * 默认的参数类型：list<object>
     */
    public static final String PARAM_TYPE_DEFAULT = "LIST_OBJECT";

    /**
     * 根据methodParamType返回不同的参数类型
     *
     * @param pathOrEncodeStr
     * @param type
     * @param fileName
     * @param headerEnglish
     * @param headerChinese
     * @param invokeClassPath
     * @param fieldType
     * @param fieldLength
     * @param isNecessary
     * @param methodParamType
     * @return
     * @author zuoyue 2015年7月6日
     */
    public static BusinessVo readExcel(String pathOrEncodeStr, String type, String fileName, String headerEnglish,
            String headerChinese, String invokeClassPath, String fieldType, String fieldLength, String isNecessary,
            String methodParamType) {
        BusinessVo businessVo = null;
        // 根据methodParamType返回不同的参数类型
        switch (methodParamType) {
            case PARAM_TYPE_MAP:
                businessVo = readExcel(pathOrEncodeStr, type, fileName, headerEnglish, headerChinese, fieldType,
                        fieldLength, isNecessary);
                break;
            default:
                businessVo = readExcel(pathOrEncodeStr, ReadExcel.ENCODE_STRING, fileName, headerEnglish,
                        headerChinese, invokeClassPath, fieldType, fieldLength, isNecessary);
                break;
        }
        return businessVo;
    }

    /**
     * 通过文件路径或者文件encode编码后的字符串读取excel，返回BusinessVo，属性List<Object>
     *
     * @param pathOrEncodeStr
     * @param type
     * @param fileName
     * @param headerEnglish
     * @param headerChinese
     * @param invokeClassPath
     * @param fieldType
     * @param fieldLength
     * @param isNecessary
     * @return
     * @author zuoyue 2015年6月9日
     */
    @SuppressWarnings("unchecked")
    public static BusinessVo readExcel(String pathOrEncodeStr, String type, String fileName, String headerEnglish,
            String headerChinese, String invokeClassPath, String fieldType, String fieldLength, String isNecessary) {
        List<Object> objectList = null;
        // 验证并解析excel
        BusinessVo businessVo = readExcel(pathOrEncodeStr, type, fileName, headerEnglish, headerChinese, fieldType,
                fieldLength, isNecessary);
        // 获取List<Map<String, Object>>
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) businessVo.getDatas();
        if (dataList != null && dataList.size() > 0) {
            try {
                objectList = ReflectUtil.reflectObject(dataList, invokeClassPath);
            } catch (ClassNotFoundException e) {
                businessVo.setResultCode(BusinessVo.CLASS_NOT_FOUND_ERROR_CODE);
                businessVo.setResultInfo(BusinessVo.CLASS_NOT_FOUND_ERROR_MSG);
                log.error(e.getMessage(), e);
            } catch (InstantiationException e) {
                businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
                businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
                log.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
                businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
                log.error(e.getMessage(), e);
            } catch (NoSuchFieldException e) {
                businessVo.setResultCode(BusinessVo.NO_SUCH_FIELD_ERROR_CODE);
                businessVo.setResultInfo(BusinessVo.NO_SUCH_FIELD_ERROR_MSG);
                log.error(e.getMessage(), e);
            } catch (SecurityException e) {
                businessVo.setResultCode(BusinessVo.GENERAL_ERROR_CODE);
                businessVo.setResultInfo(BusinessVo.GENERAL_ERROR_MSG);
                log.error(e.getMessage(), e);
            }
        } else {
            log.error("readExcel解析出的list为空");
        }
        businessVo.setDatas(objectList);
        return businessVo;
    }

    /**
     * 通过文件路径或者文件encode编码后的字符串读取excel，返回BusinessVo，属性List<Map<String, Object>>
     *
     * @param pathOrEncodeStr
     * @param type
     * @param fileName
     * @param headerEnglish
     * @param headerChinese
     * @param fieldType
     * @param fieldLength
     * @param isNecessary
     * @return
     * @author zuoyue 2015年6月9日
     */
    public static BusinessVo readExcel(String pathOrEncodeStr, String type, String fileName, String headerEnglish,
            String headerChinese, String fieldType, String fieldLength, String isNecessary) {
        BusinessVo businessVo = new BusinessVo();
        // 文件路径或文件encode后的字符串
        if (StringUtils.isNotBlank(pathOrEncodeStr)) {
            // 根据excel版本获取不同的Workbook
            Workbook workbook = null;
            try {
                workbook = acquireWorkbookFromPath(pathOrEncodeStr, type, fileName);
                if (workbook != null) {
                    // 获得第一个表单
                    Sheet sheet = workbook.getSheetAt(0);
                    if (sheet != null) {
                        // 获取列英文与中文的对照关系
                        Map<String, String> columaMap = acquireContrastMap(headerChinese, headerEnglish);
                        // 验证数据用
                        Map<String, String> fieldTypeMap = acquireContrastMap(headerEnglish, fieldType);
                        Map<String, String> fieldLengthMap = acquireContrastMap(headerEnglish, fieldLength);
                        Map<String, String> fieldChineseMap = acquireContrastMap(headerEnglish, headerChinese);
                        Map<String, String> fieldNecessaryMap = acquireContrastMap(headerEnglish, isNecessary);
                        Map<String, Pattern> fieldPatternMap = ReflectUtil.acquireFieldPatternMap(fieldTypeMap,
                                fieldLengthMap);
                        // 获得数据集合
                        businessVo = dealExcelRow(sheet, columaMap, fieldTypeMap, fieldLengthMap, fieldChineseMap,
                                fieldPatternMap, fieldNecessaryMap);
                    }
                }
            } catch (IOException e) {
                businessVo.setResultCode(BusinessVo.IO_ERROR_CODE);
                businessVo.setResultInfo(BusinessVo.IO_ERROR_MSG);
                log.error(e.getMessage(), e);
            }
        } else {
            log.error("文件路径或者文件encode编码后的字符串为空");
        }
        return businessVo;
    }

    /**
     * 根据excel版本获取不同的Workbook
     *
     * @param path
     * @return
     * @author zuoyue 2015年6月9日
     * @throws IOException
     */
    private static Workbook acquireWorkbookFromPath(String pathOrEncodeStr, String type, String fileName)
            throws IOException {
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            // 根据不同类型建立不同输入流
            inputStream = acquireInputStream(pathOrEncodeStr, type);
            // 根据文件格式(2003或者2007)来初始化
            if (pathOrEncodeStr.endsWith(".xlsx") || (StringUtils.isNotBlank(fileName) && fileName.endsWith(".xlsx"))) {
                // 2007
                workbook = new XSSFWorkbook(inputStream);
            } else {
                // 2003
                workbook = new HSSFWorkbook(inputStream);
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return workbook;
    }

    /**
     * 根据不同类型建立不同输入流
     *
     * @param path
     * @return
     * @author zuoyue 2015年6月9日
     */
    private static InputStream acquireInputStream(String pathOrEncodeStr, String type) throws FileNotFoundException {
        InputStream inputStream = null;
        switch (type) {
        // 路径
            case PATH_STRING:
                inputStream = new FileInputStream(pathOrEncodeStr);
                break;
            // base64编码
            case ENCODE_STRING:
                byte[] bytes = Base64Utils.decodeFromString(pathOrEncodeStr);
                inputStream = new ByteArrayInputStream(bytes);
                break;
            default:
                break;
        }
        return inputStream;
    }

    /**
     * 处理excel行数据,返回BusinessVo，data为 List<Map<String, Object>>
     *
     * @param sheet
     * @param columaMap
     * @param fieldTypeMap
     * @param fieldLengthMap
     * @param fieldChineseMap
     * @param fieldPatternMap
     * @param fieldNecessaryMap
     * @return
     * @author zuoyue 2015年6月9日
     */
    private static BusinessVo dealExcelRow(Sheet sheet, Map<String, String> columaMap,
            Map<String, String> fieldTypeMap, Map<String, String> fieldLengthMap, Map<String, String> fieldChineseMap,
            Map<String, Pattern> fieldPatternMap, Map<String, String> fieldNecessaryMap) {
        // 数据list
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        BusinessVo businessVo = new BusinessVo();
        // 是否为空的excel
        boolean isExcelBlank = true;
        // 是否填写了数据
        boolean isDataBlank = true;
        // 行号
        int rowNum = 0;
        // 获得第一个表单的迭代器
        Iterator<Row> rows = sheet.rowIterator();
        Map<Integer, String> titleMap = new HashMap<Integer, String>();
        breakCycle:
        while (rows.hasNext()) {
            isExcelBlank = false;
            // 计数器
            int cycleCount = 0;
            // 真实列数
            int realColumnNumber = 0;
            // 获得行数据
            Row row = rows.next();
            // 获得列数
            // int columnCounts = row.getLastCellNum();
            // 获得行号,从0开始
            rowNum = row.getRowNum();
            // 获得行的迭代器
            Iterator<Cell> cells = row.cellIterator();
            Map<String, Object> columnMap = new HashMap<String, Object>();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                // 获取列序号
                int columnIndex = cell.getColumnIndex();
                // 将列数据放入map
                if (0 == rowNum) {
                    // 将标题列放入titleMap
                    // 标题列中文
                    String columnName = String.valueOf(dealExcelColumn(cell));
                    if (ReflectUtil.stringIsNotNull(columnName)) {
                        // 标题列英文
                        String columnCode = columaMap.get(columnName);
                        if (ReflectUtil.stringIsNotNull(columnCode)) {
                            titleMap.put(columnIndex, columnCode);
                        } else {
                            businessVo.setResultCode(BusinessVo.EXCEL_NOT_MATCH_ERROR_CODE);
                            businessVo.setResultInfo(String.format(BusinessVo.EXCEL_NOT_MATCH_ERROR_MSG, columnName));
                            dataList = null;
                            break breakCycle;
                        }
                    }
                } else {
                    isDataBlank = false;
                    // 标题不为null
                    if (titleMap.get(columnIndex) != null) {
                        // 真实列数
                        realColumnNumber++;
                        // 数据列放入columnMap
                        Object columnData = dealExcelColumn(cell);
                        // columnDate为null
                        if (ReflectUtil.stringIsNull(String.valueOf(columnData))) {
                            cycleCount++;
                            // 置为"",否则数据库会插入null字符串
                            columnData = "";
                        }
                        columnMap.put(titleMap.get(columnIndex), columnData);
                    }
                }
            }
            // 无空行才放入dataList
            if (0 != rowNum && realColumnNumber != cycleCount) {
                // 验证数据格式是否正确
                for (Map.Entry<String, String> entry : fieldTypeMap.entrySet()) {
                    // 属性英文名
                    String fieldName = entry.getKey();
                    // 属性值
                    Object fieldValue = columnMap.get(fieldName);
                    // 属性类型
                    String fieldType = fieldTypeMap.get(fieldName);
                    // 属性长度
                    String fieldLengthStr = fieldLengthMap.get(fieldName);
                    // 属性中文名
                    String fieldChinese = fieldChineseMap.get(fieldName);
                    // 是否必填
                    String isNecessary = fieldNecessaryMap.get(fieldName);
                    // 属性Pattern
                    Pattern pattern = fieldPatternMap.get(fieldName);
                    // 非必填不验证
                    if (ReflectUtil.isMustValidate(String.valueOf(fieldValue), isNecessary)) {
                        businessVo = ReflectUtil.isPassValidation(fieldName, fieldType, fieldLengthStr, fieldChinese,
                                pattern, String.valueOf(fieldValue), (rowNum + 1));
                        // 验证通过
                        if (!BusinessVo.SUCCESS_CODE.equals(businessVo.getResultCode())) {
                            break breakCycle;
                        }
                    }
                }
                dataList.add(columnMap);
            }
            // 真实列数置为0
            realColumnNumber = 0;
        }
        if (BusinessVo.SUCCESS_CODE.equals(businessVo.getResultCode())
                && (isExcelBlank || isDataBlank || dataList == null || dataList.size() == 0)) {
            businessVo.setResultCode(BusinessVo.NO_DATA_ERROR_CODE);
            businessVo.setResultInfo(BusinessVo.NO_DATA_ERROR_MSG);
            log.debug("isExcelBlank:" + isExcelBlank + ",isDataBlank:" + isDataBlank + ",dataList:" + dataList);
        }
        businessVo.setDatas(dataList);
        return businessVo;
    }

    /**
     * 处理excel列数据
     *
     * @param path
     * @return
     * @author zuoyue 2015年6月9日
     */
    private static Object dealExcelColumn(Cell cell) {
        Object dataObj = null;
        // 根据cell中的类型来输出数据
        switch (cell.getCellType()) {
        // 数字
            case HSSFCell.CELL_TYPE_NUMERIC:
                dataObj = typeJudgment(cell);
                break;
            // 字符串
            case HSSFCell.CELL_TYPE_STRING:
                dataObj = replaceBlank(cell.getStringCellValue());
                break;
            // Boolean
            case HSSFCell.CELL_TYPE_BOOLEAN:
                dataObj = cell.getBooleanCellValue();
                break;
            // 公式
            case HSSFCell.CELL_TYPE_FORMULA:
                dataObj = cell.getCellFormula();
                break;
            // 空值
            case HSSFCell.CELL_TYPE_BLANK:
                break;
            // 故障
            case HSSFCell.CELL_TYPE_ERROR:
                break;
            // 未知类型
            default:
                break;
        }
        return dataObj;
    }

    /**
     * 处理单元格
     *
     * @param cell
     * @return
     * @author zuoyue 2015年6月17日
     */
    private static Object typeJudgment(Cell cell) {
        Object dataObj = null;
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
            // 日期转字符串
            dataObj = DateUtils.format(cell.getDateCellValue(), DateUtils.DATE_FORMAT_2);
        } else {
            // 格式转换，避免以科学计数法表示
            BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
            dataObj = replaceBlank(bigDecimal.toString());
        }
        return dataObj;
    }

    /**
     * 去除回车空格换行符等
     *
     * @param path
     * @return
     * @author zuoyue 2015年6月9日
     */
    private static String replaceBlank(String str) {
        String dest = str;
        if (StringUtils.isNotBlank(str)) {
            // Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            dest = RegexUtils.REGEX_BLANK.matcher(str).replaceAll("");
        }
        return dest;
    }

    /**
     * 获取对照关系，比如：列英文与中文的对照关系；列英文与字段长度的对照关系等
     *
     * @return
     * @author zuoyue 2015年6月10日
     */
    public static Map<String, String> acquireContrastMap(String key, String value) {
        Map<String, String> columaMap = null;
        if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(key)) {
            columaMap = new HashMap<String, String>();
            String[] valueArr = value.split(",");
            String[] keyArr = key.split(",");
            for (int i = 0; i < valueArr.length; i++) {
                columaMap.put(keyArr[i], valueArr[i]);
            }
        }
        return columaMap;
    }

    /**
     * 拆分list
     *
     * @param listSize
     * @param originalList
     * @return
     * @author zuoyue 2015年6月16日
     */
    public static List<List<?>> splitList(int listSize, List<?> originalList) {
        List<List<?>> targetList = new ArrayList<List<?>>();
        // 拆分list
        int count = listSize / BATCH_NUMBER;
        int mod = listSize % BATCH_NUMBER;
        for (int i = 0; i < count; i++) {
            List<?> subList = originalList.subList(i * BATCH_NUMBER, (i + 1) * BATCH_NUMBER);
            targetList.add(subList);
        }
        if (mod != 0) {
            List<?> subList = originalList.subList(count * BATCH_NUMBER, (count * BATCH_NUMBER + mod));
            targetList.add(subList);
        }
        return targetList;
    }

}
