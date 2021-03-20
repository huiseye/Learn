package com.supermap.datashare.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.WebEndpoint;
/**
 * webservice通用类
 * @author T460p
 *
 */
@WebService(targetNamespace="http://service.datashare.supermap.com/")
public interface WebServiceManager {
	
	/**
	 * 获取租赁信息（webservice方式）
	 * @param lessorName 承租人姓名
	 * @param lessorCode 承租人证件号
	 * @param lesseeName 租赁人姓名
	 * @param lesseeCode 租赁人证件号
	 * @return
	 */
	@WebMethod(operationName="getleaseinfo")
	@WebEndpoint
	@WebResult(name="info",targetNamespace="http://service.datashare.supermap.com/")
	public String getLeaseInfo(
			@WebParam(name="arg0",
			targetNamespace="http://service.datashare.supermap.com/")String lessorName,
			@WebParam(name="arg1",
			targetNamespace="http://service.datashare.supermap.com/")String lessorCode,
			@WebParam(name="arg2",
			targetNamespace="http://service.datashare.supermap.com/")String lesseeName,
			@WebParam(name="arg3",
			targetNamespace="http://service.datashare.supermap.com/")String lesseeCode
			);
}
