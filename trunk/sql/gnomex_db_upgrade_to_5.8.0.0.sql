use gnomex;

-- New create date for FileExperiment
alter table ExperimentFile add createDate Date;

-- New create date for AnalysisFile
alter table AnalysisFile add createDate Date;
