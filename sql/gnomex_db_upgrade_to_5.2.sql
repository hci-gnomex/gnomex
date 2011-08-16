-- Add FlowCellChannel columns for read1ClustersPassedFilterM, read2ClustersPassedFilterM, q30Gb
alter table gnomex.FlowCellChannel add column read1ClustersPassedFilterM INT(10) NULL;
alter table gnomex.FlowCellChannel add column read2ClustersPassedFilterM INT(10) NULL;


 