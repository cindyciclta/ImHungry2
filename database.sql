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
