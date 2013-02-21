use gnomex;

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

-- new columns for collaborator update permission
alter table RequestCollaborator add canUpdate char(1) null;
alter table AnalysisCollaborator add canUpdate char(1) null;


-- new columns for transfer log
alter table TransferLog add emailAddress varchar(1000) null;
alter table TransferLog add ipAddress varchar(50) null;
alter table TransferLog add idAppUser int(10) null;

-- property to enable guest download terms.
INSERT INTO `gnomex`.`PropertyDictionary` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) VALUES
('guest_download_terms', '', 'If property is set, then guests are prompted to agree to these terms prior to allowing download of files.  In addition an email is required which is saved in the transfer log.', 'N');
