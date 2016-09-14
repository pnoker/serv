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
package com.dc.city.common.auth.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.city.common.vo.LoginVo;

/**
 * 进行http提交的工具包
 *
 * @author xutaog
 * @version V1.0 创建时间：2015年7月28日 下午1:09:46 Copyright 2015 by DigitalChina
 */
public class HttpClientTools {
    private final static Logger loger = LoggerFactory.getLogger(HttpClientTools.class);

    /**
     * SOCKET超时时间，5分钟
     */
    private static final int SOCKET_TIME_OUT = 60000;

    /**
     * CONNECTION超时时间，5分钟
     */
    private static final int CONNECT_TIME_OUT = 60000;

    /**
     * CONNECTION请求超时时间,1分钟
     */
    private static final int CONNECT_REQUEST_TIME_OUT = 60000;

    /**
     * post请求类型
     */
    private static final String HTTP_POST_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    /**
     * httpGet请求
     *
     * @param requestUrl
     * @param decodeCharset
     * @return String
     * @author Administrator 2015年4月30日
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String httpGetRequest(String requestUrl, String decodeCharset) {
        String json = "";
        try {
            json = sendGetRequest(requestUrl, decodeCharset, null);
        } catch (IOException e) {
            loger.error("发送httpGet请求失败，返回信息：" + json);
        }
        return json;
    }

    /**
     * get的request请求
     *
     * @param requestUrl
     * @return
     * @author xutaog 2015年8月18日
     */
    public static String sendHttpGetRequest(String requestUrl) {
        String json = "";
        try {
            json = sendGetRequest(requestUrl, null, null);
        } catch (IOException e) {
            loger.error("发送httpGet请求失败，返回信息：" + json);
        }
        return json;
    }

    /**
     * 使用代理执行get请求
     *
     * @param requestUrl
     * @return
     * @author xutaog 2016年1月11日
     */
    public static String sendHttpProxyGetRequest(String requestUrl, HttpHost proxy) {
        String json = "";
        try {
            json = sendGetRequest(requestUrl, null, proxy);
        } catch (IOException e) {
            loger.error("发送httpGet请求失败，返回信息：" + json);
        }
        return json;
    }

