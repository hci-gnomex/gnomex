use gnomex;


-- Add constraint for uniqueness on corefacility / description
ALTER TABLE ProductType change column description description VARCHAR(500) NOT NULL;
ALTER TABLE ProductType ADD CONSTRAINT UNQ_ProductType_idCoreFacility_description
    UNIQUE (idCoreFacility, description);
	

-- Note that this might fail if there are duplicates in the DB already
-- We will have to null out values where email is not unique.
ALTER TABLE AppUser ADD CONSTRAINT UNQ_AppUser_email UNIQUE (email);

-- Add billing template help to context help table
INSERT INTO ContextSensitiveHelp(context1, context2, context3, helpText, toolTipText)
  values('billingTemplateHelp', '', '',
  '<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">Billing Template</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">1. Add as many billing accounts as desired to the grid</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">2. Remove any billing accounts that are not applicable.</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="1">3. Select split by percentage or by dollar amount.</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="1">4. Add a percentage or dollar amount to each billing account. One billing account should be selected to be responsible for the balance.</FONT></P></TEXTFORMAT>',
  '<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">Billing Template</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">1. Add as many billing accounts as desired to the grid</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">2. Remove any billing accounts that are not applicable.</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="1">3. Select split by percentage or by dollar amount.</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="1">4. Add a percentage or dollar amount to each billing account. One billing account should be selected to be responsible for the balance.</FONT></P></TEXTFORMAT>');

---------------------------------------------------------------------------------
-- Billing Template
---------------------------------------------------------------------------------
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

-------------------------------------------------------------------------------------
-- Assign billing templates to requests with idBillingAccount but no billing items
-------------------------------------------------------------------------------------
-- Insert BillingTemplate for each Request that has no billing items but does have a idBillingAccount (only do recent requests)
INSERT INTO BillingTemplate (targetClassIdentifier, targetClassName )
SELECT DISTINCT req.idRequest, 'hci.gnomex.model.Request'
FROM Request req
LEFT JOIN BillingItem bi on req.idRequest = bi.idRequest
WHERE req.createDate > (getDate()-360) AND bi.idBillingItem IS NULL AND req.idBillingAccount IS NOT NULL;

-- Insert BillingTemplateItem for each for each Request that has no billing items
INSERT INTO BillingTemplateItem (idBillingTemplate, idBillingAccount, percentSplit, sortOrder)
SELECT DISTINCT  bt.idBillingTemplate, req.idBillingAccount, -1, 1
FROM BillingTemplate bt
JOIN Request req on bt.targetClassIdentifier = req.idRequest
LEFT JOIN BillingItem bi on req.idRequest = bi.idRequest
WHERE bt.targetClassName = 'hci.gnomex.model.Request' AND
 req.createDate > (getDate()-360) AND bi.idBillingItem IS NULL AND req.idBillingAccount IS NOT NULL;


---------------------------------------------------------------------------------
-- Wrap existing billing items with template
---------------------------------------------------------------------------------
-- REQUESTS:
-- Insert BillingTemplate for each Request that has a BillingItem
INSERT INTO BillingTemplate (targetClassIdentifier, targetClassName )
SELECT DISTINCT bi.idRequest, 'hci.gnomex.model.Request'
from BillingItem bi WHERE bi.idRequest IS NOT NULL AND bi.idMasterBillingItem IS NULL;

-- Insert BillingTemplateItem for each BillingItem
INSERT INTO BillingTemplateItem (idBillingTemplate, idBillingAccount, percentSplit, sortOrder)
SELECT DISTINCT bt.idBillingTemplate, bi.idBillingAccount, -1, 1
FROM BillingTemplate bt
JOIN BillingItem bi on bt.targetClassIdentifier = bi.idRequest
WHERE bt.targetClassName = 'hci.gnomex.model.Request' AND
bi.idRequest IS NOT NULL AND bi.idMasterBillingItem IS NULL;

-- Insert MasterBillingItem for each BillingItem
INSERT INTO MasterBillingItem
(idBillingTemplate, codeBillingChargeKind, category, description, qty, unitPrice, totalPrice, idBillingPeriod, idPrice, idPriceCategory, idCoreFacility)
SELECT DISTINCT bt.idBillingTemplate, bi.codeBillingChargeKind, bi.category, bi.description, bi.qty, bi.unitPrice, bi.totalPrice, bi.idBillingPeriod, bi.idPrice, bi.idPriceCategory, bi.idCoreFacility
FROM BillingTemplate bt
JOIN BillingItem bi on bt.targetClassIdentifier = bi.idRequest
WHERE bt.targetClassName = 'hci.gnomex.model.Request' AND
bi.idRequest IS NOT NULL AND bi.idMasterBillingItem IS NULL;

