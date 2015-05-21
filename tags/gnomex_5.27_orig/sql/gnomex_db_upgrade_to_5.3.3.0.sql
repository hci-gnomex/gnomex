-- Add sendUploadAlert column to track labs users for which users want to receive upload alerts
alter table gnomex.LabUser add column sendUploadAlert char (1) null;
alter table gnomex.LabManager add column sendUploadAlert char (1) null;
alter table gnomex.LabCollaborator add column sendUploadAlert char (1) null;
