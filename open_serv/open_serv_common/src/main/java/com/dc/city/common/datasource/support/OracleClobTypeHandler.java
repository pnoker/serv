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
package com.dc.city.common.datasource.support;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.CLOB;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class OracleClobTypeHandler implements TypeHandler<Object> {
  
  public Object valueOf(String param) {
    return null;
  }

  @Override
  public Object getResult(ResultSet arg0, String arg1) throws SQLException {
    CLOB clob = (CLOB) arg0.getClob(arg1);
    return (clob == null || clob.length() == 0) ? null : clob.getSubString((long) 1, (int) clob.length());
  }

  @Override
  public Object getResult(ResultSet arg0, int arg1) throws SQLException {
    return null;
  }

  @Override
  public Object getResult(CallableStatement arg0, int arg1) throws SQLException {
    return null;
  }

  @SuppressWarnings("deprecation")
@Override
  public void setParameter(PreparedStatement arg0, int arg1, Object arg2, JdbcType arg3) throws SQLException {
    CLOB clob = CLOB.empty_lob();
    clob.setString(1, (String) arg2);
    arg0.setClob(arg1, clob);
  }
}