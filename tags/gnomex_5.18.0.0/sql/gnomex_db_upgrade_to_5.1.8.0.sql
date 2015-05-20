-- New table for logging file uploads and downloads
DROP TABLE IF EXISTS `gnomex`.`TransferLog`;
CREATE TABLE `gnomex`.`TransferLog` (
  `idTransferLog` INT(10) NOT NULL AUTO_INCREMENT,
  `transferType` VARCHAR(10) NOT NULL,
  `transferMethod` VARCHAR(10) NOT NULL,
  `startDateTime` DATETIME NULL,
  `endDateTime` DATETIME NULL,
  `fileName` VARCHAR(1000) NOT NULL,
  `fileSize` DECIMAL(14,0) NULL,
  `performCompression` CHAR(1) NULL DEFAULT 'N',
  `idAnalysis` INT(10)  NULL ,
  `idRequest` INT(10)  NULL ,
  `idLab` INT(10)  NULL ,
  PRIMARY KEY (`idTransferLog`),
  CONSTRAINT `FK_TransferLog_Request` FOREIGN KEY `FK_TransferLog_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_TransferLog_Analysis` FOREIGN KEY `FK_TransferLog_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_TransferLog_Lab` FOREIGN KEY `FK_TransferLog_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;