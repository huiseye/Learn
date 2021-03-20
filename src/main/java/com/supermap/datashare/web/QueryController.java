package com.supermap.datashare.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;

import com.supermap.datashare.service.QueryService;
import com.supermap.datashare.util.Rsa;

@Controller
@RequestMapping("/query")
public class QueryController {
	
	Logger logger = Logger.getLogger(QueryController.class);

	@Autowired
	private QueryService query;
	
	@RequestMapping(value="/getLessInfo",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String getAll(HttpServletRequest request, HttpServletResponse response){
		
		String str = "";
		//出租人
		String lessorName = request.getParameter("lessorName")==null?"":request.getParameter("lessorName");
		
		//出租人证件号
		String lessorCode = request.getParameter("lessorCode")==null?"":request.getParameter("lessorCode");
		
		//承租人
		String lesseeName = request.getParameter("lesseeName")==null?"":request.getParameter("lesseeName");
		
		//承租人证件号
		String lesseeCode = request.getParameter("lesseeCode")==null?"":request.getParameter("lesseeCode");
		
		String type = request.getParameter("type");
		try{
			//lessorName = Rsa.decryptBySavedKey(lessorName);
			//lessorCode = Rsa.decryptBySavedKey(lessorCode);
			//lesseeName = Rsa.decryptBySavedKey(lesseeName);
			//lesseeCode = Rsa.decryptBySavedKey(lesseeCode);
			str = query.getLeaseInfo(lessorName, lessorCode, lesseeName, lesseeCode,type);
		}catch(Exception e){
			logger.error(e.getStackTrace()+":"+e.getMessage());
		}
		return str;
	}
	
}
