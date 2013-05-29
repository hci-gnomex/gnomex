
USE gnomex;

delimiter $$




--
-- Audit Table For alignmentplatform 
--

CREATE TABLE IF NOT EXISTS `alignmentplatform_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentPlatform`  int(10)  NULL DEFAULT NULL
 ,`alignmentPlatformName`  varchar(120)  NULL DEFAULT NULL
 ,`webServiceName`  varchar(120)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For alignmentprofile 
--

CREATE TABLE IF NOT EXISTS `alignmentprofile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`alignmentProfileName`  varchar(120)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`parameters`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAlignmentPlatform`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For alignmentprofilegenomeindex 
--

CREATE TABLE IF NOT EXISTS `alignmentprofilegenomeindex_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysis 
--

CREATE TABLE IF NOT EXISTS `analysis_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisProtocol`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`privacyExpirationDate`  datetime  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysiscollaborator 
--

CREATE TABLE IF NOT EXISTS `analysiscollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`canUploadData`  char(1)  NULL DEFAULT NULL
 ,`canUpdate`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysisexperimentitem 
--

CREATE TABLE IF NOT EXISTS `analysisexperimentitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisExperimentItem`  int(10)  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`comments`  varchar(2000)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysisfile 
--

CREATE TABLE IF NOT EXISTS `analysisfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisFile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`comments`  varchar(2000)  NULL DEFAULT NULL
 ,`uploadDate`  datetime  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`qualifiedFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`baseFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysisgenomebuild 
--

CREATE TABLE IF NOT EXISTS `analysisgenomebuild_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysisgroup 
--

CREATE TABLE IF NOT EXISTS `analysisgroup_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`ucscUrl`  varchar(250)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysisgroupitem 
--

CREATE TABLE IF NOT EXISTS `analysisgroupitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysisprotocol 
--

CREATE TABLE IF NOT EXISTS `analysisprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisProtocol`  int(10)  NULL DEFAULT NULL
 ,`analysisProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysistotopic 
--

CREATE TABLE IF NOT EXISTS `analysistotopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For analysistype 
--

CREATE TABLE IF NOT EXISTS `analysistype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`analysisType`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For application 
--

CREATE TABLE IF NOT EXISTS `application_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`application`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idApplicationTheme`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeTo`  int(10)  NULL DEFAULT NULL
 ,`hasCaptureLibDesign`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For applicationtheme 
--

CREATE TABLE IF NOT EXISTS `applicationtheme_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idApplicationTheme`  int(10)  NULL DEFAULT NULL
 ,`applicationTheme`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For appuser 
--

CREATE TABLE IF NOT EXISTS `appuser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`lastName`  varchar(200)  NULL DEFAULT NULL
 ,`firstName`  varchar(200)  NULL DEFAULT NULL
 ,`uNID`  varchar(50)  NULL DEFAULT NULL
 ,`email`  varchar(200)  NULL DEFAULT NULL
 ,`phone`  varchar(50)  NULL DEFAULT NULL
 ,`department`  varchar(100)  NULL DEFAULT NULL
 ,`institute`  varchar(100)  NULL DEFAULT NULL
 ,`jobTitle`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeUserPermissionKind`  varchar(10)  NULL DEFAULT NULL
 ,`userNameExternal`  varchar(100)  NULL DEFAULT NULL
 ,`passwordExternal`  varchar(100)  NULL DEFAULT NULL
 ,`ucscUrl`  varchar(200)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For arraycoordinate 
--

CREATE TABLE IF NOT EXISTS `arraycoordinate_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`x`  int(10)  NULL DEFAULT NULL
 ,`y`  int(10)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For arraydesign 
--

CREATE TABLE IF NOT EXISTS `arraydesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idArrayDesign`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`accessionNumberUArrayExpress`  varchar(100)  NULL DEFAULT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For assay 
--

CREATE TABLE IF NOT EXISTS `assay_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAssay`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For billingaccount 
--

CREATE TABLE IF NOT EXISTS `billingaccount_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`accountName`  varchar(200)  NULL DEFAULT NULL
 ,`accountNumber`  varchar(100)  NULL DEFAULT NULL
 ,`expirationDate`  datetime  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`accountNumberBus`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberOrg`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberFund`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberActivity`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberProject`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberAccount`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberAu`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberYear`  varchar(10)  NULL DEFAULT NULL
 ,`idFundingAgency`  int(10)  NULL DEFAULT NULL
 ,`idCreditCardCompany`  int(10)  NULL DEFAULT NULL
 ,`isPO`  char(1)  NULL DEFAULT NULL
 ,`isCreditCard`  char(1)  NULL DEFAULT NULL
 ,`zipCode`  char(20)  NULL DEFAULT NULL
 ,`isApproved`  char(1)  NULL DEFAULT NULL
 ,`approvedDate`  datetime  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`submitterEmail`  varchar(200)  NULL DEFAULT NULL
 ,`submitterUID`  varchar(50)  NULL DEFAULT NULL
 ,`totalDollarAmount`  decimal(12,2)  NULL DEFAULT NULL
 ,`purchaseOrderForm`  longblob  NULL DEFAULT NULL
 ,`orderFormFileType`  varchar(10)  NULL DEFAULT NULL
 ,`orderFormFileSize`  bigint(20)  NULL DEFAULT NULL
 ,`shortAcct`  varchar(10)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`custom1`  varchar(50)  NULL DEFAULT NULL
 ,`custom2`  varchar(50)  NULL DEFAULT NULL
 ,`custom3`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For billingaccountuser 
--

CREATE TABLE IF NOT EXISTS `billingaccountuser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For billingchargekind 
--

