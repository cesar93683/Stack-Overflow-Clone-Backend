DROP TABLE IF EXISTS question_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email varchar(50) NOT NULL,
  username varchar(20) NOT NULL,
  password varchar(120) NOT NULL,
  reputation int NOT NULL
)  ;

CREATE TABLE question (
  id SERIAL PRIMARY KEY,
  user_id int NOT NULL,
  title varchar(200),
  content varchar(10000) NOT NULL,
  votes int NOT NULL,
  num_answers int NOT NULL,
  answered int NOT NULL,
  created_at TIMESTAMP(0) NOT NULL,
  updated_at TIMESTAMP(0)
 ,
  CONSTRAINT FK_QUESTION_USER
  FOREIGN KEY (user_id)
  REFERENCES users (id)
)  ;

CREATE INDEX FK_QUESTION_USER_idx ON question (user_id);

CREATE TABLE answer (
  id SERIAL PRIMARY KEY,
  user_id int NOT NULL,
  question_id int NOT NULL,
  content varchar(10000) NOT NULL,
  votes int NOT NULL,
  accepted int NOT NULL,
  created_at TIMESTAMP(0) NOT NULL,
  updated_at TIMESTAMP(0)
 ,
  CONSTRAINT FK_ANSWER_USER
  FOREIGN KEY (user_id)
  REFERENCES users (id)
 ,
  CONSTRAINT FK_ANSWER_QUESTION
  FOREIGN KEY (question_id)
  REFERENCES question (id)
)  ;

CREATE INDEX FK_ANSWER_USER_idx ON answer (user_id);
CREATE INDEX FK_ANSWER_QUESTION_idx ON answer (question_id);


CREATE TABLE comment (
  id SERIAL PRIMARY KEY,
  user_id int NOT NULL,
  content varchar(1000) NOT NULL,
  votes int NOT NULL,
  question_id int,
  answer_id int,
  created_at TIMESTAMP(0) NOT NULL
 ,
  CONSTRAINT FK_COMMENT_USER
  FOREIGN KEY (user_id)
  REFERENCES users (id)
 ,
  CONSTRAINT FK_COMMENT_QUESTION
  FOREIGN KEY (question_id)
  REFERENCES question (id)
 ,
  CONSTRAINT FK_COMMENT_ANSWER
  FOREIGN KEY (answer_id)
  REFERENCES answer (id)
)  ;

CREATE INDEX FK_COMMENT_USER_idx ON comment (user_id);
CREATE INDEX FK_COMMENT_QUESTION_idx ON comment (question_id);
CREATE INDEX FK_COMMENT_ANSWER_idx ON comment (answer_id);

CREATE TABLE vote (
  id SERIAL PRIMARY KEY,
  vote int NOT NULL,
  user_id int NOT NULL,
  question_id int,
  answer_id int,
  comment_id int
 ,
  CONSTRAINT FK_VOTE_USER
  FOREIGN KEY (user_id)
  REFERENCES users (id)
 ,
  CONSTRAINT FK_VOTE_QUESTION
  FOREIGN KEY (question_id)
  REFERENCES question (id)
 ,
  CONSTRAINT FK_VOTE_ANSWER
  FOREIGN KEY (answer_id)
  REFERENCES answer (id)
 ,
  CONSTRAINT FK_VOTE_COMMENT
  FOREIGN KEY (comment_id)
  REFERENCES comment (id)
)  ;

CREATE INDEX FK_VOTE_USER_idx ON vote (user_id);
CREATE INDEX FK_VOTE_QUESTION_idx ON vote (question_id);
CREATE INDEX FK_VOTE_ANSWER_idx ON vote (answer_id);
CREATE INDEX FK_VOTE_COMMENT_idx ON vote (comment_id);

CREATE TABLE tag (
  id SERIAL PRIMARY KEY,
  tag varchar(20) NOT NULL,
  description varchar(5000) NOT NULL,
  num_questions int NOT NULL
)  ;

CREATE TABLE question_tag (
	question_id int NOT NULL,
    tag_id int NOT NULL,
    PRIMARY KEY (question_id, tag_id),
	CONSTRAINT FK_QUESTION
    FOREIGN KEY (question_id)
    REFERENCES question (id),
	CONSTRAINT FK_TAG
    FOREIGN KEY (tag_id)
    REFERENCES tag (id)
);

insert into tag (id, tag, description, num_questions)
VALUES(DEFAULT, 'javascript', 'For questions regarding programming in ECMAScript (JavaScript/JS) and its various dialects/implementations (excluding ActionScript). Note JavaScript is NOT the same as Java!', 0);
insert into tag (id, tag, description, num_questions)
VALUES(DEFAULT, 'java', 'Java is a high-level object-oriented programming language. Use this tag when you''re having problems using or understanding the language itself. This tag is frequently used alongside other tags for libraries and/or frameworks used by Java developers.', 0);
insert into tag (id, tag, description, num_questions)
VALUES(DEFAULT, 'python', 'Python is a multi-paradigm, dynamically typed, multi-purpose programming language. It is designed to be quick to learn, understand, and use, and enforces a clean and uniform syntax. Please note that Python 2 is officially out of support as of 2020-01-01. For version-specific Python questions, add the [python-2.7] or [python-3.x] tag. When using a Python variant (e.g. Jython, PyPy) or library (e.g. Pandas, NumPy), please include it in the tags.', 0);
insert into tag (id, tag, description, num_questions)
VALUES(DEFAULT, 'android', 'Android is Google''s mobile operating system, used for programming or developing digital devices (Smartphones, Tablets, Automobiles, TVs, Wear, Glass, IoT). For topics related to Android, use Android-specific tags such as android-intent, android-activity, android-adapter, etc.', 0);