use gnomex;
SET FOREIGN_KEY_CHECKS = 0;

-- Update AppUser
update gnomex.AppUser au join genopub.User u on au.idAppUser = u.idAppUserGNomEx set au.ucscUrl = u.ucscUrl;

-- Update GenomeBuild
update gnomex.GenomeBuild gb join genopub.GenomeVersionView gv on gv.idGenomeBuildGNomEx = gb.idGenomeBuild 
       set gb.buildDate = gv.buildDate,
           gb.coordURI = gv.coordURI,
           gb.coordVersion = gv.coordVersion,
           gb.coordSource = gv.coordSource,
           gb.coordTestRange = gv.coordTestRange,
           gb.coordAuthority = gv.coordAuthority,
           gb.ucscName = gv.ucscName,
           gb.dataPath = gv.dataPath;

-- Update Organism
update gnomex.Organism o join genopub.OrganismView ov on ov.idOrganismGNomEx = o.idOrganism 
 set o.sortOrder = ov.sortOrder,
     o.binomialName = ov.binomialName,
     o.NCBITaxID = ov.NCBITaxID;



-- Add genome versions that are not already in gnomex
INSERT INTO gnomex.GenomeBuild
           (genomeBuildName
           ,idOrganism
           ,isActive
           ,isLatestBuild
           ,idAppUser
           ,buildDate
           ,coordURI
           ,coordVersion
           ,coordSource
           ,coordTestRange
           ,coordAuthority
           ,ucscName
           ,dataPath
           ,das2Name)
     select name
           ,idOrganismGNomEx
           ,'Y'
           ,'N'
           ,null
           ,buildDate
           ,coordURI
           ,coordVersion
           ,coordSource
           ,coordTestRange
           ,coordAuthority
           ,ucscName
           ,dataPath
           ,name
           from genopub.GenomeVersionView where idGenomeBuildGNomEx is null;
GO


-- Populate GenomeBuildAlias
insert into gnomex.GenomeBuildAlias (alias, idGenomeBuild)
select alias, idGenomeBuildGNomEx
from genopub.GenomeVersionAlias a
left join genopub.GenomeVersionView gv on gv.idGenomeVersion = a.idGenomeVersion
where idGenomeBuildGNomEx is not null;

-- Property
INSERT INTO gnomex.Property
           (name
           ,mageOntologyCode
           ,mageOntologyDefinition
           ,isActive
           ,idAppUser
           ,codePropertyType
           ,description
           ,isRequired
           ,forDataTrack)
    select
           name
           ,null
           ,null
           ,'N'
           ,NULL
           ,codePropertyType
           ,NULL
           ,'N' 
           ,'Y'
           FROM genopub.Property
GO

update genopub.Property p 
  join  gnomex.Property p1 on p.name = p1.name
  set idPropertyGnomEx = p1.idProperty;

  
-- Property Option
INSERT INTO gnomex.PropertyOption
           (value
           ,idProperty
           ,sortOrder
           ,isActive)
    select  name
           ,idPropertyGNomEx
           ,sortOrder
           ,isActive
    from genopub.PropertyOptionView
GO

update  genopub.PropertyOptionView   pv
  join gnomex.PropertyOption p1  on pv.name = p1.value and p1.idProperty = pv.idPropertyGNomEx
  set idPropertyOptionGnomEx = p1.idPropertyOption;

  
  
-- Populate Segment
INSERT INTO gnomex.Segment
           (length
           ,name
           ,idGenomeBuild
           ,sortOrder)
     select length
           ,name
           ,idGenomeBuildGNomEx
           ,sortOrder from genopub.SegmentView;



-- Need MEMCOL visibility
insert into gnomex.Visibility (codeVisibility, visibility) values('MEMCOL', 'Members and Collaborators');

-- Populate  DataTrack
LOCK TABLES gnomex.DataTrack WRITE;
/*!40000 ALTER TABLE gnomex.DataTrack DISABLE KEYS */;
INSERT INTO gnomex.DataTrack
           (idDataTrack
           ,name
           ,description
           ,fileName
           ,idGenomeBuild
           ,codeVisibility
           ,idAppUser
           ,idLab
           ,summary
           ,createdBy
           ,createDate
           ,isLoaded
           ,idInstitution
           ,dataPath)
