use gnomex;

-- Remove trimAdapter column from table and audit table
alter table Request Drop column trimAdapter;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN trimAdapter');



-- New properties for nano string experiments

INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES ('nano_string_batch_warning', 'Nano string runs in batches of 12 samples. Please note you will be responsible to pay for unused wells.', 'Warning for nano string experiments notifying users they will be responsible for paying for unused wells', 'N', null, null);

INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES ('nano_code_set_cost_warning', 'The pricing listed below includes sample Quality (Qubit and BioAnalyzer) and NanoString analysis.  The pricing does NOT include the cost of code sets and master kits (purchased separately).', 'Warn users that pricing listed for nano string experiments does not include the cost of code sets and master kits.', 'N', null, null);

	
-- code application added to BioanalyzerChipType
alter table BioanalyzerChipType add column codeApplication varchar(10) null;
call ExecuteIfTableExists('gnomex','BioanalyzerChipType_Audit','alter table BioanalyzerChipType_Audit add column codeApplication varchar(10) null');
alter table BioanalyzerChipType add 
  CONSTRAINT `FK_BioanalyzerChipType_Application` FOREIGN KEY `FK_BioanalyzerChipType_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
update BioanalyzerChipType set codeApplication='BIOAN';
alter table BioanalyzerChipType add column protocolDescription LONGTEXT null;
call ExecuteIfTableExists('gnomex','BioanalyzerChipType_Audit','alter table BioanalyzerChipType_Audit add column protocolDescription LONGTEXT null');
alter table Sample add column qcCodeApplication VARCHAR(10) NULL;
call ExecuteIfTableExists('gnomex','Sample_Audit','alter table Sample_Audit add column qcCodeApplication VARCHAR(10) NULL');
update Sample
  join BioanalyzerChipType on BioanalyzerChipType.codeBioanalyzerChipType=Sample.codeBioanalyzerChipType
  set Sample.qcCodeApplication=BioanalyzerChipType.codeApplication;
 
-- add hasChipTypes property
alter table Application add column hasChipTypes char(1) null;
call ExecuteIfTableExists('gnomex','Application_Audit','alter table Application_Audit add column hasChipTypes char(1) null');
update Application set hasChipTypes='N' where codeApplicationType='QC';
update Application set hasChipTypes='Y' where codeApplication='BIOAN';

-- Remove status column from ProductOrder table and audit table
alter table ProductOrder drop foreign key FK_ProductOrder_ProductOrderStatus;
alter table ProductOrder Drop column codeProductOrderStatus;
call ExecuteIfTableExists('gnomex','ProductOrder_Audit','alter table ProductOrder_Audit DROP COLUMN codeProductOrderStatus');

-- Add status to ProductLineItem
alter table ProductLineItem add column codeProductOrderStatus VARCHAR(10) NULL;
call ExecuteIfTableExists('gnomex','ProductLineItem_Audit','alter table ProductLineItem_Audit add column codeProductOrderStatus VARCHAR(10) null');
alter table ProductLineItem add 
   CONSTRAINT `FK_ProductOrder_ProductOrderStatus` FOREIGN KEY `FK_ProductOrder_ProductOrderStatus` (`codeProductOrderStatus`)
    REFERENCES `gnomex`.`ProductOrderStatus` (`codeProductOrderStatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- Add number to ProductOrder
alter table ProductOrder add column productOrderNumber VARCHAR(10) NULL;
call ExecuteIfTableExists('gnomex','ProductOrder_Audit','alter table ProductOrder_Audit add column productOrderNumber VARCHAR(10) null');

-- Add idProductLineItem to billing item
alter table BillingItem add column idProductLineItem INT(10) NULL;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit add column idProductLineItem INT(10) null');
    
