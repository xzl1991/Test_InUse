package a_数据库连接池;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class 判断_星期 {
	public static void main(String[] args) throws ParseException{
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date()); 
		int week=cal.get(Calendar.DAY_OF_WEEK)-1;
		System.out.println(week+"...");
		System.out.println(",.,.."+getMondayOfThisWeek());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal1 = Calendar.getInstance();
    	cal1.setTime(new Date());
    	cal1.add(Calendar.WEEK_OF_YEAR, -1);
    	Date time = cal1.getTime();
		System.out.println(time);
		
		
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(); //当前日期

		Date dt=null; //本月第一天日期
		try {
		dt = new SimpleDateFormat("yyyy-MM").parse(tempDate.format(date));
		} catch (Exception e) {
		e.printStackTrace();
		}

		System.out.println("当前日期："+tempDate.format(date));
		System.out.println("本月第一天日期："+tempDate.format(dt));
	}
	 public static String getMondayOfThisWeek() throws ParseException {
		  Calendar c = Calendar.getInstance();
		  SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		  if (day_of_week == 0)
		   day_of_week = 7;
		  c.add(Calendar.DATE, -day_of_week + 1);
		  Date dat0 = new SimpleDateFormat("yyyy-MM-dd").parse(df2.format(c.getTime()));
		  
		  return df2.format(dat0);
		 }
}
