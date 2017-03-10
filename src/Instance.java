
public class Instance {
	private static Instance inst = null;
	private Instance(){
	}
	public static Instance getInst(){
		if(inst==null){
			System.out.println("空的。。。。");
			inst = new Instance();
		}
		return inst;
	}
	public static void main(String[] args){
		System.out.println(get());
		getInst();
		getInst();
		Pers p = new Pers();
	}
	public static String get(){
		String st = null;
		try {
			st = "s";
			return st;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			st = "h";
		}
		return st;
	}

}
class Pers{
	private String name ;
	public Pers(){
		this.name = "苍井空";
	}
	public String getNames() {
		try {
			name = "cy";
			return name;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			name = "h";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}