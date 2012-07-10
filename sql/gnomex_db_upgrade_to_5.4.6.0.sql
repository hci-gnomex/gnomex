use gnomex;

--Add idReleaser column to the Chromatogram table
alter table Chromatogram add column `idReleaser` int(10) null;

-- Add idSubmitter to Analysis
alter table gnomex.Analysis add column idSubmitter int(10) null;
alter table gnomex.Analysis add
CONSTRAINT `FK_Analysis_AppUser1` FOREIGN KEY  (`idSubmitter`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

-- Add idSubmitter to Request
alter table gnomex.Request add column idSubmitter int(10) null;
alter table gnomex.Request add
CONSTRAINT `FK_Request_AppUser1` FOREIGN KEY  (`idSubmitter`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- This constraint should have been added to Request previously
alter table gnomex.Request add   
  CONSTRAINT `FK_Request_AppUser` FOREIGN KEY  (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;     


-- Initialize idSubmitter to current contents of idAppUser  
update Analysis
set idSubmitter = idAppUser

update Request
set idSubmitter = idAppUser


--Add new fields for commercial pricing
alter table Lab add column `isExternalPricingCommercial` CHAR(1) null;
alter table Price add column `unitPriceExternalCommercial` DECIMAL(6, 2) NULL;

-- Combine experiment_read_property and experiment_write_property
update PropertyDictionary set propertyName = 'experiment_directory' where propertyName = 'experiment_read_directory';
delete from PropertyDictionary where propertyName = 'experiment_write_directory';

-- Combine analysis_read_property and analysis_write_property
update PropertyDictionary set propertyName = 'analysis_directory' where propertyName = 'analysis_read_directory';
delete from PropertyDictionary where propertyName = 'analysis_write_directory';

-- Combine datatrack_read_property and datatrack_write_property
update PropertyDictionary set propertyName = 'datatrack_directory' where propertyName = 'datatrack_read_directory';
delete from PropertyDictionary where propertyName = 'datatrack_write_directory';

-- Create new property for instrument_run_directory
insert into PropertyDictionary (propertyName, propertyValue, forServerOnly, idCoreFacility)
SELECT  'instrument_run_directory', '/path/to/gnomex/instrumentrun','Y', idCoreFacility
from CoreFacility where coreFacilityName = 'DNA Sequencing';

-- Add Septa SealType 
insert into gnomex.SealType
(codeSealType, sealType, isActive)
values 
('SEPTA',  'Septa', 'Y');

