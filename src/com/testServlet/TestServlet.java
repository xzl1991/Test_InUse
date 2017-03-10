package com.testServlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String UserName ;
    public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	/**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub  response.setHeader("Content-type", "text/html;charset=UTF-8"); 
//		response.setHeader("Content-type", "text/html;charset=utf-8");
////		response.setContentType("text/html");
//		PrintWriter pri = response.getWriter();
//		pri.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");;
//		pri.println("<html>");
//		pri.println("<head><h1>测试servlet</h1></head>");
//		pri.println("<body>这是get方法</body>");
//		pri.println("</html>");
//		pri.flush();
////		response.sendRedirect("NewFile.jsp");
//		pri.close();
		response.sendRedirect("NewFile.jsp");
//		request.getRequestDispatcher("NewFile.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter pri = response.getWriter();
		pri.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");;
		pri.println("<html>");
		pri.println("<head><h1>测试servlet</h1></head>");
		System.out.println(UserName);
		
		//获取参数
		String s = request.getParameter("UserName");
		pri.println("<body>这是post方法<h1 align='center'><font color='gold'>"+UserName+";"+s);
//		pri.println("<body>这是post方法<h1 align='center'><font color="+"red"+">"+UserName+";"+s);
		System.out.println(s);
		pri.println("</font></h1></body>");
		pri.println("</html>");
		pri.flush();
		pri.close();
	}

}
