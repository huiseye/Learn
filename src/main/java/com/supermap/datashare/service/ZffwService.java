package com.supermap.datashare.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public interface ZffwService {

	JSONObject datatrans(HttpServletRequest request);
	


}
