use gnomex;

-- Add core facility to sample type
alter table SampleType add column idCoreFacility INT(10) NULL;
call ExecuteIfTableExists('gnomex','SampleType_Audit','alter table SampleType_Audit add column idCoreFacility INT(10) NULL;');
update SampleType set idCoreFacility=1;
alter table SampleType add 
  CONSTRAINT `FK_SampleType_CoreFacility` FOREIGN KEY `FK_SampleType_CoreFacility` (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
