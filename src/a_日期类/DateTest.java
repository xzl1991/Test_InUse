package a_日期类;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTest {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		try {
			System.out.println("方法个发广告");
			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			System.out.println("卡卡卡卡卡");
		}
		System.out.println(getFirstDayOfMonth1(0));
		String s = "2015-05-13 15:40:19.530";
		String s1 = "2015-05-17 15:55:19.530";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Date date1 = new Date();
		System.out.println("一周前:"+sdf.format(getDayFromToday(date,-7)));
		date = sdf.parse(s);
		date1= sdf.parse(s1);
		long diff = date1.getTime()-date.getTime() ;
		System.out.println(diff/60/1000);
		System.out.println(diff/60/1000/60/24);
	}
	/**
	 * 获取周一0点时间
	 * */
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
	 public static Date getYesterDay(Date date) {  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        calendar.add(Calendar.DAY_OF_MONTH, 1);  
	        date = calendar.getTime();  
	        return date;  
	    }
	 /**获取当前为起点第 i 天
	  * 负数 当前之前日期
	  * */
	 public static Date getDayFromToday(Date date,int i) {  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        calendar.add(Calendar.DAY_OF_MONTH, i);  
	        date = calendar.getTime();  
	        return date;  
	    }
	 /**获取当前为起点第 i 小时前
	  * 负数 当前之前日期
	  * */
	 public static Date getDateByHours(Date date,int i) {  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        calendar.add(Calendar.HOUR_OF_DAY, i);  
	        date = calendar.getTime();  
	        return date;  
	    }
		/**
		 * 获取当月第一天
		 * next 指定月份
		 * */
		public static String getFirstDayOfMonth(int next) {
			String firstday = null;
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			System.out.println(Calendar.MONTH+"******"+(cal.get(Calendar.MONTH) +1));//获取当前月份
			cal.add(Calendar.MONTH, next);
			cal.set(Calendar.DAY_OF_MONTH, -1);
			firstday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			return firstday;
		}
		/**
		 * 获取当月最后一天
		 * next 指定月份
		 * */
		public static String getFirstDayOfMonth1(int next) {
			String firstday = null;
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			System.out.println(Calendar.MONTH+"******"+(cal.get(Calendar.MONTH) +1));//获取当前月份
			cal.add(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH,  cal.getActualMaximum(Calendar.DAY_OF_MONTH));//-1
			firstday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			return firstday;
		}
//		/**
//		 * 获取当前为起点第 i 天 负数 当前之前日期
//		 * */
//		public static Date getDayFromToday(Date date, int i) {
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(date);
//			calendar.add(Calendar.DAY_OF_MONTH, i);
//			date = calendar.getTime();
//			return date;
//		}
}



















