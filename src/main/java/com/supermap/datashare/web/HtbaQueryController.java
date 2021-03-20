package com.supermap.datashare.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.datashare.dao.CommonDaoJY;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/htbaQuery")
public class HtbaQueryController {
		
	private static final Logger logger = Logger.getLogger(HtbaQueryController.class);
	@Autowired
	  private CommonDaoJY dao;
	@Autowired
	private com.supermap.datashare.service.HtbaQueryService htbaQueryService; 
	
	@RequestMapping(value = "/getQueryInfoHtba", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getMokeQueryHtBeiAn(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		try {
			//String jsonStr = getHttpJsonParam(request);
			//String jsonStr = "[{\"ownername\":\"谭常青\",\"ownercode\":\"360729198803180025\"}]";
			String jsonStr = request.getParameter("data");
			System.out.println("jsonstr-------------------"+jsonStr);
			jsonObj = htbaQueryService.searchHtBeiAn(jsonStr);
			logger.info("查询参数:" + jsonStr);
			logger.info("查询合同备案信息接口成功:" + jsonObj.toString());
		} catch (Exception e) {
			logger.error("查询合同备案信息接口失败:", e);
		}
		return jsonObj;
		
		
	}
	
	@RequestMapping(value = "/getFwInfoForFGHB/{relationid}/", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getFwInfoForFGHB(@PathVariable("relationid") String relationid,HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj = htbaQueryService.getFwInfoForFGHB(relationid);
			logger.info("查询参数:" + relationid);
			logger.info("查询房屋信息接口成功:" + jsonObj.toString());
		} catch (Exception e) {
			logger.error("查询房屋信息接口失败:", e);
		}
		return jsonObj;
		
	}
	
	@RequestMapping(value = "/getfcinfolist/{hth}/", method = RequestMethod.POST)
	@ResponseBody
	public com.alibaba.fastjson.JSONArray getFcinfoLlist(@PathVariable("hth") String hth,HttpServletRequest request, HttpServletResponse response) {
		com.alibaba.fastjson.JSONArray maps = new com.alibaba.fastjson.JSONArray();
		try {
			maps = htbaQueryService.getFcinfoLlist(hth);
			logger.info("查询参数:" + hth);
			logger.info("查询房产合同信息接口成功!" );
		} catch (Exception e) {
			logger.error("查询房屋信息接口失败:", e);
		}
		return maps;
		
	}
	
	@RequestMapping(value = "/getzrz", method = RequestMethod.POST)
	public @ResponseBody JSONObject getzrz(HttpServletRequest request, HttpServletResponse response) {
		JSONObject m =new JSONObject();
		m= htbaQueryService.getzrz(request);
		return m;
	}
	
	@RequestMapping(value = "/gethinfos/{zrzbdcdyid}/", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject gethinfo(@PathVariable("zrzbdcdyid") String zrzbdcdyid,HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj = htbaQueryService.gethinfos(zrzbdcdyid);
			logger.info("查询参数:" + zrzbdcdyid);
		} catch (Exception e) {
			logger.error("查询房产户信息接口失败:", e);
		}
		return jsonObj;
		
	}
	
	@RequestMapping(value = "/gethinfobyhtorzh", method = RequestMethod.POST)
	public @ResponseBody JSONObject gethinfobyhtorzh(HttpServletRequest request, HttpServletResponse response) {
		JSONObject m =new JSONObject();
		m= htbaQueryService.gethinfobyhtorzh(request);
		return m;
	}
	
}
