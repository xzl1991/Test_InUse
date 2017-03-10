package a_小工具;


public class RemoveTest {
	public static void main(String[] args){
		String name = "Test remove 姓名：小星星";
		remove(name);
		System.out.println(name+"...");
		name = remove1(name);
		System.out.println(name+"...");
	}
	public static void remove(String str){
		if(str.contains("remove")){
			str = str.replace("remove", "移除成功");
		}
	}
	public static String remove1(String str){
		if(str.contains("remove")){
			str = str.replace("remove", "移除成功");
			System.out.println(str+"hh");
		}
		System.out.println(str+"...**");
		return str;
	}
}
