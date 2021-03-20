package com.supermap.datashare.dao;

import java.util.HashMap;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.supermap.datashare.util.Page;


/**
 * 
 * @author 刘树峰
 *
 */
@Repository("baseCommonDao")
public class CommonDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	public Session OpenSession() {
		return this.sessionFactory.openSession();
	}

	/**
	 * 根据hql条件语句(where后面的部分)查询数据，不分页，非参数化，慎用
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:11:35
	 * @param t
	 * @param hqlCondition
	 *            hql条件语句
	 * @return
	 */
	public <T> List<T> getDataList(Class<T> t, String hqlCondition) {
		String className = t.getName();
		String str = " from " + className + " where " + hqlCondition;
		System.out.println(str);
		@SuppressWarnings("unchecked")
		List<T> contList = this.getCurrentSession().createQuery(str).setCacheable(true).list();

		return contList;
	}

	/**
	 * 根据hql条件语句(where后面的部分)查询数据，不分页，参数化查询
	 * @Title: getDataList
	 * @author:liushufeng
	 * @date：2016年1月14日 上午12:13:44
	 * @param t
	 *            实体类
	 * @param hqlCondition
	 *            查询条件
	 * @param map
	 *            参数map
	 * @return
	 */
	public <T> List<T> getDataList(Class<T> t, String hqlCondition, Map<String, String> map) {
		String className = t.getName();
		String str = " from " + className + " where " + hqlCondition;
		System.out.println(str);
		Query query = this.getCurrentSession().createQuery(str).setCacheable(true);
		query.setProperties(map);
		@SuppressWarnings("unchecked")
		List<T> contList = query.list();
		return contList;
	}

	/**
	 * 更新实体
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:11:27
	 * @param t
	 */
	public <T> void update(T t) {
		this.getCurrentSession().merge(t);
	}

	/**
	 * 根据ID查询实体
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:11:06
	 * @param entityClass
	 *            实体类
	 * @param id
	 *            主键值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> entityClass, String id) {
		return (T) this.getCurrentSession().get(entityClass, id);
	}

	/**
	 * 保存实体
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:12:51
	 * @param t
	 * @return
	 */
	public <T> String save(T t) {
		this.getCurrentSession().save(t);
		return null;
	}

	/**
	 * 保存或更新一个实体，调用的是hibernate的saveOrUpdate方法
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:10:38
	 * @param t
	 */
	public <T> void saveOrUpdate(T t) {
		this.getCurrentSession().saveOrUpdate(t);
	}

	/**
	 * 删除一个实体
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:10:12
	 * @param t
	 *            泛型类实体对象
	 */
	public <T> void deleteEntity(T t) {
		getCurrentSession().delete(t);
	}

	/**
	 * 根据主键删除一个实体
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @param id
	 *            String 实体内码
	 * @return
	 */
	public <T> void delete(Class<T> t, String id) {
		T entity = this.get(t, id);
		if (entity != null) {
			this.getCurrentSession().delete(entity);
		}
	}

	/**
	 * 根据hql条件删除实体，非参数化
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:08:12
	 * @param t
	 *            泛型类
	 * @param condition
	 *            hql条件语句
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> int deleteEntitysByHql(Class<T> t, String condition) {
		Session session = this.getCurrentSession();
		String className = t.getName();
		Query qry = session.createQuery(" from  " + className + " where " + condition);
		List<T> list = qry.list();
		for (int i = 0; i < list.size(); i++) {
			this.deleteEntity(list.get(i));
		}
		int count = list.size();
		return count;
	}

	/**
	 * 根据HQL条件语句(where后面的部分)分页查询数据，不用参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:05:00
	 * @param t
	 *            泛型类
	 * @param condition
	 *            where后面的条件语句
	 * @param pageIndex
	 *            当前页数
	 * @param pageSize
	 *            每页记录数
	 * @return
	 */
	public <T> Page getPageDataByHql(Class<T> t, String hqlCondition, int pageIndex, int pageSize) {
		String className = t.getName();
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		String str = " from " + className + " where ";
		String fullHQLQuery = str + hqlCondition;
		Query queryCount = this.getCurrentSession().createQuery("SELECT COUNT(1) " + fullHQLQuery);
		Long count = Long.parseLong(queryCount.uniqueResult().toString());
		if (count < 1)
			return new Page();
		Query query = this.getCurrentSession().createQuery(fullHQLQuery);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return new Page(Page.getStartOfPage(pageIndex, pageSize), count, pageSize, query.list());
	}

	/**
	 * 根据hql条件语句分页查询数据
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午9:49:34
	 * @param t
	 *            泛型类
	 * @param hqlCondition
	 *            hql语句中where后面的部分，应写成参数化的格式,例如等于查询：ZL=:ZLPARAM,其中ZLPARAM是参数，
	 *            应在map中存在键为ZLPARAM的值
	 * @param map
	 *            参数键值对，每个出现在hqlCondition中的参数都必须能在map中找到相应的键值对
	 * @param pageIndex
	 *            当前页数
	 * @param pageSize
	 *            每页记录数
	 * @return
	 */
	public <T> Page getPageDataByHql(Class<T> t, String hqlCondition, Map<String, String> map, int pageIndex, int pageSize) {
		String className = t.getName();
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		String str = " from " + className + " where ";
		String fullHQLQuery = str + hqlCondition;
		Query queryCount = this.getCurrentSession().createQuery("SELECT COUNT(1) " + fullHQLQuery);
		queryCount.setProperties(map);
		Long count = Long.parseLong(queryCount.uniqueResult().toString());
		if (count < 1)
			return new Page();
		Query query = this.getCurrentSession().createQuery(fullHQLQuery);
		query.setProperties(map);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return new Page(Page.getStartOfPage(pageIndex, pageSize), count, pageSize, query.list());
	}

	/**
	 * 根据完整的sql语句分页查询数据
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:00:26
	 * @param fulSql
	 *            完整的 sql查询语句，可以跨多个表，应使用参数化查询例如：select * from BDCS_ZS_GZ WHERE
	 *            ZSBH LIKE:ZSBHPARAM
	 * @param paramMap
	 *            参数名-参数值 映射
	 * @param pageIndex
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return 返回值是键值对List，每个map对应一行记录，获取某列数据使用 map.get("列名");
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getPageDataByFullSql(String fulSql, Map<String, String> paramMap, int pageIndex, int pageSize) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setProperties(paramMap);
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		pageSize = pageSize < 0 ? 10 : pageSize;
		query.setMaxResults(pageSize);
		// query.setFirstResult(firstResult).setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}

	/**
	 * 根据完整的sql语句分页查询数据
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日上午10:00:26
	 * @param fulSql
	 *            完整的 sql查询语句，可以跨多个表，非参数化查询，应慎用，防止SQL注入
	 * @param paramMap
	 *            参数名-参数值 映射
	 * @param pageIndex
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return 返回值是键值对List，每个map对应一行记录，获取某列数据使用 map.get("列名");
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getPageDataByFullSql(String fulSql, int pageIndex, int pageSize) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		pageSize = pageSize < 0 ? 10 : pageSize;
		query.setMaxResults(pageSize);
		// query.setFirstResult(firstResult).setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}

	/**
	 * 查询总数count，不能传完整的sql语句，要从from开始写例如： from A LEFT JOIN B ON A.xx=B.xxx WHERE
	 * 
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日下午9:33:51
	 * @param fromSql
	 * @return
	 */
	public long getCountByFullSql(String fromSql) {
		String fullSql = "select count(1) " + fromSql;
		System.out.println("=========================");

		System.out.println(fullSql);
		System.out.println("=========================");
		SQLQuery query = this.getCurrentSession().createSQLQuery(fullSql);
		String strCount = query.uniqueResult().toString();
		Long longcount = Long.valueOf(strCount);
		return (long) longcount;
	}

	/**
	 * 查询总数count，参数化不能传完整的sql语句，要从from开始写例如： from A LEFT JOIN B ON A.XX=B.xxx
	 * WHERE XXXXX XXXXXXX
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日下午9:33:51
	 * @param fromSql
	 * @return
	 */
	public long getCountByFullSql(String fromSql, Map<String, String> map) {
		String fullSql = "select count(1) " + fromSql;

		SQLQuery query = this.getCurrentSession().createSQLQuery(fullSql);
		query.setProperties(map);
		String strCount = query.uniqueResult().toString();
		Long longcount = Long.valueOf(strCount);
		return (long) longcount;
	}

	/**
	 * 根据完整SQL语句一次新查询所有数据，参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午5:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSql(String fulSql, Map<String, String> paramMap) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setProperties(paramMap);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}
	/**
	 * 根据完整SQL语句一次新查询所有数据，参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午5:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSql(String fulSql, String value,String key, String value1,String key1) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setString(key, value);
		query.setString(key1, value1);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}
	/**
	 * 根据完整SQL语句一次新查询所有数据，参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午5:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSql(String fulSql, String value,String key) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setString(key, value);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}

	/**
	 * 根据完整SQL语句一次新查询所有数据，非参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午5:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSql(String fulSql) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}

	public void flush() {
		this.getCurrentSession().flush();
	}
	
	/**
	 * 根据完整sql删除
	 * @param delsql
	 * @author diaoliwei
	 * @return
	 */
	public int updateBySql(String delsql){
		int result = 0;
		result = this.getCurrentSession().createSQLQuery(delsql).executeUpdate();
		this.getCurrentSession().flush();
		this.getCurrentSession().clear();
		return result;
	}

	public  ResultSet excuteQuery(String sql)
			throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 创建该连接下的PreparedStatement对象
		pstmt = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection().prepareStatement(sql);
		// 传递第一个参数值 root，代替第一个问号
		//pstmt.setString(1, "root");
		// 执行查询语句，将数据保存到ResultSet对象中
		rs = pstmt.executeQuery();
		// if(rs != null){
		// rs.close();
		// }
