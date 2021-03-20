/**   
 * TODO:@liushufeng:请描述这个文件
 * @Title: SuperJDBCAppender.java 
 * @Package com.supermap.wisdombusiness.core 
 * @author liushufeng 
 * @date 2015年11月15日 下午5:31:21 
 * @version V1.0   
 */

package com.supermap.datashare.core;

import org.apache.log4j.Priority;
import org.apache.log4j.jdbc.JDBCAppender;
import org.springframework.stereotype.Component;

/**
 * TODO:@liushufeng:请描述这个类或接口
 * @ClassName: SuperJDBCAppender
 * @author liushufeng
 * @date 2015年11月15日 下午5:31:21
 */
@Component
public class SuperJDBCAppender extends JDBCAppender {

	@Override
	public boolean isAsSevereAsThreshold(Priority priority) {
		// 只判断是否相等，而不判断优先级
		//return this.getThreshold().equals(priority);
	//	return true;
		if(priority.toInt()==19999)
		return true;
		else 
			return false;
	}
}
