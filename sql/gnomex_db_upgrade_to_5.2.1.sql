INSERT INTO `gnomex`.`Property` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) 
  values ('usage_user_visibility', 'none', 'Permission level for non-admin gnomex users to view lab usage data. [none, masked, full]', 'N');

alter table gnomex.FlowCellChannel alter column q30Gb q30Gb DECIMAL(4,1) null;

alter table gnomex.Lab change column excludeUsage char(1) null;