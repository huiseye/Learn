package com.supermap.datashare.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.supermap.datashare.service.ZffwService;
import com.supermap.datashare.util.GetProperties;
import com.supermap.datashare.util.HttpClientUtil;
import com.supermap.datashare.util.StringHelper;

import net.sf.json.JSONArray;


@Controller
@RequestMapping("/zffw")
public class zffwController {
	
	@Autowired
	private ZffwService zffwService;
	Logger logger = Logger.getLogger(zffwController.class);

	
	@RequestMapping(value="yslpostsw",method=RequestMethod.POST)
	public @ResponseBody JSONObject postSW(HttpServletRequest request, HttpServletResponse response) throws Exception{
		 	String params =getJ(request);
		 	JaxWsDynamicClientFactory jcf = JaxWsDynamicClientFactory.newInstance();
		 	String url=GetProperties.getConstValueByKey("yslpostsw");
		 	String service=GetProperties.getConstValueByKey("yslservicesw");
		 	String XZQHDM=GetProperties.getConstValueByKey("XZQHDM");
		    Client tx = jcf.createClient(url);
		    HTTPConduit conduit = (HTTPConduit)tx.getConduit();
		    conduit.getTarget().getAddress().setValue(service);
		    HTTPClientPolicy policy = new HTTPClientPolicy();
		    policy.setConnectionTimeout(6000);
		    policy.setReceiveTimeout(6000);
		    conduit.setClient(policy);
		    Object[] result = new Object[0];
		    try {
		    	System.out.println(params);
		        result = tx.invoke("acceptFjDbxx", new Object[]{params,XZQHDM});
		        if(result!=null&&result.length!=0) {
		        	return  JSONObject.parseObject((String) result[0]);
		        }else {
		        	return null;
		        }
		    } catch (Exception e) {
		        e.printStackTrace();   
		        logger.error(e.getMessage());
		        return null;
		    }
		   
		}
	
	@RequestMapping(value="yslposttyzf",method=RequestMethod.POST)
	public @ResponseBody JSONObject postFG(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String result = getJ(request);
		JSONObject resultJson =JSONObject.parseObject(result);
		String url=resultJson.getString("url");
		String param=resultJson.getString("param");
		System.out.println("参数："+param);
		//param = URLEncoder.encode(param);
		String resultjson =HttpClientUtil.doPostForWebs(url, param);
		System.out.println("查询结果："+resultjson);
		if(!StringHelper.isEmpty(resultjson)) {
			return JSONObject.parseObject(resultjson);
		}else {
			return null;
		}
		
	}		
	
	@RequestMapping(value="/gftuserinfo",method=RequestMethod.POST)
	public @ResponseBody JSONObject getgftuserinfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ticket = request.getParameter("ticket");
		String url=GetProperties.getConstValueByKey("gftuserinfourl");
		String resultjson =HttpClientUtil.doPost(url+"?ticket="+ticket,"");
		if(!StringHelper.isEmpty(resultjson)) {
			return JSONObject.parseObject(resultjson);
		}else {
			return null;
		}
	}	
	
	 @RequestMapping(value = "/datatrans",method = RequestMethod.POST)
		@ResponseBody
		public JSONObject datatrans(HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject();
			try {
				response.setHeader("Access-Control-Allow-Origin", "*");    //跨域设置
				response.setContentType("application/json;charset=utf-8");    //编码设置
				long start = System.currentTimeMillis();
				jsonObject = zffwService.datatrans(request);
				long end = System.currentTimeMillis();
				System.out.println("调用转发接口时长:"+(end-start)+"毫秒");	
			} catch (Exception e) {
				jsonObject.put("code", "-1");
				jsonObject.put("msg", "转发信息出现异常！ 详情：" + e.getMessage());
				e.printStackTrace();
			}
			return jsonObject;
		}

	public String getJ(HttpServletRequest request) {
		BufferedReader br = null;
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
			if((str=br.readLine())!=null){
				sb.append(str);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}finally{
			try{
				br.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	

	   
	
}



