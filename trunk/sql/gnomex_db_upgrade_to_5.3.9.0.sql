
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
