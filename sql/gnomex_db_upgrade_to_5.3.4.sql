use gnomex;

-- Remove idGenomeBuild from Analysis
alter table gnomex.Analysis drop foreign key FK_Analysis_GenomeBuild;
alter table gnomex.Analysis drop column idGenomeBuild ENGINE = InnoDB;

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
alter table PropertyEntryOption change idSampleCharacteristicOption idPropertyOption int(10) not null;
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
alter table PropertyEntryValue add
 CONSTRAINT FK_PropertyEntryValue_PropertyEntry FOREIGN KEY FK_PropertyEntryValue_PropertyEntry (idPropertyEntry)
    REFERENCES gnomex.PropertyEntry (idPropertyEntry)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
