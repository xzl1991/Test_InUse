<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery.js"></script>
<!-- <script type="text/javascript" src="js/jquery-1.8.1-vsdoc.js"></script> -->
<title>test jQuery</title>
</head>
<body>
	<div id="msg"> Hello jQuery！</div>
	<input id="show" type="button" value="显示"/>
	<input id="hide" type="button" value="隐藏"/> <br>
	<input id="change" type="button" value="修改内容为hello world too！"/>
	<script type="text/javascript">
			//现在是 通过 包装集
		$("#show").bind("click",function(event){$("#msg").show();});
			//alert($("#show")[0].value);
		//$("#show").each(function(){ alert(this.value+"测试")});
			//alert(document.getElementById("show").value);
		$("#hide").bind("click",function(event){$("#msg").hide();});
		$("#change").bind("click",function(event){$("#msg").html("hello oo");});
		 $("<div >动态div元素</div>").appendTo(hide);
		 $(document).ready(
				 function(){
					msg.innerHTML+="<div style=\"border:solid 3px green\">动态div元素$(document).ready(function()方法</div>";
					}
				 );
		 $(function(){
			msg.innerHTML+="<div style=\"border:solid 3px green\">动态div元素使用  $(function())方法</div>";
		});	
		 //jQuery 提供 each()用于遍历 jQuery 包装集， this指针是 一个 dom 对象
	/* 	 $("img1").each(function(){
			 alert("img测试"+this.alt);
			 /* alter("index:"+index+",描述:"+this.alt+",id:"+this.id+",class:");
			 this.alt ="的身世";
			 alter("index:"+index+",描述:"+this.alt+",id:"+this.id+",class:"); 
		 }); */
		 $("img1").attr({src:"img/02.jpg",alt:"Test图片"});
	</script>
	<img alt="dom属性和元素属性" src="img\01.jpg" id="img1" class="classA">
</body>
</html>













