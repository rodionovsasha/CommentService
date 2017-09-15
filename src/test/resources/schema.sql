CREATE TABLE IF NOT EXISTS user (
  id int PRIMARY KEY AUTO_INCREMENT,
  name varchar(255),
  age int,
  enabled boolean default true
);

CREATE TABLE IF NOT EXISTS topic (
  id int PRIMARY KEY AUTO_INCREMENT,
  user_id int,
  name varchar(255),
  date timestamp default CURRENT_TIMESTAMP(),
  foreign key (user_id) references user(id)
);

CREATE TABLE IF NOT EXISTS comment (
  id int PRIMARY KEY AUTO_INCREMENT,
  user_id int,
  topic_id int,
  content text,
  date timestamp default CURRENT_TIMESTAMP(),
  foreign key (user_id) references user(id),
  foreign key (topic_id) references topic(id)
);