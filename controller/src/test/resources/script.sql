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

CREATE TABLE `question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(200),
  `content` varchar(10000) NOT NULL,
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
  `content` varchar(10000) NOT NULL,
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
  `content` varchar(1000) NOT NULL,
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
  `description` varchar(5000) NOT NULL,
  `num_questions` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

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

insert into tag (id, tag, description, num_questions)
VALUES(NULL, "javascript", "For questions regarding programming in ECMAScript (JavaScript/JS) and its various dialects/implementations (excluding ActionScript). Note JavaScript is NOT the same as Java!", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "java", "Java is a high-level object-oriented programming language. Use this tag when you're having problems using or understanding the language itself. This tag is frequently used alongside other tags for libraries and/or frameworks used by Java developers.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "python", "Python is a multi-paradigm, dynamically typed, multi-purpose programming language. It is designed to be quick to learn, understand, and use, and enforces a clean and uniform syntax. Please note that Python 2 is officially out of support as of 2020-01-01. For version-specific Python questions, add the [python-2.7] or [python-3.x] tag. When using a Python variant (e.g. Jython, PyPy) or library (e.g. Pandas, NumPy), please include it in the tags.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "android", "Android is Google's mobile operating system, used for programming or developing digital devices (Smartphones, Tablets, Automobiles, TVs, Wear, Glass, IoT). For topics related to Android, use Android-specific tags such as android-intent, android-activity, android-adapter, etc.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "html", "HTML (HyperText Markup Language) is the markup language for creating web pages and other information to be displayed in a web browser. Questions regarding HTML should include a minimal reproducible example and some idea of what you're trying to achieve. This tag is rarely used alone and is often paired with [CSS] and [JavaScript].", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "c++", "C++ is a general-purpose programming language. It was originally designed as an extension to C and has a similar syntax, but it is now a completely different language. Use this tag for questions about code (to be) compiled with a C++ compiler.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "css", "CSS (Cascading Style Sheets) is a representation style sheet language used for describing the look and formatting of HTML (HyperText Markup Language), XML (Extensible Markup Language) documents and SVG elements including (but not limited to) colors, layout, fonts, and animations. It also describes how elements should be rendered on screen, on paper, in speech, or on other media.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "mysql", "MySQL is a free, open-source Relational Database Management System (RDBMS) that uses Structured Query Language (SQL). DO NOT USE this tag for other DBs such as SQL Server, SQLite etc. Those are different DBs that all use their own dialects of SQL to manage the data.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "sql", "Structured Query Language (SQL) is a language for querying databases. Questions should include code examples, table structure, sample data, and a tag for the DBMS implementation (e.g. MySQL, PostgreSQL, Oracle, MS SQL Server, IBM DB2, etc.) being used. If your question relates solely to a specific DBMS (uses specific extensions/features), use that DBMS's tag instead. Answers to questions tagged with SQL should use ISO/IEC standard SQL.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "node.js", "Node.js is an event-based, non-blocking, asynchronous I/O runtime that uses Google's V8 JavaScript engine and libuv library. It is used for developing applications that make heavy use of the ability to run JavaScript both on the client as well as on the server side and therefore benefit from the re-usability of code and the lack of context switching.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "reactjs", "React is a JavaScript library for building user interfaces. It uses a declarative, component-based paradigm and aims to be efficient and flexible.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "arrays", "An array is an ordered linear data structure consisting of a collection of elements (values, variables, or references), each identified by one or more indexes. When asking about specific variants of arrays, use these related tags instead: [vector], [arraylist], [matrix]. When using this tag, in a question that is specific to a programming language, tag the question with the programming language being used.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "json", "JSON (JavaScript Object Notation) is a serializable data interchange format that is a machine and human readable. Do not use this tag for native JavaScript objects or JavaScript object literals. Before you ask a question, validate your JSON using a JSON validator such as JSONLint (https://jsonlint.com).", 0);