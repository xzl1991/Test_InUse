drop table  if exists   users;
create table users(
	 userid varchar(20) primary key not null comment '用户id',
	 name varchar(15) not null,
	 credit int,
	 password varchar(15) not null,
	 userComment varchar(200) ,
	 ipAddress varchar(12),
	 img varchar(15),
	 log_date timestamp
)ENGINE = INNODB CHARACTER set=utf8; 


create table login_log(
	id	varchar(20) primary key not null comment 'Lid',
	 userid varchar(20) ,
	 logintime timestamp
)ENGINE = INNODB CHARACTER set=utf8; 





create table if not exists
 a_user (
 id VARCHAR(15) PRIMARY KEY NOT NULL   COMMENT '设置主键自增', 
 name VARCHAR(20) not NULL COMMENT '姓名',
 pwd VARCHAR(20) not NULL COMMENT '密码',
 age int,
 sex VARCHAR(1)
)ENGINE = INNODB CHARACTER set=utf8; 
alter table a_user MODIFY COLUMN id VARCHAR(20);					 -- 修改某列的属性
ALTER table a_user MODIFY COLUMN  sex  VARCHAR(1) COMMENT '这是字段的注释'  mysql 为列添加注释的时候， 把参数类型也要加上
ALTER TABLE a_user   add COMMENT '1:男,0:女' on column sex 
ALTER TABLE a_user  COMMENT on column a_user.sex  is '1:男,0:女' 



insert into a_user VALUES(UUID_SHORT(),'张三','1234',22,1) -- 通过uuid 方式生成 id
select * from a_user;

select @@IDENTITY;
 


drop table  if exists   users;
create table users(
	 userid varchar(20) primary key not null comment '用户id',
	 name varchar(15) not null,
	 credit int,
	 password varchar(15) not null,
	 userComment varchar(200) ,
	 ipAddress varchar(12),
	 img varchar(15),
	 log_date timestamp
)ENGINE = INNODB CHARACTER set=utf8; 



create table login_log(
	id	varchar(20) primary key not null comment 'Lid',
	 userid varchar(20) ,
	 logintime timestamp
)ENGINE = INNODB CHARACTER set=utf8; 

SELECT * from users






































