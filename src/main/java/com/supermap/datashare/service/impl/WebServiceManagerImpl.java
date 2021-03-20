package com.supermap.datashare.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.WebEndpoint;

import org.apache.log4j.Logger;
import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.supermap.datashare.dao.CommonDao;
import com.supermap.datashare.service.WebServiceManager;
import com.supermap.datashare.util.Rsa;

/**
 * webservice实现类(访问地址：http://localhost:8080/datashare/webservice/getleaseinfo?wsdl)
 * @author T460p
 *
 */
@WebService(targetNamespace="http://service.datashare.supermap.com/")
@Service("webservice")
public class WebServiceManagerImpl implements WebServiceManager{
	
	@Autowired
	private CommonDao dao;
	
	protected Logger logger = Logger.getLogger(WebServiceManagerImpl.class);
	
	/**
	 * 获取租赁信息（webservice方式）
	 * @param lessorName 承租人姓名
	 * @param lessorCode 承租人证件号
	 * @param lesseeName 租赁人姓名
	 * @param lesseeCode 租赁人证件号
	 * @return json字符串
	 */
	@Override
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
			)
	{
		Map map = new HashMap();
		try{
			lessorName = Rsa.decryptBySavedKey(lessorName);
			lessorCode = Rsa.decryptBySavedKey(lessorCode);
			lesseeName = Rsa.decryptBySavedKey(lesseeName);
			lesseeCode = Rsa.decryptBySavedKey(lesseeCode);
			StringBuilder str = new StringBuilder();
			str.append("SELECT rpad(substr(LESSOR_NAME,1,1),length(LESSOR_NAME)+1,'*') as LESSOR_NAME,"
					+ "lpad(substr(LESSOR_CODE,length(LESSOR_CODE)-3),length(LESSOR_CODE),'*') as LESSOR_CODE,"
					+ "rpad(substr(LESSEE_NAME,1,1),length(LESSEE_NAME)+1,'*') as LESSEE_NAME,"
					+ "lpad(substr(LESSEE_CODE,length(LESSEE_CODE)-3),length(LESSEE_CODE),'*') as LESSEE_CODE,"
					+ "LEASECERT_CODE,"
					+ "to_char(LEASE_START,'yyyy-mm-dd') as LEASE_START,to_char(LEASE_END,'yyyy-mm-dd') as LEASE_END,"
					+ "LEASE_USEFACT,HOUSE_AREA,HOUSE_ADDRESS,REALTYCERT_CODE,"
					+ "to_char(LEASECERT_CREATEDATE,'yyyy-mm-dd') as LEASECERT_CREATECODE "
					+ "FROM REALTYRIGHT_HISTORY.V_LEASEDATA WHERE 1=1 ");
			if(!StringHelper.isEmpty(lessorName)){
				str.append(" AND instr(LESSOR_NAME,'"+lessorName+"')>-1 ");
			}
			if(!StringHelper.isEmpty(lessorCode)){
				str.append(" AND instr(LESSOR_CODE,'"+lessorCode+"')>-1 ");
			}
			if(!StringHelper.isEmpty(lesseeName)){
				str.append(" AND instr(LESSEE_NAME,'"+lesseeName+"')>-1 ");
			}
			if(!StringHelper.isEmpty(lesseeCode)){
				str.append(" AND instr(LESSEE_CODE,'"+lesseeCode+"')>-1 ");
			}
			List<Map> maps = dao.getDataListByFullSql(str.toString());
			map.put("success", "true");
			map.put("result", maps);
		}catch(Exception e){
			map.put("success", "false");
			map.put("result", e.getMessage());
		}
		String json = JSONUtils.toJSONString(map);
		return json;
	}

}
