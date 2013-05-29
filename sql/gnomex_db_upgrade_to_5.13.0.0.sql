use gnomex;

-- Add isCustom
alter table NumberSequencingCyclesAllowed add isCustom CHAR(1) NOT NULL DEFAULT 'N';
