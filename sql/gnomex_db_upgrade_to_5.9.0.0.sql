use gnomex;

alter table Step add column sortOrder INT(10) null;

update Step set sortOrder = 1 where codeStepNext like '%QC';
update Step set sortOrder = 2 where codeStepNext like '%LIBPREP';
update Step set sortOrder = 3 where codeStepNext like '%SEQASSEM';
update Step set sortOrder = 4 where codeStepNext like '%SEQPIPE';

update Step set sortOrder = 2 where codeStepNext = 'LABEL';
update Step set sortOrder = 3 where codeStepNext = 'HYB';
update Step set sortOrder = 4 where codeStepNext = 'EXT';


