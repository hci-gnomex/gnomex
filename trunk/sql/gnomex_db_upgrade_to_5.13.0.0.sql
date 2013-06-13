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