SELECT a.idAnnotation
      ,a.name
      ,a.description
      ,a.fileName
      ,gv.idGenomeBuildGNomEx
      ,a.codeVisibility
      ,u.idAppUserGNomEx
      ,ug.idLabGNomEx
      ,a.summary
      ,a.createdBy
      ,a.createDate
      ,a.isLoaded
      ,i.idInstitutionGNomEx
      ,a.dataPath
  FROM genopub.Annotation   a
    left join genopub.GenomeVersionView gv on gv.idGenomeVersion = a.idGenomeVersion
    left join genopub.User u on a.idUser = u.idUser
    left join genopub.UserGroup ug on ug.idUserGroup = a.idUserGroup
    left join genopub.Institute i on i.idInstitute = a.idInstitute;
/*!40000 ALTER TABLE gnomex.DataTrack ENABLE KEYS */;
UNLOCK TABLES;


-- Change the DataTrack number (fileName) from 'Axxxx' to 'DTxxxx'
update gnomex.DataTrack set fileName = concat('DT', idDataTrack);


-- Populate DataTrackFolder
LOCK TABLES gnomex.DataTrack WRITE;
/*!40000 ALTER TABLE gnomex.DataTrackFolder DISABLE KEYS */;
INSERT INTO gnomex.DataTrackFolder
           (idDataTrackFolder
           ,name
           ,description
           ,idParentDataTrackFolder
           ,idGenomeBuild
           ,idLab
           ,createdBy
           ,createDate)
     select ag.idAnnotationGrouping, 
             ag.name,
               ag.description,
               ag.idParentAnnotationGrouping,
               gv.idGenomeBuildGNomEx,
               ug.idLabGNomEx,
               createdBy,
               createDate
           from genopub.AnnotationGrouping ag
            left join genopub.UserGroup ug on ag.idUserGroup = ug.idUserGroup
            left join genopub.GenomeVersionView gv on gv.idGenomeVersion = ag.idGenomeVersion;
/*!40000 ALTER TABLE gnomex.DataTrackFolder ENABLE KEYS */;
UNLOCK TABLES;




-- Migrate AnnotationToAnnotationGrouping to DataTrackToDataTrackFolder
insert into gnomex.DataTrackToFolder (idDataTrack, idDataTrackFolder)
select idAnnotation, idAnnotationGrouping from genopub.AnnotationToAnnotationGrouping;



-- Populate DataTrackCollaborator
insert into gnomex.DataTrackCollaborator (idDataTrack, idAppUser, canUploadData)
select idAnnotation, idAppUserGNomEx, canUploadData
from genopub.AnnotationCollaborator ac
left join genopub.User u on u.idUser = ac.idUser;



-- Migrate AnnotationProperty to PropertyEntry
INSERT INTO gnomex.PropertyEntry
           (valueString
           ,otherLabel
           ,idProperty
           ,idDataTrack)
     select 
            value,
            null,
            idPropertyGNomEx,
            idAnnotation
    from genopub.AnnotationPropertyView;
    
update  genopub.AnnotationPropertyView p 
  join  gnomex.PropertyEntry p1 on p.value = p1.valueString and p1.idProperty = p.idPropertyGNomEx and p1.idDataTrack = p.idAnnotation
  set idPropertyEntryGnomEx = p1.idPropertyEntry;

    
            
-- Migrate AnnotationPropertyOption to PropertyEntryOption 
INSERT INTO gnomex.PropertyEntryOption
           (idPropertyEntry
           ,idPropertyOption)
     select idPropertyEntryGNomEx,
           idPropertyOptionGNomEx
     from genopub.AnnotationPropertyOption pe
     join genopub.AnnotationPropertyView pv on pv.idAnnotationProperty = pe.idAnnotationProperty
     join genopub.PropertyOptionView po on pe.idPropertyOption = po.idPropertyOption;


-- Migrate AnnotationPropertyValue to PropertyEntryValue
INSERT INTO gnomex.PropertyEntryValue
           (idPropertyEntry
           ,value)
     select idPropertyEntryGNomEx,
           v.value
     from genopub.AnnotationPropertyValue v
     join genopub.AnnotationPropertyView pv on pv.idAnnotationProperty = v.idAnnotationProperty;


-- Need to set dataPath on GenomeBuild




SET FOREIGN_KEY_CHECKS = 1;