package com.supermap.datashare.service;



import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public interface HtbaQueryService {

	JSONObject searchHtBeiAn(String pStr);
	JSONObject getFwInfoForFGHB(String realtionid);
	//延伸平台用，通过合同号获取
	com.alibaba.fastjson.JSONArray getFcinfoLlist(String hth);
	//获取房产楼盘信息
	JSONObject getzrz(HttpServletRequest request);
	//根据自然幢id获取相关房屋信息
	JSONObject gethinfos(String zrzbdcdyid);
	//通过房产合同号或权证号获取相关房屋信息
	JSONObject gethinfobyhtorzh(HttpServletRequest request);
}
