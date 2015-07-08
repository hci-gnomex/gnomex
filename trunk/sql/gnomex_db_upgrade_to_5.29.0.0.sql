use gnomex;

-- Allow BillingItem percentagePrice have an additional decimal
alter table BillingItem MODIFY percentagePrice DECIMAL(4, 3) not null;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit MODIFY percentagePrice decimal(4, 3) null default null');