<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <script type="text/javascript">
  		function get(){
  			alert(escape("算首套房，必须算首套"));
  		}
  		alert(escape("算首套房，必须算首套"));
  		document.write("<br><br>天天天天天天");
  		document.write("<br><br>esc:"+escape("算首套房，必须算首套 ")+"<br><br>");
  		document.write("<br><br>uri:"+encodeURI("算首套房，必须算首套 ")+"<br><br>");
  		document.write("encodeURI:"+decodeURI("%E7%AE%97%E9%A6%96%E5%A5%97%E6%88%BF%EF%BC%8C%E5%BF%85%E9%A1%BB%E7%AE%97%E9%A6%96%E5%A5%97%20"));
  		document.write("unsec:"+Unescape("%u7B97%u9996%u5957%u623F%uFF0C%u5FC5%u987B%u7B97%u9996%u5957%20"));
  </script>
  <body>
    This is my JSP page.
     <br>
  </body>
</html>
