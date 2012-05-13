use gnomex;
--
-- SampleDropOffLocation 
--
DROP TABLE IF EXISTS `gnomex`.`SampleDropOffLocation`;
CREATE TABLE `gnomex`.`SampleDropOffLocation` (
  `idSampleDropOffLocation` INT(10) NOT NULL AUTO_INCREMENT,
  `sampleDropOffLocation`   VARCHAR(200) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSampleDropOffLocation`)
)
ENGINE = INNODB;

alter table Request add column idSampleDropOffLocation INT(10) NULL;
alter table Request add   
CONSTRAINT `FK_Request_SampleDropOffLocation` FOREIGN KEY `FK_Request_SampleDropOffLocation` (`idSampleDropOffLocation`)
    REFERENCES SampleDropOffLocation (`idSampleDropOffLocation`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

--
-- PlateType 
--
DROP TABLE IF EXISTS `gnomex`.`PlateType`;
CREATE TABLE `gnomex`.`PlateType` (
  `codePlateType` VARCHAR(10) NOT NULL,
  `plateTypeDescription`   VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codePlateType`)
)
ENGINE = INNODB;
INSERT INTO `gnomex`.`PlateType`(`codePlateType`, `plateTypeDescription`, `isActive`) values('REACTION', 'Reaction plate', 'Y');
INSERT INTO `gnomex`.`PlateType`(`codePlateType`, `plateTypeDescription`, `isActive`) values('SOURCE', 'Source plate', 'Y');
Alter Table Plate add column codePlateType VARCHAR(10) NOT NULL DEFAULT 'REACTION';
alter table Plate add   
CONSTRAINT `FK_Plate_PlateType` FOREIGN KEY `FK_Plate_PlateType` (`codePlateType`)
    REFERENCES PlateType (`codePlateType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
    
-- Populate new property to turn on data track feature
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('datatrack_supported','Y','Indicates if data track features is supported', 'N');

