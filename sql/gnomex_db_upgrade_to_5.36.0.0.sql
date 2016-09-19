use gnomex;

alter table Sample add column idLibPrepPerformedBy int(10) null, add foreign key FK_Sample_AppUser(idLibPrepPerformedBy) REFERENCES AppUser(idAppUser);

CALL ExecuteIfTableExists('gnomex','Sample_Audit','alter table Sample_Audit add column idLibPrepPerformedBy int(10) null');


-- new columns on request to keep track of new sample purification fields.
alter table Request add column reagent varchar(50) null;
alter table Request add column elutionBuffer varchar(50) null;
alter table Request add column usedDnase char(1) null;
alter table Request add column usedRnase char(1) null;
alter table Request add column keepSamples char(1) null;


CALL ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit add column reagent varchar(50) null');
CALL ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit add column elutionBuffer varchar(50) null');
CALL ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit add column usedDnase char(1) null');
CALL ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit add column usedRnase char(1) null');
CALL ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit add column keepSamples char(1) null');

