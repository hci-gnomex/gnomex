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

