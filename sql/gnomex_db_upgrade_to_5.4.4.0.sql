use gnomex;
--
-- SampleDropOffLocation 
--
DROP TABLE IF EXISTS `gnomex`.`SampleDropOffLocation`;
CREATE TABLE `gnomex`.`SampleDropOffLocation` (
  `idSampleDropOffLocation` INT(10) NOT NULL AUTO_INCREMENT,
  `sampleDropOffLocation`   VARCHAR(200) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`idSampleDropOffLocation`)
)
ENGINE = INNODB;

alter table Request add column idSampleDropOffLocation INT(10) NULL;
alter table request add column codeRequestStatus VARCHAR(10) NULL;
alter table Request add   
CONSTRAINT `FK_Request_SampleDropOffLocation` FOREIGN KEY `FK_Request_SampleDropOffLocation` (`idSampleDropOffLocation`)
    REFERENCES SampleDropOffLocation (`idSampleDropOffLocation`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
alter table Request add
CONSTRAINT `FK_Request_RequestStatus` FOREIGN KEY `FK_Request_RequestStatus` (`codeRequestStatus`)
    REFERENCES RequestStatus (`codeRequestStatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

--
-- PlateType 
--
DROP TABLE IF EXISTS `gnomex`.`PlateType`;
CREATE TABLE `gnomex`.`PlateType` (
  `codePlateType` VARCHAR(10) NOT NULL,
  `plateTypeDescription`   VARCHAR(50) NOT NULL,
  `isActive` CHAR(1) NULL,
  PRIMARY KEY (`codePlateType`)
)
ENGINE = INNODB;
INSERT INTO `gnomex`.`PlateType`(`codePlateType`, `plateTypeDescription`, `isActive`) values('REACTION', 'Reaction plate', 'Y');
INSERT INTO `gnomex`.`PlateType`(`codePlateType`, `plateTypeDescription`, `isActive`) values('SOURCE', 'Source plate', 'Y');
Alter Table Plate add column codePlateType VARCHAR(10) NOT NULL DEFAULT 'REACTION';
alter table Plate add   
CONSTRAINT `FK_Plate_PlateType` FOREIGN KEY `FK_Plate_PlateType` (`codePlateType`)
    REFERENCES PlateType (`codePlateType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
    
-- Populate new property to turn on data track feature
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('datatrack_supported','Y','Indicates if data track features is supported', 'N');

--
-- Table structure for table `Topic`
--
DROP TABLE IF EXISTS `gnomex`.`Topic`;
CREATE TABLE `gnomex`.`Topic` (
  `idTopic` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(2000) NOT NULL,
  `description` VARCHAR(10000) NULL,
  `idParentTopic` int(10) NOT NULL,  
  `idLab` int(10) NOT NULL,
  `createdBy` VARCHAR(200) NOT NULL,
  `createDate` datetime default NULL,
  `idAppUser` int(10)  default NULL,  
  PRIMARY KEY (`idTopic`),
  KEY `FK_Topic_AppUser` (`idAppUser`),
  KEY `FK_Topic_Lab` (`idLab`),  
  KEY `FK_Topic_ParentTopic` (`idParentTopic`),  
  CONSTRAINT `FK_Topic_AppUser` FOREIGN KEY (`idAppUser`) REFERENCES `AppUser` (`idAppUser`),
  CONSTRAINT `FK_Topic_Lab` FOREIGN KEY (`idLab`) REFERENCES `Lab` (`idLab`),
  CONSTRAINT `FK_Topic_ParentTopic` FOREIGN KEY (`idParentTopic`) REFERENCES `Topic` (`idTopic`)
) ENGINE = INNODB;


--
-- Table structure for table `RequestToTopic`
--
DROP TABLE IF EXISTS `RequestToTopic`;
CREATE TABLE `RequestToTopic` (
  `idTopic` int(10)  NOT NULL,
  `idRequest` int(10)  NOT NULL,
  PRIMARY KEY  (`idTopic`,`idRequest`),
  KEY `FK_TopicRequest_Request` (`idRequest`),
  CONSTRAINT `FK_RequestToTopic_Topic` FOREIGN KEY (`idTopic`) REFERENCES `Topic` (`idTopic`),
  CONSTRAINT `FK_TopicRequest_Request` FOREIGN KEY (`idRequest`) REFERENCES `Request` (`idRequest`)
) ENGINE=InnoDB;

--
-- Table structure for table `AnalysisToTopic`
--
DROP TABLE IF EXISTS `AnalysisToTopic`;
CREATE TABLE `AnalysisToTopic` (
  `idTopic` int(10)  NOT NULL,
  `idAnalysis` int(10)  NOT NULL,
  PRIMARY KEY  (`idTopic`,`idAnalysis`),
  KEY `FK_TopicAnalysis_Analysis` (`idAnalysis`),
  CONSTRAINT `FK_AnalysisToTopic_Topic` FOREIGN KEY (`idTopic`) REFERENCES `Topic` (`idTopic`),
  CONSTRAINT `FK_TopicAnalysis_Analysis` FOREIGN KEY (`idAnalysis`) REFERENCES `Analysis` (`idAnalysis`)
) ENGINE=InnoDB;

--
-- Table structure for table `DataTrackToTopic`
--
DROP TABLE IF EXISTS `DataTrackToTopic`;
CREATE TABLE `DataTrackToTopic` (
  `idTopic` int(10)  NOT NULL,
  `idDataTrack` int(10)  NOT NULL,
  PRIMARY KEY  (`idTopic`,`idDataTrack`),
  KEY `FK_TopicDataTrack_DataTrack` (`idDataTrack`),
  CONSTRAINT `FK_DataTrackToTopic_Topic` FOREIGN KEY (`idTopic`) REFERENCES `Topic` (`idTopic`),
  CONSTRAINT `FK_TopicDataTrack_DataTrack` FOREIGN KEY (`idDataTrack`) REFERENCES `DataTrack` (`idDataTrack`)
) ENGINE=InnoDB;

-- Property for topic feature, turned off for now
INSERT INTO `gnomex`.`PropertyDictionary` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) 
  values ('topics_supported', 'N', 'If Y then Topics feature is activated.', 'N');


-- Properties for serving data track files from the Apace Tomcat Server
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('datatrack_fileserver_url','http://yourservername:8080/das2gnomex/','The URL from Apache Tomcat where the data track files will be served', 'N');
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly) 
values ('datatrack_fileserver_web_context','/path/to/apachetomcat/webapps/das2gnomex/','The directory in the web app on Apache Tomcat that will server the data track files.', 'N');

-- Populate RequestStatus dictionary
insert into gnomex.RequestStatus 
(codeRequestStatus, requestStatus, isActive)
values 
('COMPLETE',	'complete',	'Y'),
('FAILED',	'failed',	'Y'),
('NEW',	        'new',	        'Y'),
('PROCESSING',	'processing',	'Y'),
('SUBMITTED',	'submitted',	'Y');

