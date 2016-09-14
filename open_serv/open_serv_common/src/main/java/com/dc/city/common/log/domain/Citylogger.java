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
package com.dc.city.common.log.domain;

import java.util.Date;

public class Citylogger {
    private long id;

    private String visitedip;

    private String visitedurl;

    private String paramters;

    private String responseparams;

    private String visitedhost;

    private String visiteduser;

    private String errinfo;

    private Date visitedtime;

    private String responseText;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVisitedip() {
        return visitedip;
    }

    public void setVisitedip(String visitedip) {
        this.visitedip = visitedip;
    }

    public String getVisitedurl() {
        return visitedurl;
    }

    public void setVisitedurl(String visitedurl) {
        this.visitedurl = visitedurl;
    }

    public String getParamters() {
        return paramters;
    }

    public void setParamters(String paramters) {
        this.paramters = paramters;
    }

    public String getResponseparams() {
        return responseparams;
    }

    public void setResponseparams(String responseparams) {
        this.responseparams = responseparams;
    }

    public String getVisitedhost() {
        return visitedhost;
    }

    public void setVisitedhost(String visitedhost) {
        this.visitedhost = visitedhost;
    }

    public String getVisiteduser() {
        return visiteduser;
    }

    public void setVisiteduser(String visiteduser) {
        this.visiteduser = visiteduser;
    }

    public String getErrinfo() {
        return errinfo;
    }

    public void setErrinfo(String errinfo) {
        this.errinfo = errinfo;
    }

    public Date getVisitedtime() {
        return visitedtime;
    }

    public void setVisitedtime(Date visitedtime) {
        this.visitedtime = visitedtime;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}