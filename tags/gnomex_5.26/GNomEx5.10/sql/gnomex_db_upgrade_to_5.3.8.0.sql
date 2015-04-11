use gnomex;

--
-- New column for FlowCellChannel
--
alter table Request add column captureLibDesignId VARCHAR(200) NULL;

