package a_不常用类;



public class 正则表达式_no {
	public static void main(String[] args){
		String str = "asd&sdklkl";
		comp(str);
//		char ch = 'a';
//		System.out.println(ch+0);
//		System.out.println('z'>'x');
//		System.out.println('A'+0);
//		System.out.println('Z'+0);
		
	}
	public static void comp(String str){
		int index = 0;
		char[] chs = str.toCharArray();
		String[] sts = null;
		StringBuilder sb = null;
		int len = chs.length;
		sts = new String[len/2+1];
		boolean newChar = false;
		for(int i=0;i<len;i++){
			char c = chs[i];
			if(i==0){
				sb = new StringBuilder();
				if(check(c))
					sb.append(c);
			}else{
				if(check(c)){
					if(newChar){
						sb = new StringBuilder();
						sb.append(c);
						newChar = false;
					}else{
						sb.append(c);
						if(i==len-1)
							sts[index++] = sb.toString();
					}
						
				}
				else{
					if(newChar)//特殊字符
						newChar = true;//新建字符串
					else{
						sts[index++] = sb.toString();
						newChar = true;//新建字符串
					}
				}
			}	
		}
		System.out.println("符合的字符串有:"+index+"个");
		for(int i=0;i<index;i++){
			System.out.println("依次为:"+sts[i]);
		}
	}
	private static boolean check(char c){
		return 'a'<=c&&c<='z'||('A'<=c&&c<='Z');
	}
}
















