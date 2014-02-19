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


-- DNA Prep type on request
alter table Request add codeRNAPrepType varchar(10) NULL;
alter table Request add 
  CONSTRAINT `FK_Request_RNAPrepType` FOREIGN KEY `FK_Request_RNAPrepType` (`codeRNAPrepType`)
    REFERENCES `gnomex`.`RNAPrepType` (`codeRNAPrepType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    

-- add foreign key constraint from Request to Lab
alter table Request add 
  CONSTRAINT `FK_Request_Lab` FOREIGN KEY `FK_Request_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
 