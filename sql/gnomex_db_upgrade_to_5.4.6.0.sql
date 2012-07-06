use gnomex;

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
