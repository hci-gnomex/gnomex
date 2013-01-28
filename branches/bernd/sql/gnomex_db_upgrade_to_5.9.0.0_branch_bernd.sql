use gnomex;

-- New property for hostname of MetrixServer
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly) 
  values('metrix_server_host','localhost','Hostname or IP on which the Illumina statistics Metrix Server is running.','Y');
  
-- New property for port of MetrixServer
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly)
  values('metrix_server_port','12345','Port (>1024) on which the Illumina statistics Metrix Server is running.','Y');
  
-- Add RequestCategory column for refrainFromAutoDelete (Adjust required amount of chars)
ALTER TABLE RequestCategory ADD refrainFromAutoDelete VARCHAR(50);