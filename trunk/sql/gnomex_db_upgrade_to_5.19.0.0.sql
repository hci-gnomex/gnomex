use gnomex;

insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('seq_lane_number_separator', '-', 'The default separator character for sequence lane numbers', 'Y', null, null);
	
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('seq_lane_letter', 'F', 'The default letter used in Sequence Lane naming', 'Y', null, null);
	
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('status_to_start_workflow', 'SUBMITTED', 'What request status is required before work items are shown for that request', 'Y', 1, null);
	
alter table Request add includeQubitConcentration CHAR(1) null;  
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit add includeQubitConcentration CHAR(1) null');
 
alter table Sample add qubitConcentration DECIMAL(8, 3) NULL;
call ExecuteIfTableExists('gnomex','Sample_Audit','alter table Sample_Audit add qubitConcentration DECIMAL(8, 3) null');
