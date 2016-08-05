use gnomex;

alter table Sample add column libPrepPerformedByID int(10) null;

CALL ExecuteIfTableExists('gnomex','Sample_Audit','alter table Sample_Audit add column libPrepPerformedByID int(10) null');