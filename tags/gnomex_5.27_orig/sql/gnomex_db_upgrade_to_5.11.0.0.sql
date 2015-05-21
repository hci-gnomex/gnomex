use gnomex;

-- Change barcode scheme allowed to be determined by seq lib protocol instead of request category
alter table OligoBarcodeSchemeAllowed add idSeqLibProtocol Integer;
alter table OligoBarcodeSchemeAllowed add
  CONSTRAINT `FK_OligoBarcodeSchemeAllowed_SeqLibProtocol` FOREIGN KEY `FK_OligoBarcodeSchemeAllowed_SeqLibProtocol` (`idSeqLibProtocol`)
    REFERENCES `gnomex`.`SeqLibProtocol` (`idSeqLibProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
alter table OligoBarcodeSchemeAllowed change column codeRequestCategory codeRequestCategory varchar(10) null; 
    
insert into OligoBarcodeSchemeAllowed (idOligoBarcodeScheme, idSeqLibProtocol) 
select distinct OligoBarcodeSchemeAllowed.idOligoBarcodeScheme, SeqLibProtocol.idSeqLibProtocol from OligoBarcodeSchemeAllowed
  join RequestCategory on OligoBarcodeSchemeAllowed.codeRequestCategory = requestCategory.codeRequestCategory
  join RequestCategoryApplication on requestCategory.codeRequestCategory = RequestCategoryApplication.codeRequestCategory
  join Application on RequestCategoryApplication.codeApplication = Application.codeApplication
  join SeqLibProtocolApplication on Application.codeApplication = SeqLibProtocolApplication.codeApplication
  join SeqLibProtocol on SeqLibProtocolApplication.idSeqLibProtocol = SeqLibProtocol.idSeqLibProtocol;

delete from OligoBarcodeSchemeAllowed where codeRequestCategory is not null;

alter table OligoBarcodeSchemeAllowed drop foreign key FK_OligoBarcodeSchemeAllowed_RequestCategory;

alter table OligoBarcodeSchemeAllowed drop column codeRequestCategory;
alter table OligoBarcodeSchemeAllowed change column idSeqLibProtocol idSeqLibProtocol Integer not null ;


-- Add adapter Sequence columns to seq lib protocol table.  In gnomex these are known as 3' reads and 5' reads
alter table SeqLibProtocol add adapterSequenceRead1 varchar(500);
alter table SeqLibProtocol add adapterSequenceRead2 varchar(500);

-- codeRequestCategory in PropertyDictionary
alter table PropertyDictionary add codeRequestCategory VARCHAR(10) NULL;

-- remove includePIInBilling
alter table Lab drop column includePiInBillingEmails;

-- Add table for RequestCategoryType (Alter if necessary)
DROP TABLE IF EXISTS `gnomex`.`RequestCategoryType`;
CREATE TABLE `gnomex`.`RequestCategoryType` (
  codeRequestCategoryType VARCHAR(100) NOT NULL,
  isIllumina VARCHAR(10) NOT NULL
)
ENGINE = INNODB;
