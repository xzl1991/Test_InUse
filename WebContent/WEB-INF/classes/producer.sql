delimiter $$ ; -- 结束符替换为$$
	--定义存储过程
	create Procedure mytest(IN v_id INT, IN v_title INT,IN v_date INT,OUT v_result int)
	begin 
		declare insert_count int default 0;
		start transaction;
			insert ignore into user_test(id,title,indate)
			values(v_id,v_title,v_date);
			select row_count() into insert_count;-- 
			if(insert_count=0) then
				rollback;
				set insert_count=-1;
			else if(insert_count<0) then
				rollback;
				set insert_count=-2;
			else
				update user_test set number=number-1
				where id = v_id and end_time>v_date and start_time<v_date;
				select row_count() into insert_count;-- 
				if(insert_count=0) then
							rollback;
							set insert_count=-1;
				else if(insert_count<0) then
								rollback;
								set insert_count=-2;
				else
								commit;
								set insert_count=1;
				end if;	
			end if;
		end;
	$$ --存储过程定义结束
	delimiter  ;
		set @v_result=-3;

--执行
call 	mytest(111,123213,now(),@v_result);
-- 获取结果
select 	@v_result;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	