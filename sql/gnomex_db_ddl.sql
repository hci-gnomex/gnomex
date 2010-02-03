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
  `idGenomeBuild` INT(10) NULL,
  `codeVisibility` VARCHAR(10) NOT NULL,
  `createDate` DATETIME NULL,
  `idAppUser` INT(10) NULL,
  PRIMARY KEY (`idAnalysis`),
  CONSTRAINT `FK_Analysis_Lab` FOREIGN KEY `FK_Analysis_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_AnalysisType` FOREIGN KEY `FK_Analysis_AnalysisType` (`idAnalysisType`)
    REFERENCES `gnomex`.`AnalysisType` (`idAnalysisType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_AnalysisProtocol` FOREIGN KEY `FK_Analysis_AnalysisProtocol` (`idAnalysisProtocol`)
    REFERENCES `gnomex`.`AnalysisProtocol` (`idAnalysisProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_Organism` FOREIGN KEY `FK_Analysis_Organism` (`idOrganism`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_GenomeBuild` FOREIGN KEY `FK_Analysis_GenomeBuild` (`idGenomeBuild`)
    REFERENCES `gnomex`.`GenomeBuild` (`idGenomeBuild`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_AppUser` FOREIGN KEY `FK_Analysis_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Analysis_Visibility` FOREIGN KEY `FK_Analysis_Visibility` (`codeVisibility`)
    REFERENCES `gnomex`.`Visibility` (`codeVisibility`)
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
  `comments` VARCHAR(2000) NULL,
  `uploadDate` DATETIME NULL,
  `idAnalysis` INT(10) NULL,
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

DROP TABLE IF EXISTS `gnomex`.`BillingCategory`;
CREATE TABLE `gnomex`.`BillingCategory` (
  `idBillingCategory` INT(10) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(500) NOT NULL,
  `pluginClassName` VARCHAR(500) NULL,
  `codeBillingChargeKind` VARCHAR(10) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idBillingCategory`),
  CONSTRAINT `FK_BillingTemplate_BillingChargeKind` FOREIGN KEY `FK_BillingTemplate_BillingChargeKind` (`codeBillingChargeKind`)
    REFERENCES `gnomex`.`BillingChargeKind` (`codeBillingChargeKind`)
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
  `totalPrice` DECIMAL(8, 2) NULL,
  `idBillingPeriod` INT(10) NULL,
  `codeBillingStatus` VARCHAR(10) NULL,
  `idBillingCategory` INT(10) NULL,
  `idBillingPrice` INT(10) NULL,
  `idRequest` INT(10) NOT NULL,
  `idBillingAccount` INT(10) NOT NULL,
  `percentagePrice` DECIMAL(3, 2) NOT NULL,
  `notes` VARCHAR(500) NULL,
  `idLab` INT(10) NOT NULL,
  PRIMARY KEY (`idBillingItem`),
  CONSTRAINT `FK_BillingItem_BillingCategory` FOREIGN KEY `FK_BillingItem_BillingCategory` (`idBillingCategory`)
    REFERENCES `gnomex`.`BillingCategory` (`idBillingCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingChargeKind` FOREIGN KEY `FK_BillingItem_BillingChargeKind` (`codeBillingChargeKind`)
    REFERENCES `gnomex`.`BillingChargeKind` (`codeBillingChargeKind`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingStatus` FOREIGN KEY `FK_BillingItem_BillingStatus` (`codeBillingStatus`)
    REFERENCES `gnomex`.`BillingStatus` (`codeBillingStatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingAccount` FOREIGN KEY `FK_BillingItem_BillingAccount` (`idBillingAccount`)
    REFERENCES `gnomex`.`BillingAccount` (`idBillingAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingPrice` FOREIGN KEY `FK_BillingItem_BillingPrice` (`idBillingPrice`)
    REFERENCES `gnomex`.`BillingPrice` (`idBillingPrice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_Lab` FOREIGN KEY `FK_BillingItem_Lab` (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_Request` FOREIGN KEY `FK_BillingItem_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingItem_BillingPeriod` FOREIGN KEY `FK_BillingItem_BillingPeriod` (`idBillingPeriod`)
    REFERENCES `gnomex`.`BillingPeriod` (`idBillingPeriod`)
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

DROP TABLE IF EXISTS `gnomex`.`BillingPrice`;
CREATE TABLE `gnomex`.`BillingPrice` (
  `idBillingPrice` INT(10) NOT NULL AUTO_INCREMENT,
  `idBillingCategory` INT(10) NULL,
  `idBillingTemplate` INT(10) NULL,
  `description` VARCHAR(100) NULL,
  `filter1` VARCHAR(10) NULL,
  `filter2` VARCHAR(10) NULL,
  `unitPrice` DECIMAL(6, 2) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idBillingPrice`),
  CONSTRAINT `FK_BillingPrice_BillingTemplate` FOREIGN KEY `FK_BillingPrice_BillingTemplate` (`idBillingTemplate`)
    REFERENCES `gnomex`.`BillingTemplate` (`idBillingTemplate`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
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

DROP TABLE IF EXISTS `gnomex`.`BillingTemplate`;
CREATE TABLE `gnomex`.`BillingTemplate` (
  `idBillingTemplate` INT(10) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(100) NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idBillingTemplate`),
  CONSTRAINT `FK_BillingTemplateCategory_RequestCategory` FOREIGN KEY `FK_BillingTemplateCategory_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`BillingTemplateEntry`;
CREATE TABLE `gnomex`.`BillingTemplateEntry` (
  `idBillingTemplate` INT(10) NOT NULL,
  `idBillingCategory` INT(10) NOT NULL,
  `sortOrder` INT(10) NULL,
  PRIMARY KEY (`idBillingCategory`, `idBillingTemplate`),
  CONSTRAINT `FK_BillingTemplateEntry_BillingCategory` FOREIGN KEY `FK_BillingTemplateEntry_BillingCategory` (`idBillingCategory`)
    REFERENCES `gnomex`.`BillingCategory` (`idBillingCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_BillingTemplateEntry_BillingTemplate` FOREIGN KEY `FK_BillingTemplateEntry_BillingTemplate` (`idBillingTemplate`)
    REFERENCES `gnomex`.`BillingTemplate` (`idBillingTemplate`)
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

DROP TABLE IF EXISTS `gnomex`.`FlowCell`;
CREATE TABLE `gnomex`.`FlowCell` (
  `idFlowCell` INT(10) NOT NULL AUTO_INCREMENT,
  `idNumberSequencingCycles` INT(10) NULL,
  `idSeqRunType` INT(10) NULL,
  `number` VARCHAR(10) NULL,
  `createDate` DATETIME NULL,
  `notes` VARCHAR(200) NULL,
  `barcode` VARCHAR(100) NULL,
  PRIMARY KEY (`idFlowCell`),
  CONSTRAINT `FK_FlowCell_NumberSequencingCycles` FOREIGN KEY `FK_FlowCell_NumberSequencingCycles` (`idNumberSequencingCycles`)
    REFERENCES `gnomex`.`NumberSequencingCycles` (`idNumberSequencingCycles`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_FlowCell_SeqRunType` FOREIGN KEY `FK_FlowCell_SeqRunType` (`idSeqRunType`)
    REFERENCES `gnomex`.`SeqRunType` (`idSeqRunType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`FlowCellChannel`;
CREATE TABLE `gnomex`.`FlowCellChannel` (
  `idFlowCellChannel` INT(10) NOT NULL AUTO_INCREMENT,
  `idFlowCell` INT(10) NOT NULL,
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
  PRIMARY KEY (`idGenomeBuild`),
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
  `isExternal` VARCHAR(1) NULL,
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
  `application` VARCHAR(50) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeApplication`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`NumberSequencingCycles`;
CREATE TABLE `gnomex`.`NumberSequencingCycles` (
  `idNumberSequencingCycles` INT(10) NOT NULL AUTO_INCREMENT,
  `numberSequencingCycles` INT(10) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idNumberSequencingCycles`)
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
  `barcodeSequence` VARCHAR(20) NOT NULL,
  `idOligoBarcodeScheme` INT(10) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idOligoBarcode`),
  CONSTRAINT `FK_OligoBarcode_OligoBarcodeScheme` FOREIGN KEY `FK_OligoBarcode_OligoBarcodeScheme` (`idOligoBarcodeScheme`)
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
  PRIMARY KEY (`idOrganism`),
  CONSTRAINT `FK_Organism_AppUser` FOREIGN KEY `FK_Organism_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

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

DROP TABLE IF EXISTS `gnomex`.`Property`;
CREATE TABLE `gnomex`.`Property` (
  `idProperty` INT(10) NOT NULL AUTO_INCREMENT,
  `propertyName` VARCHAR(200) NULL,
  `propertyValue` VARCHAR(2000) NULL,
  `propertyDescription` VARCHAR(2000) NULL,
  `forServerOnly` CHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`idProperty`)
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
  CONSTRAINT `FK_Request_Visibility` FOREIGN KEY `FK_Request_Visibility` (`codeVisibility`)
    REFERENCES `gnomex`.`Visibility` (`codeVisibility`)
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
  PRIMARY KEY (`codeRequestCategory`),
  CONSTRAINT `FK_RequestCategory_Vendor` FOREIGN KEY `FK_RequestCategory_Vendor` (`idVendor`)
    REFERENCES `gnomex`.`Vendor` (`idVendor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`RequestCategoryApplication`;
CREATE TABLE `gnomex`.`RequestCategoryApplication` (
  `codeRequestCategory` VARCHAR(10) NOT NULL,
  `codeApplication` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`codeRequestCategory`, `codeApplication`)
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
  `idSampleSource` INT(10) NULL,
  `idSamplePrepMethod` INT(10) NULL,
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
  `seqPrepLibConcentration` INT(10) NULL,
  `seqPrepQualCodeBioanalyzerChipType` VARCHAR(10) NULL,
  `seqPrepGelFragmentSizeFrom` INT(10) NULL,
  `seqPrepGelFragmentSizeTo` INT(10) NULL,
  `seqPrepStockLibVol` DECIMAL(8, 1) NULL,
  `seqPrepStockEBVol` DECIMAL(8, 1) NULL,
  `seqPrepStockDate` DATETIME NULL,
  `seqPrepStockFailed` CHAR(1) NULL,
  `seqPrepStockBypassed` CHAR(1) NULL,
  PRIMARY KEY (`idSample`),
  CONSTRAINT `FK_Sample_Organism` FOREIGN KEY `FK_Sample_Organism` (`idOrganism`)
    REFERENCES `gnomex`.`Organism` (`idOrganism`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Sample_SampleSource` FOREIGN KEY `FK_Sample_SampleSource` (`idSampleSource`)
    REFERENCES `gnomex`.`SampleSource` (`idSampleSource`)
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

DROP TABLE IF EXISTS `gnomex`.`SampleCharacteristic`;
CREATE TABLE `gnomex`.`SampleCharacteristic` (
  `codeSampleCharacteristic` VARCHAR(10) NOT NULL,
  `sampleCharacteristic` VARCHAR(50) NULL,
  `mageOntologyCode` VARCHAR(50) NULL,
  `mageOntologyDefinition` VARCHAR(5000) NULL,
  `isActive` CHAR(1) NULL,
  `idAppUser` INT(10) NULL,
  PRIMARY KEY (`codeSampleCharacteristic`),
  CONSTRAINT `FK_SampleCharacteristic_AppUser` FOREIGN KEY `FK_SampleCharacteristic_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SampleCharacteristicEntry`;
CREATE TABLE `gnomex`.`SampleCharacteristicEntry` (
  `idSampleCharacteristicEntry` INT(10) NOT NULL AUTO_INCREMENT,
  `codeSampleCharacteristic` VARCHAR(10) NULL,
  `idSample` INT(10) NULL,
  `valueString` VARCHAR(200) NULL,
  `otherLabel` VARCHAR(100) NULL,
  PRIMARY KEY (`idSampleCharacteristicEntry`),
  CONSTRAINT `FK_SampleCharacteristicEntry_Sample` FOREIGN KEY `FK_SampleCharacteristicEntry_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SampleCharacteristicEntry_SampleCharacteristic` FOREIGN KEY `FK_SampleCharacteristicEntry_SampleCharacteristic` (`codeSampleCharacteristic`)
    REFERENCES `gnomex`.`SampleCharacteristic` (`codeSampleCharacteristic`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
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

DROP TABLE IF EXISTS `gnomex`.`SamplePrepMethodRequestCategory`;
CREATE TABLE `gnomex`.`SamplePrepMethodRequestCategory` (
  `idSamplePrepMethodRequestCategory` INT(10) NOT NULL AUTO_INCREMENT,
  `idSamplePrepMethod` INT(10) NULL,
  `codeRequestCategory` VARCHAR(10) NULL,
  PRIMARY KEY (`idSamplePrepMethodRequestCategory`),
  CONSTRAINT `FK_SamplePrepMethodRequestCategory_SamplePrepMethod` FOREIGN KEY `FK_SamplePrepMethodRequestCategory_SamplePrepMethod` (`idSamplePrepMethod`)
    REFERENCES `gnomex`.`SamplePrepMethod` (`idSamplePrepMethod`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SamplePrepMethodRequestCategory_RequestCategory` FOREIGN KEY `FK_SamplePrepMethodRequestCategory_RequestCategory` (`codeRequestCategory`)
    REFERENCES `gnomex`.`RequestCategory` (`codeRequestCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SamplePrepMethodSampleType`;
CREATE TABLE `gnomex`.`SamplePrepMethodSampleType` (
  `idSamplePrepMethodSampleType` INT(10) NOT NULL AUTO_INCREMENT,
  `idSampleType` INT(10) NULL,
  `idSamplePrepMethod` INT(10) NULL,
  `isDefaultForSampleType` CHAR(1) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSamplePrepMethodSampleType`),
  CONSTRAINT `FK_SamplePrepMethodSampleType_SampleType` FOREIGN KEY `FK_SamplePrepMethodSampleType_SampleType` (`idSampleType`)
    REFERENCES `gnomex`.`SampleType` (`idSampleType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SamplePrepMethodSampleType_SamplePrepMethod` FOREIGN KEY `FK_SamplePrepMethodSampleType_SamplePrepMethod` (`idSamplePrepMethod`)
    REFERENCES `gnomex`.`SamplePrepMethod` (`idSamplePrepMethod`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SamplePrepMethodSeqRunType`;
CREATE TABLE `gnomex`.`SamplePrepMethodSeqRunType` (
  `idSamplePrepMethodSeqRunType` INT(10) NOT NULL AUTO_INCREMENT,
  `idSamplePrepMethod` INT(10) NULL,
  `idSeqRunType` INT(10) NULL,
  PRIMARY KEY (`idSamplePrepMethodSeqRunType`),
  CONSTRAINT `FK_SamplePrepMethodSeqRunType_SeqRunType` FOREIGN KEY `FK_SamplePrepMethodSeqRunType_SeqRunType` (`idSeqRunType`)
    REFERENCES `gnomex`.`SeqRunType` (`idSeqRunType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SamplePrepMethodFlowCellType_SamplePrepMethod` FOREIGN KEY `FK_SamplePrepMethodFlowCellType_SamplePrepMethod` (`idSamplePrepMethod`)
    REFERENCES `gnomex`.`SamplePrepMethod` (`idSamplePrepMethod`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
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

DROP TABLE IF EXISTS `gnomex`.`SeqRunType`;
CREATE TABLE `gnomex`.`SeqRunType` (
  `idSeqRunType` INT(10) NOT NULL AUTO_INCREMENT,
  `seqRunType` VARCHAR(200) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSeqRunType`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`SequenceLane`;
CREATE TABLE `gnomex`.`SequenceLane` (
  `idSequenceLane` INT(10) NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(100) NULL,
  `createDate` DATETIME NULL,
  `idRequest` INT(10) NOT NULL,
  `idSample` INT(10) NOT NULL,
  `idSeqRunType` INT(10) NULL,
  `idNumberSequencingCycles` INT(10) NULL,
  `analysisInstructions` VARCHAR(2000) NULL,
  `idGenomeBuildAlignTo` INT(10) NULL,
  `idFlowCellChannel` INT(10) NULL,
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
    ON UPDATE NO ACTION
)
ENGINE = INNODB;



SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------
-- EOF