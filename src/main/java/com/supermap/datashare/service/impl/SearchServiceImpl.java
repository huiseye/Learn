package com.supermap.datashare.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.supermap.datashare.dao.CommonDao;
import com.supermap.datashare.service.SearchService;
import com.supermap.datashare.util.GetProperties;

@Service("search")
public class SearchServiceImpl implements SearchService{
	
	@Autowired
	private CommonDao dao;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@Override
	public Map<String, String> getTCXX(String data) throws IOException {
		Map<String, String> map=new HashMap<String, String>();
		System.out.println("--------------进入跳转请求------------------");
		String result="";
		String url=GetProperties.getConstValueByKey("tccxurl");
		String conUrl="http://"+url+"/sharesearch/app/estate"; 
		URL postUrl = new URL(conUrl);
		        // 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setDoOutput(true);
		        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod("POST"); 
        // Post 请求不能使用缓存
        connection.setUseCaches(false); 
        connection.setInstanceFollowRedirects(true); 
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
	    connection.connect();
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		out.writeBytes("data="+data+"&username=admin&password=1");
		out.flush();
		out.close(); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		StringBuilder sb = new StringBuilder();
		while((result=reader.readLine())!=null){
			sb.append(result);
		}
		System.out.println("转换前："+sb.toString());
		System.out.println("转换后："+sb.toString().replace("\r\n", ""));
		map=(Map<String, String>) JSONObject.parse(sb.toString().replace("\r\n", ""));
      System.out.println("--------------跳转请求完毕------------------");  
      System.out.println("参数样例："+data+"&username=admin&password=1");
      return map;
	}

	@Override
	public List<HashMap<String, String>> getFWZT(String relationid, String bdcdylx) throws IOException {
		String result="";
		String url=GetProperties.getConstValueByKey("fwztcxutl");
		String conUrl="http://"+url+"/"+relationid+"/"+bdcdylx+"/"; 
		URL postUrl = new URL(conUrl);
		        // 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		connection.setRequestProperty("Accept-Charset", "utf-8");
		connection.setRequestProperty("contentType", "utf-8");
        connection.setDoOutput(false);
		        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod("GET"); 
        connection.setReadTimeout(100000);
        connection.setConnectTimeout(65000);
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
	    connection.connect();	
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
		StringBuilder sb = new StringBuilder();
		while((result=reader.readLine())!=null){
			sb.append(result);
		}
		List<HashMap<String, String>> maps =(List<HashMap<String, String>>) JSONObject.parse(sb.toString());
	      
      return maps;

	}
	
	
	public String swxxByHTH(String hth)
	  {
	    JaxWsDynamicClientFactory jcf = JaxWsDynamicClientFactory.newInstance();
	    Client tx = jcf.createClient("http://192.111.160.6/bdcxs/webservice/taxService?wsdl");
	    HTTPConduit conduit = (HTTPConduit)tx.getConduit();
	    conduit.getTarget().getAddress().setValue("http://192.111.160.6/bdcxs/webservice/taxService");
	    HTTPClientPolicy policy = new HTTPClientPolicy();
	    policy.setConnectionTimeout(36000L);
	    policy.setReceiveTimeout(120000L);
	    conduit.setClient(policy);
	    String qxdm = GetProperties.getConstValueByKey("qxdm");
	    Object[] result = new Object[0];
	    try
	    {
	      result = tx.invoke("getTaxInfo2", new Object[] { hth, qxdm });
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    String res = (String)result[0];

	    return res;
	  }

}
