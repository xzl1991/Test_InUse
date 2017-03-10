package a_listener_Task;

import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;




public class MyScheduleListener implements ServletContextListener{
	private Timer timer = null;
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		timer.cancel();
		sce.getServletContext().log("计时器被销毁..");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		//启动调度器
		timer = new Timer(true);
		//启动日志
		sce.getServletContext().log(new Date()+"计时器启动..");
		//调度任务
		timer.schedule( new CheckSystemStateTask(),0, 60*100);
	}
	
}
