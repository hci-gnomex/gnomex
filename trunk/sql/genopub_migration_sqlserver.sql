use GNomEx;

-- Update AppUser
update AppUser set ucscUrl = u.ucscUrl from AppUser au join GenoPubMigrate..gpUser u on au.idAppUser = u.idAppUserGNomEx;

-- Update GenomeBuild
update GenomeBuild 
       set buildDate = gv.buildDate,
           coordURI = gv.coordURI,
           coordVersion = gv.coordVersion,
           coordSource = gv.coordSource,
           coordTestRange = gv.coordTestRange,
           coordAuthority = gv.coordAuthority,
           ucscName = gv.ucscName,
           dataPath = gv.dataPath
       from GenomeBuild gb join GenoPubMigrate..GenomeVersionView gv on gv.idGenomeBuildGNomEx = gb.idGenomeBuild ;

-- Update Organism
update Organism
set  sortOrder = ov.sortOrder,
     binomialName = ov.binomialName,
     NCBITaxID = ov.NCBITaxID
from Organism o join genopub.OrganismView ov on ov.idOrganismGNomEx = o.idOrganism;

-- Add genome versions that are not already in gnomex
INSERT INTO [GNomEx].[dbo].[GenomeBuild]
           ([genomeBuildName]
           ,[idOrganism]
           ,[isActive]
           ,[isLatestBuild]
           ,[idAppUser]
           ,[buildDate]
           ,[coordURI]
           ,[coordVersion]
           ,[coordSource]
           ,[coordTestRange]
           ,[coordAuthority]
           ,[ucscName]
           ,[dataPath]
           ,[das2Name])
     select [name]
           ,[idOrganismGNomEx]
           ,'Y'
           ,'N'
           ,null
           ,[buildDate]
           ,[coordURI]
           ,[coordVersion]
           ,[coordSource]
           ,[coordTestRange]
           ,[coordAuthority]
           ,[ucscName]
           ,[dataPath]
           ,[name]
           from GenoPubMigrate..GenomeVersionView where idGenomeBuildGNomEx is null;
GO

-- Populate GenomeBuildAlias
insert into GNomEx.dbo.GenomeBuildAlias (alias, idGenomeBuild)
select alias, idGenomeBuildGNomEx
from GenoPubMigrate.dbo.gpGenomeVersionAlias a
left join GenoPubMigrate.dbo.GenomeVersionView gv on gv.idGenomeVersion = a.idGenomeVersion
where idGenomeBuildGNomEx is not null;

-- Property
INSERT INTO [GNomEx].[dbo].[Property]
           ([name]
           ,[mageOntologyCode]
           ,[mageOntologyDefinition]
           ,[isActive]
           ,[idAppUser]
           ,[codePropertyType]
           ,[description]
           ,[isRequired],
           forDataTrack)
    select
           [name]
           ,null
           ,null
           ,'N'
           ,NULL
           ,[codePropertyType]
           ,NULL
           ,'N' 
           ,'Y'
           FROM GenoPubMigrate..gpProperty
GO

update GenoPubMigrate..gpProperty set idPropertyGnomEx = p1.idProperty
from GenoPubMigrate..gpProperty p 
  join  GNomEx..Property p1 on p.name = p1.name
  
-- Property Option
INSERT INTO [GNomEx].[dbo].[PropertyOption]
           ([value]
           ,[idProperty]
           ,[sortOrder]
           ,[isActive])
    select  name
           ,[idPropertyGNomEx]
           ,[sortOrder]
           ,[isActive]
    from GenoPubMigrate..PropertyOptionView
GO

update GenoPubMigrate..PropertyOptionView 
set idPropertyOptionGnomEx = p1.idPropertyOption
from GenoPubMigrate..PropertyOptionView   
  join GNomEx..PropertyOption p1  on name = p1.value and p1.idProperty = idPropertyGNomEx
  
  
  
-- Populate Segment
INSERT INTO [GNomEx].[dbo].[Segment]
           ([length]
           ,[name]
           ,[idGenomeBuild]
           ,[sortOrder])
     select [length]
           ,[name]
           ,[idGenomeBuildGNomEx]
           ,[sortOrder] from GenoPubMigrate..SegmentView;




-- Need MEMCOL visibility
insert into GNomEx.dbo.Visibility (codeVisibility, visibility) values('MEMCOL', 'Members and Collaborators');

