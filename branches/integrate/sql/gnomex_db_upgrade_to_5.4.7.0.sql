use gnomex;

-- Add new billing account columns
alter table BillingAccount add column shortAcct VARCHAR(10) null;
alter table BillingAccount add column startDate DATETIME NULL;

insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('default_visibility_topic','MEM','Default visibility for new Topic. Use value OWNER for Owner, MEM for Members, INST for Institution and PUBLIC for Public. Property can also be eliminated or set to blank to leave original default behavior in place.', 'N');
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values('billing_gl_journal_line_ref_core_facility','','Always blank for open source GNomEx', 'Y');
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values('billing_gl_journal_id_core_facility','','Always blank for open source GNomEx', 'Y');
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
	values('generic_no_reply_email', 'DoNotReply@somewhere.edu', 'Generic no reply to use for emails going to multiple core facilities.', 'Y');
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
	values('lucene_topic_index_directory', '/home/gnomex/luceneIndex/Topic', 'The file directory for storing lucene index files on topic data.', 'Y');

-- Add Invoice table
CREATE TABLE `gnomex`.`Invoice` (
  idInvoice INT(10) NOT NULL AUTO_INCREMENT,
  idCoreFacility INT(10) NOT NULL,
  idBillingPeriod INT(10) NOT NULL,
  idBillingAccount INT(10) NOT NULL,
  invoiceNumber VARCHAR(50) NULL,
  lastEmailDate DATETIME NULL,
  PRIMARY KEY (`idInvoice`),
  UNIQUE KEY `UN_InvoiceCorePeriodAccount` (idCoreFacility, idBillingPeriod, idBillingAccount),
  CONSTRAINT `FK_Invoice_CoreFacility` FOREIGN KEY `FK_Invoice_CoreFacility` (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Invoice_BillingPeriod` FOREIGN KEY `FK_Invoice_BillingPeriod` (`idBillingPeriod`)
    REFERENCES `gnomex`.`BillingPeriod` (`idBillingPeriod`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Invoice_BillingAccount` FOREIGN KEY `FK_Invoice_BillingAccount` (`idBillingAccount`)
    REFERENCES `gnomex`.`BillingAccount` (`idBillingAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

alter table gnomex.BillingItem add column idInvoice INT(10) NULL;
alter table gnomex.BillingItem add
  CONSTRAINT `FK_BillingItem_Invoice` FOREIGN KEY  (`idInvoice`)
    REFERENCES `gnomex`.`Invoice` (`idInvoice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

-- Populate invoices
insert into Invoice(idCoreFacility, idBillingPeriod, idBillingAccount)
  select BillingItem.idCoreFacility, BillingItem.idBillingPeriod, BillingItem.idBillingAccount 
    from BillingItem 
    left join Invoice on Invoice.idCoreFacility=BillingItem.idCoreFacility
            and Invoice.idBillingPeriod=BillingItem.idBillingPeriod
            and Invoice.idBillingAccount=BillingItem.idBillingAccount
    where Invoice.idInvoice is null
    group by BillingItem.idCoreFacility, BillingItem.idBillingPeriod, BillingItem.idBillingAccount;
update Invoice set invoiceNumber=CONCAT('I-',CAST(idInvoice AS CHAR)) where invoiceNumber is null;
update BillingItem 
  join Invoice on Invoice.idCoreFacility = BillingItem.idCoreFacility and Invoice.idBillingPeriod = BillingItem.idBillingPeriod
    and Invoice.idBillingAccount = BillingItem.idBillingAccount
  set BillingItem.idInvoice = Invoice.idInvoice
  where BillingItem.idInvoice is null;
