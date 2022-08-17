USE `user_tracker`;

DROP TABLE IF EXISTS `post_response`;

CREATE TABLE `post_response` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `post_id` int NOT NULL,
  `content` varchar(500) NOT NULL,
  `votes` int NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;