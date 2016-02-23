use gnomex;


-- Add constraint for uniqueness on corefacility / description
ALTER TABLE ProductType ADD CONSTRAINT UNQ_ProductType_idCoreFacility_description
    UNIQUE (idCoreFacility, description);
	

-- Note that this might fail if there are duplicates in the DB already
-- We will have to null out values where email is not unique.
ALTER TABLE AppUser ADD CONSTRAINT UNQ_AppUser_email UNIQUE (email);


-----------------------------------------------------------------
-- Billing Template
-----------------------------------------------------------------
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

-----------------------------------------------------------------

-- Add notes to ProductLedger
ALTER TABLE ProductLedger
ADD COLUMN notes VARCHAR(5000) NULL;
CALL ExecuteIfTableExists('gnomex','ProductLedger_Audit','ALTER TABLE ProductLedger_Audit ADD COLUMN notes VARCHAR(5000) NULL');


-- Drop the alignToGenomeBuild column
ALTER TABLE Request
DROP COLUMN alignToGenomeBuild;

CALL ExecuteIfTableExists('gnomex', 'Request_Audit', 'ALTER TABLE Request_Audit DROP COLUMN alignToGenomeBuild');


-- Add property to ProductType to determine if the purchasing system should be utilized
ALTER TABLE ProductType
ADD COLUMN utilizePurchasingSystem CHAR(1) NULL;
CALL ExecuteIfTableExists('gnomex','ProductType_Audit','ALTER TABLE ProductType_Audit ADD COLUMN utilizePurchasingSystem CHAR(1) NULL');