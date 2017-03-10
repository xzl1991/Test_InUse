/**
 * <p>
 * Title:  ʱ�乤����
 * </p>
 * <p>
 * Description: ��¼ϵͳ��־
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
 * ��¼��¼��־����.
 */
public class Logger extends DailyRollingFileAppender{
    public static Date startTime=new Date();
    public static int error = 0;
    public  boolean isAsSevereAsThreshold(Priority priority) {
		// �ж��Ƿ���ȣ������ж����ȼ�
		return this.getThreshold().equals(priority);
	}
     //������Ϣ
    public static StringBuffer errorMessage = new StringBuffer(); 

    private static SimpleDateFormat formatter= new SimpleDateFormat("MM��dd��H:mm");

    public static Log getLog(Class clazz) {
        return LogFactory.getLog(clazz);
    }

    public static Log getLog(String name) {
        return LogFactory.getLog(name);
    }

    public static void write(String loginfo) {
        Log log = LogFactory.getLog("��Ϣ");
        log.info(loginfo);
    }
    public static void write(Throwable e) {
        error++;
        errorMessage.insert(0,formatter.format(new Date())+" "+e.toString()+"\n");
        Log log = LogFactory.getLog("�쳣");
        log.error(e);
    }

    public static void write(String errorinfo, Throwable e) {
        error++;
        errorMessage.insert(0,formatter.format(new Date())+" "+errorinfo+e.toString()+"\n");
        Log log = LogFactory.getLog("����");
        log.error(errorinfo, e);
    }
    public void reset(){
        error=0;
        errorMessage=new StringBuffer();
    }
}
