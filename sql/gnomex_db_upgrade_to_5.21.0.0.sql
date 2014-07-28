use gnomex;

DROP TABLE IF EXISTS `gnomex`.`AnnotationReportField`;
CREATE TABLE `gnomex`.`AnnotationReportField` (
  `idAnnotationReportField` INT(10) NOT NULL AUTO_INCREMENT, 
  `source` VARCHAR(50) NULL,
  `fieldName` VARCHAR(50) NULL,
  `display` VARCHAR(50) NULL,
  `isCustom` CHAR(1) NULL,
  `sortOrder` INT(10) NULL,
  `dictionaryLookUpTable` VARCHAR(100) NULL,
  PRIMARY KEY (`idAnnotationReportField`)
)
ENGINE = INNODB;