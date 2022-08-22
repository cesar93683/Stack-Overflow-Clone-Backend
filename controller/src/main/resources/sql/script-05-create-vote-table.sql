USE `user_tracker`;

DROP TABLE IF EXISTS `vote`;

CREATE TABLE `vote` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vote` varchar(9) NOT NULL,
  `user_id` int NOT NULL,
  `post_id` int NOT NULL,
  `comment_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;