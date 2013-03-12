use gnomex;

-- Add sortOrder to workflow step table
alter table RequestCategory add isClinicalResearch CHAR(1) null;


-- Add zipCode to BillingAccount table
alter table gnomex.BillingAccount add column zipCode varchar(20);

--Add contact_email_core_facility_workauth_reminder
INSERT INTO PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly) VALUES
('contact_email_core_facility_work_auth', 'first.last@somwhere.edu', 'Core facility''s email adresses for reminders', 'N');

