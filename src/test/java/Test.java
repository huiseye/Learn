
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import it.sauronsoftware.base64.*;
public class Test {
	public void fun() throws IOException{
		String conUrl="http://localhost:8080/datashare/app/search/tccx"; 
		URL postUrl = new URL(conUrl);
		        // 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setDoOutput(true);
		        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod("POST"); 
        // Post 请求不能使用缓存
        connection.setUseCaches(false); 
        connection.setInstanceFollowRedirects(true); 
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
	    connection.connect();
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		        // The URL-encoded contend
		        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
				
		String data ="<?xml version="+"\"1.0\""+" encoding="+"\"UTF-8\""+"?>";
			   data+="<Message>";
		       data+="<Head>";
			   data+="<CREATETIME name="+"\"生成时间\""+">2016/06/02 09:11:24</CREATETIME>";
			   data+="<DIGITALSIGN name="+"\"数字签名\""+"></DIGITALSIGN>";
			   data+="</Head>";
			   data+="<Data>";
			   data+="<BDCCXQQLIST name="+"\"不动产查询请求列表\""+">";
			   data+="<BDCCXQQ>";
			   data+="<BDHM name="+"\"查询请求单号\""+">20111128320000100002</BDHM>";
			   data+="<XZ name="+"\"性质\""+">1</XZ>";
			   data+="<XM name="+"\"被查询人姓名\""+">刘汉泉</XM>";
			   data+="<GJ name="+"\"国家\""+">中国</GJ>";
			   data+="<LB name="+"\"类别\""+">FC</LB>";
			   data+="<ZJLX name="+"\"证件类型\""+">1</ZJLX>";
			   data+="<DSRZJHM name="+"\"被查询人证件/组织机构号码\""+">360732199306240093</DSRZJHM>";
			   data+="<FZJG name="+"\"发证机关所在地\""+">江苏南京</FZJG>";
			   data+="<FYMC name="+"\"请求查询机构名称\""+">不动产局</FYMC>";
			   data+="<CBR name="+"\"承办人员\""+">admin</CBR>";
			   data+="<AH name="+"\"执行案号\""+">（2011）宁执字第00005号</AH>";
			   data+="<GZZBH name="+"\"承办人员工作证编号\""+">川A12345</GZZBH>";
			   data+="<GWZBH name="+"\"承办人员公务证编号\""+">3701010</GWZBH>";
			   data+="<CKH name="+"\"查询法律文书名称\""+">（2015）房查字第00001号</CKH>";
			   data+="<WSBH name="+"\"文书编号\""+">FCFC000001201408150001</WSBH>";
			   data+="</BDCCXQQ>";
			   data+="</BDCCXQQLIST>";
			   data+="<WSLIST name="+"\"文书列表\""+">";
			   data+="<WSINFO name="+"\"文书信息\""+">";
			   data+="<WSBH name="+"\"文书编号\""+">FCFC000001201408150001</WSBH>";
			   data+="<XH name="+"\"序号\""+">1</XH>";
			   data+="<WJMC name="+"\"文件名称\""+">协助查询通知书</WJMC>";
			   data+="<WJLX name="+"\"文件类型\""+">pdf</WJLX>";
			   data+="<DJR name="+"\"登记人\""+">1212</DJR>";
			   data+="<DJRQ name="+"\"登记日期\""+">2011/09/03 09:11:24</DJRQ>";
			   data+="<WSNR name="+"\"文书内容\""+"></WSNR>";
			   data+="</WSINFO>";
			   data+="</WSLIST>";
			   data+="<ZJLIST name="+"\"证件列表\""+">";
			   data+="<ZJINFO name="+"\"证件信息\""+">";
			   data+="<BDHM name="+"\"查询请求单号\""+">20120323330311100003</BDHM>";
			   data+="<XH name="+"\"序号\""+">1</XH>";
			   data+="<GZZBM name="+"\"工作证编号\""+">1</GZZBM>";
			   data+="<GZZWJGS name="+"\"工作证格式\""+">jpg</GZZWJGS>";
			   data+="<GZZ name="+"\"工作证\""+">1</GZZ>";
			   data+="<GWZBM name="+"\"公务证编号\""+">1</GWZBM>";
			   data+="<GWZWJGS name="+"\"公务证格式\""+">jpg</GWZWJGS>";
			   data+="<GWZ name="+"\"公务证\""+">1</GWZ>";
			   data+="</ZJINFO>";
			   data+="</ZJLIST>";
		       data+="</Data>";
	           data+="</Message>";
		data=URLEncoder.encode(URLEncoder.encode((data).toString(), "utf-8"));
		data=Base64.encode(data);
		out.writeBytes("data="+data+"&username=admin&password=1");
		out.flush();
		out.close(); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}
	public static void main(String[] args) {
		Test t = new Test();
		try {
			t.fun();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
