use gnomex;

-- Remove both foreign keys that reference DNAPrepType and RNAPrepType tables from the Request Table.
alter table Request drop
       FOREIGN KEY FK_Request_DNAPrepType;
     
alter table Request drop
    FOREIGN KEY FK_Request_RNAPrepType;
       
-- Remove dna and rna code prep type columns from Request
ALTER TABLE Request DROP COLUMN codeDNAPrepType;
ALTER TABLE Request DROP COLUMN codeRNAPrepType;

-- Add new view_limit properties
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly)
VALUES ('view_limit_analyses', '1000', 'The maximum number of analyses returned from the back-end.', 'N');
 
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly)
VALUES ('view_limit_datatracks', '200', 'The maximum number of data tracks returned from the back-end.', 'N');

INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly)
VALUES ('view_limit_chromatograms', '1000', 'The maximum number of chromatograms returned from the back-end.', 'N');
 
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly)
VALUES ('view_limit_experiments', '100', 'The maximum number of experiments returned from the back-end.', 'N');
 
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly)
VALUES ('view_limit_plates_and_runs', '200', 'The maximum number of plates and runs returned from the back-end.', 'N');

-- Remove out-dated view_limit properties
DELETE FROM PropertyDictionary WHERE propertyName = 'analysis_view_limit';

DELETE FROM PropertyDictionary WHERE propertyName = 'datatrack_view_limit';

DELETE FROM PropertyDictionary WHERE propertyName = 'chromatogram_view_limit';

DELETE FROM PropertyDictionary WHERE propertyName = 'experiment_view_limit';

DELETE FROM PropertyDictionary WHERE propertyName = 'plate_and_run_view_limit';

-- Add product_sheet_name property
INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly)
VALUES ('product_sheet_name', 'Product Purchasing', 'The name of the price sheet for products.', 'N');

-- Move BillingItem from ProductLineItem to ProductOrder --
-- Drop foreign key idProductLineItem from BillingItem
ALTER TABLE BillingItem DROP CONSTRAINT FK_BillingItem_ProductLineItem;

-- Change idProductLineItem to idProduct Order on BillingItem
ALTER TABLE BillingItem change column idProductLineItem idProductOrder INT(10) NULL;

-- Script to update idProductLineItem to idProduct Order on existing BillingItems
update bi
set bi.idProductOrder = pli.idProductOrder
from BillingItem bi 
join ProductLineItem pli on bi.idProductOrder = pli.idProductLineItem
where bi.idProductOrder is not null
  and bi.idProductOrder = pli.idProductLineItem;

-- Add foreign key idProductOrder to BillingItem
ALTER TABLE BillingItem ADD CONSTRAINT `FK_BillingItem_ProductOrder` FOREIGN KEY `FK_BillingItem_ProductOrder` (`idProductOrder`)
    REFERENCES `gnomex`.`ProductOrder` (`idProductOrder`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
