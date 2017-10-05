CREATE TABLE IF NOT EXISTS user (
  id int PRIMARY KEY AUTO_INCREMENT,
  name varchar(255),
  age int,
  active boolean default true
);

CREATE TABLE IF NOT EXISTS topic (
  id int PRIMARY KEY AUTO_INCREMENT,
  owner_id int,
  title varchar(255),
  archived boolean default false,
  date timestamp default CURRENT_TIMESTAMP(),
  foreign key (owner_id) references user(id)
);

CREATE TABLE IF NOT EXISTS comment (
  id int PRIMARY KEY AUTO_INCREMENT,
  user_id int,
  topic_id int,
  content text,
  archived boolean default false,
  date timestamp default CURRENT_TIMESTAMP(),
  foreign key (user_id) references user(id),
  foreign key (topic_id) references topic(id)
);