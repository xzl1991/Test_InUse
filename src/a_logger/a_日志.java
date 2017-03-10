package a_logger;

import org.apache.log4j.Logger;

public class a_日志 {

	/**
	 * @param args
	 */
	private static final Logger log1 = Logger.getLogger(a_日志.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Logger log = new Logger();
//		Log log1 =    log.getLog(a_日志.class);
		String message = "撒倒萨大";
//		log1.setLevel(Level.DEBUG);
		   log1.info(message+"的施工队info");  
	        log1.debug(message+"的是个负担debug");  
	        log1.error(message+"的发挥购房error");  
	}

}











