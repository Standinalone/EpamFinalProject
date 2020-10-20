-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema epam
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema epam
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `epam` DEFAULT CHARACTER SET utf8 ;
USE `epam` ;

DROP TABLE IF EXISTS Users; 
DROP TABLE IF EXISTS Roles; 
DROP TABLE IF EXISTS Statuses; 
DROP TABLE IF EXISTS Courses; 
DROP TABLE IF EXISTS Topics; 
DROP TABLE IF EXISTS Tokens; 
DROP TABLE IF EXISTS Courses_has_Users;
-- -----------------------------------------------------
-- Table `epam`.`Roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `epam`.`Roles` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `epam`.`Statuses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `epam`.`Statuses` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;




-- -----------------------------------------------------
-- Table `epam`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `epam`.`Users` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `blocked` TINYINT DEFAULT false,
  `role_id` INT DEFAULT 0,
  `login` VARCHAR(45) NOT NULL UNIQUE,
  `password` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL DEFAULT '',
  `surname` VARCHAR(45) NOT NULL DEFAULT '',
  `patronym` VARCHAR(45) NOT NULL DEFAULT '',
  `email` VARCHAR(45) NOT NULL DEFAULT '',
  `enabled` TINYINT DEFAULT false,
  PRIMARY KEY (`id`),
  INDEX `fk_Users_Roles1_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_Users_Roles1`
    FOREIGN KEY (`role_id`)
    REFERENCES `epam`.`Roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `epam`.`Topics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `epam`.`Topics` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `epam`.`Courses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `epam`.`Courses` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `start_date` DATETIME NULL,
  `end_date` DATETIME NULL,
  `status_id` INT NOT NULL,
  `topic_id` INT NOT NULL,
  `lecturer_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Courses_Topic1_idx` (`topic_id` ASC) VISIBLE,
  INDEX `fk_Courses_Users1_idx` (`lecturer_id` ASC) VISIBLE,
  INDEX `fk_Courses_Status1_idx` (`status_id` ASC) VISIBLE,
  CONSTRAINT `fk_Courses_Topic1`
    FOREIGN KEY (`topic_id`)
    REFERENCES `epam`.`Topics` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Courses_Status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `epam`.`Statuses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Courses_Users1`
    FOREIGN KEY (`lecturer_id`)
    REFERENCES `epam`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `epam`.`Tokens`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `epam`.`Tokens` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT UNSIGNED NOT NULL,
  `token` VARCHAR(64) NOT NULL,
  `expiry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Tokens_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_Tokens_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `epam`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `epam`.`Courses_has_Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `epam`.`Courses_has_Users` (
  `course_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  `grade` INT UNSIGNED NULL,
  `registered` TINYINT NOT NULL,
  PRIMARY KEY (`user_id`, `course_id`),
  INDEX `fk_Courses_has_Users_Users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_Courses_has_Users_Courses_idx` (`course_id` ASC) VISIBLE,
  CONSTRAINT `fk_Courses_has_Users_Courses`
    FOREIGN KEY (`course_id`)
    REFERENCES `epam`.`Courses` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Courses_has_Users_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `epam`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
