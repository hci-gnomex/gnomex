use gnomex;

SET foreign_key_checks = 0;

-- Add new tables: Chromatogram, CoreFacility, InstrumentRun, Plate, PlateWell

DROP TABLE IF EXISTS `gnomex`.`Chromatogram`;
CREATE TABLE `gnomex`.`Chromatogram` (
  `idChromatogram` int(10) NOT NULL AUTO_INCREMENT,
  `idPlateWell` int(10) NULL,
  `idRequest` int(10) NULL,
  `fileName` varchar(2000) NULL,
  `displayName` varchar(200) NULL,
  `readLength` int(10) NULL,
  `trimmedLength` int NULL,
  `q20` int(10) NULL,
  `q40` int (10) NULL,
  `aSignalStrength` int(10) NULL,
  `cSignalStrength` int(10) NULL,
  `gSignalStrength` int(10) NULL,
  `tSignalStrength` int(10) NULL,
  PRIMARY KEY (`idChromatogram`),
  CONSTRAINT `FK_Chromatogram_PlateWell` FOREIGN KEY  (`idPlateWell`)
    REFERENCES `gnomex`.`PlateWell` (`idPlateWell`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Chromatogram_Request` FOREIGN KEY  (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`CoreFacility`;
CREATE TABLE `gnomex`.`CoreFacility` (
  `idCoreFacility` INT(10) NOT NULL AUTO_INCREMENT,
  `facilityName` varchar(200) NULL,
  PRIMARY KEY (`idCoreFacility`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`InstrumentRun`;
CREATE TABLE `gnomex`.`InstrumentRun` (
  `idInstrumentRun` INT(10) NOT NULL AUTO_INCREMENT,
  `runDate` DATETIME NULL,
  PRIMARY KEY (`idInstrumentRun`)
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
  PRIMARY KEY (`idPlateWell`),
  CONSTRAINT `FK_PlateWell_Plate` FOREIGN KEY `FK_PlateWell_Plate` (`idPlate`)
    REFERENCES `gnomex`.`Plate` (`idPlate`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PlateWell_Sample` FOREIGN KEY `FK_PlateWell_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PlateWell_Request` FOREIGN KEY `FK_PlateWell_Request` (`idRequest`)
    REFERENCES `gnomex`.`Request` (`idRequest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `gnomex`.`Plate`;
CREATE TABLE `gnomex`.`Plate` (
  `idPlate` INT(10) NOT NULL AUTO_INCREMENT,
  `idInstrumentRun` INT(10) NULL,
  PRIMARY KEY (`idPlate`),
  CONSTRAINT `FK_Plate_InstrumentRun` FOREIGN KEY `FK_Plate_InstrumentRun` (`idInstrumentRun`)
    REFERENCES `gnomex`.`InstrumentRun` (`idInstrumentRun`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;



-- Add idCoreFacility to Analysis,  BillingItem, Request, RequestCategory, WorkItem
alter table gnomex.Analysis add idCoreFacility int(10) null;
alter table gnomex.BillingItem add idCoreFacility int(10) null;
alter table gnomex.Request add idCoreFacility int(10) null;
alter table gnomex.RequestCategory add idCoreFacility int(10) null;
alter table gnomex.WorkItem add idCoreFacility int(10) null;

ALTER TABLE gnomex.Analysis  ADD  CONSTRAINT FK_Analysis_CoreFacility FOREIGN KEY  (idCoreFacility)
REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
ALTER TABLE gnomex.BillingItem  ADD  CONSTRAINT FK_BillingItem_CoreFacility FOREIGN KEY  (idCoreFacility)
REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
ALTER TABLE gnomex.Request  ADD  CONSTRAINT FK_Request_CoreFacility FOREIGN KEY  (idCoreFacility)
REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

ALTER TABLE gnomex.RequestCategory  ADD  CONSTRAINT FK_RequestCategory_CoreFacility FOREIGN KEY  (idCoreFacility)
REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
ALTER TABLE gnomex.WorkItem  ADD  CONSTRAINT FK_Analysis_WorkItem FOREIGN KEY  (idCoreFacility)
REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;



-- Rename TotalPrice to InvoicePrice.
alter table BillingItem change totalPrice invoicePrice decimal(8,2) not null;
alter table BillingItem add totalPrice decimal(8,2) not null;

-- Add splitType
alter table BillingItem add column  `splitType` char(1) NULL;

-- Initialize split type
update BillingItem set splitType='%' where splitType is null;
update BillingItem set totalPrice=invoicePrice where invoicePrice != totalPrice or totalPrice is Null;


-- Add properties for sequence alignments feature
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('sequence_alignment_server_url', '', 'Location of web service for submitting files for sequence alignment.', 'N');
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('sequence_alignment_supported', 'N', 'True if sequence alignment feature supported.', 'N');

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


-- Add properties for datatrack feature
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('datatrack_supported', 'N', 'Indicates if datatrack feature is utilized in GNomEx installation', 'Y');


SET foreign_key_checks = 1;