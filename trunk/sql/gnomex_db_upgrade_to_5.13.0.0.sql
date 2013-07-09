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

-- Add public data notice property to be shown on the register new user screen.
insert into PropertyDictionary (propertyName, propertyDescription,forServerOnly, idCoreFacility, codeRequestCategory)
values('public_data_notice', 
'New account note informing users that no account is necessary for public data', 
'N', null, null); 


-- Add meanInsertSizeActual and idOligoBarcodeB
alter table Sample add meanInsertSizeActual INT(10) NULL;

alter table Sample add idOligoBarcodeB INT(10) NULL; 

-- Add isIndexGroupB
alter table OligoBarcodeSchemeAllowed add isIndexGroupB CHAR(1) NOT NULL DEFAULT 'N';

-- Add columns for Printable Request Form
alter table Application add corePrepSteps varchar(5000);
alter table Application add labPrepSteps varchar(5000);

