
-- Add column idAnalysis to PropertyEntry
alter table PropertyEntry add column idAnalysis int(10) null;
alter table PropertyEntry add   
CONSTRAINT `FK_PropertyEntry_Analysis` FOREIGN KEY `FK_PropertyEntry_Analysis` (`idAnalysis`)
    REFERENCES DataTrack (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
