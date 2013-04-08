use gnomex;

-- Add clinical research flag to request category
alter table RequestCategory add isClinicalResearch CHAR(1) null;

-- add application notes and core to extract DNA to request
alter table Request add coreToExtractDNA CHAR(1) null;
alter table Request add applicationNotes varchar(5000) null;

-- Add zipCode to BillingAccount table
alter table gnomex.BillingAccount add column zipCode varchar(20);

-- Add optimistic locking version to lab
alter table Lab add version bigint(20) not null default 0;

-- Add contact_email_core_facility_workauth_reminder
INSERT INTO PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly) VALUES
('contact_email_core_facility_work_auth', 'first.last@somwhere.edu', 'Who should receive daily email reminder for pending work authorizations and an email notification when user self registers.', 'N'),
('submit_request_instructions','Experiment has been submitted.  Please deliver your samples.','Instructions upon saving a new experiment', 'N');

-- Add contact_remit_address_core_facility
INSERT INTO PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly, idCoreFacility)
VALUES('contact_remit_address_core_facility', 'University of ......
..... Core Research Facility
address goes here
city, state zip', 
'Core facility remittance address', 'N', 1);

-- Add contact_address_core_facility
INSERT INTO PropertyDictionary  (propertyName,propertyValue,propertyDescription, forServerOnly, idCoreFacility)
VALUES('contact_address_core_facility', 
'University of .....
..... Sequencing Core Research Facility
address goes here
city, state zip', 
'Core facility lab address', 'N', 1);


-- Add entries to suppress Workflow and Orders menus for super admin
INSERT INTO PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly) VALUES
('menu_Orders', 'hide super', 'supress Orders main menu for super; it is in a submenu', 'N'),
('menu_Workflow', 'hide super', 'supress Orders main menu for super; it is in a submenu', 'N');
