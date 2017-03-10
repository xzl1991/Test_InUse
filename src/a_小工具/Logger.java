package a_小工具; 
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.tomcat.dbcp.pool.impl.GenericObjectPool.Config;

import java.util.Iterator;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 记录登录日志的类.
 */
public class Logger {
    public static Date startTime=new Date();
    public static int error = 0;
     //错误消息
    public static StringBuffer errorMessage = new StringBuffer(); 

    private static SimpleDateFormat formatter= new SimpleDateFormat("MM月dd日H:mm");

    static {
        try {
            Properties prop = new Properties();
            for (Iterator it = Config.getKeys("log4j"); it.hasNext();) {
                String key = (String) it.next();
                if ("log4j.rootCategory".equals(key)) {
                    String s = Config.getVector(key).toString();
                    if (s.length() > 2) {
                        s = s.substring(1, s.length() - 1);
                    } else {
                        s = "DEBUG, config1";
                    }
                    prop.setProperty(key, s);
                } else {
                    prop.setProperty(key, Config.getString(key));
                }
            }
            PropertyConfigurator.configure(prop);
            System.out.println("初始化日志系统...");
        } catch (Exception e) {
            System.out.println("不能初始化日志系统.错误信息：" + e.getMessage());
            e.printStackTrace();
        }
    }


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
    public static void reset(){
        error=0;
        errorMessage=new StringBuffer();
    }
}
