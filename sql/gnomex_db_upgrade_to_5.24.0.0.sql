use gnomex;

-- Remove trimAdapter column from table and audit table
alter table Request Drop column trimAdapter;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN trimAdapter');



-- New properties for nano string experiments

INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES ('nano_string_batch_warning', 'Nano string runs in batches of 12 samples. Please note you will be responsible to pay for unused wells.', 'Warning for nano string experiments notifying users they will be responsible for paying for unused wells', 'N', null, null);

INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES ('nano_code_set_cost_warning', 'The pricing listed below includes sample Quality (Qubit and BioAnalyzer) and NanoString analysis.  The pricing does NOT include the cost of code sets and master kits (purchased separately).', 'Warn users that pricing listed for nano string experiments does not include the cost of code sets and master kits.', 'N', null, null);

	
-- code application added to BioanalyzerChipType
alter table BioanalyzerChipType add column codeApplication varchar(10) null;
call ExecuteIfTableExists('gnomex','BioanalyzerChipType_Audit','alter table BioanalyzerChipType_Audit add column codeApplication varchar(10) null')
alter table BioanalyzerChipType add 
  CONSTRAINT `FK_BioanalyzerChipType_Application` FOREIGN KEY `FK_BioanalyzerChipType_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
update BioanalyzerChipType set codeApplication='BIOAN';

-- add hasChipTypes property
alter table Application add column hasChipTypes char(1) null;
call ExecuteIfTableExists('gnomex','Application_Audit','alter table Application_Audit add column hasChipTypes char(1) null')
update Application set hasChipTypes='N' where codeApplicationType='QC';
update Application set hasChipTypes='Y' where codeApplication='BIOAN';

-- really remove SampleTypeApplication
drop table SampleTypeApplication;

