alter table FlowCellChannel change read1ClustersPassedFilterM read1ClustersPassedFilterM decimal(6,2);

-- update any old user with USER permission to LAB
update AppUser set codeUserPermissionKind='LAB' where codeUserPermissionKind='USER';
update UserPermissionKind set isActive='N' where codeUserPermissionKind='User';

-- new properties
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
  values('experiment_default_view', 'my experiments', 'Indicates default experiment view for non-admin users.  Values are "my experiments", "my lab" or "all".','N');
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
  values('experiment_view_limit', '1000', 'Maximum number of experiments shown in experiment browse view.','N');
  
insert into PropertyDictionary(propertyName, propertyDescription, forServerOnly)
values('purchase_supplies_url', 'Link to purchase supplies for DNA SEQ CORE', 'N');

-- Add table PropertyAnalysisType
DROP TABLE IF EXISTS `gnomex`.`PropertyAnalysisType`;
CREATE TABLE gnomex.PropertyAnalysisType (
   idProperty INT(10) NOT NULL,
   idAnalysisType INT(10) NOT NULL,
   PRIMARY KEY (idProperty, idAnalysisType),
    CONSTRAINT FK_PropertyAnalysisType_Property FOREIGN KEY  (idProperty)
    REFERENCES gnomex.Property (idProperty)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_PropertyAnalysisType_AnalysisType FOREIGN KEY  (idAnalysisType)
    REFERENCES gnomex.AnalysisType (idAnalysisType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

-- Add table PlatformApplication
DROP TABLE IF EXISTS `gnomex`.`PlatformApplication`;
CREATE TABLE gnomex.PlatformApplication (
    idPlatformApplication INT(10) NOT NULL AUTO_INCREMENT,
    idProperty INT(10) NOT NULL,
    codeRequestCategory VARCHAR(10) NOT NULL,
    codeApplication VARCHAR(10),
    PRIMARY KEY (idPlatformApplication),
    CONSTRAINT FK_PlatformApplication_Property FOREIGN KEY (idProperty)
        REFERENCES gnomex.Property (idProperty)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_PlatformApplication_RequestCategory FOREIGN KEY (codeRequestCategory)
        REFERENCES gnomex.RequestCategory (codeRequestCategory)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_PlatformApplication_Application FOREIGN KEY (codeApplication)
        REFERENCES gnomex.Application (codeApplication)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT UNQ_PropertyPlatform_idProp_codeReq_app
    UNIQUE (`idProperty`, `codeRequestCategory`, `codeApplication`)
)  ENGINE=INNODB; 

-- Add table PropertyPlatformApplication
DROP TABLE IF EXISTS `gnomex`.`PropertyPlatformApplication`;
CREATE TABLE gnomex.PropertyPlatformApplication ( 
     idProperty INT(10),
     idPlatformApplication INT(10),
     PRIMARY KEY (idProperty, idPlatformApplication),
    CONSTRAINT FK_PropertyPlatformApplication_Property FOREIGN KEY FK_PropertyPlatformApplication_Property (idProperty)
    REFERENCES gnomex.Property (idProperty)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_PropertyPlatformApplication_PlatformApplication FOREIGN KEY FK_PropertyPlatformApplication_PlatformApplication (idPlatformApplication)
    REFERENCES gnomex.PlatformApplication (idPlatformApplication)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

-- Populate new table PlatformApplication from old table PropertyPlatform
INSERT INTO `gnomex`.`PlatformApplication`(`idProperty`, `codeRequestCategory`, `codeApplication`)
SELECT `idProperty`, `codeRequestCategory`, NULL
FROM `gnomex`.`PropertyPlatform`;

-- Populate table PropertyPlatformApplication
INSERT INTO `gnomex`.`PropertyPlatformApplication`(`idProperty`, `idPlatformApplication`)
SELECT `idProperty`, `idPlatformApplication`
FROM `gnomex`.`PlatformApplication`;

-- Add table BillingAccountUser
DROP TABLE IF EXISTS `gnomex`.`BillingAccountUser`;
CREATE TABLE `gnomex`.`BillingAccountUser` (
  `idBillingAccount` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  PRIMARY KEY (`idBillingAccount`, `idAppUser`),
  CONSTRAINT `FK_BillingAccountUser_AppUser` FOREIGN KEY `FK_BillingAccountUser_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingAccountUser_BillingAccount` FOREIGN KEY `FK_BillingAccountUser_BillingAccount` (`idBillingAccount`)
    REFERENCES `gnomex`.`BillingAccount` (`idBillingAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

-- new idCoreFacility column in BillingAccount
alter table gnomex.BillingAccount add column idCoreFacility INT(10) NULL;
alter table gnomex.BillingAccount add
  CONSTRAINT `FK_BillingAccount_CoreFacility` FOREIGN KEY  (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
