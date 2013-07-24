use gnomex;

SET foreign_key_checks = 0;

--
-- Table for new Instrument dictionary
--
DROP TABLE IF EXISTS `gnomex`.`Instrument`;
CREATE TABLE `gnomex`.`Instrument` (
  `idInstrument` INT(10) NOT NULL AUTO_INCREMENT,
  `instrument` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idInstrument`)
)
ENGINE = INNODB;

-- 
-- New Columns for FlowCell
--
alter table FlowCell add column runNumber int(10) NULL;
alter table FlowCell add column idInstrument int(10) NULL;
alter table FlowCell add column side char(1) NULL;

--
-- New column for FlowCellChannel
--
alter table FlowCellChannel add column q30Percent DECIMAL(4,3) NULL;

--
-- New canUploadData column for RequestCollaborator, AnalysisCollaborator
--
alter table RequestCollaborator add column canUploadData char(1) null;
alter table AnalysisCollaborator add column canUploadData char(1) null;
