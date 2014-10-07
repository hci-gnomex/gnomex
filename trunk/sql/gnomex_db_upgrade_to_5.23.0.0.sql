use gnomex;

--Hide/Show property for funding agency combo box on new billing account window
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES('show_funding_agency', 'N', 'Show the funding agency field for billing accounts', 'N', null, null);
	

--Add billing account to ProductOrder	
alter table ProductOrder add idBillingAccount int null;
call ExecuteIfTableExists('gnomex','ProductOrder_Audit','ALTER TABLE ProductOrder_Audit add idBillingAccount int null');


--Property to specify where purchase order forms are stored.
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES('product_order_directory', '/home/gnomex/PurchseOrder_HSCGenomics', 'Directory to store purchase order forms', 'Y', null, null);

	
	--Add columns to CoreFacility  
ALTER TABLE CoreFacility add labPhone VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','ALTER TABLE CoreFacility_Audit add labPhone VARCHAR(200) NULL');
ALTER TABLE CoreFacility add contactRoom VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','ALTER TABLE CoreFacility_Audit add contactRoom VARCHAR(200) NULL');
ALTER TABLE CoreFacility add labRoom VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','CoreFacility_Audit','ALTER TABLE CoreFacility_Audit add labRoom VARCHAR(200) NULL');


--Delete trim adapter question property.  No longer needed.	
delete from PropertyDictionary where propertyName = 'choose_adapter_trim_default';

--Remove trimAdapter column from table and audit table
alter table Request Drop column trimAdapter;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN trimAdapter');

--new isolation prep type custom annotations
insert into AnnotationReportField values('REQUEST', 'codeDNAPrepType', 'DNA prep type', 'N', null, 'hci.gnomex.model.DNAPrepType');
insert into AnnotationReportField values('REQUEST', 'codeRNAPrepType', 'RNA prep type', 'N', null, 'hci.gnomex.model.RNAPrepType');

-- new align to genome build.
ALTER TABLE Request add alignToGenomeBuild CHAR(1) NULL;
call ExecuteIfTableExists('gnomex','Request_Audit','ALTER TABLE Request_Audit add alignToGenomeBuild CHAR(1) NULL');


-- new adminNotes to Request
ALTER TABLE Request add adminNotes VARCHAR(5000) NULL;
call ExecuteIfTableExists('gnomex','Request_Audit','ALTER TABLE Request_Audit add adminNotes VARCHAR(5000) NULL');

-- new properties for order review screen and experiment edit screen.
insert into PropertyDictionary values ('is_plate_based_core', 'N', 'Does the core facility deal with plates for experiment submissions?', 'N', null, null);
insert into PropertyDictionary values ('show_admin_notes_on_request', 'N', 'Show admin notes box on description tab when editing an experiment', 'N', null, null);

-- add idCoreFacility to Application
Alter Table Application add idCoreFacility INT(10) NULL;
call ExecuteIfTableExists('gnomex','Request_Audit','Alter Table Application_Audit add idCoreFacility INT(10) NULL');
alter table Application add 
  CONSTRAINT `FK_Application_CoreFacility` FOREIGN KEY `FK_Application_CoreFacility` (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;



