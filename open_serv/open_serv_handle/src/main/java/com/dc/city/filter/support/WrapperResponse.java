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
package com.dc.city.filter.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
/**
 * 
 * response 封装类，提供获取response返回json的方法
 *
 * @author zhongdt
 * @version V1.0 创建时间：2016年3月25日 下午2:12:28
 *          Copyright 2016 by DigitalChina
 */
public class WrapperResponse extends HttpServletResponseWrapper {

    private ByteArrayOutputStream buffer = null;
    private ServletOutputStream out = null;
    private PrintWriter writer = null;

    public WrapperResponse(HttpServletResponse resp) throws IOException {
        super(resp);
        buffer = new ByteArrayOutputStream();// 真正存储数据的流
        out = new WapperedOutputStream(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding()));
    }

    /** 重载父类获取outputstream的方法 */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return out;
    }

    /** 重载父类获取writer的方法 */
    @Override
    public PrintWriter getWriter() throws UnsupportedEncodingException {
        return writer;
    }

    /** 重载父类获取flushBuffer的方法 */
    @Override
    public void flushBuffer() throws IOException {
        if (out != null) {
            out.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    /** 将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据 */
    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    /** 内部类，对ServletOutputStream进行包装 */
    private class WapperedOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos = null;

        public WapperedOutputStream(ByteArrayOutputStream stream) throws IOException {
            bos = stream;
        }

        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            bos.write(b, 0, b.length);
        }

        @Override
        public boolean isReady() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setWriteListener(WriteListener paramWriteListener) {
            // TODO Auto-generated method stub

        }
    }
}