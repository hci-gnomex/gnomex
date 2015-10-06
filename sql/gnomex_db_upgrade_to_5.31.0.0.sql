use gnomex;

-- Remove both foreign keys that reference DNAPrepType and RNAPrepType tables from the Request Table.
alter table Request drop
       FOREIGN KEY FK_Request_DNAPrepType;
	   
alter table Request drop
		FOREIGN KEY FK_Request_RNAPrepType;
       
-- Remove dna and rna code prep type columns from Request
ALTER TABLE Request DROP COLUMN codeDNAPrepType;
ALTER TABLE Request DROP COLUMN codeRNAPrepType;
