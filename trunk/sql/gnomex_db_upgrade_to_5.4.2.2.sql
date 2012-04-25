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


