import java.util.ArrayList;
import java.util.List;


public class Test验证 {

	/**
	 * @param args
	 * ※1过滤关键子的逻辑
   1待检测的内容 删除半角空格" " "\0" "\r\n"，
   2待检测的内容转换为小写
3数据封装 "CheckPost~全国~ask~" + 待检测的内容 + "\0"; 
4调用发送消息的接口，接口参数ip="192.168.7.119",端口号=3636，编码=“GBK”,接口返回 结果信息
5 结果信息根据'~'分组，如果数组长度=3，并且数组第一个数为1，表示含有关键子
	 */
	public static List test(){
		List ls = null;
		ls = new ArrayList();
		for(int i=0;i<4;i++){
			ls.add("撒的撒"+"..."+i);
		}
		return ls;
	}
	public static void main(String[] args) {
		String s = "s";
		 final int max = 16304;//16384
		byte[] bytes = null;
		bytes = s.getBytes();
		System.out.println("字符串的大小:"+bytes.length);
		s = "是s";
		bytes = s.getBytes();
		System.out.println("字符串  "+s+": 的大小"+bytes.length);
		List ls = null;
		ls = test();
		System.out.println("List的大小:"+ls.size()+"^^^"+(16*1024));
		
		s = "1";
		System.out.println(s);
		// TODO Auto-generated method stub
		String content = "S ad\rsaa\\0slks\\r\n好了开始的\\";
		System.out.println("..."+content);
		
		content = content.toLowerCase();
		content = content.replace("\r", "");
		content = content.replace("\n", "");
		content = content.replace("\0", "");
		content = content.replace(" ", "");
		System.out.println("****:"+content);
		String post = "CheckPost~全国~ask~"+content+"\0";
		System.out.println(post);
	}
	public String checkContent(String content){
		content = content.toLowerCase();
		content = content.replace("\r", "");
		content = content.replace("\n", "");
		content = content.replace("\0", "");
		content = content.replace(" ", "");
		System.out.println("****:"+content);
		String post = "CheckPost~全国~ask~"+content+"\0";
		System.out.println(post);
		return post;
	}
}
















