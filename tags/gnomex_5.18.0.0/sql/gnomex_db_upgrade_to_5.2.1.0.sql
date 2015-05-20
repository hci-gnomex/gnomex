INSERT INTO `gnomex`.`Property` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) 
  values ('usage_user_visibility', 'none', 'Permission level for non-admin gnomex users to view lab usage data. [none, masked, full]', 'N');

alter table FlowCellChannel change column q30Gb q30Gb DECIMAL(4,1);

alter table Lab add column excludeUsage char(1) null;