-- Populate  DataTrack
SET IDENTITY_INSERT GNomEx.dbo.DataTrack ON;
INSERT INTO [GNomEx].[dbo].[DataTrack]
           (idDataTrack
           ,[name]
           ,[description]
           ,[fileName]
           ,[idGenomeBuild]
           ,[codeVisibility]
           ,[idAppUser]
           ,[idLab]
           ,[summary]
           ,[createdBy]
           ,[createDate]
           ,[isLoaded]
           ,[idInstitution]
           ,[dataPath])
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
  FROM [GenoPubMigrate].[dbo].[gpAnnotation]   a
    left join GenoPubMigrate.dbo.GenomeVersionView gv on gv.idGenomeVersion = a.idGenomeVersion
    left join GenoPubMigrate.dbo.gpUser u on a.idUser = u.idUser
    left join GenoPubMigrate.dbo.gpUserGroup ug on ug.idUserGroup = a.idUserGroup
    left join GenoPubMigrate.dbo.gpInstitute i on i.idInstitute = a.idInstitute
GO




-- Populate DataTrackFolder
SET IDENTITY_INSERT GNomEx.dbo.DataTrackFolder ON;
INSERT INTO [GNomEx].[dbo].[DataTrackFolder]
           (idDataTrackFolder
           ,[name]
           ,[description]
           ,[idParentDataTrackFolder]
           ,[idGenomeBuild]
           ,[idLab]
           ,[createdBy]
           ,[createDate])
     select ag.idAnnotationGrouping, 
             ag.name,
               ag.description,
               ag.idParentAnnotationGrouping,
               gv.idGenomeBuildGNomEx,
               ug.idLabGNomEx,
               createdBy,
               createDate
           from GenoPubMigrate.dbo.gpAnnotationGrouping ag
            left join GenoPubMigrate.dbo.gpUserGroup ug on ag.idUserGroup = ug.idUserGroup
            left join GenoPubMigrate.dbo.GenomeVersionView gv on gv.idGenomeVersion = ag.idGenomeVersion;
GO
SET IDENTITY_INSERT GNomEx.dbo.DataTrackFolder OFF;



-- Migrate AnnotationToAnnotationGrouping to DataTrackToDataTrackFolder
insert into GNomEx.dbo.DataTrackToFolder (idDataTrack, idDataTrackFolder)
select idAnnotation, idAnnotationGrouping from GenoPubMigrate.dbo.gpAnnotationToAnnotationGrouping;


-- Populate DataTrackCollaborator
insert into GNomEx.dbo.DataTrackCollaborator (idDataTrack, idAppUser)
select idAnnotation, idAppUserGNomEx
from GenoPubMigrate.dbo.gpAnnotationCollaborator ac
left join GenoPubMigrate.dbo.gpUser u on u.idUser = ac.idUser;



-- Migrate AnnotationProperty to PropertyEntry
INSERT INTO [GNomEx].[dbo].[PropertyEntry]
           ([valueString]
           ,[otherLabel]
           ,[idProperty]
           ,[idDataTrack])
     select 
            value,
            null,
            idPropertyGNomEx,
            idAnnotation
    from GenoPubMigrate.dbo.AnnotationPropertyView;
    
update GenoPubMigrate..AnnotationPropertyView set idPropertyEntryGnomEx = p1.idPropertyEntry
from GenoPubMigrate..AnnotationPropertyView p 
  join  GNomEx..PropertyEntry p1 on p.value = p1.valueString and p1.idProperty = p.idPropertyGNomEx and p1.idDataTrack = p.idAnnotation
    
            
-- Migrate AnnotationPropertyOption to PropertyEntryOption 
INSERT INTO [GNomEx].[dbo].[PropertyEntryOption]
           ([idPropertyEntry]
           ,[idPropertyOption])
     select idPropertyEntryGNomEx,
           idPropertyOptionGNomEx
     from GenoPubMigrate..gpAnnotationPropertyOption pe
     join GenoPubMigrate..AnnotationPropertyView pv on pv.idAnnotationProperty = pe.idAnnotationProperty
     join GenoPubMigrate..PropertyOptionView po on pe.idPropertyOption = po.idPropertyOption
GO

-- Migrate AnnotationPropertyValue to PropertyEntryValue
INSERT INTO [GNomEx].[dbo].[PropertyEntryValue]
           ([idPropertyEntry]
           ,[value])
     select idPropertyEntryGNomEx,
           v.value
     from GenoPubMigrate..gpAnnotationPropertyValue v
     join GenoPubMigrate..AnnotationPropertyView pv on pv.idAnnotationProperty = v.idAnnotationProperty
GO


-- Change the DataTrack number (fileName) from 'Axxxx' to 'DTxxxx'
update GNomEx.dbo.DataTrack set fileName = 'DT' + convert(varchar(10), idDataTrack);
GO

-- Need to set dataPath on GenomeBuild
