use gnomex;

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

delimiter ';'

-- Add unique constraint
alter table SampleExperimentFile add constraint UN_SampleExperimentFile UNIQUE (idSample, idExperimentFile);

-- Categorize SampleType by Nucleotide type
create table gnomex.NucleotideType (
  codeNucleotideType varchar(50), 
  PRIMARY KEY(codeNucleotideType)) ;
insert into NucleotideType values('RNA');
insert into NucleotideType values('DNA');
alter table gnomex.SampleType add codeNucleotideType varchar(50) NULL;
update SampleType set codeNucleotideType='DNA';
update SampleType set codeNucleotideType='RNA' where sampleType like '%RNA%';
alter table gnomex.SampleType add CONSTRAINT FK_SampleType_NucleotideType 
  FOREIGN KEY FK_SampleType_NucleotideType (codeNucleotideType)
    REFERENCES gnomex.NucleotideType (codeNucleotideType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

-- Add sort order to application theme
alter table ApplicationTheme add sortOrder INT(10) null;
update ApplicationTheme set sortOrder=idApplicationTheme;

-- Add DNA prep type
DROP TABLE IF EXISTS `gnomex`.`DNAPrepType`;
CREATE TABLE `gnomex`.`DNAPrepType` (
  `codeDNAPrepType` VARCHAR(10) NOT NULL,
  `dnaPrepType` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeDNAPrepType`)
)
ENGINE = INNODB;

-- Samples per batch on Application
alter table Application add samplesPerBatch int(10) NULL;

-- DNA Prep type on request
alter table Request add codeDNAPrepType varchar(10) NULL;
alter table Request add 
  CONSTRAINT `FK_Request_DNAPrepType` FOREIGN KEY `FK_Request_DNAPrepType` (`codeDNAPrepType`)
    REFERENCES `gnomex`.`DNAPrepType` (`codeDNAPrepType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- Sequenom application type
insert into gnomex.ApplicationType values('Sequenom', 'Sequenom');

-- Bioinformatics assist flag
alter table Request add bioinformaticsAssist char(1) null;


-- Text for link 
INSERT INTO `gnomex`.`PropertyDictionary` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) VALUES
('request_work_auth_link_text','Submit Work Authorization','Text for the link on the experiment setup tab to submit a work authorization', 'N');

-- Text for details on bioinformatics tab
INSERT INTO `gnomex`.`PropertyDictionary` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) VALUES
('request_bio_analysis_note','(Extra charges may apply)','Text for note on request submission bioinformatics tab explaining details required when requesting help from the bio core', 'N');

-- New property contact_email_manage_sample_file_link
insert into `gnomex`.`PropertyDictionary` (`propertyname`,`propertyValue`,`propertyDescription`,`forServerOnly`) values
('contact_email_manage_sample_file_link', '', 'Email address of who should be notified when a sample experiment file is deleted from the database', 'Y');

-- new columns for pre-pooled samples.
alter table Request add hasPrePooledLibraries char(1) null;
alter table Request add numPrePooledTubes int(10) null;
