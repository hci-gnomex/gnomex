DROP TABLE IF EXISTS `gnomex`.`AnalysisGenomeBuild`;
CREATE TABLE `gnomex`.`AnalysisGenomeBuild` (
  `idAnalysis` INT(10) NOT NULL,
  `idGenomeBuild` INT(10) NOT NULL,
  PRIMARY KEY (`idAnalysis`, `idGenomeBuild`),
  CONSTRAINT `FK_AnalysisGenomeBuild_Analysis` FOREIGN KEY `FK_AnalysisGenomeBuild_Analysis` (`idAnalysis`)
    REFERENCES `gnomex`.`Analysis` (`idAnalysis`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_AnalysisGenomeBuild_GenomeBuild` FOREIGN KEY `FK_AnalysisGenomeBuild_GenomeBuild` (`idGenomeBuild`)
    REFERENCES `gnomex`.`GenomeBuild` (`idGenomeBuild`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

INSERT INTO `gnomex`.`AnalysisGenomeBuild` (`idAnalysis`,`idGenomeBuild`) 
 select an.idAnalysis, an.idGenomeBuild
 from Analysis an
 where an.idGenomeBuild is not null;
 
alter table gnomex.Analysis add column privacyExpirationDate DATETIME NULL;

alter table gnomex.Request add column privacyExpirationDate DATETIME NULL;