CREATE TABLE IF NOT EXISTS `billingchargekind_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`billingChargeKind`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For billingitem 
--

CREATE TABLE IF NOT EXISTS `billingitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingItem`  int(10)  NULL DEFAULT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`category`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(6,2)  NULL DEFAULT NULL
 ,`invoicePrice`  decimal(8,2)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`codeBillingStatus`  varchar(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`percentagePrice`  decimal(3,2)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`completeDate`  datetime  NULL DEFAULT NULL
 ,`splitType`  char(1)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idInvoice`  int(10)  NULL DEFAULT NULL
 ,`idDiskUsageByMonth`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For billingperiod 
--

CREATE TABLE IF NOT EXISTS `billingperiod_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`billingPeriod`  varchar(50)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`endDate`  datetime  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For billingslideproductclass 
--

CREATE TABLE IF NOT EXISTS `billingslideproductclass_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingSlideProductClass`  int(10)  NULL DEFAULT NULL
 ,`billingSlideProductClass`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For billingslideserviceclass 
--

CREATE TABLE IF NOT EXISTS `billingslideserviceclass_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
 ,`billingSlideServiceClass`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For billingstatus 
--

CREATE TABLE IF NOT EXISTS `billingstatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBillingStatus`  varchar(10)  NULL DEFAULT NULL
 ,`billingStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For bioanalyzerchiptype 
--

CREATE TABLE IF NOT EXISTS `bioanalyzerchiptype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`bioanalyzerChipType`  varchar(50)  NULL DEFAULT NULL
 ,`concentrationRange`  varchar(50)  NULL DEFAULT NULL
 ,`maxSampleBufferStrength`  varchar(50)  NULL DEFAULT NULL
 ,`costPerSample`  decimal(5,2)  NULL DEFAULT NULL
 ,`sampleWellsPerChip`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For chromatogram 
--

CREATE TABLE IF NOT EXISTS `chromatogram_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idChromatogram`  int(10)  NULL DEFAULT NULL
 ,`idPlateWell`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`displayName`  varchar(200)  NULL DEFAULT NULL
 ,`readLength`  int(10)  NULL DEFAULT NULL
 ,`trimmedLength`  int(11)  NULL DEFAULT NULL
 ,`q20`  int(10)  NULL DEFAULT NULL
 ,`q40`  int(10)  NULL DEFAULT NULL
 ,`aSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`cSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`gSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`tSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`releaseDate`  datetime  NULL DEFAULT NULL
 ,`qualifiedFilePath`  varchar(500)  NULL DEFAULT NULL
 ,`idReleaser`  int(10)  NULL DEFAULT NULL
 ,`lane`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For concentrationunit 
--

CREATE TABLE IF NOT EXISTS `concentrationunit_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`concentrationUnit`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For corefacility 
--

CREATE TABLE IF NOT EXISTS `corefacility_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`facilityName`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`showProjectAnnotations`  char(1)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`acceptOnlineWorkAuth`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For corefacilitylab 
--

CREATE TABLE IF NOT EXISTS `corefacilitylab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For corefacilitymanager 
--

CREATE TABLE IF NOT EXISTS `corefacilitymanager_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For creditcardcompany 
--

