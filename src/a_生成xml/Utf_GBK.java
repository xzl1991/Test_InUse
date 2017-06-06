package a_生成xml;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utf_GBK {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String c = "1234,sss,撒的撒";
		String d = "ss1";
		System.out.println(d+ d);
		System.out.println(c.substring(c.lastIndexOf(",")+1));
		System.out.println(c.substring(0,c.indexOf(",")));
		System.out.println(c.substring(c.indexOf(",")+1,c.lastIndexOf(",")));
		
		System.out.println(c.equals("")?"12":"sd");
		String s = "房产交易 2-家居装修 3-业主生活 -1-不分类别防注入删除最后的逗号";
		s = StringUtils.URLEncode_GBK(s);
		s = StringUtils.URLDecoder_GBK(s);
		System.out.println(s);
		Date date = new Date();
		System.out.println(date);
		 SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df2.format(getNextDay(date)));
	}
	public static Date getNextDay(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        date = calendar.getTime();  
        return date;  
    }
}
