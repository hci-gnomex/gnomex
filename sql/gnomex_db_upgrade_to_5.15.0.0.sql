use gnomex;

-- Add approved billing status for credit cards.
insert into BillingStatus (codeBillingStatus, billingStatus, isActive) 
  values ('APPROVEDCC', 'Approved (Credit Card)', 'Y');
    
-- New table to associate ExperimentFile and Sample.  Will add front end code to populate this in future release.
-- for now can be populated manually
CREATE TABLE gnomex.SampleFileType(
  codeSampleFileType varchar(10),
  description varchar(200) NULL,
 PRIMARY KEY (codeSampleFileType) 
 ) ENGINE = INNODB;
insert into gnomex.SampleFileType(codeSampleFileType, description)
  values('fastqRead1', 'fastqRead1'),
        ('fastqRead2', 'fastqRead2'),
        ('BAM', 'BAM'),
        ('Other', 'Other');
CREATE TABLE `gnomex`.`SampleExperimentFile` (
  `idSampleExperimentFile` INT(10) NOT NULL AUTO_INCREMENT,
  `idSample` INT(10),
  `idExperimentFile` INT(10),
  `codeSampleFileType` varchar(10),
  PRIMARY KEY (`idSampleExperimentFile`),
  CONSTRAINT `FK_SampleExperimentFile_Sample` FOREIGN KEY `FK_SampleExperimentFile_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleExperimentFile_ExperimentFile` FOREIGN KEY `FK_SampleExperimentFile_ExperimentFile` (`idExperimentFile`)
    REFERENCES `gnomex`.`ExperimentFile` (`idExperimentFile`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleExperimentFile_SampleFileType` FOREIGN KEY `FK_SampleExperimentFile_SampleFileType` (`codeSampleFileType`)
    REFERENCES `gnomex`.`SampleFileType` (`codeSampleFileType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;
        