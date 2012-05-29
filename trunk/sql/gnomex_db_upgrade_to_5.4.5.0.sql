use gnomex;


-- Set CoreFacility name DEFAULT to Microarray and Genomic Analysis   
update CoreFacility set facilityName = 'Microarray and Genomic Analysis' where facilityName = 'DEFAULT';


-- Default all of the request categories to the Microarray and Genomic Analysis core facility
update RequestCategory rc 
set rc.idCoreFacility = cf.idCoreFacility
  join CoreFacility cf on cf.idCoreFacility = rc.idCoreFacility
  where facilityName = 'Microarray and Genomic Analysis'
  and codeRequestCategory not in ('CAPSEQ', 'MITSEQ', 'FRAGANAL', 'CHERRYPICK');

-- Set the idCoreFacility on Request based on the request category's core facility
update Request r 
set r.idCoreFacility = cf.idCoreFacility
  join RequestCategory r on r.codeRequestCategory = rc.codeRequestCategory
  join CoreFacility cf on rc.idCoreFacility = cf.idCoreFacility;

-- new isControl for Sample
alter table Sample add column isControl char(1) NULL;