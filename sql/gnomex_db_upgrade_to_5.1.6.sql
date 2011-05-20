-- Add table SampleCharacteristicPlatform
CREATE TABLE gnomex.SampleCharacteristicPlatform ( 
     idSampleCharacteristic	INT(10),
     codeRequestCategory    VARCHAR(10),
     PRIMARY KEY (idSampleCharacteristic, codeRequestCategory),
    CONSTRAINT FK_SampleCharacteristicPlantform_SampleCharacteristic FOREIGN KEY FK_SampleCharacteristicPlatform_SampleCharacteristic (idSampleCharacteristic)
    REFERENCES gnomex.SampleCharacteristic (idSampleCharacteristic)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_SampleCharacteristicPlatform_RequestCategory FOREIGN KEY FK_SampleCharacteristicPlatform_RequestCategory (codeRequestCategory)
    REFERENCES gnomex.RequestCategory (codeRequestCategory)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;

-- Add isRequired column to SampleCharacteristic
ALTER TABLE gnomex.SampleCharacteristic add column isRequired CHAR(1);
update gnomex.SampleCharacteristic set isRequired = 'N';

