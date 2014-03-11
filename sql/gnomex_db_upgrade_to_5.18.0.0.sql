use gnomex;

-- Add RNA prep type
DROP TABLE IF EXISTS `gnomex`.`RNAPrepType`;
CREATE TABLE `gnomex`.`RNAPrepType` (
  `codeRNAPrepType` VARCHAR(10) NOT NULL,
  `rnaPrepType` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeRNAPrepType`)
)
ENGINE = INNODB;


-- RNA Prep type on request
alter table Request add codeRNAPrepType varchar(10) NULL;
alter table Request add 
  CONSTRAINT `FK_Request_RNAPrepType` FOREIGN KEY `FK_Request_RNAPrepType` (`codeRNAPrepType`)
    REFERENCES `gnomex`.`RNAPrepType` (`codeRNAPrepType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table Request add includeBisulfideConversion CHAR(1) null;  

-- add foreign key constraint from Request to Lab
alter table Request add 
  CONSTRAINT `FK_Request_Lab` FOREIGN KEY `FK_Request_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
 
DROP TABLE IF EXISTS `gnomex`.`ContextSensitiveHelp`;
CREATE TABLE `gnomex`.`ContextSensitiveHelp` (
  `idContextSensitiveHelp` INT(10) NOT NULL AUTO_INCREMENT, 
  `context1` VARCHAR(100) NOT NULL,
  `context2` VARCHAR(100),
  `context3` VARCHAR(100),
  `helpText` VARCHAR(10000) NULL,
  `toolTipText` VARCHAR(10000) NULL,
  PRIMARY KEY (`idContextSensitiveHelp`)
)
ENGINE = INNODB;
 