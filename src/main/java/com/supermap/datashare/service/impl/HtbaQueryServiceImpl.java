package com.supermap.datashare.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.supermap.datashare.dao.CommonDaoJY;
import com.supermap.datashare.service.HtbaQueryService;
import com.supermap.datashare.util.GetProperties;
import com.supermap.datashare.util.StringHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("htbaqueryservice")
public class HtbaQueryServiceImpl implements HtbaQueryService {
	@Autowired
	private CommonDaoJY baseCommonDao;
	
	String ORCLNAME=GetProperties.getConstValueByKey("ORCLNAME");

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject searchHtBeiAn(String pStr) {
		
		JSONObject resultMessage=new JSONObject();
		JSONArray jsonArr = JSONArray.fromObject( pStr );
		Iterator<Object> it = jsonArr.iterator();
        String bdcdydids ="";
        String ownerName = "";
        String ownerZjh = "";
        while (it.hasNext()){
            JSONObject obj = (JSONObject) it.next();
            ownerName += ","+obj.get("ownername");
            ownerZjh +=","+obj.get("ownercode");
        }
        ownerName =ownerName.substring(1);
        ownerZjh =ownerZjh.substring(1);
        String[] strqlrs = ownerName.split(",");
        String[] strzjhs=ownerZjh.split(",");
        StringBuilder qlr_sql=new StringBuilder();
        StringBuilder sb_head=new StringBuilder();
        StringBuilder sb_from=new StringBuilder();
        StringBuilder sb=new StringBuilder();
        //将权利人信息 拼接为查询条件
        for(int i=0;i<strqlrs.length;i++){
        	qlr_sql.append(" OR"+ " (r.xm  like '%"+strqlrs[i]+"%' AND" + " UPPER(r.zjhm) like UPPER('%"+strzjhs[i]+"%'))");
        }
        String qlr_tjsql=qlr_sql.toString().substring(3);
         
        //所需查询所有字段
         sb_head.append(" select h.fwbm,ht.htbh as HTH ,to_char(ht.htqdrq,'yyyy-mm-dd') as QDSJ,to_char(ht.htqrrq,'yyyy-mm-dd') as BASJ,ht.qxh as AREACODE,ht.zt AS HTZT,x.xmmc as PROJECTNAME,h.fh as HH,h.zl as ZL,NVL2(h.scjzmj,h.scjzmj,h.ycjzmj) AS SCJZMJ,h.bdcdyh as BDCDYH,h.fwyt_s as FWYT, z.zrzmc as ZRZH ,q1.qlrmc AS QLRMC ,q1.qlrzjh AS QLRZJH ");
        //所需查询所有表信息
       
          sb_from.append(" FROM "+ORCLNAME+".t_lp_h h ")
			        	.append(" LEFT JOIN "+ORCLNAME+".t_lp_zrz z ON h.zrzid=z.zrzid  ")
			        	.append(" LEFT JOIN (")
			        	//先通过证件号和人名条件查出所有符合条件的fwbm，再将同一fwbm下的权利人信息进行拼接。去重
			        	.append(" select q.fwbm,q.ywid, LISTAGG( to_char(q.xm), ',') WITHIN GROUP(ORDER BY q.xm) AS qlrmc,LISTAGG( to_char(q.zjhm), ',') WITHIN GROUP(ORDER BY q.xm) AS qlrzjh from  "+ORCLNAME+".t_qs_qlr q  ")
			        	.append(" INNER JOIN (")
			        	.append(" select distinct r.fwbm ,b.htid from "+ORCLNAME+".t_qs_qlr  r  LEFT join  "+ORCLNAME+".t_qs_jyht b on b.htid=r.ywid  where  b.htbh is not null AND b.lx <>'2' AND ( ")
			        	.append(qlr_tjsql)
			        	.append(" ) ) r1 on q.fwbm=r1.fwbm and q.ywid = r1.htid group by q.fwbm,q.ywid ")
			        	.append(" ) q1 ON h.fwbm=q1.fwbm ")
			        	.append(" LEFT JOIN  "+ORCLNAME+".t_qs_jyht ht ON ht.htid=q1.ywid ")
			        	.append(" LEFT JOIN "+ORCLNAME+".t_lp_xmxx x ON h.xmid=x.xmid ")
			        	.append(" where  ht.htbh is not null ");
//          long count =baseCommonDao.getCountByFullSql(sb_from.toString()); 
//          resultMessage.put("total",count);
          sb=sb_head.append(sb_from);
          List<Map> htbalist=baseCommonDao.getDataListByFullSql(sb.toString());
          List<Map> houseMaps =new ArrayList<Map>();
        try{
            if(htbalist!=null && htbalist.size()>0) {
            	for(Map htba:htbalist) {
    				Map houseInfo=new HashMap();
            		houseInfo.put("hth",StringHelper.isEmpty(htba.get("HTH"))?"" : htba.get("HTH").toString() );
            		houseInfo.put("qdsj",StringHelper.isEmpty(htba.get("QDSJ"))? "" : htba.get("QDSJ").toString());
            		houseInfo.put("basj",StringHelper.isEmpty(htba.get("BASJ"))?"" : htba.get("BASJ").toString());
            		houseInfo.put("projectName",StringHelper.isEmpty(htba.get("PROJECTNAME"))? "" : htba.get("PROJECTNAME").toString());
            		houseInfo.put("areaCode",StringHelper.isEmpty(htba.get("AREACODE"))?"" : htba.get("AREACODE").toString());
            		houseInfo.put("hh",StringHelper.isEmpty(htba.get("HH"))?"" : htba.get("HH").toString());
            		houseInfo.put("zrzh",StringHelper.isEmpty(htba.get("ZRZH"))?"" : htba.get("ZRZH").toString());
            		houseInfo.put("scjzmj",StringHelper.isEmpty(htba.get("SCJZMJ"))?"" : htba.get("SCJZMJ").toString());
            		houseInfo.put("bdcdyh",StringHelper.isEmpty(htba.get("BDCDYH"))?"" : htba.get("BDCDYH").toString());
            		houseInfo.put("qlrmc",StringHelper.isEmpty(htba.get("QLRMC"))?"" : htba.get("QLRMC").toString());
            		houseInfo.put("qlrzjh",StringHelper.isEmpty(htba.get("QLRZJH"))?"" : htba.get("QLRZJH").toString());
            		houseInfo.put("fwyt",StringHelper.isEmpty(htba.get("FWYT"))?"" : htba.get("FWYT").toString());
            		houseInfo.put("htzt",StringHelper.isEmpty(htba.get("HTZT"))?"" : htba.get("HTZT").toString());
            		houseMaps.add(houseInfo);
            	}

            }
            resultMessage.put("total",htbalist.size());
            resultMessage.put("list",houseMaps);
            resultMessage.put("success","success");
        }catch (Exception e) {
        	resultMessage.put("success","false");
        	e.printStackTrace();
		}

		return resultMessage;
	}

