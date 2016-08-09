use gnomex;

alter table Sample add column idLibPrepPerformedBy int(10) null, add foreign key FK_Sample_AppUser(idLibPrepPerformedBy) REFERENCES AppUser(idAppUser);

CALL ExecuteIfTableExists('gnomex','Sample_Audit','alter table Sample_Audit add column idLibPrepPerformedBy int(10) null');