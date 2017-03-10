<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery.js"></script>
<title>Insert title here</title>
</head>
<script type="text/javascript">
	$(function(){
		// 获取对象 添加 单击事件
		//document.getElementById("msg1").onclick=show;
		$("#msg1")[0].onclick=show;
	}
	);
	function show(event){
		alert("事件单击:");
	}
	$("#msg4")[0].live("click",show);
	$("#msg2")[0].live("click",show);
	
</script>
<body>
	<div id="msg" onclick="show()">单击事件</div>
	<div id="msg1">单击事件1</div>
	<div id="msg2">单击事件2</div>
	<div id="msg4" >获取自定义数据</div>
	<input type="button" value="msg3" value="单击">
</body>
</html>