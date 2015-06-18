use gnomex;

-- Add code product type to request category
alter table RequestCategory 
ADD COLUMN codeProductType VARCHAR(10) NULL,
ADD CONSTRAINT `FK_RequestCategory_ProductType` FOREIGN KEY `FK_RequestCategory_ProductType` (`codeProductType`)
REFERENCES `gnomex`.`ProductType` (`codeProductType`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
call ExecuteIfTableExists('gnomex', 'RequestCategory_Audit', 'alter table RequestCategory_Audit add column codeProductType VARCHAR(10) NULL');

-- Add associated with analysis to request category
ALTER TABLE RequestCategory ADD COLUMN associatedWithAnalysis CHAR(1) NULL;
call ExecuteIfTableExists('gnomex', 'RequestCategory_Audit', 'alter table RequestCategory_Audit add column associatedWithAnalysis CHAR(1) NULL');

-- Add id sample to analysis experiment item
alter table AnalysisExperimentItem
ADD COLUMN idSample INT(10) NULL,
ADD CONSTRAINT `FK_AnalysisExperimentItem_Sample` FOREIGN KEY `FK_AnalysisExperimentItem_Sample` (`idSample`)
REFERENCES `gnomex`.`Sample` (`idSample`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
call ExecuteIfTableExists('gnomex', 'AnalysisExperimentItem_Audit', 'alter table AnalysisExperimentItem_Audit add column idSample INT(10) NULL');



call ExecuteIfTableExists('gnomex', 'AppUser_Audit', ' ALTER TABLE AppUser_Audit ADD COLUMN confirmEmailGuid varchar(100) null');

call ExecuteIfTableExists('gnomex', 'SampleType_Audit', ' ALTER TABLE SampleType_Audit ADD COLUMN idCoreFacility int(10) null');
