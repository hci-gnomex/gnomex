use gnomex;

-- Remove trimAdapter column from table and audit table
alter table Request Drop column trimAdapter;
call ExecuteIfTableExists('gnomex','Request_Audit','alter table Request_Audit DROP COLUMN trimAdapter');



-- New properties for nano string experiments

INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES ('nano_string_batch_warning', 'Nano string runs in batches of 12 samples. Please note you will be responsible to pay for unused wells.', 'Warning for nano string experiments notifying users they will be responsible for paying for unused wells', 'N', null, null);

INSERT INTO PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
	VALUES ('nano_code_set_cost_warning', 'The pricing listed below includes sample Quality (Qubit and BioAnalyzer) and NanoString analysis.  The pricing does NOT include the cost of code sets and master kits (purchased separately).', 'Warn users that pricing listed for nano string experiments does not include the cost of code sets and master kits.', 'N', null, null);
