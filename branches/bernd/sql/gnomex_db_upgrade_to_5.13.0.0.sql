use gnomex;

-- Add isCustom
alter table NumberSequencingCyclesAllowed add isCustom CHAR(1) NOT NULL DEFAULT 'N';

-- Remove unused columns
alter table NumberSequencingCyclesAllowed drop column notes;

-- Members is now All Lab Members
update Visibility set visibility='All Lab Members' where codeVisibility='MEM';
