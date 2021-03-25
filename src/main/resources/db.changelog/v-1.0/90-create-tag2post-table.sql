CREATE TABLE `tag2post` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `FKjou6suf2w810t2u3l96uasw3r` (`tag_id`),
  CONSTRAINT `FKjou6suf2w810t2u3l96uasw3r` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKpjoedhh4h917xf25el3odq20i` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8

GO

INSERT INTO `tag2post` (`post_id`, `tag_id`)
    VALUES (1, 1),
           (1, 2),
           (3, 3),
           (3, 4),
           (3, 1),
           (6, 2),
           (7, 3),
           (8, 3);
