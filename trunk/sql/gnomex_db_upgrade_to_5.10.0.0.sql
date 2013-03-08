use gnomex;

-- Add sortOrder to workflow step table
alter table RequestCategory add isClinicalResearch CHAR(1) null;


-- Add zipCode to BillingAccount table
alter table gnomex.BillingAccount add column zipCode varchar(20);
