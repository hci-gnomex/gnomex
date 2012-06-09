use gnomex;


-- Set CoreFacility name DEFAULT to Microarray and Genomic Analysis   
update CoreFacility set facilityName = 'Microarray and Genomic Analysis' where facilityName = 'DEFAULT';


-- Default all of the request categories to the Microarray and Genomic Analysis core facility
update RequestCategory rc 
set rc.idCoreFacility = cf.idCoreFacility
  join CoreFacility cf on cf.idCoreFacility = rc.idCoreFacility
  where facilityName = 'Microarray and Genomic Analysis'
  and codeRequestCategory not in ('CAPSEQ', 'MITSEQ', 'FRAGANAL', 'CHERRYPICK');

-- Set the idCoreFacility on Request based on the request category's core facility
update Request r 
set r.idCoreFacility = cf.idCoreFacility
  join RequestCategory r on r.codeRequestCategory = rc.codeRequestCategory
  join CoreFacility cf on rc.idCoreFacility = cf.idCoreFacility;

  
-- New table Primer
CREATE TABLE gnomex.Primer(
	idPrimer int(10) auto_increment NOT NULL,
	name varchar(50) NOT NULL,
	description varchar(200) NULL,
	sequencing varchar(2000) null,
	isActive char(1) NULL,
 PRIMARY KEY (idPrimer) 
 ) ENGINE = INNODB;
 
-- New table Assay
CREATE TABLE gnomex.Assay(
	idAssay int(10) auto_increment NOT NULL,
	name varchar(50) NOT NULL,
	description varchar(200) NULL,
	isActive char(1) NULL,
 PRIMARY KEY (idAssay) 
 ) ENGINE = INNODB;
 
 
-- Add idPrimer, idAssay to PlateWell
alter table gnomex.PlateWell add column idAssay int(10) null;
alter table gnomex.PlateWell add column idPrimer int(10) null;
alter table gnomex.PlateWell  add
CONSTRAINT `FK_PlateWell_Assay` FOREIGN KEY `FK_PlateWell_Assay` (`idAssay`)
    REFERENCES Assay (`idAssay`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table gnomex.PlateWell  add
CONSTRAINT `FK_PlateWell_Primer` FOREIGN KEY `FK_PlateWell_Primer` (`idPrimer`)
    REFERENCES Primer (`idPrimer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- Move redoFlag from Sample to PlateWell
alter table gnomex.PlateWell add column redoFlag char(1) null;
alter table gnomex.Sample drop column redoFlag;    


-- new isControl for Sample
alter table Sample add column isControl char(1) NULL;

-- new purchaseOrderForm and orderFormFileType for BillingAccount
alter table BillingAccount add column purchaseOrderForm LONGBLOB NULL;
alter table BillingAccount add column orderFormFileType VARCHAR(10) NULL;


-- PriceCategory description is null
alter table gnomex.PriceCategory change column description description varchar(5000) null;