use gnomex;

-- Add new billing account columns
alter table BillingAccount add column shortAcct VARCHAR(10) null;
alter table BillingAccount add column startDate DATETIME NULL;

insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('default_visibility_topic','MEM','Default visibility for new Topic. Use value OWNER for Owner, MEM for Members, INST for Institution and PUBLIC for Public. Property can also be eliminated or set to blank to leave original default behavior in place.', 'N');

