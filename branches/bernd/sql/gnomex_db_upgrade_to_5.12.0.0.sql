use gnomex;

-- Add chips per kit to iscan chip
alter table IScanChip add chipsPerKit INT(10) NULL;

-- add table RequestCategoryType
CREATE TABLE `gnomex`.`RequestCategoryType` (
  codeRequestCategoryType VARCHAR(10) NOT NULL,
  description VARCHAR(50) NOT NULL,
  defaultIcon VARCHAR(100) NOT NULL,
  isIllumina CHAR(1) NOT NULL,
  hasChannels CHAR(1) NOT NULL,
  PRIMARY KEY (`codeRequestCategoryType`)
)
ENGINE = INNODB;
insert into RequestCategoryType values('CAPSEQ','Capillary Sequencing','assets/dna-helix-icon.png','N','N');
insert into RequestCategoryType values('CHERRYPICK','Cherry Picking','assets/cherrypick.png','N','N');
insert into RequestCategoryType values('CLINSEQ','Clinical Sequenom','assets/cherrypick.png','N','N');
insert into RequestCategoryType values('FRAGANAL','Fragment Analysis Panels','assets/fraganal.png','N','N');
insert into RequestCategoryType values('HISEQ','Illumina Sequencing','assets/DNA_diag.png','Y','Y');
insert into RequestCategoryType values('MISEQ','Illumina Sequencing','assets/DNA_diag.png','Y','Y');
insert into RequestCategoryType values('ILLUMINA','Illumina Sequencing','assets/DNA_diag.png','Y','Y');
insert into RequestCategoryType values('ISCAN','iScan','assets/iscan.png','N','N');
insert into RequestCategoryType values('MICROARRAY','Microarray','assets/microarray_small.png','N','Y');
insert into RequestCategoryType values('MITSEQ','Mitochondrial D-Loop Sequencing','assets/mitseq.png','N','N');
insert into RequestCategoryType values('QC','Sample Quality','assets/chart_line.png','N','N');
insert into RequestCategoryType values('SEQUENOM', 'Sequenom', 'assets/cherrypick.png', 'N', 'N');
alter table RequestCategory add
  CONSTRAINT `FK_RequestCategory_RequestCategoryType` FOREIGN KEY `FK_RequestCategory_RequestCategoryType` (`type`)
    REFERENCES `gnomex`.`RequestCategoryType` (`codeRequestCategoryType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
update RequestCategory set type='MISEQ' where requestCategory like '%MISEQ%';
update RequestCategory set type='HISEQ' where type='illumina';
delete from RequestCategoryType where codeRequestCategoryType='ILLUMINA';
  
