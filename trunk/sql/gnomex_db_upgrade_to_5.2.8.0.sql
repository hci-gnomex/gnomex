CREATE TABLE `gnomex`.`ExperimentFile` (
  `idExperimentFile` INT(10) NOT NULL AUTO_INCREMENT,
  `fileName` VARCHAR(2000) NULL,
  `fileSize` DECIMAL(14,0) NULL,
  `idRequest` INT(10) NULL,
  PRIMARY KEY (`idExperimentFile`),
  CONSTRAINT `FK_ExpeirmentFile_Request` FOREIGN KEY `FK_RequestFile_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


alter table gnomex.AnalysisFile add column fileSize DECIMAL(14,0) null;