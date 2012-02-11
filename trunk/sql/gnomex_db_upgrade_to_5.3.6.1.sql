use gnomex;

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