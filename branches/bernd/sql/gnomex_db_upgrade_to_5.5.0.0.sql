-- New properties for core facility
alter table gnomex.CoreFacility add acceptOnlineWorkAuth char(1) NOT NULL Default 'Y';
alter table gnomex.CoreFacility add description varchar(10000) NULL;


-- Alter Platform/Application table design for Annotations ** Note that the following two commands should be run separately
-- they will fail if run concurrently:

-- Delete extraneous entries in PlatformApplication caused by incorrect db design.
delete from PlatformApplication
where idPlatformApplication not in (select idPlatformApplication from PropertyPlatformApplication)
and idProperty > -1;

-- Drop PropertyPlatformApplication table (rendered unnecessary by new design)
drop table PropertyPlatformApplication;

