
$(function(){
	$('#menu>ul>li>ul').find('li:has(ul:not(:empty))>a').append("<span" +
			" class='arrow'>></span>");
	//添加 对于 li标签，鼠标移上和离开 子标签 ul的事件
	$("#menu>ul>li").bind('mouseover',function(){
		$(this).children('ul').slideDown('slow');
		}).bind('mouseleave',function(){
			$(this).children('ul').slideUp('slow');
		});
	
	//添加 对于 最底层li标签，鼠标移上和离开 子标签 ul的事件
	 $('#menu>ul>li>ul li').bind('mouseover',function(){
		 //移动到上面时展开
		 $(this).children('ul').slideDown('slow');
	 })	.bind('mouseleave',function(){
		 //移除时收起
		$(this).children('ul').slideUp('slow'); 
	 })	 ;
	
	}
);






















