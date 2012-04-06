update Sample join SamplePrepMethod on samplePrepMethod.idSamplePrepMethod = Sample.idSamplePrepMethod
  set otherSamplePrepMethod=SamplePrepMethod
  where otherSamplePrepMethod is null and Sample.idSamplePrepMethod is not null;

DROP TABLE IF EXISTS `gnomex`.`SamplePrepMethodRequestCategory`;
DROP TABLE IF EXISTS `gnomex`.`SamplePrepMethodSampleType`;

SET FOREIGN_KEY_CHECKS = 0;
alter table gnomex.RequestCategory drop foreign key FK_RequestCategory_SamplePrepMethod;
alter table gnomex.RequestCategory drop column idSamplePrepMethod;

-- Allow nulls for idGenomeBuild, sortOrder on Segment
alter table gnomex.Segment change column idGenomeBuild idGenomeBuild INT(10) NULL;
alter table gnomex.Segment change column sortOrder sortOrder INT(10) unsigned NULL;

 


