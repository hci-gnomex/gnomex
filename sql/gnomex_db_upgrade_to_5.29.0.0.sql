use gnomex;

-- Allow BillingItem percentagePrice have an additional decimal
alter table BillingItem MODIFY percentagePrice DECIMAL(4, 3) not null;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit MODIFY percentagePrice decimal(4, 3) null default null');

-- Add tag to BillingItem for identification of split billing items
ALTER TABLE BillingItem ADD COLUMN tag VARCHAR(10) NULL;
call ExecuteIfTableExists('gnomex', 'BillingItem', 'ALTER TABLE BillingItem ADD COLUMN tag VARCHAR(10) NULL');


-- Default folder location for sequenom prices
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES('illumina_libprep_default_price_category', 'Sequenom Panel', 'Default price category for sequenom prices created through experiment platform', 'N', 3, 'SEQUENOM'); 


-- Remove these properties from the dictionary.  We now grab this info from the CoreFacility database table.	
DELETE from PropertyDictionary where propertyName = 'contact_name_core_facility';
DELETE from PropertyDictionary where propertyName = 'contact_email_core_facility';
DELETE from PropertyDictionary where propertyName = 'contact_phone_core_facility';
DELETE from PropertyDictionary where propertyName = 'core_facility_name';

