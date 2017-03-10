
public class 空值判断 {
	public static void main(String[] args){
		String s = null;
		String s1 = null;
		System.out.println("null:s:"+s.equals("s"));
		System.out.println("s:null:"+"s".equals(s1));
	}
	public boolean compare(String s,String s1){
		return s.equals(s1);
	}
}