	@Override
	public JSONObject getFwInfoForFGHB(String realtionid) {
		//code 状态 0成功 -1失败
		String code="-1";
		String success="false";
		String msg="";
		JSONObject result=new JSONObject();
		JSONArray fwinfos=new JSONArray();
		// 查询字段
		String  cxzd ="SELECT  H.FWBM AS FWBM,H.ZL AS ZL,H.YCTNJZMJ AS YCTNJZMJ,H.YCFTJZMJ AS YCFTJZMJ,H.YCJZMJ AS YCJZMJ,H.YCFTXS AS YCFTXS,H.SCTNJZMJ AS SCTNJZMJ,H.SCFTJZMJ AS SCFTJZMJ,H.SCJZMJ AS SCJZMJ,H.SCFTXS AS SCFTXS,H.FWLX AS FWLX,NVL(H.YINROOMID,H.FWBM) AS RELATIONID,H.ZCS AS ZCS,H.FH AS FH,H.HX AS HX,H.SJC AS SZC ,H.FWXZ AS FWXZ ";
		// 查询合并信息，提供合并前任意一个房屋的realtionid，去关联qxfwid
		String hbfromsql =" FROM  T_LP_H H LEFT JOIN T_LP_FWZT ZT ON H.FWID = ZT.QXFWID WHERE ZT.FWID IN ("+
				"  SELECT DISTINCT H.FWID FROM "+ORCLNAME+".T_LP_H H LEFT JOIN "+ORCLNAME+".T_LP_FWZT ZT ON H.FWID = ZT.FWID WHERE ZT.QXFWID IS NOT NULL AND NVL(H.YINROOMID,H.FWBM) = '" +realtionid+"' ) ";
		// 查询分割信息，提供合并前房屋的realtionid，去关联lyfwid
		String fgfromsql =" FROM "+ORCLNAME+".T_LP_H H LEFT JOIN "+ORCLNAME+".T_LP_FWZT ZT ON H.FWID = ZT.FWID WHERE ZT.LYFWID IN ("+
				" SELECT DISTINCT H.FWID FROM "+ORCLNAME+".T_LP_H H LEFT JOIN "+ORCLNAME+".T_LP_FWZT ZT ON H.FWID = ZT.LYFWID WHERE ZT.LYFWID IS NOT NULL AND NVL(H.YINROOMID,H.FWBM) = '"+realtionid+"' ) ";
		//完整查询sql
		StringBuilder cxsqlsql = new StringBuilder();
		//拼接sql 并写入查询类型 0合并 1分割
		try {
			cxsqlsql.append(cxzd).append(" ,'0' AS BGLX ").append(hbfromsql).append( " UNION ALL ").append(cxzd).append(" ,'1' AS BGLX ").append(fgfromsql);
			List<Map> fwinfolist=baseCommonDao.getDataListByFullSql(cxsqlsql.toString());
			if(fwinfolist!=null&&fwinfolist.size()>0) {
				//判断变更类型
				String bglx= StringHelper.formatObject(fwinfolist.get(0).get("BGLX"));
				//合并
				if(bglx.equals("0")) {
					if(fwinfolist.size()>1) {
						msg="读取失败，合并后房屋数量异常";
					}else {
						fwinfos=getFwinfos(fwinfolist);
						code="0";
						success="true";
						msg="读取成功";
					}
				}else {
					if(fwinfolist.size()<=1) {
						msg="读取失败，分割后房屋数量异常";
					}else {
						fwinfos=getFwinfos(fwinfolist);
						code="0";
						success="true";
						msg="读取成功";
					}
				}
			}else {
				msg = "未读取到变更信息";			
			}
		} catch (Exception e) {
			msg ="读取失败，内部错误:"+e;
		}
		result.put("code", code);
		result.put("success", success);
		result.put("msg", msg);
		result.put("fwinfos", fwinfos);
		return result;
	}
	
