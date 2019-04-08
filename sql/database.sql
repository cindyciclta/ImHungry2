DROP database if exists ImHungry;
CREATE database ImHungry;
USE ImHungry;

CREATE table users(
	user_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, 
    user_name VARCHAR(500) NOT NULL,
    user_password VARCHAR(40) NOT NULL
);

CREATE table searches(
	user_id INT(11),
	FOREIGN KEY fk1 (user_id) REFERENCES  users(user_id),
    term VARCHAR(500),
    limit_search INT(11),
    radius INT(11)
);


CREATE table list_restaurants(
	user_id INT(11),
	FOREIGN KEY fk2 (user_id) REFERENCES  users(user_id),
    is_favorite BOOLEAN not null,
    is_to_explore BOOLEAN not null,
    do_not_show BOOLEAN not null,
    name_item VARCHAR(500) not null,
    position_item INT(11) not null
);