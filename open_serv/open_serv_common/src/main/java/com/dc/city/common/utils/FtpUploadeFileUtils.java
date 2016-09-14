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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * ftp上传文件
 *
 * @author ligen
 * @version V1.0 创建时间：2015年8月28日 上午9:38:26
 *          Copyright 2015 by dcits
 */
public class FtpUploadeFileUtils {
    // 上传ftp失败时返回的标记
    public static final String UPLOAD_ERROR_FLAG = "ftp_error";
    private static FTPClient ftp;
    private static Log log = LogFactory.getLog("FtpConUtils");
    // ftp Ip地址
    private static String ftpIpAddress = ConfigUtils.getString("ftp.static.ip", null);
    // appache Ip地址
    private static String appacheIpAddress = ConfigUtils.getString("appache.static.ip", null);
    // ftp端口
    private static String ftpPort = ConfigUtils.getString("ftp.static.port", "21");

    // appache端口
    private static String appachePort = ConfigUtils.getString("appache.static.port", "8082");

    // ftp用户名
    private static String ftpUserName = ConfigUtils.getString("ftp.static.username", null);
    // ftp密码
    private static String ftpUpassWord = ConfigUtils.getString("ftp.static.password", null);

    private static final FtpUploadeFileUtils uploade = new FtpUploadeFileUtils();

    /**
     * ftp连接方法
     *
     * @param path 上传到ftp服务器哪个路径下
     * @param ip 地址
     * @param port 端口号
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     * @throws Exception
     * @author ligen 2015年8月28日
     */
    private boolean connect(String ip, int port, String username, String password) {
        boolean result = false;
        ftp = new FTPClient();
        int reply;
        try {

            ftp.connect(ip, port);
            ftp.login(username, password);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            result = true;
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 关闭连接
     *
     * @author ligen 2016年5月9日
     */
    private void closeServer() {
        if (ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                log.error("ftp连接关闭错误");
            }
        }
    }

    /**
     * 保存文件到服务器上
     *
     * @param bigModeName 该文件所在的程序大模块
     * @param modeName 该文件所在的程序小模块
     * @param originalFileName 本来的文件的名
     * @param fileBytes 文件的内容-字节的形式
     * @return
     * @author ligen 2015年8月28日
     * @throws IOException
     */
    public static String uploadToFtp(String bigModeName, String modeName, String originalFileName, byte[] fileBytes) {
        try {
            int port = Integer.parseInt(ftpPort);
            boolean f = uploade.connect(ftpIpAddress, port, ftpUserName, ftpUpassWord);
            if (!f) {
                return "不能连接到ftp服务器";
            }
            // 文件后缀名
            String extName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            // 原始文件名 没有后缀
            String originalNameNoExt = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            // 上传文件存在的日期文件夹里面
            String ymd = new SimpleDateFormat("yyyy").format(new Date());
            String filePath = String.format("%s/%s/%s/%s", bigModeName, modeName, ymd, extName);
            String fileFullName = String.format("/%s/%s_%s.%s", filePath, String.valueOf(System.currentTimeMillis()),
                    originalNameNoExt, extName);
            mkDir(filePath);
            // 获取bate数组，如果为空就设置为空格
            try (InputStream sbs = new ByteArrayInputStream(fileBytes == null || fileBytes.length == 0 ? " ".getBytes()
                    : fileBytes)) {
                // 设置文件名读取为GBK
                ftp.storeFile(new String(fileFullName.substring(fileFullName.lastIndexOf("/") + 1).getBytes("GBK"),
                        "iso-8859-1"), sbs);
                uploade.closeServer();
                return fileFullName;
            }
        } catch (Exception e) {
            return UPLOAD_ERROR_FLAG;
        }

    }

    /**
     * 递归创建文件夹
     *
     * @param file
     * @author chenzpa 2015年7月8日
     * @throws IOException
     */
    private static void mkDir(String dir) {
        String[] dirString = dir.split("/");
        for (String d : dirString) {
            try {
                ftp.makeDirectory(d);
                ftp.changeWorkingDirectory(d);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\user\\Desktop\\新建文本文档.txt");
        byte[] fileByte = getBytesFromFile(file);
        String reString = uploadToFtp("city", "output", file.getName(), fileByte);
        System.out.println(reString);
    }

    /**
     * 返回一个byte数组
     *
     * @param file
     * @return
     * @throws IOException
     * @author ligen 2015年8月31日
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            // 获取文件大小
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // 文件太大，无法读取
                throw new IOException("File is to large " + file.getName());
            }
            // 创建一个数据来保存文件数据
            byte[] bytes = new byte[(int) length];
            // 读取数据到byte数组中
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            return bytes;
        }
    }

    /**
     * 获取appache服务器的IP地址
     * http://ip:port
     *
     * @return
     * @author chenzpa 2015年9月11日
     */
    public static String QueryAppacheHttpUrl() {
        String aUrl = "http://%s:%s";
        return String.format(aUrl, appacheIpAddress, appachePort);
    }

}
