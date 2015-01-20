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


