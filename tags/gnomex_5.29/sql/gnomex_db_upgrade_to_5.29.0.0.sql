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




-- SQL upgrade script for DNA/RNA isolation combining


-- Add table Isolation Prep Type  FOREIGN KEY on codeRequestCategory???
DROP TABLE IF EXISTS `gnomex`.`IsolationPrepType`;
CREATE TABLE `gnomex`.`IsolationPrepType` ( 
     `codeIsolationPrepType`	varchar(15) NOT NULL,
     `isolationPrepType`  		varchar(100) NULL,
	 `type`			varchar(10) NULL,
	 `isActive`		char(1) NULL,
	 `codeRequestCategory` varchar(50) NULL,
    PRIMARY KEY (`codeIsolationPrepType`),
	CONSTRAINT `FK_IsolationPrepType_RequestCategory` FOREIGN KEY (`codeRequestCategory`) 
	REFERENCES `gnomex`.`RequestCategory`(`codeRequestCategory`)
	ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

-- Add codeIsolationPrepType Column
ALTER TABLE Request ADD COLUMN codeIsolationPrepType varchar(15) null;
call ExecuteIfTableExists('gnomex', 'Request_Audit', ' ALTER TABLE Request_Audit ADD COLUMN codeIsolationPrepType varchar(15) null');

-- Add the new foreign key to Request for Isolation Prep Type
alter table Request add 
  CONSTRAINT `FK_Request_IsolationPrepType` FOREIGN KEY (`codeIsolationPrepType`)
    REFERENCES `gnomex`.`IsolationPrepType` (`codeIsolationPrepType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
	
	
-- Remove both foreign keys that reference DNAPrepType and RNAPrepType tables from the Request Table.
alter table Request drop
	CONSTRAINT FK_Request_DNAPrepType;
	
alter table Request drop
	CONSTRAINT FK_Request_RNAPrepType;	

-- Remove dna and rna code prep type columns from Request
ALTER TABLE Request DROP COLUMN codeDNAPrepType;
ALTER TABLE Request DROP COLUMN codeRNAPrepType;
