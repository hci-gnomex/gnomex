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

-- Add table PropertyAppUser
DROP TABLE IF EXISTS `gnomex`.`PropertyAppUser`;
CREATE TABLE gnomex.PropertyAppUser ( 
     idProperty	int(10),
     idAppUser  INT(10),
    PRIMARY KEY (idProperty, idAppUser),
    CONSTRAINT FK_PropertyAppUser_Property FOREIGN KEY FK_PropertyAppUser_Property (idProperty)
    REFERENCES gnomex.Property (idProperty)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_PropertyAppUser_AppUser FOREIGN KEY FK_PropertyAppUser_AppUser (idAppUser)
    REFERENCES gnomex.AppUser (idAppUser)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

-- Increase size of PropertyDictionary value
alter table PropertyDictionary MODIFY propertyValue varchar(20000) null;
call ExecuteIfTableExists('gnomex','PropertyDictionary_Audit','alter table PropertyDictionary_Audit MODIFY propertyValue varchar(20000) null');
