package com.supermap.datashare.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class GetProperties {
	public static String CONFIG_PATH = "/config.properties";// 配置文件名称

	public GetProperties() {

	}
	/**
	 * 读取配置文件中的配置
	 * 
	 * @param key
	 *            配置名称
	 * @author diaoliwei
	 * @return
	 */
	public static String getConstValueByKey(String key) {
		String value = "";
		try {
			if (getPropertiesFromResorceStream().containsKey(key)) {
				value = getPropertiesFromResorceStream().getProperty(key);
			}
		} catch (Exception e) {
			
		}
		return value;
	}

	/**
	 * 加载Properties文件
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月15日下午9:55:34
	 * @return
	 */
	private static Properties getPropertiesFromResorceStream() {
		Properties prop = new Properties();
		try {
			// TODO 需修改为只加载一次就可，不需要每次获取属性都加载文件
			prop.load(getInputStream());
		} catch (IOException e) {
			
		}
		return prop;
	}

	/**
	 * InputStream读取文件
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月15日下午9:53:51
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static InputStreamReader getInputStream()
			throws UnsupportedEncodingException {
		return new InputStreamReader(GetProperties.class.getClassLoader()
				.getResourceAsStream("config.properties"), "UTF-8");
		// return GetProperties.class.getResourceAsStream(CONFIG_PATH);
	}
}
