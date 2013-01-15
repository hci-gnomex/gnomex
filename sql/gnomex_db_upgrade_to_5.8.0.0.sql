use gnomex;

-- New create date for FileExperiment
alter table ExperimentFile add createDate Date;

-- New create date for AnalysisFile
alter table AnalysisFile add createDate Date;

-- Create diskusagebymonth table
CREATE TABLE `gnomex`.`DiskUsageByMonth` (
  idDiskUsageByMonth INT(10) NOT NULL AUTO_INCREMENT,
  idLab INT(10) NOT NULL,
  `asOfDate` datetime NOT NULL,
  `lastCalcDate` datetime NOT NULL,
  `totalAnalysisDiskSpace` DECIMAL(16, 0) NOT NULL,
  `assessedAnalysisDiskSpace` DECIMAL(16, 0) NOT NULL,
  `totalExperimentDiskSpace` DECIMAL(16, 0) NOT NULL,
  `assessedExperimentDiskSpace` DECIMAL(16, 0) NOT NULL,
  idBillingPeriod INT(10) NOT NULL,
  idBillingAccount INT(10) NULL,
  idCoreFacility INT(10) NOT NULL,
  PRIMARY KEY (`idDiskUsageByMonth`),
  CONSTRAINT `FK_DiskUsageByMonth_Lab` FOREIGN KEY  (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_DiskUsageByMonth_CoreFacility` FOREIGN KEY  (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_DiskUsageByMonth_BillingAccount` FOREIGN KEY  (`idBillingAccount`)
    REFERENCES `gnomex`.`BillingAccount` (`idBillingAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_DiskUsageByMonth_BillingPeriod` FOREIGN KEY  (`idBillingPeriod`)
    REFERENCES `gnomex`.`BillingPeriod` (`idBillingPeriod`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  UNIQUE KEY `UN_InternalAccountFieldsConfiguration` (idCoreFacility, idBillingPeriod, idLab)
)
ENGINE = INNODB;

--Billing Item may not have an idRequest and if not will have an idDiskUsageByMonth
ALTER TABLE BillingItem MODIFY idRequest Integer;
alter table BillingItem add idDiskUsageByMonth Integer;
alter table BillingItem add
  CONSTRAINT `FK_BillingItem_DiskUsageByMonth` FOREIGN KEY `FK_BillingItem_DiskUsageByMonth` (`idDiskUsageByMonth`)
    REFERENCES `gnomex`.`DiskUsageByMonth` (`idDiskUsageByMonth`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

 --Flag on request category to exclude category from auto delete
 alter table RequestCategory add refrainFromAutoDelete CHAR(1);
 update RequestCategory set refrainFromAutoDelete='Y';
 