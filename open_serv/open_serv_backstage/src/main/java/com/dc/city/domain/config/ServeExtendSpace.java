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
package com.dc.city.domain.config;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServeExtendSpace {
    private long id;

    private String urlTest;

    private String urlFormal;

    private int ge0Features;

    private int maxLength;

    private int isVisible;

    private int layerType;

    private double displayScaleMax;

    private double displayScaleMin;

    private int isLabel;

    private int gisServer;

    private String coordinateSystem;

    private String coordinateCode;

    private Date updatetime;

    private List<ServeGisDictionary> gisDictionaries;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrlTest() {
        return urlTest;
    }

    public void setUrlTest(String urlTest) {
        this.urlTest = urlTest;
    }

    public String getUrlFormal() {
        return urlFormal;
    }

    public void setUrlFormal(String urlFormal) {
        this.urlFormal = urlFormal;
    }

    public int getGe0Features() {
        return ge0Features;
    }

    public void setGe0Features(int ge0Features) {
        this.ge0Features = ge0Features;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getLayerType() {
        return layerType;
    }

    public void setLayerType(int layerType) {
        this.layerType = layerType;
    }

    public double getDisplayScaleMax() {
        return displayScaleMax;
    }

    public void setDisplayScaleMax(double displayScaleMax) {
        this.displayScaleMax = displayScaleMax;
    }

    public double getDisplayScaleMin() {
        return displayScaleMin;
    }

    public void setDisplayScaleMin(double displayScaleMin) {
        this.displayScaleMin = displayScaleMin;
    }

    public int getIsLabel() {
        return isLabel;
    }

    public void setIsLabel(int isLabel) {
        this.isLabel = isLabel;
    }

    public int getGisServer() {
        return gisServer;
    }

    public void setGisServer(int gisServer) {
        this.gisServer = gisServer;
    }

    public String getCoordinateSystem() {
        return coordinateSystem;
    }

    public void setCoordinateSystem(String coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    public String getCoordinateCode() {
        return coordinateCode;
    }

    public void setCoordinateCode(String coordinateCode) {
        this.coordinateCode = coordinateCode;
    }

    public Date getUpdatetime() {
        this.updatetime = Calendar.getInstance().getTime();
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public List<ServeGisDictionary> getGisDictionaries() {
        return gisDictionaries;
    }

    public void setGisDictionaries(List<ServeGisDictionary> gisDictionaries) {
        this.gisDictionaries = gisDictionaries;
    }

}