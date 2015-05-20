use gnomex;

-- Remove idGenomeBuild from Analysis
alter table gnomex.Analysis drop foreign key FK_Analysis_GenomeBuild;
alter table gnomex.Analysis drop column idGenomeBuild;

-- Rename Property to PropertyDictionary
rename table Property to PropertyDictionary;
alter table PropertyDictionary change idProperty idPropertyDictionary int(10)auto_increment not null;


-- Rename CharacteristicType to PropertyType
rename table CharacteristicType to PropertyType;
alter table SampleCharacteristic drop foreign key `FK_SampleCharacteristic_CharacteristicType`;
alter table PropertyType change codeCharacteristicType codePropertyType varchar(10) not null;

-- Rename SampleCharacteristic to Property
alter table SampleCharacteristicPlatform drop foreign key FK_SampleCharacteristicPlantform_SampleCharacteristic;
alter table SampleCharacteristicOrganism drop foreign key FK_SampleCharacteristicOrganism_SampleCharacteristic;
alter table SampleCharacteristicEntry drop foreign key FK_SampleCharacteristicEntry_SampleCharacteristic;
alter table SampleCharacteristicOption drop foreign key FK_SampleCharacteristicOption_SampleCharacteristic;
rename table SampleCharacteristic to Property;
alter table Property change idSampleCharacteristic idProperty int(10) auto_increment NOT NULL;
alter table Property change sampleCharacteristic name varchar(50) not null;
alter table Property change codeCharacteristicType codePropertyType varchar(10) not null;
alter table Property drop foreign key `FK_SampleCharacteristic_AppUser`;
alter table Property add
  CONSTRAINT `FK_Property_AppUser` FOREIGN KEY `FK_Property_AppUser` (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table Property add 
  CONSTRAINT FK_Property_PropertyType FOREIGN KEY FK_Property_PropertyType (codePropertyType)
    REFERENCES gnomex.PropertyType (codePropertyType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
    
-- Rename SampleCharacteristicPlatform to PropertyPlatform
rename table SampleCharacteristicPlatform to PropertyPlatform;
alter table PropertyPlatform change idSampleCharacteristic idProperty int(10) not null;
alter table PropertyPlatform drop foreign key FK_SampleCharacteristicPlatform_RequestCategory;
alter table PropertyPlatform add
CONSTRAINT FK_PropertyPlatform_Property FOREIGN KEY FK_PropertyPlatform_Property (idProperty)
    REFERENCES gnomex.Property (idProperty)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table PropertyPlatform add    
    CONSTRAINT FK_PropertyPlatform_RequestCategory FOREIGN KEY FK_PropertyPlatform_RequestCategory (codeRequestCategory)
    REFERENCES gnomex.RequestCategory (codeRequestCategory)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- Rename SampleCharacteristicOrganism to PropertyOrganism;
rename table SampleCharacteristicOrganism to PropertyOrganism;
alter table PropertyOrganism change idSampleCharacteristic idProperty int(10) not null;
alter table PropertyOrganism drop foreign key FK_SampleCharacteristicOrganism_Organism;
alter table PropertyOrganism add
    CONSTRAINT FK_PropertyOrganism_Property FOREIGN KEY FK_PropertyOrganism_Property (idProperty)
    REFERENCES gnomex.Property (idProperty)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table PropertyOrganism add
    CONSTRAINT FK_PropertyOrganism_Organism FOREIGN KEY FK_PropertyOrganism_Organism (idOrganism)
    REFERENCES gnomex.Organism (idOrganism)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
      

-- Rename SampleCharacteristicOption to PropertyOption
rename table SampleCharacteristicOption to PropertyOption;
alter table SampleCharacteristicEntryOption drop foreign key FK_SampleCharacteristicEntryOption_SampleCharacteristicOption;
alter table PropertyOption change idSampleCharacteristicOption idPropertyOption int(10) auto_increment not null;
alter table PropertyOption change idSampleCharacteristic idProperty int(10) not null;
alter table PropertyOption add
   CONSTRAINT `FK_PropertyOption_Property` 
   FOREIGN KEY (`idProperty`) 
   REFERENCES `Property` (`idProperty`) 
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;
   
   
   

-- Rename SampleCharacteristicEntry to PropertyEntry
rename table SampleCharacteristicEntry to PropertyEntry;
alter table PropertyEntry drop foreign key FK_SampleCharacteristicEntry_Sample;
alter table PropertyEntry drop key FK_SampleCharacteristicEntry_Sample;
alter table PropertyEntry drop key FK_SampleCharacteristicEntry_SampleCharacteristic;
alter table SampleCharacteristicEntryOption drop foreign key FK_SampleCharacteristicEntryOption_SampleCharacteristicEntry;
alter table SampleCharacteristicEntryValue drop foreign key FK_SampleCharacteristicEntryValue_SampleCharacteristicEntry;
alter table PropertyEntry change idSampleCharacteristicEntry idPropertyEntry int(10) auto_increment not null;
alter table PropertyEntry change idSampleCharacteristic idProperty int(10) not null;
alter table PropertyEntry add 
  CONSTRAINT `FK_PropertyEntry_Sample` FOREIGN KEY `FK_PropertyEntry_Sample` (`idSample`)
    REFERENCES `gnomex`.`Sample` (`idSample`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table PropertyEntry add
  CONSTRAINT `FK_PropertyEntry_Property` FOREIGN KEY `FK_PropertyEntry_Property` (`idProperty`)
    REFERENCES `gnomex`.`Property` (`idProperty`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
          
   
-- Rename table SampleCharacteristicEntryOption
rename table SampleCharacteristicEntryOption to PropertyEntryOption;
alter table PropertyEntryOption change idSampleCharacteristicEntry idPropertyEntry int(10) not null;
alter table PropertyEntryOption change idSampleCharacteristicOption idPropertyOption int(10) unsigned not null;
alter table PropertyEntryOption add
   CONSTRAINT FK_PropertyEntryOption_PropertyEntry FOREIGN KEY FK_PropertyEntryOption_PropertyEntry (idPropertyEntry)
    REFERENCES gnomex.PropertyEntry (idPropertyEntry)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table PropertyEntryOption add
   CONSTRAINT FK_PropertyEntryOption_PropertyOption FOREIGN KEY FK_PropertyEntryOption_PropertyOption (idPropertyOption)
    REFERENCES gnomex.PropertyOption (idPropertyOption)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- Rename table SampleCharacteristicEntryValue to PropertyEntryValue
rename table SampleCharacteristicEntryValue to PropertyEntryValue;
alter table PropertyEntryValue drop key FK_SampleCharacteristicEntryValue_SampleCharacteristicEntry;
alter table PropertyEntryValue change idSampleCharacteristicEntryValue idPropertyEntryValue int(10) auto_increment not null;
alter table PropertyEntryValue change idSampleCharacteristicEntry idPropertyEntry int(10) not null;
alter table PropertyEntryValue add
 CONSTRAINT FK_PropertyEntryValue_PropertyEntry FOREIGN KEY FK_PropertyEntryValue_PropertyEntry (idPropertyEntry)
    REFERENCES gnomex.PropertyEntry (idPropertyEntry)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

    
-- Add columns to Organism
alter table Organism add column  `das2Name` varchar(200) NULL;
alter table Organism add column  `sortOrder` int(10)   NULL;
alter table Organism add column  `binomialName` varchar(200) NULL;
alter table Organism add column  `NCBITaxID` varchar(45)  NULL;

-- Add columns to GenomeBuild
alter table GenomeBuild add column  `das2Name` varchar(200) NULL;
alter table GenomeBuild add column  `buildDate` datetime  NULL;
alter table GenomeBuild add column  `coordVersion` varchar(50)  NULL;
alter table GenomeBuild add column  `coordURI` varchar(2000)  NULL;
alter table GenomeBuild add column  `coordSource` varchar(50)  NULL;
alter table GenomeBuild add column  `coordTestRange` varchar(100)  NULL;
alter table GenomeBuild add column  `coordAuthority` varchar(50)  NULL;
alter table GenomeBuild add column  `ucscName` varchar(100)  NULL;
alter table GenomeBuild add column  `dataPath` varchar(500)  NULL;

-- Add columns for AppUser
alter table AppUser add column ucscUrl  varchar(250) null;


-- Add new GenoPub tables (Segment, GenomeVersion, DataTrack, etc.)
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
-- Table structure for table dbo.gpSegment 
--
--
-- Table structure for table `Segment`
--

DROP TABLE IF EXISTS `Segment`;
CREATE TABLE `Segment` (
  `idSegment` int(10) NOT NULL auto_increment,
  `length` int(10)  NOT NULL,
  `name` varchar(100) NOT NULL,
  `idGenomeBuild` int(10)  NOT NULL,
  `sortOrder` int(10)  NOT NULL,
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
-- Table structure for table `DataTrackToFolder`
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

  

-- Add column idDataTrack to PropertyEntry
alter table PropertyEntry add column idDataTrack int(10) null;
alter table PropertyEntry add   
CONSTRAINT `FK_PropertyEntry_DataTrack` FOREIGN KEY `FK_PropertyEntry_DataTrack` (`idDataTrack`)
    REFERENCES DataTrack (`idDataTrack`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
    
-- Add columns to Property
alter table Property add column forSample char(1) default 'Y';
alter table Property add column forAnalysis char(1) default 'N';
alter table Property add column forDataTrack char(1) default 'N';