CREATE TABLE IF NOT EXISTS `creditcardcompany_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCreditCardCompany`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  varchar(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For datatrack 
--

CREATE TABLE IF NOT EXISTS `datatrack_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`summary`  varchar(5000)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`isLoaded`  char(1)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`dataPath`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For datatrackcollaborator 
--

CREATE TABLE IF NOT EXISTS `datatrackcollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For datatrackfile 
--

CREATE TABLE IF NOT EXISTS `datatrackfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrackFile`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisFile`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For datatrackfolder 
--

CREATE TABLE IF NOT EXISTS `datatrackfolder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`idParentDataTrackFolder`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For datatracktofolder 
--

CREATE TABLE IF NOT EXISTS `datatracktofolder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For datatracktotopic 
--

CREATE TABLE IF NOT EXISTS `datatracktotopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For diskusagebymonth 
--

CREATE TABLE IF NOT EXISTS `diskusagebymonth_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDiskUsageByMonth`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`asOfDate`  datetime  NULL DEFAULT NULL
 ,`lastCalcDate`  datetime  NULL DEFAULT NULL
 ,`totalAnalysisDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`assessedAnalysisDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`totalExperimentDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`assessedExperimentDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For experimentdesign 
--

CREATE TABLE IF NOT EXISTS `experimentdesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeExperimentDesign`  varchar(10)  NULL DEFAULT NULL
 ,`experimentDesign`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For experimentdesignentry 
--

CREATE TABLE IF NOT EXISTS `experimentdesignentry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentDesignEntry`  int(10)  NULL DEFAULT NULL
 ,`codeExperimentDesign`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For experimentfactor 
--

CREATE TABLE IF NOT EXISTS `experimentfactor_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeExperimentFactor`  varchar(10)  NULL DEFAULT NULL
 ,`experimentFactor`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For experimentfactorentry 
--

CREATE TABLE IF NOT EXISTS `experimentfactorentry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentFactorEntry`  int(10)  NULL DEFAULT NULL
 ,`codeExperimentFactor`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For experimentfile 
--

CREATE TABLE IF NOT EXISTS `experimentfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentFile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For featureextractionprotocol 
--

CREATE TABLE IF NOT EXISTS `featureextractionprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFeatureExtractionProtocol`  int(10)  NULL DEFAULT NULL
 ,`featureExtractionProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For flowcell 
--

CREATE TABLE IF NOT EXISTS `flowcell_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFlowCell`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`notes`  varchar(200)  NULL DEFAULT NULL
 ,`barcode`  varchar(100)  NULL DEFAULT NULL
 ,`codeSequencingPlatform`  varchar(10)  NULL DEFAULT NULL
 ,`idInstrument`  int(10)  NULL DEFAULT NULL
 ,`side`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For flowcellchannel 
--

CREATE TABLE IF NOT EXISTS `flowcellchannel_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`idFlowCell`  int(10)  NULL DEFAULT NULL
 ,`number`  int(10)  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idSequencingControl`  int(10)  NULL DEFAULT NULL
 ,`numberSequencingCyclesActual`  int(10)  NULL DEFAULT NULL
 ,`clustersPerTile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(500)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`firstCycleDate`  datetime  NULL DEFAULT NULL
 ,`firstCycleCompleted`  char(1)  NULL DEFAULT NULL
 ,`firstCycleFailed`  char(1)  NULL DEFAULT NULL
 ,`lastCycleDate`  datetime  NULL DEFAULT NULL
 ,`lastCycleCompleted`  char(1)  NULL DEFAULT NULL
 ,`lastCycleFailed`  char(1)  NULL DEFAULT NULL
 ,`sampleConcentrationpM`  decimal(8,2)  NULL DEFAULT NULL
 ,`pipelineDate`  datetime  NULL DEFAULT NULL
 ,`pipelineFailed`  char(1)  NULL DEFAULT NULL
 ,`isControl`  char(1)  NULL DEFAULT NULL
 ,`phiXErrorRate`  decimal(4,4)  NULL DEFAULT NULL
 ,`read1ClustersPassedFilterM`  decimal(6,2)  NULL DEFAULT NULL
 ,`read2ClustersPassedFilterM`  int(10)  NULL DEFAULT NULL
 ,`q30Gb`  decimal(4,1)  NULL DEFAULT NULL
 ,`q30Percent`  decimal(4,3)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For fundingagency 
--

CREATE TABLE IF NOT EXISTS `fundingagency_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFundingAgency`  int(10)  NULL DEFAULT NULL
 ,`fundingAgency`  varchar(200)  NULL DEFAULT NULL
 ,`isPeerReviewedFunding`  char(1)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For genomebuild 
--

CREATE TABLE IF NOT EXISTS `genomebuild_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`genomeBuildName`  varchar(500)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`isLatestBuild`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`das2Name`  varchar(200)  NULL DEFAULT NULL
 ,`buildDate`  datetime  NULL DEFAULT NULL
 ,`coordURI`  varchar(2000)  NULL DEFAULT NULL
 ,`coordVersion`  varchar(50)  NULL DEFAULT NULL
 ,`coordSource`  varchar(50)  NULL DEFAULT NULL
 ,`coordTestRange`  varchar(100)  NULL DEFAULT NULL
 ,`coordAuthority`  varchar(50)  NULL DEFAULT NULL
 ,`ucscName`  varchar(100)  NULL DEFAULT NULL
 ,`igvName`  varchar(50)  NULL DEFAULT NULL
 ,`dataPath`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For genomebuildalias 
--

CREATE TABLE IF NOT EXISTS `genomebuildalias_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idGenomeBuildAlias`  int(10)  NULL DEFAULT NULL
 ,`alias`  varchar(100)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For genomeindex 
--

CREATE TABLE IF NOT EXISTS `genomeindex_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
 ,`genomeIndexName`  varchar(120)  NULL DEFAULT NULL
 ,`webServiceName`  varchar(120)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For hybprotocol 
--

CREATE TABLE IF NOT EXISTS `hybprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idHybProtocol`  int(10)  NULL DEFAULT NULL
 ,`hybProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For hybridization 
--

CREATE TABLE IF NOT EXISTS `hybridization_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`notes`  varchar(2000)  NULL DEFAULT NULL
 ,`codeSlideSource`  varchar(10)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`idHybProtocol`  int(10)  NULL DEFAULT NULL
 ,`idHybBuffer`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSampleChannel1`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSampleChannel2`  int(10)  NULL DEFAULT NULL
 ,`idSlide`  int(10)  NULL DEFAULT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
 ,`idScanProtocol`  int(10)  NULL DEFAULT NULL
 ,`idFeatureExtractionProtocol`  int(10)  NULL DEFAULT NULL
 ,`hybDate`  datetime  NULL DEFAULT NULL
 ,`hybFailed`  char(1)  NULL DEFAULT NULL
 ,`hybBypassed`  char(1)  NULL DEFAULT NULL
 ,`extractionDate`  datetime  NULL DEFAULT NULL
 ,`extractionFailed`  char(1)  NULL DEFAULT NULL
 ,`extractionBypassed`  char(1)  NULL DEFAULT NULL
 ,`hasResults`  char(1)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For institution 
--

CREATE TABLE IF NOT EXISTS `institution_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`institution`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`isDefault`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For institutionlab 
--

CREATE TABLE IF NOT EXISTS `institutionlab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For instrument 
--

CREATE TABLE IF NOT EXISTS `instrument_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstrument`  int(10)  NULL DEFAULT NULL
 ,`instrument`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For instrumentrun 
--

CREATE TABLE IF NOT EXISTS `instrumentrun_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstrumentRun`  int(10)  NULL DEFAULT NULL
 ,`runDate`  datetime  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeInstrumentRunStatus`  varchar(10)  NULL DEFAULT NULL
 ,`comments`  varchar(200)  NULL DEFAULT NULL
 ,`label`  varchar(100)  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`creator`  varchar(50)  NULL DEFAULT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For instrumentrunstatus 
--

CREATE TABLE IF NOT EXISTS `instrumentrunstatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeInstrumentRunStatus`  varchar(10)  NULL DEFAULT NULL
 ,`instrumentRunStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For internalaccountfieldsconfiguration 
--

CREATE TABLE IF NOT EXISTS `internalaccountfieldsconfiguration_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInternalAccountFieldsConfiguration`  int(10)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`include`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`displayName`  varchar(50)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
 ,`isNumber`  char(1)  NULL DEFAULT NULL
 ,`minLength`  int(10)  NULL DEFAULT NULL
 ,`maxLength`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For invoice 
--

CREATE TABLE IF NOT EXISTS `invoice_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInvoice`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`invoiceNumber`  varchar(50)  NULL DEFAULT NULL
 ,`lastEmailDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For iscanchip 
--

CREATE TABLE IF NOT EXISTS `iscanchip_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idIScanChip`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`costPerSample`  decimal(5,2)  NULL DEFAULT NULL
 ,`samplesPerChip`  int(10)  NULL DEFAULT NULL
 ,`chipsPerKit`  int(10)  NULL DEFAULT NULL
 ,`markersPerSample`  varchar(100)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For lab 
--

CREATE TABLE IF NOT EXISTS `lab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`department`  varchar(200)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`contactName`  varchar(200)  NULL DEFAULT NULL
 ,`contactAddress`  varchar(200)  NULL DEFAULT NULL
 ,`contactCodeState`  varchar(10)  NULL DEFAULT NULL
 ,`contactZip`  varchar(50)  NULL DEFAULT NULL
 ,`contactCity`  varchar(200)  NULL DEFAULT NULL
 ,`contactPhone`  varchar(50)  NULL DEFAULT NULL
 ,`contactEmail`  varchar(200)  NULL DEFAULT NULL
 ,`isCCSGMember`  char(1)  NULL DEFAULT NULL
 ,`firstName`  varchar(200)  NULL DEFAULT NULL
 ,`lastName`  varchar(200)  NULL DEFAULT NULL
 ,`isExternalPricing`  varchar(1)  NULL DEFAULT NULL
 ,`isExternalPricingCommercial`  char(1)  NULL DEFAULT NULL
 ,`isActive`  varchar(1)  NULL DEFAULT NULL
 ,`excludeUsage`  varchar(1)  NULL DEFAULT NULL
 ,`billingContactEmail`  varchar(200)  NULL DEFAULT NULL
 ,`version`  bigint(20)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For labcollaborator 
--

CREATE TABLE IF NOT EXISTS `labcollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For label 
--

CREATE TABLE IF NOT EXISTS `label_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLabel`  int(10)  NULL DEFAULT NULL
 ,`label`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For labeledsample 
--

CREATE TABLE IF NOT EXISTS `labeledsample_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLabeledSample`  int(10)  NULL DEFAULT NULL
 ,`labelingYield`  decimal(8,2)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idLabel`  int(10)  NULL DEFAULT NULL
 ,`idLabelingProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeLabelingReactionSize`  varchar(20)  NULL DEFAULT NULL
 ,`numberOfReactions`  int(10)  NULL DEFAULT NULL
 ,`labelingDate`  datetime  NULL DEFAULT NULL
 ,`labelingFailed`  char(1)  NULL DEFAULT NULL
 ,`labelingBypassed`  char(1)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For labelingprotocol 
--

CREATE TABLE IF NOT EXISTS `labelingprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLabelingProtocol`  int(10)  NULL DEFAULT NULL
 ,`labelingProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For labelingreactionsize 
--

CREATE TABLE IF NOT EXISTS `labelingreactionsize_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeLabelingReactionSize`  varchar(20)  NULL DEFAULT NULL
 ,`labelingReactionSize`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For labmanager 
--

CREATE TABLE IF NOT EXISTS `labmanager_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For labuser 
--

CREATE TABLE IF NOT EXISTS `labuser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For numbersequencingcycles 
--

CREATE TABLE IF NOT EXISTS `numbersequencingcycles_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`numberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For numbersequencingcyclesallowed 
--

CREATE TABLE IF NOT EXISTS `numbersequencingcyclesallowed_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNumberSequencingCyclesAllowed`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For oligobarcode 
--

CREATE TABLE IF NOT EXISTS `oligobarcode_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcode`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`barcodeSequence`  varchar(20)  NULL DEFAULT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For oligobarcodescheme 
--

CREATE TABLE IF NOT EXISTS `oligobarcodescheme_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`oligoBarcodeScheme`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For oligobarcodeschemeallowed 
--

CREATE TABLE IF NOT EXISTS `oligobarcodeschemeallowed_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcodeSchemeAllowed`  int(10)  NULL DEFAULT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For organism 
--

CREATE TABLE IF NOT EXISTS `organism_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`organism`  varchar(50)  NULL DEFAULT NULL
 ,`abbreviation`  varchar(10)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`das2Name`  varchar(200)  NULL DEFAULT NULL
 ,`sortOrder`  int(10) unsigned  NULL DEFAULT NULL
 ,`binomialName`  varchar(200)  NULL DEFAULT NULL
 ,`NCBITaxID`  varchar(45)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For otheraccountfieldsconfiguration 
--

CREATE TABLE IF NOT EXISTS `otheraccountfieldsconfiguration_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOtherAccountFieldsConfiguration`  int(10)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`include`  char(1)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For plate 
--

CREATE TABLE IF NOT EXISTS `plate_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPlate`  int(10)  NULL DEFAULT NULL
 ,`idInstrumentRun`  int(10)  NULL DEFAULT NULL
 ,`codePlateType`  varchar(10)  NULL DEFAULT NULL
 ,`quadrant`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`comments`  varchar(200)  NULL DEFAULT NULL
 ,`label`  varchar(50)  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`creator`  varchar(50)  NULL DEFAULT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For platetype 
--

CREATE TABLE IF NOT EXISTS `platetype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codePlateType`  varchar(10)  NULL DEFAULT NULL
 ,`plateTypeDescription`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For platewell 
--

CREATE TABLE IF NOT EXISTS `platewell_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPlateWell`  int(10)  NULL DEFAULT NULL
 ,`row`  varchar(50)  NULL DEFAULT NULL
 ,`col`  int(10)  NULL DEFAULT NULL
 ,`ind`  int(10)  NULL DEFAULT NULL
 ,`idPlate`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`redoFlag`  char(1)  NULL DEFAULT NULL
 ,`isControl`  char(1)  NULL DEFAULT NULL
 ,`idAssay`  int(10)  NULL DEFAULT NULL
 ,`idPrimer`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For price 
--

CREATE TABLE IF NOT EXISTS `price_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(6,2)  NULL DEFAULT NULL
 ,`unitPriceExternalAcademic`  decimal(6,2)  NULL DEFAULT NULL
 ,`unitPriceExternalCommercial`  decimal(6,2)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For pricecategory 
--

CREATE TABLE IF NOT EXISTS `pricecategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`pluginClassName`  varchar(500)  NULL DEFAULT NULL
 ,`dictionaryClassNameFilter1`  varchar(500)  NULL DEFAULT NULL
 ,`dictionaryClassNameFilter2`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For pricecategorystep 
--

CREATE TABLE IF NOT EXISTS `pricecategorystep_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For pricecriteria 
--

CREATE TABLE IF NOT EXISTS `pricecriteria_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCriteria`  int(10)  NULL DEFAULT NULL
 ,`filter1`  varchar(10)  NULL DEFAULT NULL
 ,`filter2`  varchar(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For pricesheet 
--

CREATE TABLE IF NOT EXISTS `pricesheet_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For pricesheetpricecategory 
--

CREATE TABLE IF NOT EXISTS `pricesheetpricecategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For pricesheetrequestcategory 
--

CREATE TABLE IF NOT EXISTS `pricesheetrequestcategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For primer 
--

CREATE TABLE IF NOT EXISTS `primer_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPrimer`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`sequence`  varchar(2000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For project 
--

CREATE TABLE IF NOT EXISTS `project_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(4000)  NULL DEFAULT NULL
 ,`publicDateForAppUsers`  datetime  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For property 
--

CREATE TABLE IF NOT EXISTS `property_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
 ,`forSample`  char(1)  NULL DEFAULT NULL
 ,`forAnalysis`  char(1)  NULL DEFAULT NULL
 ,`forDataTrack`  char(1)  NULL DEFAULT NULL
 ,`codePropertyType`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertyanalysistype 
--

CREATE TABLE IF NOT EXISTS `propertyanalysistype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertydictionary 
--

CREATE TABLE IF NOT EXISTS `propertydictionary_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyDictionary`  int(10)  NULL DEFAULT NULL
 ,`propertyName`  varchar(200)  NULL DEFAULT NULL
 ,`propertyValue`  varchar(2000)  NULL DEFAULT NULL
 ,`propertyDescription`  varchar(2000)  NULL DEFAULT NULL
 ,`forServerOnly`  char(1)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertyentry 
--

CREATE TABLE IF NOT EXISTS `propertyentry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(200)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertyentryoption 
--

CREATE TABLE IF NOT EXISTS `propertyentryoption_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertyentryvalue 
--

CREATE TABLE IF NOT EXISTS `propertyentryvalue_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntryValue`  int(10) unsigned  NULL DEFAULT NULL
 ,`value`  varchar(200)  NULL DEFAULT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertyoption 
--

CREATE TABLE IF NOT EXISTS `propertyoption_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
 ,`value`  varchar(200)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertyorganism 
--

CREATE TABLE IF NOT EXISTS `propertyorganism_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertyplatform 
--

CREATE TABLE IF NOT EXISTS `propertyplatform_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertyplatformapplication 
--

CREATE TABLE IF NOT EXISTS `propertyplatformapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPlatformApplication`  int(10)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For propertytype 
--

CREATE TABLE IF NOT EXISTS `propertytype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codePropertyType`  varchar(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For protocoltype 
--

CREATE TABLE IF NOT EXISTS `protocoltype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeProtocolType`  varchar(10)  NULL DEFAULT NULL
 ,`protocolType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For qualitycontrolstep 
--

CREATE TABLE IF NOT EXISTS `qualitycontrolstep_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeQualityControlStep`  varchar(10)  NULL DEFAULT NULL
 ,`qualityControlStep`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For qualitycontrolstepentry 
--

CREATE TABLE IF NOT EXISTS `qualitycontrolstepentry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idQualityControlStepEntry`  int(10)  NULL DEFAULT NULL
 ,`codeQualityControlStep`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For reactiontype 
--

CREATE TABLE IF NOT EXISTS `reactiontype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`reactionType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For request 
--

CREATE TABLE IF NOT EXISTS `request_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(50)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeProtocolType`  varchar(10)  NULL DEFAULT NULL
 ,`protocolNumber`  varchar(100)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`idSampleTypeDefault`  int(10)  NULL DEFAULT NULL
 ,`idOrganismSampleDefault`  int(10)  NULL DEFAULT NULL
 ,`idSampleSourceDefault`  int(10)  NULL DEFAULT NULL
 ,`idSamplePrepMethodDefault`  int(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`notes`  varchar(2000)  NULL DEFAULT NULL
 ,`completedDate`  datetime  NULL DEFAULT NULL
 ,`isArrayINFORequest`  char(1)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`lastModifyDate`  datetime  NULL DEFAULT NULL
 ,`isExternal`  char(1)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`privacyExpirationDate`  datetime  NULL DEFAULT NULL
 ,`description`  varchar(5000)  NULL DEFAULT NULL
 ,`corePrepInstructions`  varchar(5000)  NULL DEFAULT NULL
 ,`analysisInstructions`  varchar(5000)  NULL DEFAULT NULL
 ,`captureLibDesignId`  varchar(200)  NULL DEFAULT NULL
 ,`avgInsertSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeTo`  int(10)  NULL DEFAULT NULL
 ,`idSampleDropOffLocation`  int(10)  NULL DEFAULT NULL
 ,`codeRequestStatus`  varchar(10)  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
 ,`numberIScanChips`  int(10)  NULL DEFAULT NULL
 ,`idIScanChip`  int(10)  NULL DEFAULT NULL
 ,`coreToExtractDNA`  char(1)  NULL DEFAULT NULL
 ,`applicationNotes`  varchar(2000)  NULL DEFAULT NULL
 ,`processingDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For requestcategory 
--

CREATE TABLE IF NOT EXISTS `requestcategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`requestCategory`  varchar(50)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`numberOfChannels`  int(10)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`icon`  varchar(200)  NULL DEFAULT NULL
 ,`type`  varchar(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`isSampleBarcodingOptional`  char(1)  NULL DEFAULT NULL
 ,`isInternal`  char(1)  NULL DEFAULT NULL
 ,`isExternal`  char(1)  NULL DEFAULT NULL
 ,`refrainFromAutoDelete`  char(1)  NULL DEFAULT NULL
 ,`isClinicalResearch`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For requestcategoryapplication 
--

CREATE TABLE IF NOT EXISTS `requestcategoryapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idLabelingProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idHybProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idScanProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idFeatureExtractionProtocolDefault`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For requestcategorytype 
--

CREATE TABLE IF NOT EXISTS `requestcategorytype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestCategoryType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(50)  NULL DEFAULT NULL
 ,`defaultIcon`  varchar(100)  NULL DEFAULT NULL
 ,`isIllumina`  char(1)  NULL DEFAULT NULL
 ,`hasChannels`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For requestcollaborator 
--

CREATE TABLE IF NOT EXISTS `requestcollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`canUploadData`  char(1)  NULL DEFAULT NULL
 ,`canUpdate`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For requesthybridization 
--

CREATE TABLE IF NOT EXISTS `requesthybridization_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For requestseqlibtreatment 
--

CREATE TABLE IF NOT EXISTS `requestseqlibtreatment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For requeststatus 
--

CREATE TABLE IF NOT EXISTS `requeststatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestStatus`  varchar(10)  NULL DEFAULT NULL
 ,`requestStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For requesttotopic 
--

CREATE TABLE IF NOT EXISTS `requesttotopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sample 
--

CREATE TABLE IF NOT EXISTS `sample_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`concentration`  decimal(8,3)  NULL DEFAULT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`otherOrganism`  varchar(100)  NULL DEFAULT NULL
 ,`idSampleSource`  int(10)  NULL DEFAULT NULL
 ,`idSamplePrepMethod`  int(10)  NULL DEFAULT NULL
 ,`otherSamplePrepMethod`  varchar(300)  NULL DEFAULT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`idOligoBarcode`  int(10)  NULL DEFAULT NULL
 ,`qualDate`  datetime  NULL DEFAULT NULL
 ,`qualFailed`  char(1)  NULL DEFAULT NULL
 ,`qualBypassed`  char(1)  NULL DEFAULT NULL
 ,`qual260nmTo280nmRatio`  decimal(3,2)  NULL DEFAULT NULL
 ,`qual260nmTo230nmRatio`  decimal(3,2)  NULL DEFAULT NULL
 ,`qualCalcConcentration`  decimal(8,2)  NULL DEFAULT NULL
 ,`qual28sTo18sRibosomalRatio`  decimal(2,1)  NULL DEFAULT NULL
 ,`qualRINNumber`  varchar(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`fragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`fragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepByCore`  char(1)  NULL DEFAULT NULL
 ,`seqPrepDate`  datetime  NULL DEFAULT NULL
 ,`seqPrepFailed`  char(1)  NULL DEFAULT NULL
 ,`seqPrepBypassed`  char(1)  NULL DEFAULT NULL
 ,`qualFragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`qualFragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepLibConcentration`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepQualCodeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`seqPrepGelFragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`seqPrepGelFragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepStockLibVol`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepStockEBVol`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepStockDate`  datetime  NULL DEFAULT NULL
 ,`seqPrepStockFailed`  char(1)  NULL DEFAULT NULL
 ,`seqPrepStockBypassed`  char(1)  NULL DEFAULT NULL
 ,`prepInstructions`  varchar(2000)  NULL DEFAULT NULL
 ,`ccNumber`  varchar(20)  NULL DEFAULT NULL
 ,`multiplexGroupNumber`  int(10)  NULL DEFAULT NULL
 ,`barcodeSequence`  varchar(20)  NULL DEFAULT NULL
 ,`isControl`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sampledropofflocation 
--

CREATE TABLE IF NOT EXISTS `sampledropofflocation_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleDropOffLocation`  int(10)  NULL DEFAULT NULL
 ,`sampleDropOffLocation`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sampleprepmethod 
--

CREATE TABLE IF NOT EXISTS `sampleprepmethod_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSamplePrepMethod`  int(10)  NULL DEFAULT NULL
 ,`samplePrepMethod`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For samplesource 
--

CREATE TABLE IF NOT EXISTS `samplesource_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleSource`  int(10)  NULL DEFAULT NULL
 ,`sampleSource`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sampletype 
--

CREATE TABLE IF NOT EXISTS `sampletype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`sampleType`  varchar(50)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sampletypeapplication 
--

CREATE TABLE IF NOT EXISTS `sampletypeapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleTypeApplication`  int(10)  NULL DEFAULT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idLabelingProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idHybProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idScanProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idFeatureExtractionProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sampletyperequestcategory 
--

CREATE TABLE IF NOT EXISTS `sampletyperequestcategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleTypeRequestCategory`  int(10)  NULL DEFAULT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For scanprotocol 
--

CREATE TABLE IF NOT EXISTS `scanprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idScanProtocol`  int(10)  NULL DEFAULT NULL
 ,`scanProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sealtype 
--

CREATE TABLE IF NOT EXISTS `sealtype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
 ,`sealType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For segment 
--

CREATE TABLE IF NOT EXISTS `segment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSegment`  int(10)  NULL DEFAULT NULL
 ,`length`  int(10) unsigned  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10) unsigned  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For seqlibprotocol 
--

CREATE TABLE IF NOT EXISTS `seqlibprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`seqLibProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`adapterSequenceRead1`  varchar(500)  NULL DEFAULT NULL
 ,`adapterSequenceRead2`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For seqlibprotocolapplication 
--

CREATE TABLE IF NOT EXISTS `seqlibprotocolapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For seqlibtreatment 
--

CREATE TABLE IF NOT EXISTS `seqlibtreatment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
 ,`seqLibTreatment`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For seqruntype 
--

CREATE TABLE IF NOT EXISTS `seqruntype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`seqRunType`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sequencelane 
--

CREATE TABLE IF NOT EXISTS `sequencelane_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`analysisInstructions`  varchar(2000)  NULL DEFAULT NULL
 ,`idGenomeBuildAlignTo`  int(10)  NULL DEFAULT NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`readCount`  int(10)  NULL DEFAULT NULL
 ,`pipelineVersion`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sequencingcontrol 
--

CREATE TABLE IF NOT EXISTS `sequencingcontrol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSequencingControl`  int(10)  NULL DEFAULT NULL
 ,`sequencingControl`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For sequencingplatform 
--

CREATE TABLE IF NOT EXISTS `sequencingplatform_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSequencingPlatform`  varchar(10)  NULL DEFAULT NULL
 ,`sequencingPlatform`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For slide 
--

CREATE TABLE IF NOT EXISTS `slide_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlide`  int(10)  NULL DEFAULT NULL
 ,`barcode`  varchar(100)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`slideName`  varchar(200)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For slidedesign 
--

CREATE TABLE IF NOT EXISTS `slidedesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`slideDesignProtocolName`  varchar(100)  NULL DEFAULT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`accessionNumberArrayExpress`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For slideproduct 
--

CREATE TABLE IF NOT EXISTS `slideproduct_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`arraysPerSlide`  int(10)  NULL DEFAULT NULL
 ,`slidesInSet`  int(10)  NULL DEFAULT NULL
 ,`isSlideSet`  char(1)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idBillingSlideProductClass`  int(10)  NULL DEFAULT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For slideproductapplication 
--

CREATE TABLE IF NOT EXISTS `slideproductapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For slidesource 
--

CREATE TABLE IF NOT EXISTS `slidesource_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSlideSource`  varchar(10)  NULL DEFAULT NULL
 ,`slideSource`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For state 
--

CREATE TABLE IF NOT EXISTS `state_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeState`  varchar(10)  NULL DEFAULT NULL
 ,`state`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For step 
--

CREATE TABLE IF NOT EXISTS `step_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
 ,`step`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For submissioninstruction 
--

CREATE TABLE IF NOT EXISTS `submissioninstruction_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSubmissionInstruction`  int(10)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`url`  varchar(2000)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For topic 
--

CREATE TABLE IF NOT EXISTS `topic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`idParentTopic`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For treatmententry 
--

CREATE TABLE IF NOT EXISTS `treatmententry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTreatmentEntry`  int(10)  NULL DEFAULT NULL
 ,`treatment`  varchar(2000)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For unloaddatatrack 
--

CREATE TABLE IF NOT EXISTS `unloaddatatrack_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idUnloadDataTrack`  int(10)  NULL DEFAULT NULL
 ,`typeName`  varchar(2000)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For userpermissionkind 
--

CREATE TABLE IF NOT EXISTS `userpermissionkind_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeUserPermissionKind`  varchar(10)  NULL DEFAULT NULL
 ,`userPermissionKind`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For vendor 
--

CREATE TABLE IF NOT EXISTS `vendor_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`vendorName`  varchar(100)  NULL DEFAULT NULL
 ,`description`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For visibility 
--

CREATE TABLE IF NOT EXISTS `visibility_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`visibility`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For workitem 
--

CREATE TABLE IF NOT EXISTS `workitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idWorkItem`  int(10)  NULL DEFAULT NULL
 ,`codeStepNext`  varchar(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSample`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`status`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$
