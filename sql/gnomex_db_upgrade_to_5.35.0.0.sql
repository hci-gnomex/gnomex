use gnomex;

-- Add description to Product
ALTER TABLE Product ADD COLUMN description VARCHAR(500) NULL;
CALL ExecuteIfTableExists('gnomex', 'Product_Audit', 'ALTER TABLE Product_Audit ADD COLUMN description VARCHAR(500) NULL');

ALTER TABLE ProductType
DROP COLUMN utilizePurchasingSystem;
CALL ExecuteIfTableExists('gnomex', 'ProductType_Audit', 'ALTER TABLE ProductType_Audit DROP COLUMN utilizePurchasingSystem');

ALTER TABLE ProductOrder
DROP COLUMN uuid;
CALL ExecuteIfTableExists('gnomex', 'ProductOrder_Audit', 'ALTER TABLE ProductOrder_Audit DROP COLUMN uuid');

ALTER TABLE BillingItem
DROP COLUMN tag;
CALL ExecuteIfTableExists('gnomex', 'BillingItem_Audit', 'ALTER TABLE BillingItem_Audit DROP COLUMN tag');

-- Change displayName to fileName on Chromatogram
ALTER TABLE Chromatogram change column displayName fileName VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','Chromatogram_Audit','alter table Chromatogram_Audit change column displayName fileName VARCHAR(200) NULL;');



-- New work flow step script

alter table PriceCategoryStep drop foreign key FK_PriceCategoryStep_Step;
alter table WorkItem drop foreign key FK_WorkItem_Step;
alter table Step drop primary key;

alter table Step modify COLUMN codeStep VARCHAR (20) not null;
alter table PriceCategoryStep modify COLUMN codeStep VARCHAR (20) not null;
alter table WorkItem modify COLUMN codeStepNext VARCHAR (20) not null;

alter table Step add constraint PK_STEP Primary key (codeStep);
alter table PriceCategoryStep add constraint FK_PriceCategoryStep_Step Foreign key(codeStep) references Step(codeStep);
alter table WorkItem add constraint FK_WorkItem_Step Foreign key(codeStepNext) references Step(codeStep);

CALL ExecuteIfTableExists('gnomex','WorkItem_Audit','alter table WorkItem_Audit modify column codeStepNext VARCHAR(20) null');
CALL ExecuteIfTableExists('gnomex','PriceCategoryStep_Audit','alter table PriceCategoryStep_Audit modify column codeStep VARCHAR(20) null;');
CALL ExecuteIfTableExists('gnomex','Step_Audit','alter table Step_Audit modify column codeStep VARCHAR(20) null');

insert into Step (codeStep, step, isActive, sortOrder)
Values ('HSEQPREPQC', 'Illumina HiSeq Library Prep QC', 'Y', null);

insert into Step (codeStep, step, isActive, sortOrder)
Values ('MISEQPREPQC', 'Illumina MiSeq Library Prep QC', 'Y', null);


alter table Sample add qcLibConcentration decimal(8,1) NULL;
CALL ExecuteIfTableExists('gnomex','Sample_Audit','alter table Sample_Audit add qcLibConcentration decimal(8,1) NULL');

create table LibraryPrepQCProtocol(
idLibPrepQCProtocol int AUTO_INCREMENT Primary Key,
protocolDisplay varchar(50) not null,
codeRequestCategory varchar(10) not null,
constraint FK_RequestCategory_LibraryPrepQCProtocol foreign key (codeRequestCategory) references RequestCategory(codeRequestCategory)
) ENGINE = INNODB;

alter table Sample add idLibPrepQCProtocol int NULL;
alter table Sample add constraint FK_Sample_LibraryPrepQCProtocol foreign key (idLibPrepQCProtocol) references LibraryPrepQCProtocol(idLibPrepQCProtocol);

CALL ExecuteIfTableExists('gnomex','Sample_Audit','alter table Sample_Audit add idLibPrepQCProtocol int NULL');

alter table Sample add sampleVolume decimal (8,1) NULL;
CALL ExecuteIfTableExists('gnomex','Sample_Audit','alter table Sample_Audit add sampleVolume decimal(8,1) NULL');

create table LibraryPrepQCProtocol_Audit(
idLibPrepQCProtocol int not null,
protocolDisplay varchar(50) not null,
codeRequestCategory varchar(10) not null,
AuditAppuser varchar(128) NULL,
AuditOperation char(1) NULL,
AuditSystemUser varchar(30) NULL,
AuditOperatioDate datetime NULL,
AuditEditedByPersonID numeric(7, 0) NULL
);