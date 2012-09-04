alter table FlowCellChannel change read1ClustersPassedFilterM read1ClustersPassedFilterM decimal(6,2);

-- update any old user with USER permission to LAB
update AppUser set codeUserPermissionKind='LAB' where codeUserPermissionKind='USER';
update UserPermissionKind set isActive='N' where codeUserPermissionKind='User';

-- new properties
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
  values('experiment_default_view', 'my experiments', 'Indicates default experiment view for non-admin users.  Values are "my experiments", "my lab" or "all".','N');
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
  values('experiment_view_limit', '1000', 'Maximum number of experiments shown in experiment browse view.','N');
  
insert into PropertyDictionary(propertyName, propertyDescription, forServerOnly)
values('purchase_supplies_url', 'Link to purchase supplies for DNA SEQ CORE', 'N');

-- Add table PropertyAnalysisType
DROP TABLE IF EXISTS `gnomex`.`PropertyAnalysisType`;
CREATE TABLE gnomex.PropertyAnalysisType ( 
     idProperty	int(10),
     idAnalysisType           INT(10),
     PRIMARY KEY (idProperty, idAnalysisType),
    CONSTRAINT FK_PropertyAnalysisType_Property FOREIGN KEY FK_PropertyAnalysisType_Property (idProperty)
    REFERENCES gnomex.Property (idProperty)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_PropertyAnalysisType_AnalysisType FOREIGN KEY FK_PropertyAnalysisType_AnalysisType (idAnalysisType)
    REFERENCES gnomex.AnalysisType (idAnalysisType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = INNODB;