use gnomex;

-- Add sortOrder to workflow step table
alter table Step add column sortOrder INT(10) null;

update Step set sortOrder = 1 where codeStep like '%QC';
update Step set sortOrder = 2 where codeStep like '%LIBPREP';
update Step set sortOrder = 3 where codeStep like '%SEQASSEM';
update Step set sortOrder = 4 where codeStep like '%SEQPIPE';

update Step set sortOrder = 2 where codeStep = 'LABEL';
update Step set sortOrder = 3 where codeStep = 'HYB';
update Step set sortOrder = 4 where codeStep = 'EXT';



-- Create many-many between step and price category table
CREATE TABLE `gnomex`.`PriceCategoryStep` (
  idPriceCategory INT(10),
  codeStep VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idPriceCategory`, codeStep),
  CONSTRAINT `FK_PriceCategoryStep_PriceCategory` FOREIGN KEY  (`idPriceCategory`)
    REFERENCES `gnomex`.`PriceCategory` (`idPriceCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PriceCategoryStep_Step` FOREIGN KEY  (`codeStep`)
    REFERENCES `gnomex`.`Step` (`codeStep`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

-- Create IScanChip table
CREATE TABLE `gnomex`.`IScanChip` (
  `idIScanChip` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NULL,
  `costPerSample` DECIMAL(5, 2) NULL,
  `samplesPerChip` INT(10) NULL,
  `markersPerSample` VARCHAR(100) NULL,
  `catalogNumber` VARCHAR(100) NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idIScanChip`)
)
ENGINE = INNODB;


alter table Request add numberIScanChips Integer;
alter table Request add idIScanChip Integer;
alter table Request add
  CONSTRAINT `FK_Request_IScanChip` FOREIGN KEY `FK_Request_IScanChip` (`idIScanChip`)
    REFERENCES `gnomex`.`IScanChip` (`idIScanChip`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

-- Add isCreditCard to BillingAccount table
alter table gnomex.BillingAccount add column isCreditCard varchar(1);

-- Add idCreditCardCompany to BillingAccount table
alter table gnomex.BillingAccount add column idCreditCardCompany INT(10);
alter table BillingAccount add
  CONSTRAINT `FK_BillingAccount_CreditCardCompany` FOREIGN KEY `FK_BillingAccount_CreditCardCompany` (`idCreditCardCompany`)
    REFERENCES `gnomex`.`CreditCardCompany` (`idCreditCardCompany`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;


-- Add CreditCardCompany dictionary
CREATE TABLE gnomex.CreditCardCompany (
   idCreditCardCompany INT(10) NOT NULL AUTO_INCREMENT,
   name varchar(100),
   isActive varchar(1),
   sortOrder INT(10),
   PRIMARY KEY (idCreditCardCompany)
)
ENGINE = INNODB;



-- new columns for collaborator update permission
alter table RequestCollaborator add canUpdate char(1) null;
alter table AnalysisCollaborator add canUpdate char(1) null;


-- new columns for transfer log
alter table TransferLog add emailAddress varchar(1000) null;
alter table TransferLog add ipAddress varchar(50) null;
alter table TransferLog add idAppUser int(10) null;

-- new column for BillingAccount
alter table BillingAccount add orderFormFileSize bigint(20) null;

-- property to enable guest download terms.
INSERT INTO `gnomex`.`PropertyDictionary` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) VALUES
('guest_download_terms', '', 'If property is set, then guests are prompted to agree to these terms prior to allowing download of files.  In addition an email is required which is saved in the transfer log.', 'N');


-- new columns on SeqLibProtocol 
alter table SeqLibProtocol add adapterSequence5Prime varchar(500) null;
alter table SeqLibProtocol add adapterSequence3Prime varchar(500) null;

-- new columns on lab
alter table Lab add billingContactEmail varchar(200) null;
alter table Lab add includePiInBillingEmails char(1) null;
update Lab set includePiInBillingEmails='Y';
