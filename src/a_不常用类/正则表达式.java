package a_不常用类;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class 正则表达式 {
	public static void main(String[] args){
//		testStringFilter();
		String str = "<(/strong)><*adCVs*34_a _09_b5*>[/435^*&城池()^$$&*).</>{}+.|.)%%*(*.中国}34{45[]12.fd'*&999下面是中文的字符￥……{}【】。，；’“‘”？";
		System.out.println(removeHtml(str));
	}
	public static String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static void testStringFilter() throws PatternSyntaxException {
		String str = "*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中国}34{45[]12.fd'*&999下面是中文的字符￥……{}【】。，；’“‘”？";
		System.out.println(str);
		System.out.println(StringFilter(str));
	}
	public static String removeHtml(String xml){
		try {
			if(xml==null){
				return "";
			}else{
				xml = xml.replaceAll("<[.[^<]]*>", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}
}