//		if (pstmt != null) {
//			pstmt.close();
//		}

		return rs;
	}
	
	
	/**
	 * 查询总数count，参数化不能传完整的sql语句，要从from开始写例如： from A LEFT JOIN B ON A.XX=B.xxx
	 * WHERE XXXXX XXXXXXX
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月5日 8:49:51
	 * @param fromSql
	 * @return
	 */
	public long getCountByFullSqlEx(String fromSql, Map<String,Object> map) {
		String fullSql = "select count(1) " + fromSql;
		SQLQuery query = this.getCurrentSession().createSQLQuery(fullSql);
		query.setProperties(map);
		String strCount = query.uniqueResult().toString();
		Long longcount = Long.valueOf(strCount);
		return (long) longcount;
	}
	
	/**
	 * 根据完整的sql语句分页查询数据
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月5日 8:49:51
	 * @param fulSql
	 *            完整的 sql查询语句，可以跨多个表，应使用参数化查询例如：select * from BDCS_ZS_GZ WHERE
	 *            ZSBH LIKE:ZSBHPARAM
	 * @param paramMap
	 *            参数名-参数值 映射
	 * @param pageIndex
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return 返回值是键值对List，每个map对应一行记录，获取某列数据使用 map.get("列名");
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getPageDataByFullSqlEx(String fulSql, Map<String, Object> paramMap, int pageIndex, int pageSize) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setProperties(paramMap);
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		pageSize = pageSize < 0 ? 10 : pageSize;
		query.setMaxResults(pageSize);
		// query.setFirstResult(firstResult).setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}
	
	/**
	 * 根据完整SQL语句一次新查询所有数据，参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2016年5月5日 8:49:51
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSqlEx(String fulSql, Map<String, Object> paramMap) {
	    Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    query.setProperties(paramMap);
	    @SuppressWarnings("unchecked")
	    List<Map> list = query.list();
	    return list;
	}
	
	
	/**
	 * 根据SQL语句查询到的结果，返回指定的类，查询的结果字段必须包含返回类的字段，
	 * 如果查询结果的字段和返回类的字段不存在，那返回的类字段值为空；
	 * @作者 凌广清
	 * @创建时间 2016年12月28日 
	 * @param t 返回值：类
	 * @param hqlCondition 查询的SQL语句
	 * @return t的类集合
	 */
	public <T> List<T> getDataListBySQL(Class<T> t, String hqlCondition) {
		Query query = this.getCurrentSession().createSQLQuery(hqlCondition).addEntity("c", t);
		@SuppressWarnings("unchecked")
		List<T> contList =query.list();
		return contList;
	}
	
	/**
	 * 根据SQL语句查询到的结果，返回指定的类，查询的结果字段必须包含返回类的字段，
	 * 如果查询结果的字段和返回类的字段不存在，那返回的类字段值为空；参数化
	 * @作者 凌广清
	 * @创建时间 2016年12月28日 
	 * @param t 返回值：类
	 * @param hqlCondition 查询的SQL语句
	 * @return t的类集合
	 */
	public <T> List<T> getDataListBySQLEx(Class<T> t, String hqlCondition, Map<String, Object> paramMap) {
	    Query query = this.getCurrentSession().createSQLQuery(hqlCondition).addEntity("c", t);
	    query.setProperties(paramMap);
	    @SuppressWarnings("unchecked")
	    List<T> contList =query.list();
	    return contList;
	}
	
	/**
	 * 根据sql直接对数据进行删除
	 * @param sql
	 */
	public void delete(String sql){
		this.getCurrentSession().createQuery(sql).executeUpdate();
	}
	
	/**
	 * 根据完整SQL语句一次新查询所有数据，非参数化查询，分页
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月15日下午1:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getDataListByFullSql(String fulSql, int pageIndex, int pageSize) {
		int firstResult = (pageIndex - 1) * pageSize;
		/*List<Map> list = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() < 1)
			return list;*/
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return query.list();
	}
}
