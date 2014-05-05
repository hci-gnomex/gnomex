use gnomex;

insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('seq_lane_number_separator', '-', 'The default separator character for sequence lane numbers', 'Y', null, null);
	
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('seq_lane_letter', 'F', 'The default letter used in Sequence Lane naming', 'Y', null, null);
	
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('status_to_start_workflow', 'SUBMITTED', 'What request status is required before work items are shown for that request', 'Y', 1, null);

insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('metrix_server_host','localhost','Hostname or IP on which the Illumina statistics Metrix Server is running.','Y', null, null);
	
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('metrix_server_port','12345','Port (>1024) on which the Illumina statistics Metrix Server is running.','Y', null, null);
	
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('show_activity_dashboard','N','Should the activity feed be shown on the dashboard','N', null, null);
	
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('show_metrix_dashboard','N','Should the metrix server feed be shown on the dashboard','N', null, null);
	
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory) 
	VALUES ('show_news_dashboard','N','Should the news feed be shown on the dashboard','N', null, null);

 
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

-- Remove unused properties for sequence alignment
delete from PropertyDictionary where propertyName='sequence_alignment_supported';
delete from PropertyDictionary where propertyName='sequence_alignment_server_url';     

-- Dashboard tables
CREATE TABLE `gnomex`.`NewsItem` (
	`idNewsItem` INT(10) NOT NULL AUTO_INCREMENT,
	`idSubmitter` INT(10) NOT NULL,
	`idCoreFacility` INT(10) NULL,
	`idCoreSender` INT(10) NOT NULL,
	`idCoreTarget` INT(10) NULL,
	`title` VARCHAR(200) NOT NULL,
	`message` VARCHAR(4000) NOT NULL,
	`date` DATETIME NULL,
	CONSTRAINT `FK_NewsItem_Submitter` FOREIGN KEY `FK_NewsItem_Submitter` (`idSubmitter`)
       REFERENCES `gnomex`.`AppUser` (`idAppUser`)
       ON DELETE NO ACTION
       ON UPDATE NO ACTION,
    PRIMARY KEY (`idNewsItem`)
)
ENGINE = INNODB;

CREATE TABLE `gnomex`.`FAQ` (
  `idFAQ` INT(10) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(300) NOT NULL,
  `url` VARCHAR(500) NOT NULL,
  `views` INT(10) NULL,
  `idCoreFacility` INT(10) NULL,
  PRIMARY KEY (`idFAQ`)
)
ENGINE = INNODB;

CREATE TABLE `gnomex`.`Notification` (
	`idNotification` INT(10) NOT NULL AUTO_INCREMENT,
	`idUserTarget` INT(10) NOT NULL,
	`idLabTarget` INT(10) NULL,
	`sourceType` VARCHAR(20) NOT NULL,
	`message` VARCHAR(250) NULL,
	`date` DATETIME NULL,
	`expID` INT(10) NULL,
	`type`	VARCHAR(25) NULL,
	`fullNameUser`	VARCHAR(100) NULL,
	`imageSource` VARCHAR(50),
	`idCoreFacility` INT(10) NULL,	
	PRIMARY KEY (`idNotification`)
)
ENGINE = INNODB;

CREATE TABLE `gnomex`.`MetrixObject` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `run_id` VARCHAR(512) NULL,
  `object_value` VARBINARY(8000),
  `state` INT(10) NULL,
  PRIMARY KEY (`id`)
)
ENGINE = INNODB;
