USE `epam`;

INSERT INTO `Roles` (`id`, `name`) VALUES(1, "user");
INSERT INTO `Roles` (`id`, `name`) VALUES(2, "admin");
INSERT INTO `Roles` (`id`, `name`) VALUES(3, "lecturer");

INSERT INTO `Users` VALUES(1, false, 2, "admin", "123", "admin", "admin", "admin", "admin@gmail.com");
#INSERT INTO `Users` (`id`, `blocked`, `role_id`, `login`, `password`) VALUES(1, false, 0, "user", "123");
INSERT INTO `Users` VALUES(2, false, 3, "lecturer", "123", "Name", "Surname", "Patronym", "lecturer@gmail.com");

INSERT INTO `Topics` VALUES (1, "C#");
INSERT INTO `Topics` VALUES (2, "JAVA");

INSERT INTO `Courses` VALUES (1, "Basics", "05.05.2020", "06.06.2020", true, 1, 2);
INSERT INTO `Courses` VALUES (2, "Logical expressions", "06.07.2020", "07.07.2020", true, 1, 2);

INSERT INTO `Courses_has_Users` VALUES (1, 1, 0, true);
INSERT INTO `Courses_has_Users` VALUES (2, 1, 0, true);
