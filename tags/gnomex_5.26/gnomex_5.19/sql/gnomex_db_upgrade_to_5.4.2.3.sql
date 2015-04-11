
--
-- Core Facility Manager
--
DROP TABLE IF EXISTS `gnomex`.`CoreFacilityManager`;
CREATE TABLE `gnomex`.`CoreFacilityManager` (
  `idCoreFacility` INT(10) NOT NULL,
  `idAppUser` INT(10) NOT NULL,
  PRIMARY KEY (`idCoreFacility`, `idAppUser`),
  CONSTRAINT `FK_CoreFacilityManager_AppUser` FOREIGN KEY  (`idAppUser`)
    REFERENCES `gnomex`.`AppUser` (`idAppUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CoreFacilityManager_CoreFacility` FOREIGN KEY  (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

--
-- Core Facility Lab
--
DROP TABLE IF EXISTS `gnomex`.`CoreFacilityLab`;
CREATE TABLE `gnomex`.`CoreFacilityLab` (
  `idCoreFacility` INT(10) NOT NULL,
  `idLab` INT(10) NOT NULL,
  PRIMARY KEY (`idCoreFacility`, `idLab`),
  CONSTRAINT `FK_CoreFacilityLab_Lab` FOREIGN KEY  (`idLab`)
    REFERENCES `gnomex`.`Lab` (`idLab`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CoreFacilityLab_CoreFacility` FOREIGN KEY  (`idCoreFacility`)
    REFERENCES `gnomex`.`CoreFacility` (`idCoreFacility`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;
INSERT INTO `gnomex`.`CoreFacilityLab`(`idCoreFacility`, `idLab`) select 1, idLab from Lab;