	private JSONArray getFwinfos(List<Map> fwinfolist) {
		JSONArray fwinfos=new JSONArray();
		if(fwinfolist!=null&&fwinfolist.size()>0) {
		for(Map fwinfomap:fwinfolist) {
			JSONObject fwinfo=new JSONObject();					
			fwinfo.put("FWBM", StringHelper.formatObject(fwinfomap.get("FWBM")));
			fwinfo.put("ZL", StringHelper.formatObject(fwinfomap.get("ZL")));
			fwinfo.put("YCTNJZMJ", StringHelper.formatObject(fwinfomap.get("YCTNJZMJ")));
			fwinfo.put("YCFTJZMJ", StringHelper.formatObject(fwinfomap.get("YCFTJZMJ")));
			fwinfo.put("YCJZMJ", StringHelper.formatObject(fwinfomap.get("YCJZMJ")));
			fwinfo.put("YCFTXS", StringHelper.formatObject(fwinfomap.get("YCFTXS")));
			fwinfo.put("SCTNJZMJ", StringHelper.formatObject(fwinfomap.get("SCTNJZMJ")));
			fwinfo.put("SCFTJZMJ", StringHelper.formatObject(fwinfomap.get("SCFTJZMJ")));
			fwinfo.put("SCJZMJ", StringHelper.formatObject(fwinfomap.get("SCJZMJ")));
			fwinfo.put("SCFTXS", StringHelper.formatObject(fwinfomap.get("SCFTXS")));
			fwinfo.put("FWLX", StringHelper.formatObject(fwinfomap.get("FWLX")));
			fwinfo.put("RELATIONID", StringHelper.formatObject(fwinfomap.get("RELATIONID")));
			fwinfo.put("ZCS", StringHelper.formatObject(fwinfomap.get("ZCS")));
			fwinfo.put("FH", StringHelper.formatObject(fwinfomap.get("FH")));
			fwinfo.put("HX", StringHelper.formatObject(fwinfomap.get("HX")));
			fwinfo.put("SZC", StringHelper.formatObject(fwinfomap.get("SZC")));
			fwinfo.put("HXJG", StringHelper.formatObject(fwinfomap.get("HXJG")));
			fwinfo.put("FWXZ", StringHelper.formatObject(fwinfomap.get("FWXZ")));
			fwinfos.add(fwinfo);
		}
		}
		return fwinfos;
	}
	
