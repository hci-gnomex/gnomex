use gnomex;

alter table Application add column avgInsertSizeFrom int(10) NULL;
alter table Application add column avgInsertSizeTo int(10) NULL;
alter table Application add column hasCaptureLibDesign char(1) NULL;

update Application set hasCaptureLibDesign = 'Y' 
where codeApplication = 'EXCAPSSC' or codeApplication = 'TDNASEQ'; 

update Application set avgInsertSizeFrom = 100, avgInsertSizeTo = 600
where codeApplication = 'DNASEQ' or codeApplication = 'MONNUCSEQ';