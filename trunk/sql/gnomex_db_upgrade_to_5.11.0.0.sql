use gnomex;

-- Change barcode scheme allowed to be determined by seq lib protocol instead of request category
insert into OligoBarcodeSchemeAllowed (idOligoBarcodeScheme, idSeqLibProtocol) 
select distinct OligoBarcodeSchemeAllowed.idOligoBarcodeScheme, SeqLibProtocol.idSeqLibProtocol from OligoBarcodeSchemeAllowed
  join RequestCategory on OligoBarcodeSchemeAllowed.codeRequestCategory = requestCategory.codeRequestCategory
  join RequestCategoryApplication on requestCategory.codeRequestCategory = RequestCategoryApplication.codeRequestCategory
  join Application on RequestCategoryApplication.codeApplication = Application.codeApplication
  join SeqLibProtocolApplication on Application.codeApplication = SeqLibProtocolApplication.codeApplication
  join SeqLibProtocol on SeqLibProtocolApplication.idSeqLibProtocol = SeqLibProtocol.idSeqLibProtocol
delete from OligoBarcodeSchemeAllowed where codeRequestCategory is not null
alter table OligoBarcodeSchemeAllowed add idSeqLibProtocol Integer;
alter table OligoBarcodeSchemeAllowed add
  CONSTRAINT `FK_OligoBarcodeSchemeAllowed_SeqLibProtocol` FOREIGN KEY `FK_OligoBarcodeSchemeAllowed_SeqLibProtocol` (`idSeqLibProtocol`)
    REFERENCES `gnomex`.`SeqLibProtocol` (`idSeqLibProtocol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table OligoBarcodeSchemeAllowed drop column codeRequestCategory;