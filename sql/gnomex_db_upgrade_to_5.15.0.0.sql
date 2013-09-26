use gnomex;

-- Add approved billing status for credit cards.
insert into BillingStatus (codeBillingStatus, billingStatus, isActive) 
  values ('APPROVEDCC', 'Approved (Credit Card)', 'Y');
  
--New property which is a note that is appended to the "sequencing complete" email that notifies users who to contact if they have
--any further questions about the processing of their request. 
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
  values('analysis_assistance_note', 'If you would like data analysis assistance from the Bioinformatics Shared Resource, please contact them via email at bioinformaticshelp@bio.hci.utah.edu . Please include the sequencing or microarray request number, the aims of the experiment, and any specific analysis questions you would like to have addressed.', 'Note that is sent when sequencing of an experiment is complete notifiying user who to contact if they have further questions', 'N', 1, null);

    
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

alter table Lab add contactAddress2 varchar(200) NULL;
alter table Lab add contactCountry varchar(200) NULL;
        