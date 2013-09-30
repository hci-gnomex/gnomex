use gnomex;

-- Add approved billing status for credit cards.
insert into BillingStatus (codeBillingStatus, billingStatus, isActive) 
  values ('APPROVEDCC', 'Approved (Credit Card)', 'Y');
  
-- New property which is a note that is appended to the "sequencing complete" email that notifies users who to contact if they have
-- any further questions about the processing of their request. 
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

-- Set prices to not null default 0
update Price set unitPrice=0 where unitPrice is null;
update Price set unitPriceExternalAcademic=0 where unitPriceExternalAcademic is null;
update Price set unitPriceExternalCommercial=0 where unitPriceExternalCommercial is null;
alter table gnomex.Price MODIFY unitPrice DECIMAL(6,2) NOT NULL DEFAULT 0;
alter table gnomex.Price MODIFY unitPriceExternalAcademic DECIMAL(6,2) NOT NULL DEFAULT 0;
alter table gnomex.Price MODIFY unitPriceExternalCommercial DECIMAL(6,2) NOT NULL DEFAULT 0;

-- Changes for the new experiment platform logic.
alter table Application add onlyForLabPrepped char(1) not null default 'Y'; 
create table gnomex.ApplicationType (
  codeApplicationType varchar(10) not null,
  applicationType varchar(100),
  PRIMARY KEY (codeApplicationType)
) ENGINE = INNODB;
alter table Application add codeApplicationType varchar(10);
alter table gnomex.Application add CONSTRAINT FK_Application_ApplicationType 
  FOREIGN KEY FK_Application_ApplicationType (codeApplicationType)
    REFERENCES gnomex.ApplicationType (codeApplicationType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
insert into gnomex.ApplicationType values('Illumina', 'Illumina'),
  ('Microarray', 'Microarray'),
  ('QC', 'Sample Quality'),
  ('Other', 'Other');
update Application set codeApplicationType = 'Illumina'
  where codeApplication in ('CHIPSEQ','DMRNASEQ','DNASEQ','TDNASEQ','MRNASEQ','SMRNASEQ','DNAMETHSEQ','MONNUCSEQ','TSCRPTSEQ');
update Application set codeApplicationType = 'Microarray'
  where codeApplication in ('CGH','CHIP','EXON','EXP','METH','MIRNA','SNP','WTRANSCRP');
update Application set codeApplicationType = 'QC'
  where codeApplication in ('BIOAN','QUBIT');
update Application   
  join RequestCategoryApplication on RequestCategoryApplication.codeApplication = Application.codeApplication
  join RequestCategory on RequestCategory.codeRequestCategory=RequestCategoryApplication.codeRequestCategory
  join RequestCategoryType on RequestCategoryType.codeRequestCategoryType = RequestCategory.type
  set codeApplicationType='Illumina'
  where RequestCategoryType.isIllumina = 'Y' and Application.isActive='Y' and codeApplicationType is null;
update Application 
  join RequestCategoryApplication on RequestCategoryApplication.codeApplication = Application.codeApplication
  join RequestCategory on RequestCategory.codeRequestCategory=RequestCategoryApplication.codeRequestCategory
  join RequestCategoryType on RequestCategoryType.codeRequestCategoryType = RequestCategory.type
  set codeApplicationType='Microarray'
  where RequestCategoryType.codeRequestCategoryType='Microarray' and codeApplicationType is null;
update Application
  join RequestCategoryApplication on RequestCategoryApplication.codeApplication = Application.codeApplication
  join RequestCategory on RequestCategory.codeRequestCategory=RequestCategoryApplication.codeRequestCategory
  join RequestCategoryType on RequestCategoryType.codeRequestCategoryType = RequestCategory.type
  set codeApplicationType='QC'
  where RequestCategoryType.codeRequestCategoryType='QC' and codeApplicationType is null;
update Application set codeApplicationType='Other' where codeApplicationType is null;

# Update the help_url and fdt_help properties
update PropertyDictionary set propertyValue = 'http://hci-scrum.hci.utah.edu/gnomexdoc' where propertyName = 'help_url';
update PropertyDictionary set propertyValue = 'http://hci-scrum.hci.utah.edu/gnomexdoc/?page_id=116' where propertyName = 'fdt_help_url';

