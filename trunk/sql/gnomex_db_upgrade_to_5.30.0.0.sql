use gnomex;

-- Remove both foreign keys that reference DNAPrepType and RNAPrepType tables from the Request Table.
alter table Request drop
	FOREIGN KEY FK_Request_DNAPrepType;
	
-- Remove dna and rna code prep type columns from Request
ALTER TABLE Request DROP COLUMN codeDNAPrepType;
ALTER TABLE Request DROP COLUMN codeRNAPrepType;


-- Add idPrice to isolation prep type
ALTER TABLE IsolationPrepType ADD COLUMN idPrice int NULL,
ADD CONSTRAINT `FK_IsolationPrepType_Price` FOREIGN KEY `FK_IsolationPrepType_Price`(`idPrice`)
REFERENCES `gnomex`.`Price` (`idPrice`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
call ExecuteIfTableExists('gnomex', 'IsolationPrepType_Audit', 'alter table IsolationPrepType_Audit add column idPrice int NULL');

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