package com.supermap.datashare.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.datashare.dao.CommonDao;
import com.supermap.datashare.service.SearchService;
import com.supermap.datashare.service.ZffwService;
import com.supermap.datashare.util.GetProperties;
import com.supermap.datashare.util.HttpClientUtil;
import com.supermap.datashare.util.StringHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import it.sauronsoftware.base64.*;

@Service("zffw")
public class zffwServiceImpl implements ZffwService{
	
	@Autowired
	private CommonDao dao;
	
	@Override
	public JSONObject datatrans(HttpServletRequest request) {
		JSONObject obj = new JSONObject();
		JSONObject decryptJson= JSONObject.parseObject(getJ(request));
		String url = decryptJson.getString("url");
		if(!StringHelper.isEmpty(url)) {
			decryptJson.remove("url");
			String jsonstr = HttpClientUtil.requestPost(decryptJson.toJSONString(), url);
			if(!StringHelper.isEmpty(jsonstr)) {
				return JSONObject.parseObject(jsonstr);
			}else {
				return null;
			}
		}else {
			return null;
		}
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
