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


DROP TABLE IF EXISTS `vote`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `answer`;
DROP TABLE IF EXISTS `question`;

CREATE TABLE `question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(50),
  `content` varchar(500) NOT NULL,
  `votes` int NOT NULL,
  `num_answers` int NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,
  PRIMARY KEY (`id`),
  KEY `FK_QUESTION_USER_idx` (`user_id`),
  CONSTRAINT `FK_QUESTION_USER`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `answer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `question_id` int NOT NULL,
  `content` varchar(500) NOT NULL,
  `votes` int NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,
  PRIMARY KEY (`id`),
  KEY `FK_ANSWER_USER_idx` (`user_id`),
  CONSTRAINT `FK_ANSWER_USER`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`),
  KEY `FK_ANSWER_QUESTION_idx` (`question_id`),
  CONSTRAINT `FK_ANSWER_QUESTION`
  FOREIGN KEY (`question_id`)
  REFERENCES `question` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `content` varchar(100) NOT NULL,
  `votes` int NOT NULL,
  `question_id` int,
  `answer_id` int,
  `created_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COMMENT_USER_idx` (`user_id`),
  CONSTRAINT `FK_COMMENT_USER`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`),
  KEY `FK_COMMENT_QUESTION_idx` (`question_id`),
  CONSTRAINT `FK_COMMENT_QUESTION`
  FOREIGN KEY (`question_id`)
  REFERENCES `question` (`id`),
  KEY `FK_COMMENT_ANSWER_idx` (`answer_id`),
  CONSTRAINT `FK_COMMENT_ANSWER`
  FOREIGN KEY (`answer_id`)
  REFERENCES `answer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `vote` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vote` int NOT NULL,
  `user_id` int NOT NULL,
  `question_id` int,
  `answer_id` int,
  `comment_id` int,
  PRIMARY KEY (`id`),
  KEY `FK_VOTE_USER_idx` (`user_id`),
  CONSTRAINT `FK_VOTE_USER`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`),
  KEY `FK_VOTE_QUESTION_idx` (`question_id`),
  CONSTRAINT `FK_VOTE_QUESTION`
  FOREIGN KEY (`question_id`)
  REFERENCES `question` (`id`),
  KEY `FK_VOTE_ANSWER_idx` (`answer_id`),
  CONSTRAINT `FK_VOTE_ANSWER`
  FOREIGN KEY (`answer_id`)
  REFERENCES `answer` (`id`),
  KEY `FK_VOTE_COMMENT_idx` (`comment_id`),
  CONSTRAINT `FK_VOTE_COMMENT`
  FOREIGN KEY (`comment_id`)
  REFERENCES `comment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;