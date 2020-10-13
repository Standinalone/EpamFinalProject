USE `epam`;

-- TRUNCATE TABLE `Users`;
-- TRUNCATE TABLE `Roles`;
-- TRUNCATE TABLE `Statuses`;
-- TRUNCATE TABLE `Courses`;
-- TRUNCATE TABLE `Topics`;
-- TRUNCATE TABLE `Tokens`;
-- TRUNCATE TABLE `Courses_has_Users`;

INSERT INTO `Roles` (`id`, `name`) VALUES(1, "USER");
INSERT INTO `Roles` (`id`, `name`) VALUES(2, "ADMIN");
INSERT INTO `Roles` (`id`, `name`) VALUES(3, "LECTURER");

INSERT INTO `Users` VALUES(1, false, 2, "admin",  md5("admin"), "Admin", "Adminov", "Adminovich", "admin@gmail.com", true);
INSERT INTO `Users` VALUES(2, false, 3, "lecturer",  md5("lecturer"), "Lecturer", "Lecturerov", "Lecturerovich", "lecturer@gmail.com", true);
INSERT INTO `Users` VALUES(3, false, 1, "user",  md5("user"), "User", "Userov", "Userovich", "user@gmail.com", true);
INSERT INTO `Users` VALUES(4, true, 1, "blocked_user",  md5("blocked_user"), "BlockedUser", "BlockedUserov", "BlockedUserovich", "blockeduser@gmail.com", true);

INSERT INTO `Topics` VALUES (1, "Programming");
INSERT INTO `Topics` VALUES (2, "Computer Science");
INSERT INTO `Topics` VALUES (3, "Languages");
INSERT INTO `Topics` VALUES (4, "Math");

INSERT INTO `Statuses` VALUES (1, "NOT_STARTED");
INSERT INTO `Statuses` VALUES (2, "IN_PROGRESS");
INSERT INTO `Statuses` VALUES (3, "FINISHED");

INSERT INTO `Courses` VALUES (1, "JAVA Basics", "2020.07.15", "2020.08.15", 1, 1, 2);
INSERT INTO `Courses` VALUES (2, "JAVA EE", "2020.08.20", "2020.10.20", 1, 1, 2);
INSERT INTO `Courses` VALUES (3, "Machine Learning", "2020.02.01", "2021.02.01", 1, 2, 2);
INSERT INTO `Courses` VALUES (4, "Computer Architecture", "2019.05.01", "2019.06.01", 1, 2, 2);
INSERT INTO `Courses` VALUES (5, "English", "2016.02.01", "2016.02.12", 1, 3, 2);
INSERT INTO `Courses` VALUES (6, "French", "2016.02.01", "2016.12.01", 1, 3, 2);
INSERT INTO `Courses` VALUES (7, "Linear Algebra", "2016.01.02", "2016.12.02", 1, 4, 2);
INSERT INTO `Courses` VALUES (8, "Mathematical Analysis", "2016.01.02", "2016.12.02", 1, 4, 2);

INSERT INTO `Courses_has_Users` VALUES (1, 1, 0, true);
INSERT INTO `Courses_has_Users` VALUES (2, 1, 0, true);
INSERT INTO `Courses_has_Users` VALUES (3, 1, 0, true);
INSERT INTO `Courses_has_Users` VALUES (4, 1, 0, true);
INSERT INTO `Courses_has_Users` VALUES (5, 1, 0, false);
INSERT INTO `Courses_has_Users` VALUES (6, 1, 0, false);
INSERT INTO `Courses_has_Users` VALUES (7, 1, 0, false);

#INSERT INTO `Tokens` VALUES (1, 15,  "fcc6678c-2a0b-49b8-a28d-e74f7cc256a7", "2020.10.01");

