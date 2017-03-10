<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding= "UTF-8"%>
<!DOCTYPE html>
<html>
<script src="js/jquery.min.js" type="text/javascript"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<h1>对过得好</h1>
<body>
    <!-- 2、设置表单的id，在调用ajax的方法时要用到。 -->
    <div >
    <table id="table">
    	<tr><td>1</td></tr>
        <tr><td>Header 1</td></tr>
  		<tr><td>Value 1</td></tr>
        <tr><td>Value 2</td></tr>
    </table>
    </div>
	<form id="ajaxFrm" >
		请输入用户名:<input type="text" name="UserName" >
		<!-- 3、设置一个div，用于显示ajax.jsp页面返回的结果 -->
		<div id="ajaxDiv"></div>
		<!-- 4、增加一个按钮，用来调用ajax -->
		<input type="button" onClick="doFind();" value="调用一下ajax" >
	</form>
	<form id="ajaxAction" >
		<font color='red'>请输入</font>用户名:<input type="text" name="UserName" >
		<!-- 3、设置一个div，用于显示ajax.jsp页面返回的结果 -->
		<div id="ajaxDiv"></div>
		<!-- 4、增加一个按钮，用来调用ajax -->
		<input type="button" onClick="doFind1();" value="调用一下ajax1" >
		<div id="ajaxDiv1"></div>
	</form>
</body>
<script type="text/javascript">
	/* $(document.body).css( "background", "lightgreen" ); */
	$("table").css({ 
		position:'absolute', 
		left: ($(window).width() - $('.className').outerWidth())/2, 
		top: ($(window).height() - $('.className').outerHeight())/2 + $(document).scrollTop() 
		});
	/* $("div").css("background-color", "#bbf" ); */
	$("#table tbody tr:odd").css("background-color", "#bbf" ); 
	$("table tbody tr:even").css("background-color", "#aff");
/* 5、增加调用ajax的函数： */
		function doFind(){
		$.ajax({
		cache: false,
		type: "POST",
		url:"hh.jsp",	//把表单数据发送到ajax.jsp
		data:$('#ajaxFrm').serialize(),	//要发送的是ajaxFrm表单中的数据
		async: false,
		error: function(request) {
		alert("发送请求失败！");
		},
		success: function(data) {
		$("#ajaxDiv").html(data);	//将返回的结果显示到ajaxDiv中
		alert("发送请求成功！");
		}
		});
		}
		
		function doFind1(){
			$.ajax({
			type: "POST",
			url:"http://jkcard.test.fang.com/Other/RedBagSignUp.aspx?city=％b1％b1％be％a9&newcode=1010718911&phone=13522520385&realname=％cc％ef％d0％a1％c1％fa&mediumflag=2&userid=123&username=txl816&source=app_newhousedetail&imei=123213234&projectid=1012&key=BF47816D40A8C8FEA801BB7A63C4DD70",	//把表单数据发送到ajax.jsp
			//dataType: "JSONP", 
			//dataType:'JSONP',
			data:$('#ajaxAction').serialize(),	//要发送的是ajaxFrm表单中的数据
			async: false,
			error: function(xml) {
			alert("发送请求失败！");
				//$("#ajaxDiv1").html(data);	//将返回的结果显示到ajaxDiv中
				alert(xml);
				$(xml).find("soufun_card").each(function(i){ 
			         var id=$(this).children("Message"); //取对象 
			         var idvalue=$(this).children("Message").text(); //取文本 
			         alert("xml参数");
			              });
			}, 
			success: function(xml){
				//alert(JSON.stringify(msg));
				  alert(xml);
       $(xml).find("Message").each(function(i){ 
         var id=$(this).children("Message"); //取对象 
         var idvalue=$(this).children("Message").text(); //取文本 
              });
				}
			});
			}
    </script>
</html>