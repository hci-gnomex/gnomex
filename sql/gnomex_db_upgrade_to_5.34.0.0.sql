use gnomex;

alter table Request add column archived char(1) NULL;

CALL ExecuteIfTableExists('gnomex','Request_Audit','ALTER TABLE Request_Audit ADD COLUMN archived CHAR(1) NULL');