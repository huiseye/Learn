package com.supermap.datashare.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;

import com.supermap.datashare.service.QueryService;
import com.supermap.datashare.service.SearchService;
import com.supermap.datashare.util.Rsa;

@Controller
@RequestMapping("/search")
public class SearchController {

	Logger logger = Logger.getLogger(QueryController.class);

	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "/tccx", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, String> getTCXX(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String data = request.getParameter("data");
		return searchService.getTCXX(data);
	}

	@RequestMapping(value = "/fwztcx/{relationid}/{bdcdylx}/", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<HashMap<String, String>> getFWZT(@PathVariable("relationid") String relationid,
			@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		return searchService.getFWZT(relationid, bdcdylx);
	}

	@RequestMapping(value ="/swxxbyhth", method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public String getSWXXByHTH(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String hth = request.getParameter("hth");
		String str = searchService.swxxByHTH(hth);
		return str;
	}
}
