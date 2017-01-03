use gnomex;

ALTER TABLE BillingTemplate ADD COLUMN isActive char(1) null;
CALL ExecuteIfTableExists('gnomex', 'BillingTemplate_Audit', 'ALTER TABLE BillingTemplate_Audit ADD COLUMN isActive char(1) null');