use gnomex;


-- Add PriceCategory to Property
ALTER TABLE Property
ADD COLUMN idPriceCategory int default NULL;

ALTER TABLE Property ADD CONSTRAINT FK_Property_PriceCategory FOREIGN KEY FK_Property_PriceCategory (idPriceCategory)
    REFERENCES gnomex.PriceCategory (idPriceCategory)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION   


call ExecuteIfTableExists('gnomex','Property_Audit','alter table Property_Audit ADD COLUMN idPriceCategory int NULL');


-- script for billThroughGnomex MYSQL
ALTER TABLE Product ADD COLUMN billThroughGnomex CHAR(1) NULL;
CALL ExecuteIfTableExists('gnomex', 'Product_Audit', 'ALTER TABLE Product_Audit ADD COLUMN billThroughGnomex CHAR(1) NULL');

-- new property that allows users to just save their request before actually submitting.  Submitting the experiment will prevent the user from making changes to their samples.
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
  VALUES('new_request_save_before_submit', 'N', 'Allow users to save a new request and still make changes to the request until they mark the request as submitted.', 'N', NULL, NULL);
  
  -- update directory names
update PropertyDictionary set propertyName = 'directory_experiment' where propertyName = 'experiment_directory';
update PropertyDictionary set propertyName = 'directory_analysis' where propertyName = 'analysis_directory';
update PropertyDictionary set propertyName = 'directory_datatrack' where propertyName = 'datatrack_directory'; 
update PropertyDictionary set propertyName = 'directory_flowcell' where propertyName = 'flowcell_directory'; 
update PropertyDictionary set propertyName = 'directory_instrument_run' where propertyName = 'instrument_run_directory'; 
update PropertyDictionary set propertyName = 'directory_product_order' where propertyName = 'product_order_directory'; 


-- iobio viewer URL's
INSERT INTO gnomex.PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly) VALUES
 ('bam_iobio_viewer_url','http://bam.iobio.io/?bam=','','N'),
 ('vcf_iobio_viewer_url','http://vcf.iobio.io/?vcf=','','N'),
 ('gene_iobio_viewer_url','http://gene.iobio.io','','N');


  -- update directory names
update PropertyDictionary set propertyName = 'directory_experiment' where propertyName = 'experiment_directory';
update PropertyDictionary set propertyName = 'directory_analysis' where propertyName = 'analysis_directory';
update PropertyDictionary set propertyName = 'directory_datatrack' where propertyName = 'datatrack_directory';
update PropertyDictionary set propertyName = 'directory_flowcell' where propertyName = 'flowcell_directory';
update PropertyDictionary set propertyName = 'directory_instrument_run' where propertyName = 'instrument_run_directory';
update PropertyDictionary set propertyName = 'directory_product_order' where propertyName = 'product_order_directory';

insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
values ('directory_product_order', '/home/gnomex/ProductOrders/', 'file directory for product order files', 'N', NULL, NULL);

-------------------------------------------------------------
-- Change codeProductType to idProductType
-- This assumes no Product Ordering rows have been added yet
-------------------------------------------------------------
-- Drop foreign keys to codeProduct Type
ALTER TABLE RequestCategory DROP FOREIGN KEY FK_RequestCategory_ProductType;
ALTER TABLE Product DROP FOREIGN KEY FK_Product_ProductType;
ALTER TABLE ProductOrder DROP FOREIGN KEY FK_ProductOrder_ProductType;

-- Drop primary key on ProductType
ALTER TABLE ProductType drop primary key;

-- Drop the column on ProductType
ALTER TABLE ProductType DROP COLUMN codeProductType;
call ExecuteIfTableExists('gnomex','ProductType_Audit','alter table ProductType_Audit DROP COLUMN codeProductType');

-- Add new column to ProductType (and audit) as primary key
ALTER TABLE ProductType ADD COLUMN idProductType int(10) PRIMARY KEY NOT NULL AUTO_INCREMENT;
call ExecuteIfTableExists('gnomex','ProductType_Audit','alter table ProductType_Audit ADD COLUMN idProductType int NULL');

-- Add constraint for uniqueness on corefacility / description
ALTER TABLE ProductType change column idCoreFacility idCoreFacility INT(10) NOT NULL;
ALTER TABLE ProductType change column description description VARCHAR(5000) NOT NULL
ALTER TABLE ProductType ADD CONSTRAINT UNQ_ProductType_idCoreFacility_description
    UNIQUE (idCoreFacility, description)

-- Change name/type on other tables from codeProductType to idProductType
ALTER TABLE RequestCategory change column codeProductType idProductType INT(10) NULL;
ALTER TABLE Product change column codeProductType idProductType INT(10) NULL;
ALTER TABLE ProductOrder change column codeProductType idProductType INT(10) NULL;

