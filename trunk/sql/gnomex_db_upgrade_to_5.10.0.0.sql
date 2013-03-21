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

--Add contact_email_core_facility_workauth_reminder
INSERT INTO PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly) VALUES
('contact_email_core_facility_work_auth', 'first.last@somwhere.edu', 'Who should receive daily email reminder for pending work authorizations and an email notification when user self registers.', 'N'),
('submit_request_instructions','Experiment has been submitted.  Please deliver your samples.','Instructions upon saving a new experiment', 'N');

--Add contact_remit_address_core_facility
INSERT INTO PropertyDictionary VALUES('contact_remit_address_core_facility', 'University of Utah' + CHAR(13) + 'DNA Sequencing Core Research Facility' + CHAR(13) + '30 N. 1900 E. 5C124 SOM' + CHAR(13) + 'Salt Lake City, Utah 84132', 'Core facility remittance address', 'N', 3);

--Add contact_address_core_facility
INSERT INTO PropertyDictionary VALUES('contact_address_core_facility', 'University of Utah' + CHAR(13) + 'DNA Sequencing Core Research Facility' + CHAR(13) + '30 N. 1900 E. 5C124 SOM' + CHAR(13) + 'Salt Lake City, Utah 84132', 'Core facility lab address', 'N', 3);