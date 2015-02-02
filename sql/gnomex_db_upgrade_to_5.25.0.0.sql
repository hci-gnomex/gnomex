use gnomex;

-- Remove materialQuoteNumber column from Request table and audit table
alter table Request Drop column materialQuoteNumber;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN materialQuoteNumber');

-- Remove materialQuoteNumber column from Request table and audit table
alter table Request Drop column quoteReceivedDate;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN quoteReceivedDate');

-- Remove uuid column from Request table and audit table
alter table Request Drop column uuid;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN uuid');


-- update the property so it is generic and not tied to nano string
UPDATE PropertyDictionary SET propertyName = 'sample_batch_warning' WHERE propertyName = 'nano_string_batch_warning';

-- add sample batch size column to request category
ALTER TABLE RequestCategory ADD sampleBatchSize int NULL;
call ExecuteIfTableExists('gnomex','RequestCategory_Audit','alter table RequestCategory_Audit add column sampleBatchSize int null');

-- Partition Property table (annotations) by core facility
Alter Table Property Add Column idCoreFacility INT(10);
call ExecuteIfTableExists('gnomex','Property_Audit','alter table Property_Audit Add Column idCoreFacility INT(10)');
alter table Property add 
  CONSTRAINT `FK_Property_CoreFacility` FOREIGN KEY `FK_Property_CoreFacility` (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table Property drop index name;
update Property set idCoreFacility=1;

-- unused table
DROP TABLE IF EXISTS `gnomex`.`SampleTypeApplication`;
DROP TABLE IF EXISTS `gnomex`.`SampleTypeApplication_Audit`;

-- Add notes to SampleType
alter table SampleType add column notes varchar(5000) null;
call ExecuteIfTableExists('gnomex', 'SampleType_Audit', 'alter table SampleType_Audit ADD COLUMN notes varchar(5000) NULL');

-- Remove idProductOrder column from BillingItem table and audit table
alter table BillingItem drop foreign key FK_BillingItem_ProductOrder;
alter table BillingItem Drop column idProductOrder;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit DROP COLUMN idProductOrder');
