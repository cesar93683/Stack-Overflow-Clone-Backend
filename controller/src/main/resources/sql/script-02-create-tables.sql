CREATE DATABASE  IF NOT EXISTS `user_tracker`;
USE `user_tracker`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- password is pass01
insert into user (id, email, username, password)
VALUES(NULL, "user01@gmail.com", "user01", "$2a$10$WRmYG3DRvHNRihopk.RCFOHeOvCe0yPZId6amT574VyEJxRRDBxvC");
-- password is pass02
insert into user (id, email, username, password)
VALUES(NULL, "user02@gmail.com", "user02", "$2a$10$Ue1cDhxgg5Hm58AS4w7aHurS/zzFTQGBSfMxnKRlAMe2iaFFu9NXC");
-- password is pass03
insert into user (id, email, username, password)
VALUES(NULL, "user03@gmail.com", "user03", "$2a$10$/Pp1T8qY87InP97Et4pSPuK0egPdxTBFDZjL9KSw5bdFH46ID9o3K");
-- password is pass04
insert into user (id, email, username, password)
VALUES(NULL, "user04@gmail.com", "user04", "$2a$10$Yuk6YEde4zComZ2AzoliX.aLNWMYOlZnWZJKu3yZLxdRF2SgdwGZm");
-- password is pass05
insert into user (id, email, username, password)
VALUES(NULL, "user05@gmail.com", "user05", "$2a$10$c8D7sSh28qon5iwSzwaYj.o47lTfdq0ECtxAKd8rFo9q1G1bsaYcy");


DROP TABLE IF EXISTS `post`;

CREATE TABLE `post` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(50),
  `content` varchar(500) NOT NULL,
  `votes` int NOT NULL,
  `num_post_responses` int NOT NULL,
  `post_response_id` int,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `vote`;

CREATE TABLE `vote` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vote` varchar(9) NOT NULL,
  `user_id` int NOT NULL,
  `post_id` int NOT NULL,
  `comment_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `content` varchar(100) NOT NULL,
  `votes` int NOT NULL,
  `post_id` int NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;