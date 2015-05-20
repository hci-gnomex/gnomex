use GNomEx;

select au.idAppUser, au.lastName, au.firstName, u.idUser, u.lastName, u.firstName from  AppUser au join GenoPubMigrate..gpUser u on au.idAppUser = u.idAppUserGNomEx 
select l.lastName, l.firstName, ug.name from Lab l join GenoPubMigrate..gpUserGroup ug on ug.idLabGNomEx = l.idLab;
select gv.name, gb.das2Name, gb.genomeBuildName from GenoPubMigrate..GenomeVersionView gv join gnomex..GenomeBuild gb on gb.idGenomeBuild = gv.idGenomeBuildGNomEx


-- Create views that used to be in GenoPubMigrate
select idProperty, name, isActive, codePropertyType, idUser, sortOrder, idPropertyGNomEx
into GNomEx.dbo.gpProperty
from GenopubMigrate..gpProperty;
GO

create view [dbo].[AnnotationPropertyView] as
SELECT     ap.idAnnotationProperty, ap.name, ap.value, ap.idProperty, ap.idAnnotation, p.idPropertyGNomEx, ap.idPropertyEntryGNomEx
FROM         GNomEx.dbo.AnnotationProperty AS ap LEFT  JOIN
                      GNomEx.dbo.gpProperty AS p ON p.idProperty = ap.idProperty;               
GO

select idPropertyOption, name, isActive, idProperty, sortOrder, idPropertyOptionGNomEx
into GNomEx.dbo.gpPropertyOption
from GenopubMigrate..gpPropertyOption;
GO;




-- Update AppUser
update AppUser set ucscUrl = u.ucscUrl from AppUser au join GenoPubMigrate..gpUser u on au.idAppUser = u.idAppUserGNomEx where au.ucscURL is null or au.ucscURL = '';

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
from Organism o join GenoPubMigrate..OrganismView ov on ov.idOrganismGNomEx = o.idOrganism;

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
  FROM Annotation   a
    left join GenoPubMigrate.dbo.GenomeVersionView gv on gv.idGenomeVersion = a.idGenomeVersion
    left join GenoPubMigrate.dbo.gpUser u on a.idUser = u.idUser
    left join GenoPubMigrate.dbo.gpUserGroup ug on ug.idUserGroup = a.idUserGroup
    left join GenoPubMigrate.dbo.gpInstitute i on i.idInstitute = a.idInstitute
GO
SET IDENTITY_INSERT GNomEx.dbo.DataTrack OFF;




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
           from AnnotationGrouping ag
            left join GenoPubMigrate.dbo.gpUserGroup ug on ag.idUserGroup = ug.idUserGroup
            left join GenoPubMigrate.dbo.GenomeVersionView gv on gv.idGenomeVersion = ag.idGenomeVersion;
GO
SET IDENTITY_INSERT GNomEx.dbo.DataTrackFolder OFF;



-- Migrate AnnotationToAnnotationGrouping to DataTrackToDataTrackFolder
insert into GNomEx.dbo.DataTrackToFolder (idDataTrack, idDataTrackFolder)
select idAnnotation, idAnnotationGrouping from AnnotationToAnnotationGrouping;


-- Populate DataTrackCollaborator
insert into GNomEx.dbo.DataTrackCollaborator (idDataTrack, idAppUser)
select idAnnotation, idAppUserGNomEx
from AnnotationCollaborator ac
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
    from AnnotationPropertyView;
    
update AnnotationPropertyView set idPropertyEntryGnomEx = p1.idPropertyEntry
from AnnotationPropertyView p 
  join  GNomEx..PropertyEntry p1 on p.value = p1.valueString and p1.idProperty = p.idPropertyGNomEx and p1.idDataTrack = p.idAnnotation
    
            
-- Migrate AnnotationPropertyOption to PropertyEntryOption 
INSERT INTO [GNomEx].[dbo].[PropertyEntryOption]
           ([idPropertyEntry]
           ,[idPropertyOption])
     select idPropertyEntryGNomEx,
           idPropertyOptionGNomEx
     from AnnotationPropertyOption pe
     join AnnotationPropertyView pv on pv.idAnnotationProperty = pe.idAnnotationProperty
     join GenoPubMigrate..PropertyOptionView po on pe.idPropertyOption = po.idPropertyOption
GO

-- Migrate AnnotationPropertyValue to PropertyEntryValue
INSERT INTO [GNomEx].[dbo].[PropertyEntryValue]
           ([idPropertyEntry]
           ,[value])
     select idPropertyEntryGNomEx,
           v.value
     from AnnotationPropertyValue v
     join AnnotationPropertyView pv on pv.idAnnotationProperty = v.idAnnotationProperty
GO


-- Change the DataTrack number (fileName) from 'Axxxx' to 'DTxxxx'
update GNomEx.dbo.DataTrack set fileName = 'DT' + convert(varchar(10), idDataTrack);
GO

-- Need to set dataPath on GenomeBuild


-- Set the new seed value for data track and data track folder
DBCC CHECKIDENT('DataTrackFolder', RESEED, 5999);
DBCC CHECKIDENT('DataTrackFolder', RESEED, 7999);

