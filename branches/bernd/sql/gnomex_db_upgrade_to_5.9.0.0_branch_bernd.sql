use gnomex;

-- New property for hostname of MetrixServer
insert into `gnomex`.`PropertyDictionary`(`propertyName`, `propertyValue`, `propertyDescription`, `forServerOnly`) 
  values('metrix_server_host','localhost','Hostname or IP on which the Illumina statistics Metrix Server is running.','Y');
  
-- New property for port of MetrixServer
insert into `gnomex`.`PropertyDictionary`(`propertyName`, `propertyValue`, `propertyDescription`, `forServerOnly`)
  values('metrix_server_port','12345','Port (>1024) on which the Illumina statistics Metrix Server is running.','Y');
  
-- Add RequestCategory column for refrainFromAutoDelete (Adjust required amount of chars)
ALTER TABLE `gnomex`.`RequestCategory` ADD refrainFromAutoDelete VARCHAR(50);

-- New table to hold News items in
DROP TABLE IF EXISTS `gnomex`.`NewsItem`;
CREATE TABLE `gnomex`.`NewsItem` (
	`idNewsItem` INT(10) NOT NULL AUTO_INCREMENT,
	`idSubmitter` INT(10) NOT NULL,
	`idCoreSender` INT(10) NOT NULL,
	`idCoreTarget` INT(10) NULL,
	`title` VARCHAR(200) NOT NULL,
	`message` VARCHAR(4000) NOT NULL,
	`date` DATETIME NULL,
      PRIMARY KEY (`idNewsItem`),
    CONSTRAINT `FK_NewsItem_Submitter` FOREIGN KEY `FK_NewsItem_Submitter` (`idSubmitter`)
       REFERENCES `gnomex`.`AppUser` (`idAppUser`)
       ON DELETE NO ACTION
       ON UPDATE NO ACTION,
)
ENGINE = INNODB;

-- New table to hold notification items in
DROP TABLE IF EXISTS `gnomex`.`Notification`;
CREATE TABLE `gnomex`.`Notification` (
	`idNotification` INT(10) NOT NULL AUTO_INCREMENT,
	`idUserTarget` INT(10) NOT NULL,
	`idLabTarget` INT(10) NULL,
	`sourceType` VARCHAR(20) NOT NULL,
	`message` VARCHAR(250) NULL,
	`date` DATETIME NULL,
	`expID` INT(10) NULL,
	`type`	VARCHAR(25) NULL, 
	PRIMARY KEY (`idNotification`)
)
ENGINE = INNODB;

-- Create FAQ table
CREATE TABLE `gnomex`.`FAQ` (
  `idFAQ` INT(10) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(300) NOT NULL,
  `url` VARCHAR(500) NOT NULL,
  `views` INT(10) NULL,
  PRIMARY KEY (`idFAQ`)
)
ENGINE = INNODB;