use gnomex;

-- Add idReleaser column to the Chromatogram table
alter table Chromatogram add column idReleaser int(10) null;

-- Add lane column to the chromatogram table
alter table Chromatogram add column lane int(10) null;

-- Add idSubmitter to Analysis
alter table gnomex.Analysis add column idSubmitter int(10) null;
alter table gnomex.Analysis add
CONSTRAINT FK_Analysis_AppUser1 FOREIGN KEY  (idSubmitter)
    REFERENCES gnomex.AppUser (idAppUser)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

-- Add idSubmitter to Request
alter table gnomex.Request add column idSubmitter int(10) null;
alter table gnomex.Request add
CONSTRAINT FK_Request_AppUser1 FOREIGN KEY  (idSubmitter)
    REFERENCES gnomex.AppUser (idAppUser)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- This constraint should have been added to Request previously
alter table gnomex.Request add   
  CONSTRAINT FK_Request_AppUser FOREIGN KEY  (idAppUser)
    REFERENCES gnomex.AppUser (idAppUser)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;     


-- Initialize idSubmitter to current contents of idAppUser  
update Analysis
set idSubmitter = idAppUser;

update Request
set idSubmitter = idAppUser;


-- Add new fields for commercial pricing
alter table Lab add column isExternalPricingCommercial CHAR(1) null;
alter table Price add column unitPriceExternalCommercial DECIMAL(6, 2) NULL;

-- Combine experiment_read_property and experiment_write_property
update PropertyDictionary set propertyName = 'experiment_directory' where propertyName = 'experiment_read_directory';
delete from PropertyDictionary where propertyName = 'experiment_write_directory';

-- Combine analysis_read_property and analysis_write_property
update PropertyDictionary set propertyName = 'analysis_directory' where propertyName = 'analysis_read_directory';
delete from PropertyDictionary where propertyName = 'analysis_write_directory';

-- Combine datatrack_read_property and datatrack_write_property
update PropertyDictionary set propertyName = 'datatrack_directory' where propertyName = 'datatrack_read_directory';
delete from PropertyDictionary where propertyName = 'datatrack_write_directory';

-- Create new property for instrument_run_directory
insert into PropertyDictionary (propertyName, propertyValue, forServerOnly, idCoreFacility)
SELECT  'instrument_run_directory', '/path/to/gnomex/instrumentrun','Y', idCoreFacility
from CoreFacility where facilityName = 'DNA Sequencing';

-- Add Septa SealType 
insert into gnomex.SealType
(codeSealType, sealType, isActive)
values 
('SEPTA',  'Septa', 'Y');

-- Add isDefault field to Institution table

alter table gnomex.Institution add column isDefault char(1) null;


-- Alter Topic idParentTopic column to allow nulls

alter table gnomex.Topic change column idParentTopic idParentTopic int(10) NULL;



--
--  Add configuration for MiSeq experiment type and workflow
--
insert into gnomex.RequestCategory (codeRequestCategory, requestCategory, isActive, 
numberOfChannels, icon, type, idCoreFacility, 
isSampleBarcodingOptional, isInternal, isExternal)
values ('MISEQ', 'Illumina MiSeq Sequencing', 'Y', 1, 'assets/DNA_diag_miseq.png', 'ILLUMINA', 1, 'N', 'Y', 'N');

INSERT INTO gnomex.NumberSequencingCycles(idNumberSequencingCycles, numberSequencingCycles, isActive)
VALUES  (7, 150, 'Y');

update gnomex.NumberSequencingCycles set isActive = 'Y' where numberSequencingCycles = 26;

INSERT INTO gnomex.NumberSequencingCyclesAllowed(idNumberSequencingCyclesAllowed, idNumberSequencingCycles, codeRequestCategory, idSeqRunType, name)
VALUES 
  (9, 2, 'MISEQ', 4, '26 cycle paired-end reads'),
  (10, 7, 'MISEQ', 4, '150 cycle paired-end reads');
  
INSERT INTO gnomex.RequestCategoryApplication(codeRequestCategory, codeApplication)
VALUES 
  ('MISEQ', 'CHIPSEQ' ),
  ('MISEQ', 'DMRNASEQ'),
  ('MISEQ', 'DNASEQ'),
  ('MISEQ', 'TDNASEQ'),
  ('MISEQ', 'MRNASEQ'),
  ('MISEQ', 'SMRNASEQ'),
  ('MISEQ', 'DNAMETHSEQ'),
  ('MISEQ', 'MONNUCSEQ'),
  ('MISEQ', 'TSCRPTSEQ');
  
