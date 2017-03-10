function compute(obj){
		var values=document.getElementById("cal");
		values.value = eval(values.value);
		//alert("计算器"+(values));
	}
	function enter(obj){
		
		document.getElementById("cal").value += obj;
		//doucment.getElementById("cal").value=obj;
	}
	function clears(){
		alert("清空");
		document.getElementById("cal").value ='';
	}