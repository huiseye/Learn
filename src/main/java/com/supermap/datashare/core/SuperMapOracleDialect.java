package com.supermap.datashare.core;

import java.sql.Types;

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.dialect.Oracle10gDialect;

/**
 * 
 * @Description:自定义oracle数据库方言，加入类型映射
 * @author 刘树峰
 * @date 2015年6月12日 上午11:56:47
 * @Copyright SuperMap
 */
public class SuperMapOracleDialect extends Oracle10gDialect {
	public SuperMapOracleDialect() {

		super();
		registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.NVARCHAR,
				StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.LONGNVARCHAR,
				StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.DECIMAL,
				StandardBasicTypes.DOUBLE.getName());
	}
}
