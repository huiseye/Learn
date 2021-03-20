package com.supermap.datashare.core;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;


public class SuperHelper {

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T GeneratePrimaryKey() {
		return (T) UUID.randomUUID().toString().replaceAll("-", "");
	}

	// 获取@Entity注解的Name
	public static <T> String getEntityName(Class<T> clazz) {
		return clazz.getAnnotation(Entity.class).name();
	}
	// 直接执行SQL，慎用！
	public static int excuteBySql(Session session, String sql,
			Map<String, Object> parameterCondition) {
		int result;
		SQLQuery query = session.createSQLQuery(sql);
		for (Map.Entry<String, Object> entry : parameterCondition.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		result = query.executeUpdate();
		return result;
	}

	public static String GetTableName(
			@SuppressWarnings("rawtypes") Class classtype) {
		Annotation[] anno = classtype.getAnnotations();
		String tableName = "";
		for (int i = 0; i < anno.length; i++) {
			if (anno[i] instanceof Table) {
				Table table = (Table) anno[i];
				tableName = table.name();
				break;
			}
		}
		return tableName;
	}

	public static <T> T getModelClass(Model springModel,
			String modelAttributeName) {
		ModelMap map = (ModelMap) springModel.asMap();
		if (springModel.containsAttribute(modelAttributeName)) {
			@SuppressWarnings("unchecked")
			T t = (T) map.get(modelAttributeName);
			return t;
		}
		return null;
	}

}
