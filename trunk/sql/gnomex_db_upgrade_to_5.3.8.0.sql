use gnomex;

SET foreign_key_checks = 0;
--
-- New column for FlowCellChannel
--
alter table Request add column captureLibDesignId VARCHAR(200) NULL;

