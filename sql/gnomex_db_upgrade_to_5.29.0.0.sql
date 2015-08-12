use gnomex;

-- Allow BillingItem percentagePrice have an additional decimal
alter table BillingItem MODIFY percentagePrice DECIMAL(4, 3) not null;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit MODIFY percentagePrice decimal(4, 3) null default null');

-- Add tag to BillingItem for identification of split billing items
ALTER TABLE BillingItem ADD COLUMN tag VARCHAR(10) NULL;
call ExecuteIfTableExists('gnomex', 'BillingItem_Audit', 'ALTER TABLE BillingItem_Audit ADD COLUMN tag VARCHAR(10) NULL');


-- Default folder location for sequenom prices
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES('illumina_libprep_default_price_category', 'Sequenom Panel', 'Default price category for sequenom prices created through experiment platform', 'N', 3, 'SEQUENOM'); 


-- Remove these properties from the dictionary.  We now grab this info from the CoreFacility database table.	
DELETE from PropertyDictionary where propertyName = 'contact_name_core_facility';
DELETE from PropertyDictionary where propertyName = 'contact_email_core_facility';
DELETE from PropertyDictionary where propertyName = 'contact_phone_core_facility';
DELETE from PropertyDictionary where propertyName = 'core_facility_name';

-- Add idProduct to Request
ALTER TABLE Request 
ADD COLUMN idProduct INT(10) NULL,
ADD CONSTRAINT `FK_Request_Product` FOREIGN KEY `FK_Request_Product` (`idProduct`)
REFERENCES `gnomex`.`Product` (`idProduct`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
call ExecuteIfTableExists('gnomex', 'Request_Audit', 'ALTER TABLE Request_Audit ADD COLUMN idProduct INT(10) NULL');

-- Add foreign key Sample to AnalysisExperimentItem
ALTER TABLE AnalysisExperimentItem
ADD CONSTRAINT `FK_AnalysisExperimentItem_Sample` FOREIGN KEY `FK_AnalysisExperimentItem_Sample` (`idSample`)
REFERENCES `gnomex`.`Sample` (`idSample`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-- Add foreign key ProductType to RequestCategory
ALTER TABLE RequestCategory
ADD CONSTRAINT `FK_RequestCategory_ProductType` FOREIGN KEY `FK_RequestCategory_ProductType` (`codeProductType`)
REFERENCES `gnomex`.`ProductType` (`codeProductType`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
