-- new tables for configuration of billing account for open source.
DROP TABLE IF EXISTS `gnomex`.`InternalAccountFieldsConfiguration`;
CREATE TABLE `gnomex`.`InternalAccountFieldsConfiguration` (
  idInternalAccountFieldsConfiguration INT(10) NOT NULL AUTO_INCREMENT,
  fieldName VARCHAR(50) NOT NULL,
  `include` CHAR(1) NULL,
  sortOrder INT(10) NULL,
  displayName VARCHAR(50) NOT NULL,
  `isRequired` CHAR(1) NOT NULL DEFAULT 'N',
  `isNumber` CHAR(1) NOT NULL DEFAULT 'N',
  minLength INT(10) NULL,
  maxLength INT(10) NULL,
  PRIMARY KEY (`idInternalAccountFieldsConfiguration`),
  UNIQUE KEY `UN_InternalAccountFieldsConfiguration` (fieldName)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`OtherAccountFieldsConfiguration`;
CREATE TABLE `gnomex`.`OtherAccountFieldsConfiguration` (
  idOtherAccountFieldsConfiguration INT(10) NOT NULL AUTO_INCREMENT,
  fieldName VARCHAR(50) NOT NULL,
  `include` CHAR(1) NULL,
  `isRequired` CHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`idOtherAccountFieldsConfiguration`),
  UNIQUE KEY `UN_OtherAccountFieldsConfiguration` (fieldName)
)
ENGINE = INNODB;

-- New custom fields for use in configuration of billing accounts.
alter table gnomex.BillingAccount add custom1 varchar(50) NULL;
alter table gnomex.BillingAccount add custom2 varchar(50) NULL;
alter table gnomex.BillingAccount add custom3 varchar(50) NULL;

-- New property to indicate if configurable billing is to be used.  Default to Y for open source.
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
  values('configurable_billing_accounts', 'Y', 'Y/N value indicating whether hard-coded or configuration billing account fields should be used.', 'N');

