package a_listener_Task;

import java.util.TimerTask;

public class CheckSystemStateTask extends TimerTask {
	private boolean isRunning = false;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(!isRunning){
			isRunning =true;
			System.out.println("开始");
			for(int i=0;i<5;i++){
				System.out.println("完成:"+i);
			}
			isRunning =false;
			System.out.println("所有任务结束..");
		}else{
			System.out.println("退出");
		}
	}

}
