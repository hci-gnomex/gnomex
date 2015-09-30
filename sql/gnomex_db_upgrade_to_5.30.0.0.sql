use gnomex;

-- Remove both foreign keys that reference DNAPrepType and RNAPrepType tables from the Request Table.
alter table Request drop
	FOREIGN KEY FK_Request_DNAPrepType;
	
-- Remove dna and rna code prep type columns from Request
ALTER TABLE Request DROP COLUMN codeDNAPrepType;
ALTER TABLE Request DROP COLUMN codeRNAPrepType;

-- Default price category for isolation prep types
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
VALUES ('isolation_default_price_category', 'Nucleic Acid Isolation', 'Default price category for isolation prep types created through experiment platform', 'N', 3, 'ISOL');


-- Increase size of filter columns on Price Criteria
alter table PriceCriteria
alter column filter1 varchar(20);

alter table PriceCriteria
alter column filter2 varchar(20);

CALL ExecuteIfTableExists('gnomex', 'PriceCriteria_Audit', 'alter table PriceCriteria_Audit alter column filter1 varchar(20)');
CALL ExecuteIfTableExists('gnomex', 'PriceCriteria_Audit', 'alter table PriceCriteria_Audit alter column filter2 varchar(20)');

-- Add batchSamplesByUseQuantity to Product
ALTER TABLE Product
ADD COLUMN batchSamplesByUseQuantity CHAR(1) NULL;
CALL ExecuteIfTableExists('gnomex', 'Product_Audit', 'ALTER TABLE Product_Audit ADD COLUMN batchSamplesByUseQuantity CHAR(1) NULL');

-- Increase size of bioanalyzerChipType on BioanalyzerChipType
ALTER TABLE BioanalyzerChipType MODIFY bioanalyzerChipType VARCHAR(100) NULL;
CALL ExecuteIfTableExists('gnomex', 'BioanalyzerChipType_Audit', 'ALTER TABLE BioanalyzerChipType_Audit MODIFY bioanalyzerChipType VARCHAR(100) NULL');

-- New property for custom sample concentrations on experiment submission
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
    VALUES ('custom_sample_conc_units', 'N', 'Allow for configurable sample concentration units on experiment submission', 'N', NULL, NULL);