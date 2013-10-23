-- Procedure to modify columns in audit tables if they exist.
drop procedure if exists ExecuteIfTableExists;
delimiter '//'

create procedure ExecuteIfTableExists(
  IN dbName tinytext,
  IN tableName tinytext,
  IN statement text)
begin
  IF EXISTS (SELECT * FROM information_schema.TABLES WHERE table_name=tableName and table_schema=dbName)
  THEN
    set @ddl=statement;
    prepare stmt from @ddl;
    execute stmt;
  END IF;
end;
//

delimiter ';'

-- Add unique constraint
alter table SampleExperimentFile add constraint UN_SampleExperimentFile UNIQUE (idSample, idExperimentFile);

-- Add DNA prep type
DROP TABLE IF EXISTS `gnomex`.`DNAPrepType`;
CREATE TABLE `gnomex`.`DNAPrepType` (
  `codeDNAPrepType` VARCHAR(10) NOT NULL,
  `dnaPrepType` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codeDNAPrepType`)
)
ENGINE = INNODB;

-- Samples per batch on Application
alter table Application add samplesPerBatch int(10) NULL;

-- DNA Prep type on request
alter table Request add codeDNAPrepType varchar(10) NULL;
alter table Request add 
  CONSTRAINT `FK_Request_DNAPrepType` FOREIGN KEY `FK_Request_DNAPrepType` (`codeDNAPrepType`)
    REFERENCES `gnomex`.`DNAPrepType` (`codeDNAPrepType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- Sequenom application type
insert into gnomex.ApplicationType values('Sequenom', 'Sequenom');
