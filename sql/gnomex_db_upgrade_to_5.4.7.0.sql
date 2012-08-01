use gnomex;

-- Add new billing account columns
alter table BillingAccount add column shortAcct VARCHAR(10) null;
alter table BillingAccount add column startDate DATETIME NULL;
