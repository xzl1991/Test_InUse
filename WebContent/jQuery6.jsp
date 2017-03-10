<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery.js"></script>
<title>Insert title here</title>
	<style type="text/css">
		* { margin:0; padding:0;}
		body{
			background-image: url(img/01.jpg);
			margin: 20px;
			padding: 0px;
			align:center;
		}
		img{
		align:center;
		margin: 120px;
			padding: 0px;
			border:solid 3px gold;
		}
	</style>
</head>
<body>
	<script type="text/javascript">
		$(function(){
			$("img").hover(
				function(oEve){
					$(oEve.target).css("opacity","0.5");
				},
				
				function(oEve){
					$(oEve.target).css("opacity","1.0");
				}	
				);
			}
		);
	</script>
	<img alt="图片" src="img/02.jpg" align="middle">
</body>
</html>










