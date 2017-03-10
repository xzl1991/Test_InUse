package com.testServlet;

import java.io.IOException;  

import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import a_小工具.二维码;

  
public class CodeServlet extends HttpServlet {  
  
    private static final long serialVersionUID = 1L;  
      
    @Override  
    protected void service(HttpServletRequest requset, HttpServletResponse response)  
            throws ServletException, IOException {  
//        String content = "姓名:maysnow 电话:123687495";  
//                  二维码 encoder = new 二维码();  
//        encoder.encoderQRCoder(content, response);  
    	ZXingCode. getLogoQRCode("http://weixin.qq.com/r/Wf1AWC-EHXJirWs796hL", null, "", response);
    }  
  
}  
























