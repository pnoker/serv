
package com.dc.city.vo.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 数据库执行vo
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月8日 上午11:00:55
 *          Copyright 2016 by DigitalChina
 */
@XmlRootElement(name = "dataBaseExcuteVo")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlSeeAlso(HashMap.class)
public class DataBaseExcuteVo {

    /**
     * 返回码：0：成功；非0：失败；默认值为0
     */
    private String resultCode = "0";
    /**
     * 返回码信息，默认值为:查询成功
     */
    private String resultInfo ="处理成功";

    //返回结果列名
    protected List<Map<String,String>> columns = new ArrayList<Map<String,String>>();
    //返回数据
    protected List<Map<String,Object>> datas= new ArrayList<Map<String,Object>>();
    //数据长度
    protected int dataSize = 0;
    //耗时(ms)
    protected double costTime=0.0;

    public double getCostTime() {
        return costTime;
    }

    public void setCostTime(double costTime) {
        this.costTime = costTime;
    }

    public DataBaseExcuteVo() {
        super();
    }
    
    public DataBaseExcuteVo(String resultCode, String resultInfo) {
        this.setResultCode(resultCode);
        this.setResultInfo(resultInfo);
    }

    public List<Map<String,String>> getColumns(){
        return this.columns;
    }
    public void setColumns(List<Map<String,String>>columns) {
        this.columns = columns;
    }


    public int getDataSize(){
        return this.dataSize;
    }
    public void setDataSize(int size) {
        this.dataSize = size;
    }
    
    public List<?> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
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
