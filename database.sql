DROP database if exists ImHungry;
CREATE database ImHungry;
USE ImHungry;

CREATE table users(
	user_id int(11) primary key not null auto_increment, 
    user_name varchar(500) not null,
    user_password varchar(40) not null
);
