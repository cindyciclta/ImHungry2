DROP database if exists ImHungry;
CREATE database ImHungry;
USE ImHungry;

CREATE table users(
	user_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, 
    user_name VARCHAR(500) NOT NULL,
    user_password VARCHAR(40) NOT NULL
);

CREATE table searches(
	search_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, 
	user_id INT(11),
	FOREIGN KEY fk1 (user_id) REFERENCES  users(user_id),
    term VARCHAR(500) not null,
    limit_search INT(11) not null,
    radius INT(11) not null,
    insert_time LONG not null
);

CREATE table images(
	image_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	term VARCHAR(100),
    url VARCHAR(400),
    UNIQUE KEY unq (term, url)
);

CREATE table recipes(
	item_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	search_id INT(11),
	FOREIGN KEY fk6 (search_id) REFERENCES  searches(search_id),
    json_string longtext
);

CREATE table restaurants(
	item_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	search_id INT(11),
	FOREIGN KEY fk7 (search_id) REFERENCES  searches(search_id),
    json_string longtext
);


CREATE table list_restaurants(
	list_item_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	user_id INT(11),
	FOREIGN KEY fk2 (user_id) REFERENCES  users(user_id),
    item_id INT(11),
    FOREIGN KEY fk3 (item_id) REFERENCES  restaurants(item_id),
    name VARCHAR(100),
    place INT(11)
);

CREATE table list_recipes(
	list_item_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	user_id INT(11),
	FOREIGN KEY fk4 (user_id) REFERENCES  users(user_id),
    item_id INT(11),
    FOREIGN KEY fk5 (item_id) REFERENCES  recipes(item_id),
    name VARCHAR(100),
    place INT(11)
);

CREATE table grocery_list(
	object_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    selected_item VARCHAR(100) NOT NULL,
    user_id INT(11),
    FOREIGN KEY fk1 (user_id) REFERENCES  users(user_id),
    ordering INT(11)
);




