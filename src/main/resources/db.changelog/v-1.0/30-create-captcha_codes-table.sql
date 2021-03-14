CREATE TABLE `captcha_codes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `time` datetime NOT NULL,
  `secret_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

GO

INSERT INTO `captcha_codes` (`id`, `code`, `time`, `secret_code`)
    VALUES (1, '123', '2017-11-15 15:30:14.332', '123'),
           (2, '321', '2017-11-15 15:30:14.332', '321');
