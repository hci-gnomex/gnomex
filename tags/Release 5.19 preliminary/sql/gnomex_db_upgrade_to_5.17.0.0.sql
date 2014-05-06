use gnomex;

-- Make billing account nullable on DiskUsageByMonth
alter table DiskUsageByMonth change idBillingAccount idBillingAccount INT(10) NULL;

-- Procedure to modify columns in audit tables if they exist.
drop procedure if exists ExecuteIfTableExists;
delimiter '//'

create procedure ExecuteIfTableExists(
  IN dbName tinytext,
  IN tableName tinytext,
  IN statement text)
begin
  IF EXISTS (SELECT * FROM information_schema.TABLES WHERE table_name=tableName and table_schema=dbName)
  THEN
    set @ddl=statement;
    prepare stmt from @ddl;
    execute stmt;
  END IF;
end;
//

DROP PROCEDURE IF EXISTS setAppUser//
CREATE PROCEDURE setAppUser( IN userName text)
BEGIN
  SET @userName=userName;
END;
//

delimiter ';'

alter table gnomex.FlowCell add idCoreFacility int(10) NULL;
call ExecuteIfTableExists('gnomex','FlowCell_Audit','alter table gnomex.FlowCell_Audit add idCoreFacility int(10) NULL');
alter table gnomex.FlowCell add CONSTRAINT FK_FlowCell_CoreFacility 
  FOREIGN KEY FK_FlowCell_CoreFacility (idCoreFacility)
    REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

    use gnomex;

-- Add owner only flag to request category
alter table RequestCategory add isOwnerOnly CHAR(1) null;
call ExecuteIfTableExists('gnomex','RequestCategory_Audit','alter table gnomex.RequestCategory_Audit add isOwnerOnly CHAR(1) null');

-- Add VisitLog Table
DROP TABLE IF EXISTS `gnomex`.`VisitLog`;
CREATE TABLE `gnomex`.`VisitLog` (
    `idVisitLog`      INT(10) NOT NULL AUTO_INCREMENT
  , `visitDateTime`   DATETIME NOT NULL
  , `idAppUser`       INT(10) NOT NULL
  , `ipAddress`       VARCHAR(50) NOT NULL DEFAULT 'Unknown'
  , `sessionID`       VARCHAR(255) NOT NULL DEFAULT 'Unknown'
  , PRIMARY KEY (`idVisitLog`)
)
ENGINE = INNODB;

-- New trimAdapter column for Request table
alter table gnomex.Request add trimAdapter char(1) NULL;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table gnomex.Request_Audit add trimAdapter char(1) NULL');

-- New property experiment_file_sample_linking_enabled
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES('experiment_file_sample_linking_enabled', 'Y', 'Enable this property in order to see the Sample Linking button on the Files tab of the experiment edit view', 'N', null, null);
	
-- New property for bio informatics tab on request submission to notify the conditions in which the alignment of sequences is offered 	
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES('request_bio_alignment_note', '(This service is only offered to University of Utah investigators and is performed with no additional charge.)', 'Text for note on request submission bioinformatics tab explaining details required when requesting sequences to be aligned', 'N', null, null);	

-- Property that indicates who should be contacted when analysis assistance has been requested for experiments.	
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES ('contact_email_bioinformatics_analysis_requests', 'bioinformaticshelp@bio.hci.utah.edu', 'Who should be contacted when Analysis assistance is requested on experiments', 'N', null, null);

INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	values('analysis_assistance_group', 'Bioinformatics Shared Resource', 'Name of the group who handles analysis assistance', 'N', null, null);
	
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	values('analysis_assistance_header', 'The following optional services are made available through the Bioinformatics Shared Resource.', 'Header found on bioinformatics tab to notify user who handles analysis assistance requests', 'N', null, null);	


-- *********CONVERSION SCRIPT for sample experiment file table****************

RENAME TABLE `SampleExperimentFile` TO `oldSampleExperimentFile`;
ALTER TABLE	`oldSampleExperimentFile` Drop Foreign key `FK_SampleExperimentFile_Sample`,
Add constraint `FK_SampleExperimentFile_Sample1` Foreign key `FK_SampleExperimentFile_Sample1` (`idSample`)
	References `gnomex`.`Sample` (`idSample`)
	ON DELETE NO ACTION
    ON UPDATE NO ACTION;

CREATE TABLE `gnomex`.`SampleExperimentFile` (
  `idSampleExperimentFile` INT(10) NOT NULL AUTO_INCREMENT,
  `idSample` INT(10),
  `idExpFileRead1` INT(10),
  `idExpFileRead2` INT(10),
  `seqRunNumber` INT(10),
  PRIMARY KEY (`idSampleExperimentFile`),
  UNIQUE KEY `UN_SampleExperimentFile` (idSample, idExpFileRead1, idExpFileRead2),
  CONSTRAINT `FK_SampleExperimentFile_Sample` FOREIGN KEY `FK_SampleExperimentFile_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleExperimentFile_ExperimentFile1` FOREIGN KEY `FK_SampleExperimentFile_ExperimentFile1` (`idExpFileRead1`)
    REFERENCES `gnomex`.`ExperimentFile` (`idExperimentFile`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleExperimentFile_ExperimentFile2` FOREIGN KEY `FK_SampleExperimentFile_ExperimentFile2` (`idExpFileRead2`)
    REFERENCES `gnomex`.`ExperimentFile` (`idExperimentFile`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

INSERT INTO `gnomex`.`SampleExperimentFile` (idSample, idExpFileRead1)
SELECT idSample, idExperimentFile FROM `gnomex`.`oldSampleExperimentFile`
WHERE `gnomex`.`oldSampleExperimentFile`.codeSampleFileType = 'fastqRead1';

UPDATE `gnomex`.`SampleExperimentFile`
JOIN `gnomex`.`oldSampleExperimentFile`
SET `gnomex`.`SampleExperimentFile`.idExpFileRead2 = `gnomex`.`oldSampleExperimentFile`.idExperimentFile
WHERE `gnomex`.`SampleExperimentFile`.idSample = `gnomex`.`oldSampleExperimentFile`.idSample AND `gnomex`.`oldSampleExperimentFile`.codeSampleFileType = 'fastqRead2';

UPDATE `gnomex`.`SampleExperimentFile` SET seqRunNumber = 1;