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

INSERT INTO `gnomex`.`Instrument`(`idInstrument`, `instrument`, `isActive`)
VALUES (1, 'SN141', 'Y');

-- 
-- New Columns for FlowCell
--
alter table FlowCell add column runNumber int(10) NULL;
alter table FlowCell add idInstrument int(10) NULL;
alter table FlowCell add side char(1) NULL;
alter table FlowCell add numberSequencingCyclesActual int(10) NULL;
