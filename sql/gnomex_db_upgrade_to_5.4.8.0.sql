alter table FlowCellChannel change read1ClustersPassedFilterM read1ClustersPassedFilterM decimal(6,2);

-- update any old user with USER permission to LAB
update AppUser set codeUserPermissionKind='LAB' where codeUserPermissionKind='USER';
update UserPermissionKind set isActive='N' where codeUserPermissionKind='User';
