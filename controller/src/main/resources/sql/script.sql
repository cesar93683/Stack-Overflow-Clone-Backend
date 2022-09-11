CREATE DATABASE  IF NOT EXISTS `user_tracker`;
USE `user_tracker`;

DROP TABLE IF EXISTS `question_tag`;
DROP TABLE IF EXISTS `tag`;
DROP TABLE IF EXISTS `vote`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `answer`;
DROP TABLE IF EXISTS `question`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `reputation` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- password is pass01
insert into user (id, email, username, password, reputation)
VALUES(NULL, "user01@gmail.com", "user01", "$2a$10$WRmYG3DRvHNRihopk.RCFOHeOvCe0yPZId6amT574VyEJxRRDBxvC", 1);
-- password is pass02
insert into user (id, email, username, password, reputation)
VALUES(NULL, "user02@gmail.com", "user02", "$2a$10$Ue1cDhxgg5Hm58AS4w7aHurS/zzFTQGBSfMxnKRlAMe2iaFFu9NXC", 1);
-- password is pass03
insert into user (id, email, username, password, reputation)
VALUES(NULL, "user03@gmail.com", "user03", "$2a$10$/Pp1T8qY87InP97Et4pSPuK0egPdxTBFDZjL9KSw5bdFH46ID9o3K", 1);
-- password is pass04
insert into user (id, email, username, password, reputation)
VALUES(NULL, "user04@gmail.com", "user04", "$2a$10$Yuk6YEde4zComZ2AzoliX.aLNWMYOlZnWZJKu3yZLxdRF2SgdwGZm", 1);
-- password is pass05
insert into user (id, email, username, password, reputation)
VALUES(NULL, "user05@gmail.com", "user05", "$2a$10$c8D7sSh28qon5iwSzwaYj.o47lTfdq0ECtxAKd8rFo9q1G1bsaYcy", 1);

CREATE TABLE `question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(50),
  `content` varchar(500) NOT NULL,
  `votes` int NOT NULL,
  `num_answers` int NOT NULL,
  `answered` int NOT NULL,
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
  `accepted` int NOT NULL,
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

CREATE TABLE `tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(20) NOT NULL,
  `description` varchar(500) NOT NULL,
  `num_questions` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

insert into tag (id, tag, description, num_questions)
VALUES(NULL, "javascript", "For questions regarding programming in ECMAScript (JavaScript/JS) and its various dialects/implementations (excluding ActionScript). Note JavaScript is NOT the same as Java!", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "java", "Java is a high-level object-oriented programming language. Use this tag when you're having problems using or understanding the language itself. This tag is frequently used alongside other tags for libraries and/or frameworks used by Java developers.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "python", "Python is a multi-paradigm, dynamically typed, multi-purpose programming language. It is designed to be quick to learn, understand, and use, and enforces a clean and uniform syntax. Please note that Python 2 is officially out of support as of 2020-01-01. For version-specific Python questions, add the [python-2.7] or [python-3.x] tag. When using a Python variant (e.g. Jython, PyPy) or library (e.g. Pandas, NumPy), please include it in the tags.", 0);

CREATE TABLE `question_tag` (
	`question_id` int NOT NULL,
    `tag_id` int NOT NULL,
    PRIMARY KEY (`question_id`, `tag_id`),
	CONSTRAINT `FK_QUESTION`
    FOREIGN KEY (`question_id`)
    REFERENCES `question` (`id`),
	CONSTRAINT `FK_TAG`
    FOREIGN KEY (`tag_id`)
    REFERENCES `tag` (`id`)
);