	@Override
	public com.alibaba.fastjson.JSONArray getFcinfoLlist(String hth) {
		StringBuilder cxsql = new StringBuilder();
		cxsql.append("SELECT  *  FROM ( ")
				.append(" SELECT to_char(qlr.xm) AS QLRMC,qlr.zjlx AS ZJZL, qlr.zjhm AS ZJH,qlr.lxdh AS DH,qlr.lx AS QLRLX,'1' AS SQRLB,ba.fwjg AS FDCJYJG,ba.htbh AS CASENUM,h.yinroomid AS RELATIONID from "+ORCLNAME+".t_qs_qlr qlr inner join "+ORCLNAME+".t_qs_jyba ba on qlr.ywid = ba.ywid inner join "+ORCLNAME+".t_lp_h h on ba.fwbm = h.fwbm  where ba.bgzt<>'2' ")
				.append(" UNION ALL ")
				.append(" SELECT to_char(ywr.xm) AS QLRMC,ywr.zjlx AS ZJZL,ywr.zjhm AS ZJH,ywr.lxdh AS DH,ywr.lx AS QLRLX,'2' AS SQRLB,ba.fwjg AS FDCJYJG,ba.htbh AS CASENUM,h.yinroomid AS RELATIONID from "+ORCLNAME+".t_qs_ywr ywr inner join "+ORCLNAME+".t_qs_jyba ba on ywr.ywid = ba.ywid inner join "+ORCLNAME+".t_lp_h h on ba.fwbm = h.fwbm where ba.bgzt<>'2' ")
				.append("  ) where CASENUM = '"+hth+"' ");
		List<Map> result = baseCommonDao.getDataListByFullSql(cxsql.toString());
		com.alibaba.fastjson.JSONArray realresult= com.alibaba.fastjson.JSONArray.parseArray(com.alibaba.fastjson.JSON.toJSONString(result));
		return realresult;
	}

