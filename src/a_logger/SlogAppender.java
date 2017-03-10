package a_logger;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

public class SlogAppender extends DailyRollingFileAppender  {
	public boolean isAsSevereAsThreshold(Priority priority) {
		// 判断是否相等，而不判断优先级
		return this.getThreshold().equals(priority);
	}
}
