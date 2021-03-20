package com.supermap.datashare.core;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;


public class SuperDataLog {

	/**
	 * 继承Level类
	 * @ClassName: DataOperate
	 * @author liushufeng
	 * @date 2015年11月15日 下午7:10:49
	 */
	private static class DataOperate extends Level {
		/**
		 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
		 */
		private static final long serialVersionUID = 1L;

		public DataOperate(int level, String levelStr, int syslogEquivalent) {
			super(level, levelStr, syslogEquivalent);
		}
	}

	/**
	 * 自定义级别名称，以及级别范围
	 */
	public static final Level DATAOPERATE = new DataOperate(19999, "DATAOPERATE", SyslogAppender.LOG_LOCAL0);

	/**
	 * 使用日志打印logger中的log方法
	 * 
	 * @param logger
	 * @param objLogInfo
	 */
	public static void Log(Logger logger, Object objLogInfo) {
			logger.setLevel(SuperDataLog.DATAOPERATE);
			logger.log(DATAOPERATE, objLogInfo);
	}

}