-- Add idMasterBillingItem to each BillingItem
UPDATE bi
set bi.idMasterBillingItem = mbi.idMasterBillingItem
FROM BillingItem bi
JOIN MasterBillingItem mbi on
bi.idPrice = mbi.idPrice
JOIN BillingTemplate bt
on bt.idBillingTemplate = mbi.idBillingTemplate
WHERE bt.targetClassIdentifier = bi.idRequest AND
bt.targetClassName = 'hci.gnomex.model.Request' AND
bi.idRequest IS NOT NULL AND bi.idMasterBillingItem IS NULL;
---------------------------------------------------------------------------------

-- PRODUCTORDERS:
-- Insert BillingTemplate for each ProductOrder that has a BillingItem
INSERT INTO BillingTemplate (targetClassIdentifier, targetClassName )
SELECT DISTINCT bi.idProductOrder, 'hci.gnomex.model.ProductOrder'
from BillingItem bi WHERE bi.idProductOrder IS NOT NULL AND bi.idMasterBillingItem IS NULL;

-- Insert BillingTemplateItem for each BillingItem
INSERT INTO BillingTemplateItem (idBillingTemplate, idBillingAccount, percentSplit, sortOrder)
SELECT DISTINCT bt.idBillingTemplate, bi.idBillingAccount, -1, 1
FROM BillingTemplate bt
JOIN BillingItem bi on bt.targetClassIdentifier = bi.idProductOrder
WHERE bt.targetClassName = 'hci.gnomex.model.ProductOrder' AND
bi.idProductOrder IS NOT NULL AND bi.idMasterBillingItem IS NULL;

-- Insert MasterBillingItem for each BillingItem
INSERT INTO MasterBillingItem
(idBillingTemplate, codeBillingChargeKind, category, description, qty, unitPrice, totalPrice, idBillingPeriod, idPrice, idPriceCategory, idCoreFacility)
SELECT DISTINCT bt.idBillingTemplate, bi.codeBillingChargeKind, bi.category, bi.description, bi.qty, bi.unitPrice, bi.totalPrice, bi.idBillingPeriod, bi.idPrice, bi.idPriceCategory, bi.idCoreFacility
FROM BillingTemplate bt
JOIN BillingItem bi on bt.targetClassIdentifier = bi.idProductOrder
WHERE bt.targetClassName = 'hci.gnomex.model.ProductOrder' AND
bi.idProductOrder IS NOT NULL AND bi.idMasterBillingItem IS NULL;

-- Add idMasterBillingItem to each BillingItem
Update bi
set bi.idMasterBillingItem = mbi.idMasterBillingItem
FROM BillingItem bi
JOIN MasterBillingItem mbi on
bi.idPrice = mbi.idPrice
JOIN BillingTemplate bt
on bt.idBillingTemplate = mbi.idBillingTemplate
WHERE bt.targetClassIdentifier = bi.idProductOrder AND
bt.targetClassName = 'hci.gnomex.model.ProductOrder' AND
bi.idProductOrder IS NOT NULL AND bi.idMasterBillingItem IS NULL;
---------------------------------------------------------------------------------
---------------------------------------------------------------------------------

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

--------------------------------------------------------------------
-- Drop IScanChip References
--------------------------------------------------------------------
-- Drop iScanChip column from request
ALTER TABLE Request DROP FOREIGN KEY FK_Request_IScanChip;
ALTER TABLE Request
DROP COLUMN idIScanChip;
CALL ExecuteIfTableExists('gnomex', 'Request_Audit', 'ALTER TABLE Request_Audit DROP COLUMN idIScanChip');
ALTER TABLE Request
DROP COLUMN numberIscanChips;
CALL ExecuteIfTableExists('gnomex', 'Request_Audit', 'ALTER TABLE Request_Audit DROP COLUMN numberIscanChips');

-- Drop iScanChip table
DROP TABLE IF EXISTS gnomex.IScanChip;
DROP TABLE IF EXISTS gnomex.IScanChip_Audit;

delimiter '//'

-- Function to create new request number
drop function if exists GetNextMicroarrayRequestNumber//
CREATE FUNCTION GetNextMicroarrayRequestNumber() RETURNS varchar(50) CHARSET utf8
    READS SQL DATA
BEGIN
 DECLARE nextNumber INT DEFAULT 0;
 DECLARE RequestNumber VARCHAR(50);

 INSERT INTO MicroArrayRequestNumber (dummy) VALUES ('');

 SELECT MAX(number) INTO nextNumber FROM MicroArrayRequestNumber;
 SELECT CONCAT(CAST(nextNumber AS CHAR),'R') INTO RequestNumber;
 RETURN RequestNumber;
END;
//

delimiter ';'


