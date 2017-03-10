package a_不常用类;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Spilt分隔符 {
	public static void main(String[] args){
		StringBuffer sb = new StringBuffer(10);
		System.out.println(sb.length()+"..*****");
		String s1 = "0,1,1,,";
		String[] sl = s1.split(",");
		System.out.println("..."+sl.length);
		List l = new ArrayList<Object>();
		System.out.println(l.size()+"...");
		String[] userType = new String[]{"深度"};
		for(int i=1;i<userType.length;i++){
			System.out.println(userType[i]);
		}
		String s = "";
		String citys = "北京, 武汉, 石家庄, 合肥, 南昌, 上海, 重庆, 西安青岛, 东莞, 天津, 深圳, 苏州, 郑州, 南京, 沈阳, 杭州, 广州, 成都,宁波, 济南, 无锡, 长沙";
		System.out.println(citys.contains(""));
		String[] a = s.split(",");
		List<String> arr = Arrays.asList(a);
		for(String st:a){
			System.out.println(st);
		}
		for(int i=0;i<arr.size();i++){
			System.out.println(arr.get(i));
		}
		System.out.println(arr.isEmpty()+""+arr.size()+","+a.length);
		System.out.println(l.isEmpty()+""+l.size());
	}
}