	@Override
	public JSONObject getzrz(HttpServletRequest request) {
		JSONObject resultmap = new JSONObject();
		resultmap.put("msg", "");
		com.alibaba.fastjson.JSONArray data = new com.alibaba.fastjson.JSONArray();
		BufferedReader br = null;
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
			if((str=br.readLine())!=null){
				sb.append(str);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}finally{
			try{
				br.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		try {
			com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(sb.toString());
			String zrzh=StringHelper.formatObject(json.get("ZRZH"));
			String xmmc=StringHelper.formatObject(json.get("XMMC"));
			String hth=StringHelper.formatObject(json.get("HTH"));
			String zl=StringHelper.formatObject(json.get("ZL"));
			if(StringHelper.isEmpty(zrzh)&&StringHelper.isEmpty(xmmc)&&StringHelper.isEmpty(hth)&&StringHelper.isEmpty(zl)) {
				resultmap.put("msg", "请传递至少一个查询条件。");
				resultmap.put("result", "false");
				resultmap.put("code", "-1");
				resultmap.put("data", data);
				return resultmap;
			}else {
				StringBuilder cxsql = new StringBuilder();
				cxsql.append("SELECT ZRZ.* ,XM.XMMC FROM "+ORCLNAME+".T_LP_ZRZ ZRZ LEFT JOIN "+ORCLNAME+".T_LP_XMXX  XM ON ZRZ.XMID =XM.XMID WHERE 1>0 ");
				if(!StringHelper.isEmpty(zrzh)) {
					cxsql.append("  AND ZRZ.ZRZMC LIKE '%"+zrzh+"%'");
				}
				if(!StringHelper.isEmpty(zl)) {
					cxsql.append(" AND ZRZ.ZL LIKE '%"+zl+"%'");
				}
				if(!StringHelper.isEmpty(xmmc)) {
					cxsql.append(" AND XM.XMMC LIKE '%"+xmmc+"%'");
				}
				if(!StringHelper.isEmpty(hth)) {
					cxsql.append(" AND XM.XMBM LIKE '%"+hth+"%'");
				}
				
				List<Map> zrzinfos = baseCommonDao.getDataListByFullSql(cxsql.toString());
				if(zrzinfos!=null&&zrzinfos.size()>0) {
					data = com.alibaba.fastjson.JSONArray.parseArray(com.alibaba.fastjson.JSONArray.toJSON(zrzinfos).toString());
					resultmap.put("msg", "查询成功。");
					resultmap.put("result", "true");
					resultmap.put("code", "0");
					resultmap.put("data", data);
					return resultmap;
				}else {
					resultmap.put("msg", "未查到相关自然幢信息。");
					resultmap.put("result", "true");
					resultmap.put("code", "0");
					resultmap.put("data", data);
					return resultmap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultmap.put("msg", "查询失败，接口异常。");
			resultmap.put("result", "false");
			resultmap.put("code", "-1");
			resultmap.put("data", data);
			return resultmap;
		}
	}

	@Override
	public JSONObject gethinfos(String zrzbdcdyid) {
		JSONObject resultmap = new JSONObject();
		resultmap.put("msg", "");
		com.alibaba.fastjson.JSONArray data = new com.alibaba.fastjson.JSONArray();
		try {
			String cxsql = "SELECT H.* FROM "+ORCLNAME+".T_LP_H  H LEFT JOIN "+ORCLNAME+".T_LP_FWZT ZT ON ZT.FWID = H.FWID WHERE ZT.QXFWID IS NULL  AND NVL(H.BGZT,'99')<>'2' AND H.ZRZID = '"+zrzbdcdyid+"' ";
			List<Map> hinfos =baseCommonDao.getDataListByFullSql(cxsql);
			if(hinfos!=null&&hinfos.size()>0) {
				data = com.alibaba.fastjson.JSONArray.parseArray(com.alibaba.fastjson.JSONArray.toJSON(hinfos).toString());
			 
				resultmap.put("result", "true");
				resultmap.put("code", "0");
				resultmap.put("data", data);
				return resultmap;
			}else {
				resultmap.put("msg", "未查到相关房屋信息。");
				resultmap.put("result", "true");
				resultmap.put("code", "0");
				resultmap.put("data", data);
				return resultmap;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultmap.put("msg", "查询失败，接口异常。");
			resultmap.put("result", "false");
			resultmap.put("code", "-1");
			resultmap.put("data", data);
			return resultmap;
		}

	}
	
	@Override
	public JSONObject gethinfobyhtorzh(HttpServletRequest request) {
		JSONObject resultmap = new JSONObject();
		resultmap.put("msg", "");
		com.alibaba.fastjson.JSONArray data = new com.alibaba.fastjson.JSONArray();
		String  relationids = "";
		BufferedReader br = null;
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
			if((str=br.readLine())!=null){
				sb.append(str);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}finally{
			try{
				br.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		try {
			com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(sb.toString());
			String hth=StringHelper.formatObject(json.get("HTH"));
			if(StringHelper.isEmpty(hth)) {
				resultmap.put("msg", "查询失败，请至少传递1个条件");
				resultmap.put("result", "flase");
				resultmap.put("code", "-1");
				resultmap.put("data", data);
				resultmap.put("relationids", relationids);
				return resultmap;
			}
			String realhths=changeToInSql(hth);
			StringBuilder cxsql = new StringBuilder();
			cxsql.append(" SELECT to_char(qlr.xm) AS QLRMC,qlr.zjlx AS ZJZL, qlr.zjhm AS ZJH,qlr.lxdh AS DH,qlr.lx AS QLRLX,'1' AS SQRLB,ba.fwjg AS FDCJYJG,ba.htbh AS CASENUM,ba.zsbh as ZSBH,h.yinroomid AS RELATIONID from "+ORCLNAME+".t_qs_qlr qlr inner join "+ORCLNAME+".t_qs_jyba ba on qlr.ywid = ba.ywid inner join "+ORCLNAME+".t_lp_h h on ba.fwbm = h.fwbm  ")
					.append("   where ba.htbh in ("+realhths+") or ba.zsbh in ("+realhths+")  and ba.bgzt<>'2' ");
			List<Map> result = baseCommonDao.getDataListByFullSql(cxsql.toString());
			if(result==null|| result.size()==0) {
				resultmap.put("msg", "未查询到数据，请确认所输入号码是否有误。");
				resultmap.put("result", "flase");
				resultmap.put("code", "-1");
				resultmap.put("data", data);
				resultmap.put("relationids", relationids);
				return resultmap;
			}
			data= com.alibaba.fastjson.JSONArray.parseArray(com.alibaba.fastjson.JSON.toJSONString(result));
			for(int i=0;i<data.size();i++) {
				if(!relationids.contains(StringHelper.formatObject(data.getJSONObject(i).get("RELATIONID")))) {
					relationids+=StringHelper.formatObject(data.getJSONObject(i).get("RELATIONID"))+",";
				}
			}
			resultmap.put("msg", "查询成功。");
			resultmap.put("result", "true");
			resultmap.put("code", "0");
			resultmap.put("data", data);
			resultmap.put("relationids", relationids.subSequence(0, relationids.length()-1));
			return resultmap;
		} catch (Exception e) {
			e.printStackTrace();
			resultmap.put("msg", "查询失败，查询接口异常。");
			resultmap.put("result", "false");
			resultmap.put("code", "-1");
			resultmap.put("data", data);
			resultmap.put("relationids", relationids);
			return resultmap;
		}
		
	} 
	
	public String changeToInSql(String arrString) {
		String[] arr=arrString.replaceAll("，",",").split(",");
		String realhths="";
		if(arr.length==0) {
			return "";
		}
		for (int i=0;i<arr.length;i++) {
			realhths +="'"+arr[i]+"'"+",";
		}
		return realhths.substring(0, realhths.length()-1);
	}
}
