-- Change codeSampleCharacteristic to idSampleCharacteristic

SET FOREIGN_KEY_CHECKS = 0;
alter table gnomex.SampleCharacteristicEntry drop foreign key FK_SampleCharacteristicEntry_SampleCharacteristic; 
alter table gnomex.SampleCharacteristic drop primary key;

alter table gnomex.SampleCharacteristic add column idSampleCharacteristic INT(10) primary KEY AUTO_INCREMENT NOT NULL;
alter table gnomex.SampleCharacteristicEntry add column idSampleCharacteristic int(10);

alter table gnomex.SampleCharacteristicEntry add CONSTRAINT FK_SampleCharacteristicEntry_SampleCharacteristic 
FOREIGN KEY FK_SampleCharacteristicEntry_SampleCharacteristic (idSampleCharacteristic)
    REFERENCES gnomex.SampleCharacteristic (idSampleCharacteristic)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    alter table gnomex.SampleCharacteristicEntry add CONSTRAINT FK_SampleCharacteristicEntry_SampleCharacteristic 
    
update gnomex.SampleCharacteristicEntry sce, SampleCharacteristicEntry sc
set sce.idSampleCharacteristic = sc.idSampleCharacteristic
where sc.codeSampleCharacteristic = sce.codeSampleCharacteristic;

alter table gnomex.SampleCharacteristic drop column codeSampleCharacteristic;
alter table gnomex.SampleCharacteristicEntry drop column codeSampleCharacteristic;

SET FOREIGN_KEY_CHECKS = 1;


-- Add table CharacteristicType
CREATE TABLE gnomex.CharacteristicType ( 
    codeCharacteristicType	VARCHAR(10) NOT NULL,
    name 	                varchar(200) NOT NULL,
    isActive     	        char(1) NULL,
    PRIMARY KEY (codeCharacteristicType)
) ENGINE = INNODB;

-- Add columns to SampleCharacteristic
ALTER TABLE gnomex.SampleCharacteristic add column codeCharacteristicType VARCHAR(10) null;
ALTER TABLE gnomex.SampleCharacteristic  add CONSTRAINT FK_SampleCharacteristic_CharacteristicType FOREIGN KEY FK_SampleCharacteristic_CharacteristicType (codeCharacteristicType)
    REFERENCES gnomex.CharacteristicType (codeCharacteristicType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    



-- Add table SampleCharacteristicOrganism
CREATE TABLE gnomex.SampleCharacteristicOrganism ( 
     idSampleCharacteristic	INT(10),
     idOrganism           INT(10),
     PRIMARY KEY (idSampleCharacteristic, idOrganism),
    CONSTRAINT FK_SampleCharacteristicOrganism_SampleCharacteristic FOREIGN KEY FK_SampleCharacteristicOrganism_SampleCharacteristic (idSampleCharacteristic)
    REFERENCES gnomex.SampleCharacteristic (idSampleCharacteristic)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_SampleCharacteristicOrganism_Organism FOREIGN KEY FK_SampleCharacteristicOrganism_Organism (idOrganism)
    REFERENCES gnomex.Organism (idOrganism)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

-- Add table SampleCharacteristicOption
CREATE  TABLE gnomex.SampleCharacteristicOption (
  idSampleCharacteristicOption INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  value VARCHAR(200)  NULL,
  idSampleCharacteristic int(10)   NULL,
  sortOrder INT(10) NULL,
  isActive     	        char(1) NULL,
  PRIMARY KEY (idSampleCharacteristicOption),
  KEY `FK_SampleCharacteristicOption_SampleCharacteristic` (`idSampleCharacteristic`),
  CONSTRAINT `FK_SampleCharacteristicOption_SampleCharacteristic` FOREIGN KEY (`idSampleCharacteristic`) REFERENCES `SampleCharacteristic` (`idSampleCharacteristic`)
) ENGINE=InnoDB;



-- Add new Table SampleCharacteristicEntryOption
DROP TABLE IF EXISTS SampleCharacteristicEntryOption;
CREATE TABLE gnomex.SampleCharacteristicEntryOption (
  idSampleCharacteristicEntry INT(10)  NOT NULL,
  idSampleCharacteristicOption INT(10) unsigned  NOT NULL,
  PRIMARY KEY (idSampleCharacteristicEntry, idSampleCharacteristicOption) ,
  CONSTRAINT FK_SampleCharacteristicEntryOption_SampleCharacteristicEntry FOREIGN KEY FK_SampleCharacteristicEntryOption_SampleCharacteristicEntry (idSampleCharacteristicEntry)
    REFERENCES gnomex.SampleCharacteristicEntry (idSampleCharacteristicEntry)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION, 
   CONSTRAINT FK_SampleCharacteristicEntryOption_SampleCharacteristicOption FOREIGN KEY FK_SampleCharacteristicEntryOption_SampleCharacteristicOption (idSampleCharacteristicOption)
    REFERENCES gnomex.SampleCharacteristicOption (idSampleCharacteristicOption)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)ENGINE=InnoDB;

-- Add new Table SampleCharacteristicEntryValue
DROP TABLE IF EXISTS SampleCharacteristicEntryValue;
CREATE TABLE gnomex.SampleCharacteristicEntryValue (
  idSampleCharacteristicEntryValue INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  value VARCHAR(200) NULL,
  idSampleCharacteristicEntry INT(10)  NOT NULL,
  PRIMARY KEY (idSampleCharacteristicEntryValue),
  CONSTRAINT FK_SampleCharacteristicEntryValue_SampleCharacteristicEntry FOREIGN KEY FK_SampleCharacteristicEntryValue_SampleCharacteristicEntry (idSampleCharacteristicEntry)
    REFERENCES gnomex.SampleCharacteristicEntry (idSampleCharacteristicEntry)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)ENGINE=InnoDB;


-- Insert entries into CharacteristicType
INSERT INTO gnomex.CharacteristicType (codeCharacteristicType, name) values ('TEXT',        'Text');
INSERT INTO gnomex.CharacteristicType (codeCharacteristicType, name) values ('URL',         'URL');
INSERT INTO gnomex.CharacteristicType (codeCharacteristicType, name) values ('CHECK',       'Checkbox');
INSERT INTO gnomex.CharacteristicType (codeCharacteristicType, name) values ('OPTION',      'Option (Single selection)');
INSERT INTO gnomex.CharacteristicType (codeCharacteristicType, name) values ('MOPTION',     'Option (Multiple selection)');

-- Update all existing SampleCharacteristics to datatype of TEXT
update gnomex.SampleCharacteristic set codeCharacteristicType = 'TEXT';
