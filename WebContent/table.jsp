<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding= "UTF-8"%>
<!DOCTYPE html>
<head>
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript">
		function show(){
			 
		}
		function choose(){
			var pSel = document.getElementById("province").value;
			var city = document.getElementById("city");
			city.options.length=0;
			if(pSel=="重庆"){
				var cq = new Option("重庆","重庆");
				city.options.add(cq);
				var wz = new Option("万州","万州");
				city.options.add(wz);
				var kx = new Option("开县","开县");
				city.options.add(kx);
				var hc = new Option("合川","合川");
				city.options.add(hc);
			}
			if(pSel=="四川"){
				var wc = new Option("汶川");
				city.options.add(wc);
				var djy = new Option("都江堰");
				city.options.add(djy);
			} 
		}
		if(window.ActiveXObject){
			alert("支持ajax：");	
		}else{
		alert("bu支持ajax：");
		}
		function checkForm(form){
			var bir =birth.value;
			alert(bir);
			if(bir==""||bir==null){
				alert("请输入生日");
				birth.focus();
				return false;
			}
			alert("完成");
			return true;
		}
	</script>
	<form action="" id="myform" onsubmit="return checkForm(this)" >
		<tr>
		生日:<input type="date" name="birth1" id="birth" align="center">
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="确认" id="submit">
			</td>
		</tr>
	</form>
	<center>
		<select id="province" onChange="choose()" >
			<option value="重庆">重庆</option>
			<option value="四川">四川</option>
		</select>省/直辖市
		<select id="city">
			<option value="重庆">重庆</option>
			<option value="成都">成都</option>
			<option value="和县">和县</option>
			<option value="徐州">徐州</option>
		</select>
	</center>
	<input type="button" value="查看" onclick="show()">
	<table id = "tab" width="300" border="3" bordercolor="#003399" align="center">
		<tr bgcolor="#0099ff">
			<th>姓名:</th><th>java</th><th>高数</th><th>线性代数</th><th>总成绩</th>
		</tr>
		<tr>
			<td>张三</td>
			<td>优秀</td>
			<td>99</td>
			<td>78</td>
			<td>47</td>
			<td>555</td>
		</tr>
		<tr>
			<td>李四</td>
			<td>77</td>
			<td>合格</td>
			<td>88</td>
			<td>良好</td>
			<td>666</td>
		</tr>
	</table>
	<div background-color="green" align="center" id='val'></div>
	
	</body>
</html>