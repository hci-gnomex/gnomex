alter table gnomex.FlowCellChannel change column idFlowCell idFlowCell INT(10) NULL;
alter table gnomex.SequenceLane change column idFlowCellChannel idFlowCellChannel INT(10) NULL;

alter table gnomex.SequenceLane add column readCount INT(10) null;
alter table gnomex.SequenceLane add column pipelineVersion varchar (10) null;
