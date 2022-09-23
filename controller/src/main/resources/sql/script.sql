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
  `title` varchar(50),
  `content` varchar(5000) NOT NULL,
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
  `content` varchar(5000) NOT NULL,
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
VALUES(NULL, "c#", "C# (pronounced \"see sharp\") is a high level, statically typed, multi-paradigm programming language developed by Microsoft. C# code usually targets Microsoft's .NET family of tools and run-times, which include .NET, .NET Framework and Xamarin among others. Use this tag for questions about code written in C# or about C#'s formal specification.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "php", "PHP is a widely used, open source, general-purpose, multi-paradigm, dynamically typed and interpreted scripting language designed initially for server-side web development. Use this tag for questions about programming in the PHP language.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "android", "Android is Google's mobile operating system, used for programming or developing digital devices (Smartphones, Tablets, Automobiles, TVs, Wear, Glass, IoT). For topics related to Android, use Android-specific tags such as android-intent, android-activity, android-adapter, etc.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "html", "HTML (HyperText Markup Language) is the markup language for creating web pages and other information to be displayed in a web browser. Questions regarding HTML should include a minimal reproducible example and some idea of what you're trying to achieve. This tag is rarely used alone and is often paired with [CSS] and [JavaScript].", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "c++", "C++ is a general-purpose programming language. It was originally designed as an extension to C and has a similar syntax, but it is now a completely different language. Use this tag for questions about code (to be) compiled with a C++ compiler.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "css", "CSS (Cascading Style Sheets) is a representation style sheet language used for describing the look and formatting of HTML (HyperText Markup Language), XML (Extensible Markup Language) documents and SVG elements including (but not limited to) colors, layout, fonts, and animations. It also describes how elements should be rendered on screen, on paper, in speech, or on other media.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "ios", "iOS is the mobile operating system running on the Apple iPhone, iPod touch, and iPad. Use this tag [ios] for questions related to programming on the iOS platform. Use the related tags [objective-c] and [swift] for issues specific to those programming languages.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "mysql", "MySQL is a free, open-source Relational Database Management System (RDBMS) that uses Structured Query Language (SQL). DO NOT USE this tag for other DBs such as SQL Server, SQLite etc. Those are different DBs that all use their own dialects of SQL to manage the data.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "sql", "Structured Query Language (SQL) is a language for querying databases. Questions should include code examples, table structure, sample data, and a tag for the DBMS implementation (e.g. MySQL, PostgreSQL, Oracle, MS SQL Server, IBM DB2, etc.) being used. If your question relates solely to a specific DBMS (uses specific extensions/features), use that DBMS's tag instead. Answers to questions tagged with SQL should use ISO/IEC standard SQL.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "r", "R is a free, open-source programming language & software environment for statistical computing, bioinformatics, visualization & general computing. Please use minimal reproducible example(s) others can run using copy & paste. Show desired output. Use dput() for data & specify all non-base packages with library(). Don't embed pictures for data or code, use indented code blocks instead.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "node.js", "Node.js is an event-based, non-blocking, asynchronous I/O runtime that uses Google's V8 JavaScript engine and libuv library. It is used for developing applications that make heavy use of the ability to run JavaScript both on the client as well as on the server side and therefore benefit from the re-usability of code and the lack of context switching.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "reactjs", "React is a JavaScript library for building user interfaces. It uses a declarative, component-based paradigm and aims to be efficient and flexible.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "arrays", "An array is an ordered linear data structure consisting of a collection of elements (values, variables, or references), each identified by one or more indexes. When asking about specific variants of arrays, use these related tags instead: [vector], [arraylist], [matrix]. When using this tag, in a question that is specific to a programming language, tag the question with the programming language being used.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "c", "C is a general-purpose programming language used for system programming (OS and embedded), libraries, games and cross-platform. This tag should be used with general questions concerning the C language, as defined in the ISO 9899 standard (the latest version, 9899:2018, unless otherwise specified — also tag version-specific requests with c89, c99, c11, etc). C is distinct from C++ and it should not be combined with the C++ tag absent a rational reason.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "asp.net", "ASP.NET is a Microsoft web application development framework that allows programmers to build dynamic web sites, web applications and web services. It is useful to use this tag in conjunction with the project type tag e.g. [asp.net-mvc], [asp.net-webforms], or [asp.net-web-api]. Do NOT use this tag for questions about ASP.NET Core - use [asp.net-core] instead.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "json", "JSON (JavaScript Object Notation) is a serializable data interchange format that is a machine and human readable. Do not use this tag for native JavaScript objects or JavaScript object literals. Before you ask a question, validate your JSON using a JSON validator such as JSONLint (https://jsonlint.com).", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, "ruby-on-rails", "Ruby on Rails is an open source full-stack web application framework written in Ruby. It follows the popular MVC framework model and is known for its \"convention over configuration\" approach to application development.", 0);
insert into tag (id, tag, description, num_questions)
VALUES(NULL, ".net", "Do NOT use for questions about .NET Core or .NET 5+ - use [.net-core] instead. The .NET Framework is a software framework designed mainly for the Microsoft Windows operating system. It includes an implementation of the Base Class Library, Common Language Runtime (commonly referred to as CLR), Common Type System (commonly referred to as CTS) and Dynamic Language Runtime. It supports many programming languages, including C#, VB.NET, F# and C++/CLI.", 0);