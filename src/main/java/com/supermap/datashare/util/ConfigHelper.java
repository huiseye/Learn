package com.supermap.datashare.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.datashare.core.SuperSpringContext;
import com.supermap.datashare.dao.CommonDao;


/**
 * 
 * @Description:获取配置表信息
 * @author 俞学斌
 * @date 2015年12月20日 21:20:14
 * @Copyright SuperMap
 */
public class ConfigHelper {

	private static CommonDao dao;

	static {
		if (dao == null) {
			dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
	}

	/**
	 * 配置缓存
	 */
	private static final Map<String, String> configMap = new HashMap<String, String>();

//	/**
//	 * 根据属性名称获取属性值(先读取缓存，没有的时候再读取数据库)
//	 * 
//	 * @作者 俞学斌
//	 * @创建时间 2015年12月20日21:26:23
//	 * @param name
//	 * @return
//	 */
//	public static String getNameByValue(String name) {
//		String value="";
//		if(StringHelper.isEmpty(name))
//			return "";
//		name=name.toUpperCase();
//		if (configMap.containsKey(name)) {
//			value = configMap.get(name);
//		} else {
//			value = getValueByName(name);
//			configMap.put(name, value);
//		}
//		return value;
//	}
	
//	/**
//	 * 根据属性名称获取属性值(读取数据库)
//	 * 
//	 * @作者 俞学斌
//	 * @创建时间 2015年12月20日21:26:23
//	 * @param name
//	 * @return
//	 */
//	private static String getValueByName(String name) {
//		String Value="";
//		List<T_CONFIG> configs =dao.getDataList(T_CONFIG.class, " UPPER(NAME)='"+name+"'");
//		if (configs.size() > 0) {
//			if(!StringHelper.isEmpty(configs.get(0).getVALUE())){
//				Value=configs.get(0).getVALUE();
//			}
//		}
//		return Value;
//	}


	public static void reload()
	{
		configMap.clear();
	}
	
	/**
	 * 移除指定名称的缓存项
	 * @param name 参数名称
	 */
	public static void remove(String name)
	{
		if (name != null)
		{
			name=name.toUpperCase();
			if (configMap.containsKey(name))
			{
				configMap.remove(name);
			}
		}
	}
	
	/**
	 * 根据流程编号获取
	 * @作者 think
	 * @创建时间 2016年1月16日下午2:16:31
	 * @param workflowcode
	 * @return
	 */
//	public static T_PUSHCONFIG getPUSHCONFIG(String workflowcode){
//		T_PUSHCONFIG config=null;
//		try{
//		List<T_PUSHCONFIG> configs =dao.getDataList(T_PUSHCONFIG.class, " workflowcode='"+workflowcode+"'");
//		if (configs!=null&&configs.size() > 0) {
//				config=configs.get(0);
//		}
//		}
//		catch(Exception ex){
//			
//		}
//		return config;
//	}
}
