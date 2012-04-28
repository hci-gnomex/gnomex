use gnomex;

--
-- New columns for Chromatogram, Plate, InstrumentRun, PlateWell, Sample
--
alter table Chromatogram add column releaseDate DATETIME NULL;

alter table Plate add column quadrant INT(10) NULL;
alter table Plate add column createDate DATETIME NULL;
alter table Plate add column comments VARCHAR(200) NULL;
alter table Plate add column label VARCHAR(50) NULL;
alter table Plate add column codeReactionType VARCHAR(10) NULL;
alter table Plate add column creator VARCHAR(50) NULL;
alter table Plate add column codeSealType VARCHAR(10) NULL;

alter table InstrumentRun add column createDate DATETIME NULL;
alter table InstrumentRun add column codeInstrumentRunStatus VARCHAR(10) NULL;
alter table InstrumentRun add column comments VARCHAR(200) NULL;
alter table InstrumentRun add column label VARCHAR(100) NULL;
alter table InstrumentRun add column codeReactionType VARCHAR(10) NULL;
alter table InstrumentRun add column creator VARCHAR(50) NULL;
alter table InstrumentRun add column codeSealType VARCHAR(10) NULL;

alter table PlateWell add column createDate DATETIME NULL;
alter table PlateWell add column codeReactionType VARCHAR(10) NULL;

alter table Sample add column redoFlag CHAR(1) NULL;


--
-- New tables
--

DROP TABLE IF EXISTS `gnomex`.`InstrumentRunStatus`;
CREATE TABLE `gnomex`.`InstrumentRunStatus` (
  `codeInstrumentRunStatus` VARCHAR(10) NOT NULL,
  `instrumentRunStatus` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeInstrumentRunStatus`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ReactionType`;
CREATE TABLE `gnomex`.`ReactionType` (
  `codeReactionType` VARCHAR(10) NOT NULL,
  `reactionType` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeReactionType`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SealType`;
CREATE TABLE `gnomex`.`SealType` (
  `codeSealType` VARCHAR(10) NOT NULL,
  `sealType` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeSealType`)
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`RequestStatus`;
CREATE TABLE `gnomex`.`RequestStatus` (
  `codeRequestStatus` VARCHAR(10) NOT NULL,
  `requestStatus` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeRequestStatus`)
)
ENGINE = INNODB;


--
-- Property Dictionary core facility
--
alter table PropertyDictionary add idCoreFacility INT(10) NULL;

--
-- Core facility isactive flag
--
alter table CoreFacility add isActive CHAR(1) NOT NULL DEFAULT 'Y';

--
-- Populate default core facility
--
INSERT INTO `gnomex`.`CoreFacility`(`idCoreFacility`, `facilityName`, `isActive`) VALUES (1, 'DEFAULT', 'Y');

--
-- Core Facility User
--
DROP TABLE IF EXISTS `gnomex`.`CoreFacilityUser`;
CREATE TABLE `gnomex`.`CoreFacilityUser` (
  `idCoreFacility` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  PRIMARY KEY (`idCoreFacility`, `idAppUser`),
  CONSTRAINT `FK_CoreFacilityUser_AppUser` FOREIGN KEY  (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CoreFacilityUser_CoreFacility` FOREIGN KEY  (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

INSERT INTO `gnomex`.`CoreFacilityUser`(`idCoreFacility`, `idAppUser`) select 1, idAppUser from AppUser;

--
-- Super Admin Role
--
INSERT INTO `gnomex`.`UserPermissionKind`(`codeUserPermissionKind`, `userPermissionKind`, `isActive`) 
values ('SUPER','Super','Y');

UPDATE `gnomex`.`AppUser` SET codeUserPermissionKind='SUPER' WHERE codeUserPermissionKind='ADMIN'; 

--
-- Rename Price.unitPriceExternal
--
ALTER TABLE `gnomex`.`Price` change column `unitPriceExternal` `unitPriceExternalAcademic` DECIMAL(6, 2) NULL;