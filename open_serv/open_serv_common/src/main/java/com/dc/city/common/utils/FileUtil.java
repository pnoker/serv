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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Base64Utils;

/**
 * 文件上传下载工具类
 *
 * @author chenzpa
 * @version V1.0 创建时间：2015年7月7日 下午5:49:13
 *          Copyright 2015 by Dcits
 */
public final class FileUtil {

    // 项目的根目录
    private final static String ROOT_DIRECTORY = new File(new File(new File(new File(new File(FileUtil.class
            .getClassLoader().getResource("/").getPath()).getParent()).getParent()).getParent()).getParent())
            .getParent();
    // 所有上传的文件的存放目录
    private final static String FILE_DIRECTORY = ROOT_DIRECTORY + "/city_uoloadfile";

    /**
     * 保存文件到磁盘
     *
     * @param fileExtensionName 文件的后缀名
     * @param fileEncodeBase64Str 转换成Base64字符串的文件内容
     * @param modeName 该文件所在的程序模块
     * @return 该文件存在服务器上的磁盘路径
     * @author chenzpa 2015年7月7日
     * @throws IOException
     */
    public static String saveFile(String bigModeName, String modeName, String fileExtensionName,
            String fileEncodeBase64Str) throws IOException {
        return saveFile(bigModeName, modeName, fileExtensionName, Base64Utils.decodeFromString(fileEncodeBase64Str));
    }

    /**
     * 保存文件到磁盘
     *
     * @param bigModeName 该文件所在的程序大模块
     * @param modeName 该文件所在的程序小模块
     * @param originalFileName 本来的文件的名
     * @param fileBytes 文件的内容-字节的形式
     * @return
     * @author chenzpa 2015年7月7日
     * @throws IOException
     */
    public static String saveFile(String bigModeName, String modeName, String originalFileName, byte[] fileBytes)
            throws IOException {

        // 文件后缀名
        String extName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        // 原始文件名 没有后缀
        String originalNameNoExt = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        // 上传文件存在的日期文件夹里面
        String ymd = new SimpleDateFormat("yyyy").format(new Date());
        String filePath = String.format("%s/%s/%s/%s/%s", FILE_DIRECTORY, bigModeName, modeName, ymd, extName);
        String fileFullName = String.format("%s/%s_%s.%s", filePath, String.valueOf(System.currentTimeMillis()),
                originalNameNoExt, extName);
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {// 文件目录不存在
            mkDir(fileDir);
        }
        File file = new File(fileFullName);
        if (file.exists()) {// 文件存在了则删除它
            file.delete();
        }
        if (!file.createNewFile()) {// 创建一个新文件
            return "创建文件失败";
        } else {
            try (FileOutputStream os = new FileOutputStream(file)) {
                os.write(fileBytes);// 将字节写入文件
            }
            return "downloadfile?file=" + fileFullName.replace(ROOT_DIRECTORY, "");
        }
    }

    /**
     * 下载上传了的文件
     *
     * @param fileName 文件URL
     * @param response
     * @return
     * @throws IOException
     * @author chenzpa 2015年7月10日
     */
    public static Object downloadFile(String fileURL, HttpServletResponse response) throws IOException {
        File file = new File(ROOT_DIRECTORY + fileURL);
        if (!file.exists()) {
            return "文件不存在：" + fileURL;
        }
        // 设置下载的文件名
        response.setHeader("content-disposition",
                "attachment;filename=" + URLEncoder.encode(fileURL.substring(fileURL.lastIndexOf("/") + 1), "UTF-8")); // 设置返回的文件名
        // 向输出流里面写文件
        try (BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        }
        return "SUCCESS";
    }

    /**
     * 递归创建文件夹
     *
     * @param file
     * @author chenzpa 2015年7月8日
     */
    private static void mkDir(File file) {
        if (file.getParentFile().exists()) {
            file.mkdir();
        } else {
            mkDir(file.getParentFile());
            file.mkdir();
        }
    }
}
