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
		$("old").click( 
				function(){
				alert("的是个负担");
				}
		);
		$("new").click(function(){
			alert("十大3");
			$("#Result").html("第二个new");
			//使用 triggerHandler
			$("input").triggerHandler("focus");
		});
</script>
<body>
<input id="old" type="button" value="聚焦1" >
<input id="new" type="button" value="聚焦2" >
	<input type="text" value="聚焦" >
	<div id="Result"></div>
</body>
</html>












