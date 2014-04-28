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
  
-- Increase size of prices
alter table gnomex.Price MODIFY unitPrice DECIMAL(7,2) NOT NULL DEFAULT 0;
call ExecuteIfTableExists('gnomex','Price_Audit','alter table gnomex.Price_Audit MODIFY unitPrice DECIMAL(7,2) NULL');
alter table gnomex.Price MODIFY unitPriceExternalAcademic DECIMAL(7,2) NOT NULL DEFAULT 0;
call ExecuteIfTableExists('gnomex','Price_Audit','alter table gnomex.Price_Audit MODIFY unitPriceExternalAcademic DECIMAL(7,2) NULL');
alter table gnomex.Price MODIFY unitPriceExternalCommercial DECIMAL(7,2) NOT NULL DEFAULT 0;
call ExecuteIfTableExists('gnomex','Price_Audit','alter table gnomex.Price_Audit MODIFY unitPriceExternalCommercial DECIMAL(7,2) NULL');

alter table BillingItem MODIFY unitPrice DECIMAL(7,2) NULL;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit MODIFY unitPrice DECIMAL(7,2) NULL');
alter table BillingItem MODIFY invoicePrice DECIMAL(9,2) NULL;
call ExecuteIfTableExists('gnomex','BillingItem_Audit','alter table BillingItem_Audit MODIFY invoicePrice DECIMAL(9,2) NULL');

-- add sort order, isactive and protocolDescription to NumberSequencingCyclesAllowed
alter table NumberSequencingCyclesAllowed add sortOrder INT(10) null;
call ExecuteIfTableExists('gnomex','NumberSequencingCyclesAllowed_Audit','alter table NumberSequencingCyclesAllowed_Audit add sortOrder INT(10) null');
alter table NumberSequencingCyclesAllowed add isActive char(1) not null default 'Y';
call ExecuteIfTableExists('gnomex','NumberSequencingCyclesAllowed_Audit','alter table NumberSequencingCyclesAllowed_Audit add isActive char(1) null');
alter table NumberSequencingCyclesAllowed add protocolDescription LONGTEXT NULL;
call ExecuteIfTableExists('gnomex','NumberSequencingCyclesAllowed_Audit','alter table NumberSequencingCyclesAllowed_Audit add protocolDescription LONGTEXT NULL');

-- SequenceLane now has idNumberSequencingCyclesAllowed
alter table SequenceLane add idNumberSequencingCyclesAllowed INT(10) null;
call ExecuteIfTableExists('gnomex','SequenceLane_Audit','alter table SequenceLane_Audit add idNumberSequencingCyclesAllowed INT(10) null');
alter table SequenceLane add 
  CONSTRAINT `FK_SequenceLane_NumberSequencingCyclesAllowed` FOREIGN KEY `FK_SequenceLane_NumberSequencingCyclesAllowed` (`idNumberSequencingCyclesAllowed`)
    REFERENCES `gnomex`.`NumberSequencingCyclesAllowed` (`idNumberSequencingCyclesAllowed`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
update SequenceLane
  join Request on Request.idRequest=SequenceLane.idRequest
  join NumberSequencingCyclesAllowed on NumberSequencingCyclesAllowed.idNumberSequencingCycles = SequenceLane.idNumberSequencingCycles
      and NumberSequencingCyclesAllowed.idSeqRunType=SequenceLane.idSeqRunType
      and NumberSequencingCyclesAllowed.codeRequestCategory=Request.codeRequestCategory
 set SequenceLane.idNumberSequencingCyclesAllowed=NumberSequencingCyclesAllowed.idNumberSequencingCyclesAllowed
 where SequenceLane.idNumberSequencingCyclesAllowed is null;

-- Remove isSampleBarcodingOptional
alter table gnomex.RequestCategory drop column isSampleBarcodingOptional;
call ExecuteIfTableExists('gnomex','RequestCategory_Audit','alter table RequestCategory_Audit drop column isSampleBarcodingOptional');