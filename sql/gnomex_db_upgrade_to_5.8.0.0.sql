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
  idBillingAccount INT(10) NOT NULL,
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
    
-- Add new column to GenomeBuild table and populate known fields

ALTER TABLE gnomex.GenomeBuild add column igvName VARCHAR(50) NULL;

UPDATE gnomex.GenomeBuild set IGVName='tair8' WHERE idGenomeBuild = 9;
UPDATE gnomex.GenomeBuild set IGVName='WS220' WHERE idGenomeBuild = 30;
UPDATE gnomex.GenomeBuild set IGVName='ce6' WHERE idGenomeBuild = 5;
UPDATE gnomex.GenomeBuild set IGVName='galGal3' WHERE idGenomeBuild = 20;
UPDATE gnomex.GenomeBuild set IGVName='dm3' WHERE idGenomeBuild = 18;
UPDATE gnomex.GenomeBuild set IGVName='hg17' WHERE idGenomeBuild = 21;
UPDATE gnomex.GenomeBuild set IGVName='hg18' WHERE idGenomeBuild = 2;
UPDATE gnomex.GenomeBuild set IGVName='hg19' WHERE idGenomeBuild = 8;
UPDATE gnomex.GenomeBuild set IGVName='mm10' WHERE idGenomeBuild = 44;
UPDATE gnomex.GenomeBuild set IGVName='mm9' WHERE idGenomeBuild = 4;
UPDATE gnomex.GenomeBuild set IGVName='mm8' WHERE idGenomeBuild = 3;
UPDATE gnomex.GenomeBuild set IGVName='rn5' WHERE idGenomeBuild = 33;
UPDATE gnomex.GenomeBuild set IGVName='rn4' WHERE idGenomeBuild = 22;
UPDATE gnomex.GenomeBuild set IGVName='xenTro2' WHERE idGenomeBuild = 27;
UPDATE gnomex.GenomeBuild set IGVName='sacCer2' WHERE idGenomeBuild = 23;
UPDATE gnomex.GenomeBuild set IGVName='danRer5' WHERE idGenomeBuild = 7;
UPDATE gnomex.GenomeBuild set IGVName='danRer6' WHERE idGenomeBuild = 6;
UPDATE gnomex.GenomeBuild set IGVName='danRer7' WHERE idGenomeBuild = 12;