-- Reinstitute foreign keys to ProductType
ALTER TABLE RequestCategory ADD CONSTRAINT `FK_RequestCategory_ProductType` FOREIGN KEY `FK_RequestCategory_ProductType` (`idProductType`)
    REFERENCES `gnomex`.`ProductType` (`idProductType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
ALTER TABLE Product ADD CONSTRAINT `FK_Product_ProductType` FOREIGN KEY `FK_Product_ProductType` (`idProductType`)
    REFERENCES `gnomex`.`ProductType` (`idProductType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
ALTER TABLE ProductOrder ADD CONSTRAINT `FK_ProductOrder_ProductType` FOREIGN KEY `FK_ProductOrder_ProductType` (`idProductType`)
    REFERENCES `gnomex`.`ProductType` (`idProductType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
--------------------------------------------------------------

-- Add BillingTemplate Table
DROP TABLE IF EXISTS `gnomex`.`BillingTemplate`;
CREATE TABLE `gnomex`.`BillingTemplate` (
	`idBillingTemplate` INT(10) NOT NULL AUTO_INCREMENT,
	`targetClassIdentifier` INT(10) NOT NULL,
	`targetClassName` VARCHAR(100) NOT NULL,
	PRIMARY KEY (`idBillingTemplate`)
)
ENGINE = INNODB;

-- Add BillingTemplateItem Table
DROP TABLE IF EXISTS `gnomex`.`BillingTemplateItem`;
CREATE TABLE `gnomex`.`BillingTemplateItem` (
	`idBillingTemplateItem` INT(10) NOT NULL AUTO_INCREMENT,
	`idBillingTemplate` INT(10) NOT NULL,
	`idBillingAccount` INT(10) NOT NULL,
	`percentSplit` DECIMAL(4, 3) NULL,
	`dollarAmount` DECIMAL(7, 2) NULL,
	`dollarAmountBalance` DECIMAL(7, 2) NULL,
	PRIMARY KEY (`idBillingTemplateItem`),
	CONSTRAINT `FK_BillingTemplateItem_BillingAccount` FOREIGN KEY `FK_BillingTemplateItem_BillingAccount` (`idBillingAccount`)
		REFERENCES `gnomex`.`BillingAccount` (`idBillingAccount`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT `FK_BillingTemplateItem_BillingTemplate` FOREIGN KEY `FK_BillingTemplateItem_BillingTemplate` (`idBillingTemplate`)
		REFERENCES `gnomex`.`BillingTemplate` (`idBillingTemplate`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
ENGINE = INNODB;

-- Add MasterBillingItem Table
DROP TABLE IF EXISTS gnomex.MasterBillingItem;
CREATE TABLE gnomex.MasterBillingItem (
	idMasterBillingItem INT(10) NOT NULL AUTO_INCREMENT,
	idBillingTemplate INT(10) NOT NULL,
	codeBillingChargeKind VARCHAR(10) NULL,
	category VARCHAR(200) NULL,
	description VARCHAR(500) NULL,
	qty INT(10) NULL,
	unitPrice DECIMAL(7, 2) NULL,
	totalPrice DECIMAL(9, 2) NULL,
	idBillingPeriod INT(10) NULL,
	idPrice INT(10) NULL,
	idPriceCategory INT(10) NULL,
	idCoreFacility INT(10) NULL,
	PRIMARY KEY (idMasterBillingItem),
	CONSTRAINT FK_MasterBillingItem_BillingChargeKind FOREIGN KEY (codeBillingChargeKind)
		REFERENCES gnomex.BillingChargeKind (codeBillingChargeKind)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT FK_MasterBillingItem_BillingTemplate FOREIGN KEY (idBillingTemplate)
		REFERENCES gnomex.BillingTemplate (idBillingTemplate)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT FK_MasterBillingItem_BillingPeriod FOREIGN KEY (idBillingPeriod)
		REFERENCES gnomex.BillingPeriod (idBillingPeriod)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT FK_MasterBillingItem_Price FOREIGN KEY (idPrice)
		REFERENCES gnomex.Price (idPrice)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT FK_MasterBillingItem_PriceCategory FOREIGN KEY (idPriceCategory)
		REFERENCES gnomex.PriceCategory (idPriceCategory)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT FK_MasterBillingItem_CoreFacility FOREIGN KEY (idCoreFacility)
		REFERENCES gnomex.CoreFacility (idCoreFacility)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
ENGINE = INNODB;

-- Add idMasterBillingItem to BillingItem
ALTER TABLE BillingItem
ADD COLUMN idMasterBillingItem INT(10) NULL;

ALTER TABLE BillingItem ADD CONSTRAINT FK_BillingItem_MasterBillingItem FOREIGN KEY FK_BillingItem_MasterBillingItem (idMasterBillingItem)
    REFERENCES gnomex.MasterBillingItem (idMasterBillingItem)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

CALL ExecuteIfTableExists('gnomex','BillingItem_Audit','ALTER TABLE BillingItem_Audit ADD COLUMN idMasterBillingItem INT(10) NULL');

-- Add sort order to BillingTemplateItem
ALTER TABLE BillingTemplateItem
ADD COLUMN sortOrder INT(10) NULL;
CALL ExecuteIfTableExists('gnomex','BillingTemplateItem_Audit','ALTER TABLE BillingTemplateItem_Audit ADD COLUMN sortOrder INT(10) NULL');
