DROP TABLE IF EXISTS `post_vote`;

CREATE TABLE `post_vote` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vote` varchar(9) NOT NULL,
  `user_id` int NOT NULL,
  `post_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;