INSERT INTO gnomex.SampleTypeRequestCategory(idSampleType, codeRequestCategory)
VALUES (1, 'MISEQ'),
  (5, 'MISEQ'),
  (9, 'MISEQ'),
  (7, 'MISEQ'),
  (4, 'MISEQ'),
  (9, 'MISEQ'),
  (10, 'MISEQ'),
  (11, 'MISEQ'),
  (12, 'MISEQ');
  
INSERT INTO gnomex.SequencingPlatform(codeSequencingPlatform, sequencingPlatform, isActive)
VALUES 
   ('MISEQ', 'Illumina MiSeq Sequencing', 'Y');
   
INSERT INTO gnomex.Step(codeStep, step, isActive)
VALUES 
  ('MISEQASSEM', 'Illumina MiSeq Seq Prep', 'Y'),
  ('MISEQPIPE', 'Illumina MiSeq Data Pipeline', 'Y'),
  ('MISEQPREP', 'Illumina MiSeq Library Prep', 'Y'),
  ('MISEQQC', 'Illumina MiSeq Quality Control', 'Y');
  
INSERT INTO gnomex.OligoBarcodeSchemeAllowed(idOligoBarcodeScheme, codeRequestCategory)
VALUES (2, 'MISEQ');

INSERT INTO gnomex.PriceSheet(idPriceSheet, name, description, isActive)
VALUES 
  (10, 'Illumina MiSeq Sequencing', 'Illumina MiSeq Sequencing Price Sheet', 'Y');
  
INSERT INTO gnomex.PriceSheetRequestCategory(idPriceSheet, codeRequestCategory)
VALUES 
  (10, 'MISEQ');

INSERT INTO gnomex.PriceCategory(idPriceCategory, name, description, pluginClassName, codeBillingChargeKind, dictionaryClassNameFilter1, dictionaryClassNameFilter2, isActive)
VALUES   (100, 'Illumina MiSeq Sequencing', 'Illumina MiSeq Sequencing', 'hci.gnomex.billing.IlluminaSeqPlugin', 'SERVICE', 'hci.gnomex.model.SeqRunType', 'hci.gnomex.model.NumberSequencingCycles', 'Y');

INSERT INTO gnomex.PriceSheetPriceCategory(idPriceSheet, idPriceCategory, sortOrder)
VALUES
  (10, 1, 1),
  (10, 5, 2),
  (10, 8, 3),
  (10, 100, 6);
  
INSERT INTO gnomex.Price (idPrice,name,description,unitPrice,unitPriceExternalAcademic,idPriceCategory,isActive) VALUES
 (200,'MiSeq 26 cycle paired end','MiSeq 26 cycle paired end','0.00',NULL,100,'Y'),
 (201,'MiSeq 150 cycle paired end','MiSeq 150 cycle paired end','0.00',NULL,100,'Y');
 
 
INSERT INTO gnomex.PriceCriteria (idPriceCriteria,filter1,filter2,idPrice) VALUES 
 (300,'4','2',200),
 (301,'4','7',201);
 
 -- Properties for setting default visibility
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('default_visibility_analysis','MEM','Default visibility for new Analysis. Use value OWNER for Owner, MEM for Members, INST for Institution and PUBLIC for Public. Property can also be eliminated or set to blank to leave original default behavior in place.', 'N');
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('default_visibility_datatrack','MEM','Default visibility for new Data Track. Use value OWNER for Owner, MEM for Members, INST for Institution and PUBLIC for Public. Property can also be eliminated or set to blank to leave original default behavior in place.', 'N');
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('default_visibility_experiment','MEM','Default visibility for new Experiment. Use value OWNER for Owner, MEM for Members, INST for Institution and PUBLIC for Public. Property can also be eliminated or set to blank to leave original default behavior in place.', 'N');
 
-- Property for core facility
alter table gnomex.CoreFacility add showProjectAnnotations char(1) NOT NULL Default 'Y';


-- Add codeVisibility to Topic
alter table gnomex.Topic add column codeVisibility VARCHAR(10) NOT NULL;
alter table gnomex.Topic add
CONSTRAINT FK_Topic_Visibility FOREIGN KEY  (codeVisibility)
    REFERENCES gnomex.Visibility (codeVisibility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- Add idInstitution to Topic
alter table gnomex.Topic add column idInstitution INT(10)  NULL;
alter table gnomex.Topic add
CONSTRAINT FK_Topic_Institution FOREIGN KEY  (idInstitution)
    REFERENCES gnomex.Institution (idInstitution)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;


-- Set number of channels on HiSeq, MiSeq, GAII request categories
update gnomex.RequestCategory set numberOfChannels = 8 where codeRequestCategory = 'HISEQ' or codeRequestCategory = 'SOLEXA';
update gnomex.RequestCategory set numberOfChannels = 1 where codeRequestCategory = 'MISEQ';