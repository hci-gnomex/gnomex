use gnomex;


-- Add tables for Product ordering

DROP TABLE IF EXISTS `gnomex`.`ProductType`;
CREATE TABLE `gnomex`.`ProductType` (
  `codeProductType` VARCHAR(10) NOT NULL,
  `description` VARCHAR(5000) NULL,
  `idCoreFacility` INT(10) NULL,
  `idVendor` INT(10) NULL,
  `idPriceCategory` INT(10) NULL,
  PRIMARY KEY (`codeProductType`),
  CONSTRAINT `FK_ProductType_CoreFacility` FOREIGN KEY `FK_ProductType_CoreFacility` (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductType_Vendor` FOREIGN KEY `FK_ProductType_Vendor` (`idVendor`)
    REFERENCES `gnomex`.`Vendor` (`idVendor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductType_PriceCategory` FOREIGN KEY `FK_ProductType_PriceCategory` (`idPriceCategory`)
    REFERENCES `gnomex`.`PriceCategory` (`idPriceCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`ProductOrderStatus`;
CREATE TABLE `gnomex`.`ProductOrderStatus` (
  `codeProductOrderStatus` VARCHAR(10) NOT NULL,
  `productOrderStatus` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeProductOrderStatus`)
)
ENGINE = INNODB;

    
DROP TABLE IF EXISTS `gnomex`.`Product`;
CREATE TABLE `gnomex`.`Product` (
  `idProduct` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL,
  `codeProductType` VARCHAR(10) NULL,
  `idPrice` INT(10) NULL,
  `orderQty` INT(10) NULL,
  `useQty` INT(10) NULL,
  `catalogNumber` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idProduct`),
  CONSTRAINT `FK_Product_Price` FOREIGN KEY `FK_Product_Price` (`idPrice`)
    REFERENCES `gnomex`.`Price` (`idPrice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Product_ProductType` FOREIGN KEY `FK_Product_ProductType` (`codeProductType`)
    REFERENCES `gnomex`.`ProductType` (`codeProductType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`ProductOrder`;
CREATE TABLE `gnomex`.`ProductOrder` (
  `idProductOrder` INT(10) NOT NULL AUTO_INCREMENT,
  `idAppUser` INT(10) NULL,
  `idLab` INT(10) NULL,
  `idCoreFacility` INT(10) NULL,
  `submitDate` DATETIME NULL,
  `codeProductType` VARCHAR(10) NULL,
  `codeProductOrderStatus` VARCHAR(10) NULL,
  `quoteNumber` VARCHAR(50) NULL,
  `quoteReceivedDate` DATETIME NULL,
  `uuid` VARCHAR(36) NULL,
  PRIMARY KEY (`idProductOrder`),
  CONSTRAINT `FK_ProductOrder_AppUser` FOREIGN KEY `FK_ProductOrder_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductOrder_CoreFacility` FOREIGN KEY `FK_ProductOrder_CoreFacility` (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductOrder_Lab` FOREIGN KEY `FK_ProductOrder_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductOrder_ProductOrderStatus` FOREIGN KEY `FK_ProductOrder_ProductOrderStatus` (`codeProductOrderStatus`)
    REFERENCES `gnomex`.`ProductOrderStatus` (`codeProductOrderStatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductOrder_ProductType` FOREIGN KEY `FK_ProductOrder_ProductType` (`codeProductType`)
    REFERENCES `gnomex`.`ProductType` (`codeProductType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`ProductLedger`;
CREATE TABLE `gnomex`.`ProductLedger` (
  `idProductLedger` INT(10) NOT NULL AUTO_INCREMENT,
  `idLab` INT(10) NULL,
  `idProduct` INT(10) NULL,
  `qty` INT(10) NULL,
  `comment` VARCHAR(5000) NULL,
  `timeStame` DATETIME NULL,
  `idProductOrder` INT(10) NULL,
  `requestNumber` INT(10) NULL,
  PRIMARY KEY (`idProductLedger`),
  CONSTRAINT `FK_ProductLedger_Lab` FOREIGN KEY `FK_ProductLedger_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductLedger_Product` FOREIGN KEY `FK_ProductLedger_Product` (`idProduct`)
    REFERENCES `gnomex`.`Product` (`idProduct`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductLedger_ProductOrder` FOREIGN KEY `FK_ProductLedger_ProductOrder` (`idProductOrder`)
    REFERENCES `gnomex`.`ProductOrder` (`idProductOrder`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`ProductLineItem`;
CREATE TABLE `gnomex`.`ProductLineItem` (
  `idProductLineItem` INT(10) NOT NULL AUTO_INCREMENT,
  `idProductOrder` INT(10) NULL,
  `idProduct` INT(10) NULL,
  `qty` INT(10) NULL,
  `unitPrice` DECIMAL(7, 2) NULL,
  PRIMARY KEY (`idProductLineItem`),
  CONSTRAINT `FK_ProductLineItem_Product` FOREIGN KEY `FK_ProductLineItem_Product` (`idProduct`)
    REFERENCES `gnomex`.`Product` (`idProduct`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductLineItem_ProductOrder` FOREIGN KEY `FK_ProductLineItem_ProductOrder` (`idProductOrder`)
    REFERENCES `gnomex`.`ProductOrder` (`idProductOrder`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`ProductOrderFile`;
CREATE TABLE `gnomex`.`ProductOrderFile` (
  `idProductOrderFile` INT(10) NOT NULL AUTO_INCREMENT,
  `idProductOrder` INT(10) NULL,
  `fileName` VARCHAR(2000) NULL,
  `fileSize` DECIMAL(14, 0) NULL,
  `createDate` DATE NULL,
  PRIMARY KEY (`idProductOrderFile`),
  CONSTRAINT `FK_ProductOrderFile_ProductOrder` FOREIGN KEY `FK_ProductOrderFile_ProductOrder` (`idProductOrder`)
    REFERENCES `gnomex`.`ProductOrder` (`idProductOrder`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

-- Add idProductOrder to BillingItem
ALTER TABLE BillingItem ADD idProductOrder INT(10) NULL;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit add idProductOrder INT(10) null');
alter table BillingItem add 
  CONSTRAINT `FK_BillingItem_ProductOrder` FOREIGN KEY `FK_BillingItem_ProductOrder` (`idProductOrder`)
    REFERENCES `gnomex`.`ProductOrder` (`idProductOrder`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;


-- Uniqueness constraint for Property table
ALTER TABLE `Property` ADD UNIQUE (`name`);

-- Add columns to CoreFacility
ALTER TABLE CoreFacility ADD shortDescription VARCHAR(1000) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','alter table CoreFacility_Audit add shortDescription VARCHAR(1000) null');
ALTER TABLE CoreFacility ADD contactName VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','alter table CoreFacility_Audit add contactName VARCHAR(200) null');
ALTER TABLE CoreFacility ADD contactEmail VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','alter table CoreFacility_Audit add contactEmail VARCHAR(200) null');
ALTER TABLE CoreFacility ADD contactPhone VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','alter table CoreFacility_Audit add contactPhone VARCHAR(200) null');

-- New properties for gl interface.
Insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility)
	values('billing_gl_header_currency', 'USDX', 'currency on the gl interface header', 'Y', null);
Insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility)
	values('billing_gl_blank_year', 'N', 'leave year blank in gl line', 'Y', null);
	
-- Sort Order for CoreFacility
ALTER TABLE CoreFacility add sortOrder INT(10) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','ALTER TABLE CoreFacility_Audit add sortOrder INT(10) NULL');

-- Contact Image for CoreFacility
ALTER TABLE CoreFacility add contactImage VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','ALTER TABLE CoreFacility_Audit add contactImage VARCHAR(200) NULL');


DROP TABLE IF EXISTS `gnomex`.`AnnotationReportField`;
CREATE TABLE `gnomex`.`AnnotationReportField` (
  `idAnnotationReportField` INT(10) NOT NULL AUTO_INCREMENT, 
  `source` VARCHAR(50) NULL,
  `fieldName` VARCHAR(50) NULL,
  `display` VARCHAR(50) NULL,
  `isCustom` CHAR(1) NULL,
  `sortOrder` INT(10) NULL,
  `dictionaryLookUpTable` VARCHAR(100) NULL,
  PRIMARY KEY (`idAnnotationReportField`)
)
ENGINE = INNODB;