    /**
     * httpGet具体实现
     *
     * @param requestUrl
     * @param decodeCharset
     * @return
     * @author Administrator 2015年4月30日
     * @throws IOException
     * @throws ClientProtocolException
     */
    private static String sendGetRequest(String requestUrl, String decodeCharset, HttpHost proxy)
            throws ClientProtocolException, IOException {
        String responseContent = "";
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            httpGet = new HttpGet(requestUrl);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT)
                    .setConnectTimeout(CONNECT_TIME_OUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT).build();
            if (proxy != null) {// 是否使用代理
                requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).setProxy(proxy)
                        .setConnectTimeout(CONNECT_TIME_OUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT)
                        .build();
            }
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                entity = response.getEntity();
                if (entity != null) {
                    responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                } else {
                    loger.error("http_post返回信息为空");
                }
            } else {
                loger.error("发送http_post请求失败，返回信息：" + responseContent);
            }
        } finally {
            if (null != entity) {
                EntityUtils.consume(entity);
            }
            if (null != response) {
                response.close();
            }
            if (httpGet != null && httpGet.isAborted()) {
                httpGet.abort();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
        return responseContent;
    }

    /**
     * 方法的注释
     *
     * @param requestUrl
     * @param charset
     * @return
     * @author xutaog 2015年7月30日
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static LoginVo sendRequestGet(String requestUrl) throws ClientProtocolException, IOException {

        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        CloseableHttpClient httpClient = null;
        InputStream instream = null;
        LoginVo loginVo = new LoginVo();
        try {
            loger.debug("开始发送http_post请求");
            httpClient = HttpClients.createDefault();
            httpGet = new HttpGet(requestUrl);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT)
                    .setConnectTimeout(CONNECT_TIME_OUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    ObjectMapper mapper = new ObjectMapper();
                    loginVo = mapper.readValue(instream, LoginVo.class);
                } else {
                    // 读取配置文件的跳转错误路径
                    loginVo.setResultCode("-1");
                    loginVo.setResultInfo("验证方法返回的消息为空");
                    loger.error("http_post返回信息为空");
                }
            } else {
                // 读取配置文件的跳转错误路径
                loginVo.setResultCode("-1");
                loginVo.setResultInfo("调用验证方法出错");
            }
        } catch (Exception e) {
            loginVo.setResultCode("-1");
            loginVo.setResultInfo("调用验证方法出错");
            e.printStackTrace();
        } finally {
            try {

                if (instream != null) {
                    instream.close();
                }
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
                if (null != response) {
                    response.close();
                }
                if (httpGet != null && httpGet.isAborted()) {
                    httpGet.abort();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return loginVo;

    }

    /**
     * 数据流与字符串之间发生转换
     *
     * @param is
     * @return
     * @author xutaog 2015年7月24日
     */
    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 外部调用的post提交的方法
     *
     * @param requestUrl
     *            请求的url
     * @param params
     *            传输参数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @author xutaog 2015年7月28日
     */
    public static LoginVo httpPostRequest(String requestUrl, Map<String, String> params)
            throws ClientProtocolException, IOException {
        return sendPostRequest(requestUrl, params, null);
    }

    /**
     * 返回字符串的的post请求
     *
     * @param requestUrl
     * @param params
     * @return
     * @throws IOException
     * @author xutaog 2015年8月17日
     */
    public static String sendHttpPostRequest(String requestUrl, Map<String, String> params) throws IOException {
        return sendHttpPostRequest(requestUrl, params, null);
    }

    /**
     * 返回字符串的的post请求
     *
     * @param requestUrl
     * @param params
     * @param encodeCharset
     * @return
     * @throws IOException
     * @author xutaog 2015年8月17日
     */
    public static String sendHttpPostRequest(String requestUrl, Map<String, String> params, String encodeCharset)
            throws IOException {
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        CloseableHttpClient httpClient = null;
        InputStream instream = null;
        String responseContent = "-1";
        try {
            loger.debug("开始发送http_post请求");
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(requestUrl);
            httpPost.setHeader(HTTP.CONTENT_TYPE, HTTP_POST_CONTENT_TYPE);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT)
                    .setConnectTimeout(CONNECT_TIME_OUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT).build();
            httpPost.setConfig(requestConfig);
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            // 组装请求参数
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new StringEntity(URLEncodedUtils.format(formParams, encodeCharset == null ? "UTF-8"
                    : encodeCharset)));
            // 执行请求
            response = httpClient.execute(httpPost);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                entity = response.getEntity();
                if (entity != null) {
                    responseContent = EntityUtils.toString(entity, "UTF-8");
                }
            } else {
                // 读取配置文件的跳转错误路径
                responseContent = "-1";
            }
        } catch (Exception q) {
            q.printStackTrace();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
                if (null != response) {
                    response.close();
                }
                if (httpPost != null && httpPost.isAborted()) {
                    httpPost.abort();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * httpPost具体实现
     *
     * @param requestUrl
     *            url 地址
     * @param params
     *            参数map
     * @param encodeCharset
     *            字符串格式 默认utf-8
     * @return
     * @author xutaog 2015年7月28日
     * @throws IOException
     * @throws ClientProtocolException
     */
    private static LoginVo sendPostRequest(String requestUrl, Map<String, String> params, String encodeCharset)
            throws ClientProtocolException, IOException {

        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        CloseableHttpClient httpClient = null;
        InputStream instream = null;
        LoginVo loginVo = new LoginVo();
        try {
            loger.debug("开始发送http_post请求");
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(requestUrl);
            httpPost.setHeader(HTTP.CONTENT_TYPE, HTTP_POST_CONTENT_TYPE);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT)
                    .setConnectTimeout(CONNECT_TIME_OUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT).build();
            httpPost.setConfig(requestConfig);
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            // 组装请求参数
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new StringEntity(URLEncodedUtils.format(formParams, encodeCharset == null ? "UTF-8"
                    : encodeCharset)));
            // 执行请求
            response = httpClient.execute(httpPost);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    ObjectMapper mapper = new ObjectMapper();
                    loginVo = mapper.readValue(instream, LoginVo.class);
                } else {
                    // 读取配置文件的跳转错误路径
                    loginVo.setResultCode("-1");
                    loginVo.setResultInfo("验证方法返回的消息为空");
                    loger.error("http_post返回信息为空");
                }
            } else {
                // 读取配置文件的跳转错误路径
                loginVo.setResultCode("-1");
                loginVo.setResultInfo("调用验证方法出错");
                loger.info(response.toString());
            }
        } catch (Exception p) {
            loginVo.setResultCode("-1");
            loginVo.setResultInfo(p.getMessage());
            p.printStackTrace();
        } finally {
            try {

                if (instream != null) {
                    instream.close();
                }
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
                if (null != response) {
                    response.close();
                }
                if (httpPost != null && httpPost.isAborted()) {
                    httpPost.abort();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (Exception e) {
                loginVo.setResultCode("-1");
                loginVo.setResultInfo(e.getMessage());
                e.printStackTrace();
            }
        }
        return loginVo;
    }

}
