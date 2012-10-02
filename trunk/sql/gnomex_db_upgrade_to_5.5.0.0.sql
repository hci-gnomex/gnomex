-- New properties for core facility
alter table gnomex.CoreFacility add acceptOnlineWorkAuth char(1) NOT NULL Default 'Y';
alter table gnomex.CoreFacility add description varchar(10000) NULL;
