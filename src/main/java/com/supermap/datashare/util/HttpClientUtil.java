package com.supermap.datashare.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author taochunda
 * @Description http请求工具类
 * @Date 2019-08-04 12:04
 **/
public class HttpClientUtil {

    /** 链接超时时间 **/
    private static final Integer CONNECT_TIME_OUT = 10 * 1000;

    /** 数据传输超时时间 **/
    private static final Integer SOCKET_TIME_OUT = 40 * 1000;

    private static Logger logger = Logger.getLogger(HttpClientUtil.class);


    public static HttpClient getHttpClient(){
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(CONNECT_TIME_OUT);
        // TODO 先不设置数据传输超时，根据实际情况可放开
//        configBuilder.setSocketTimeout(SOCKET_TIME_OUT);

        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setDefaultRequestConfig(configBuilder.build());
        HttpClient httpClient = clientBuilder.build();

        return httpClient;
    }

    /**
     * @Author taochunda
     * @Description 普通 post请求
     * @Date 2019-08-04 11:52
     * @param data       请求参数
     * @param requestUrl 请求地址
     * @return 请求返回结果
     **/
    public static String requestPost(final String data, final String requestUrl) {
        logger.info(String.format("发送 POST 请求--请求地址【%s】，请求参数【%s】", requestUrl, data));
        HttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(requestUrl);
        StringEntity stringEntity = new StringEntity(data, "UTF-8");
        httpPost.setEntity(stringEntity);
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");
//            logger.info(String.format("发送 POST 请求--请求返回结果【%s】", result));
//            logger.info(String.format("发送 POST 请求--请求返回结果【%s】", result));
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("发送 POST 请求--请求失败！ 详情：%s", e));
        }
        return null;
    }

    /**
     * @Author taochunda
     * @Description 普通 get 请求
     * @Date 2019-08-04 12:04
     * @Param requestUrl 请求地址
     * @return java.lang.String
     **/
    public static String requestGet(final String requestUrl) {
        logger.info(String.format("发送 GET 请求--请求地址【%s】", requestUrl));
        HttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");
            logger.info(String.format("发送 GET 请求--请求返回结果【%s】", result));
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("发送 GET 请求--请求失败！ 详情：%s", e));
        }
        return null;
    }
    
    
    /**
	 * 以payload形式发送post请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param payload
	 *            请求参数
	 * @return 响应信息
	 * @throws Exception
	 */
	public static String doPost(String url, String payload) throws Exception
	{
		if (url == null || url.isEmpty())
		{
			throw new Exception("请求地址不能为空。");
		}
		System.out.println("发送post请求：" + url);
		System.out.println("post参数：" + payload);
		URL realurl = new URL(url);
		URLConnection con = realurl.openConnection();
		con.setRequestProperty("accept", "*/*");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.connect();
		OutputStream os = con.getOutputStream();
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
		pw.write(payload);
		pw.close();

		InputStream inputStream = con.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null)
		{
			sb.append(line);
		}
		inputStream.close();
		String response = sb.length() == 0 ? null : sb.toString();
		System.out.println("响应消息：" + response);
		return response;
	}
	
	  /**
		 * 以payload形式发送post请求。
		 * 
		 * @param url
		 *            请求地址
		 * @param payload
		 *            请求参数
		 * @return 响应信息
		 * @throws Exception
		 */
		public static String doPostForWebs(String url, String param) throws Exception
		{
			String result="";
			
			URL postUrl = new URL(url);
	        // 打开连接
				HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
				connection.setDoOutput(true);
	        // Read from the connection. Default is true.
				connection.setDoInput(true);
				// 默认是 GET方式
				connection.setRequestMethod("POST"); 
				//connection.setRequestProperty("Accept-Charset", "gbk");
				//connection.setRequestProperty("contentType", "gbk");
				// Post 请求不能使用缓存
			    connection.setUseCaches(false); 
			    connection.setInstanceFollowRedirects(true); 
			    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
			    connection.connect();
			    //
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.write(param.getBytes("utf-8"));
				out.flush();
				out.close(); 
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
				StringBuilder sb = new StringBuilder();
				while((result=reader.readLine())!=null){
					sb.append(result);
				}
				
				return sb.toString().replace("\r\n", "");
		}

}
