use gnomex;

-- COLUMN ADDS -- 
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
set @exist := (select count(*) from information_schema.statistics where table_name = 'Property' and index_name = 'name' and table_schema = database());
set @sqlstmt := if( @exist > 0, 'alter table Property drop index name','select ''INFO: Index name already dropped''');
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;    
update Property set idCoreFacility=1;

-- Add notes to SampleType
alter table SampleType add column notes varchar(5000) null;
call ExecuteIfTableExists('gnomex', 'SampleType_Audit', 'alter table SampleType_Audit ADD COLUMN notes varchar(5000) NULL');

-- Add columns to keep track of who approves a billing account
alter table BillingAccount 
ADD approverEmail varchar(200) NULL;
call ExecuteIfTableExists('gnomex','BillingAccount_Audit','alter table BillingAccount_Audit ADD approverEmail varchar(200) NULL');
alter table BillingAccount
ADD idApprover int(10) NULL,
ADD CONSTRAINT `FK_BillingAccount_Approver` FOREIGN KEY `FK_BillingAccount_Approver`(`idApprover`)
REFERENCES `gnomex`.`AppUser` (`idAppUser`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
call ExecuteIfTableExists('gnomex','BillingAccount_Audit','alter table BillingAccount_Audit ADD idApprover int(10) NULL');

-- add column to Property for annotations for requests
alter table Property add column forRequest char(1) null;
call ExecuteIfTableExists('gnomex','Property_Audit','alter table Property_Audit add column forRequest char(1) null');

-- add column to PropertyEntry for annotations for requests
alter table PropertyEntry add column idRequest int null;
ADD CONSTRAINT `FK_PropertyEntry_Request` FOREIGN KEY `FK_PropertyEntry_Request`(`idRequest`)
REFERENCES `gnomex`.`Request` (`idRequest`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
call ExecuteIfTableExists('gnomex','PropertyEntry_Audit','alter table PropertyEntry_Audit add column idRequest int null');

-- COLUMN DROPS --
-- Remove idProductOrder column from BillingItem table and audit table
alter table BillingItem drop foreign key FK_BillingItem_ProductOrder;
alter table BillingItem Drop column idProductOrder;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit DROP COLUMN idProductOrder');

-- Remove requestNumber column from ProductLedger table and audit table
alter table ProductLedger Drop column requestNumber;
call ExecuteIfTableExists('gnomex','ProductLedger_Audit','alter table ProductLedger_Audit DROP COLUMN requestNumber');

-- Remove materialQuoteNumber column from Request table and audit table
alter table Request Drop column materialQuoteNumber;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN materialQuoteNumber');

-- Remove quoteReceivedDate column from Request table and audit table
alter table Request Drop column quoteReceivedDate;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN quoteReceivedDate');

-- Remove uuid column from Request table and audit table
alter table Request Drop column uuid;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN uuid');

-- Remove isSampleBarcodingOptional column from RequestCategory table and audit table
alter table RequestCategory Drop column isSampleBarcodingOptional;
call ExecuteIfTableExists('gnomex','RequestCategory_Audit','alter table RequestCategory_Audit DROP COLUMN isSampleBarcodingOptional');

-- TABLE DROPS -- 
-- unused table
DROP TABLE IF EXISTS `gnomex`.`SampleTypeApplication`;
DROP TABLE IF EXISTS `gnomex`.`SampleTypeApplication_Audit`;

-- UPDATES AND INSERTS -- 
-- update the property so it is generic and not tied to nano string
UPDATE PropertyDictionary SET propertyName = 'sample_batch_warning' WHERE propertyName = 'nano_string_batch_warning';

insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES('core_billing_office', '', 'Name of who handles billing for the core', 'Y', NULL, NULL);



