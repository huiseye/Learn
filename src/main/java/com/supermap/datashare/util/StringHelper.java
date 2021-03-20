package com.supermap.datashare.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

/**
 * 字符串操作工具类
 * 
 * @author 刘树峰
 *
 */
public class StringHelper {

	/**
	 * 用正则表达式匹配不动产权证号中的省简称、年度，地区名和流水号,获取不动产权证号序号
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月17日下午9:55:55
	 * @param bdczmh
	 * @return
	 */
	public static String getBDCQZHXH(String BDCQZH) {
		String BDCQZHXH = "";
		String regex = "(.*)[(](.*)[)](.*)不动产.*第(.*)号";
		Pattern pattern = Pattern.compile(regex);
		if (!StringHelper.isEmpty(BDCQZH)) {
			Matcher m = pattern.matcher(BDCQZH);
			while (m.find()) {
				BDCQZHXH = m.group(2) + m.group(4);
			}
			if (StringHelper.isEmpty(BDCQZHXH)) {
				BDCQZHXH = BDCQZH;
			}
		}
		return BDCQZHXH;
	}

	/**
	 * 判断字符串是否纯数字的
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月15日下午6:09:24
	 * @param bdcqzh
	 * @return
	 */
	public static boolean isNumericByString(String bdcqzh) {
		boolean flag = true;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(bdcqzh);
		if (!isNum.matches()) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 用正则表达式匹配不动产权证号中的省简称、年度，地区名和流水号
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月17日下午9:55:55
	 * @param bdczmh
	 * @return
	 */
	public static List<String> MatchBDCQZH(String bdcqzh) {
		String regex = "(.*)[(](.*)[)](.*)不动产权第(.*)号";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(bdcqzh);
		List<String> list = new ArrayList<String>();
		while (m.find()) {
			list.add(m.group(1));
			list.add(m.group(2));
			list.add(m.group(3));
			list.add(m.group(4));
		}
		return list;
	}

	/**
	 * 用正则表达式匹配不动产登记证明号中的省简称、年度，地区名和流水号
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月17日下午9:55:55
	 * @param bdczmh
	 * @return
	 */
	public static List<String> MatchBDCZMH(String bdczmh) {
		String regex = "(.*)[(](.*)[)](.*)不动产证明第(.*)号";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(bdczmh);
		List<String> list = new ArrayList<String>();
		while (m.find()) {
			list.add(m.group(1));
			list.add(m.group(2));
			list.add(m.group(3));
			list.add(m.group(4));
		}
		return list;
	}

	/**
	 * 对于数字型字段，根据内容是否null,获取对应的值
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月17日下午9:54:55
	 * @param obj
	 * @return
	 */
	public static String FormatByDatatype(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * 对于日期，根据内容是否null,获取对应的值yyyy年MM月dd日
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月17日下午9:55:49
	 * @param obj
	 * @return
	 */
	public static String FormatByDatetime(Object obj) {
		String sj = "";
		if (obj != null && obj != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			sj = sdf.format(obj);
		}
		return sj;
	}
	
	/** 对于日期，根据内容是否null,获取对应的值yyyy-MM-dd
	 * @param obj
	 * @return
	 */
	public static String FormatByDatetime_(Object obj) {
		String sj = "";
		if (obj != null && obj != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sj = sdf.format(obj);
		}
		return sj;
	}

	/**
	 * 转成date类型
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月20日上午1:30:43
	 * @param obj
	 * @return
	 * @throws ParseException
	 */
	public static Date FormatByDate(Object obj) throws ParseException {
		Date date = new Date();
		if (obj != null) {
			String sj = obj.toString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(sj);
		}
		return date;
	}

	/**
	 * 转成date类型
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月5日 10:15:43
	 * @param obj
	 * @param formatType
	 * @return
	 * @throws ParseException
	 */
	public static Date FormatByDate(Object obj, String formatType)
			throws ParseException {
		Date date = new Date();
		if (obj != null) {
			String sj = obj.toString();
			SimpleDateFormat sdf = new SimpleDateFormat(formatType);
			date = sdf.parse(sj);
		}
		return date;
	}

	/**
	 * 日期格式转换成年月日时分秒形式
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月2日下午3:31:01
	 * @param obj
	 * @return
	 * @throws ParseException
	 */
	public static String FormatYmdhmsByDate(Object obj) {
		DateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");// 日期转换
		String strDate = "";
		try {
			if (!isEmpty(obj)) {
				strDate = sdf1.format(obj);
			}
		} catch (Exception e) {
		}

		return strDate;
	}

	/**
	 * 根据指定格式转化日期
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月2日下午3:31:01
	 * @param obj
	 * @return
	 * @throws ParseException
	 */
	public static String FormatDateOnType(Object obj, String formatType) {
		DateFormat sdf1 = new SimpleDateFormat(formatType);// 日期转换
		String strDate = "";
		try {
			if (!isEmpty(obj)) {
				strDate = sdf1.format(obj);
			}
		} catch (Exception e) {
		}

		return strDate;
	}

	/**
	 * 格式化数组，生成以逗号隔开的字符串
	 * 
	 * @param lst
	 * @return
	 */
	public static String formatList(List<String> lst) {
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (String str : lst) {
			if (flag) {
				result.append(",");
			} else {
				flag = true;
			}
			result.append(str);
		}
		return result.toString();
	}

	/**
	 * 根据指定字符格式化数组，生成字符串
	 * 
	 * @param lst
	 * @param splitinfo
	 * @return
	 */
	public static String formatList(List<String> lst, String splitinfo) {
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (String str : lst) {
			if (flag) {
				result.append(splitinfo);
			} else {
				flag = true;
			}
			result.append(str);
		}
		return result.toString();
	}
	
	/**
	 * 根据指定字符格式化数组，生成字符串
	 * 
	 * @param lst
	 * @param splitinfo
	 * @return
	 */
	public static <T> String formatListEx(List<T> lst, String splitinfo) {
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (Object str : lst) {
			if (flag) {
				result.append(splitinfo);
			} else {
				flag = true;
			}
			result.append(str);
		}
		return result.toString();
	}

//	/**
//	 * 根据指定字符格式化数组，生成字符串
//	 * 
//	 * @param lst
//	 * @param splitinfo
//	 * @return
//	 */
//	public static String formatListByConfig(List<String> lst, String splitinfo, String config) {
//		int count = 0;
//		if(isEmpty(config)) {
//			String configSBT = ConfigHelper.getNameByValue("SQSPBDY_BDCDYH_YT");
//			if (!StringHelper.isEmpty(configSBT)) {
//				 count = Integer.parseInt(configSBT);
//			}
//		}else {
//			count = Integer.parseInt(config);
//		}
//		StringBuilder result = new StringBuilder();
//		boolean flag = false;
//		int countFlag = 0;
//		for (String str : lst) {
//			if(++countFlag>count) {
//				result.append("等"+lst.size()+"个");
//				break;
//			}
//			if (flag) {
//				result.append(splitinfo);
//			} else {
//				flag = true;
//			}
//			result.append(str);
//		}
//		return result.toString();
//	}
	
	/**
	 * 获取对象的tostring，如果对象为空返回空字符串
	 * 
	 * @Title: formatObject
	 * @author:liushufeng
	 * @date：2015年7月12日 下午10:17:13
	 * @param o
	 * @return
	 */
	public static String formatObject(Object o) {
		if (o != null) {
			if((o instanceof Integer)||(o instanceof Double)) {
				return formatDouble(o);
			}
			return o.toString();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * double转换为字符串，去除末尾的0，如果没有小数部分，结果返回整数部分字符串
	 * 
	 * @Title: formatDouble
	 * @author:liushufeng
	 * @date：2015年7月12日 下午10:15:20
	 * @param d
	 *            double值
	 * @return 转换后的字符串
	 */
	public static String formatDouble(Object obj) {
		String _doubleStr = "";
		if (isEmpty(obj)) {
			return _doubleStr;
		}
		if(obj instanceof String){
			String str=obj.toString();
			str=StringHelper.TrimAll(str, "0");
			if(StringHelper.isEmpty(str)){
				return _doubleStr;
			}
			if(".".equals(str)){
				return _doubleStr;
			}
			if(str.endsWith(".")){
				str=StringHelper.TrimRight(str, ".");
			}
			if(str.endsWith(".")){
				str=StringHelper.TrimRight(str, ".");
			}
			if(str.startsWith(".")){
				str="0"+str;
			}
			obj=str;
		}
		String i = DecimalFormat.getInstance().format(obj);
		_doubleStr = i.replaceAll(",", "");
		return _doubleStr;
	}
	
	/**
	 * 去掉指定字符串的开头和结尾的指定字符
	 *
	 * @param stream 要处理的字符串
	 * @param trimstr 要去掉的字符串
	 * @return 处理后的字符串
	 */
	public static String TrimAll(String stream, String trimstr) {
		// null或者空字符串的时候不处理
		if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
			return stream;
		}
		// 结束位置
		int epos = 0;
		// 正规表达式
		String regpattern = "[" + trimstr + "]*+";
		Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
		// 去掉结尾的指定字符 
		StringBuffer buffer = new StringBuffer(stream).reverse();
		Matcher matcher = pattern.matcher(buffer);
		if (matcher.lookingAt()) {
			epos = matcher.end();
			stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
		}
		// 去掉开头的指定字符 
		matcher = pattern.matcher(stream);
		if (matcher.lookingAt()) {
			epos = matcher.end();
			stream = stream.substring(epos);
		}
		// 返回处理后的字符串
		return stream;
	}
	
	/**
	 * 去掉指定字符串的开头指定字符
	 *
	 * @param stream 要处理的字符串
	 * @param trimstr 要去掉的字符串
	 * @return 处理后的字符串
	 */
	public static String TrimLeft(String stream, String trimstr) {
		// null或者空字符串的时候不处理
		if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
			return stream;
		}
		// 结束位置
		int epos = 0;
		// 正规表达式
		String regpattern = "[" + trimstr + "]*+";
		Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
		// 去掉开头的指定字符  
		StringBuffer buffer = new StringBuffer(stream).reverse();
		Matcher matcher = pattern.matcher(buffer);
		matcher = pattern.matcher(stream);
		if (matcher.lookingAt()) {
			epos = matcher.end();
			stream = stream.substring(epos);
		}
		// 返回处理后的字符串
		return stream;
	}
	
	/**
	 * 去掉指定字符串结尾的指定字符
	 *
	 * @param stream 要处理的字符串
	 * @param trimstr 要去掉的字符串
	 * @return 处理后的字符串
	 */
	public static String TrimRight(String stream, String trimstr) {
		// null或者空字符串的时候不处理
		if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
			return stream;
		}
		// 结束位置
		int epos = 0;
		// 正规表达式
		String regpattern = "[" + trimstr + "]*+";
		Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
		// 去掉结尾的指定字符 
		StringBuffer buffer = new StringBuffer(stream).reverse();
		Matcher matcher = pattern.matcher(buffer);
		if (matcher.lookingAt()) {
			epos = matcher.end();
			stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
		}
		// 返回处理后的字符串
		return stream;
	}

	/**
	 * 根据指定格式格式化双精度值
	 * 
	 * @Title: formatDouble
	 * @author:yuxuebin
	 * @date：2016年10月08日 09:24:20
	 * @param obj
	 *            双精度值
	 * @param formatType
	 *            格式化类型
	 * @return 转换后的字符串
	 */
	public static String formatDouble(Object obj, String formatType) {
		String value = "";
		if (!StringHelper.isEmpty(obj)) {
			DecimalFormat f = new DecimalFormat(formatType);
			f.setRoundingMode(RoundingMode.HALF_UP);
			value = f.format(obj);
		}
		return value;
	}

	/**
	 * 转换为double类型
	 * 
	 * @作者 liushufeng
	 * @创建时间 2015年7月24日上午12:20:13
	 * @param o
	 * @return
	 */
	public static double getDouble(Object o) {
		double d = 0;
		if (o == null) {
			d = 0;
		} else {
			try {
				d = Double.parseDouble(o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	/**
	 * 转换为BigDecimal类型
	 * 
	 * @author diaoliwei
	 * @date 2016-1-28
	 * @param value
	 * @return
	 */
	public static BigDecimal getBigDecimal(Object value) {
		BigDecimal ret = new BigDecimal(0);
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else {
				throw new ClassCastException("未找到转换的类型");
			}
		}
		return ret;
	}

	/**
	 * 转换为long类型
	 * 
	 * @作者 海豹
	 * @创建时间 2015年12月1日下午6:45:34
	 * @param o
	 * @return
	 */
	public static long getLong(Object o) {
		long d = 1;
		if (o == null) {
			d = 0;
		} else {
			try {
				d = Long.parseLong(o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	/**
	 * 将对象转换成整形的
	 * 
	 * @作者 海豹
	 * @创建时间 2016年3月18日下午3:42:27
	 * @param obj
	 * @return
	 */
	public static int getInt(Object obj) {
		int i = 0;
		if (isEmpty(obj)) {
			i = 0;
		} else {
			try {
				i = Integer.parseInt(obj.toString());
			} catch (Exception e) {
				i = 0;
			}
		}
		return i;
	}

	public static boolean isEmpty(Object str) {
		return org.springframework.util.StringUtils.isEmpty(str);
	}

	public static String PadLeft(String input, int size, char symbol) {
		while (input.length() < size) {
			input = symbol + input;
		}
		return input;
	}

	public static String PadRight(String input, int size, char symbol) {
		while (input.length() < size) {
			input = input + symbol;
		}
		return input;
	}

	/**
	 * @author diaoliwei
	 * @since 2016/1/28 14:22
	 * @param math
	 *            将要截取的数字
	 * @param bit
	 *            要保留的小数点位数
	 * @return
	 */
	public static BigDecimal cutBigDecimal(BigDecimal math, int bit) {
		if (math == null) {
			return new BigDecimal(0);
		}
		BigDecimal bigDecimal = null;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(100);
		nf.setMaximumIntegerDigits(100);
		String mathProxy = nf.format(math);
		BigDecimal bg = new BigDecimal(mathProxy);
		String num;
		String mathProxy2 = "";
		if (mathProxy.indexOf(".") > 0) {
			num = mathProxy.substring(mathProxy.indexOf(".") + 1);
			mathProxy2 = mathProxy.substring(0, mathProxy.indexOf(".") + 1);
			if (bit == 4 && num.length() >= 4) {
				num = num.substring(0, bit);
				mathProxy2 = mathProxy2.concat(num);
			} else if (bit == 2 && num.length() >= 2) {
				num = num.substring(0, bit);
				mathProxy2 = mathProxy2.concat(num);
			} else if (bit == 3 && num.length() >= 3) {
				num = num.substring(0, bit);
				mathProxy2 = mathProxy2.concat(num);
			} else if (bit == 8 && num.length() >= 8) {
				num = num.substring(0, bit);
				mathProxy2 = mathProxy2.concat(num);
			} else {
				return bg;
			}
		} else {
			mathProxy2 = nf.format(math);
		}
		bigDecimal = new BigDecimal(mathProxy2);
		return bigDecimal;
	}

	/**
	 * @author hanxu
	 * @since 2015/10/26 14:22
	 * @param math
	 *            将要截取的数字
	 * @param bit
	 *            要保留的小数点位数
	 * @bug 位数超过16位，会丧失精度
	 * @return
	 */
	public static String cut(Double math, int bit) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(100);
		nf.setMaximumIntegerDigits(100);
		String mathProxy = nf.format(math);
		BigDecimal bg = new BigDecimal(mathProxy);
		String yuan = bg.toPlainString();
		String num;
		String mathProxy2 = "";
		if (mathProxy.indexOf(".") > 0) {
			num = mathProxy.substring(mathProxy.indexOf(".") + 1);
			mathProxy2 = mathProxy.substring(0, mathProxy.indexOf(".") + 1);
			if (bit == 4 && num.length() >= 4) {
				num = num.substring(0, bit);
				mathProxy2 = mathProxy2.concat(num);
			} else if (bit == 2 && num.length() >= 2) {
				num = num.substring(0, bit);
				mathProxy2 = mathProxy2.concat(num);
			} else if (bit == 3 && num.length() >= 3) {
				num = num.substring(0, bit);
				mathProxy2 = mathProxy2.concat(num);
			} else if (bit == 8 && num.length() >= 8) {
				num = num.substring(0, bit);
				mathProxy2 = mathProxy2.concat(num);
			} else {
				return yuan;
			}
			return mathProxy2;
		} else {
			mathProxy2 = nf.format(math);
		}
		return mathProxy2;
	}

	/**
	 * 字符串数字根据参数加（前面有0的情况下）
	 * 
	 * @author diaoliwei
	 * @date 2015-12-12 17:10
	 * @param num
	 * @param length
	 * @param addend
	 * @return
	 */
	public static String getCountByParamter(String num, int length, int addend) {
		int i = Integer.valueOf(num) + addend;
		StringBuffer sb = new StringBuffer();
		String countNum = String.valueOf(i);
		for (int j = 0; j < length - countNum.length(); j++) {
			sb.append("0");
		}
		if (countNum.length() < length) {
			countNum = sb.toString() + countNum;
		} else {
			System.out.println("超出长度");
		}
		return countNum;
	}

	/**
	 * 写入文件 --likun
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static void writeToFile(String strContent, String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists())
				file.createNewFile();
			// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			FileOutputStream out = new FileOutputStream(file, false); // 如果追加方式用true
			StringBuffer sb = new StringBuffer();
			// sb.append("-----------"+sdf.format(new Date())+"------------\n");
			sb.append(strContent + "\n");
			out.write(sb.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
			out.close();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	/**
	 * 读取文件 --likun
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static String readFile(String filepath) {
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(filepath);
			if (!file.exists())
				throw new FileNotFoundException();

			FileInputStream fis = new FileInputStream(file);
			// BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			// while((tempstr=br.readLine())!=null)
			// sb.append(tempstr);
			InputStreamReader isr = new InputStreamReader(fis, "utf8");
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			isr.close();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
		return sb.toString();
	}

	//
	public static final String MAP_PARAM_CLASS = "收件资料匹配";// Map中类名后缀

	/**
	 * 序列化成Json字符串 --likun
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static String mapToJson(Map<String, String> map) throws Exception {
		JSONObject jsonOjb = new JSONObject();
		// Param中无数据
		if (map.isEmpty()) {
			return jsonOjb.toString();
		}

		JSONObject mapObj = new JSONObject();
		for (String key : map.keySet()) {
			mapObj.put(key, map.get(key));// .getClass().getName()
		}
		jsonOjb.put(MAP_PARAM_CLASS, mapObj);
		return jsonOjb.toString();
	}

	/**
	 * Json反序列化 --likun
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public static Map<String, String> JsonToMap(String json) throws Exception {
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject jsonMapClass = jsonObj.getJSONObject(MAP_PARAM_CLASS);
		// jsonObj.remove(MAP_PARAM_CLASS);
		JSONObject jsonMap = jsonObj.getJSONObject(MAP_PARAM_CLASS);// MAP_PARAM
		// RequestParam request = (RequestParam)
		// JSONObject.toBean(jsonObj,RequestParam.class);
		// Param中无数据
		if (jsonMapClass == null || jsonMapClass.isNullObject()) {
			return null;
		}
		JsonConfig jsonCfg = new JsonConfig();
		jsonCfg.setRootClass(Map.class);
		Map<String, String> classMap = new HashMap<String, String>();
		Iterator<String> it = jsonMapClass.keys();
		while (it.hasNext()) {
			String key = it.next();
			// Class<?> c = Class.forName(jsonMapClass.getString(key));
			classMap.put(key, jsonMapClass.getString(key));
		}
		jsonCfg.setClassMap(classMap);
		Map<String, String> map = (Map<String, String>) JSONObject.toBean(
				jsonMap, jsonCfg);
		// request.getReqParam().putAll(map);
		return map;
	}

	/**
	 * 判断当前是否有公章图片
	 * 
	 * @author diaoliwei
	 * @date 2016-1-14
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean existZhangImg(HttpServletRequest request) {
		// 判断是否有公章图片，没有则避免显示损坏图片
		String path = request.getRealPath("/") + File.separator + "resources"
				+ File.separator + "images" + File.separator + "zhang.png";
		File file = new File(path);
		boolean exist = false;
		if (file.exists()) {
			exist = true;
		}
		return exist;
	}

	/**
	 * 判断对象是否为空，为空时默认给值：“无”
	 * 
	 * @author diaoliwei
	 * @date 2016-1-27
	 * @param o
	 * @return o or "无"
	 */
	public static String formatDefaultValue(Object o) {
		if (o != null) {
			return o.toString();
		}
		return "无";
	}

	/**
	 * 根据Map对象对Object对象赋值
	 * 
	 * @author yuxuebin
	 * @date 2016年4月27日 00:02
	 * @param map
	 * @param thisObj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static void setValue(Map map, Object thisObj) {
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			Object val = map.get(obj);
			setMethod(obj, val, thisObj);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setMethod(Object method, Object value, Object thisObj) {
		Class c;
		try {
			c = Class.forName(thisObj.getClass().getName());
			String met = (String) method;
			met = met.trim();
			if (!met.substring(0, 1).equals(met.substring(0, 1).toUpperCase())) {
				met = met.toUpperCase();
			}
			if (!String.valueOf(method).startsWith("set")) {
				met = "set" + met;
			}
			Class types[] = getMethodParamTypes(thisObj, met);
			if (types != null && types.length > 0) {
				Method m = c.getMethod(met, types);
				if (types[0].getName().contains("String")) {
					String strValue = StringHelper.formatObject(value);
					m.invoke(thisObj, strValue);
				} else if (types[0].getName().contains("Double")) {

					Double doubleValue = 0.0;
					if (!StringHelper.isEmpty(value)) {
						doubleValue = StringHelper.getDouble(value);
					}
					m.invoke(thisObj, doubleValue);
				} else if (types[0].getName().contains("Date")) {
					Object obj = null;
					if (!StringHelper.isEmpty(value)) {
						if (StringHelper.formatObject(value).contains("-")) {
							m.invoke(thisObj, StringHelper.FormatByDate(value));
						} else if (StringHelper.formatObject(value).contains(
								"/")) {
							m.invoke(thisObj, StringHelper.FormatByDate(value,
									"yyyy/MM/dd"));
						} else {
							m.invoke(thisObj, StringHelper.FormatByDate(value,
									"yyyyMMdd"));
						}
					} else {
						m.invoke(thisObj, obj);
					}
				} else if (types[0].getName().contains("Integer")) {
					int intValue = 0;
					if (!StringHelper.isEmpty(value)) {
						intValue = (int) StringHelper.getDouble(value);
					}
					m.invoke(thisObj, intValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public static Class[] getMethodParamTypes(Object classInstance,
			String methodName) throws ClassNotFoundException {
		Class[] paramTypes = null;
		Method[] methods = classInstance.getClass().getMethods();// 全部方法
		for (int i = 0; i < methods.length; i++) {
			if (methodName.equals(methods[i].getName())) {// 和传入方法名匹配
				Class[] params = methods[i].getParameterTypes();
				paramTypes = new Class[params.length];
				for (int j = 0; j < params.length; j++) {
					paramTypes[j] = Class.forName(params[j].getName());
				}
				break;
			}
		}
		return paramTypes;
	}

	/**
	 * 日期格式转换成年月日时分秒形式
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月2日下午3:31:01
	 * @param obj
	 * @return
	 * @throws ParseException
	 */
	public static String FormatYmdhmsByDateEx(Object obj) {
		DateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");// 日期转换
		String strDate = "";
		try {
			if (!isEmpty(obj)) {
				strDate = sdf1.format(obj);
			}
		} catch (Exception e) {
		}

		return strDate;
	}

	/**
	 * 日期格式转换成年月日时分秒形式
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月2日下午3:31:01
	 * @param obj
	 * @return
	 * @throws ParseException
	 */
	public static String FormatYmdByDate(Object obj) {
		DateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");// 日期转换
		String strDate = "";
		try {
			if (!isEmpty(obj)) {
				strDate = sdf1.format(obj);
			}
		} catch (Exception e) {
		}

		return strDate;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map ConvertDateOnMap(Map m) {
		if (m == null) {
			return m;
		}
		Set set = m.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			Object val = m.get(obj);
			if (val != null && val instanceof Date) {
				m.put(obj.toString(), StringHelper.FormatDateOnType(val,
						"yyyy-MM-dd HH:mm:ss"));
			}
		}
		return m;
	}

	@SuppressWarnings("rawtypes")
	public static List<Map> ConvertDateOnListMap(List<Map> list) {
		if (list == null || list.size() <= 0) {
			return list;
		}
		List<Map> list_new = new ArrayList<Map>();
		for (Map m : list) {
			Map m_new = ConvertDateOnMap(m);
			list_new.add(m_new);
		}
		return list_new;
	}

	/**
	 * 数字转换为汉语中人民币的大写
	 * 
	 * @author zmf
	 * @create 2016-10-19 11:24:51
	 */
	public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {

		/**
		 * 汉语中数字大写
		 */
		final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆",
				"柒", "捌", "玖" };
		/**
		 * 汉语中货币单位大写，这样的设计类似于占位符
		 */
		final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟",
				"万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟" };
		/**
		 * 特殊字符：整
		 */
		final String CN_FULL = "整";
		/**
		 * 特殊字符：负
		 */
		final String CN_NEGATIVE = "负";
		/**
		 * 金额的精度，默认值为2
		 */
		final int MONEY_PRECISION = 4;
		/**
		 * 特殊字符：零元整
		 */
		final String CN_ZEOR_FULL = "零元" + CN_FULL;

		StringBuffer sb = new StringBuffer();
		// -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
		// positive.
		int signum = numberOfMoney.signum();
		// 零元整的情况
		if (signum == 0) {
			return CN_ZEOR_FULL;
		}
		// 这里会进行金额的四舍五入
		long number = numberOfMoney.movePointRight(MONEY_PRECISION)
				.setScale(0, 4).abs().longValue();
		// 得到小数点后两位值
		long scale = number % 100;
		int numUnit = 0;
		int numIndex = 0;
		boolean getZero = false;
		// 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
		if (!(scale > 0)) {
			numIndex = 2;
			number = number / 100;
			getZero = true;
		}
		if ((scale > 0) && (!(scale % 10 > 0))) {
			numIndex = 1;
			number = number / 10;
			getZero = true;
		}
		int zeroSize = 0;
		while (true) {
			if (number <= 0) {
				break;
			}
			// 每次获取到最后一个数
			numUnit = (int) (number % 10);
			if (numUnit > 0) {
				if ((numIndex == 9) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
				}
				if ((numIndex == 13) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
				}
				sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				getZero = false;
				zeroSize = 0;
			} else {
				++zeroSize;
				if (!(getZero)) {
					sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				}
				if (numIndex == 2) {
					if (number > 0) {
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					}
				} else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				}
				getZero = true;
			}
			// 让number每次都去掉最后一个数
			number = number / 10;
			++numIndex;
		}
		// 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
		if (signum == -1) {
			sb.insert(0, CN_NEGATIVE);
		}
		// 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
		if (!(scale > 0)) {
			sb.append(CN_FULL);
		}
		return sb.toString();
	}

	/**
	 * xml转换成map，当节点位置和名称均相同时则转成list 注意不处理节点的属性 
	 * 
	 * @author yuxuebin
	 * @date 2016年11月02日 013:56
	 * @param xmlString
	 * @return
	 */
	public static HashMap<String,Object> xmlTomap(String xmlString) throws DocumentException {
		Document doc = DocumentHelper.parseText(xmlString);
		Element rootElement = doc.getRootElement();
		HashMap<String, Object> map = new HashMap<String, Object>();
		eleTomap(map, rootElement);
		System.out.println(map);
		// 到此xml2map完成，下面的代码是将map转成了json用来观察我们的xml2map转换的是否ok
		String string = JSONObject.fromObject(map).toString();
		System.out.println(string);
		return map;
	}

	/**
	 * element转换成map，当节点位置和名称均相同时则转成list 注意不处理节点的属性 
	 * 
	 * @author yuxuebin
	 * @date 2016年11月02日 013:56
	 * @param map
	 * @param ele
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static void eleTomap(Map map, Element ele) {
		System.out.println(ele);
		// 获得当前节点的子节点
		List<Element> elements = ele.elements();
		if (elements.size() == 0) {
			// 没有子节点说明当前节点是叶子节点，直接取值即可
			map.put(ele.getName(), ele.getText());
		} else if (elements.size() == 1) {
			// 只有一个子节点说明不用考虑list的情况，直接继续递归即可
			Map<String, Object> tempMap = new HashMap<String, Object>();
			eleTomap(tempMap, elements.get(0));
			map.put(ele.getName(), tempMap);
		} else {
			// 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
			// 构造一个map用来去重
			Map<String, Object> tempMap = new HashMap<String, Object>();
			for (Element element : elements) {
				tempMap.put(element.getName(), null);
			}
			Set<String> keySet = tempMap.keySet();
			for (String string : keySet) {
				Namespace namespace = elements.get(0).getNamespace();
				List<Element> elements2 = ele.elements(new QName(string,
						namespace));
				// 如果同名的数目大于1则表示要构建list
				if (elements2.size() > 1) {
					List<Map> list = new ArrayList<Map>();
					for (Element element : elements2) {
						Map<String, Object> tempMap1 = new HashMap<String, Object>();
						eleTomap(tempMap1, element);
						list.add(tempMap1);
					}
					map.put(string, list);
				} else {
					// 同名的数量不大于1则直接递归去
					Map<String, Object> tempMap1 = new HashMap<String, Object>();
					eleTomap(tempMap1, elements2.get(0));
					map.put(string, tempMap1);
				}
			}
		}
	}
	//将实体bean转换为Map类型
	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>(0);
		if(obj !=null){
			try {
				PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
				PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
				for (int i = 0; i < descriptors.length; i++) {
					String name = descriptors[i].getName();
					if (!"class".equals(name)) {
						params.put(name.toLowerCase(), propertyUtilsBean.getNestedProperty(obj, name));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return params;
	}	
	
	/** 
     * 提供精确的加法运算。 
     *  liangq 2017年3月2日
     * @param v1 
     *            被加数 
     * @param v2 
     *            加数 
     * @return 两个参数的和 
     */  
  
    public static double addDouble(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.add(b2).doubleValue();  
    }  
    
    /**
     * 
     * @Description: 将数组转化为list
     * @Title: Arrary2List
     * @Author: zhaomengfan
     * @Date: 2017年3月15日上午9:44:39
     * @param arrary
     * @return
     * @return List<T>
     */
    public static <T> List<T> Arrary2List(T[] arrary){
    	if(arrary!=null&&arrary.length>0){
    		List<T> list = new ArrayList<T>(Arrays.asList(arrary));
    		return list;
    	}else{
    		return new ArrayList<T>();
    	}
    	
    }
    /**
     * 
     * 格式化数字，三位一逗号
     * @param str
     * @return
     */
    public static String formatNum(String str){
    	String newStr = "";
    	int count = 0;
    	if(str.indexOf(".")==-1){
    	   for(int i=str.length()-1;i>=0;i--){
    		   if(count % 3 == 0 && count != 0){
    			   newStr = str.charAt(i) + "," + newStr;
    		   }else{
    			   newStr = str.charAt(i) + newStr;
    		   }
    		   count++;
    	   	}
    	   	str = newStr + ".00"; //自动补小数点后两位
    	  
    	}
    	else{
    	   for(int i = str.indexOf(".")-1;i>=0;i--){
    		   	if(count % 3 == 0 && count != 0){
    		   		newStr = str.charAt(i) + "," + newStr;
    		   	}else{
    		   		newStr = str.charAt(i) + newStr; //逐个字符相接起来
    		   	}
    		   	count++;
    	   	}
    	   	str = newStr + (str + "00").substring((str + "00").indexOf("."),3);
    	}
		return newStr;
    }
    /**
     * 格式化证件 四位一空格
     * @param str
     * @return
     */
    public static String formatZJ(String str){
    	String newStr = "";
    	int count = 0;
    	for(int i=0;i<str.length();i++){
    		 if(count % 4 == 0 && count != 0){
    			 newStr =newStr+ " " +str.charAt(i)  ;
    		 }else{
    			 newStr =  newStr+str.charAt(i) ;
    		 }
    		 count++;
    	 }
		return newStr;
    }
    /**
     * 
     * 格式化数字并保留两位小数
     * 
     */
    public static String formatMJ(String str){
    	Double f = Double.parseDouble(str); 
    	DecimalFormat df2  = new DecimalFormat("###.00");
    	String s = df2.format(f);
		return s; 
    }
    /**
     * 
     * 获得15位的身份证
     * 
     */
    public static String get15Ic(String identityCard) {  
        String retId = "";  
        if(null == identityCard){  
            return retId;  
        }  
        if(identityCard.length() == 18){  
            retId = identityCard.substring(0, 6) + identityCard.substring(8, 17);  
        }else{  
            return identityCard;  
        }  
        return retId;  
    }  
    /**
     * 
     * 获得18位的身份证
     * 
     */
    public static String get18Ic(String identityCard) {  
        String retId = "";  
        String id17 = "";  
        int sum = 0;  
        int y = 0;  
        // 定义数组存放加权因子（weight factor）  
        int[] wf = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };  
        // 定义数组存放校验码（check code）  
        String[] cc = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };  
        if (identityCard.length() != 15) {  
            return identityCard;  
        }  
        // 加上两位年19  
        id17 = identityCard.substring(0, 6) + "19" + identityCard.substring(6);  
        // 十七位数字本体码加权求和  
        for (int i = 0; i < 17; i++) {  
            sum = sum + Integer.valueOf(id17.substring(i, i + 1)) * wf[i];  
        }  
        // 计算模  
        y = sum % 11;  
        // 通过模得到对应的校验码 cc[y]  
        retId = id17 + cc[y];  
        return retId;  
    }  

    /**
     * 格式化超长字符串（处理通过分隔符组合起来的字符串，如：1,2,3）
     * @param str 要处理的字符串
     * @param count 格式化后要显示的长度
     * @param separators 分隔符数组
     * @param newseparator 格式化后组合新字符串的分隔符
     * @return
     */
    public static String formatOverlengthString(String str,int count,char[] separators,char newseparator)
    {
		count = count < 1 ? 3 : count;
		separators = separators == null || separators.length == 0 ? new char[] { ',' } : separators;
		String newStr = str;
		if (newStr != null)
		{
			for (char st : separators)
			{
				newStr = newStr.replace(st, ',');
			}
			String[] arrs = newStr.split(",");
			if (arrs.length > count)
			{
				String[] arrStr = new String[count];
				for (int i = 0; i < count; i++)
				{
					arrStr[i] = arrs[i];
				}
				newStr = StringUtils.join(arrStr, newseparator);
				newStr += "等" + arrs.length + "个";
			}
			else
			{
				newStr = str;
			}
		}
		return newStr;
    }
    
    /**
     * 默认格式化超长字符串
     * @param str
     * @return
     */
	public static String formatOverlengthString(String str)
	{
		return formatOverlengthString(str, 3, new char[] { '，', ',','、'}, ',');
	}
	
	/**
	 * 按指定显示个数格式化超长字符串
	 * @param str
	 * @param count 要显示的个数
	 * @return
	 */
	public static String formatOverlengthString(String str,int count)
	{
		return formatOverlengthString(str, count, new char[] { '，', ',','、'}, ',');
	}

}
