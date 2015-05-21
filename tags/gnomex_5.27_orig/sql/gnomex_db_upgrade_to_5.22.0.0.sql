use gnomex;

-- New property to control adapter trimming on illumina experiment creation.
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly)
	values('choose_adapter_trim_default','N','Indicates if adapter trimming should be chosen by default on illumina experiment creation','N');
	
	
	
ALTER TABLE Sample add groupName VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','Sample_Audit','ALTER TABLE Sample_Audit add groupName VARCHAR(200) NULL');
