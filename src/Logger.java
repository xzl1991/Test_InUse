/**
 * <p>
 * Title:  时间工具类
 * </p>
 * <p>
 * Description: 记录系统日志
 * </p>
 * 
 * <p>
 * Company: eu
 * </p>
 * <p>
 * Creat Date:2006-7-30
 * 
 * @version 1.0
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

/**
 * 记录登录日志的类.
 */
public class Logger extends DailyRollingFileAppender{
    public static Date startTime=new Date();
    public static int error = 0;
    public  boolean isAsSevereAsThreshold(Priority priority) {
		// 判断是否相等，而不判断优先级
		return this.getThreshold().equals(priority);
	}
     //错误消息
    public static StringBuffer errorMessage = new StringBuffer(); 

    private static SimpleDateFormat formatter= new SimpleDateFormat("MM月dd日H:mm");

    public static Log getLog(Class clazz) {
        return LogFactory.getLog(clazz);
    }

    public static Log getLog(String name) {
        return LogFactory.getLog(name);
    }

    public static void write(String loginfo) {
        Log log = LogFactory.getLog("消息");
        log.info(loginfo);
    }
    public static void write(Throwable e) {
        error++;
        errorMessage.insert(0,formatter.format(new Date())+" "+e.toString()+"\n");
        Log log = LogFactory.getLog("异常");
        log.error(e);
    }

    public static void write(String errorinfo, Throwable e) {
        error++;
        errorMessage.insert(0,formatter.format(new Date())+" "+errorinfo+e.toString()+"\n");
        Log log = LogFactory.getLog("错误");
        log.error(errorinfo, e);
    }
    public void reset(){
        error=0;
        errorMessage=new StringBuffer();
    }
}
