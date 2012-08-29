alter table FlowCellChannel change read1ClustersPassedFilterM read1ClustersPassedFilterM decimal(6,2);

-- update any old user with USER permission to LAB
update AppUser set codeUserPermissionKind='LAB' where codeUserPermissionKind='USER';
update UserPermissionKind set isActive='N' where codeUserPermissionKind='User';

-- new properties
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
  values('experiment_default_view', 'my experiments', 'Indicates default experiment view for non-admin users.  Values are "my experiments", "my lab" or "all".','N');
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
  values('experiment_view_limit', '1000', 'Maximum number of experiments shown in experiment browse view.','N');
