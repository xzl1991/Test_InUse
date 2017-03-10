package a_反射测试;

import java.util.HashMap;
import java.util.Map;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String[] params = new String[]{"张三","北京","10086"};
		String[] params1 = new String[]{"Name","Address","Phone"};
		double ma = addMap(params);
		double pe = addPerson(params)==0?1:addPerson(params);
		System.out.println((pe/ma)+"Map/person:"+(ma/pe));
	}
	public static long addMap(String[] params) {
		long start = System.currentTimeMillis();
		Map<String,String> map = null;
		for(int i=0;i<10;i++){
			map = new HashMap<String, String>();
//			for(int j=0;j<params1.length;j++){
//				map.put(params[i], params[i]);
//			}
			map.put("name", params[0]);
			map.put("address", params[1]);
			map.put("phone", params[2]);
			System.out.println("姓名:"+map.get("name")+"，地址:"+map.get("address")+"，手机:"+map.get("phone"));
		}
		System.out.println("map的put时间:"+(System.currentTimeMillis()-start));
		return System.currentTimeMillis()-start;
	}
	public static long addPerson(String[] params) {
		long start = System.currentTimeMillis();
		Person person = null;
		for(int i=0;i<10;i++){
			person = new Person();
			person.setName(params[0]);
			person.setAddress(params[1]);
			person.setPhone(params[2]);
			System.out.println("姓名:"+person.getName()+"，地址:"+person.getAddress()+"，手机:"+person.getPhone());
		}
		System.out.println("Person的set时间:"+(System.currentTimeMillis()-start));
		return System.currentTimeMillis()-start;
	}
}






















