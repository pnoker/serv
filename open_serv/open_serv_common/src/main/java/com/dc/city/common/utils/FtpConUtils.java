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

import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP类
 *
 * @author zuoyue
 * @version V1.0 创建时间：2015年8月11日 下午3:57:28
 *          Copyright 2015 by DigitalChina
 */
public class FtpConUtils {

    private static Log log = LogFactory.getLog("FtpConUtils");

    private static FTPClient ftpClient;

    /**
     * 连接到服务器
     * 
     * @param path FTP服务器目录
     * @throws IOException
     * @throws SocketException
     * @throws NumberFormatException
     * @throws Exception
     */
    public static FTPClient connectServer(String ip, String port, String userName, String userPwd, String path) {
        // 连接
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ip, Integer.parseInt(port));
            // 登录
            ftpClient.login(userName, userPwd);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                log.error("未连接到ftp,用户名或密码错误");
            }
            // 跳转到指定目录
            ftpClient.changeWorkingDirectory(path);
        } catch (NumberFormatException | IOException e) {
            ftpClient = null;
            log.error("ftp连接出错");
        }
        return ftpClient;
    }

    /**
     * 关闭连接
     * 
     * @throws IOException
     */
    public static void closeServer() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("ftp连接关闭错误");
            }
        }
    }

}
