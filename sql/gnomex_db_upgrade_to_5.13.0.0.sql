use gnomex;

-- Add isCustom
alter table NumberSequencingCyclesAllowed add isCustom CHAR(1) NOT NULL DEFAULT 'N';

-- Remove unused columns
alter table NumberSequencingCyclesAllowed drop column notes;

-- Members is now All Lab Members
update Visibility set visibility='All Lab Members' where codeVisibility='MEM';

-- Add quote columns to Request
alter table Request add materialQuoteNumber VARCHAR(50) NULL; 
alter table Request add quoteReceivedDate DATETIME NULL; 
alter table Request add uuid VARCHAR(36) NULL; 

-- Add public data notice property to be shown on the register new user screen.
insert into PropertyDictionary (propertyName, propertyDescription,forServerOnly, idCoreFacility, codeRequestCategory)
values('public_data_notice', 
'New account note informing users that no account is necessary for public data', 
'N', null, null); 

-- Add meanLibSizeActual and idOligoBarcodeB
alter table Sample add meanLibSizeActual INT(10) NULL;

alter table Sample add idOligoBarcodeB INT(10) NULL; 

-- Add base insert size default property.
insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
values('base_insert_size', '0', 'Base size to subtract from actual mean lib size', 'N', null, null); 

-- Add isIndexGroupB
alter table OligoBarcodeSchemeAllowed add isIndexGroupB CHAR(1) NOT NULL DEFAULT 'N';

-- Add columns for Printable Request Form
alter table Application add coreSteps varchar(5000);
alter table Application add coreStepsNoLibPrep varchar(5000);

-- Add meanLibSizeActual column to Request (needed?)
alter table Request add meanLibSizeActual INT(10) NULL;
