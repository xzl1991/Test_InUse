package a_request乱码;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Request_乱码 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	public static void test(HttpServletRequest request,
			HttpServletResponse response){
		String city = null;
		try {
//			request.setCharacterEncoding("utf-8");  
			city = request.getParameter("city");
			//解决 获取 url 汉字 乱码问题
			city = new String(city.getBytes("iso-8859-1"),"utf-8");
			/**
			 * 根据 url 编码类型自己觉得 重构编码
			 * */
			city = new String(city.getBytes("iso-8859-1"),request.getCharacterEncoding());
			
			System.out.println("*****"+city);
			System.out.println("*****"+city);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
