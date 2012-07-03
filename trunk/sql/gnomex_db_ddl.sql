-- ----------------------------------------------------------------------
-- MySQL Migration Toolkit
-- SQL Create Script
-- ----------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `gnomex`
  CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `gnomex`;
-- -------------------------------------
-- Tables

DROP TABLE IF EXISTS `gnomex`.`Analysis`;
CREATE TABLE `gnomex`.`Analysis` (
  `idAnalysis` INT(10) NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(10) NULL,
  `name` VARCHAR(200) NULL,
  `description` LONGTEXT NULL,
  `idLab` INT(10) NULL,
  `idAnalysisType` INT(10) NULL,
  `idAnalysisProtocol` INT(10) NULL,
  `idOrganism` INT(10) NULL,
  `codeVisibility` VARCHAR(10) NOT NULL,
  `createDate` DATETIME NULL,
  `idAppUser` INT(10) NULL,
  `idInstitution` INT(10) NULL,
  `idCoreFacility` INT(10) NULL,
  `privacyExpirationDate` DATETIME NULL,
  PRIMARY KEY (`idAnalysis`),
  CONSTRAINT `FK_Analysis_Lab` FOREIGN KEY  (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_AnalysisType` FOREIGN KEY  (`idAnalysisType`)
    REFERENCES `gnomex`.`AnalysisType` (`idAnalysisType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_AnalysisProtocol` FOREIGN KEY  (`idAnalysisProtocol`)
    REFERENCES `gnomex`.`AnalysisProtocol` (`idAnalysisProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_Organism` FOREIGN KEY  (`idOrganism`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_AppUser` FOREIGN KEY  (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_Visibility` FOREIGN KEY  (`codeVisibility`)
    REFERENCES `gnomex`.`Visibility` (`codeVisibility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT FK_Analysis_Institution FOREIGN KEY  (idInstitution)
    REFERENCES gnomex.Institution (idInstitution)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_Analysis_CoreFacility FOREIGN KEY  (idCoreFacility)
    REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

    

DROP TABLE IF EXISTS `gnomex`.`AnalysisExperimentItem`;
CREATE TABLE `gnomex`.`AnalysisExperimentItem` (
  `idAnalysisExperimentItem` INT(10) NOT NULL AUTO_INCREMENT,
  `idSequenceLane` INT(10) NULL,
  `idHybridization` INT(10) NULL,
  `comments` VARCHAR(2000) NULL,
  `idAnalysis` INT(10) NULL,
  `idRequest` INT(10) NULL,
  PRIMARY KEY (`idAnalysisExperimentItem`),
  CONSTRAINT `FK_AnalysisExperimentItem_SequenceLane` FOREIGN KEY `FK_AnalysisExperimentItem_SequenceLane` (`idSequenceLane`)
    REFERENCES `gnomex`.`SequenceLane` (`idSequenceLane`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisExperimentItem_Hybridization` FOREIGN KEY `FK_AnalysisExperimentItem_Hybridization` (`idHybridization`)
    REFERENCES `gnomex`.`Hybridization` (`idHybridization`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisExperimentItem_Analysis` FOREIGN KEY `FK_AnalysisExperimentItem_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisExperimentItem_Request` FOREIGN KEY `FK_AnalysisExperimentItem_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`AnalysisFile`;
CREATE TABLE `gnomex`.`AnalysisFile` (
  `idAnalysisFile` INT(10) NOT NULL AUTO_INCREMENT,
  `fileName` VARCHAR(2000) NULL,
  `fileSize` DECIMAL(14,0) NULL,
  `comments` VARCHAR(2000) NULL,
  `uploadDate` DATETIME NULL,
  `idAnalysis` INT(10) NULL,
  `qualifiedFilePath` varchar(300) null,
  `baseFilePath` varchar(300) null,
  PRIMARY KEY (`idAnalysisFile`),
  CONSTRAINT `FK_AnalysisFile_Analysis` FOREIGN KEY `FK_AnalysisFile_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`AnalysisGroup`;
CREATE TABLE `gnomex`.`AnalysisGroup` (
  `idAnalysisGroup` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NULL,
  `description` LONGTEXT NULL,
  `idLab` INT(10) NULL,
  `codeVisibility` VARCHAR(10) NULL,
  `idAppUser` INT(10) NULL,
  ucscUrl varchar(250) null,
  PRIMARY KEY (`idAnalysisGroup`),
  CONSTRAINT `FK_AnalysisGroup_AppUser` FOREIGN KEY `FK_AnalysisGroup_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisGroup_Lab` FOREIGN KEY `FK_AnalysisGroup_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`AnalysisGroupItem`;
CREATE TABLE `gnomex`.`AnalysisGroupItem` (
  `idAnalysisGroup` INT(10) NOT NULL,
  `idAnalysis` INT(10) NOT NULL,
  PRIMARY KEY (`idAnalysisGroup`, `idAnalysis`),
  CONSTRAINT `FK_AnalysisGroupItem_AnalysisGroup` FOREIGN KEY `FK_AnalysisGroupItem_AnalysisGroup` (`idAnalysisGroup`)
    REFERENCES `gnomex`.`AnalysisGroup` (`idAnalysisGroup`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisGroupItem_Analysis` FOREIGN KEY `FK_AnalysisGroupItem_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`AnalysisCollaborator`;
CREATE TABLE `gnomex`.`AnalysisCollaborator` (
  `idAnalysis` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  `canUploadData` char(1) null,
  PRIMARY KEY (`idAnalysis`, `idAppUser`),
  CONSTRAINT `FK_AnalysisCollaborator_AppUser` FOREIGN KEY `FK_AnalysisCollaborator_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisCollaborator_Analysis` FOREIGN KEY `FK_AnalysisCollaborator_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`AnalysisGenomeBuild`;
CREATE TABLE `gnomex`.`AnalysisGenomeBuild` (
  `idAnalysis` INT(10) NOT NULL,
  `idGenomeBuild` INT(10) NOT NULL,
  PRIMARY KEY (`idAnalysis`, `idGenomeBuild`),
  CONSTRAINT `FK_AnalysisGenomeBuild_Analysis` FOREIGN KEY `FK_AnalysisGenomeBuild_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisGenomeBuild_GenomeBuild` FOREIGN KEY `FK_AnalysisGenomeBuild_GenomeBuild` (`idGenomeBuild`)
    REFERENCES `gnomex`.`GenomeBuild` (`idGenomeBuild`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`AnalysisProtocol`;
CREATE TABLE `gnomex`.`AnalysisProtocol` (
  `idAnalysisProtocol` INT(10) NOT NULL AUTO_INCREMENT,
  `analysisProtocol` VARCHAR(200) NULL,
  `description` LONGTEXT NULL,
  `url` VARCHAR(500) NULL,
  `idAnalysisType` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  PRIMARY KEY (`idAnalysisProtocol`),
  CONSTRAINT `FK_AnalysisProtocol_AnalysisType` FOREIGN KEY `FK_AnalysisProtocol_AnalysisType` (`idAnalysisType`)
    REFERENCES `gnomex`.`AnalysisType` (`idAnalysisType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisProtocol_AppUser` FOREIGN KEY `FK_AnalysisProtocol_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`AnalysisType`;
CREATE TABLE `gnomex`.`AnalysisType` (
  `idAnalysisType` INT(10) NOT NULL AUTO_INCREMENT,
  `analysisType` VARCHAR(200) NULL,
  `isActive` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  PRIMARY KEY (`idAnalysisType`),
  CONSTRAINT `FK_AnalysisType_AppUser` FOREIGN KEY `FK_AnalysisType_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ApplicationTheme`;
CREATE TABLE `gnomex`.`ApplicationTheme` (
  `idApplicationTheme` INT(10) NOT NULL AUTO_INCREMENT,
  `applicationTheme` VARCHAR(200) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idApplicationTheme`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`AppUser`;
CREATE TABLE `gnomex`.`AppUser` (
  `idAppUser` INT(10) NOT NULL AUTO_INCREMENT,
  `lastName` VARCHAR(200) NULL,
  `firstName` VARCHAR(200) NULL,
  `uNID` VARCHAR(50) NULL,
  `email` VARCHAR(200) NULL,
  `phone` VARCHAR(50) NULL,
  `department` VARCHAR(100) NULL,
  `institute` VARCHAR(100) NULL,
  `jobTitle` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  `codeUserPermissionKind` VARCHAR(10) NULL,
  `userNameExternal` VARCHAR(100) NULL,
  `passwordExternal` VARCHAR(100) NULL,
   `ucscUrl` VARCHAR(200) null,
  PRIMARY KEY (`idAppUser`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ArrayCoordinate`;
CREATE TABLE `gnomex`.`ArrayCoordinate` (
  `idArrayCoordinate` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NULL,
  `x` INT(10) NULL,
  `y` INT(10) NULL,
  `idSlideDesign` INT(10) NOT NULL,
  PRIMARY KEY (`idArrayCoordinate`),
  CONSTRAINT `FK_ArrayCoordinate_SlideDesign` FOREIGN KEY `FK_ArrayCoordinate_SlideDesign` (`idSlideDesign`)
    REFERENCES `gnomex`.`SlideDesign` (`idSlideDesign`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ArrayDesign`;
CREATE TABLE `gnomex`.`ArrayDesign` (
  `idArrayDesign` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL,
  `accessionNumberUArrayExpress` VARCHAR(100) NULL,
  `idArrayCoordinate` INT(10) NOT NULL,
  PRIMARY KEY (`idArrayDesign`),
  CONSTRAINT `FK_ArrayDesign_ArrayCoordinate` FOREIGN KEY `FK_ArrayDesign_ArrayCoordinate` (`idArrayCoordinate`)
    REFERENCES `gnomex`.`ArrayCoordinate` (`idArrayCoordinate`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


-- New table Assay
DROP TABLE IF EXISTS `gnomex`.`Assay`;
CREATE TABLE gnomex.Assay(
	idAssay int(10) auto_increment NOT NULL,
	name varchar(50) NOT NULL,
	description varchar(200) NULL,
	isActive char(1) NULL,
 PRIMARY KEY (idAssay) 
 ) ENGINE = INNODB;
 


DROP TABLE IF EXISTS `gnomex`.`BillingAccount`;
CREATE TABLE `gnomex`.`BillingAccount` (
  `idBillingAccount` INT(10) NOT NULL AUTO_INCREMENT,
  `accountName` VARCHAR(200) NULL,
  `accountNumber` VARCHAR(100) NULL,
  `expirationDate` DATETIME NULL,
  `idLab` INT(10) NULL,
  `accountNumberBus` VARCHAR(10) NULL,
  `accountNumberOrg` VARCHAR(10) NULL,
  `accountNumberFund` VARCHAR(10) NULL,
  `accountNumberActivity` VARCHAR(10) NULL,
  `accountNumberProject` VARCHAR(10) NULL,
  `accountNumberAccount` VARCHAR(10) NULL,
  `accountNumberAu` VARCHAR(10) NULL,
  `accountNumberYear` VARCHAR(10) NULL,
  `idFundingAgency` INT(10) NULL,
  `isPO` CHAR(1) NULL,
  `isApproved` CHAR(1) NULL,
  `approvedDate` DATETIME NULL,
  `createDate` DATETIME NULL,
  `submitterEmail` VARCHAR(200) NULL,
  `submitterUID` VARCHAR(50) NULL,
  `totalDollarAmount` DECIMAL(12,2) NULL,
  `purchaseOrderForm` LONGBLOB NULL,
  `orderFormFileType` VARCHAR(10) NULL,
    PRIMARY KEY (`idBillingAccount`),
  CONSTRAINT `FK_BillingAccount_Lab` FOREIGN KEY `FK_BillingAccount_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingAccount_FundingAgency` FOREIGN KEY `FK_BillingAccount_FundingAgency` (`idFundingAgency`)
    REFERENCES `gnomex`.`FundingAgency` (`idFundingAgency`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`BillingChargeKind`;
CREATE TABLE `gnomex`.`BillingChargeKind` (
  `codeBillingChargeKind` VARCHAR(10) NOT NULL,
  `billingChargeKind` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeBillingChargeKind`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`BillingItem`;
CREATE TABLE `gnomex`.`BillingItem` (
  `idBillingItem` INT(10) NOT NULL AUTO_INCREMENT,
  `codeBillingChargeKind` VARCHAR(10) NULL,
  `category` VARCHAR(200) NULL,
  `description` VARCHAR(500) NULL,
  `qty` INT(10) NULL,
  `unitPrice` DECIMAL(6, 2) NULL,
  `invoicePrice` DECIMAL(8, 2) NULL,
  `idBillingPeriod` INT(10) NULL,
  `codeBillingStatus` VARCHAR(10) NULL,
  `idPriceCategory` INT(10) NULL,
  `idPrice` INT(10) NULL,
  `idRequest` INT(10) NOT NULL,
  `idBillingAccount` INT(10) NOT NULL,
  `percentagePrice` DECIMAL(3, 2) NOT NULL,
  `notes` VARCHAR(500) NULL,
  `idLab` INT(10) NOT NULL,
  `completeDate` DATETIME NULL,
  `splitType` CHAR(1) NULL,
  `idCoreFacility` INT(10) NULL,
  PRIMARY KEY (`idBillingItem`),
  CONSTRAINT `FK_BillingItem_PriceCategory` FOREIGN KEY  (`idPriceCategory`)
    REFERENCES `gnomex`.`PriceCategory` (`idPriceCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingChargeKind` FOREIGN KEY  (`codeBillingChargeKind`)
    REFERENCES `gnomex`.`BillingChargeKind` (`codeBillingChargeKind`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingStatus` FOREIGN KEY  (`codeBillingStatus`)
    REFERENCES `gnomex`.`BillingStatus` (`codeBillingStatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingAccount` FOREIGN KEY  (`idBillingAccount`)
    REFERENCES `gnomex`.`BillingAccount` (`idBillingAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_Price` FOREIGN KEY  (`idPrice`)
    REFERENCES `gnomex`.`Price` (`idPrice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_Lab` FOREIGN KEY  (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_Request` FOREIGN KEY  (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingPeriod` FOREIGN KEY  (`idBillingPeriod`)
    REFERENCES `gnomex`.`BillingPeriod` (`idBillingPeriod`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_CoreFacility` FOREIGN KEY  (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`BillingPeriod`;
CREATE TABLE `gnomex`.`BillingPeriod` (
  `idBillingPeriod` INT(10) NOT NULL AUTO_INCREMENT,
  `billingPeriod` VARCHAR(50) NOT NULL,
  `startDate` DATETIME NOT NULL,
  `endDate` DATETIME NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idBillingPeriod`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`BillingSlideProductClass`;
CREATE TABLE `gnomex`.`BillingSlideProductClass` (
  `idBillingSlideProductClass` INT(10) NOT NULL AUTO_INCREMENT,
  `billingSlideProductClass` VARCHAR(200) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idBillingSlideProductClass`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`BillingSlideServiceClass`;
CREATE TABLE `gnomex`.`BillingSlideServiceClass` (
  `idBillingSlideServiceClass` INT(10) NOT NULL AUTO_INCREMENT,
  `billingSlideServiceClass` VARCHAR(200) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idBillingSlideServiceClass`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`BillingStatus`;
CREATE TABLE `gnomex`.`BillingStatus` (
  `codeBillingStatus` VARCHAR(10) NOT NULL,
  `billingStatus` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeBillingStatus`)
)
ENGINE = INNODB;




DROP TABLE IF EXISTS `gnomex`.`PriceSheet`;
CREATE TABLE `gnomex`.`PriceSheet` (
  `idPriceSheet` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL,
  `description` VARCHAR(500) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idPriceSheet`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`PriceSheetRequestCategory`;
CREATE TABLE `gnomex`.`PriceSheetRequestCategory` (
  `idPriceSheet` INT(10) NOT NULL,
  `codeRequestCategory` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idPriceSheet`, `codeRequestCategory`),
  CONSTRAINT `FK_PriceSheetRequestCategory_RequestCategory` FOREIGN KEY `FK_PriceSheetRequestCategory_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PriceSheetRequestCategory_PriceSheet` FOREIGN KEY `FK_PriceSheetRequestCategory_PriceSheet` (`idPriceSheet`)
    REFERENCES `gnomex`.`PriceSheet` (`idPriceSheet`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`PriceSheetPriceCategory`;
CREATE TABLE `gnomex`.`PriceSheetPriceCategory` (
  `idPriceSheet` INT(10) NOT NULL,
  `idPriceCategory` INT(10) NOT NULL,
  `sortOrder` INT(10) NULL,
  PRIMARY KEY (`idPriceSheet`, `idPriceCategory`),
  CONSTRAINT `FK_PriceSheetPriceCategory_PriceSheet` FOREIGN KEY `FK_PriceSheetPriceCategory_PriceSheet` (`idPriceSheet`)
    REFERENCES `gnomex`.`PriceSheet` (`idPriceSheet`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PriceSheetPriceCategory_PriceCategory` FOREIGN KEY `FK_PriceSheetPriceCategory_PriceCategory` (`idPriceCategory`)
    REFERENCES `gnomex`.`PriceCategory` (`idPriceCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`PriceCategory`;
CREATE TABLE `gnomex`.`PriceCategory` (
  `idPriceCategory` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `description` VARCHAR(500) NULL,
  `codeBillingChargeKind` VARCHAR(10) NULL,
  `pluginClassName` VARCHAR(500) NULL,
  `dictionaryClassNameFilter1` VARCHAR(500) NULL,
  `dictionaryClassNameFilter2` VARCHAR(500) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idPriceCategory`),
  CONSTRAINT `FK_PriceCategory_BillingChargeKind` FOREIGN KEY `FK_PriceCategory_BillingChargeKind` (`codeBillingChargeKind`)
    REFERENCES `gnomex`.`BillingChargeKind` (`codeBillingChargeKind`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`Price`;
CREATE TABLE `gnomex`.`Price` (
  `idPrice` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL,
  `description` VARCHAR(500) NULL,
  `unitPrice` DECIMAL(6, 2) NULL,
  `unitPriceExternalAcademic` DECIMAL(6, 2) NULL,
  `idPriceCategory` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idPrice`),
  CONSTRAINT `FK_Price_PriceCategory` FOREIGN KEY `FK_Price_PriceCategory` (`idPriceCategory`)
    REFERENCES `gnomex`.`PriceCategory` (`idPriceCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`PriceCriteria`;
CREATE TABLE `gnomex`.`PriceCriteria` (
  `idPriceCriteria` INT(10) NOT NULL AUTO_INCREMENT,
  `filter1` VARCHAR(10) NULL,
  `filter2` VARCHAR(10) NULL,
  `idPrice` INT(10) NULL,
  PRIMARY KEY (`idPriceCriteria`),
  CONSTRAINT `FK_PriceCriteria_Price` FOREIGN KEY `FK_PriceCriteria_Price` (`idPrice`)
    REFERENCES `gnomex`.`Price` (`idPrice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`BioanalyzerChipType`;
CREATE TABLE `gnomex`.`BioanalyzerChipType` (
  `codeBioanalyzerChipType` VARCHAR(10) NOT NULL,
  `bioanalyzerChipType` VARCHAR(50) NULL,
  `concentrationRange` VARCHAR(50) NULL,
  `maxSampleBufferStrength` VARCHAR(50) NULL,
  `costPerSample` DECIMAL(5, 2) NULL,
  `sampleWellsPerChip` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  `codeConcentrationUnit` VARCHAR(10) NULL,
  PRIMARY KEY (`codeBioanalyzerChipType`)
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`Chromatogram`;
CREATE TABLE `gnomex`.`Chromatogram` (
  `idChromatogram` int(10) NOT NULL AUTO_INCREMENT,
  `idPlateWell` int(10) NULL,
  `idRequest` int(10) NULL,
  `displayName` varchar(200) NULL,
  `readLength` int(10) NULL,
  `trimmedLength` int NULL,
  `q20` int(10) NULL,
  `q40` int (10) NULL,
  `aSignalStrength` int(10) NULL,
  `cSignalStrength` int(10) NULL,
  `gSignalStrength` int(10) NULL,
  `tSignalStrength` int(10) NULL,
  `releaseDate` DATETIME NULL,
  `qualifiedFilePath` varchar(500) null,
  `idReleaser` int(10) null,
  PRIMARY KEY (`idChromatogram`),
  CONSTRAINT `FK_Chromatogram_PlateWell` FOREIGN KEY `FK_Chromatogram_PlateWell` (`idPlateWell`)
    REFERENCES `gnomex`.`PlateWell` (`idPlateWell`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Chromatogram_Request` FOREIGN KEY `FK_Chromatogram_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;



DROP TABLE IF EXISTS `gnomex`.`ConcentrationUnit`;
CREATE TABLE `gnomex`.`ConcentrationUnit` (
  `codeConcentrationUnit` VARCHAR(10) NOT NULL,
  `concentrationUnit` VARCHAR(50) NOT NULL,
  `mageOntologyCode` VARCHAR(50) NULL,
  `mageOntologyDefinition` VARCHAR(5000) NULL,
  `isActive` CHAR(1) NOT NULL,
  PRIMARY KEY (`codeConcentrationUnit`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`CoreFacility`;
CREATE TABLE `gnomex`.`CoreFacility` (
  `idCoreFacility` INT(10) NOT NULL AUTO_INCREMENT,
  `facilityName` varchar(200) NULL,
  `isActive` CHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`idCoreFacility`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`dtproperties`;
CREATE TABLE `gnomex`.`dtproperties` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `objectid` INT(10) NULL,
  `property` VARCHAR(64) NOT NULL,
  `value` VARCHAR(255) NULL,
  `uvalue` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `lvalue` LONGBLOB NULL,
  `version` INT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`, `property`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ExperimentDesign`;
CREATE TABLE `gnomex`.`ExperimentDesign` (
  `codeExperimentDesign` VARCHAR(10) NOT NULL,
  `experimentDesign` VARCHAR(50) NULL,
  `mageOntologyCode` VARCHAR(50) NULL,
  `mageOntologyDefinition` VARCHAR(5000) NULL,
  `isActive` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  PRIMARY KEY (`codeExperimentDesign`),
  CONSTRAINT `FK_ExperimentDesign_AppUser` FOREIGN KEY `FK_ExperimentDesign_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ExperimentDesignEntry`;
CREATE TABLE `gnomex`.`ExperimentDesignEntry` (
  `idExperimentDesignEntry` INT(10) NOT NULL AUTO_INCREMENT,
  `codeExperimentDesign` VARCHAR(10) NULL,
  `idProject` INT(10) NULL,
  `valueString` VARCHAR(100) NULL,
  `otherLabel` VARCHAR(100) NULL,
  PRIMARY KEY (`idExperimentDesignEntry`),
  CONSTRAINT `FK_ExperimentDesignEntry_ExperimentDesign` FOREIGN KEY `FK_ExperimentDesignEntry_ExperimentDesign` (`codeExperimentDesign`)
    REFERENCES `gnomex`.`ExperimentDesign` (`codeExperimentDesign`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ExperimentDesignEntry_Project` FOREIGN KEY `FK_ExperimentDesignEntry_Project` (`idProject`)
    REFERENCES `gnomex`.`Project` (`idProject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ExperimentFactor`;
CREATE TABLE `gnomex`.`ExperimentFactor` (
  `codeExperimentFactor` VARCHAR(10) NOT NULL,
  `experimentFactor` VARCHAR(50) NULL,
  `mageOntologyCode` VARCHAR(50) NULL,
  `mageOntologyDefinition` VARCHAR(5000) NULL,
  `isActive` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  PRIMARY KEY (`codeExperimentFactor`),
  CONSTRAINT `FK_ExperimentFactor_AppUser` FOREIGN KEY `FK_ExperimentFactor_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ExperimentFactorEntry`;
CREATE TABLE `gnomex`.`ExperimentFactorEntry` (
  `idExperimentFactorEntry` INT(10) NOT NULL AUTO_INCREMENT,
  `codeExperimentFactor` VARCHAR(10) NULL,
  `idProject` INT(10) NULL,
  `valueString` VARCHAR(100) NULL,
  `otherLabel` VARCHAR(100) NULL,
  PRIMARY KEY (`idExperimentFactorEntry`),
  CONSTRAINT `FK_ExperimentFactorEntry_ExperimentFactor` FOREIGN KEY `FK_ExperimentFactorEntry_ExperimentFactor` (`codeExperimentFactor`)
    REFERENCES `gnomex`.`ExperimentFactor` (`codeExperimentFactor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ExperimentFactorEntry_Project` FOREIGN KEY `FK_ExperimentFactorEntry_Project` (`idProject`)
    REFERENCES `gnomex`.`Project` (`idProject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`FeatureExtractionProtocol`;
CREATE TABLE `gnomex`.`FeatureExtractionProtocol` (
  `idFeatureExtractionProtocol` INT(10) NOT NULL AUTO_INCREMENT,
  `featureExtractionProtocol` VARCHAR(200) NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  `description` LONGTEXT NULL,
  `url` VARCHAR(500) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idFeatureExtractionProtocol`),
  CONSTRAINT `FK_FeatureExtractionProtocol_RequestCategory` FOREIGN KEY `FK_FeatureExtractionProtocol_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ExperimentFile`;
CREATE TABLE `gnomex`.`ExperimentFile` (
  `idExperimentFile` INT(10) NOT NULL AUTO_INCREMENT,
  `fileName` VARCHAR(2000) NULL,
  `fileSize` DECIMAL(14,0) NULL,
  `idRequest` INT(10) NULL,
  PRIMARY KEY (`idExperimentFile`),
  CONSTRAINT `FK_ExperimentFile_Request` FOREIGN KEY `FK_RequestFile_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`FlowCell`;
CREATE TABLE `gnomex`.`FlowCell` (
  `idFlowCell` INT(10) NOT NULL AUTO_INCREMENT,
  `idNumberSequencingCycles` INT(10) NULL,
  `idSeqRunType` INT(10) NULL,
  `number` VARCHAR(10) NULL,
  `createDate` DATETIME NULL,
  `notes` VARCHAR(200) NULL,
  `barcode` VARCHAR(100) NULL,
  `codeSequencingPlatform` VARCHAR(10) NULL,
  PRIMARY KEY (`idFlowCell`),
  CONSTRAINT `FK_FlowCell_NumberSequencingCycles` FOREIGN KEY `FK_FlowCell_NumberSequencingCycles` (`idNumberSequencingCycles`)
    REFERENCES `gnomex`.`NumberSequencingCycles` (`idNumberSequencingCycles`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_FlowCell_SeqRunType` FOREIGN KEY `FK_FlowCell_SeqRunType` (`idSeqRunType`)
    REFERENCES `gnomex`.`SeqRunType` (`idSeqRunType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_FlowCell_SequencingPlatform` FOREIGN KEY `FK_FlowCell_SequencingPlatform` (`codeSequencingPlatform`)
    REFERENCES `gnomex`.`SequencingPlatform` (`codeSequencingPlatform`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`FlowCellChannel`;
CREATE TABLE `gnomex`.`FlowCellChannel` (
  `idFlowCellChannel` INT(10) NOT NULL AUTO_INCREMENT,
  `idFlowCell` INT(10) NULL,
  `number` INT(10) NULL,
  `idSequenceLane` INT(10) NULL,
  `idSequencingControl` INT(10) NULL,
  `numberSequencingCyclesActual` INT(10) NULL,
  `clustersPerTile` INT(10) NULL,
  `fileName` VARCHAR(500) NULL,
  `startDate` DATETIME NULL,
  `firstCycleDate` DATETIME NULL,
  `firstCycleCompleted` CHAR(1) NULL,
  `firstCycleFailed` CHAR(1) NULL,
  `lastCycleDate` DATETIME NULL,
  `lastCycleCompleted` CHAR(1) NULL,
  `lastCycleFailed` CHAR(1) NULL,
  `sampleConcentrationpM` DECIMAL(8, 2) NULL,
  `pipelineDate` DATETIME NULL,
  `pipelineFailed` CHAR(1) NULL,
  `isControl` CHAR(1) NULL,
  `phiXErrorRate` DECIMAL(4, 4) NULL,
  `read1ClustersPassedFilterM` INT(10) NULL,
  `read2ClustersPassedFilterM` INT(10) NULL,
  `q30Gb` DECIMAL(4,1) NULL,
  `q30Percent` DECIMAL(4,3) NULL,
  PRIMARY KEY (`idFlowCellChannel`),
  CONSTRAINT `FK_FlowCellChannel_FlowCell` FOREIGN KEY `FK_FlowCellChannel_FlowCell` (`idFlowCell`)
    REFERENCES `gnomex`.`FlowCell` (`idFlowCell`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_FlowCellChannel_SequencingControl` FOREIGN KEY `FK_FlowCellChannel_SequencingControl` (`idSequencingControl`)
    REFERENCES `gnomex`.`SequencingControl` (`idSequencingControl`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_FlowCellChannel_SequenceLane` FOREIGN KEY `FK_FlowCellChannel_SequenceLane` (`idSequenceLane`)
    REFERENCES `gnomex`.`SequenceLane` (`idSequenceLane`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`FundingAgency`;
CREATE TABLE `gnomex`.`FundingAgency` (
  `idFundingAgency` INT(10) NOT NULL AUTO_INCREMENT,
  `fundingAgency` VARCHAR(200) NULL,
  `isPeerReviewedFunding` CHAR(1) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idFundingAgency`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`GenomeBuild`;
CREATE TABLE `gnomex`.`GenomeBuild` (
  `idGenomeBuild` INT(10) NOT NULL AUTO_INCREMENT,
  `genomeBuildName` VARCHAR(500) NOT NULL,
  `idOrganism` INT(10) NOT NULL,
  `isActive` CHAR(1) NULL,
  `isLatestBuild` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  `das2Name` varchar(200) NULL,  
  `buildDate` datetime  NULL,
  `coordURI` varchar(2000)  NULL,
  `coordVersion` varchar(50)  NULL,
  `coordSource` varchar(50)  NULL,
  `coordTestRange` varchar(100)  NULL,
  `coordAuthority` varchar(50)  NULL,
  `ucscName` varchar(100)  NULL,
  `dataPath` varchar(500)  NULL,
  PRIMARY KEY (`idGenomeBuild`),
  UNIQUE KEY `Index_GenomeBuildDas2Name` (`das2Name`),
  CONSTRAINT `FK_GenomeBuild_Organism` FOREIGN KEY `FK_GenomeBuild_Organism` (`idOrganism`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_GenomeBuild_AppUser` FOREIGN KEY `FK_GenomeBuild_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`HybProtocol`;
CREATE TABLE `gnomex`.`HybProtocol` (
  `idHybProtocol` INT(10) NOT NULL AUTO_INCREMENT,
  `hybProtocol` VARCHAR(200) NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  `description` LONGTEXT NULL,
  `url` VARCHAR(500) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idHybProtocol`),
  CONSTRAINT `FK_HybProtocol_RequestCategory` FOREIGN KEY `FK_HybProtocol_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Hybridization`;
CREATE TABLE `gnomex`.`Hybridization` (
  `idHybridization` INT(10) NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(100) NULL,
  `notes` VARCHAR(2000) NULL,
  `codeSlideSource` VARCHAR(10) NULL,
  `idSlideDesign` INT(10) NULL,
  `idHybProtocol` INT(10) NULL,
  `idHybBuffer` INT(10) NULL,
  `idLabeledSampleChannel1` INT(10) NULL,
  `idLabeledSampleChannel2` INT(10) NULL,
  `idSlide` INT(10) NULL,
  `idArrayCoordinate` INT(10) NULL,
  `idScanProtocol` INT(10) NULL,
  `idFeatureExtractionProtocol` INT(10) NULL,
  `hybDate` DATETIME NULL,
  `hybFailed` CHAR(1) NULL,
  `hybBypassed` CHAR(1) NULL,
  `extractionDate` DATETIME NULL,
  `extractionFailed` CHAR(1) NULL,
  `extractionBypassed` CHAR(1) NULL,
  `hasResults` CHAR(1) NULL,
  `createDate` DATETIME NULL,
  PRIMARY KEY (`idHybridization`),
  CONSTRAINT `FK_Hybridization_SlideSource` FOREIGN KEY `FK_Hybridization_SlideSource` (`codeSlideSource`)
    REFERENCES `gnomex`.`SlideSource` (`codeSlideSource`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Hybridization_SlideDesign` FOREIGN KEY `FK_Hybridization_SlideDesign` (`idSlideDesign`)
    REFERENCES `gnomex`.`SlideDesign` (`idSlideDesign`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Hybridization_Slide` FOREIGN KEY `FK_Hybridization_Slide` (`idSlide`)
    REFERENCES `gnomex`.`Slide` (`idSlide`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Hybridization_ArrayCoordinate` FOREIGN KEY `FK_Hybridization_ArrayCoordinate` (`idArrayCoordinate`)
    REFERENCES `gnomex`.`ArrayCoordinate` (`idArrayCoordinate`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Hybridization_LabeledSample` FOREIGN KEY `FK_Hybridization_LabeledSample` (`idLabeledSampleChannel1`)
    REFERENCES `gnomex`.`LabeledSample` (`idLabeledSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Hybridization_LabeledSample1` FOREIGN KEY `FK_Hybridization_LabeledSample1` (`idLabeledSampleChannel2`)
    REFERENCES `gnomex`.`LabeledSample` (`idLabeledSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Hybridization_HybProtocol` FOREIGN KEY `FK_Hybridization_HybProtocol` (`idHybProtocol`)
    REFERENCES `gnomex`.`HybProtocol` (`idHybProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Hybridization_ScanProtocol` FOREIGN KEY `FK_Hybridization_ScanProtocol` (`idScanProtocol`)
    REFERENCES `gnomex`.`ScanProtocol` (`idScanProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Hybridization_FeatureExtractionProtocol` FOREIGN KEY `FK_Hybridization_FeatureExtractionProtocol` (`idFeatureExtractionProtocol`)
    REFERENCES `gnomex`.`FeatureExtractionProtocol` (`idFeatureExtractionProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`InstrumentRun`;
CREATE TABLE `gnomex`.`InstrumentRun` (
  `idInstrumentRun` INT(10) NOT NULL AUTO_INCREMENT,
  `runDate` DATETIME NULL,
  `createDate` DATETIME NULL,
  `codeInstrumentRunStatus` VARCHAR(10) NULL,
  `comments` VARCHAR(200) NULL,
  `label` VARCHAR(100) NULL,
  `codeReactionType` VARCHAR(10) NULL,
  `creator` VARCHAR(50) NULL,
  `codeSealType` VARCHAR(10) NULL,
  PRIMARY KEY (`idInstrumentRun`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`InstrumentRunStatus`;
CREATE TABLE `gnomex`.`InstrumentRunStatus` (
  `codeInstrumentRunStatus` VARCHAR(10) NOT NULL,
  `instrumentRunStatus` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeInstrumentRunStatus`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Lab`;
CREATE TABLE `gnomex`.`Lab` (
  `idLab` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL,
  `department` VARCHAR(200) NULL,
  `notes` VARCHAR(500) NULL,
  `contactName` VARCHAR(200) NULL,
  `contactAddress` VARCHAR(200) NULL,
  `contactCodeState` VARCHAR(10) NULL,
  `contactZip` VARCHAR(50) NULL,
  `contactCity` VARCHAR(200) NULL,
  `contactPhone` VARCHAR(50) NULL,
  `contactEmail` VARCHAR(200) NULL,
  `isCCSGMember` CHAR(1) NULL,
  `firstName` VARCHAR(200) NULL,
  `lastName` VARCHAR(200) NULL,
  `isExternalPricing` VARCHAR(1) NULL,
  `isActive` VARCHAR(1) NULL,
  `excludeUsage` VARCHAR(1) NULL,
  PRIMARY KEY (`idLab`),
  CONSTRAINT `FK_Lab_State` FOREIGN KEY `FK_Lab_State` (`contactCodeState`)
    REFERENCES `gnomex`.`State` (`codeState`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`LabCollaborator`;
CREATE TABLE `gnomex`.`LabCollaborator` (
  `idLab` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  `sendUploadAlert` CHAR(1) NULL,
  PRIMARY KEY (`idLab`, `idAppUser`),
  CONSTRAINT `FK_LabCollaborator_Lab` FOREIGN KEY `FK_LabCollaborator_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_LabCollaborator_AppUser` FOREIGN KEY `FK_LabCollaborator_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

-- Add new Table Institution
DROP TABLE IF EXISTS gnomex.Institution;
CREATE TABLE gnomex.Institution ( 
    idInstitution	INT(10) NOT NULL AUTO_INCREMENT,
    institution 	varchar(200) NOT NULL,
    description  	varchar(500) NULL,
    isActive     	char(1) NULL,
    PRIMARY KEY (idInstitution)
    )
ENGINE = INNODB;
    
    
-- Add new Table to link lab to multiple institutions
DROP TABLE IF EXISTS gnomex.InstitutionLab;
CREATE TABLE gnomex.InstitutionLab ( 
    idInstitution	INT(10),
    idLab           INT(10),
    PRIMARY KEY (idInstitution, idLab),
    CONSTRAINT FK_InstitutionLab_Institution FOREIGN KEY FK_InstitutionLab_Institution (idInstitution)
    REFERENCES gnomex.Institution (idInstitution)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_InstitutionLab_Lab FOREIGN KEY FK_InstitutionLab_Lab (idLab)
    REFERENCES gnomex.Lab (idLab)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    )
ENGINE = INNODB;    

DROP TABLE IF EXISTS `gnomex`.`Label`;
CREATE TABLE `gnomex`.`Label` (
  `idLabel` INT(10) NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idLabel`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`LabeledSample`;
CREATE TABLE `gnomex`.`LabeledSample` (
  `idLabeledSample` INT(10) NOT NULL AUTO_INCREMENT,
  `labelingYield` DECIMAL(8, 2) NULL,
  `idSample` INT(10) NULL,
  `idLabel` INT(10) NULL,
  `idLabelingProtocol` INT(10) NULL,
  `codeLabelingReactionSize` VARCHAR(20) NULL,
  `numberOfReactions` INT(10) NULL,
  `labelingDate` DATETIME NULL,
  `labelingFailed` CHAR(1) NULL,
  `labelingBypassed` CHAR(1) NULL,
  `idRequest` INT(10) NULL,
  PRIMARY KEY (`idLabeledSample`),
  CONSTRAINT `FK_LabeledSample_Sample` FOREIGN KEY `FK_LabeledSample_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_LabeledSample_Label` FOREIGN KEY `FK_LabeledSample_Label` (`idLabel`)
    REFERENCES `gnomex`.`Label` (`idLabel`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_LabeledSample_LabelingReactionSize` FOREIGN KEY `FK_LabeledSample_LabelingReactionSize` (`codeLabelingReactionSize`)
    REFERENCES `gnomex`.`LabelingReactionSize` (`codeLabelingReactionSize`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_LabeledSample_LabelingProtocol` FOREIGN KEY `FK_LabeledSample_LabelingProtocol` (`idLabelingProtocol`)
    REFERENCES `gnomex`.`LabelingProtocol` (`idLabelingProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`LabelingProtocol`;
CREATE TABLE `gnomex`.`LabelingProtocol` (
  `idLabelingProtocol` INT(10) NOT NULL AUTO_INCREMENT,
  `labelingProtocol` VARCHAR(200) NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  `description` LONGTEXT NULL,
  `url` VARCHAR(500) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idLabelingProtocol`),
  CONSTRAINT `FK_LabelingProtocol_RequestCategory` FOREIGN KEY `FK_LabelingProtocol_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`LabelingReactionSize`;
CREATE TABLE `gnomex`.`LabelingReactionSize` (
  `codeLabelingReactionSize` VARCHAR(20) NOT NULL,
  `labelingReactionSize` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  `sortOrder` INT(10) NULL,
  PRIMARY KEY (`codeLabelingReactionSize`),
  INDEX `IX_LabelingReactionSize` (`sortOrder`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`LabManager`;
CREATE TABLE `gnomex`.`LabManager` (
  `idLab` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  `sendUploadAlert` CHAR(1) NULL,
  PRIMARY KEY (`idLab`, `idAppUser`),
  CONSTRAINT `FK_LabManager_Lab` FOREIGN KEY `FK_LabManager_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_LabManager_AppUser` FOREIGN KEY `FK_LabManager_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`LabUser`;
CREATE TABLE `gnomex`.`LabUser` (
  `idLab` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  `sortOrder` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  `sendUploadAlert` CHAR(1) NULL,
  PRIMARY KEY (`idLab`, `idAppUser`),
  CONSTRAINT `FK_LabUser_AppUser` FOREIGN KEY `FK_LabUser_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_LabUser_Lab` FOREIGN KEY `FK_LabUser_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Application`;
CREATE TABLE `gnomex`.`Application` (
  `codeApplication` VARCHAR(10) NOT NULL,
  `application` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  `idApplicationTheme` INT(10) NULL,
  `sortOrder` INT(10) NULL,
  `avgInsertSizeFrom` INT(10) NULL,
  `avgInsertSizeTo` INT(10) NULL,
  `hasCaptureLibDesign` CHAR(1) NULL,
  PRIMARY KEY (`codeApplication`),
  CONSTRAINT `FK_Application_ApplicationTheme` FOREIGN KEY `FK_Application_ApplicationTheme` (`idApplicationTheme`)
    REFERENCES `gnomex`.`ApplicationTheme` (`idApplicationTheme`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`NumberSequencingCycles`;
CREATE TABLE `gnomex`.`NumberSequencingCycles` (
  `idNumberSequencingCycles` INT(10) NOT NULL AUTO_INCREMENT,
  `numberSequencingCycles` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  `sortOrder` INT(10) NULL,
  `notes` VARCHAR(500) NULL,
  PRIMARY KEY (`idNumberSequencingCycles`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`NumberSequencingCyclesAllowed`;
CREATE TABLE `gnomex`.`NumberSequencingCyclesAllowed` (
  `idNumberSequencingCyclesAllowed` INT(10) NOT NULL AUTO_INCREMENT,
  `idNumberSequencingCycles` INT(10) NOT NULL,
  `codeRequestCategory` VARCHAR(10) NOT NULL,
   idSeqRunType INT(10) NULL,
   name varchar(100)  NOT NULL,
   notes varchar(500) NULL,
  PRIMARY KEY (`idNumberSequencingCyclesAllowed`),
  CONSTRAINT `FK_NumberSequencingCyclesAllowed_RequestCategory` FOREIGN KEY `FK_NumberSequencingCyclesAllowed_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_NumberSequencingCyclesAllowed_NumberSequencingCycles` FOREIGN KEY `FK_NumberSequencingCyclesAllowed_NumberSequencingCycles` (`idNumberSequencingCycles`)
    REFERENCES `gnomex`.`NumberSequencingCycles` (`idNumberSequencingCycles`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT FK_NumberSequencingCyclesAllowed_SeqRunType FOREIGN KEY FK_NumberSequencingCyclesAllowed_SeqRunType (idSeqRunType)
    REFERENCES gnomex.SeqRunType (idSeqRunType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`OligoBarcodeScheme`;
CREATE TABLE `gnomex`.`OligoBarcodeScheme` (
  `idOligoBarcodeScheme` INT(10) NOT NULL AUTO_INCREMENT,
  `oligoBarcodeScheme` VARCHAR(200) NULL,
  `description` VARCHAR(2000) NULL,
  `isActive` CHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`idOligoBarcodeScheme`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`OligoBarcode`;
CREATE TABLE `gnomex`.`OligoBarcode` (
  `idOligoBarcode` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NULL,
  `barcodeSequence` VARCHAR(20) NOT NULL,
  `idOligoBarcodeScheme` INT(10) NOT NULL,
  `sortOrder` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idOligoBarcode`),
  CONSTRAINT `FK_OligoBarcode_OligoBarcodeScheme` FOREIGN KEY `FK_OligoBarcode_OligoBarcodeScheme` (`idOligoBarcodeScheme`)
    REFERENCES `gnomex`.`OligoBarcodeScheme` (`idOligoBarcodeScheme`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`OligoBarcodeSchemeAllowed`;
CREATE TABLE `gnomex`.`OligoBarcodeSchemeAllowed` (
  `idOligoBarcodeSchemeAllowed` INT(10) NOT NULL AUTO_INCREMENT,
  `idOligoBarcodeScheme` INT(10) NOT NULL,
  `codeRequestCategory` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idOligoBarcodeSchemeAllowed`),
  CONSTRAINT `FK_OligoBarcodeSchemeAllowed_RequestCategory` FOREIGN KEY `FK_OligoBarcodeSchemeAllowed_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_OligoBarcodeSchemeAllowed_idOligoBarcodeScheme` FOREIGN KEY `FK_OligoBarcodeSchemeAllowed_idOligoBarcodeScheme` (`idOligoBarcodeScheme`)
    REFERENCES `gnomex`.`OligoBarcodeScheme` (`idOligoBarcodeScheme`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;



DROP TABLE IF EXISTS `gnomex`.`Organism`;
CREATE TABLE `gnomex`.`Organism` (
  `idOrganism` INT(10) NOT NULL AUTO_INCREMENT,
  `organism` VARCHAR(50) NULL,
  `abbreviation` VARCHAR(10) NULL,
  `mageOntologyCode` VARCHAR(50) NULL,
  `mageOntologyDefinition` VARCHAR(5000) NULL,
  `isActive` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  `das2Name` varchar(200) NOT NULL,
  `sortOrder` int(10) unsigned  NULL,
  `binomialName` varchar(200) NOT NULL,
  `NCBITaxID` varchar(45)  NULL,  
  PRIMARY KEY (`idOrganism`),
  UNIQUE KEY `Index_OrganismName` (`organism`),
  UNIQUE KEY `Index_OrganismDas2Name` (`das2Name`),
  UNIQUE KEY `Index_OrganismBinomialName` (`binomialName`),  
  CONSTRAINT `FK_Organism_AppUser` FOREIGN KEY `FK_Organism_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Plate`;
CREATE TABLE `gnomex`.`Plate` (
  `idPlate` INT(10) NOT NULL AUTO_INCREMENT,
  `idInstrumentRun` INT(10) NULL,
  `codePlateType` VARCHAR(10) NOT NULL DEFAULT 'REACTION',
  `quadrant` INT(10) NULL,
  `createDate` DATETIME NULL,
  `comments` VARCHAR(200) NULL,
  `label` VARCHAR(50) NULL,
  `codeReactionType` VARCHAR(10) NULL,
  `creator` VARCHAR(50) NULL,
  `codeSealType` VARCHAR(10) NULL,
  PRIMARY KEY (`idPlate`),
  CONSTRAINT `FK_Plate_InstrumentRun` FOREIGN KEY `FK_Plate_InstrumentRun` (`idInstrumentRun`)
    REFERENCES `gnomex`.`InstrumentRun` (`idInstrumentRun`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Plate_PlateType` FOREIGN KEY `FK_Plate_PlateType` (`codePlateType`)
    REFERENCES PlateType (`codePlateType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`PlateWell`;
CREATE TABLE `gnomex`.`PlateWell` (
  `idPlateWell` INT(10) NOT NULL AUTO_INCREMENT,
  `row` varchar(50)  NULL,  
  `col` int(10) NULL,
  `ind` int(10) NULL,
  `idPlate` INT(10) NULL,
  `idSample` INT(10) NULL,
  `idRequest` INT(10) NULL,
  `createDate` DATETIME NULL,
  `codeReactionType` VARCHAR(10) NULL,
  `redoFlag` CHAR(1) NULL, 
  `isControl` CHAR(1) NULL,
  `idAssay` INT(10) NULL,
  `idPrimer` INT(10) NULL,
  PRIMARY KEY (`idPlateWell`),
  CONSTRAINT `FK_PlateWell_Plate` FOREIGN KEY `FK_PlateWell_Plate` (`idPlate`)
    REFERENCES `gnomex`.`Plate` (`idPlate`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PlateWell_Sample` FOREIGN KEY `FK_PlateWell_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PlateWell_Assay` FOREIGN KEY `FK_PlateWell_Assay` (`idAssay`)
    REFERENCES `gnomex`.`Assay` (`idAssay`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PlateWell_Primer` FOREIGN KEY `FK_PlateWell_Primer` (`idPrimer`)
    REFERENCES `gnomex`.`Primer` (`idPrimer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PlateWell_Request` FOREIGN KEY `FK_PlateWell_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


-- New table Primer
DROP TABLE IF EXISTS `gnomex`.`Primer`;
CREATE TABLE gnomex.Primer(
	idPrimer int(10) auto_increment NOT NULL,
	name varchar(50) NOT NULL,
	description varchar(200) NULL,
	sequence varchar(2000) null,
	isActive char(1) NULL,
 PRIMARY KEY (idPrimer) 
 ) ENGINE = INNODB;
 

DROP TABLE IF EXISTS `gnomex`.`Project`;
CREATE TABLE `gnomex`.`Project` (
  `idProject` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL,
  `description` VARCHAR(4000) NULL,
  `publicDateForAppUsers` DATETIME NULL,
  `idLab` INT(10) NULL,
  `idAppUser` INT(10) NULL,
  `codeVisibility` VARCHAR(10) NULL,
  PRIMARY KEY (`idProject`),
  CONSTRAINT `FK_Project_AppUser` FOREIGN KEY `FK_Project_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Project_Lab` FOREIGN KEY `FK_Project_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Project_Visibility` FOREIGN KEY `FK_Project_Visibility` (`codeVisibility`)
    REFERENCES `gnomex`.`Visibility` (`codeVisibility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ProtocolType`;
CREATE TABLE `gnomex`.`ProtocolType` (
  `codeProtocolType` VARCHAR(10) NOT NULL,
  `protocolType` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeProtocolType`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`PropertyDictionary`;
CREATE TABLE `gnomex`.`PropertyDictionary` (
  `idPropertyDictionary` INT(10) NOT NULL AUTO_INCREMENT,
  `propertyName` VARCHAR(200) NULL,
  `propertyValue` VARCHAR(2000) NULL,
  `propertyDescription` VARCHAR(2000) NULL,
  `forServerOnly` CHAR(1) NOT NULL DEFAULT 'N',
  `idCoreFacility` INT(10) NULL,
  PRIMARY KEY (`idPropertyDictionary`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`QualityControlStep`;
CREATE TABLE `gnomex`.`QualityControlStep` (
  `codeQualityControlStep` VARCHAR(10) NOT NULL,
  `qualityControlStep` VARCHAR(50) NULL,
  `mageOntologyCode` VARCHAR(50) NULL,
  `mageOntologyDefinition` VARCHAR(5000) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeQualityControlStep`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`QualityControlStepEntry`;
CREATE TABLE `gnomex`.`QualityControlStepEntry` (
  `idQualityControlStepEntry` INT(10) NOT NULL AUTO_INCREMENT,
  `codeQualityControlStep` VARCHAR(10) NULL,
  `idProject` INT(10) NULL,
  `valueString` VARCHAR(100) NULL,
  `otherLabel` VARCHAR(100) NULL,
  PRIMARY KEY (`idQualityControlStepEntry`),
  CONSTRAINT `FK_QualityControlStepEntry_Project` FOREIGN KEY `FK_QualityControlStepEntry_Project` (`idProject`)
    REFERENCES `gnomex`.`Project` (`idProject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_QualityControlStepEntry_QualityControlStep` FOREIGN KEY `FK_QualityControlStepEntry_QualityControlStep` (`codeQualityControlStep`)
    REFERENCES `gnomex`.`QualityControlStep` (`codeQualityControlStep`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Request`;
CREATE TABLE `gnomex`.`Request` (
  `idRequest` INT(10) NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(50) NULL,
  `createDate` DATETIME NULL,
  `codeProtocolType` VARCHAR(10) NULL,
  `protocolNumber` VARCHAR(100) NULL,
  `idLab` INT(10) NULL,
  `idAppUser` INT(10) NULL,
  `idBillingAccount` INT(10) NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  `codeApplication` VARCHAR(10) NULL,
  `idProject` INT(10) NULL,
  `idSlideProduct` INT(10) NULL,
  `idSampleTypeDefault` INT(10) NULL,
  `idOrganismSampleDefault` INT(10) NULL,
  `idSampleSourceDefault` INT(10) NULL,
  `idSamplePrepMethodDefault` INT(10) NULL,
  `codeBioanalyzerChipType` VARCHAR(10) NULL,
  `notes` VARCHAR(2000) NULL,
  `completedDate` DATETIME NULL,
  `isArrayINFORequest` CHAR(1) NULL,
  `codeVisibility` VARCHAR(10) NOT NULL,
  `lastModifyDate` DATETIME NULL,
  `isExternal` CHAR(1) NULL,
  `idInstitution` INT(10) NULL,
  `idCoreFacility` INT(10) NULL,
  `name` VARCHAR(200) NOT NULL,
  `privacyExpirationDate` DATETIME NULL,
  `description` varchar(5000) null,
  `corePrepInstructions` varchar(5000) null,
  `analysisInstructions` varchar(5000) null,
  `captureLibDesignId` varchar(200) null,
  `avgInsertSizeFrom` INT(10) null,
  `avgInsertSizeTo` INT(10) null,
  `idSampleDropOffLocation` INT(10) NULL,
  `codeRequestStatus` VARCHAR(10) NULL,
  PRIMARY KEY (`idRequest`),
  CONSTRAINT `FK_Request_Project` FOREIGN KEY `FK_Request_Project` (`idProject`)
    REFERENCES `gnomex`.`Project` (`idProject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_SlideProduct` FOREIGN KEY `FK_Request_SlideProduct` (`idSlideProduct`)
    REFERENCES `gnomex`.`SlideProduct` (`idSlideProduct`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_Application` FOREIGN KEY `FK_Request_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_Organism` FOREIGN KEY `FK_Request_Organism` (`idOrganismSampleDefault`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_SampleSource` FOREIGN KEY `FK_Request_SampleSource` (`idSampleSourceDefault`)
    REFERENCES `gnomex`.`SampleSource` (`idSampleSource`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_BillingAccount` FOREIGN KEY `FK_Request_BillingAccount` (`idBillingAccount`)
    REFERENCES `gnomex`.`BillingAccount` (`idBillingAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_RequestCategory` FOREIGN KEY `FK_Request_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_Visibility` FOREIGN KEY `FK_Request_Visibility` (`codeVisibility`)
    REFERENCES `gnomex`.`Visibility` (`codeVisibility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_Institution` FOREIGN KEY FK_Request_Institution (`idInstitution`)
    REFERENCES `gnomex`.`Institution` (`idInstitution`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_CoreFacility` FOREIGN KEY FK_Request_CoreFacility (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_SampleDropOffLocation` FOREIGN KEY `FK_Request_SampleDropOffLocation` (`idSampleDropOffLocation`)
    REFERENCES SampleDropOffLocation (`idSampleDropOffLocation`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Request_RequestStatus` FOREIGN KEY `FK_Request_RequestStatus` (`codeRequestStatus`)
    REFERENCES `gnomex`.`RequestStatus` (`codeRequestStatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`RequestCategory`;
CREATE TABLE `gnomex`.`RequestCategory` (
  `codeRequestCategory` VARCHAR(10) NOT NULL,
  `requestCategory` VARCHAR(50) NULL,
  `idVendor` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  `numberOfChannels` INT(10) NULL,
  `notes` VARCHAR(500) NULL,
  `icon` VARCHAR(200) NULL,
  `type` VARCHAR(10) NULL,
  `sortOrder` INT(10) NULL,
  `idOrganism` INT(10) NULL,
  `idCoreFacility` INT(10) NULL,
  `isSampleBarcodingOptional` CHAR(1) NULL,
  `isInternal` CHAR(1) NULL,
  `isExternal` CHAR(1) NULL,
  PRIMARY KEY (`codeRequestCategory`),
  CONSTRAINT `FK_RequestCategory_Vendor` FOREIGN KEY `FK_RequestCategory_Vendor` (`idVendor`)
    REFERENCES `gnomex`.`Vendor` (`idVendor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestCategory_Organism` FOREIGN KEY `FK_RequestCategory_Organism` (`idOrganism`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestCategory_CoreFacility` FOREIGN KEY `FK_RequestCategory_CoreFacility` (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`RequestCategoryApplication`;
CREATE TABLE `gnomex`.`RequestCategoryApplication` (
  `codeRequestCategory` VARCHAR(10) NOT NULL,
  `codeApplication` VARCHAR(10) NOT NULL,
  `idLabelingProtocolDefault` INT(10) NULL,
  `idHybProtocolDefault` INT(10) NULL,
  `idScanProtocolDefault` INT(10) NULL,
  `idFeatureExtractionProtocolDefault` INT(10) NULL,
  PRIMARY KEY (`codeRequestCategory`, `codeApplication`),
  CONSTRAINT `FK_RequestCategoryApplication_RequestCategory` FOREIGN KEY `FK_RequestCategoryApplication_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestCategoryApplication_Application` FOREIGN KEY `FK_RequestCategoryApplication_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestCategoryApplication_HybProtocol` FOREIGN KEY `FK_RequestCategoryApplication_HybProtocol` (`idHybProtocolDefault`)
    REFERENCES `gnomex`.`HybProtocol` (`idHybProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestCategoryApplication_LabelingProtocol` FOREIGN KEY `FK_RequestCategoryApplication_LabelingProtocol` (`idLabelingProtocolDefault`)
    REFERENCES `gnomex`.`LabelingProtocol` (`idLabelingProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestCategoryApplication_ScanProtocol` FOREIGN KEY `FK_RequestCategoryApplication_ScanProtocol` (`idScanProtocolDefault`)
    REFERENCES `gnomex`.`ScanProtocol` (`idScanProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestCategoryApplication_FeatureExtractionProtocol` FOREIGN KEY `FK_RequestCategoryApplication_FeatureExtractionProtocol` (`idFeatureExtractionProtocolDefault`)
    REFERENCES `gnomex`.`FeatureExtractionProtocol` (`idFeatureExtractionProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`RequestCollaborator`;
CREATE TABLE `gnomex`.`RequestCollaborator` (
  `idRequest` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  `canUploadData` char(1) null,
  PRIMARY KEY (`idRequest`, `idAppUser`),
  CONSTRAINT `FK_RequestCollaborator_AppUser` FOREIGN KEY `FK_RequestCollaborator_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestCollaborator_Request` FOREIGN KEY `FK_RequestCollaborator_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`RequestHybridization`;
CREATE TABLE `gnomex`.`RequestHybridization` (
  `idRequest` INT(10) NOT NULL,
  `idHybridization` INT(10) NOT NULL,
  PRIMARY KEY (`idRequest`, `idHybridization`),
  CONSTRAINT `FK_RequestHybridization_Hybridization` FOREIGN KEY `FK_RequestHybridization_Hybridization` (`idHybridization`)
    REFERENCES `gnomex`.`Hybridization` (`idHybridization`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestHybridization_Request` FOREIGN KEY `FK_RequestHybridization_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`RequestSeqLibTreatment`;
CREATE TABLE `gnomex`.`RequestSeqLibTreatment` (
  `idRequest` INT(10) NOT NULL,
  `idSeqLibTreatment` INT(10) NOT NULL,
  PRIMARY KEY (`idRequest`, `idSeqLibTreatment`),
  CONSTRAINT `FK_RequestSeqLibTreatment_SeqLibTreatment` FOREIGN KEY `FK_RequestSeqLibTreatment_SeqLibTreatment` (`idSeqLibTreatment`)
    REFERENCES `gnomex`.`SeqLibTreatment` (`idSeqLibTreatment`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_RequestSeqLibTreatment_Request` FOREIGN KEY `FK_RequestSeqLibTreatment_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Sample`;
CREATE TABLE `gnomex`.`Sample` (
  `idSample` INT(10) NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(100) NULL,
  `name` VARCHAR(200) NULL,
  `description` VARCHAR(2000) NULL,
  `concentration` DECIMAL(8, 3) NULL,
  `codeConcentrationUnit` VARCHAR(10) NULL,
  `idSampleType` INT(10) NULL,
  `idOrganism` INT(10) NULL,
  `otherOrganism` VARCHAR(100) NULL,
  `idSampleSource` INT(10) NULL,
  `idSamplePrepMethod` INT(10) NULL,
  `otherSamplePrepMethod` VARCHAR(300) NULL,
  `idSeqLibProtocol` INT(10) NULL,
  `codeBioanalyzerChipType` VARCHAR(10) NULL,
  `idOligoBarcode` INT(10) NULL,
  `qualDate` DATETIME NULL,
  `qualFailed` CHAR(1) NULL,
  `qualBypassed` CHAR(1) NULL,
  `qual260nmTo280nmRatio` DECIMAL(3, 2) NULL,
  `qual260nmTo230nmRatio` DECIMAL(3, 2) NULL,
  `qualCalcConcentration` DECIMAL(8, 2) NULL,
  `qual28sTo18sRibosomalRatio` DECIMAL(2, 1) NULL,
  `qualRINNumber` VARCHAR(10) NULL,
  `idRequest` INT(10) NULL,
  `fragmentSizeFrom` INT(10) NULL,
  `fragmentSizeTo` INT(10) NULL,
  `seqPrepByCore` CHAR(1) NULL,
  `seqPrepDate` DATETIME NULL,
  `seqPrepFailed` CHAR(1) NULL,
  `seqPrepBypassed` CHAR(1) NULL,
  `qualFragmentSizeFrom` INT(10) NULL,
  `qualFragmentSizeTo` INT(10) NULL,
  `seqPrepLibConcentration` DECIMAL(8, 1) NULL,
  `seqPrepQualCodeBioanalyzerChipType` VARCHAR(10) NULL,
  `seqPrepGelFragmentSizeFrom` INT(10) NULL,
  `seqPrepGelFragmentSizeTo` INT(10) NULL,
  `seqPrepStockLibVol` DECIMAL(8, 1) NULL,
  `seqPrepStockEBVol` DECIMAL(8, 1) NULL,
  `seqPrepStockDate` DATETIME NULL,
  `seqPrepStockFailed` CHAR(1) NULL,
  `seqPrepStockBypassed` CHAR(1) NULL,
  `prepInstructions` VARCHAR(2000) NULL,
  `ccNumber` VARCHAR(20) NULL,
  `multiplexGroupNumber` INT(10) NULL,
  `barcodeSequence` VARCHAR(20) NULL,
  `isControl` CHAR(1) NULL, 
  PRIMARY KEY (`idSample`),
  CONSTRAINT `FK_Sample_Organism` FOREIGN KEY `FK_Sample_Organism` (`idOrganism`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Sample_SampleSource` FOREIGN KEY `FK_Sample_SampleSource` (`idSampleSource`)
    REFERENCES `gnomex`.`SampleSource` (`idSampleSource`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Sample_SamplePrepMethod` FOREIGN KEY `FK_Sample_SamplePrepMethod` (`idSamplePrepMethod`)
    REFERENCES `gnomex`.`SamplePrepMethod` (`idSamplePrepMethod`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Sample_SeqLibProtocol` FOREIGN KEY `FK_Sample_SeqLibProtocol` (`idSeqLibProtocol`)
    REFERENCES `gnomex`.`SeqLibProtocol` (`idSeqLibProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Sample_BioanalyzerChipType` FOREIGN KEY `FK_Sample_BioanalyzerChipType` (`seqPrepQualCodeBioanalyzerChipType`)
    REFERENCES `gnomex`.`BioanalyzerChipType` (`codeBioanalyzerChipType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
 CONSTRAINT `FK_Sample_OligoBarcode` FOREIGN KEY `FK_Sample_OligoBarcode` (`idOligoBarcode`)
    REFERENCES `gnomex`.`OligoBarcode` (`idOligoBarcode`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Sample_ConcentrationUnit` FOREIGN KEY `FK_Sample_ConcentrationUnit` (`codeConcentrationUnit`)
    REFERENCES `gnomex`.`ConcentrationUnit` (`codeConcentrationUnit`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Sample_Request` FOREIGN KEY `FK_Sample_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


-- Add table PropertyType
DROP TABLE IF EXISTS `gnomex`.`PropertyType`;
CREATE TABLE gnomex.PropertyType ( 
    codePropertyType	    VARCHAR(10) NOT NULL,
    name 	                varchar(200) NOT NULL,
    isActive     	        char(1) NULL,
    PRIMARY KEY (codePropertyType)
) ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`Property`;
CREATE TABLE `gnomex`.`Property` (
  `idProperty` int(10) auto_increment NOT NULL,
  `name` VARCHAR(50) NULL,
  `description` VARCHAR(2000) NULL,
  `mageOntologyCode` VARCHAR(50) NULL,
  `mageOntologyDefinition` VARCHAR(5000) NULL,
  `isActive` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  `isRequired` CHAR(1) NULL,
  `forSample` CHAR(1) NULL default 'Y',
  `forAnalysis` CHAR(1) NULL default 'N',
  `forDataTrack` CHAR(1) NULL default 'N',
  codePropertyType VARCHAR(10) not null,
  PRIMARY KEY (`idProperty`),
  CONSTRAINT `FK_Property_AppUser` FOREIGN KEY `FK_Property_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT FK_Property_PropertyType FOREIGN KEY FK_Property_PropertyType (codePropertyType)
    REFERENCES gnomex.PropertyType (codePropertyType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION        
)
ENGINE = INNODB;


-- Add table PropertyOrganism
DROP TABLE IF EXISTS `gnomex`.`PropertyOrganism`;
CREATE TABLE gnomex.PropertyOrganism ( 
     idProperty	int(10),
     idOrganism           INT(10),
     PRIMARY KEY (idProperty, idOrganism),
    CONSTRAINT FK_PropertyOrganism_Property FOREIGN KEY FK_PropertyOrganism_Property (idProperty)
    REFERENCES gnomex.Property (idProperty)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_PropertyOrganism_Organism FOREIGN KEY FK_PropertyOrganism_Organism (idOrganism)
    REFERENCES gnomex.Organism (idOrganism)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`PropertyPlatform`;
CREATE TABLE gnomex.PropertyPlatform ( 
     idProperty	INT(10),
     codeRequestCategory    VARCHAR(10),
     PRIMARY KEY (idProperty, codeRequestCategory),
    CONSTRAINT FK_PropertyPlatform_Property FOREIGN KEY FK_PropertyPlatform_Property (idProperty)
    REFERENCES gnomex.Property (idProperty)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_PropertyPlatform_RequestCategory FOREIGN KEY FK_PropertyPlatform_RequestCategory (codeRequestCategory)
    REFERENCES gnomex.RequestCategory (codeRequestCategory)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;


-- Add table PropertyOption
DROP TABLE IF EXISTS `gnomex`.`PropertyOption`;
CREATE  TABLE gnomex.PropertyOption (
  idPropertyOption INT(10)  NOT NULL AUTO_INCREMENT ,
  value VARCHAR(200)  NULL,
  idProperty int(10) not  NULL,
  sortOrder INT(10) NULL,
  isActive     	        char(1) NULL,  
  PRIMARY KEY (idPropertyOption),
  KEY `FK_PropertyOption_Property` (`idProperty`),
  CONSTRAINT `FK_PropertyOption_Property` FOREIGN KEY (`idProperty`) REFERENCES `Property` (`idProperty`)
) ENGINE=InnoDB;



DROP TABLE IF EXISTS `gnomex`.`PropertyEntry`;
CREATE TABLE `gnomex`.`PropertyEntry` (
  `idPropertyEntry` INT(10) NOT NULL AUTO_INCREMENT,
  `idProperty` int(10) NULL,
  `idSample` INT(10) NULL,
  `valueString` VARCHAR(200) NULL,
  `otherLabel` VARCHAR(100) NULL,
  idDataTrack INT(10) NULL,
  idAnalysis INT(10) NULL,
  PRIMARY KEY (`idPropertyEntry`),
  CONSTRAINT `FK_PropertyEntry_Sample` FOREIGN KEY `FK_PropertyEntry_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PropertyEntry_DataTrack` FOREIGN KEY `FK_PropertyEntry_DataTrack` (`idDataTrack`)
    REFERENCES `gnomex`.`DataTrack` (`idDataTrack`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PropertyEntry_Analysis` FOREIGN KEY `FK_PropertyEntry_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PropertyEntry_Property` FOREIGN KEY `FK_PropertyEntry_Property` (`idProperty`)
    REFERENCES `gnomex`.`Property` (`idProperty`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


-- Add new Table PropertyEntryOption
DROP TABLE IF EXISTS PropertyEntryOption;
CREATE TABLE gnomex.PropertyEntryOption (
  idPropertyEntry INT(10)  NOT NULL,
  idPropertyOption INT(10)  NOT NULL,
  PRIMARY KEY (idPropertyEntry, idPropertyOption) ,
  CONSTRAINT FK_PropertyEntryOption_PropertyEntry FOREIGN KEY FK_PropertyEntryOption_PropertyEntry (idPropertyEntry)
    REFERENCES gnomex.PropertyEntry (idPropertyEntry)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION, 
   CONSTRAINT FK_PropertyEntryOption_PropertyOption FOREIGN KEY FK_PropertyEntryOption_PropertyOption (idPropertyOption)
    REFERENCES gnomex.PropertyOption (idPropertyOption)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)ENGINE=InnoDB;

-- Add new Table PropertyEntryValue
DROP TABLE IF EXISTS PropertyEntryValue;
CREATE TABLE gnomex.PropertyEntryValue (
  idPropertyEntryValue INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  value VARCHAR(200) NULL,
  idPropertyEntry INT(10)   NULL,
  PRIMARY KEY (idPropertyEntryValue),
  CONSTRAINT FK_PropertyEntryValue_PropertyEntry FOREIGN KEY FK_PropertyEntryValue_PropertyEntry (idPropertyEntry)
    REFERENCES gnomex.PropertyEntry (idPropertyEntry)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)ENGINE=InnoDB;

DROP TABLE IF EXISTS `gnomex`.`ReactionType`;
CREATE TABLE `gnomex`.`ReactionType` (
  `codeReactionType` VARCHAR(10) NOT NULL,
  `reactionType` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeReactionType`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SealType`;
CREATE TABLE `gnomex`.`SealType` (
  `codeSealType` VARCHAR(10) NOT NULL,
  `sealType` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeSealType`)
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`RequestStatus`;
CREATE TABLE `gnomex`.`RequestStatus` (
  `codeRequestStatus` VARCHAR(10) NOT NULL,
  `requestStatus` VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeRequestStatus`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SamplePrepMethod`;
CREATE TABLE `gnomex`.`SamplePrepMethod` (
  `idSamplePrepMethod` INT(10) NOT NULL AUTO_INCREMENT,
  `samplePrepMethod` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSamplePrepMethod`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SampleSource`;
CREATE TABLE `gnomex`.`SampleSource` (
  `idSampleSource` INT(10) NOT NULL AUTO_INCREMENT,
  `sampleSource` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSampleSource`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SampleType`;
CREATE TABLE `gnomex`.`SampleType` (
  `idSampleType` INT(10) NOT NULL AUTO_INCREMENT,
  `sampleType` VARCHAR(50) NULL,
  `sortOrder` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSampleType`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SampleTypeApplication`;
CREATE TABLE `gnomex`.`SampleTypeApplication` (
  `idSampleTypeApplication` INT(10) NOT NULL AUTO_INCREMENT,
  `idSampleType` INT(10) NULL,
  `codeApplication` VARCHAR(10) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSampleTypeApplication`),
  UNIQUE INDEX `IX_ApplicationSampleType` (`idSampleType`, `codeApplication`),
  CONSTRAINT `FK_ApplicationSampleType_SampleType` FOREIGN KEY `FK_ApplicationSampleType_SampleType` (`idSampleType`)
    REFERENCES `gnomex`.`SampleType` (`idSampleType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ApplicationSampleType_Application` FOREIGN KEY `FK_ApplicationSampleType_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SampleTypeRequestCategory`;
CREATE TABLE `gnomex`.`SampleTypeRequestCategory` (
  `idSampleTypeRequestCategory` INT(10) NOT NULL AUTO_INCREMENT,
  `idSampleType` INT(10) NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  PRIMARY KEY (`idSampleTypeRequestCategory`),
  CONSTRAINT `FK_SampleTypeRequestCategory_SampleType` FOREIGN KEY `FK_SampleTypeRequestCategory_SampleType` (`idSampleType`)
    REFERENCES `gnomex`.`SampleType` (`idSampleType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleTypeRequestCategory_RequestCategory` FOREIGN KEY `FK_SampleTypeRequestCategory_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`ScanProtocol`;
CREATE TABLE `gnomex`.`ScanProtocol` (
  `idScanProtocol` INT(10) NOT NULL AUTO_INCREMENT,
  `scanProtocol` VARCHAR(200) NULL,
  `description` LONGTEXT NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  `url` VARCHAR(500) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idScanProtocol`),
  CONSTRAINT `FK_ScanProtocol_RequestCategory` FOREIGN KEY `FK_ScanProtocol_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SeqLibProtocol`;
CREATE TABLE `gnomex`.`SeqLibProtocol` (
  `idSeqLibProtocol` INT(10) NOT NULL AUTO_INCREMENT,
  `seqLibProtocol` VARCHAR(200) NULL,
  `description` LONGTEXT NULL,
  `url` VARCHAR(500) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSeqLibProtocol`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SeqLibTreatment`;
CREATE TABLE `gnomex`.`SeqLibTreatment` (
  `idSeqLibTreatment` INT(10) NOT NULL AUTO_INCREMENT,
  `seqLibTreatment` VARCHAR(200) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSeqLibTreatment`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SeqLibProtocolApplication`;
CREATE TABLE `gnomex`.`SeqLibProtocolApplication` (
  `idSeqLibProtocol` INT(10) NOT NULL,
  `codeApplication` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idSeqLibProtocol`, `codeApplication`)
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`SeqRunType`;
CREATE TABLE `gnomex`.`SeqRunType` (
  `idSeqRunType` INT(10) NOT NULL AUTO_INCREMENT,
  `seqRunType` VARCHAR(200) NULL,
  `isActive` CHAR(1) NULL,
  `sortOrder` INT(10) NULL,
  PRIMARY KEY (`idSeqRunType`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SequenceLane`;
CREATE TABLE `gnomex`.`SequenceLane` (
  `idSequenceLane` INT(10) NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(100) NULL,
  `createDate` DATETIME NULL,
  `idRequest` INT(10) NOT NULL,
  `idSample` INT(10) NULL,
  `idSeqRunType` INT(10) NULL,
  `idNumberSequencingCycles` INT(10) NULL,
  `analysisInstructions` VARCHAR(2000) NULL,
  `idGenomeBuildAlignTo` INT(10) NULL,
  `idFlowCellChannel` INT(10) NULL,
  `readCount` INT(10) NULL,
  `pipelineVersion` VARCHAR(10) NULL,
  PRIMARY KEY (`idSequenceLane`),
  CONSTRAINT `FK_SequenceLane_GenomeBuild` FOREIGN KEY `FK_SequenceLane_GenomeBuild` (`idGenomeBuildAlignTo`)
    REFERENCES `gnomex`.`GenomeBuild` (`idGenomeBuild`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SequenceLane_NumberSequencingCycles` FOREIGN KEY `FK_SequenceLane_NumberSequencingCycles` (`idNumberSequencingCycles`)
    REFERENCES `gnomex`.`NumberSequencingCycles` (`idNumberSequencingCycles`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SequenceLane_Sample` FOREIGN KEY `FK_SequenceLane_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SequenceLane_FlowCellChannel` FOREIGN KEY `FK_SequenceLane_FlowCellChannel` (`idFlowCellChannel`)
    REFERENCES `gnomex`.`FlowCellChannel` (`idFlowCellChannel`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SequenceLane_Request` FOREIGN KEY `FK_SequenceLane_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SequenceLane_SeqRunType` FOREIGN KEY `FK_SequenceLane_SeqRunType` (`idSeqRunType`)
    REFERENCES `gnomex`.`SeqRunType` (`idSeqRunType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SequencingControl`;
CREATE TABLE `gnomex`.`SequencingControl` (
  `idSequencingControl` INT(10) NOT NULL AUTO_INCREMENT,
  `sequencingControl` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  PRIMARY KEY (`idSequencingControl`),
  CONSTRAINT `FK_SequencingControl_AppUser` FOREIGN KEY `FK_SequencingControl_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`SequencingPlatform`;
CREATE TABLE `gnomex`.`SequencingPlatform` (
  `codeSequencingPlatform` VARCHAR(10) NOT NULL,
  `sequencingPlatform` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeSequencingPlatform`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Slide`;
CREATE TABLE `gnomex`.`Slide` (
  `idSlide` INT(10) NOT NULL AUTO_INCREMENT,
  `barcode` VARCHAR(100) NULL,
  `idSlideDesign` INT(10) NULL,
  `slideName` VARCHAR(200) NULL,
  PRIMARY KEY (`idSlide`),
  CONSTRAINT `FK_Slide_SlideDesign` FOREIGN KEY `FK_Slide_SlideDesign` (`idSlideDesign`)
    REFERENCES `gnomex`.`SlideDesign` (`idSlideDesign`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SlideDesign`;
CREATE TABLE `gnomex`.`SlideDesign` (
  `idSlideDesign` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NULL,
  `slideDesignProtocolName` VARCHAR(100) NULL,
  `idSlideProduct` INT(10) NULL,
  `accessionNumberArrayExpress` VARCHAR(200) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSlideDesign`),
  CONSTRAINT `FK_SlideDesign_SlideProduct` FOREIGN KEY `FK_SlideDesign_SlideProduct` (`idSlideProduct`)
    REFERENCES `gnomex`.`SlideProduct` (`idSlideProduct`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SlideProduct`;
CREATE TABLE `gnomex`.`SlideProduct` (
  `idSlideProduct` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NULL,
  `catalogNumber` VARCHAR(100) NULL,
  `isCustom` CHAR(1) NULL,
  `idLab` INT(10) NULL,
  `codeApplication` VARCHAR(10) NULL,
  `idVendor` INT(10) NULL,
  `idOrganism` INT(10) NULL,
  `arraysPerSlide` INT(10) NULL,
  `slidesInSet` INT(10) NULL,
  `isSlideSet` CHAR(1) NULL,
  `isActive` CHAR(1) NULL,
  `idBillingSlideProductClass` INT(10) NULL,
  `idBillingSlideServiceClass` INT(10) NULL,
  PRIMARY KEY (`idSlideProduct`),
  CONSTRAINT `FK_SlideProduct_Organism` FOREIGN KEY `FK_SlideProduct_Organism` (`idOrganism`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SlideProduct_Application` FOREIGN KEY `FK_SlideProduct_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SlideProduct_Vendor` FOREIGN KEY `FK_SlideProduct_Vendor` (`idVendor`)
    REFERENCES `gnomex`.`Vendor` (`idVendor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SlideProduct_BillingSlideProductClass` FOREIGN KEY `FK_SlideProduct_BillingSlideProductClass` (`idBillingSlideProductClass`)
    REFERENCES `gnomex`.`BillingSlideProductClass` (`idBillingSlideProductClass`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SlideProduct_BillingSlideServiceClass` FOREIGN KEY `FK_SlideProduct_BillingSlideServiceClass` (`idBillingSlideServiceClass`)
    REFERENCES `gnomex`.`BillingSlideServiceClass` (`idBillingSlideServiceClass`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SlideProduct_Lab` FOREIGN KEY `FK_SlideProduct_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SlideProductApplication`;
CREATE TABLE `gnomex`.`SlideProductApplication` (
  `idSlideProduct` INT(10) NOT NULL,
  `codeApplication` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idSlideProduct`, `codeApplication`),
  CONSTRAINT `FK_SlideProductApplication_SlideProduct` FOREIGN KEY `FK_SlideProductApplication_SlideProduct` (`idSlideProduct`)
    REFERENCES `gnomex`.`SlideProduct` (`idSlideProduct`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SlideProductApplication_Application` FOREIGN KEY `FK_SlideProductApplication_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SlideSource`;
CREATE TABLE `gnomex`.`SlideSource` (
  `codeSlideSource` VARCHAR(10) NOT NULL,
  `slideSource` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  `sortOrder` INT(10) NULL,
  PRIMARY KEY (`codeSlideSource`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`State`;
CREATE TABLE `gnomex`.`State` (
  `codeState` VARCHAR(10) NOT NULL,
  `state` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeState`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Step`;
CREATE TABLE `gnomex`.`Step` (
  `codeStep` VARCHAR(10) NOT NULL,
  `step` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeStep`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SampleTypeApplication`;
CREATE TABLE `gnomex`.`SampleTypeApplication` (
  `idSampleTypeApplication` INT(10) NOT NULL AUTO_INCREMENT,
  `idSampleType` INT(10) NULL,
  `codeApplication` VARCHAR(10) NULL,
  `idLabelingProtocolDefault` INT(10) NULL,
  `idHybProtocolDefault` INT(10) NULL,
  `idScanProtocolDefault` INT(10) NULL,
  `idFeatureExtractionProtocolDefault` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSampleTypeApplication`),
  UNIQUE INDEX `IX_ApplicationSampleType` (`idSampleType`, `codeApplication`),
  CONSTRAINT `FK_ApplicationSampleType_SampleType` FOREIGN KEY `FK_ApplicationSampleType_SampleType` (`idSampleType`)
    REFERENCES `gnomex`.`SampleType` (`idSampleType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ApplicationSampleType_Application` FOREIGN KEY `FK_ApplicationSampleType_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleTypeApplication_HybProtocol` FOREIGN KEY `FK_SampleTypeApplication_HybProtocol` (`idHybProtocolDefault`)
    REFERENCES `gnomex`.`HybProtocol` (`idHybProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleTypeApplication_LabelingProtocol` FOREIGN KEY `FK_SampleTypeApplication_LabelingProtocol` (`idLabelingProtocolDefault`)
    REFERENCES `gnomex`.`LabelingProtocol` (`idLabelingProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleTypeApplication_ScanProtocol` FOREIGN KEY `FK_SampleTypeApplication_ScanProtocol` (`idScanProtocolDefault`)
    REFERENCES `gnomex`.`ScanProtocol` (`idScanProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleTypeApplication_FeatureExtractionProtocol` FOREIGN KEY `FK_SampleTypeApplication_FeatureExtractionProtocol` (`idFeatureExtractionProtocolDefault`)
    REFERENCES `gnomex`.`FeatureExtractionProtocol` (`idFeatureExtractionProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SubmissionInstruction`;
CREATE TABLE `gnomex`.`SubmissionInstruction` (
  `idSubmissionInstruction` INT(10) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(200) NULL,
  `url` VARCHAR(2000) NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  `codeApplication` VARCHAR(10) NULL,
  `codeBioanalyzerChipType` VARCHAR(10) NULL,
  `idBillingSlideServiceClass` INT(10) NULL,
  PRIMARY KEY (`idSubmissionInstruction`),
  CONSTRAINT `FK_SubmissionInstruction_BillingSlideServiceClass` FOREIGN KEY `FK_SubmissionInstruction_BillingSlideServiceClass` (`idBillingSlideServiceClass`)
    REFERENCES `gnomex`.`BillingSlideServiceClass` (`idBillingSlideServiceClass`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SubmissionInstruction_RequestCategory` FOREIGN KEY `FK_SubmissionInstruction_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SubmissionInstruction_Application` FOREIGN KEY `FK_SubmissionInstruction_Application` (`codeApplication`)
    REFERENCES `gnomex`.`Application` (`codeApplication`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SubmissionInstruction_BioanalyzerChipType` FOREIGN KEY `FK_SubmissionInstruction_BioanalyzerChipType` (`codeBioanalyzerChipType`)
    REFERENCES `gnomex`.`BioanalyzerChipType` (`codeBioanalyzerChipType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`TreatmentEntry`;
CREATE TABLE `gnomex`.`TreatmentEntry` (
  `idTreatmentEntry` INT(10) NOT NULL AUTO_INCREMENT,
  `treatment` VARCHAR(2000) NULL,
  `idSample` INT(10) NULL,
  `otherLabel` VARCHAR(100) NULL,
  PRIMARY KEY (`idTreatmentEntry`),
  CONSTRAINT `FK_TreatmentEntry_Sample` FOREIGN KEY `FK_TreatmentEntry_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`UserPermissionKind`;
CREATE TABLE `gnomex`.`UserPermissionKind` (
  `codeUserPermissionKind` VARCHAR(10) NOT NULL,
  `userPermissionKind` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeUserPermissionKind`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Vendor`;
CREATE TABLE `gnomex`.`Vendor` (
  `idVendor` INT(10) NOT NULL AUTO_INCREMENT,
  `vendorName` VARCHAR(100) NULL,
  `description` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idVendor`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Visibility`;
CREATE TABLE `gnomex`.`Visibility` (
  `codeVisibility` VARCHAR(10) NOT NULL,
  `visibility` VARCHAR(100) NULL,
  PRIMARY KEY (`codeVisibility`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`WorkItem`;
CREATE TABLE `gnomex`.`WorkItem` (
  `idWorkItem` INT(10) NOT NULL AUTO_INCREMENT,
  `codeStepNext` VARCHAR(10) NULL,
  `idSample` INT(10) NULL,
  `idLabeledSample` INT(10) NULL,
  `idHybridization` INT(10) NULL,
  `idRequest` INT(10) NULL,
  `createDate` DATETIME NULL,
  `idSequenceLane` INT(10) NULL,
  `idFlowCellChannel` INT(10) NULL,
  `idCoreFacility` INT(10) NULL,
  `status` VARCHAR(50) NULL,
  PRIMARY KEY (`idWorkItem`),
  CONSTRAINT `FK_WorkItem_Sample` FOREIGN KEY `FK_WorkItem_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_WorkItem_FlowCellChannel` FOREIGN KEY `FK_WorkItem_FlowCellChannel` (`idFlowCellChannel`)
    REFERENCES `gnomex`.`FlowCellChannel` (`idFlowCellChannel`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_WorkItem_Step` FOREIGN KEY `FK_WorkItem_Step` (`codeStepNext`)
    REFERENCES `gnomex`.`Step` (`codeStep`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_WorkItem_LabeledSample` FOREIGN KEY `FK_WorkItem_LabeledSample` (`idLabeledSample`)
    REFERENCES `gnomex`.`LabeledSample` (`idLabeledSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_WorkItem_Hybridization` FOREIGN KEY `FK_WorkItem_Hybridization` (`idHybridization`)
    REFERENCES `gnomex`.`Hybridization` (`idHybridization`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_WorkItem_Request` FOREIGN KEY `FK_WorkItem_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
   CONSTRAINT `FK_WorkItem_CoreFacility` FOREIGN KEY `FK_WorkItem_CoreFacility` (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


DROP TABLE IF EXISTS `gnomex`.`TransferLog`;
CREATE TABLE `gnomex`.`TransferLog` (
  `idTransferLog` INT(10) NOT NULL AUTO_INCREMENT,
  `transferType` VARCHAR(10) NOT NULL,
  `transferMethod` VARCHAR(10) NOT NULL,
  `startDateTime` DATETIME NULL,
  `endDateTime` DATETIME NULL,
  `fileName` VARCHAR(1000) NOT NULL,
  `fileSize` DECIMAL(14,0) NULL,
  `performCompression` CHAR(1) NULL DEFAULT 'N',
  `idAnalysis` INT(10)  NULL ,
  `idRequest` INT(10)  NULL ,
  `idLab` INT(10)  NULL ,
  PRIMARY KEY (`idTransferLog`),
  CONSTRAINT `FK_TransferLog_Request` FOREIGN KEY `FK_TransferLog_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_TransferLog_Analysis` FOREIGN KEY `FK_TransferLog_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_TransferLog_Lab` FOREIGN KEY `FK_TransferLog_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;



--
--
--  GenoPub tables
--
--

--
-- Table structure for table `GenomeBuildAlias`
--
DROP TABLE IF EXISTS `GenomeBuildAlias`;
CREATE TABLE `GenomeBuildAlias` (
  `idGenomeBuildAlias` int(10) NOT NULL auto_increment,
  `alias` varchar(100) NOT NULL,
  `idGenomeBuild` int(10)  NOT NULL,
  PRIMARY KEY  (`idGenomeBuildAlias`),
  KEY `FK_GenomeBuildAlias_GenomeBuild` (`idGenomeBuild`),
  CONSTRAINT `FK_GenomeBuildAlias_GenomeBuild` FOREIGN KEY (`idGenomeBuild`) REFERENCES `GenomeBuild` (`idGenomeBuild`)
) ENGINE=InnoDB;


--
-- Table structure for table dbo.Segment 
--
--
-- Table structure for table `Segment`
--

DROP TABLE IF EXISTS `Segment`;
CREATE TABLE `Segment` (
  `idSegment` int(10)  NOT NULL auto_increment,
  `length` int(10) unsigned NOT NULL,
  `name` varchar(100) NOT NULL,
  `idGenomeBuild` int(10)  NULL,
  `sortOrder` int(10) unsigned NULL,
  PRIMARY KEY  (`idSegment`),
  KEY `FK_Segment_GenomeBuild` (`idGenomeBuild`),
  CONSTRAINT `FK_Segment_GenomeBuild` FOREIGN KEY (`idGenomeBuild`) REFERENCES `GenomeBuild` (`idGenomeBuild`)
) ENGINE=InnoDB;


--
-- Table structure for table `DataTrack`
--

DROP TABLE IF EXISTS `DataTrack`;
CREATE TABLE `DataTrack` (
  `idDataTrack` int(10)  NOT NULL auto_increment,
  `name` varchar(2000) NOT NULL,
  `description` varchar(10000) default NULL,
  `fileName` varchar(2000) default NULL,
  `idGenomeBuild` int(10)  NOT NULL,
  `codeVisibility` varchar(10) NOT NULL,
  `idAppUser` int(10)  default NULL,
  `idLab` int(10)  default NULL,
  `summary` varchar(5000) default NULL,
  `createdBy` varchar(200) default NULL,
  `createDate` datetime default NULL,
  `isLoaded` char(1) default 'N',
  `idInstitution` int(10)  default NULL,
  `dataPath` varchar(500) default NULL,
  PRIMARY KEY  (`idDataTrack`),
  KEY `FK_DataTrack_GenomeBuild` (`idGenomeBuild`),
  KEY `FK_DataTrack_AppUser` (`idAppUser`),
  KEY `FK_DataTrack_Visibility` (`codeVisibility`),
  KEY `FK_DataTrack_group` USING BTREE (`idLab`),
  KEY `FK_DataTrack_Institution` (`idInstitution`),
  CONSTRAINT `FK_DataTrack_GenomeBuild` FOREIGN KEY (`idGenomeBuild`) REFERENCES `GenomeBuild` (`idGenomeBuild`),
  CONSTRAINT `FK_DataTrack_Institution` FOREIGN KEY (`idInstitution`) REFERENCES `Institution` (`idInstitution`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_DataTrack_AppUser` FOREIGN KEY (`idAppUser`) REFERENCES `AppUser` (`idAppUser`),
  CONSTRAINT `FK_DataTrack_Lab` FOREIGN KEY (`idLab`) REFERENCES `Lab` (`idLab`),
  CONSTRAINT `FK_DataTrack_Visibility` FOREIGN KEY (`codeVisibility`) REFERENCES `Visibility` (`codeVisibility`)
) ENGINE=InnoDB;


--
-- Table structure for table `DataTrackCollaborator`
--

DROP TABLE IF EXISTS `DataTrackCollaborator`;
CREATE TABLE `DataTrackCollaborator` (
  `idDataTrack` int(10)  NOT NULL,
  `idAppUser` int(10)  NOT NULL,
  PRIMARY KEY  (`idDataTrack`,`idAppUser`),
  KEY `FK_DataTrackCollaborator_AppUser` (`idAppUser`),
  CONSTRAINT `FK_DataTrackCollaborator_DataTrack` FOREIGN KEY (`idDataTrack`) REFERENCES `DataTrack` (`idDataTrack`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_DataTrackCollaborator_AppUser` FOREIGN KEY (`idAppUser`) REFERENCES `AppUser` (`idAppUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

--
-- Table structure for table `DataTrackFolder`
--

DROP TABLE IF EXISTS `DataTrackFolder`;
CREATE TABLE `DataTrackFolder` (
  `idDataTrackFolder` int(10)  NOT NULL auto_increment,
  `name` varchar(2000) NOT NULL,
  `description` varchar(10000) default NULL,
  `idParentDataTrackFolder` int(10)  default NULL,
  `idGenomeBuild` int(10)  default NULL,
  `idLab` int(10)  default NULL,
  `createdBy` varchar(200) default NULL,
  `createDate` datetime default NULL,
  PRIMARY KEY  USING BTREE (`idDataTrackFolder`),
  KEY `FK_DataTrackFolder_GenomeBuild` (`idGenomeBuild`),
  KEY `FK_DataTrackFolder_parentDataTrackFolder` USING BTREE (`idParentDataTrackFolder`),
  KEY `FK_DataTrackFolder_Lab` (`idLab`),
  CONSTRAINT `FK_DataTrackFolder_GenomeBuild` FOREIGN KEY (`idGenomeBuild`) REFERENCES `GenomeBuild` (`idGenomeBuild`),
  CONSTRAINT `FK_DataTrackFolder_parentDataTrackFolder` FOREIGN KEY (`idParentDataTrackFolder`) REFERENCES `DataTrackFolder` (`idDataTrackFolder`),
  CONSTRAINT `FK_DataTrackFolder_Lab` FOREIGN KEY (`idLab`) REFERENCES `Lab` (`idLab`)
) ENGINE=InnoDB;

--
-- Table structure for table `DataTrackToDataTrackFolder`
--

DROP TABLE IF EXISTS `DataTrackToFolder`;
CREATE TABLE `DataTrackToFolder` (
  `idDataTrack` int(10)  NOT NULL,
  `idDataTrackFolder` int(10)  NOT NULL,
  PRIMARY KEY  (`idDataTrack`,`idDataTrackFolder`),
  KEY `FK_DataTrackToDataTrackFolder_DataTrackFolder` (`idDataTrackFolder`),
  CONSTRAINT `FK_DataTrackToDataTrackFolder_DataTrackFolder` FOREIGN KEY (`idDataTrackFolder`) REFERENCES `DataTrackFolder` (`idDataTrackFolder`),
  CONSTRAINT `FK_DataTrackToGrouping_DataTrack` FOREIGN KEY (`idDataTrack`) REFERENCES `DataTrack` (`idDataTrack`)
) ENGINE=InnoDB;


--
-- Table structure for table `UnloadDataTrack`
--

DROP TABLE IF EXISTS `UnloadDataTrack`;
CREATE TABLE `UnloadDataTrack` (
  `idUnloadDataTrack` int(10)  NOT NULL auto_increment,
  `typeName` varchar(2000) NOT NULL,
  `idAppUser` int(10)  default NULL,
  `idGenomeBuild` int(10)  NOT NULL,
  PRIMARY KEY  (`idUnloadDataTrack`),
  KEY `FK_UnloadDataTrack_AppUser` (`idAppUser`),
  KEY `FK_UnloadDataTrack_GenomeBuild` (`idGenomeBuild`)
) ENGINE=InnoDB;

--
-- Table structure for table `DataTrackFile`
--
DROP TABLE IF EXISTS `DataTrackFile`;
CREATE TABLE `DataTrackFile` (
  `idDataTrackFile` int(10)  NOT NULL auto_increment,
  `idAnalysisFile` int(10)   NULL,
  `idDataTrack` int(10)  NULL,
  PRIMARY KEY  (`idDataTrackFile`),
  KEY `FK_DataTrackFile_DataTrack` (`idDataTrack`),
  KEY `FK_DataTrackFile_AnalysisFile` (`idAnalysisFile`),
  CONSTRAINT `FK_DataTrackFile_DataTrack` FOREIGN KEY (`idDataTrack`) REFERENCES `DataTrack` (`idDataTrack`),
  CONSTRAINT `FK_AnalysisFile_DataTrack` FOREIGN KEY (`idAnalysisFile`) REFERENCES `AnalysisFile` (`idAnalysisFile`)
) ENGINE=InnoDB;

--
-- Table structure for table `AlignmentProfile`
--
DROP TABLE IF EXISTS `gnomex`.`AlignmentProfile`;
CREATE TABLE `gnomex`.`AlignmentProfile` (
  `idAlignmentProfile` INT(10) NOT NULL AUTO_INCREMENT,
  `alignmentProfileName` VARCHAR(120) NOT NULL,
  `description` VARCHAR(10000) NULL,
  `parameters` VARCHAR(500) NULL,
  `isActive` CHAR(1) NULL,
  `idAlignmentPlatform` int(10) NOT NULL,
  `idSeqRunType` INT(10) NOT NULL,
  PRIMARY KEY (`idAlignmentProfile`),
  CONSTRAINT `FK_AlignmentProfile_AlignmentPlatform` FOREIGN KEY `FK_AlignmentProfile_AlignmentPlatform` (`idAlignmentPlatform`)
    REFERENCES `gnomex`.`AlignmentPlatform` (`idAlignmentPlatform`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AlignmentProfile_SeqRunType` FOREIGN KEY `FK_AlignmentProfile_SeqRunType` (`idSeqRunType`)
    REFERENCES `gnomex`.`SeqRunType` (`idSeqRunType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

--
-- Table structure for table `AlignmentPlatform`
--
DROP TABLE IF EXISTS `gnomex`.`AlignmentPlatform`;
CREATE TABLE `gnomex`.`AlignmentPlatform` (
  `idAlignmentPlatform` INT(10) NOT NULL AUTO_INCREMENT,
  `alignmentPlatformName` VARCHAR(120) NOT NULL,
  `webServiceName` VARCHAR(120) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idAlignmentPlatform`)
) ENGINE = INNODB;

--
-- Table structure for table `GenomeIndex`
--
DROP TABLE IF EXISTS `gnomex`.`GenomeIndex`;
CREATE TABLE `gnomex`.`GenomeIndex` (
  `idGenomeIndex` INT(10) NOT NULL AUTO_INCREMENT,
  `genomeIndexName` VARCHAR(120) NOT NULL,
  `webServiceName` VARCHAR(120) NOT NULL,
  `isActive` CHAR(1) NULL,
  `idOrganism` INT(10) NOT NULL,
  PRIMARY KEY (`idGenomeIndex`),
  CONSTRAINT `FK_GenomeIndex_Organism` FOREIGN KEY `FK_GenomeIndex_Organism` (`idOrganism`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

--
-- Table structure for table `AlignmentProfileGenomeIndex`
--
DROP TABLE IF EXISTS `gnomex`.`AlignmentProfileGenomeIndex`;
CREATE TABLE `gnomex`.`AlignmentProfileGenomeIndex` (
  `idAlignmentProfile` INT(10) NOT NULL,
  `idGenomeIndex` INT(10) NOT NULL,
  PRIMARY KEY (`idAlignmentProfile`, `idGenomeIndex`),
  CONSTRAINT `FK_AlignmentProfileGenomeIndex_AlignmentProfile` FOREIGN KEY `FK_AlignmentProfileGenomeIndex_AlignmentProfile` (`idAlignmentProfile`)
    REFERENCES `gnomex`.`AlignmentProfile` (`idAlignmentProfile`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AlignmentProfileGenomeIndex_GenomeIndex` FOREIGN KEY `FK_AlignmentProfileGenomeIndex_GenomeIndex` (`idGenomeIndex`)
    REFERENCES `gnomex`.`GenomeIndex` (`idGenomeIndex`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Instrument`;
CREATE TABLE `gnomex`.`Instrument` (
  `idInstrument` INT(10) NOT NULL AUTO_INCREMENT,
  `instrument` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idInstrument`)
)
ENGINE = INNODB;

--
-- Core Facility Manager
--
DROP TABLE IF EXISTS `gnomex`.`CoreFacilityManager`;
CREATE TABLE `gnomex`.`CoreFacilityManager` (
  `idCoreFacility` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  PRIMARY KEY (`idCoreFacility`, `idAppUser`),
  CONSTRAINT `FK_CoreFacilityManager_AppUser` FOREIGN KEY  (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CoreFacilityManager_CoreFacility` FOREIGN KEY  (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

--
-- Core Facility Lab
--
DROP TABLE IF EXISTS `gnomex`.`CoreFacilityLab`;
CREATE TABLE `gnomex`.`CoreFacilityLab` (
  `idCoreFacility` INT(10) NOT NULL,
  `idLab` INT(10) NOT NULL,
  PRIMARY KEY (`idCoreFacility`, `idLab`),
  CONSTRAINT `FK_CoreFacilityLab_Lab` FOREIGN KEY  (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CoreFacilityLab_CoreFacility` FOREIGN KEY  (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

--
-- SampleDropOffLocation 
--
DROP TABLE IF EXISTS `gnomex`.`SampleDropOffLocation`;
CREATE TABLE `gnomex`.`SampleDropOffLocation` (
  `idSampleDropOffLocation` INT(10) NOT NULL AUTO_INCREMENT,
  `sampleDropOffLocation`   VARCHAR(200) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSampleDropOffLocation`)
)
ENGINE = INNODB;

--
-- PlateType 
--
DROP TABLE IF EXISTS `gnomex`.`PlateType`;
CREATE TABLE `gnomex`.`PlateType` (
  `codePlateType` VARCHAR(10) NOT NULL,
  `plateTypeDescription`   VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codePlateType`)
)
ENGINE = INNODB;

-- 
-- New Columns for FlowCell
--
alter table FlowCell add column runNumber int(10) NULL;
alter table FlowCell add idInstrument int(10) NULL;
alter table FlowCell add side char(1) NULL;

--
-- Table structure for table `Topic`
--
DROP TABLE IF EXISTS `gnomex`.`Topic`;
CREATE TABLE `gnomex`.`Topic` (
  `idTopic` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(2000) NOT NULL,
  `description` VARCHAR(10000) NULL,
  `idParentTopic` int(10) NOT NULL,  
  `idLab` int(10) NOT NULL,
  `createdBy` VARCHAR(200) NOT NULL,
  `createDate` datetime default NULL,
  `idAppUser` int(10)  default NULL,  
  PRIMARY KEY (`idTopic`),
  KEY `FK_Topic_AppUser` (`idAppUser`),
  KEY `FK_Topic_Lab` (`idLab`),  
  KEY `FK_Topic_ParentTopic` (`idParentTopic`),  
  CONSTRAINT `FK_Topic_AppUser` FOREIGN KEY (`idAppUser`) REFERENCES `AppUser` (`idAppUser`),
  CONSTRAINT `FK_Topic_Lab` FOREIGN KEY (`idLab`) REFERENCES `Lab` (`idLab`),
  CONSTRAINT `FK_Topic_ParentTopic` FOREIGN KEY (`idParentTopic`) REFERENCES `Topic` (`idTopic`)
) ENGINE = INNODB;


--
-- Table structure for table `RequestToTopic`
--
DROP TABLE IF EXISTS `RequestToTopic`;
CREATE TABLE `RequestToTopic` (
  `idTopic` int(10)  NOT NULL,
  `idRequest` int(10)  NOT NULL,
  PRIMARY KEY  (`idTopic`,`idRequest`),
  KEY `FK_TopicRequest_Request` (`idRequest`),
  CONSTRAINT `FK_RequestToTopic_Topic` FOREIGN KEY (`idTopic`) REFERENCES `Topic` (`idTopic`),
  CONSTRAINT `FK_TopicRequest_Request` FOREIGN KEY (`idRequest`) REFERENCES `Request` (`idRequest`)
) ENGINE=InnoDB;

--
-- Table structure for table `AnalysisToTopic`
--
DROP TABLE IF EXISTS `AnalysisToTopic`;
CREATE TABLE `AnalysisToTopic` (
  `idTopic` int(10)  NOT NULL,
  `idAnalysis` int(10)  NOT NULL,
  PRIMARY KEY  (`idTopic`,`idAnalysis`),
  KEY `FK_TopicAnalysis_Analysis` (`idAnalysis`),
  CONSTRAINT `FK_AnalysisToTopic_Topic` FOREIGN KEY (`idTopic`) REFERENCES `Topic` (`idTopic`),
  CONSTRAINT `FK_TopicAnalysis_Analysis` FOREIGN KEY (`idAnalysis`) REFERENCES `Analysis` (`idAnalysis`)
) ENGINE=InnoDB;

--
-- Table structure for table `DataTrackToTopic`
--
DROP TABLE IF EXISTS `DataTrackToTopic`;
CREATE TABLE `DataTrackToTopic` (
  `idTopic` int(10)  NOT NULL,
  `idDataTrack` int(10)  NOT NULL,
  PRIMARY KEY  (`idTopic`,`idDataTrack`),
  KEY `FK_TopicDataTrack_DataTrack` (`idDataTrack`),
  CONSTRAINT `FK_DataTrackToTopic_Topic` FOREIGN KEY (`idTopic`) REFERENCES `Topic` (`idTopic`),
  CONSTRAINT `FK_TopicDataTrack_DataTrack` FOREIGN KEY (`idDataTrack`) REFERENCES `DataTrack` (`idDataTrack`)
) ENGINE=InnoDB;



SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------
-- EOF