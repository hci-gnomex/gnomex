use gnomex;

-- Add column idAnalysis to PropertyEntry
alter table PropertyEntry add column idAnalysis int(10) null;
alter table PropertyEntry add   
CONSTRAINT `FK_PropertyEntry_Analysis` FOREIGN KEY `FK_PropertyEntry_Analysis` (`idAnalysis`)
    REFERENCES Analysis (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

-- Populate new property in anticipation of Data Track being in search
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('lucene_datatrack_index_directory','/home/gnomex/luceneIndex/DataTrack','The file directory for storing lucene index files on data track data.', 'Y');

-- Add average insert size range columns to Request Table
alter table Request add column avgInsertSizeFrom int(10) NULL;
alter table Request add column avgInsertSizeTo int(10) NULL;

CREATE TABLE `UnloadDataTrack` (
  `idUnloadDataTrack` int(10)  NOT NULL auto_increment,
  `typeName` varchar(2000) NOT NULL,
  `idAppUser` int(10)  default NULL,
  `idGenomeBuild` int(10)  NOT NULL,
  PRIMARY KEY  (`idUnloadDataTrack`),
  KEY `FK_UnloadDataTrack_AppUser` (`idAppUser`),
  KEY `FK_UnloadDataTrack_GenomeBuild` (`idGenomeBuild`)
) ENGINE=InnoDB;



-- allow nulls for idPropertyEntry on PropertyEntryValue
alter table gnomex.PropertyEntryValue change column idPropertyEntry idPropertyEntry INT(10) NULL;
