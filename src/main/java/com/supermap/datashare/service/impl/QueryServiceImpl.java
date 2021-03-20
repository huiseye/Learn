package com.supermap.datashare.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebParam;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.supermap.datashare.dao.CommonDao;
import com.supermap.datashare.service.QueryService;

@Service("query")
public class QueryServiceImpl implements QueryService{
	
	@Autowired
	private CommonDao dao;

	/**
	 * 获取租赁信息（webservice方式）
	 * @param lessorName 承租人姓名
	 * @param lessorCode 承租人证件号
	 * @param lesseeName 租赁人姓名
	 * @param lesseeCode 租赁人证件号
	 * @return json字符串
	 */
	@Override
	public String getLeaseInfo(
			String lessorName, 
			String lessorCode, 
			String lesseeName, 
			String lesseeCode,
			String type
			) 
	{
		Map map = new HashMap();
		StringBuilder str = new StringBuilder();
		try{
			if("spf".equals(type)){
				str.append("SELECT HOUSE_ADDRESS,"
						+ " COMPACT_CODE,"
						+ " REALTYOWNER_NAME,"
						+ " REALTYOWNER_CODE,"
						+ " COMPACT_PRICE,"
						+ " BUILD_AREA,"
						+ " PAY_TYPE,"
						+ "to_char(COMPACTRECORDS_DATE,'yyyy-mm-dd') as COMPACTRECORDS_DATE "
						+ "FROM REALTYRIGHT_HISTORY.V_COMPACTDATA WHERE 1=1 ");
			
				if(!StringHelper.isEmpty(lessorName)){
					str.append(" AND instr(REALTYOWNER_NAME,'"+lessorName+"')>0 ");
				}
				if(!StringHelper.isEmpty(lessorCode)){
					str.append(" AND instr(REALTYOWNER_CODE,'"+lessorCode+"')>0 ");
				}
				if(!StringHelper.isEmpty(lesseeName)){
					str.append(" AND instr(REALTYOWNER_NAME,'"+lesseeName+"')>0 ");
				}
				if(!StringHelper.isEmpty(lesseeCode)){
					str.append(" AND instr(REALTYOWNER_CODE,'"+lesseeCode+"')>0 ");
				}
				
			}else if("wxzj".equals(type)){
				str.append("SELECT XQMC,"
						+ " FWZL,"
						+ " FH,"
						+ " MJ,"
						+ " BJHJ,"
						+ " LXZH,"
						+ " KYYE,"
						+ "to_char(CZSJ,'yyyy-mm-dd') as CZSJ,YZXM,ZJHM "
						+ "FROM REALTYRIGHT_HISTORY.V_MAINTAINFUND WHERE 1=1 ");
			
				if(!StringHelper.isEmpty(lesseeName)){
					str.append(" AND instr(YZXM,'"+lesseeName+"')>0 ");
				}
				if(!StringHelper.isEmpty(lesseeCode)){
					str.append(" AND instr(ZJHM,'"+lesseeCode+"')>0 ");
				}
			}
			else{
				str.append("SELECT LESSOR_NAME,"
						+ " LESSOR_CODE,"
						+ " LESSEE_NAME,"
						+ " LESSEE_CODE,"
						+ "LEASECERT_CODE,"
						+ "to_char(LEASE_START,'yyyy-mm-dd') as LEASE_START,to_char(LEASE_END,'yyyy-mm-dd') as LEASE_END,"
						+ "LEASE_USEFACT,HOUSE_AREA,HOUSE_ADDRESS,REALTYCERT_CODE,"
						+ "to_char(LEASECERT_CREATEDATE,'yyyy-mm-dd') as LEASECERT_CREATEDATE "
						+ "FROM REALTYRIGHT_HISTORY.V_LEASEDATA WHERE 1=1 ");
			
				if(!StringHelper.isEmpty(lessorName)){
					str.append(" AND instr(LESSOR_NAME,'"+lessorName+"')>0 ");
				}
				if(!StringHelper.isEmpty(lessorCode)){
					str.append(" AND instr(LESSOR_CODE,'"+lessorCode+"')>0");
				}
				if(!StringHelper.isEmpty(lesseeName)){
					str.append(" AND instr(LESSEE_NAME,'"+lesseeName+"')>0 ");
				}
				if(!StringHelper.isEmpty(lesseeCode)){
					str.append(" AND instr(LESSEE_CODE,'"+lesseeCode+"')>0 ");
				}
			}
			if("".equals(lessorName)&&"".equals(lessorCode)&&"".equals(lesseeName)&&"".equals(lesseeCode)){
				map.put("success", "false");
				map.put("result", "请输入条件");
				return JSONUtils.toJSONString(map);
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
