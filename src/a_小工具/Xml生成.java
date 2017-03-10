package a_小工具;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Xml生成 {
	public static void main(String [] args){
		StringBuffer sb = new StringBuffer(2);
		sb.append("313132132132");
		System.out.println(sb.toString());
		//添加时间参数时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.WEEK_OF_YEAR, -1);
    	Date time = cal.getTime();
    	String sql = "SELECT * FROM [AskBegXuanShang] WITH(NOLOCK) WHERE UserID=?  AND State=-1  AND AddTime<='?"
    		 + "' AND AddTime>'?'"; 
	}
}
